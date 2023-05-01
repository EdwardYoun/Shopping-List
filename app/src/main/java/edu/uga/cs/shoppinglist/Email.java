package edu.uga.cs.shoppinglist;

/**
 * The email object for user emails.
 */
public class Email {

    private String email;

    public Email(){}

    public Email(String email) {
        this.email = email;
    }

    /**
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
