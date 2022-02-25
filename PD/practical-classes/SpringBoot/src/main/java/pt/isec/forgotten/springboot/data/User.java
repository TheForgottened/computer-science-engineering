package pt.isec.forgotten.springboot.data;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password)  {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + ":" + password;
    }
}
