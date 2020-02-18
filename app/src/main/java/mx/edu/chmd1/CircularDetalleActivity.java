package mx.edu.chmd1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

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
import com.bitly.Bitly;
import com.bitly.Error;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import mx.edu.chmd1.modelos.Circular;
import mx.edu.chmd1.modelosDB.DBCircular;
import mx.edu.chmd1.utilerias.OnSwipeTouchListener;

public class CircularDetalleActivity extends AppCompatActivity {
    static String METODO="getCircularId2.php";
    static String METODO_CIRCULAR="getCircularId.php";
    static String METODO2="getCircularesUsuarios.php";
    static String METODO_REG="leerCircular.php";
    static String METODO_NOLEER="noleerCircular.php";
    static String METODO_DEL="eliminarCircular.php";
    static String METODO_FAV="favCircular.php";
    static String BASE_URL;
    static String RUTA;
    private OnSwipeTouchListener onSwipeTouchListener;
    SharedPreferences sharedPreferences;
    TextView lblTitulo,lblTitulo2,lblEncabezado,lblFecha;
    WebView wvwDetalleCircular;
    String idCircular,contenidoCircular;
    String idUsuario,rsp;
    String temaIcs,fechaIcs,ubicacionIcs,horaInicioIcs,horaFinIcs;
    ImageView imgEliminarSeleccionados,imgMoverFavSeleccionados,imgMoverNoLeidos,imgCompartir,imgCalendario;
    ImageView btnSiguiente,btnAnterior;
    Typeface t;
    String t2;
    int pos=0;
    ArrayList<Circular> circulares = new ArrayList<>();
    ArrayList<Circular> circularesId = new ArrayList<>();
    public boolean hayConexion() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_detalle);
        idCircular = getIntent().getStringExtra("idCircular");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(hayConexion())
            Bitly.initialize(this, "9bd1d4e87ce38e38044ff0c7c60c07c90483e2a4");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CircularDetalleActivity.this, CircularActivity.class);
                startActivity(intent);
                finish();
            }
        });
        t = Typeface.createFromAsset(getAssets(),"fonts/GothamRoundedBold_21016.ttf");
        lblEncabezado = toolbar.findViewById(R.id.lblTextoToolbar);
        lblEncabezado.setText("Circular");
        lblEncabezado.setTypeface(t);
        wvwDetalleCircular = findViewById(R.id.wvwDetalleCircular);
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);

        sharedPreferences = getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        lblTitulo = findViewById(R.id.lblTitulo);
        lblTitulo2 = findViewById(R.id.lblTitulo2);
        lblFecha = findViewById(R.id.lblFecha);
        lblTitulo.setTypeface(t);
        lblTitulo2.setTypeface(t);
        lblFecha.setTypeface(t);
        lblFecha.setText(getIntent().getStringExtra("fechaCircular"));
        imgMoverFavSeleccionados = findViewById(R.id.imgMoverFavSeleccionados);
        imgEliminarSeleccionados = findViewById(R.id.imgEliminarSeleccionados);
        imgMoverNoLeidos = findViewById(R.id.imgMoverNoLeidos);
        imgCalendario = findViewById(R.id.imgCalendario);
        imgCompartir = findViewById(R.id.imgMoverComp);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnAnterior = findViewById(R.id.btnAnterior);
        String idUsuarioCredencial="";

        final int viaNotif = getIntent().getIntExtra("viaNotif",0);
        if(viaNotif==1){
            int idCirc = Integer.parseInt(getIntent().getStringExtra("idCircularNotif"));
            lblFecha.setText(getIntent().getStringExtra("fechaCircularNotif"));
            getCircularId(idCirc);
           }

        try{
            idUsuarioCredencial = sharedPreferences.getString("idUsuarioCredencial","0");
            idUsuario = idUsuarioCredencial;
            contenidoCircular = getIntent().getStringExtra("contenidoCircular");
            /*temaIcs = getIntent().getStringExtra("temaIcs");
            fechaIcs = getIntent().getStringExtra("fechaIcs");
            ubicacionIcs = getIntent().getStringExtra("ubicaIcs");
            horaInicioIcs = getIntent().getStringExtra("horaInicioIcs");
            horaFinIcs = getIntent().getStringExtra("horaFinIcs");*/
        }catch (Exception ex){

        }




        if(hayConexion())
            getCirculares(idUsuarioCredencial);
        else
            leeCirculares(Integer.parseInt(idUsuarioCredencial));

        imgCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CircularDetalleActivity.this);
                builder.setTitle("Compartir Circular");
                builder.setMessage("¿Desea compartir esta circular?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       share("https://www.chmd.edu.mx/WebAdminCirculares/ws/getCircularId2.php?id=300");
                        //Compartir

                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        imgEliminarSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CircularDetalleActivity.this);
                builder.setTitle("Eliminar Circular");
                builder.setMessage("¿Estás seguro que quieres eliminar esta circular?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new EliminaAsyncTask(idCircular,idUsuario).execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        imgMoverFavSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CircularDetalleActivity.this);
                builder.setTitle("Mover a favoritos");
                builder.setMessage("¿Estás seguro que quieres mover esta circular a favoritas?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new FavAsyncTask(idCircular,idUsuario).execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        imgMoverNoLeidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CircularDetalleActivity.this);
                builder.setTitle("Mover a favoritos");
                builder.setMessage("¿Estás seguro que quieres mover esta circular a no leídas?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new NoLeerAsyncTask(idCircular,idUsuario).execute();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        imgCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hayConexion()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CircularDetalleActivity.this);
                    builder.setTitle("Calendario");
                    builder.setMessage("¿Estás seguro que quieres agregar este evento a tu calendario?");
                    builder.setPositiveButton("Establecer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String startDate = fechaIcs + " " + horaInicioIcs;
                                String endDate = fechaIcs + " " + horaFinIcs;
                                String title = temaIcs;
                                String description = "";
                                String location = ubicacionIcs;

                                if(!fechaIcs.equalsIgnoreCase("")){
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date sdate = sdf.parse(startDate);
                                    Date edate = sdf.parse(endDate);

                                    long startTime = sdate.getTime();
                                    long endTime = edate.getTime();

                                    Intent intent = new Intent(Intent.ACTION_INSERT);
                                    intent.setType("vnd.android.cursor.item/event");
                                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
                                    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                                    intent.putExtra(CalendarContract.Events.TITLE, title);
                                    intent.putExtra(CalendarContract.Events.DESCRIPTION, "");
                                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
                                    //intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

                                    startActivity(intent);

                                }else{
                                    Toast.makeText(getApplicationContext(),"No se puede agregar al calendario, no tiene fecha",Toast.LENGTH_LONG).show();
                                }

                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();





                }
            }
        });


        wvwDetalleCircular.getSettings().setJavaScriptEnabled(true);
        wvwDetalleCircular.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvwDetalleCircular.setWebViewClient(new WebViewClient());
        wvwDetalleCircular.getSettings().setSupportZoom(true);
        wvwDetalleCircular.getSettings().setBuiltInZoomControls(true);
        wvwDetalleCircular.getSettings().setDisplayZoomControls(true);
        wvwDetalleCircular.getSettings().setDomStorageEnabled(true);
        wvwDetalleCircular.getSettings().setAppCacheEnabled(true);
        wvwDetalleCircular.getSettings().setLoadsImagesAutomatically(true);
        wvwDetalleCircular.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        //cortar el título por los espacios
        try{
            String[] titulo = getIntent().getStringExtra("tituloCircular").split(" ");
            int totalElementos = titulo.length;
            int totalEspacios = totalElementos-1;
            if(totalElementos>2){
                lblTitulo.setText(titulo[0]+" "+titulo[1]);
                String t="";
                //el titulo 2 tiene desde titulo[2] hasta titulo[totalElementos-1];
                for(int i=2; i<totalElementos; i++){
                    t += titulo[i]+" ";

                }
                lblTitulo2.setVisibility(View.VISIBLE);

                lblTitulo2.setText(t);
            }else{
                lblTitulo2.setVisibility(View.INVISIBLE);
                lblTitulo.setText(getIntent().getStringExtra("tituloCircular"));
            }
        }catch (Exception ex){

        }




        new RegistrarLecturaAsyncTask(idCircular,idUsuario).execute();
        if(hayConexion())
            wvwDetalleCircular.loadUrl(BASE_URL+RUTA+METODO+"?id="+idCircular);
        else {
            contenidoCircular = Html.fromHtml(contenidoCircular).toString();
            wvwDetalleCircular.loadData(contenidoCircular,"text/html", Xml.Encoding.UTF_8.toString());
        }
        onSwipeTouchListener = new OnSwipeTouchListener() {
            public void onSwipeRight() {
                //Toast.makeText(CircularDetalleActivity.this, "der.", Toast.LENGTH_SHORT).show();
                if(pos>0){
                    for(int i=0; i<circulares.size(); i++){
                        if(circulares.get(i).getIdCircular()==idCircular)
                            pos=i;
                    }

                    pos = pos - 1;
                    idCircular = circulares.get(pos).getIdCircular();

                    temaIcs = circulares.get(pos).getTemaIcs();
                    fechaIcs = circulares.get(pos).getFechaIcs();
                    ubicacionIcs = circulares.get(pos).getUbicacionIcs();
                    horaInicioIcs = circulares.get(pos).getHoraInicialIcs();
                    horaFinIcs = circulares.get(pos).getHoraFinalIcs();

                    new RegistrarLecturaAsyncTask(idCircular,idUsuario).execute();
                    if(viaNotif==0)
                        wvwDetalleCircular.loadUrl(BASE_URL+RUTA+METODO+"?id="+idCircular);


                    //lblTitulo.setText(circulares.get(pos).getNombre());

                    String tituloCompleto = circulares.get(pos).getNombre();
                    String[] titulo = tituloCompleto.split(" ");
                    int totalElementos = titulo.length;
                    if(totalElementos>2){
                        lblTitulo.setText(titulo[0]+" "+titulo[1]);
                        String t="";
                        //el titulo 2 tiene desde titulo[2] hasta titulo[totalElementos-1];
                        for(int i=2; i<totalElementos; i++){
                            t += titulo[i]+" ";

                        }
                        lblTitulo2.setVisibility(View.VISIBLE);

                        lblTitulo2.setText(t);
                    }else{
                        lblTitulo2.setVisibility(View.INVISIBLE);
                        lblTitulo.setText(tituloCompleto);
                    }

                }else {
                    pos = circulares.size() - 1;
                }

            }
            public void onSwipeLeft() {
                // Toast.makeText(CircularDetalleActivity.this, "izq.", Toast.LENGTH_SHORT).show();

                if(pos<circulares.size()){
                    for(int i=0; i<circulares.size(); i++){
                        if(circulares.get(i).getIdCircular()==idCircular)
                            pos=i;
                    }

                    //despues de obtenerla pasar a la siguiente circular
                    pos = pos+1;
                    idCircular = circulares.get(pos).getIdCircular();
                    temaIcs = circulares.get(pos).getTemaIcs();
                    fechaIcs = circulares.get(pos).getFechaIcs();
                    ubicacionIcs = circulares.get(pos).getUbicacionIcs();
                    horaInicioIcs = circulares.get(pos).getHoraInicialIcs();
                    horaFinIcs = circulares.get(pos).getHoraFinalIcs();

                    new RegistrarLecturaAsyncTask(idCircular,idUsuario).execute();
                    wvwDetalleCircular.loadUrl(BASE_URL+RUTA+METODO+"?id="+idCircular);
                    //lblTitulo.setText(circulares.get(pos).getNombre());


                    String tituloCompleto = circulares.get(pos).getNombre();
                    String[] titulo = tituloCompleto.split(" ");
                    int totalElementos = titulo.length;
                    int totalEspacios = totalElementos-1;
                    if(totalElementos>2){
                        lblTitulo.setText(titulo[0]+" "+titulo[1]);
                        String t="";
                        //el titulo 2 tiene desde titulo[2] hasta titulo[totalElementos-1];
                        for(int i=2; i<totalElementos; i++){
                            t += titulo[i]+" ";

                        }
                        lblTitulo2.setVisibility(View.VISIBLE);

                        lblTitulo2.setText(t);
                    }else{
                        lblTitulo2.setVisibility(View.INVISIBLE);
                        lblTitulo.setText(tituloCompleto);
                    }


                }else{
                    pos=0;
                }



            }
        };

        wvwDetalleCircular.setOnTouchListener(onSwipeTouchListener);



        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(pos<=0){
                    return;
                }

                if(pos>0){
                    for(int i=0; i<circulares.size(); i++){
                        if(circulares.get(i).getIdCircular()==idCircular)
                            pos=i;
                    }

                    pos = pos - 1;
                    idCircular = circulares.get(pos).getIdCircular();
                    temaIcs = circulares.get(pos).getTemaIcs();
                    fechaIcs = circulares.get(pos).getFechaIcs();
                    ubicacionIcs = circulares.get(pos).getUbicacionIcs();
                    horaInicioIcs = circulares.get(pos).getHoraInicialIcs();
                    horaFinIcs = circulares.get(pos).getHoraFinalIcs();

                    new RegistrarLecturaAsyncTask(idCircular,idUsuario).execute();
                    wvwDetalleCircular.loadUrl(BASE_URL+RUTA+METODO+"?id="+idCircular);
                    //lblTitulo.setText(circulares.get(pos).getNombre());

                    String tituloCompleto = circulares.get(pos).getNombre();
                    String[] titulo = tituloCompleto.split(" ");
                    int totalElementos = titulo.length;
                    if(totalElementos>2){
                        lblTitulo.setText(titulo[0]+" "+titulo[1]);
                        String t="";
                        //el titulo 2 tiene desde titulo[2] hasta titulo[totalElementos-1];
                        for(int i=2; i<totalElementos; i++){
                            t += titulo[i]+" ";

                        }
                        lblTitulo2.setVisibility(View.VISIBLE);

                        lblTitulo2.setText(t);
                    }else{
                        lblTitulo2.setVisibility(View.INVISIBLE);
                        lblTitulo.setText(tituloCompleto);
                    }

                }else {
                    pos = circulares.size() - 1;
                }

            }
        });


        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtener la posición del id

                try{
                    if(pos<circulares.size()){
                        for(int i=0; i<circulares.size(); i++){
                            if(circulares.get(i).getIdCircular()==idCircular)
                                pos=i;
                        }

                        //despues de obtenerla pasar a la siguiente circular
                        pos = pos+1;
                        idCircular = circulares.get(pos).getIdCircular();
                        temaIcs = circulares.get(pos).getTemaIcs();
                        fechaIcs = circulares.get(pos).getFechaIcs();
                        ubicacionIcs = circulares.get(pos).getUbicacionIcs();
                        horaInicioIcs = circulares.get(pos).getHoraInicialIcs();
                        horaFinIcs = circulares.get(pos).getHoraFinalIcs();
                        new RegistrarLecturaAsyncTask(idCircular,idUsuario).execute();
                        wvwDetalleCircular.loadUrl(BASE_URL+RUTA+METODO+"?id="+idCircular);
                        //lblTitulo.setText(circulares.get(pos).getNombre());


                        String tituloCompleto = circulares.get(pos).getNombre();
                        String[] titulo = tituloCompleto.split(" ");
                        int totalElementos = titulo.length;
                        int totalEspacios = totalElementos-1;
                        if(totalElementos>2){
                            lblTitulo.setText(titulo[0]+" "+titulo[1]);
                            String t="";
                            //el titulo 2 tiene desde titulo[2] hasta titulo[totalElementos-1];
                            for(int i=2; i<totalElementos; i++){
                                t += titulo[i]+" ";

                            }
                            lblTitulo2.setVisibility(View.VISIBLE);

                            lblTitulo2.setText(t);
                        }else{
                            lblTitulo2.setVisibility(View.INVISIBLE);
                            lblTitulo.setText(tituloCompleto);
                        }


                    }else{
                        pos=0;
                    }



                }catch (Exception ex){

                }




            }

        });


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_zoom, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.zoomIn:
                wvwDetalleCircular.zoomIn();
                break;
            case R.id.zoomOut:
                wvwDetalleCircular.zoomOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
