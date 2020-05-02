package com.muhammad_sohag.admin_bjs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateData extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference databaseRef;
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
    private StorageReference imagePath;


    private CircleImageView upImage;
    private EditText upName, upNumber, upNumber2;
    private ProgressBar progressBar;
    private Button deletBtn, upBtn, upPhotoBt;
    private Spinner bloodGroupSpinner;
    private String[] bloodGroups;
    private TextView blood;
    private static final String TAG = "UpdateData";

    private Uri imageUri = null;
    private String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        getSupportActionBar().setTitle("সদস্য এডিট");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.up_progress_circular);
        progressBar.setVisibility(View.VISIBLE);
        upImage = findViewById(R.id.up_photo);
        upName = findViewById(R.id.up_name);
        upNumber = findViewById(R.id.up_phone_number);
        upNumber2 = findViewById(R.id.up_phone_number2);
        bloodGroups = getResources().getStringArray(R.array.blood_group);
        upBtn = findViewById(R.id.up_btn);
        upPhotoBt = findViewById(R.id.up_photo_btn);
        deletBtn = findViewById(R.id.deletBtn);
        blood = findViewById(R.id.up_blood);
        bloodGroupSpinner = findViewById(R.id.up_blood_group);


        upPhotoBt.setVisibility(View.GONE);

        ID = getIntent().getStringExtra("uid");

        databaseRef = database.collection("Sodesso_List").document(ID);
        imagePath = storage.child("Profile_images").child(ID + ".jpg");


        final ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, bloodGroups);
        bloodGroupSpinner.setAdapter(bloodAdapter);


        databaseRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(UpdateData.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot != null) {
                    //Placeholder Request
//                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions.placeholder(R.drawable.ic_launcher_background);
//                    Glide.with(UpdateActivity.this)
//                            .setDefaultRequestOptions(requestOptions)
//                            .load(documentSnapshot.getString("url"))
//                            .into(upImage);

                    upName.setText(documentSnapshot.getString("name"));
                    upNumber.setText(documentSnapshot.getString("number"));
                    upNumber2.setText(documentSnapshot.getString("number2"));
                    blood.setText(documentSnapshot.getString("blood"));
                    String bg = documentSnapshot.getString("blood").toLowerCase();
                    progressBar.setVisibility(View.GONE);


                    switch (bg) {
                        case "a+":
                            bloodGroupSpinner.setSelection(0);
                            break;
                        case "a-":
                            bloodGroupSpinner.setSelection(1);
                            break;
                        case "ab+":
                            bloodGroupSpinner.setSelection(2);
                            break;
                        case "ab-":
                            bloodGroupSpinner.setSelection(3);
                            break;
                        case "b+":
                            bloodGroupSpinner.setSelection(4);
                            break;
                        case "b-":
                            bloodGroupSpinner.setSelection(5);
                            break;
                        case "o+":
                            bloodGroupSpinner.setSelection(6);
                            break;
                        case "o-":
                            bloodGroupSpinner.setSelection(7);
                            break;
                        default:
                            bloodGroupSpinner.setSelection(8);
                            break;
                    }

                }
            }
        });
        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.delete().addOnCompleteListener(UpdateData.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });


        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    public void updateInfo(View view) {
        progressBar.setVisibility(View.VISIBLE);

        upBtn.setText("Updating Info...");
        upBtn.setClickable(false);


        String nameValue = upName.getText().toString();
        String phoneValue = upNumber.getText().toString();
        String phoneValue2 = upNumber2.getText().toString();
        String bloodList = bloodGroupSpinner.getSelectedItem().toString();


        Map<String, Object> values = new HashMap<>();
        values.put("name", nameValue);
        values.put("number", phoneValue);
        values.put("number2", phoneValue2);
        values.put("blood", bloodList);


        databaseRef.update(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(UpdateData.this, "Update !!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            upBtn.setText("Updated");

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }

    private void uploadImage() {
        if (imageUri != null) {
            Toast.makeText(this, "1111111111", Toast.LENGTH_LONG).show();
            String ID = getIntent().getStringExtra("uid");
            StorageReference imageRef = storage.child(ID + ".jpeg");

            imageRef.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getApplicationContext(), "22222222", Toast.LENGTH_LONG).show();


                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UpdateData.this, "Photo upload", Toast.LENGTH_LONG).show();
//                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        if (uri!=null) {
//                                            Toast.makeText(UpdateData.this, "Photo url get", Toast.LENGTH_LONG).show();
//                                            String downloadUrl = String.valueOf(uri);
//                                            if (!downloadUrl.isEmpty()) {
//                                                Map<String, Object> uValue = new HashMap<>();
//                                                uValue.put("url", downloadUrl);
//                                                databaseRef.update(uValue)
//                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                            @Override
//                                                            public void onSuccess(Void aVoid) {
//
//                                                                Toast.makeText(UpdateData.this, "Database Update", Toast.LENGTH_LONG).show();
//                                                                progressBar.setVisibility(View.GONE);
//                                                                upBtn.setText("Updated");
//                                                                finish();
//                                                            }
//                                                        })
//                                                        .addOnFailureListener(new OnFailureListener() {
//                                                            @Override
//                                                            public void onFailure(@NonNull Exception e) {
//                                                                progressBar.setVisibility(View.GONE);
//                                                                Toast.makeText(UpdateData.this, "Upload Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//
//                                            } else {
//                                                progressBar.setVisibility(View.GONE);
//
//                                                Toast.makeText(UpdateData.this, "error", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }else{
//                                            Log.d(TAG, "onSuccess: uri pai nai");
//                                            progressBar.setVisibility(View.GONE);
//                                        }
//                                    }
//                                })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                progressBar.setVisibility(View.GONE);
//                                                Log.e(TAG, "onFailure Url: ", e);
//                                            }
//                                        });

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.e(TAG, "onComplete: ", task.getException());
                                Toast.makeText(UpdateData.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "onFailure: ", e);
                            Toast.makeText(UpdateData.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "EERROROR 1111111111", Toast.LENGTH_LONG).show();

        }
    }


    //Upload Profile Image
    private void selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(UpdateData.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UpdateData.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                cropImage();

            } else {
                cropImage();
            }

        } else {
            cropImage();
        }
    }


    //Crop Activity Method
    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .start(UpdateData.this);
    }


    //Image Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Toast.makeText(this, imageUri + "", Toast.LENGTH_SHORT).show();
                upImage.setImageURI(imageUri);
                upBtn.setVisibility(View.GONE);
                upPhotoBt.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updatePhoto(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (imageUri != null) {
            imagePath.putFile(imageUri)
                    .addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            imagePath.getDownloadUrl().addOnSuccessListener(UpdateData.this, new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (uri!= null){
                                        String downloadUrl = String.valueOf(uri);
                                        updateData(downloadUrl);
                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: ", e);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                            progressBar.setVisibility(View.GONE);
                        }
                    });

        }else {
            Toast.makeText(this, "ছবি সিলেক্ট করুন ", Toast.LENGTH_LONG).show();
        }
    }

    private void updateData(String downloadUrl) {
        HashMap<String, Object> value = new HashMap<>();
        value.put("url", downloadUrl);
        databaseRef.update(value)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateData.this, "Photo Upload Complete", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "onComplete: ", task.getException());
                        }
                    }
                });
    }
}
