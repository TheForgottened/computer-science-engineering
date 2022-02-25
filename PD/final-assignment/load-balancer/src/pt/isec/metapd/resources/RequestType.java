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

    // Message-related requests
    GET_MESSAGES,
    GET_CONTACTS,
    SEND_GROUP_MESSAGE,
    SEND_USER_MESSAGE,
    DELETE_MESSAGE,

    // File-related requests
    GET_FILE,

    // Invite-related requests
    SEND_FRIEND_REQUEST,
    SEND_GROUP_REQUEST
}