*/




    private class RegistrarLecturaAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idCircular;
        private String idUsuario;

        public RegistrarLecturaAsyncTask(String idCircular, String idUsuario) {
            this.idCircular = idCircular;
            this.idUsuario = idUsuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RESPONSE","ejecutando...");
        }

        public void registraLectura(){
            HttpClient httpClient;
            HttpPost httppost;
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL+RUTA+METODO_REG);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("circular_id",idCircular));
                nameValuePairs.add(new BasicNameValuePair("usuario_id",idUsuario));
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
            registraLectura();
            return null;
        }
    }
    private class NoLeerAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idCircular;
        private String idUsuario;

        public NoLeerAsyncTask(String idCircular, String idUsuario) {
            this.idCircular = idCircular;
            this.idUsuario = idUsuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RESPONSE","ejecutando...");
        }

        public void registraLectura(){
            HttpClient httpClient;
            HttpPost httppost;
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL+RUTA+METODO_NOLEER);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("circular_id",idCircular));
                nameValuePairs.add(new BasicNameValuePair("usuario_id",idUsuario));
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
            registraLectura();
            return null;
        }
    }
    private class EliminaAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idCircular;
        private String idUsuario;

        public EliminaAsyncTask(String idCircular, String idUsuario) {
            this.idCircular = idCircular;
            this.idUsuario = idUsuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RESPONSE","ejecutando...");
        }

        public void registraLectura(){
            HttpClient httpClient;
            HttpPost httppost;
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL+RUTA+METODO_DEL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("circular_id",idCircular));
                nameValuePairs.add(new BasicNameValuePair("usuario_id",idUsuario));
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
            Intent intent = new Intent(CircularDetalleActivity.this,CircularActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            registraLectura();
            return null;
        }
    }
    private class FavAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idCircular;
        private String idUsuario;

        public FavAsyncTask(String idCircular, String idUsuario) {
            this.idCircular = idCircular;
            this.idUsuario = idUsuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RESPONSE","ejecutando...");
        }

        public void registraLectura(){
            HttpClient httpClient;
            HttpPost httppost;
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL+RUTA+METODO_FAV);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("circular_id",idCircular));
                nameValuePairs.add(new BasicNameValuePair("usuario_id",idUsuario));
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
            Intent intent = new Intent(CircularDetalleActivity.this,CircularActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            registraLectura();
            return null;
        }
    }


    public void share(String urlLargo){
        try{


            Bitly.shorten(urlLargo, new Bitly.Callback() {
                @Override
                public void onResponse(com.bitly.Response response) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, response.getBitlink());
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(getApplicationContext(),error.getErrorMessage(),Toast.LENGTH_LONG).show();
                }


            });





        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**/




    }



