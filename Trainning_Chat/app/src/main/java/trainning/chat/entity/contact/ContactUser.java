package trainning.chat.entity.contact;

/**
 * Created by ASUS on 12/10/2015.
 */
public class ContactUser {
    private String name;
    private String email;

    public ContactUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
