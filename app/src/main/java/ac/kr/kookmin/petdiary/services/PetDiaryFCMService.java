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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();
        notificationManagerCompat.notify(1, notification);
    }
}
