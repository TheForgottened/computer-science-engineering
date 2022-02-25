package pt.isec.metapd.restapi.token;

public record Token(String username, String token, long creationTime) { }
