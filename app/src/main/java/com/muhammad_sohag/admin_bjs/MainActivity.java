package com.muhammad_sohag.admin_bjs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.protobuf.Api;
import com.muhammad_sohag.admin_bjs.api.ApiClint;
import com.muhammad_sohag.admin_bjs.api.ApiService;
import com.muhammad_sohag.admin_bjs.blood.BloodBank;
import com.muhammad_sohag.admin_bjs.blood.BloodMemberAdd;

import static com.muhammad_sohag.admin_bjs.Const.TOPIC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("এডমিন প্যানেল");
        Log.d("TAG", "onCreate: HI SOHAG");






        FirebaseMessaging.getInstance().subscribeToTopic("notice").addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "onComplete: ");
                   // Toast.makeText(MainActivity.this, "Success to subscribe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error to subscribe", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Toast.makeText(this, "Have", Toast.LENGTH_SHORT).show();
        }
    }



    public void notification(View view) {
        Intent notice = new Intent(MainActivity.this, SendNotice.class);
        startActivity(notice);
    }

    public void bloodMember(View view) {
        Intent intent = new Intent(this, BloodBank.class);
        startActivity(intent);
    }

    public void sodesso(View view) {
        Intent intent = new Intent(this, People.class);
        startActivity(intent);
    }

    public void addSodesso(View view) {
        Intent intent = new Intent(this, AddPeople.class);
        startActivity(intent);
    }

    public void addBloodMember(View view) {
        Intent intent = new Intent(this, BloodMemberAdd.class);
        startActivity(intent);
    }
}
