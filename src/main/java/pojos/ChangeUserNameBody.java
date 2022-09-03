package pojos;

public class ChangeUserNameBody {
    private String name;

    public ChangeUserNameBody(){}

    public ChangeUserNameBody(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
