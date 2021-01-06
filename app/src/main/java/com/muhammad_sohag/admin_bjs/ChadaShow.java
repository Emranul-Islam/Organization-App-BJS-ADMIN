package com.muhammad_sohag.admin_bjs;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class ChadaShow extends AppCompatActivity {

    private TextView chadaDetails, totalTV;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference dataRef;
    private StringBuilder data;
    private int total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chada_show);

        getSupportActionBar().setTitle("চাঁদার লিস্ট");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        totalTV = findViewById(R.id.c_total);
        chadaDetails = findViewById(R.id.c_show);
        chadaDetails.setText("ডাটা নাই !");

        showData();




    }

    private void showData() {
        final String chada_year = getIntent().getStringExtra("chada");
        final String uid = getIntent().getStringExtra("uid");

        dataRef = firestore.collection("Sodesso_List").document(uid).collection(chada_year);
        dataRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    total = queryDocumentSnapshots.size();

                    data = new StringBuilder();
                    for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {

                        //Format Date:
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(documentSnapshots.getString("time")));
                        String time = DateFormat.format("dd-MM-yyyy hh:mm aa", cal).toString();

                        data.append("ক্রমিক নং: ").append(documentSnapshots.get("kromik_no").toString()).append("\nগ্রাহকের নাম: ")
                                .append(documentSnapshots.getString("grahok")).append("\nটাকার পরিমান: ").append(documentSnapshots.getString("taka")).append("\nসময়: ").append(time)
                                .append("\n --------------------------\n");

                    }

                    totalTV.setText(chada_year +" এর \nমোট চাঁদা দেয়া হইছে: "+total+" টা,\nবাকি আছে: "+(12-total)+" টা।" );

                    chadaDetails.setText(data.toString());
                } else {
                    chadaDetails.setText("ডাটা নেই/লোড হচ্ছে না...।");
                }
            }
        });

    }

    public void refresh(View view) {

        chadaDetails.setText("রিফ্রেশ হচ্ছে !!");
        showData();



    }
}
