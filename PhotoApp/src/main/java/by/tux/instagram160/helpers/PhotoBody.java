package by.tux.instagram160.helpers;

public class PhotoBody {

    private String disc;
    private String url;
    private String name;

    public PhotoBody(String disc, String url, String name) {
        this.disc = disc;
        this.url = url;
        this.name = name;
    }



    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
