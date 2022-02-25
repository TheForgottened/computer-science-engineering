package pt.isec.metapd.concurrentserver.runnables;

import pt.isec.metapd.communication.*;
import pt.isec.metapd.files.FileUtility;
import pt.isec.metapd.repository.ServerRepository;
import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.resources.RequestType;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import static pt.isec.metapd.Server.LOGGER;

public class TcpHandler implements Runnable {
    private final AtomicBoolean mustStop;

    private final ServerRepository serverRepository;

    private final ConcurrentMap<ObjectOutputStream, String> connectedClients;

    private final ServerSocket serverSocket;

    private final DatagramSocket datagramSocket;
    private final String lbAddress;
    private final int lbPort;

    public TcpHandler(
            AtomicBoolean mustStop,
            ConcurrentMap<ObjectOutputStream, String> connectedClients,
            ServerSocket serverSocket,
            DatagramSocket datagramSocket,
            String lbAddress,
            int lbPort,
            ServerRepository serverRepository
    ) {
        this.mustStop = mustStop;
        this.connectedClients = connectedClients;
        this.serverSocket = serverSocket;
        this.datagramSocket = datagramSocket;
        this.lbAddress = lbAddress;
        this.lbPort = lbPort;
        this.serverRepository = serverRepository;
    }

    @Override
    public void run() {
        try {
            serverSocket.setSoTimeout(MetaPDConstants.MAX_TIMEOUT * 1000);
            LOGGER.log(Level.INFO, "Server socket successfully opened!");

            while (!mustStop.get()) {
                try {
                    Socket toClientSocket = serverSocket.accept();
                    toClientSocket.setSoTimeout(MetaPDConstants.MAX_TIMEOUT * 1000);

                    Thread thread = new Thread(new ClientRequestHandler(
                            mustStop,
                            connectedClients,
                            toClientSocket,
                            datagramSocket,
                            lbAddress,
                            lbPort
                    ));
                    thread.start();
                } catch (SocketTimeoutException e) { /* this is needed so the server can close gracefully */ }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to open server socket!\n{0}", e.toString());
            mustStop.set(true);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing sockets.\n{0}", e.toString());
            }
        }
    }

    private class ClientRequestHandler implements Runnable {
        private final AtomicBoolean mustStop;

        private final ConcurrentMap<ObjectOutputStream, String> connectedClients;

        private final Socket socket;

        private final DatagramSocket datagramSocket;
        private final String lbAddress;
        private final int lbPort;

        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        private Lock isDownloading = new ReentrantLock();

        private ClientRequestHandler(
                AtomicBoolean mustStop,
                ConcurrentMap<ObjectOutputStream, String> connectedClients,
                Socket socket,
                DatagramSocket datagramSocket,
                String lbAddress,
                int lbPort
        ) {
            this.mustStop = mustStop;
            this.connectedClients = connectedClients;
            this.socket = socket;
            this.datagramSocket = datagramSocket;
            this.lbAddress = lbAddress;
            this.lbPort = lbPort;
        }

