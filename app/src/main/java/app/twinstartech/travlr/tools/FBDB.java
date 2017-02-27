package app.twinstartech.travlr.tools;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.twinstartech.travlr.models.User;

/**
 * Created by Siri on 2/25/2017.
 */

public class FBDB {



    /**
     * FIREBASE TABLES
     */
    public static final String TABLE_USERS = "users";
    public static final String TABLE_USER_INFO="info";
    public static final String KEY_USER_CREATE_DATE = "createDate";
    public static final String KEY_USER_PROFILE_PHOTO_URL = "profile_photo_url";

    public static void updateUserProfileImage(String url){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(FBDB.TABLE_USERS)
                .child(Constants.currentUser.uid)
                .child(FBDB.TABLE_USER_INFO)
                .child(FBDB.KEY_USER_PROFILE_PHOTO_URL)
                .setValue(url);
    }
    public static DatabaseReference getUserProfileImageReference(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        return database.child(FBDB.TABLE_USERS)
                .child(Constants.currentUser.uid)
                .child(FBDB.TABLE_USER_INFO)
                .child(FBDB.KEY_USER_PROFILE_PHOTO_URL);
    }
}
