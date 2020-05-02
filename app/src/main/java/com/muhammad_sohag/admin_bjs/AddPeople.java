package com.muhammad_sohag.admin_bjs;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class AddPeople extends AppCompatActivity {

    private static final String TAG = "AddPeople";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    private EditText name, phone_number, phone_number2, password, email;

    private ProgressBar progressBar;
    private Button btn;
    private Spinner bloodGroupSpinner;
    private String[] bloodGroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);

        getSupportActionBar().setTitle("সদস্য যোগ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bloodGroupSpinner = findViewById(R.id.blood_group);
        bloodGroups = getResources().getStringArray(R.array.blood_group);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        phone_number = findViewById(R.id.phone_number);
        phone_number2 = findViewById(R.id.phone_number2);
        password = findViewById(R.id.password);


        progressBar = findViewById(R.id.progress_circular);

        btn = findViewById(R.id.btn);


        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, bloodGroups);
        bloodGroupSpinner.setAdapter(bloodAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }


    public void saveInfo() {

        if (!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(phone_number.getText()) && !TextUtils.isEmpty(password.getText())) {
            btn.setText("Saveing Info...");
            btn.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            final String emailValue = email.getText().toString().toLowerCase() + "@bijos.com";

            Log.d(TAG, "saveInfo: " + emailValue);

            final String passwordValue = password.getText().toString();

            auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(AddPeople.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {
                                final String uid = task.getResult().getUser().getUid();
                                Log.d(TAG, "onComplete UID: " + uid);
                                Toast.makeText(AddPeople.this, "Id Created", Toast.LENGTH_SHORT).show();
                                //  final StorageReference storageRef = storage.child("profile_image").child(uid + ".jpeg");
                                uploadData(emailValue, passwordValue, uid);

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

    private void uploadData(String emailValue, String passwordValue, final String uid) {

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
        values.put("url", null);
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

                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(AddPeople.this, "Somossa ache", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}


