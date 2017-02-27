package app.twinstartech.travlr.models;

import com.google.firebase.database.ServerValue;

/**
 * Created by Siri on 2/5/2017.
 */

public class User {

    public String uid;
    public String name;
    public String email;


    public User(){

    }
    public User(String uid, String name, String email) {
        this.uid = uid;this.name = name;
        this.email = email;
    }
}
