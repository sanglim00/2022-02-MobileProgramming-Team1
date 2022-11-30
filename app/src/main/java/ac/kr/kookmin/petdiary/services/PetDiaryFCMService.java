package ac.kr.kookmin.petdiary.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.common.reflect.TypeToken;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ac.kr.kookmin.petdiary.R;


public class PetDiaryFCMService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        // 새로운 Token 생성 시,
        super.onNewToken(token);
        Log.d("newToken", token);
        SharedPreferences preferences = getSharedPreferences("fcmToken", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcmToken", token);
        editor.commit();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManagerCompat.getNotificationChannel("PetDiary") == null) {
                NotificationChannel channel = new NotificationChannel("PetDiary", "FCM", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManagerCompat.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), "PetDiary");
        }else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();
        Map<String, String> data = message.getData();
        SharedPreferences preferences = getSharedPreferences("notification", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = preferences.getString("notifications", "");
        Type type = new TypeToken<List<ac.kr.kookmin.petdiary.models.Notification>>(){}.getType();
        List<ac.kr.kookmin.petdiary.models.Notification> notiList = gson.fromJson(json, type);
        if (notiList == null) notiList = new ArrayList<>();
        notiList.add(new ac.kr.kookmin.petdiary.models.Notification(title, body, data.get("senderUid"), data.get("postId"), data.get("postId")));
        String newNoti = gson.toJson(notiList);
        editor.putString("notifications", newNoti);
        editor.commit();

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_main);

        Notification notification = builder.build();
        notificationManagerCompat.notify(1, notification);
    }
}
