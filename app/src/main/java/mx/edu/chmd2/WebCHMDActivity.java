package mx.edu.chmd2;

import android.annotation.TargetApi;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.webkit.WebViewClient;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebCHMDActivity extends AppCompatActivity {
TextView lblTextToolbar;
WebView webView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_chmd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        lblTextToolbar = toolbar.findViewById(R.id.lblTextoToolbar);
        lblTextToolbar.setText("CHMD - Web");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebCHMDActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sharedPreferences = this.getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(wvc);
        webView.loadUrl("http://moodle.chmd.edu.mx/login/index.php");


    }



    public WebViewClient wvc =  new WebViewClient() {

        @SuppressWarnings("deprecation")
        public WebResourceResponse shouldInterceptRequest(WebView view, String url){
            try {
                final String acToken = sharedPreferences.getString("idToken", "DEFAULT");

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).addHeader("Authorization" , "Bearer " + acToken)
                        .build();

                Response response = okHttpClient.newCall(request).execute();

                return new WebResourceResponse(response.header("content-type", response.body().contentType().type()),
                        response.header("content-encoding", "utf-8"),
                        response.body().byteStream());


            } catch (ClientProtocolException e) {

                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    };


}
