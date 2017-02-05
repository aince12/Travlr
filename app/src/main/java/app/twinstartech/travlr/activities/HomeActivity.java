package app.twinstartech.travlr.activities;

import android.graphics.PorterDuff;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import app.twinstartech.travlr.R;
import app.twinstartech.travlr.fragments.WallFragment;
import app.twinstartech.travlr.tools.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends TravlrActivity {

    ResideMenu resideMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        setupMenu();


        openFragment(new WallFragment());
    }

    void setupMenu(){

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.mipmap.bg_menu);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;


        for (int i = 0; i < Constants.menu_titles.length; i++){

            final int index = i;

            ResideMenuItem item = new ResideMenuItem(this,  Constants.menu_icons[i],  Constants.menu_titles[i]);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openMenuItem(index);

                }
            });
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
    }

    void openFragment(Fragment fragment){
        FragmentManager manager  = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.flContainer,fragment).commit();
    }

    void openMenuItem(int index){

    }




    @OnClick(R.id.btnMenu)
    void menuClicked(){
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
}
