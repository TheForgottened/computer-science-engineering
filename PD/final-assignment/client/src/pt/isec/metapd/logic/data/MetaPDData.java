package pt.isec.metapd.logic.data;

import pt.isec.metapd.communication.*;
import pt.isec.metapd.communication.TinyIP;
import pt.isec.metapd.communication.TinyMessageReceived;
import pt.isec.metapd.communication.TinyRequest;
import pt.isec.metapd.communication.TinyUser;
import pt.isec.metapd.logic.MetaPdObservable;
import pt.isec.metapd.logic.data.runnables.TcpHandler;
import pt.isec.metapd.logic.files.FileUtility;
import pt.isec.metapd.resources.MetaPDConstants;
import pt.isec.metapd.resources.RequestType;

import java.util.*;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MetaPDData {
    private String username;
    private String md5Password;
    private String name;
    private boolean isLoggedIn;

    private final AtomicBoolean operationSuccessful = new AtomicBoolean(false);
    private List<TinyUser> contactList; // username - alias - estado
    private List<TinyUser> systemUserList; // username - alias - estado
    private List<TinyUser> friendshipRequestList; // username - alias - estado
    private List<TinyMessageReceived> messageGroupList; // sender - text - filename - date
    private List<TinyMessageReceived> messageContactList; // sender - text - filename - date
    //private ArrayList<String> notificationList;
    private List<TinyGroup> groupList; // nome - idgrupo - membros
    private List<TinyGroup> myGroupsList; // nome - idgrupo - membros
    private List<TinyGroupRequest> groupRequestList;

    private final AtomicBoolean mustStop;
    private final String lbAddress;
    private final int lbPort;

    private final AtomicBoolean isDataReady = new AtomicBoolean(false);
    private Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    private final Thread tcpHandlerThread;

    private MetaPdObservable metaPdObservable = null;

    public MetaPDData(AtomicBoolean mustStop, String lbAddress, int lbPort) {
        this.mustStop = mustStop;
        this.lbAddress = lbAddress;
        this.lbPort = lbPort;

        connectToServer();

        tcpHandlerThread = new Thread(new TcpHandler(mustStop, isDataReady, this));
        tcpHandlerThread.start();
    }

    public void setObservable(MetaPdObservable metaPdObservable) { this.metaPdObservable = metaPdObservable; }

    public void connectToNewServer() {
        connectToServer();

        //send to server
        if (!isLoggedIn) return;

        Thread thread = new Thread(() -> {
            try {
                oos.writeObject(new TinyRequest(
                        RequestType.LOGIN_ACCOUNT,
                        new TinyCredentials(username, md5Password)
                ));
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!isDataReady.get()) {}
            isDataReady.set(false);

            if (operationSuccessful.get()) {
                setLoggedIn(true);
                return;
            }

            setLoggedIn(false);
        });
        thread.start();
    }

    public void connectToServer() {
        while (true) {
            TinyIP tinyIP = getValidServer(lbAddress, lbPort);

            if (tinyIP == null) continue;
            try {
                this.socket = new Socket(InetAddress.getByName(tinyIP.address()), tinyIP.port());

                socket.setSoTimeout(1000);
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                continue;
            }

            break;
        }
    }

    public TinyIP getValidServer(String lbAddress, int lbPort) {
        try (
                DatagramSocket datagramSocket = new DatagramSocket();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
                ) {
            datagramSocket.setSoTimeout(MetaPDConstants.MAX_TIMEOUT * 1000);

            objectOutputStream.writeObject(new TinyRequest(RequestType.GET_VALID_SERVER, ""));
            objectOutputStream.flush();

            DatagramPacket datagramPacket = new DatagramPacket(
                    byteArrayOutputStream.toByteArray(),
                    byteArrayOutputStream.size(),
                    InetAddress.getByName(lbAddress),
                    lbPort
            );
            datagramSocket.send(datagramPacket);

            datagramPacket = new DatagramPacket(
                    new byte[MetaPDConstants.MAX_PACKET_SIZE],
                    MetaPDConstants.MAX_PACKET_SIZE
            );
            datagramSocket.receive(datagramPacket);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    datagramPacket.getData(),
                    0,
                    datagramPacket.getLength()
            );
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return (TinyIP) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TinyUser> getContactList() {
        return contactList;
    }

    public void setContactList(List<TinyUser> contactList) {
        this.contactList = contactList;
    }

    public List<TinyUser> getSystemUserList() {
        return systemUserList;
    }

    public void setSystemUserList(List<TinyUser> systemUserList) {
        this.systemUserList = systemUserList;
    }

    public void setMyGroupsLists(List<TinyGroup> myGroupsList) {
        this.myGroupsList = myGroupsList;
    }

    public List<TinyGroup> getMyGroupsList() {
        return myGroupsList;
    }

    public List<TinyUser> getFriendshipRequestList() {
        return friendshipRequestList;
    }

    public void setFriendshipRequestList(List<TinyUser> friendshipRequestList) {
        this.friendshipRequestList = friendshipRequestList;
    }

    public List<TinyMessageReceived> getMessageGroupList() {
        return messageGroupList;
    }

    public void setMessageGroupList(List<TinyMessageReceived> messageGroupList) {
        this.messageGroupList = messageGroupList;
    }

    public List<TinyMessageReceived> getMessageContactList() {
        return messageContactList;
    }

    public void setMessageContactList(List<TinyMessageReceived> messageContactList) {
        this.messageContactList = messageContactList;
    }

    public void setGroupList(List<TinyGroup> groupList) {
        this.groupList = groupList;
    }

    public void setGroupRequestList(List<TinyGroupRequest> groupRequestList) {
        this.groupRequestList = groupRequestList;
    }

    public List<TinyGroupRequest> getGroupRequestList() {
        return groupRequestList;
    }

    public void setOperationSuccessful(boolean b) {
        operationSuccessful.set(b);
    }

    public List<TinyGroup> getGroupList() {
        return groupList;
    }

    public boolean getOperationSuccessful() {
        return operationSuccessful.get();
    }

    private boolean isLoggedIn() {
        return isLoggedIn;
    }

    private void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    private String encryptPassword(String passwordToHash){
        String generatedPassword=null;
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public boolean signup(String username, String name, String password) {
        password = encryptPassword(password);
        //send to server
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.REGISTER_ACCOUNT,
                    new TinyRegisterCredentials(username, name, password)
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);

        return operationSuccessful.get();
    }

    public boolean login(String username, String password) {
        password = encryptPassword(password);
        md5Password = password;
        //send to server
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.LOGIN_ACCOUNT,
                    new TinyCredentials(username, password)
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);

        if (operationSuccessful.get()) {
            setUsername(username); //do argumento da funcao
            getNameFromServer();
            setLoggedIn(true);
            return true;
        }

        setLoggedIn(false);
        return false;
    }

    public boolean logout() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.LOGOUT_ACCOUNT,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUsername(null);
        setName(null);
        setLoggedIn(false);
        return true;
    }

    public boolean makeCommand(int commandCode, String info1, String info2, File info3) {
        boolean value = switch (commandCode) {
            case 0 -> editName(info1);
            case 1 -> editPassword(info1);
            case 2 -> listUserRequest(info1);
            case 3 -> listContactList();
            case 4 -> deleteContactFromContactList(info1);
            case 5 -> sendMessage(0,info1,info2,info3);
            case 6 -> getMessages(0);
            case 8 -> deleteMessage(Integer.parseInt(info1));
            case 10 -> sendFriendRequest(info1);
            case 11 -> getFriendRequests();
            case 12 -> answerFriendRequest(true,info1);
            case 13 -> answerFriendRequest(false,info1);
            case 14 -> getAllGroupList();
            case 15 -> sendGroupRequest(Integer.parseInt(info1));
            case 16 -> getGroupRequests();
            case 17 -> answerGroupRequest(true,info1,Integer.parseInt(info2));
            case 18 -> answerGroupRequest(false,info1,Integer.parseInt(info2));
            case 19 -> createGroup(info1);
            case 20 -> exitGroup(Integer.parseInt(info1));
            case 21 -> editGroupName(Integer.parseInt(info1),info2);
            case 22 -> deleteGroup(Integer.parseInt(info1));
            case 23 -> removeMemberFromGroup(info1,Integer.parseInt(info2));
            case 24 -> sendMessage(1,info1,info2,info3);
            case 25 -> getMessages(1);
            case 26 -> getFile(Integer.parseInt(info1), info2);

            default -> false;
        };

        return value;
    }

    public void updateAllData() {
        getMessages(0);
        getMessages(1);
        listContactList();
        listUserRequest(null);
        getFriendRequests();
        getAllGroupList();
        getSelfGroupsList();
        getGroupRequests();
        getNameFromServer();

        if (metaPdObservable != null) metaPdObservable.updateObservers();
    }

    private boolean getFile(int messageId, String fileName) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_FILE,
                    messageId
            ));
            oos.flush();

            FileUtility.downloadFile(new File(fileName), socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean editName(String newName) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.CHANGE_NAME,
                    newName
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);

        if (operationSuccessful.get()) {
            setName(newName);
        }
        return operationSuccessful.get();
    }

    private boolean editPassword(String newPassword) {
        newPassword=encryptPassword(newPassword);
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.CHANGE_PASSWORD,
                    newPassword
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);

        if (operationSuccessful.get()) {
            logout();
        }
        return operationSuccessful.get();
    }

    private boolean listUserRequest(String nameToSearch) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_USERS,
                    nameToSearch
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean listContactList() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_CONTACTS,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean deleteContactFromContactList(String username) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.DELETE_CONTACT,
                    username
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean sendFriendRequest(String username) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.SEND_FRIEND_REQUEST,
                    username
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }


    private boolean getFriendRequests() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_FRIEND_REQUESTS,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return true;
    }

    private boolean getNameFromServer() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_NAME,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return true;
    }

    private boolean answerFriendRequest(boolean answer,String username) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.ANSWER_FRIEND_REQUEST,
                    new TinyAnswerContactRequest(username, answer)
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean sendMessage(int typeOfDestination,String identityOfDestination, String message, File file) {
        switch (typeOfDestination) {
            case 0: //send to contact
                try {
                    if (file == null) {
                        oos.writeObject(new TinyRequest(
                                RequestType.SEND_USER_MESSAGE,
                                new TinyMessageSent(identityOfDestination, message, "")
                        ));
                        oos.flush();

                        while (!isDataReady.get()) {}
                        isDataReady.set(false);
                        return operationSuccessful.get();
                    }

                    oos.writeObject(new TinyRequest(
                            RequestType.SEND_USER_MESSAGE,
                            new TinyMessageSent(identityOfDestination, message, file.getName())
                    ));
                    oos.flush();

                    while (!isDataReady.get()) {}
                    isDataReady.set(false);
                    if (!operationSuccessful.get()) return false;

                    FileUtility.uploadFile(file, oos);

                    while (!isDataReady.get()) {}
                    isDataReady.set(false);
                    System.out.println("mais depois!");
                    return operationSuccessful.get();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            case 1: //send to group
                try {
                    if (file == null) {
                        oos.writeObject(new TinyRequest(
                                RequestType.SEND_GROUP_MESSAGE,
                                new TinyMessageSent(identityOfDestination, message, "")
                        ));
                        oos.flush();

                        while (!isDataReady.get()) {}
                        isDataReady.set(false);
                        return operationSuccessful.get();
                    }

                    oos.writeObject(new TinyRequest(
                            RequestType.SEND_GROUP_MESSAGE,
                            new TinyMessageSent(identityOfDestination, message, file.getName())
                    ));
                    oos.flush();

                    while (!isDataReady.get()) {}
                    isDataReady.set(false);
                    if (!operationSuccessful.get()) return false;

                    FileUtility.uploadFile(file, oos);

                    while (!isDataReady.get()) {}
                    isDataReady.set(false);
                    return operationSuccessful.get();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
        }
        return false;
    }

    private boolean getMessages(int typeOfDestination) {
        switch (typeOfDestination) {
            case 0: // contact
                try {
                    oos.writeObject(new TinyRequest(
                            RequestType.GET_CONTACT_MESSAGES,
                            null
                    ));
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (!isDataReady.get()) {}
                isDataReady.set(false);
                return true;
            case 1: // group
                try {
                    oos.writeObject(new TinyRequest(
                            RequestType.GET_GROUP_MESSAGES,
                            null
                    ));
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (!isDataReady.get()) {}
                isDataReady.set(false);
                return true;
            default:
        }
        return false;
    }

    private boolean deleteMessage(int idMessage) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.DELETE_MESSAGE,
                    idMessage
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean getAllGroupList() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_ALL_GROUP_LIST,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return true;
    }

    private boolean getSelfGroupsList() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_MY_GROUP_LIST,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return true;
    }

    private boolean sendGroupRequest(int groupID) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.SEND_GROUP_REQUEST,
                    groupID
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean getGroupRequests() {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.GET_GROUP_REQUESTS,
                    null
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return true;
    }

    private boolean answerGroupRequest(boolean answer, String username, int group) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.ANSWER_GROUP_REQUEST,
                    new TinyAnswerGroupRequest(group,username,answer)
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean createGroup(String groupName) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.CREATE_GROUP,
                    groupName
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean exitGroup(int groupID) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.EXIT_GROUP,
                    groupID
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean editGroupName(int groupID, String newGroupName) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.EDIT_GROUP_NAME,
                    new TinyEditGroupRequest(groupID,newGroupName)
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean deleteGroup(int groupID) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.DELETE_GROUP,
                    groupID
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }

    private boolean removeMemberFromGroup(String username, int groupID) {
        try {
            oos.writeObject(new TinyRequest(
                    RequestType.REMOVE_MEMBER_FROM_GROUP,
                    new TinyEditGroupRequest(groupID,username)
            ));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!isDataReady.get()) {}
        isDataReady.set(false);
        return operationSuccessful.get();
    }
}
