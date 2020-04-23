package com.muhammad_sohag.admin_bjs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Chada extends AppCompatActivity {
    private EditText grahokNameET, kromikNoET;
    private TextView name;
    private Button cBtn, detailsBtn;
    private ProgressBar progressBar;

    private FirebaseFirestore data = FirebaseFirestore.getInstance();
    private CollectionReference dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chada);

        cBtn = findViewById(R.id.c_btn);
        detailsBtn = findViewById(R.id.c_show_detalis);
        name = findViewById(R.id.c_name);
        grahokNameET = findViewById(R.id.c_grahok_name);
        kromikNoET = findViewById(R.id.c_kromik_no);
        progressBar = findViewById(R.id.c_progress);


        final String uid = getIntent().getStringExtra("uid");
        String nameExtra = getIntent().getStringExtra("name");

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChadaShow.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });


        dataBase = data.collection("Sodesso_List").document(uid).collection("Chada_2020");


        name.setText(nameExtra);

        cBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Chada.this);
                builder.setTitle("আপনি কি সব ঠিক মত দিয়েছেন?")
                        .setNegativeButton("না", null)
                        .setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Modifies all data method call:
                                modifiesChada(uid);


                            }
                        })
                        .show();


            }
        });


    }

    //Modifies all data:
    private void modifiesChada(String uid) {

        String grahok = grahokNameET.getText().toString();
        String k = kromikNoET.getText().toString();
        String time = String.valueOf(System.currentTimeMillis());

        if (!grahok.isEmpty()){
            if (!k.isEmpty()) {


                int kromikNo = Integer.parseInt(kromikNoET.getText().toString());

                cBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                final Map<String, Object> value = new HashMap<>();
                value.put("uid", uid);
                value.put("time", time);
                value.put("grahok", grahok);
                value.put("kromik_no", kromikNo);

                final Query query = dataBase.whereEqualTo("kromik_no", kromikNo);
                query.addSnapshotListener(Chada.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.isEmpty()){

                            //Finally upload data Method call:
                            uploadData(value);
                        }else {
                            progressBar.setVisibility(View.GONE);
                            cBtn.setClickable(true);
                            kromikNoET.setError("এই ক্রমিক নং এর চাঁদা রশিদ এড করা আছে !");
                        }
                    }
                });

            }else {
                kromikNoET.setError("ক্রমিক নং না দিলে হবে না !!");
            }
        }else{
            grahokNameET.setError("গ্রাহকের তথ্য না দিলে হবে না !!");
        }



    }


    //Finally upload data:
    private void uploadData(Map<String, Object> value) {
        dataBase.add(value)
                .addOnCompleteListener(Chada.this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Chada.this, "চাঁদার রশিদ সংরক্ষন করা হয়েছে !", Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            Toast.makeText(Chada.this, "সমস্যা !! চাদা সংযুক্ত হয় নি", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
