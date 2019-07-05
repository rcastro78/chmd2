package mx.edu.chmd2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.VideoView;

import java.util.ArrayList;

import mx.edu.chmd2.Alumnos.AlumnosActivity;
import mx.edu.chmd2.Padres.PadresActivity;
import mx.edu.chmd2.adapter.MenuAdapter;
import mx.edu.chmd2.modelos.Menu;

public class PrincipalActivity extends AppCompatActivity {
    MenuAdapter menuAdapter = null;
    ListView lstPrincipal;
    ArrayList<Menu> items = new ArrayList<>();
    VideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        videoview = findViewById(R.id.videoView);
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoview.start();
            }
        });
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_app);
        videoview.setVideoURI(uri);
        videoview.start();

        lstPrincipal = findViewById(R.id.lstPrincipal);
        llenarMenu();
        lstPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu m = (Menu)lstPrincipal.getItemAtPosition(position);
                if(m.getIdMenu()==1){
                    //Intent intent = new Intent(PrincipalActivity.this,MainActivity.class);
                    Intent intent = new Intent(PrincipalActivity.this, MenuCircularesActivity.class);
                    startActivity(intent);
                }
                //web
                if(m.getIdMenu()==2){
                    Intent intent = new Intent(PrincipalActivity.this,WebCHMDActivity.class);
                    startActivity(intent);
                }


            }
        });
    }

    private void llenarMenu(){
        items.add(new Menu(1,"Circulares",R.drawable.appmenu06));
        items.add(new Menu(2,"Mi Maguen",R.drawable.appmenu06));
        menuAdapter = new MenuAdapter(PrincipalActivity.this,items);
        lstPrincipal.setAdapter(menuAdapter);
    }
}