/*
    private class CompartirAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idCircular;
        private String idUsuario;

        public CompartirAsyncTask(String idCircular, String idUsuario) {
            this.idCircular = idCircular;
            this.idUsuario = idUsuario;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("RESPONSE","ejecutando...");

        }

        public void registraLectura(){
            HttpClient httpClient;
            HttpPost httppost;
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL+RUTA+METODO_COMPARTIR);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("circular_id",idCircular));
                nameValuePairs.add(new BasicNameValuePair("usuario_id",idUsuario));
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

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"CHMD");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Te comparto la circular: "+wvwDetalleCircular.getUrl()+"");
            startActivity(Intent.createChooser(sharingIntent, "Compartir mediante"));


            //Intent intent = new Intent(CircularDetalleActivity.this,CircularActivity.class);
            //startActivity(intent);
            //finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            new RecortarUrlAsyncTask(wvwDetalleCircular.getUrl()).execute();
            registraLectura();
            return null;
        }
    }
*/
public void getCircularId(final int id){
    circularesId.clear();
    final SimpleDateFormat formatoInicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat formatoDestino = new SimpleDateFormat("HH:mm:ss");
    final SimpleDateFormat formatoDestino2 = new SimpleDateFormat("dd/MM/yyyy");

    JsonArrayRequest req = new JsonArrayRequest(BASE_URL+RUTA+METODO_CIRCULAR+"?id="+id,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for(int i=0; i<response.length(); i++){

                            JSONObject jsonObject = (JSONObject) response
                                    .get(i);
                            String idCircular = jsonObject.getString("id");
                            String nombre = jsonObject.getString("titulo");
                            t2 = nombre;
                            String fecha1 = jsonObject.getString("created_at");
                            String fecha2 = jsonObject.getString("updated_at");
                            Date date1=new Date(),date2=new Date();
                            try{
                                date1 = formatoInicio.parse(fecha1);
                                date2 = formatoInicio.parse(fecha2);
                            }catch (ParseException ex){

                            }
                            String strFecha1 = formatoDestino.format(date1);
                            String strFecha2 = formatoDestino2.format(date2);
                            String estado = jsonObject.getString("id_estatus");
                            //String favorito = jsonObject.getString("favorito");
                            //String leido = jsonObject.getString("leido");
                            String contenido = jsonObject.getString("contenido");
                            //String eliminada = jsonObject.getString("eliminado");

                            String temaIcs = jsonObject.getString("tema_ics");
                            String fechaIcs = jsonObject.getString("fecha_ics");
                            String horaInicialIcs = jsonObject.getString("hora_inicial_ics");
                            String horaFinalIcs = jsonObject.getString("hora_final_ics");
                            String ubicacionIcs = jsonObject.getString("ubicacion_ics");

                            //No mostrar las eliminadas

                                circularesId.add(new Circular(idCircular,
                                        "Circular No. "+idCircular,
                                        nombre,"",
                                        strFecha1,
                                        strFecha2,
                                        estado,
                                        0,
                                        0,
                                        contenido,
                                        temaIcs,
                                        fechaIcs,
                                        horaInicialIcs,
                                        horaFinalIcs,
                                        ubicacionIcs));


                            //String idCircular, String encabezado, String nombre,
                            //                    String textoCircular, String fecha1, String fecha2, String estado

                        }


                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }


                    //TODO: Cambiarlo cuando pase a prueba en MX
                    // if (existe.equalsIgnoreCase("1")) {
                    //llenado de datos
                    //eliminar circulares y guardar las primeras 10 del registro
                    //Borra toda la tabla
                    /*contenidoCircular = circularesId.get(0).getTextoCircular();
                    temaIcs = circularesId.get(0).getTemaIcs();
                    fechaIcs = circularesId.get(0).getFechaIcs();
                    ubicacionIcs = circularesId.get(0).getUbicacionIcs();
                    horaInicioIcs = circularesId.get(0).getHoraInicialIcs();
                    horaFinIcs = circularesId.get(0).getHoraFinalIcs();
*/

                    try{
                        String[] titulo = circularesId.get(0).getNombre().split(" ");
                        int totalElementos = titulo.length;
                        int totalEspacios = totalElementos-1;
                        if(totalElementos>2){
                            lblTitulo.setText(titulo[0]+" "+titulo[1]);
                            String t="";
                            //el titulo 2 tiene desde titulo[2] hasta titulo[totalElementos-1];
                            for(int i=2; i<totalElementos; i++){
                                t += titulo[i]+" ";

                            }
                            lblTitulo2.setVisibility(View.VISIBLE);

                            lblTitulo2.setText(t);
                        }else{
                            lblTitulo2.setVisibility(View.INVISIBLE);
                            lblTitulo.setText(circularesId.get(0).getNombre());
                        }
                    }catch (Exception ex){

                    }
                    wvwDetalleCircular.loadUrl(BASE_URL+RUTA+METODO+"?id="+id);


                }
            }, new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError error)
        {
            VolleyLog.d("ERROR", "Error: " + error.getMessage());


        }
    });

    // Adding request to request queue
    AppCHMD.getInstance().addToRequestQueue(req);
}



    public void getCirculares(String usuario_id){

        final SimpleDateFormat formatoInicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat formatoDestino = new SimpleDateFormat("HH:mm:ss");
        final SimpleDateFormat formatoDestino2 = new SimpleDateFormat("dd/MM/yyyy");

        JsonArrayRequest req = new JsonArrayRequest(BASE_URL+RUTA+METODO2+"?usuario_id="+usuario_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length(); i++){

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                String idCircular = jsonObject.getString("id");
                                String nombre = jsonObject.getString("titulo");
                                String fecha1 = jsonObject.getString("created_at");
                                String fecha2 = jsonObject.getString("updated_at");
                                Date date1=new Date(),date2=new Date();
                                try{
                                    date1 = formatoInicio.parse(fecha1);
                                    date2 = formatoInicio.parse(fecha2);
                                }catch (ParseException ex){

                                }
                                String strFecha1 = formatoDestino.format(date1);
                                String strFecha2 = formatoDestino2.format(date2);
                                String estado = jsonObject.getString("estatus");
                                String favorito = jsonObject.getString("favorito");
                                String leido = jsonObject.getString("leido");
                                String contenido = jsonObject.getString("contenido");
                                String eliminada = jsonObject.getString("eliminado");

                                String temaIcs = jsonObject.getString("tema_ics");
                                String fechaIcs = jsonObject.getString("fecha_ics");
                                String horaInicialIcs = jsonObject.getString("hora_inicial_ics");
                                String horaFinalIcs = jsonObject.getString("hora_final_ics");
                                String ubicacionIcs = jsonObject.getString("ubicacion_ics");

                                //No mostrar las eliminadas
                                if(eliminada!="1"){
                                    circulares.add(new Circular(idCircular,
                                            "Circular No. "+idCircular,
                                            nombre,"",
                                            strFecha1,
                                            strFecha2,
                                            estado,
                                            Integer.parseInt(leido),
                                            Integer.parseInt(favorito),
                                            contenido,
                                            temaIcs,
                                            fechaIcs,
                                            horaInicialIcs,
                                            horaFinalIcs,
                                            ubicacionIcs));
                                }

                                //String idCircular, String encabezado, String nombre,
                                //                    String textoCircular, String fecha1, String fecha2, String estado

                            }


                        }catch (JSONException e)
                        {
                            e.printStackTrace();


                        }
                        //TODO: Cambiarlo cuando pase a prueba en MX
                        // if (existe.equalsIgnoreCase("1")) {
                        //llenado de datos
                        //eliminar circulares y guardar las primeras 10 del registro
                        //Borra toda la tabla




                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        AppCHMD.getInstance().addToRequestQueue(req);
    }

    public void leeCirculares(int idUsuario){

        ArrayList<DBCircular> dbCirculares = new ArrayList<>();
        List<DBCircular> list = new Select().from(DBCircular.class).where("idUsuario=? AND favorita=0",idUsuario).execute();
        dbCirculares.addAll(list);
        //llenar el adapter
        for(int i=0; i<dbCirculares.size(); i++){
            String idCircular = dbCirculares.get(i).idCircular;
            String nombre = dbCirculares.get(i).nombre;
            String fecha1 =dbCirculares.get(i).created_at;
            String fecha2 = dbCirculares.get(i).updated_at;


            String estado = "0";
            String favorito =  String.valueOf(dbCirculares.get(i).favorita);
            String leido = String.valueOf(dbCirculares.get(i).leida);
            String contenido = String.valueOf(dbCirculares.get(i).contenido);

            //Toast.makeText(getActivity(),contenido,Toast.LENGTH_LONG).show();

            circulares.add(new Circular(idCircular,
                    "Circular No. "+idCircular,
                    nombre,"",
                    fecha1,
                    fecha2,
                    estado,
                    Integer.parseInt(leido),
                    Integer.parseInt(favorito),
                    contenido,"","","","",""));



        } //fin del for


    }



    private void agregarEventoCalendario(String startDate, String endDate, String title,String description, String location) {

        String stDate = startDate;
        String enDate = endDate;

        GregorianCalendar calDate = new GregorianCalendar();
        GregorianCalendar caleDate = new GregorianCalendar();
         //GregorianCalendar calEndDate = new GregorianCalendar();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
        Date date,edate;
        try {
            date = originalFormat.parse(startDate);
            stDate=targetFormat.format(date);
            edate = originalFormat.parse(endDate);
            enDate=targetFormat.format(edate);


        } catch (ParseException ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        long startMillis = 0;
        long endMillis = 0;
        String dates[] = stDate.split(",");
        String SD_YeaR = dates[0];
        String SD_MontH = dates[1];
        String SD_DaY = dates[2];
        String SD_HouR = dates[3];
        String SD_MinutE = dates[4];

        String edates[] = enDate.split(",");
        String E_SD_YeaR = edates[0];
        String E_SD_MontH = edates[1];
        String E_SD_DaY = edates[2];
        String E_SD_HouR = edates[3];
        String E_SD_MinutE = edates[4];






        /*Log.e("YeaR ", SD_YeaR);
        Log.e("MontH ",SD_MontH );
        Log.e("DaY ", SD_DaY);
        Log.e(" HouR", SD_HouR);
        Log.e("MinutE ", SD_MinutE);*/

        calDate.set(Integer.parseInt(SD_YeaR), Integer.parseInt(SD_MontH)-1, Integer.parseInt(SD_DaY), Integer.parseInt(SD_HouR), Integer.parseInt(SD_MinutE));
        startMillis = calDate.getTimeInMillis();

        caleDate.set(Integer.parseInt(E_SD_YeaR), Integer.parseInt(E_SD_MontH)-1, Integer.parseInt(E_SD_DaY), Integer.parseInt(E_SD_HouR), Integer.parseInt(E_SD_MinutE));
        endMillis = caleDate.getTimeInMillis();
/*
        try {
            edate = originalFormat.parse(endDate);
            enDate=targetFormat.format(edate);

        } catch (ParseException ex) {}


        String end_dates[] = endDate.split(",");

        String ED_YeaR = end_dates[0];
        String ED_MontH = end_dates[1];
        String ED_DaY = end_dates[2];

        String ED_HouR = end_dates[3];
        String ED_MinutE = end_dates[4];


        calEndDate.set(Integer.parseInt(ED_YeaR), Integer.parseInt(ED_MontH)-1, Integer.parseInt(ED_DaY), Integer.parseInt(ED_HouR), Integer.parseInt(ED_MinutE));
        endMillis = calEndDate.getTimeInMillis();*/

        try {
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, description);
            values.put(CalendarContract.Events.EVENT_LOCATION,location);
            values.put(CalendarContract.Events.HAS_ALARM,1);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                    .getTimeZone().getID());
            System.out.println(Calendar.getInstance().getTimeZone().getID());
            if (ActivityCompat.checkSelfPermission(CircularDetalleActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            long eventId = Long.parseLong(uri.getLastPathSegment());
            Log.d("Ketan_Event_Id", String.valueOf(eventId));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}

