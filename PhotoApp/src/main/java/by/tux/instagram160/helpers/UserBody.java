package by.tux.instagram160.helpers;
import java.util.Objects;

public class UserBody {
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String mainPhoto;

    public static UserBody.Builder newBuilder() {
        return new UserBody().new Builder();
    }

    public class Builder {

        protected Builder() {}

        public UserBody.Builder setLogin(String login) {
            UserBody.this.login = login;
            return this;
        }

        public UserBody.Builder setPassword(String password) {
            UserBody.this.password = password;
            return this;
        }

        public UserBody.Builder setName(String name) {
            UserBody.this.name = name;
            return this;
        }

        public UserBody.Builder setLastName(String lastName) {
            UserBody.this.lastName = lastName;
            return this;
        }

        public UserBody.Builder setMainPhoto(String mainPhoto) {
            UserBody.this.mainPhoto = mainPhoto;
            return this;
        }

        public UserBody build() {
            return UserBody.this;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBody userBody = (UserBody) o;
        return Objects.equals(name, userBody.name) && Objects.equals(lastName, userBody.lastName) && Objects.equals(login, userBody.login) && Objects.equals(password, userBody.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, login, password);
    }

}
