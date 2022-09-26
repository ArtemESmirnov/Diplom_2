package pojos;

public class ChangeUserPasswordBody implements UserBody{
    private String password;

    public ChangeUserPasswordBody(){}

    public ChangeUserPasswordBody(String password){
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
