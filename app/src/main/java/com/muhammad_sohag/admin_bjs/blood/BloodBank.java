package com.muhammad_sohag.admin_bjs.blood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.muhammad_sohag.admin_bjs.R;

public class BloodBank extends AppCompatActivity {
    TextView blood;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference dataRef = database.collection("blood");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        blood = findViewById(R.id.bloodText);
        blood.setText("Loading......");

        dataRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()){
                    StringBuffer dataS = new StringBuffer();

                    String bloodDetails = "";
                    for (QueryDocumentSnapshot documentSnapshots: queryDocumentSnapshots){
                        dataS.append(documentSnapshots.getString("name")).append("\n").append(documentSnapshots.getString("number")).append("\n").append(documentSnapshots.getString("thikana")).append("\n").append(documentSnapshots.getString("blood")).append("\n\n\n");
                    }
                    blood.setText(dataS);
                }else{
                    blood.setText("Data nai");
                }
            }
        });


    }
}
