package com.muhammad_sohag.admin_bjs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sNotice(View view) {
        Intent notice = new Intent(MainActivity.this, SendNotice.class);
        startActivity(notice);

    }

    public void ePeople(View view) {
        Intent editIntent = new Intent(this, People.class);
        startActivity(editIntent);

    }

    public void addPeople(View view) {
        Intent addIntent = new Intent(this, AddPeople.class);
        startActivity(addIntent);

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
}
