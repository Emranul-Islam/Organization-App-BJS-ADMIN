package com.muhammad_sohag.admin_bjs;

import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;
import com.muhammad_sohag.admin_bjs.api.ApiClint;
import com.muhammad_sohag.admin_bjs.api.ApiService;
import com.muhammad_sohag.admin_bjs.notification.NotificationData;
import com.muhammad_sohag.admin_bjs.notification.NotificationResponse;
import com.muhammad_sohag.admin_bjs.notification.PushNotification;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.muhammad_sohag.admin_bjs.Const.TOPIC;

public class SendNotice extends AppCompatActivity {

    private static final String TAG = "SendNotice";

    private EditText names;
    private EditText massage;
    private Button sBtn;
    private ProgressBar progressBar;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference dataRef = firestore.collection("Notice");
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);


        api = ApiClint.getRetrofit().create(ApiService.class);


        names = findViewById(R.id.s_name);
        massage = findViewById(R.id.s_massage);
        progressBar = findViewById(R.id.sProg);
        sBtn = findViewById(R.id.sBtn);

        getSupportActionBar().setTitle("নোটিশ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Map<String, Object> value = new HashMap<>();


        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBtn.setClickable(false);
                final String nameV = names.getText().toString();
                final String mValue = massage.getText().toString();



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
                            sendNotice(nameV,mValue);
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

    private void sendNotice(String title, String body) {
        try {

            PushNotification pushNotification = new PushNotification(new NotificationData(title, body), TOPIC);
            Call<ResponseBody> responseBodyCall = api.sendNotification(pushNotification);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SendNotice.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SendNotice.this, "Faild", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SendNotice.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "sendNotice: "+e.getMessage());
        }
    }
}
