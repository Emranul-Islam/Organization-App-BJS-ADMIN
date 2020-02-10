package com.muhammad_sohag.admin_bjs;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class People extends AppCompatActivity {
    RecyclerView peopleRecyclerView;
    private FirebaseFirestore database=FirebaseFirestore.getInstance();
    private CollectionReference databaseRaf = database.collection("Sodesso_List");
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.VISIBLE);
        peopleRecyclerView = findViewById(R.id.people_recycler_view);
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<PeopleModel> values = new ArrayList<>();
        final PeopleAdapter peopleAdapter = new PeopleAdapter(this, values);
        peopleRecyclerView.setAdapter(peopleAdapter);

        //todo:  INDEX NAMES NUMBER ID URL Sodesso_List


        databaseRaf.orderBy("INDEX").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Toast.makeText(People.this, "Somthing is Wronng", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        values.add(new PeopleModel(documentSnapshot.getString("URL"),documentSnapshot.getString("NAMES"),
                                documentSnapshot.getString("ID"),documentSnapshot.getString("NUMBER")));
                    }
                    peopleAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        values.add(new PeopleModel("image", "Name","id","number"));



        Toast.makeText(this, "Open", Toast.LENGTH_SHORT).show();


    }
}
