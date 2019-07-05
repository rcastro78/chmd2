package mx.edu.chmd2.Alumnos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.edu.chmd2.AppCHMD;
import mx.edu.chmd2.MenuCircularesActivity;
import mx.edu.chmd2.R;
import mx.edu.chmd2.adapter.AlumnosAdapter;
import mx.edu.chmd2.modelos.Alumno;

public class AlumnosActivity extends AppCompatActivity {
ArrayList<Alumno> alumnos = new ArrayList<>();
AlumnosAdapter alumnosAdapter = null;
ProgressDialog progressDialog;
ListView lstAlumnos;
TextView lblToolbar;
FloatingActionButton fabNuevo;
    static String GET_ALUMNOS="getAlumnos.php";
    static String BASE_URL;
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        lstAlumnos = findViewById(R.id.lstAlumnos);
        fabNuevo = findViewById(R.id.fabNuevoAlumno);
        tf = Typeface.createFromAsset(getAssets(),"fonts/GothamRoundedBold_21016.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlumnosActivity.this, MenuCircularesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        lblToolbar = toolbar.findViewById(R.id.lblTextoToolbar);
        lblToolbar.setText("Alumnos");
        lblToolbar.setTypeface(tf);
        BASE_URL = this.getString(R.string.BASE_URL);
        datosAlumnos();
        lstAlumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void cargar() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    public void datosAlumnos(){
        cargar();
        JsonArrayRequest req = new JsonArrayRequest(BASE_URL+"pruebascd/app/webservices/"+GET_ALUMNOS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();

                        try {


                            for (int i=0; i<response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                String id = jsonObject.getString("id");
                                String nombre = jsonObject.getString("alumno");
                                String apellido = jsonObject.getString("familia");
                                String sexo = jsonObject.getString("sexo");
                                String nivel = jsonObject.getString("nivel");
                                String grado = jsonObject.getString("grado");
                                String grupo = jsonObject.getString("grupo");
                                String estatus = jsonObject.getString("estatus");
                                alumnos.add(new Alumno(Integer.parseInt(id),nombre,"",sexo,nivel,grado,grupo,estatus));

                                //mercanciasServicio.add(new MercanciaServicio(idTm,marca+"\n"+modelo+"\n"+subtipo,peso,tipoMercancia));

                            }

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Error",
                                    Toast.LENGTH_LONG).show();
                        }
                        alumnosAdapter = new AlumnosAdapter(AlumnosActivity.this,alumnos);
                        lstAlumnos.setAdapter(alumnosAdapter);
                        //mercanciaServicioAdapter = new MercanciaServicioAdapter(DatosServicioActivity.this,mercanciasServicio);
                        //lstMercancias.setAdapter(mercanciaServicioAdapter);



                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppCHMD.getInstance().addToRequestQueue(req);
    }

}
    /*
    * public void datosMercancia(final String svc, final String idT) {
        //cargar();

        JsonArrayRequest req = new JsonArrayRequest(BASE_URL+METODO_MERCANCIA+"?svc="+svc+"&idt="+idT,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();

                        try {


                            for (int i=0; i<response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                String idTm = jsonObject.getString("IdTM");
                                String marca = jsonObject.getString("marca");
                                String modelo = jsonObject.getString("modelo");
                                String subtipo = jsonObject.getString("SubTipoMercancia");
                                String tipoMercancia = jsonObject.getString("TipoMercancia");
                                String peso = jsonObject.getString("peso");
                                if (marca.equals("null")){marca="";}
                                if (modelo.equals("null")){modelo="";}

                                mercanciasServicio.add(new MercanciaServicio(idTm,marca+"\n"+modelo+"\n"+subtipo,peso,tipoMercancia));

                            }

                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Error",
                                    Toast.LENGTH_LONG).show();
                        }

                        mercanciaServicioAdapter = new MercanciaServicioAdapter(DatosServicioActivity.this,mercanciasServicio);
                        lstMercancias.setAdapter(mercanciaServicioAdapter);



                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppTransporte.getInstance().addToRequestQueue(req);
    }

    * */


