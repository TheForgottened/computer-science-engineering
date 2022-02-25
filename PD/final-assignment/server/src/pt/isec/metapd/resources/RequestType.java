package pt.isec.metapd.resources;

public enum RequestType {
    //
    // UDP
    // REQUESTS
    //

    //
    // Server Requests
    //

    // Pinging-related requests
    PING,

    // File-server-requesting-related requests
    GET_VALID_FILE_SERVER,

    // Db-update-related requests
    DB_UPDATE,

    //
    // Client Requests
    //

    // Server-requesting-related requests
    GET_VALID_SERVER,

    //
    // TCP
    // REQUESTS
    //

    //
    // Server Requests
    //

    // Order to update views
    UPDATE_VIEW,

    // Tell the user the result of the asked operation
    OPERATION_SUCCESSFUL,
    OPERATION_FAILED,

    //
    // Client Requests
    //

    // Account-related requests
    REGISTER_ACCOUNT,
    LOGIN_ACCOUNT,
    LOGOUT_ACCOUNT,
    CHANGE_NAME,
    CHANGE_PASSWORD,

    // Message-related requests
    GET_CONTACT_MESSAGES,
    GET_GROUP_MESSAGES,
    GET_CONTACTS,
    GET_ALL_GROUP_LIST,
    GET_MY_GROUP_LIST,
    GET_USERS,
    GET_FRIEND_REQUESTS,
    GET_GROUP_REQUESTS,
    GET_NAME,
    SEND_GROUP_MESSAGE,
    SEND_USER_MESSAGE,
    DELETE_MESSAGE,
    DELETE_CONTACT,

    // File-related requests
    GET_FILE,

    // Invite-related requests
    SEND_FRIEND_REQUEST,
    SEND_GROUP_REQUEST,
    ANSWER_FRIEND_REQUEST,
    ANSWER_GROUP_REQUEST,

    //Group Management-related requests
    CREATE_GROUP,
    EXIT_GROUP,
    EDIT_GROUP_NAME,
    DELETE_GROUP,
    REMOVE_MEMBER_FROM_GROUP
}
