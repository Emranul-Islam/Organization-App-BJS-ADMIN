package com.muhammad_sohag.admin_bjs;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPeople extends AppCompatActivity {

    private static final String TAG = "AddPeople";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storage = FirebaseStorage.getInstance().getReference();

    private CircleImageView photo;
    private EditText name, phone_number, phone_number2, password, email;

    private ProgressBar progressBar;
    private Button btn;
    private Spinner bloodGroupSpinner;
    private String[] bloodGroups;
    private Uri photoUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);

        bloodGroupSpinner = findViewById(R.id.blood_group);
        bloodGroups = getResources().getStringArray(R.array.blood_group);
        email = findViewById(R.id.email);
        photo = findViewById(R.id.photo);
        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        phone_number2 = findViewById(R.id.phone_number2);
        password = findViewById(R.id.password);


        progressBar = findViewById(R.id.progress_circular);

        btn = findViewById(R.id.btn);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(AddPeople.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddPeople.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        cropImage();

                    } else {
                        cropImage();
                    }

                } else {
                    cropImage();
                }
            }
        });

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, bloodGroups);
        bloodGroupSpinner.setAdapter(bloodAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddPeople.this);
    }

    public void saveInfo() {
//photoUri != null &&
        if (!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(phone_number.getText()) && !TextUtils.isEmpty(password.getText())) {
            btn.setText("Saveing Info...");
            btn.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            final String emailValue = email.getText().toString().toLowerCase() + "@bijos.com";

            Log.d(TAG, "saveInfo: " + emailValue);

            final String passwordValue = password.getText().toString();
            final String passwordValue2 = password.getText().toString();

            auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(AddPeople.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {
                                final String uid = task.getResult().getUser().getUid();
                                Log.d(TAG, "onComplete UID: " + uid);
                                Toast.makeText(AddPeople.this, "Id Created", Toast.LENGTH_SHORT).show();
                                //  final StorageReference storageRef = storage.child("profile_image").child(uid + ".jpeg");
                                uploadData(emailValue, passwordValue, uid, null, null);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "onComplete: " + task.getException().getMessage());
                                Toast.makeText(AddPeople.this, "2 " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "আগে সব গুলা পুরন করুন", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadData(String emailValue, String passwordValue, final String uid, String downloadUrl, final StorageReference sRef) {

        String nameValue = name.getText().toString();
        String phoneValue = phone_number.getText().toString();
        String phoneValue2 = phone_number2.getText().toString();
        String blood = bloodGroupSpinner.getSelectedItem().toString();

        //todo:  email name number password blood uid url
        HashMap<String, Object> values = new HashMap<>();
        values.put("email", emailValue);
        values.put("name", nameValue);
        values.put("number", phoneValue);
        values.put("number2", phoneValue2);
        values.put("password", passwordValue);
        values.put("blood", blood);
        values.put("uid", uid);
        values.put("url", downloadUrl);
        DocumentReference dRef = database.collection("Sodesso_List").document(uid);
        dRef.set(values)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddPeople.this, "Data Added", Toast.LENGTH_SHORT).show();


                            progressBar.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(AddPeople.this, "last" + task.getException(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Objects.requireNonNull(auth.getCurrentUser()).delete();
                        //sRef.delete();
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(AddPeople.this, "Somossa ache", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void uploadPhoto(final String uid) {
//        final StorageReference image_path = storage.child("Profile_Images").child(uid + ".jpeg");
//        image_path.putFile(photoUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Toast.makeText(AddPeople.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
//
//                                //Updating values............
//                                if (uri != null) {
//                                    Map<String, Object> newValues = new HashMap<>();
//                                    newValues.put("url", uri.toString());
//                                    DocumentReference documentReference = database.collection("Sodesso_List").document(uid);
//                                    documentReference.update(newValues)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(AddPeople.this, "EveryTHing Fine", Toast.LENGTH_SHORT).show();
//                                                    progressBar.setVisibility(View.GONE);
//                                                    photo.setVisibility(View.GONE);
//
//
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//
//                                                }
//                                            });
//                                } else {
//                                    Toast.makeText(AddPeople.this, "Somthing is Wrong", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//    }


    //kaj complete hole ei alert dialog ta show korbe:
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPeople.this);
        builder.setMessage("People Added Success :)");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    auth.signOut();
                }
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();
                photo.setImageURI(photoUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}


