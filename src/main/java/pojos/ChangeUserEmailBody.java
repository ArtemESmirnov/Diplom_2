package pojos;

public class ChangeUserEmailBody implements UserBody{
    private String email;

    public ChangeUserEmailBody(){}

    public ChangeUserEmailBody(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
