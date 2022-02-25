package pt.isec.metapd.restapi.token;

import pt.isec.metapd.restapi.data.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;

public class TokenManager {
    private static final int MAX_TOKEN_TIME = 120; // seconds

    private final Set<Token> tokens = new HashSet<>();

    public TokenManager() { }

    public String getUsernameForToken(String receivedToken) throws NoSuchElementException {
        for (Token token : tokens) {
            if (token.token().equals(receivedToken)) {
                return token.username();
            }
        }

        throw new NoSuchElementException("Requested token doesn't exist!");
    }

    public String generateToken(User user) {
        long thisInstantEpoch = Instant.now().getEpochSecond();
        String finalToken = encryptToken(user.username() + user.md5Password() + thisInstantEpoch);

        tokens.add(new Token(user.username(), finalToken, thisInstantEpoch));
        return finalToken;
    }

    public String encryptToken(String receivedToken) {
        String generatedToken = null;

        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(receivedToken.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedToken = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken;
    }

    public boolean isTokenValid(String receivedToken) {
        for (Token token : tokens) {
            if (token.token().equals(receivedToken)) {
                if (isTokenExpired(token)) {
                    tokens.remove(token);
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    private boolean isTokenExpired(Token receivedToken) {
        return (Instant.now().getEpochSecond() - receivedToken.creationTime()) > MAX_TOKEN_TIME;
    }
}
