package firstmob.firstbank.com.firstagent.model;

public class User implements IUser {
    String name;
    String passwd;

    public User(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPasswd() {
        return passwd;
    }

}