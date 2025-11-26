package models;

public class User {
    private int id;         // Map với cột uID
    private String user;    // Map với cột [user]
    private String pass;    // Map với cột pass
    private int isSell;     // Map với cột isSell
    private int isAdmin;    // Map với cột isAdmin
    private String email;   // Map với cột email

    public User() {
    }

    public User(int id, String user, String pass, int isSell, int isAdmin, String email) {
        this.id = id;
        this.user = user;
        this.pass = pass;
        this.isSell = isSell;
        this.isAdmin = isAdmin;
        this.email = email;
    }

    // Getter và Setter (Bấm Alt+Insert để tự tạo lại nếu lười copy)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public int getIsSell() { return isSell; }
    public void setIsSell(int isSell) { this.isSell = isSell; }

    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", user=" + user + ", email=" + email + '}';
    }
}