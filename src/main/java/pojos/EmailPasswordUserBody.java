package pojos;

public class EmailPasswordUserBody {
    private String email;
    private String password;

    public EmailPasswordUserBody(){}

    public EmailPasswordUserBody(String email, String password){
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
