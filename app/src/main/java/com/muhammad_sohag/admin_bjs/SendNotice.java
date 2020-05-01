package com.muhammad_sohag.admin_bjs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendNotice extends AppCompatActivity {

    private EditText names;
    private EditText massage;
    private Button sBtn;
    private ProgressBar progressBar;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference dataRef = firestore.collection("Notice");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);

        names = findViewById(R.id.s_name);
        massage = findViewById(R.id.s_massage);
        progressBar = findViewById(R.id.sProg);
        sBtn = findViewById(R.id.sBtn);
        getSupportActionBar().setTitle("নোটিশ");

        final Map<String, Object> value = new HashMap<>();


        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBtn.setClickable(false);
                String nameV = names.getText().toString();
                String mValue = massage.getText().toString();

                String time = String.valueOf(System.currentTimeMillis());

                value.put("name", nameV);
                value.put("massage", mValue);
                value.put("time", time);

                progressBar.setVisibility(View.VISIBLE);

                dataRef.add(value).addOnCompleteListener(SendNotice.this, new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SendNotice.this, "নোটিশ পাঠানো হয়েছে ", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            names.setText("");
                            massage.setText("");
                            sBtn.setClickable(true);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            sBtn.setClickable(true);
                            Toast.makeText(SendNotice.this, "Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


    }
}
