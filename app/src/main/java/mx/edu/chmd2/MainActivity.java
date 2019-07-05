package mx.edu.chmd2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
DrawerLayout drawerLayout;
ImageView imgPerfil;
TextView lblNombre,lblEmail,lblTextToolbar;
NavigationView navigationView;
SharedPreferences sharedPreferences;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        lblTextToolbar = toolbar.findViewById(R.id.lblTextoToolbar);
        lblTextToolbar.setText("CHMD - Circulares");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sharedPreferences = this.getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem menuItem = navigationView.getMenu().getItem(0);
        onNavigationItemSelected(menuItem);
        menuItem.setChecked(true);

        drawerLayout.addDrawerListener(this);

        View header = navigationView.getHeaderView(0);
        imgPerfil = header.findViewById(R.id.imgPerfil);
        lblNombre = header.findViewById(R.id.lblNombreUsuario);
        lblEmail = header.findViewById(R.id.lblEmailUsuario);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .fitCenter()
                .skipMemoryCache(true)
                .transform(new CircularTransformation())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(400, 400)
                .format(DecodeFormat.DEFAULT);

        Glide.with(imgPerfil.getContext())
                .load(sharedPreferences.getString("userPic",""))
                .apply(requestOptions)
                .into(imgPerfil);

        lblNombre.setText(sharedPreferences.getString("nombre","Nombre de usuario"));
        lblEmail.setText(sharedPreferences.getString("email","Email de usuario"));





    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_inicio:
                break;
            case R.id.nav_cerrar:

                mGoogleSignInClient.signOut()

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"No se pudo desconectar: "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Cerrando la sesión",Toast.LENGTH_LONG).show();
                               Intent intent = new Intent(MainActivity.this,InicioActivity.class);
                               startActivity(intent);
                            }


                        });



                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }



    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {

    }

    @Override
    public void onDrawerStateChanged(int i) {

    }


    public class CircularTransformation extends BitmapTransformation {

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(null, toTransform);
            circularBitmapDrawable.setCircular(true);
            Bitmap bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            circularBitmapDrawable.setBounds(0, 0, outWidth, outHeight);
            circularBitmapDrawable.draw(canvas);
            return bitmap;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {}

    }
}