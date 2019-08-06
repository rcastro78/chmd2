package mx.edu.chmd2.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import mx.edu.chmd2.CircularActivity;
import mx.edu.chmd2.CircularDetalleActivity;
import mx.edu.chmd2.InicioActivity;
import mx.edu.chmd2.R;

public class CHMDMessagingService extends FirebaseMessagingService {
    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private NotificationManager notificationManager;
    boolean notificacionRecibida=false;
    private static String CHAT="chat";
    private static String FEED="noticias";
    private static String CIRCULAR="circular";


    static String BASE_URL;
    static String RUTA;
    SharedPreferences sharedPreferences;

    String title="",body="",idCircular="",tituloCircular="",token="";
    String strTipo="",rsp="",correoRegistrado="";

    @Override
    public void onCreate() {
        super.onCreate();
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);
        sharedPreferences = getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        correoRegistrado = sharedPreferences.getString("correoRegistrado","");
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("TOKEN",s);
        //guardar el token del dispositivo en la tabla correspondiente.
        token = s;

    }




    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }
        int tipo=0;
        //el strTipo viene en el data de la notificación
        strTipo = message.getData().get("strTipo");
        title = message.getData().get("title");
        body = message.getData().get("body");
        idCircular = message.getData().get("idCircular");
        tituloCircular = message.getData().get("tituloCircular");
        if(message.getNotification()!=null){
            notificacionRecibida = true;
            strTipo = message.getData().get("strTipo");
            title = message.getData().get("title");
            body = message.getData().get("body");
            idCircular = message.getData().get("idCircular");
            tituloCircular = message.getData().get("tituloCircular");

        }
        sendNotification(message,strTipo);



    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = ADMIN_CHANNEL_ID;
        String adminChannelDescription = ADMIN_CHANNEL_ID;

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


    private void sendNotification(RemoteMessage r, String tipo) {

        Intent intent = null;
        PendingIntent pendingIntent = null;
        if (tipo.equalsIgnoreCase(CIRCULAR)) {
            if(!notificacionRecibida){
                intent = new Intent(this, CircularDetalleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("idCircular",idCircular);
                intent.putExtra("tituloCircular",tituloCircular);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            }else{
                intent = new Intent(this, InicioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            }


        }

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.appmenu05)//notification icon
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }




}
