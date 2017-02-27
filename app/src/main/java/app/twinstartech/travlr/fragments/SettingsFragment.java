package app.twinstartech.travlr.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import app.twinstartech.travlr.R;
import app.twinstartech.travlr.activities.LoginActivity;
import app.twinstartech.travlr.tools.Constants;
import app.twinstartech.travlr.tools.FBDB;
import app.twinstartech.travlr.tools.FBSTORE;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

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

    FirebaseUser user;

    public static String TAG = Constants.GLOBAL_TAG;
    private final int RESULT_LOAD_IMAGE = 100;
    FirebaseStorage storage = FirebaseStorage.getInstance();
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
         user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            logout();
            return;
        }
        loadDetails();

    }


    void loadDetails(){
        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());

//        storage.getReference().child(FBSTORE.FOLDER_USER_IMAGES+FBSTORE.NAME_PROFILEIMAGE).getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Picasso.with(getActivity()).load(uri).into(civProfileImage);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //load default facebook profile.
//                        Picasso.with(getActivity()).load(user.getPhotoUrl()).into(civProfileImage);
//                    }
//                });


        FBDB.getUserProfileImageReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Picasso.with(getActivity()).load(dataSnapshot.getValue().toString()).into(civProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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


    @OnClick(R.id.fabUpload)
    void uploadNewImage(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if (columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor
            Log.e(TAG, picturePath);
            Bitmap b = BitmapFactory.decodeFile(picturePath);
            uploadBitmap(b);


        }
    }

    void uploadBitmap(Bitmap bitmap){

        /***
         * PREPARE LOADING DIALOG
         */
        final SweetAlertDialog dialog = new SweetAlertDialog(
                getActivity(),
                SweetAlertDialog.PROGRESS_TYPE
        );

        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setContentText("Uploading new profile..");
        dialog.setTitleText("Changing Image");
        dialog.show();

        /***
         * ACTUAL UPLOAD TASK
         */
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storage.getReference().child(FBSTORE.FOLDER_USER_IMAGES+FBSTORE.NAME_PROFILEIMAGE).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                dialog.dismissWithAnimation();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                dialog.dismissWithAnimation();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Picasso.with(getActivity()).load(downloadUrl).into(civProfileImage);

                FBDB.updateUserProfileImage(downloadUrl.toString());

            }
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                float progress = (100.0f * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                dialog.getProgressHelper().setProgress(progress);
            }
        });
    }
}
