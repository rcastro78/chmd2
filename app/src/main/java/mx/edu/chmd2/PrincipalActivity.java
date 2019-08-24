package mx.edu.chmd2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.edu.chmd2.adapter.MenuAdapter;
import mx.edu.chmd2.modelos.Menu;
import mx.edu.chmd2.modelos.Usuario;

public class PrincipalActivity extends AppCompatActivity {
    MenuAdapter menuAdapter = null;
    ListView lstPrincipal;
    ArrayList<Menu> items = new ArrayList<>();
    VideoView videoview;

    static String BASE_URL;
    static String RUTA;
    SharedPreferences sharedPreferences;
    String correo,rsp;
    static String METODO_REG="registrarDispositivo.php";
    ArrayList<Usuario> usuario = new ArrayList<>();
    static String TAG=PrincipalActivity.class.getName();
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);

        videoview = findViewById(R.id.videoView);
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoview.start();
            }
        });
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_app);
        videoview.setVideoURI(uri);
        videoview.start();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sharedPreferences = this.getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        correo = sharedPreferences.getString("email","");

        FirebaseInstanceId.getInstance().getInstanceId() .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                new RegistrarDispositivoAsyncTask(correo,token,"Android OS").execute();
            }
        });



        lstPrincipal = findViewById(R.id.lstPrincipal);

        llenarMenu();
        lstPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu m = (Menu)lstPrincipal.getItemAtPosition(position);
                if(m.getIdMenu()==1){
                    //Circulares
                    Intent intent = new Intent(PrincipalActivity.this, CircularActivity.class);
                    startActivity(intent);
                }
                //Web
                if(m.getIdMenu()==2){
                    Intent intent = new Intent(PrincipalActivity.this,WebCHMDActivity.class);
                    startActivity(intent);
                }

                if(m.getIdMenu()==3){
                    //Cerrar Sesión
                    try{
                        mGoogleSignInClient.signOut();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email","");
                        editor.putString("nombre","");
                        editor.putString("userPic","");
                        editor.putString("idToken","");
                        editor.commit();
                        Intent intent = new Intent(PrincipalActivity.this,InicioActivity.class);
                        startActivity(intent);
                    }catch (Exception ex){

                    }
                }
            }
        });
    }

    private void llenarMenu(){
        items.add(new Menu(1,"Circulares",R.drawable.circulares));
        items.add(new Menu(2,"Mi Maguen",R.drawable.mi_maguen));
        items.add(new Menu(3,"Cerrar Sesión",R.drawable.appmenu09));
        menuAdapter = new MenuAdapter(PrincipalActivity.this,items);
        lstPrincipal.setAdapter(menuAdapter);
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
