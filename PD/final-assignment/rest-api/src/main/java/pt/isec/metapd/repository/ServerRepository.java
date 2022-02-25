package pt.isec.metapd.repository;

import pt.isec.metapd.communication.*;
import pt.isec.metapd.repository.database.DbConnectionHelper;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ServerRepository {
    private final String address;

    public ServerRepository(String address) {
        this.address = address;
    }

    public List<TinyFile> getAllFiles() throws SQLException {
        List<TinyFile> formattedFileNames = new LinkedList<>();

        try (
            Connection connection = DbConnectionHelper.getConnection(address);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT message_id, file_name " +
                    "FROM message "
            )
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int messageId = resultSet.getInt(1);
                String fileName = resultSet.getString(2);

                if (fileName == null || fileName.isBlank()) continue;

                formattedFileNames.add(new TinyFile(messageId, fileName));
            }

            return formattedFileNames;
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public String getFileNameForMessageId(int messageId) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT file_name " +
                        "FROM message " +
                        "WHERE message_id = ? "
                )
        ) {
            preparedStatement.setInt(1, messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;

            return resultSet.getString(1);
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public int getLastMessageForUsername(String username) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement getMsgIdPrepStmt = connection.prepareStatement(
                        "SELECT MAX(message_id) " +
                        "FROM message " +
                        "WHERE sender_username = ? "
                )
        ) {
            getMsgIdPrepStmt.setString(1, username);
            ResultSet resultSet = getMsgIdPrepStmt.executeQuery();
            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public List<String> getUsernamesAssociatedWithMessage(int messageId) throws SQLException {
        List<String> usernamesAssociated = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement contactMessagePrepStmt = connection.prepareStatement(
                        "SELECT username " +
                        "FROM user_receives " +
                        "WHERE message_id = ? "
                );
                PreparedStatement groupMessagePrepStmt = connection.prepareStatement(
                        "SELECT username " +
                        "FROM part_of " +
                        "WHERE group_id = ( " +
                                "SELECT group_id " +
                                "FROM group_receives " +
                                "WHERE message_id = ? " +
                        ") "
                );
        ) {
            contactMessagePrepStmt.setInt(1, messageId);
            ResultSet resultSet = contactMessagePrepStmt.executeQuery();

            while (resultSet.next()) {
                usernamesAssociated.add(resultSet.getString(1));
            }

            groupMessagePrepStmt.setInt(1, messageId);
            resultSet = groupMessagePrepStmt.executeQuery();

            while (resultSet.next()) {
                usernamesAssociated.add(resultSet.getString(1));
            }

            return usernamesAssociated;
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public String getGroupOwnerUsername(int groupId) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT manager_username " +
                        "FROM chat_group " +
                        "WHERE group_id = ? "
                )
        ) {
            preparedStatement.setInt(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return resultSet.getString(1);
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public String getNameForUser(String username) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT name " +
                        "FROM user " +
                        "WHERE username = ? "
                )
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return resultSet.getString(1);
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public List<TinyGroup> getGroupsForUser(String username) throws SQLException {
        List<TinyGroup> groups = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement getGroupPrepStmt = connection.prepareStatement(
                        "SELECT group_id, group_name " +
                        "FROM chat_group " +
                        "WHERE group_id IN ( " +
                                "SELECT group_id " +
                                "FROM part_of " +
                                "WHERE username = ? " +
                                "AND accepted = 1 " +
                        ") "
                );
                PreparedStatement getGroupMembersPrepStmt = connection.prepareStatement(
                        "SELECT username, name, status " +
                        "FROM user " +
                        "WHERE username IN ( " +
                                "SELECT username " +
                                "FROM part_of " +
                                "WHERE group_id = ? " +
                                "AND accepted = 1 " +
                        ") "
                )
        ) {
            getGroupPrepStmt.setString(1, username);
            ResultSet resultSet = getGroupPrepStmt.executeQuery();

            while (resultSet.next()) {
                List<TinyUser> groupMembers = new LinkedList<>();

                getGroupMembersPrepStmt.setInt(1, resultSet.getInt(1));
                ResultSet membersResultSet = getGroupMembersPrepStmt.executeQuery();

                while (membersResultSet.next()) {
                    groupMembers.add(new TinyUser(
                            membersResultSet.getString(1),
                            membersResultSet.getString(2),
                            membersResultSet.getInt(3) == 1
                    ));
                }

                groups.add(new TinyGroup(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        groupMembers
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return groups;
    }

    public List<TinyGroupRequest> getGroupRequestsForUser(String username) throws SQLException {
        List<TinyGroupRequest> requests = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement getGroupsRequestsPrepStmt = connection.prepareStatement(
                        "SELECT DISTINCT group_id, username " +
                        "FROM part_of " +
                        "WHERE group_id IN ( " +
                                "SELECT group_id " +
                                "FROM chat_group " +
                                "WHERE manager_username = ? " +
                        ") " +
                        "AND accepted = 0 "
                );
                PreparedStatement getUserInfo = connection.prepareStatement(
                        "SELECT username, name, status " +
                        "FROM user " +
                        "WHERE username = ? "
                )
        ) {
            getGroupsRequestsPrepStmt.setString(1, username);
            ResultSet resultSet = getGroupsRequestsPrepStmt.executeQuery();

            while (resultSet.next()) {
                getUserInfo.setString(1, resultSet.getString(2));

                ResultSet userInfoResultSet = getUserInfo.executeQuery();
                userInfoResultSet.next();

                requests.add(new TinyGroupRequest(
                        new TinyUser(
                                userInfoResultSet.getString(1),
                                userInfoResultSet.getString(2),
                                userInfoResultSet.getInt(3) == 1
                        ),
                        resultSet.getInt(1)
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return requests;
    }

    public List<TinyUser> getFriendRequestsForUser(String username) throws SQLException {
        List<TinyUser> users = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT username, name, status " +
                        "FROM user " +
                        "WHERE username IN ( " +
                                "SELECT requester_username " +
                                "FROM contact_of " +
                                "WHERE receiver_username = ? " +
                                "AND accepted = 0 " +
                        ") "
                )
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(new TinyUser(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3) == 1
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return users;
    }

    public List<TinyGroup> getAllGroups() throws SQLException {
        List<TinyGroup> allGroups = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement groupInfoPrepStmt = connection.prepareStatement(
                        "SELECT group_id, group_name " +
                        "FROM chat_group "
                );
                PreparedStatement getGroupMembersPrepStmt = connection.prepareStatement(
                        "SELECT username, name, status " +
                        "FROM user " +
                        "WHERE username IN ( " +
                                "SELECT username " +
                                "FROM part_of " +
                                "WHERE group_id = ? " +
                                "AND accepted = 1 " +
                        ") "
                )
        ) {
            ResultSet resultSet = groupInfoPrepStmt.executeQuery();

            while (resultSet.next()) {
                List<TinyUser> groupMembers = new LinkedList<>();

                getGroupMembersPrepStmt.setInt(1, resultSet.getInt(1));
                ResultSet membersResultSet = getGroupMembersPrepStmt.executeQuery();

                while (membersResultSet.next()) {
                    groupMembers.add(new TinyUser(
                            membersResultSet.getString(1),
                            membersResultSet.getString(2),
                            membersResultSet.getInt(3) == 1
                    ));
                }

                allGroups.add(new TinyGroup(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        groupMembers
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return allGroups;
    }

    public List<TinyUser> getAllUsers() throws SQLException {
        List<TinyUser> allUsers = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT username, name, status " +
                        "FROM user "
                )
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                allUsers.add(new TinyUser(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3) == 1
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return allUsers;
    }

    public List<TinyUser> getContactsForUser(String username) throws SQLException {
        List<TinyUser> contacts = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT u.username, u.name, u.status " +
                        "FROM user as u, ( " +
                                "SELECT requester_username, receiver_username " +
                                "FROM contact_of " +
                                "WHERE accepted = 1 " +
                                "AND (requester_username = ? " +
                                "OR receiver_username = ?) " +
                        ") as t " +
                        "WHERE u.username = t.requester_username " +
                        "OR u.username = t.receiver_username "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString(1).equals(username)) continue;

                contacts.add(new TinyUser(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3) == 1
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return contacts;
    }

    public List<String> getGroupUsers(int groupId) throws SQLException {
        List<String> users = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT username " +
                        "FROM part_of " +
                        "WHERE group_id = ? " +
                        "AND accepted = 1 "
                )
        ) {
            preparedStatement.setInt(1, groupId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return users;
    }

    public List<TinyMessageReceived> getContactMessagesForUser(String username) throws SQLException {
        List<TinyMessageReceived> messages = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement privateMessagesPrepStmt = connection.prepareStatement(
                        "SELECT m.message_id, m.sender_username, m.text, m.file_name, m.send_date " +
                        "FROM message AS m, user_receives AS ur " +
                        "WHERE ur.username = ? " +
                        "AND m.message_id = ur.message_id "
                );
                PreparedStatement sentPrivateMessagesPrepStmt = connection.prepareStatement(
                        "SELECT DISTINCT m.message_id, m.sender_username, m.text, m.file_name, m.send_date " +
                        "FROM message AS m, user_receives AS ur " +
                        "WHERE m.message_id IN ( " +
                                "SELECT message_id " +
                                "FROM user_receives " +
                        ") AND m.sender_username = ? "
                )
        ) {
            privateMessagesPrepStmt.setString(1, username);
            ResultSet resultSet = privateMessagesPrepStmt.executeQuery();

            while (resultSet.next()) {
                messages.add(new TinyMessageReceived(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        ""
                ));
            }

            sentPrivateMessagesPrepStmt.setString(1, username);
            resultSet = sentPrivateMessagesPrepStmt.executeQuery();

            while (resultSet.next()) {
                messages.add(new TinyMessageReceived(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        ""
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        messages.sort(Collections.reverseOrder());
        return messages;
    }

    public List<TinyMessageReceived> getMessagesFromContactForUser(String username, String contactUsername) throws SQLException {
        List<TinyMessageReceived> messages = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement privateMessagesPrepStmt = connection.prepareStatement(
                        "SELECT m.message_id, m.sender_username, m.text, m.file_name, m.send_date " +
                        "FROM message AS m, user_receives AS ur " +
                        "WHERE ur.username = ? " +
                        "AND m.message_id = ur.message_id " +
                        "AND m.sender_username = ? "
                )
        ) {
            privateMessagesPrepStmt.setString(1, username);
            privateMessagesPrepStmt.setString(2, contactUsername);
            ResultSet resultSet = privateMessagesPrepStmt.executeQuery();

            while (resultSet.next()) {
                messages.add(new TinyMessageReceived(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        ""
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        messages.sort(Collections.reverseOrder());
        return messages;
    }

    public List<TinyMessageReceived> getGroupMessagesForUser(String username) throws SQLException {
        List<TinyMessageReceived> messages = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement groupsPrepStmt = connection.prepareStatement(
                        "SELECT DISTINCT pof.group_id " +
                        "FROM user AS u, part_of AS pof " +
                        "where pof.username = ? "
                )
        ) {
            groupsPrepStmt.setString(1, username);

            ResultSet resultSet = groupsPrepStmt.executeQuery();

            while (resultSet.next()) {
                messages.addAll(getMessagesFromGroup(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        messages.sort(Collections.reverseOrder());
        return messages;
    }

    public List<TinyMessageReceived> getMessagesFromGroup(int groupId) throws SQLException {
        List<TinyMessageReceived> messages = new LinkedList<>();

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT m.message_id, m.sender_username, m.text, m.file_name, m.send_date " +
                        "FROM message AS m, group_receives AS gr " +
                        "WHERE gr.group_id = ? " +
                        "AND m.message_id = gr.message_id "
                );
                PreparedStatement getGroupNamePrepStmt = connection.prepareStatement(
                        "SELECT group_name " +
                        "FROM chat_group " +
                        "WHERE group_id = ? "
                )
        ) {
            preparedStatement.setInt(1, groupId);
            getGroupNamePrepStmt.setInt(1, groupId);

            ResultSet resultSet = getGroupNamePrepStmt.executeQuery();
            resultSet.next();
            String groupName = resultSet.getString(1);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                messages.add(new TinyMessageReceived(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getTimestamp(5),
                        groupName
                ));
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return messages;
    }

    public boolean sendMessageToUser(TinyMessageSent tinyMessageSent, String senderUsername) throws SQLException {
        if (senderUsername.isBlank()) return false;

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement insertMsgPrepStmt = connection.prepareStatement(
                        "INSERT INTO message (sender_username, text, file_name) " +
                        "VALUES (?, ?, ?) "
                );
                PreparedStatement getMsgIdPrepStmt = connection.prepareStatement(
                        "SELECT MAX(message_id) " +
                        "FROM message " +
                        "WHERE sender_username = ? "
                );
                PreparedStatement insertMsgReceivesPrepStmt = connection.prepareStatement(
                        "INSERT INTO user_receives (username, message_id) " +
                        "VALUES (?, ?) "
                )
        ) {
            insertMsgPrepStmt.setString(1, senderUsername);

            if (tinyMessageSent.text() == null || tinyMessageSent.text().isBlank()) {
                insertMsgPrepStmt.setNull(2, Types.NULL);
            } else {
                insertMsgPrepStmt.setString(2, tinyMessageSent.text());
            }

            if (tinyMessageSent.fileName() == null || tinyMessageSent.fileName().isBlank()) {
                insertMsgPrepStmt.setNull(3, Types.NULL);
            } else {
                insertMsgPrepStmt.setString(3, tinyMessageSent.fileName());
            }

            if (insertMsgPrepStmt.executeUpdate() == 1) {
                connection.commit();
            } else {
                connection.rollback();
                return false;
            }

            getMsgIdPrepStmt.setString(1, senderUsername);
            ResultSet resultSet = getMsgIdPrepStmt.executeQuery();

            resultSet.next();
            int messageId = resultSet.getInt(1);

            insertMsgReceivesPrepStmt.setString(1, tinyMessageSent.receiver());
            insertMsgReceivesPrepStmt.setInt(2, messageId);

            if (insertMsgReceivesPrepStmt.executeUpdate() == 1) {
                connection.commit();
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
        return true;
    }

    public boolean sendMessageToGroup(TinyMessageSent tinyMessageSent, String senderUsername) throws SQLException {
        if(senderUsername.isBlank())
            return false;

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement insertMsgPrepStmt = connection.prepareStatement(
                        "INSERT INTO message (sender_username, text, file_name) " +
                        "VALUES (?, ?, ?) "
                );
                PreparedStatement getMsgIdPrepStmt = connection.prepareStatement(
                        "SELECT MAX(message_id) " +
                        "FROM message " +
                        "WHERE sender_username = ? "
                );
                PreparedStatement insertMsgReceivesPrepStmt = connection.prepareStatement(
                        "INSERT INTO group_receives (group_id, message_id) " +
                        "VALUES (?, ?) "
                )
        ) {
            insertMsgPrepStmt.setString(1, senderUsername);

            if (tinyMessageSent.text() == null || tinyMessageSent.text().isBlank()) {
                insertMsgPrepStmt.setNull(2, Types.NULL);
            } else {
                insertMsgPrepStmt.setString(2, tinyMessageSent.text());
            }

            if (tinyMessageSent.fileName() == null || tinyMessageSent.fileName().isBlank()) {
                insertMsgPrepStmt.setNull(3, Types.NULL);
            } else {
                insertMsgPrepStmt.setString(3, tinyMessageSent.fileName());
            }

            if (insertMsgPrepStmt.executeUpdate() == 1) {
                connection.commit();
            } else {
                connection.rollback();
                return false;
            }

            getMsgIdPrepStmt.setString(1, senderUsername);
            ResultSet resultSet = getMsgIdPrepStmt.executeQuery();

            resultSet.next();
            int messageId = resultSet.getInt(1);

            insertMsgReceivesPrepStmt.setInt(1, Integer.parseInt(tinyMessageSent.receiver().trim()));
            insertMsgReceivesPrepStmt.setInt(2, messageId);

            if (insertMsgReceivesPrepStmt.executeUpdate() == 1) {
                connection.commit();
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        return true;
    }

    public boolean checkLoginCredentials(String username, String md5Password) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * " +
                        "FROM user " +
                        "WHERE username = ? " +
                        "AND password = ? "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, md5Password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToLogin(String username, String md5Password) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * " +
                        "FROM user " +
                        "WHERE username = ? " +
                        "AND password = ? "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, md5Password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                setStatus(username, true);
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToRegister(String username, String name, String md5Password) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * " +
                        "FROM user " +
                        "WHERE username = ? "
                )
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) return false;
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO user (username, name, password, status) " +
                        "VALUES (?, ?, ?, ?) "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, md5Password);
            preparedStatement.setInt(4, 0);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToChangeName(String username, String newName) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE user " +
                        "SET name = ? " +
                        "WHERE username = ? "
                )
        ) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, username);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToChangePassword(String username, String newMd5Password) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE user " +
                        "SET password = ? " +
                        "WHERE username = ? "
                )
        ) {
            preparedStatement.setString(1, newMd5Password);
            preparedStatement.setString(2, username);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToDeleteMessage(String username, int messageId) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM message " +
                        "WHERE sender_username = ? " +
                        "AND message_id = ? "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, messageId);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToDeleteContact(String username, String contactUsername) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM contact_of " +
                        "WHERE accepted = 1 " +
                        "AND (( " +
                                "requester_username = ? AND receiver_username = ? " +
                        ") OR ( " +
                                "requester_username = ? AND receiver_username = ? " +
                        ")) "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, contactUsername);
            preparedStatement.setString(3, contactUsername);
            preparedStatement.setString(4, username);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToCreateGroup(String groupName, String username) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO chat_group (manager_username, group_name) " +
                        "VALUES (?, ?) "
                );
                PreparedStatement getCreatedGroupIdPrepStmt = connection.prepareStatement(
                        "SELECT MAX(group_id) " +
                        "FROM chat_group " +
                        "WHERE manager_username = ? "
                );
                PreparedStatement makeRelationPrepStmt = connection.prepareStatement(
                        "INSERT INTO part_of (username, group_id, accepted)" +
                        "VALUES (?, ?, 1) "
                );
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, groupName);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();

                getCreatedGroupIdPrepStmt.setString(1, username);
                ResultSet resultSet = getCreatedGroupIdPrepStmt.executeQuery();
                resultSet.next();

                makeRelationPrepStmt.setString(1, username);
                makeRelationPrepStmt.setInt(2, resultSet.getInt(1));

                if (makeRelationPrepStmt.executeUpdate() == 1) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToEditGroupName(String username, int groupId, String newGroupName) throws SQLException {
        if (!isUserGroupManager(username, groupId)) return false;

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE chat_group " +
                        "SET group_name = ? " +
                        "WHERE group_id = ? "
                )
        ) {
            preparedStatement.setString(1, newGroupName);
            preparedStatement.setInt(2, groupId);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToRemoveUserFromGroup(String managerUsername, String username, int groupId) throws SQLException {
        if (!isUserGroupManager(managerUsername, groupId)) return false;

        return tryToExitGroup(username, groupId);
    }

    public boolean tryToExitGroup(String username, int groupId) throws SQLException {
        if (isUserGroupManager(username, groupId)) return tryToDeleteGroup(groupId, username);

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM part_of " +
                        "WHERE username = ? " +
                        "AND group_id = ? "
                )
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, groupId);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToDeleteGroup(int groupId, String username) throws SQLException {
        if(!isUserGroupManager(username, groupId)) return false;

        System.out.println("WTF, NÃO POSSO APARECER");

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM chat_group " +
                        "WHERE group_id = ? "
                )
        ) {
            preparedStatement.setInt(1, groupId);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToAnswerGroupRequest(
            String managerUsername,
            String senderUsername,
            int groupId,
            boolean answer
    ) throws SQLException {
        if (!isUserGroupManager(managerUsername, groupId)) return false;

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement acceptPrepStmt = connection.prepareStatement(
                        "UPDATE part_of " +
                        "SET accepted = 1 " +
                        "WHERE username = ? " +
                        "AND group_id = ? "
                );
                PreparedStatement rejectPrepStmt = connection.prepareStatement(
                        "DELETE FROM part_of " +
                        "WHERE username = ? " +
                        "AND group_id = ? " +
                        "AND accepted = 0 "
                )
        ) {
            acceptPrepStmt.setString(1, senderUsername);
            acceptPrepStmt.setInt(2, groupId);

            rejectPrepStmt.setString(1, senderUsername);
            rejectPrepStmt.setInt(2, groupId);

            if (answer) {
                if (acceptPrepStmt.executeUpdate() == 1) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } else {
                if (rejectPrepStmt.executeUpdate() == 1) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToAnswerFriendRequest(
            String receiverUsername,
            String senderUsername,
            boolean answer
    ) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement acceptPrepStmt = connection.prepareStatement(
                        "UPDATE contact_of " +
                        "SET accepted = 1 " +
                        "WHERE requester_username = ? " +
                        "AND receiver_username = ? "
                );
                PreparedStatement rejectPrepStmt = connection.prepareStatement(
                        "DELETE FROM contact_of " +
                        "WHERE requester_username = ? " +
                        "AND receiver_username = ? " +
                        "AND accepted = 0 "
                )
        ) {
            acceptPrepStmt.setString(1, senderUsername);
            acceptPrepStmt.setString(2, receiverUsername);

            rejectPrepStmt.setString(1, senderUsername);
            rejectPrepStmt.setString(2, receiverUsername);

            if (answer) {
                if (acceptPrepStmt.executeUpdate() == 1) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } else {
                if (rejectPrepStmt.executeUpdate() == 1) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToSendFriendRequest(String senderUsername, String receiverUsername) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement queryFriendRequestPrepStmt = connection.prepareStatement(
                        "SELECT * " +
                        "FROM contact_of " +
                        "WHERE ((" +
                                "requester_username = ? AND receiver_username = ? " +
                        ") OR (" +
                                "requester_username = ? AND receiver_username = ? " +
                        ")) "
                );
                PreparedStatement friendRequestPrepStmt = connection.prepareStatement(
                        "INSERT INTO contact_of (requester_username, receiver_username, accepted) " +
                        "VALUES (?, ?, 0) "
                )
        ) {
            queryFriendRequestPrepStmt.setString(1, senderUsername);
            queryFriendRequestPrepStmt.setString(2, receiverUsername);
            queryFriendRequestPrepStmt.setString(3, receiverUsername);
            queryFriendRequestPrepStmt.setString(4, senderUsername);

            ResultSet resultSet = queryFriendRequestPrepStmt.executeQuery();

            if (resultSet.next()) return false;

            friendRequestPrepStmt.setString(1, senderUsername);
            friendRequestPrepStmt.setString(2, receiverUsername);

            if (friendRequestPrepStmt.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean tryToSendGroupRequest(String username, int groupId) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement queryGroupRequestPrepStmt = connection.prepareStatement(
                        "SELECT * " +
                        "FROM part_of " +
                        "WHERE username = ? " +
                        "AND group_id = ? "
                );
                PreparedStatement groupRequestPrepStmt = connection.prepareStatement(
                        "INSERT INTO part_of (username, group_id, accepted) " +
                        "VALUES (?, ?, 0) "
                )
        ) {
            queryGroupRequestPrepStmt.setString(1, username);
            queryGroupRequestPrepStmt.setInt(2, groupId);

            ResultSet resultSet = queryGroupRequestPrepStmt.executeQuery();

            if (resultSet.next()) return false;

            groupRequestPrepStmt.setString(1, username);
            groupRequestPrepStmt.setInt(2, groupId);

            if (groupRequestPrepStmt.executeUpdate() == 1) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public void setStatus(String username, boolean status) throws SQLException {
        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE user " +
                        "SET status = ? " +
                        "WHERE username = ? "
                )
        ) {
            preparedStatement.setInt(1, status ? 1 : 0);
            preparedStatement.setString(2, username);

            if (preparedStatement.executeUpdate() == 1) {
                connection.commit();
            } else {
                connection.rollback();
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }

    public boolean isUserGroupManager(String managerUsername, int groupId) throws SQLException {

        try (
                Connection connection = DbConnectionHelper.getConnection(address);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT manager_username, group_id " +
                        "FROM chat_group " +
                        "WHERE manager_username = ? " +
                        "AND group_id = ? "
                )
        ) {
            preparedStatement.setString(1, managerUsername);
            preparedStatement.setInt(2, groupId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new SQLException(e.toString());
        }
    }
}
