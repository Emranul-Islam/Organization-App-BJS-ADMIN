package com.muhammad_sohag.admin_bjs.blood;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.muhammad_sohag.admin_bjs.R;
import com.muhammad_sohag.admin_bjs.adapter.BloodAdapter;
import com.muhammad_sohag.admin_bjs.model.BloodModel;

import java.util.ArrayList;
import java.util.List;

public class BloodBank extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference dataRef = database.collection("blood");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        getSupportActionBar().setTitle("রক্তদাতাগন");
        recyclerView = findViewById(R.id.b_recycler);
        progressBar = findViewById(R.id.b_progressbar);

        final List<BloodModel> bloodModelsList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BloodAdapter bloodAdapter = new BloodAdapter(BloodBank.this, bloodModelsList);

        recyclerView.setAdapter(bloodAdapter);

        progressBar.setVisibility(View.VISIBLE);




        dataRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {

                    for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                        //String name, String number, String thikana, String blood, String id

                        BloodModel bloodModel = new BloodModel(documentSnapshots.getString("name"), documentSnapshots.getString("number")
                                , documentSnapshots.getString("thikana"), documentSnapshots.getString("blood"), documentSnapshots.getId());

                        bloodModelsList.add(bloodModel);
                    }
                    bloodAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(BloodBank.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }
}
