package com.findclass.renan.findclass;

import com.findclass.renan.findclass.Notification.MyResponse;
import com.findclass.renan.findclass.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAATmZoPfo:APA91bH_wFlL0gA6Okd4EyLgCg7X87KHtpf7BWqdxciU1YUli1GekhWx2kyOheekQ2H2qWzzzBu7TTyf8TJkqAvzTtbxOqbku8z2QNubD_3JL13M_qDNlzScQ6SJOOuC3Ym5HXqGvuJx"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
