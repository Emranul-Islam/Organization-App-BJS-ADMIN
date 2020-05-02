package com.muhammad_sohag.admin_bjs.blood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.muhammad_sohag.admin_bjs.R;

import java.util.HashMap;
import java.util.Map;

public class BloodUpdate extends AppCompatActivity {

    private EditText nameET,numberET,thikanaET;
    private Button upBtn,delBtn;
    private String id;
    private ProgressBar progressBar;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_update);

        getSupportActionBar().setTitle("তথ্য এডিট");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameET = findViewById(R.id.bu_name);
        numberET = findViewById(R.id.bu_number);
        thikanaET = findViewById(R.id.bu_thikana);
        upBtn = findViewById(R.id.bu_upbtn);
        delBtn = findViewById(R.id.bu_dltbtn);
        progressBar = findViewById(R.id.bu_pro);
        progressBar.setVisibility(View.VISIBLE);

        id = getIntent().getStringExtra("id");

        dataRef = firestore.collection("blood").document(id);
        dataRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    nameET.setText(documentSnapshot.getString("name"));
                    numberET.setText(documentSnapshot.getString("number"));
                    thikanaET.setText(documentSnapshot.getString("thikana"));
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(BloodUpdate.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateBlood(View view) {

        String name = nameET.getText().toString();
        String number = numberET.getText().toString();
        String thikana = thikanaET.getText().toString();

        Map<String, Object> value = new HashMap<>();
        value.put("name", name);
        value.put("number", number);
        value.put("thikana", thikana);

        progressBar.setVisibility(View.VISIBLE);
        upBtn.setClickable(true);

        dataRef.update(value).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(BloodUpdate.this, "তথ্য আপডেট হয়েছে", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    upBtn.setClickable(false);
                    Toast.makeText(BloodUpdate.this, "তথ্য আপডেট হয় নাই !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void deleteBlood(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("সত্যি কি ডিলেট করে দিবেন?")
                .setMessage("তথ্য কেটে ফেললে আর ফিরে পাওয়া যাবে না")
                .setNegativeButton("না", null)
                .setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataRef.delete();
                        Toast.makeText(BloodUpdate.this, "তথ্য মুছে ফেলা হয়েছে !", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .show();
    }
}
