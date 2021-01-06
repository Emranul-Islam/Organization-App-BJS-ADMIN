package com.muhammad_sohag.admin_bjs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private Spinner cTakaSpinner;
    private String[] takaString;
    private Spinner cYearSpinner;
    private String[] yearString;

    private FirebaseFirestore data = FirebaseFirestore.getInstance();
    private CollectionReference dataBase;
    private CollectionReference chadaDataBase;

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
        cTakaSpinner = findViewById(R.id.c_taka);
        cYearSpinner = findViewById(R.id.c_year);


        getSupportActionBar().setTitle("চাঁদা");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String uid = getIntent().getStringExtra("uid");
        String nameExtra = getIntent().getStringExtra("name");
        name.setText(nameExtra);

        takaString = getResources().getStringArray(R.array.taka);

        ArrayAdapter<String> takaAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, takaString);

        cTakaSpinner.setAdapter(takaAdapter);

        yearString = getResources().getStringArray(R.array.chada_year);


        //chada year facility start:


        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);


        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, yearString);

        cYearSpinner.setAdapter(yearAdapter);

        int intValue = sharedPreferences.getInt("chada", 2);
        cYearSpinner.setSelection(intValue);


        cYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("chada", i);
                editor.apply();
                // Toast.makeText(Chada.this, "value: " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = cYearSpinner.getSelectedItem().toString();
                Intent intent = new Intent(getApplicationContext(), ChadaShow.class);
                intent.putExtra("uid", uid);
                intent.putExtra("chada", s);
                startActivity(intent);
            }
        });
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
        String taka = cTakaSpinner.getSelectedItem().toString();
        String year = cYearSpinner.getSelectedItem().toString();
        String time = String.valueOf(System.currentTimeMillis());

        dataBase = data.collection("Sodesso_List").document(uid).collection(year);


        if (!grahok.isEmpty()) {
            if (!k.isEmpty()) {


                int kromikNo = Integer.parseInt(kromikNoET.getText().toString());

                cBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                final Map<String, Object> value = new HashMap<>();
                value.put("uid", uid);
                value.put("time", time);
                value.put("taka", taka);
                value.put("grahok", grahok);
                value.put("kromik_no", kromikNo);

                final Query query = dataBase.whereEqualTo("kromik_no", kromikNo);
                query.addSnapshotListener(Chada.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots.isEmpty()) {

                            //Finally upload data Method call:
                            uploadData(value, year);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            cBtn.setClickable(true);
                            kromikNoET.setError("এই ক্রমিক নং এর চাঁদা রশিদ এড করা আছে !");
                        }
                    }
                });

            } else {
                kromikNoET.setError("ক্রমিক নং না দিলে হবে না !!");
            }
        } else {
            grahokNameET.setError("গ্রাহকের তথ্য না দিলে হবে না !!");
        }


    }


    //Finally upload data:
    private void uploadData(final Map<String, Object> value, String year) {
        dataBase.add(value)
                .addOnCompleteListener(Chada.this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            chadaDataBase = data.collection(year);
                            chadaDataBase.add(value);
                            Toast.makeText(Chada.this, "চাঁদার রশিদ সংরক্ষন করা হয়েছে !", Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            Toast.makeText(Chada.this, "সমস্যা !! চাদা সংযুক্ত হয় নি", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }




}
