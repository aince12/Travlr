package app.twinstartech.travlr.tools;

import com.facebook.Profile;

import app.twinstartech.travlr.R;
import app.twinstartech.travlr.models.User;

/**
 * Created by Siri on 2/5/2017.
 */

public class Constants {
    public static final String GLOBAL_TAG = "Travlr";

    public static Profile currentProfile;

    public static String menu_titles[] = { "Wall", "Travels", "Buds", "Settings" };
    public static int menu_icons[] = {
            R.mipmap.ic_card_travel_white,
            R.mipmap.ic_airplane_white,
            R.mipmap.ic_people_white,
            R.mipmap.ic_settings_white
    };

    /***
     * FIREBASE VARIABLES
     */
    public static User currentUser;


}
