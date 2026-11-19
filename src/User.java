public final class User {
    private final String username;
    private String password;
    private String displayName;

    public User(String username, String password, String displayName) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDisplayName() { return displayName; }
    public boolean authenticate(String candidate) { return password.equals(candidate); }

    public void updateProfile(String displayName, String password) {
        this.displayName = displayName.trim();
        this.password = password;
    }
}