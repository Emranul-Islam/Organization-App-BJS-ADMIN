package com.muhammad_sohag.admin_bjs.api;

import com.muhammad_sohag.admin_bjs.notification.NotificationResponse;
import com.muhammad_sohag.admin_bjs.notification.PushNotification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {





    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAS6HhmBE:APA91bFfIk7U3zaVjD4Oj_vp5u-NSW1xBDmJEI-4ShjGLi1wqBNS_FHaVzfJ-hguqWQinsQBe8R5gjWk8e82u5QrNYE3vsTidfoF-LB15v1rVkbCPfkwYdrIw7BolpodSjB3cX2wmRxq"
            }
    )
    @POST("send")
    Call<ResponseBody> sendNotification(@Body PushNotification pushNotification);

}

