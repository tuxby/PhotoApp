package by.tux.instagram160.helpers;

public class Constants {
    public final static String registerUrl = "https://tuxxx.ddns.net/user/register";
    public final static String loginUrl = "https://tuxxx.ddns.net/user/login";
    public final static String editUrl = "https://tuxxx.ddns.net/user/change";
    public final static String photosUrl = "https://tuxxx.ddns.net/photos/feed";
    public final static String photosUser = "https://tuxxx.ddns.net/photos/forUser";
    public final static String addPhoto = "https://tuxxx.ddns.net/photos/add";

    public final static String getFirebaseImageFullURL(String url){
        String firebaseImageUrlPart1 = "https://firebasestorage.googleapis.com/v0/b/instagram160.appspot.com/o/";
        String firebaseImageUrlPart2 = "?alt=media&token=91f6b1d6-c2cb-41e4-b6e9-55e332466d77";
        return firebaseImageUrlPart1 + url + firebaseImageUrlPart2;
    }
}
