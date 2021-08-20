package sample;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String surname;
    private String email;
    private String password;
    private int permission;

    public User() {
        this.name = "";
        this.surname = "";
        this.email = "";
        this.password = "";
        this.permission = 0;
    }

    public User(final String name, final String sur, final String email, final String password ,final int perm) {
        this.name = name;
        this.surname = sur;
        this.email = email;
        this.password = password;
        this.permission = perm;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }


    public String getEmail() {
        return this.email;
    }

    public int getPermission() {
        return this.permission;
    }

    public String getPassword() {
        return this.password;
    }

}