        @Override
        public void run() {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                socket.setSoTimeout(MetaPDConstants.MAX_TIMEOUT);

                connectedClients.put(oos, "");

                while (!mustStop.get()) {
                    try {
                        TinyRequest tinyRequest = (TinyRequest) ois.readObject();

                        if (tinyRequest == null) continue;

                        switch (tinyRequest.requestType()) {
                            case REGISTER_ACCOUNT -> registerAccount(tinyRequest);
                            case LOGIN_ACCOUNT -> loginAccount(tinyRequest);
                            case LOGOUT_ACCOUNT -> logoutAccount();
                            case CHANGE_NAME -> changeName(tinyRequest);
                            case CHANGE_PASSWORD -> changePassword(tinyRequest);
                            case SEND_GROUP_MESSAGE -> sendGroupMessage(tinyRequest);
                            case SEND_USER_MESSAGE -> sendUserMessage(tinyRequest);
                            case DELETE_MESSAGE -> deleteMessage(tinyRequest);
                            case DELETE_CONTACT -> deleteContact(tinyRequest);
                            case SEND_FRIEND_REQUEST -> sendFriendRequest(tinyRequest);
                            case SEND_GROUP_REQUEST -> sendGroupRequest(tinyRequest);
                            case ANSWER_FRIEND_REQUEST -> answerFriendRequest(tinyRequest);
                            case ANSWER_GROUP_REQUEST -> answerGroupRequest(tinyRequest);
                            case CREATE_GROUP -> createGroup(tinyRequest);
                            case EXIT_GROUP -> exitGroup(tinyRequest);
                            case EDIT_GROUP_NAME -> editGroupName(tinyRequest);
                            case DELETE_GROUP -> deleteGroup(tinyRequest);
                            case REMOVE_MEMBER_FROM_GROUP -> removeMemberFromGroup(tinyRequest);

                            case GET_CONTACT_MESSAGES -> getContactMessages();
                            case GET_GROUP_MESSAGES -> getGroupMessages();
                            case GET_CONTACTS -> getContacts();
                            case GET_ALL_GROUP_LIST -> getAllGroupList();
                            case GET_MY_GROUP_LIST -> getMyGroupList();
                            case GET_USERS -> getUsers();
                            case GET_FRIEND_REQUESTS -> getFriendRequests();
                            case GET_GROUP_REQUESTS -> getGroupRequests();
                            case GET_FILE -> getFile(tinyRequest);
                            case GET_NAME -> getName();

                            default -> LOGGER.log(
                                    Level.WARNING,
                                    "Invalid TCP request " + tinyRequest.requestType() +
                                    " from " + socket.getInetAddress().getHostAddress() + "."
                            );
                        }
                    } catch (SocketTimeoutException e) {
                        /* needed so the server can close gracefully */
                    } catch (SocketException e) {
                        if (!connectedClients.get(oos).isBlank()) {
                            serverRepository.setStatus(connectedClients.get(oos), false);
                            sendDbUpdateToAllUsers();
                        }
                        connectedClients.remove(oos);
                        socket.close();
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        private void getName() {
            try {
                String name = serverRepository.getNameForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_NAME, name));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void removeMemberFromGroup(TinyRequest tinyRequest) {
            TinyEditGroupRequest tinyEditGroupRequest = (TinyEditGroupRequest) tinyRequest.object();

            try {
                if (serverRepository.tryToRemoveUserFromGroup(
                        connectedClients.get(oos),
                        tinyEditGroupRequest.name(),
                        tinyEditGroupRequest.id()
                )) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void deleteGroup(TinyRequest tinyRequest) {
            int groupId = (Integer) tinyRequest.object();

            try {
                if (serverRepository.tryToDeleteGroup(groupId, connectedClients.get(oos))) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void editGroupName(TinyRequest tinyRequest) {
            TinyEditGroupRequest tinyEditGroupRequest = (TinyEditGroupRequest) tinyRequest.object();

            try {
                if (serverRepository.tryToEditGroupName(
                        connectedClients.get(oos),
                        tinyEditGroupRequest.id(),
                        tinyEditGroupRequest.name()
                )) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void exitGroup(TinyRequest tinyRequest) {
            int groupId = (Integer) tinyRequest.object();

            try {
                if (serverRepository.tryToExitGroup(connectedClients.get(oos), groupId)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void createGroup(TinyRequest tinyRequest) {
            String groupName = (String) tinyRequest.object();

            try {
                if (serverRepository.tryToCreateGroup(groupName, connectedClients.get(oos))) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void answerGroupRequest(TinyRequest tinyRequest) {
            TinyAnswerGroupRequest tinyAnswerGroupRequest = (TinyAnswerGroupRequest) tinyRequest.object();

            try {
                if (serverRepository.tryToAnswerGroupRequest(
                        connectedClients.get(oos),
                        tinyAnswerGroupRequest.requestSenderUsername(),
                        tinyAnswerGroupRequest.groupId(),
                        tinyAnswerGroupRequest.answer()
                )) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void answerFriendRequest(TinyRequest tinyRequest) {
            TinyAnswerContactRequest tinyAnswerContactRequest = (TinyAnswerContactRequest) tinyRequest.object();

            try {
                if (serverRepository.tryToAnswerFriendRequest(
                        connectedClients.get(oos),
                        tinyAnswerContactRequest.requestSenderUsername(),
                        tinyAnswerContactRequest.answer()
                )) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    List<String> affectedUsers = new LinkedList<>();
                    affectedUsers.add(tinyAnswerContactRequest.requestSenderUsername());
                    affectedUsers.add(connectedClients.get(oos));

                    sendDbUpdate(affectedUsers);
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getMyGroupList() {
            try {
                List<TinyGroup> groups = serverRepository.getGroupsForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_MY_GROUP_LIST, groups));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getAllGroupList() {
            try {
                List<TinyGroup> groups = serverRepository.getAllGroups();

                oos.writeObject(new TinyRequest(RequestType.GET_ALL_GROUP_LIST, groups));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getFriendRequests() {
            try {
                List<TinyUser> allUsers = serverRepository
                        .getFriendRequestsForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_FRIEND_REQUESTS, allUsers));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getGroupRequests() {
            try {
                List<TinyGroupRequest> allGroups = serverRepository
                        .getGroupRequestsForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_GROUP_REQUESTS, allGroups));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getUsers() {
            try {
                List<TinyUser> allUsers = serverRepository
                        .getAllUsers();

                oos.writeObject(new TinyRequest(RequestType.GET_USERS, allUsers));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void deleteContact(TinyRequest tinyRequest) {
            String contactUsername = (String) tinyRequest.object();

            try {
                if (serverRepository.tryToDeleteContact(connectedClients.get(oos), contactUsername)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    List<String> affectedUsers = new LinkedList<>();
                    affectedUsers.add(connectedClients.get(oos));
                    affectedUsers.add(contactUsername);

                    sendDbUpdate(affectedUsers);
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void sendGroupRequest(TinyRequest tinyRequest) {
            int groupId = (Integer) tinyRequest.object();

            try {
                if (serverRepository.tryToSendGroupRequest(connectedClients.get(oos), groupId)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();


                    List<String> affectedUsers = new LinkedList<>();
                    affectedUsers.add(serverRepository.getGroupOwnerUsername(groupId));

                    sendDbUpdate(affectedUsers);
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void sendFriendRequest(TinyRequest tinyRequest) {
            String receiverUsername = (String) tinyRequest.object();

            try {
                if (serverRepository.tryToSendFriendRequest(connectedClients.get(oos), receiverUsername)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    List<String> affectedUsers = new LinkedList<>();
                    affectedUsers.add(receiverUsername);

                    sendDbUpdate(affectedUsers);
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getFile(TinyRequest tinyRequest) {
            int messageId = (Integer) tinyRequest.object();

            try {
                String fileName = serverRepository.getFileNameForMessageId(messageId);

                File fileToSend = new File(FileUtility.LOCAL_FILE_PATH + fileName);

                FileUtility.uploadFile(fileToSend, oos);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void deleteMessage(TinyRequest tinyRequest) {
            int messageId = (Integer) tinyRequest.object();

            try {
                List<String> affectedUsers = serverRepository.getUsernamesAssociatedWithMessage(messageId);
                String fileName = serverRepository.getFileNameForMessageId(messageId);

                if (serverRepository.tryToDeleteMessage(connectedClients.get(oos), messageId)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    affectedUsers.add(connectedClients.get(oos));
                    sendDbUpdate(affectedUsers);

                    if (fileName != null && !fileName.isBlank()) FileUtility.deleteIfExists(new File(
                            FileUtility.LOCAL_FILE_PATH + fileName
                    ));
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void sendUserMessage(TinyRequest tinyRequest) {
            TinyMessageSent tinyMessageSent = (TinyMessageSent) tinyRequest.object();

            try {
                if (serverRepository.sendMessageToUser(tinyMessageSent, connectedClients.get(oos))){
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    if (tinyMessageSent.fileName() != null && !tinyMessageSent.fileName().isBlank()) {
                        File fileFromClient = new File(FileUtility.LOCAL_FILE_PATH + FileUtility.getFileName(
                                serverRepository.getLastMessageForUsername(connectedClients.get(oos)),
                                tinyMessageSent.fileName()
                        ));

                        System.out.println(fileFromClient.isFile() + FileUtility.LOCAL_FILE_PATH + FileUtility.getFileName(
                                serverRepository.getLastMessageForUsername(connectedClients.get(oos)),
                                tinyMessageSent.fileName()));
                        FileUtility.downloadFile(fileFromClient, ois);

                        oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                        oos.flush();
                    }

                    List<String> affectedUsers = new LinkedList<>();
                    affectedUsers.add(tinyMessageSent.receiver());
                    affectedUsers.add(connectedClients.get(oos));

                    sendDbUpdate(affectedUsers);
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void sendGroupMessage(TinyRequest tinyRequest) {
            TinyMessageSent tinyMessageSent = (TinyMessageSent) tinyRequest.object();

            try {
                if (serverRepository.sendMessageToGroup(tinyMessageSent, connectedClients.get(oos))){
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    if (tinyMessageSent.fileName() != null && !tinyMessageSent.fileName().isBlank()) {
                        File fileFromClient = new File(FileUtility.LOCAL_FILE_PATH + FileUtility.getFileName(
                                serverRepository.getLastMessageForUsername(connectedClients.get(oos)),
                                tinyMessageSent.fileName()
                        ));

                        FileUtility.downloadFile(fileFromClient, ois);

                        oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                        oos.flush();
                    }

                    sendDbUpdate(serverRepository.getGroupUsers(Integer.parseInt(tinyMessageSent.receiver())));
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getContacts() {
            try {
                List<TinyUser> contacts = serverRepository
                        .getContactsForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_CONTACTS, contacts));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void registerAccount(TinyRequest tinyRequest) {
            TinyRegisterCredentials tinyRegisterCredentials = (TinyRegisterCredentials) tinyRequest.object();

            try {
                if (serverRepository.tryToRegister(
                        tinyRegisterCredentials.username(),
                        tinyRegisterCredentials.name(),
                        tinyRegisterCredentials.md5Password()
                )) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void loginAccount(TinyRequest tinyRequest) {
            TinyCredentials tinyCredentials = (TinyCredentials) tinyRequest.object();

            try {
                if (serverRepository.tryToLogin(tinyCredentials.username(), tinyCredentials.md5Password())) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();
                    connectedClients.put(oos, tinyCredentials.username());

                    List<String> affectedUsers = new LinkedList<>();

                    for (TinyUser tinyUser : serverRepository.getAllUsers()) {
                        if (tinyUser.username().equals(connectedClients.get(oos))) continue;

                        affectedUsers.add(tinyUser.username());
                    }

                    sendDbUpdate(affectedUsers);
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void logoutAccount() {
            String username = connectedClients.get(oos);

            try {
                serverRepository.setStatus(username, false);
                connectedClients.put(oos, "");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            }
        }

        private void changeName(TinyRequest tinyRequest) {
            String newName = (String) tinyRequest.object();

            try {
                if (serverRepository.tryToChangeName(connectedClients.get(oos), newName)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();

                    sendDbUpdateToAllUsers();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void changePassword(TinyRequest tinyRequest) {
            String newMd5Password = (String) tinyRequest.object();

            try {
                if (serverRepository.tryToChangePassword(connectedClients.get(oos), newMd5Password)) {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_SUCCESSFUL, null));
                    oos.flush();
                } else {
                    oos.writeObject(new TinyRequest(RequestType.OPERATION_FAILED, null));
                    oos.flush();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getContactMessages() {
            try {
                List<TinyMessageReceived> tinyMessageReceivedList = serverRepository
                        .getContactMessagesForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_CONTACT_MESSAGES, tinyMessageReceivedList));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void getGroupMessages() {
            try {
                List<TinyMessageReceived> tinyMessageReceivedList = serverRepository
                        .getGroupMessagesForUser(connectedClients.get(oos));

                oos.writeObject(new TinyRequest(RequestType.GET_GROUP_MESSAGES, tinyMessageReceivedList));
                oos.flush();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error accessing database.\n{0}", e.toString());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unable to write to TCP socket.\n{0}", e.toString());
            }
        }

        private void sendDbUpdate(List<String> affectedUsers) {
            try (
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
                    ) {
                objectOutputStream.writeObject(new TinyRequest(RequestType.DB_UPDATE, affectedUsers));
                objectOutputStream.flush();

                DatagramPacket datagramPacket = new DatagramPacket(
                        byteArrayOutputStream.toByteArray(),
                        byteArrayOutputStream.size(),
                        InetAddress.getByName(lbAddress),
                        lbPort
                );

                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Unable to notify clients.\n{0}", e.toString());
            }
        }

        private void sendDbUpdateToAllUsers() {
            try (
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
            ) {
                List<TinyUser> allTinyUsers = serverRepository.getAllUsers();
                List<String> allUsers = new LinkedList<>();

                for (TinyUser tinyUser : allTinyUsers) allUsers.add(tinyUser.username());

                objectOutputStream.writeObject(new TinyRequest(RequestType.DB_UPDATE, allUsers));
                objectOutputStream.flush();

                DatagramPacket datagramPacket = new DatagramPacket(
                        byteArrayOutputStream.toByteArray(),
                        byteArrayOutputStream.size(),
                        InetAddress.getByName(lbAddress),
                        lbPort
                );

                datagramSocket.send(datagramPacket);
            } catch (IOException | SQLException e) {
                LOGGER.log(Level.SEVERE, "Unable to notify clients.\n{0}", e.toString());
            }
        }
    }
}
