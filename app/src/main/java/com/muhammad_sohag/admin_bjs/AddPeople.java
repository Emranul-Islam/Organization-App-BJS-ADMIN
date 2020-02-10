package com.muhammad_sohag.admin_bjs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPeople extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private EditText indexNumber;
    private CircleImageView photo;
    private EditText name;
    private EditText phone_number;
    private ProgressBar progressBar;
    private Button btn;
    private String peopleId = null;
    private Uri photoUri = null;
    private Uri downloadUri = null;
    private LinearLayout linearLayoutComplet;

    private HashMap<String, Object> values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);

        storageReference = FirebaseStorage.getInstance().getReference();
        indexNumber = findViewById(R.id.index_number);
        photo = findViewById(R.id.photo);
        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        linearLayoutComplet = findViewById(R.id.linear_layout_complet);
        linearLayoutComplet.setVisibility(View.GONE);

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


    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddPeople.this);
    }

    public void saveInfo(final View view) {
        btn.setText("Saveing Info...");
        btn.setClickable(false);


        progressBar.setVisibility(View.VISIBLE);
        String indexValue = indexNumber.getText().toString();
        String nameValue = name.getText().toString();
        String phoneValue = phone_number.getText().toString();

        //todo:  INDEX NAMES NUMBER ID URL
        values = new HashMap<>();
        values.put("INDEX", indexValue);
        values.put("NAMES", nameValue);
        values.put("NUMBER", phoneValue);
        values.put("ID", "");
        values.put("URL", "");

        database.collection("Sodesso_List").add(values)
                .addOnSuccessListener(this, new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        peopleId = documentReference.getId();
                        indexNumber.setVisibility(View.GONE);
                        name.setVisibility(View.GONE);
                        phone_number.setVisibility(View.GONE);

                        btn.setVisibility(View.GONE);

                        //Saving photo on database
                        if (photoUri != null && peopleId != null) {
                            final StorageReference image_path = storageReference.child("Profile_Images").child(peopleId + ".jpeg");
                            image_path.putFile(photoUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    downloadUri = uri;
                                                    Toast.makeText(AddPeople.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();

                                                    //Updating values............
                                                    if (downloadUri != null) {
                                                        Map<String, Object> newValues = new HashMap<>();
                                                        newValues.put("ID", peopleId);
                                                        newValues.put("URL", downloadUri.toString());
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        DocumentReference documentReference = database.collection("Sodesso_List").document(peopleId);
                                                        documentReference.update(newValues)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(AddPeople.this, "EveryTHing Fine", Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.GONE);
                                                                        photo.setVisibility(View.GONE);

                                                                        linearLayoutComplet.setVisibility(View.VISIBLE);
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(AddPeople.this, "Somthing is Wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        } else {
                            Toast.makeText(AddPeople.this, "Photo Select Koron", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPeople.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        btn.setClickable(true);
                        btn.setText("Try again to save");
                    }
                });

            String name = "Rashed Jailla";
            String gfName = "Resheder DJ";
            String valobasa= name+gfName;
    }

////    public void savePhoto(View view) {
////        if (photoUri != null) {
////            photoBtn.setClickable(false);
////            progressBar.setVisibility(View.VISIBLE);
////            final StorageReference image_path = storageReference.child("Profile_Images").child(peopleId + ".jpeg");
////            image_path.putFile(photoUri)
////                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
////                        @Override
////                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                            image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                @Override
////                                public void onSuccess(Uri uri) {
////                                    downloadUri = uri;
////                                    Toast.makeText(AddPeople.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
////                                    confirmBtn.setVisibility(View.VISIBLE);
////                                    progressBar.setVisibility(View.GONE);
////                                    photoBtn.setVisibility(View.GONE);
////                                    photo.setVisibility(View.GONE);
////
////                                }
////                            });
////                        }
////                    })
////                    .addOnFailureListener(this, new OnFailureListener() {
////                        @Override
////                        public void onFailure(@NonNull Exception e) {
////
////                        }
////                    });
////        } else {
////            Toast.makeText(this, "Photo Select Koron", Toast.LENGTH_SHORT).show();
////        }
//
//
//    }

//    public void Confirm(View view) {
//
//        if (downloadUri != null) {
//            confirmBtn.setClickable(false);
//            Map<String, Object> newValues = new HashMap<>();
//            newValues.put("ID", peopleId);
//            newValues.put("URL", downloadUri.toString());
//            progressBar.setVisibility(View.VISIBLE);
//            DocumentReference documentReference = database.collection("Sodesso_List").document(peopleId);
//            documentReference.update(newValues)
//                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(AddPeople.this, "EveryTHing Fine", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            confirmBtn.setVisibility(View.GONE);
//                            linearLayoutComplet.setVisibility(View.VISIBLE);
//                        }
//                    })
//                    .addOnFailureListener(this, new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "Somthing is Wrong", Toast.LENGTH_SHORT).show();
//        }
//    }
//

    public void Complete(View view) {
        finish();
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


