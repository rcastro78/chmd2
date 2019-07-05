package mx.edu.chmd2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class InicioActivity extends AppCompatActivity {
FloatingActionButton fabLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    static int RC_SIGN_IN=999;
    SharedPreferences sharedPreferences;
    static String TAG = InicioActivity.class.getName();
    VideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        sharedPreferences = this.getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);

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
            Intent intent = new Intent(InicioActivity.this,PrincipalActivity.class);
            startActivity(intent);
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
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email",account.getEmail());
            editor.putString("nombre",account.getDisplayName());
            editor.putString("userPic",account.getPhotoUrl().toString());
            editor.putString("idToken",account.getIdToken());

            editor.commit();
            Intent intent = new Intent(InicioActivity.this,PrincipalActivity.class);
            startActivity(intent);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }



}
