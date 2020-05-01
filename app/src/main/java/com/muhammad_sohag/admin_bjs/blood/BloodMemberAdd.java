package com.muhammad_sohag.admin_bjs.blood;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.muhammad_sohag.admin_bjs.R;

import java.util.HashMap;

public class BloodMemberAdd extends AppCompatActivity {
    private EditText nameET, numberET, thikanaET;
    private Spinner bloodGroup;
    private Button save;
    private String[] bGroupStr;
    private ProgressBar progressBar;

    private int size;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference dataRef = database.collection("blood");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_member_add);

        nameET = findViewById(R.id.b_name);
        numberET = findViewById(R.id.b_number);
        thikanaET = findViewById(R.id.b_thikana);
        bloodGroup = findViewById(R.id.b_blood_group);
        save = findViewById(R.id.b_save);
        progressBar = findViewById(R.id.b_progressbar);
        bGroupStr = getResources().getStringArray(R.array.blood_group);


        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this, R.layout.blood_group_layout, R.id.blood_group_layout_text, bGroupStr);
        bloodGroup.setAdapter(bloodAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setClickable(false);
                final String name = nameET.getText().toString();
                final String number = numberET.getText().toString();
                final String thikana = thikanaET.getText().toString();
                final String blood = bloodGroup.getSelectedItem().toString();



                final Query query = dataRef.whereEqualTo("number", number);
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            numberET.setError("এই নাম্বার ইতিমধ্যে আছে !");

                            save.setClickable(true);
                        }else {
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("name", name);
                            data.put("number", number);
                            data.put("thikana", thikana);
                            data.put("blood", blood);

                            dataRef.add(data)
                                    .addOnCompleteListener(BloodMemberAdd.this, new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isComplete()) {
                                                progressBar.setVisibility(View.GONE);
                                                nameET.setText("");
                                                numberET.setText("");
                                                thikanaET.setText("");
                                                save.setClickable(true);
                                                Toast.makeText(BloodMemberAdd.this, "তথ্য সংযুক্ত হয়েছে", Toast.LENGTH_LONG).show();
                                            } else {
                                                save.setClickable(true);
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(BloodMemberAdd.this, "তথ্য যোগ হয় নি", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }
                    }
                });




            }
        });

    }
}
