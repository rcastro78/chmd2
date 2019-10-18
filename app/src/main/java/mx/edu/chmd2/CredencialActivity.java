package mx.edu.chmd2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class CredencialActivity extends AppCompatActivity {
ImageView imgFotoPadre,imgQR;
TextView lblNombrePadre,lblPadre,lblNumFam;
private static String BASE_URL_FOTO="http://chmd.chmd.edu.mx:65083/CREDENCIALES/padres/";
    static String BASE_URL;
    static String RUTA;
    SharedPreferences sharedPreferences;
    String fotoPadre="";
    int idUsuario=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credencial);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CredencialActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView lblEncabezado = toolbar.findViewById(R.id.lblTextoToolbar);
        lblEncabezado.setText("Credencial de padre");
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/GothamRoundedMedium_21022.ttf");
        lblNombrePadre = findViewById(R.id.lblNombrePadre);
        lblPadre = findViewById(R.id.lblPadre);
        lblNumFam = findViewById(R.id.lblNumFam);
        imgFotoPadre =findViewById(R.id.imgFotoPadre);
        imgQR =findViewById(R.id.imgQR);
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);
        sharedPreferences = getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        lblNombrePadre.setText(sharedPreferences.getString("nombreCredencial",""));
        lblNumFam.setText("Número de familia: "+sharedPreferences.getString("numeroCredencial",""));
        lblPadre.setText(sharedPreferences.getString("responsableCredencial",""));

        lblNombrePadre.setTypeface(tf);
        lblPadre.setTypeface(tf);
        lblNumFam.setTypeface(tf);
        String fotoUrl = sharedPreferences.getString("fotoCredencial","");
        if (fotoUrl.length()<5){
            //No tiene foto
            fotoUrl = "http://chmd.chmd.edu.mx:65083/CREDENCIALES/padres/sinfoto.png";
        }


        //Cargar la foto del padre
        Glide.with(this)
                .load(BASE_URL_FOTO+fotoUrl)
                .into(imgFotoPadre);

        //generar QR a partir de la URL
        try{
            Bitmap bmp = crearQR(BASE_URL_FOTO+fotoUrl);
            imgQR.setImageBitmap(bmp);
        }catch(Exception ex){
        }



        /*

*  editor.putString("idUsuario",idUsuario);
                                editor.putString("nombre",nombre);
                                editor.putString("numero",numero);
                                editor.putString("telefono",telefono);
                                editor.putString("responsable",responsable);
                                editor.putString("familia",familia);
* */

    }


    Bitmap crearQR(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 200, 200, null);
        } catch (IllegalArgumentException iae) {

            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 200, 0, 0, w, h);
        return bitmap;
    }

}