package com.findclass.renan.findclass.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.findclass.renan.findclass.MessageActivity;
import com.findclass.renan.findclass.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireaseMessaging extends FirebaseMessagingService {

    private String adminId = "n4XdU5nhXEdCFBN0byQs7NtWRO93";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");

        SharedPreferences preferences = getSharedPreferences("Prefs",MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser","none");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null && sented.equals(firebaseUser.getUid()))
        {
            if (!currentUser.equals(sented))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    if (firebaseUser.getUid().equals(adminId))
                    {
                        sendManegerOreoNotification(remoteMessage);
                    }
                    else {
                        sendOreoNotification(remoteMessage);
                    }
                }
                else {
                    if (firebaseUser.getUid().equals(adminId))
                    {
                        sendManegerNotification(remoteMessage);
                    }
                    else {
                        sendNotification(remoteMessage);
                    }
                }
            }

        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon);

        int i = 0;
        if (j>0){i=j;}

        oreoNotification.getManager().notify(i,builder.build());
    }


    private void sendManegerOreoNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent broadcastIntent = new Intent(this, YesNotificationReceiver.class);
        Intent broadcastIntent1 = new Intent(this,NoNotificationReceiver.class);

        broadcastIntent1.putExtra("toastMessage",getResources().getString(R.string.Reject_Request));
        broadcastIntent1.putExtra("user",user);
        broadcastIntent1.putExtra("j",j);
        PendingIntent actionRejectIntent = PendingIntent.getBroadcast(this,j,broadcastIntent1,PendingIntent.FLAG_ONE_SHOT);

        broadcastIntent.putExtra("toastMessage",getResources().getString(R.string.Approved_Request));
        broadcastIntent.putExtra("user",user);
        broadcastIntent.putExtra("j",j);
        PendingIntent actionApprovedIntent = PendingIntent.getBroadcast(this,j,broadcastIntent,PendingIntent.FLAG_ONE_SHOT);


        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getManegerOreoNotification(title,body,pendingIntent,actionApprovedIntent,actionRejectIntent,defaultSound,icon);

        int i = 0;
        if (j>0){i=j;}

        oreoNotification.getManager().notify(i,builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        //int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.putExtra("user",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentIntent(pendingIntent)
                .setSound(defaultSound)
                .setAutoCancel(true);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

       /* int i = 0;
        if (j>0){i= j;}*/

        noti.notify(1,builder.build());
    }

    private void sendManegerNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j =1; /*Integer.parseInt(user.replaceAll("[\\D]",""));*/
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent broadcastIntent = new Intent(this, YesNotificationReceiver.class);
        Intent broadcastIntent1 = new Intent(this,NoNotificationReceiver.class);


        broadcastIntent1.putExtra("toastMessage",getResources().getString(R.string.Reject_Request));
        broadcastIntent1.putExtra("user",user);
        broadcastIntent1.putExtra("j",j);
        PendingIntent actionRejectIntent = PendingIntent.getBroadcast(this,j,broadcastIntent1,PendingIntent.FLAG_ONE_SHOT);

        broadcastIntent.putExtra("toastMessage",getResources().getString(R.string.Approved_Request));
        broadcastIntent.putExtra("user",user);
        broadcastIntent.putExtra("j",j);
        PendingIntent actionApprovedIntent = PendingIntent.getBroadcast(this,j,broadcastIntent,PendingIntent.FLAG_ONE_SHOT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentIntent(pendingIntent)
                .setSound(defaultSound)
                .addAction(R.drawable.ic_v,"Approve",actionApprovedIntent)
                .addAction(R.drawable.ic_x,"Reject",actionRejectIntent)
                .setAutoCancel(true);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j>0){i= j;}

        noti.notify(1,builder.build());
    }

}
