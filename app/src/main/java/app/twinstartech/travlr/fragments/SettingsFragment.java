package app.twinstartech.travlr.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import app.twinstartech.travlr.R;
import app.twinstartech.travlr.activities.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    @BindView(R.id.civProfileImage)
    CircularImageView civProfileImage;

    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvName)
    TextView tvName;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //setup contents
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            logout();
            return;
        }
        Picasso.with(getActivity()).load(user.getPhotoUrl()).into(civProfileImage);
        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
    }

    @OnClick(R.id.btnLogout)
    void logout(){
        //logout firebase
        FirebaseAuth.getInstance().signOut();
        //logout facebook;
        LoginManager.getInstance().logOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
