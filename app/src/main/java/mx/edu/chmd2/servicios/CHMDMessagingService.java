package mx.edu.chmd2.servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import mx.edu.chmd2.CircularActivity;
import mx.edu.chmd2.CircularDetalleActivity;
import mx.edu.chmd2.InicioActivity;
import mx.edu.chmd2.R;
import mx.edu.chmd2.modelosDB.DBNotificacion;

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
    static String METODO_REG="registrarDispositivo.php";
    SharedPreferences sharedPreferences;

    String title="",body="",idCircular="",tituloCircular="",token="";
    String strTipo="",rsp="",correoRegistrado="",fechaCircularNotif="";

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
        String versionRelease = Build.VERSION.RELEASE;
        token = s;
        new RegistrarDispositivoAsyncTask(correoRegistrado,token,"Android OS "+versionRelease).execute();
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
        fechaCircularNotif = message.getData().get("fechaCircularNotif");
        if(message.getNotification()!=null){
            notificacionRecibida = true;
            strTipo = message.getData().get("strTipo");
            title = message.getData().get("title");
            body = message.getData().get("body");
            idCircular = message.getData().get("idCircular");
            fechaCircularNotif = message.getData().get("fechaCircularNotif");
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
        if (notificationManager != null)
            notificationManager.createNotificationChannel(adminChannel);
    }


    private void sendNotification(RemoteMessage r, String tipo) {

        Intent intent = null;
        PendingIntent pendingIntent = null;
        if (tipo.equalsIgnoreCase(CIRCULAR)) {
            if(!notificacionRecibida){
                intent = new Intent(this, CircularDetalleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("idCircularNotif",idCircular);
                intent.putExtra("fechaCircularNotif",fechaCircularNotif);

                intent.putExtra("viaNotif",1);
                intent.putExtra("tituloCircular",tituloCircular);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                //Con esto colocamos el badge
                ShortcutBadger.applyCount(getApplicationContext(), 1);

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String hoy=sdf.format(c.getTime());
                DBNotificacion notificacion = new DBNotificacion();
                notificacion.estado=0;
                notificacion.idCircular=idCircular;
                notificacion.titulo = title;
                notificacion.descripcion="";
                notificacion.recibido=hoy;
                notificacion.save();
                //Toast.makeText(getApplicationContext(),guardado+"",Toast.LENGTH_LONG).show();
            }else{
                //Aquí se recibió por fcm
                intent = new Intent(this, CircularDetalleActivity.class);
                intent.putExtra("idCircularNotif",idCircular);
                intent.putExtra("fechaCircularNotif",idCircular);

                intent.putExtra("tituloCircular",title);
                intent.putExtra("viaNotif",1);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                ShortcutBadger.applyCount(getApplicationContext(), 1);
                /*Guardar las notificaciones recibidas*/
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String hoy=sdf.format(c.getTime());
                DBNotificacion notificacion = new DBNotificacion();
                notificacion.estado=0;
                notificacion.idCircular=idCircular;
                notificacion.titulo = title;
                notificacion.descripcion="";
                notificacion.recibido=hoy;
                notificacion.save();
                //Toast.makeText(getApplicationContext(),guardado+"",Toast.LENGTH_LONG).show();
            }


        }

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icono_notificaciones)//notification icon
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.icono_grande))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setNumber(1)

                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }


    private class RegistrarDispositivoAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String correo;
        private String device_token;
        private String plataforma;


        public RegistrarDispositivoAsyncTask(String correo, String device_token, String plataforma) {
            this.correo = correo;
            this.device_token = device_token;
            this.plataforma = plataforma;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RESPONSE","ejecutando...");
        }

        public void registraDispositivo(){
            HttpClient httpClient;
            HttpPost httppost;
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL+RUTA+METODO_REG);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("correo",correo));
                nameValuePairs.add(new BasicNameValuePair("device_token",device_token));
                nameValuePairs.add(new BasicNameValuePair("plataforma",plataforma));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httppost);
                int responseCode = response.getStatusLine().getStatusCode();
                Log.d("RESPONSE", ""+responseCode);
                switch(responseCode) {
                    case 200:
                        HttpEntity entity = response.getEntity();
                        if(entity != null) {
                            String responseBody = EntityUtils.toString(entity);
                            rsp=responseBody;
                        }
                        break;
                }
                Log.d("RESPONSE", rsp);




            }catch (Exception e){
                Log.d("RESPONSE",e.getMessage());
            }



        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.d("RESPONSE","ejecutado.-");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            registraDispositivo();
            return null;
        }
    }


}
