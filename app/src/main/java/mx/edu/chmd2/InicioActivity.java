package mx.edu.chmd2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.edu.chmd2.validaciones.ValidarPadreActivity;

public class InicioActivity extends AppCompatActivity {
ImageButton fabLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    static int RC_SIGN_IN=999;
    SharedPreferences sharedPreferences;
    static String TAG = InicioActivity.class.getName();
    VideoView videoview;
    int valida;
    static String BASE_URL;
    static String RUTA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);

        sharedPreferences = this.getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        valida = sharedPreferences.getInt("cuentaValida",0);
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

        fabLogin = findViewById(R.id.fabLogin);
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){

            if(valida==1) {
                Intent intent = new Intent(InicioActivity.this, ValidarPadreActivity.class);
                startActivity(intent);
            }else{
                mGoogleSignInClient.signOut();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email",account.getEmail());
            editor.putString("nombre",account.getDisplayName());
            String userPic = "";
            //Al venir la pic vacía daba error, se cerraba luego de escoger la cuenta.
            try{
                userPic = account.getPhotoUrl().toString();
            }catch (Exception ex){
                userPic = "";
            }
            editor.putString("userPic",userPic);
            editor.putString("idToken",account.getIdToken());

            editor.commit();

            Intent intent = new Intent(InicioActivity.this, ValidarPadreActivity.class);
            startActivity(intent);

        }else{
            Log.w(TAG, "No se pudo iniciar sesión");
        }

    }











}
