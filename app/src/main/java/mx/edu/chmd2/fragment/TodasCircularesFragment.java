package mx.edu.chmd2.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.edu.chmd2.AppCHMD;
import mx.edu.chmd2.CircularActivity;
import mx.edu.chmd2.CircularDetalleActivity;
import mx.edu.chmd2.R;
import mx.edu.chmd2.adapter.CircularesAdapter;
import mx.edu.chmd2.modelos.Circular;

public class TodasCircularesFragment extends Fragment {
ListView lstCirculares;
ArrayList<Circular> circulares = new ArrayList<>();
CircularesAdapter adapter = null;
    static String METODO="getCirculares.php";
    static String METODO_REG="leerTodasCircular.php";
    static String METODO_DEL="eliminarTodasCircular.php";
    static String METODO_FAV="favTodasCircular.php";
    static String BASE_URL;
    static String RUTA;
    String rsp;
    SharedPreferences sharedPreferences;
    ImageView imgMoverFavSeleccionados,imgMoverLeidos,imgEliminarSeleccionados;
    boolean todos=false;
    CheckBox chkTodos;
    @Override
    public void onPause() {
        super.onPause();
        circulares.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCirculares(1660);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);
        sharedPreferences = getActivity().getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);

        View v = inflater.inflate(R.layout.fragment_circulares, container, false);
        lstCirculares = v.findViewById(R.id.lstCirculares);
        imgMoverFavSeleccionados = v.findViewById(R.id.imgMoverFavSeleccionados);
        imgMoverLeidos = v.findViewById(R.id.imgMoverComp);
        imgEliminarSeleccionados = v.findViewById(R.id.imgEliminarSeleccionados);
        chkTodos = v.findViewById(R.id.chkTodos);
        chkTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todos=true;
                }else{
                    todos=false;
                }
            }
        });

        imgMoverFavSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todos){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¡Advertencia!");
                    builder.setMessage("¿Estás seguro que quieres marcar TODAS las circulares como favoritas?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new FavTodasAsyncTask("1660").execute();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();



                }
            }
        });

        imgMoverLeidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todos){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¡Advertencia!");
                    builder.setMessage("¿Estás seguro que quieres marcar TODAS las circulares como leídas?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new LeerTodasAsyncTask("1660").execute();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();



                }
            }
        });

        imgEliminarSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(todos){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¡Advertencia!");
                    builder.setMessage("¿Estás seguro que quieres eliminar TODAS las circulares?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new EliminarTotasAsyncTask("1660").execute();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();





                }
            }
        });


        lstCirculares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Se desplegará la circular
                Circular circular = (Circular)lstCirculares.getItemAtPosition(position);
                String idCircular = circular.getIdCircular();
                Intent intent = new Intent(getActivity(), CircularDetalleActivity.class);
                intent.putExtra("idCircular",idCircular);
                intent.putExtra("tituloCircular",circular.getNombre());
                getActivity().startActivity(intent);

            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        circulares.clear();
    }



    //dummy
    /*public void llenaCirculares(){
        circulares.clear();
        circulares.add(new Circular("1","Encabezado","Nueva Circular","Este es el texto de la circular","11/07/2019","11/07/2019",1));
        circulares.add(new Circular("2","Encabezado B","Nueva Circular","Este es el texto de la circular","11/07/2019","11/07/2019",1));
        circulares.add(new Circular("3","Encabezado C","Nueva Circular","Este es el texto de la circular","11/07/2019","11/07/2019",1));
        adapter = new CircularesAdapter(getActivity(),circulares);
        lstCirculares.setAdapter(adapter);
    }*/




    public void getCirculares(int usuario_id){

        final SimpleDateFormat formatoInicio = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat formatoDestino = new SimpleDateFormat("dd/MM/yyyy");

        JsonArrayRequest req = new JsonArrayRequest(BASE_URL+RUTA+METODO+"?usuario_id="+usuario_id,
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
                                String strFecha2 = formatoDestino.format(date2);
                                String estado = jsonObject.getString("estatus");
                                String favorito = jsonObject.getString("favorito");
                                String leido = jsonObject.getString("leido");
                                String compartida = jsonObject.getString("compartida");
                                circulares.add(new Circular(idCircular,"Circular No. "+idCircular,nombre,"",strFecha1,strFecha2,estado,Integer.parseInt(leido),Integer.parseInt(favorito),Integer.parseInt(compartida) ));
                                //String idCircular, String encabezado, String nombre,
                                //                    String textoCircular, String fecha1, String fecha2, String estado

                            }





                        }catch (JSONException e)
                        {
                            e.printStackTrace();

                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error",
                                    Toast.LENGTH_LONG).show();
                        }
                        //TODO: Cambiarlo cuando pase a prueba en MX
                        // if (existe.equalsIgnoreCase("1")) {
                       //llenado de datos
                        //eliminar circulares y guardar las primeras 10 del registro
                        //Borra toda la tabla
                        /*new Delete().from(DBCircular.class).execute();

                        for(int i=0; i<10; i++){
                            DBCircular dbCircular = new DBCircular();
                            dbCircular.idCircular = circulares.get(i).getIdCircular();
                            dbCircular.estado = circulares.get(i).getEstado();
                            dbCircular.nombre = circulares.get(i).getNombre();
                            dbCircular.textoCircular = circulares.get(i).getTextoCircular();
                            dbCircular.save();
                        }*/

                        adapter = new CircularesAdapter(getActivity(),circulares);
                        lstCirculares.setAdapter(adapter);

                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());

                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppCHMD.getInstance().addToRequestQueue(req);
    }


    private class LeerTodasAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idUsuario;

        public LeerTodasAsyncTask(String idUsuario) {
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
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
    private class EliminarTotasAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idUsuario;

        public EliminarTotasAsyncTask(String idUsuario) {
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
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
            Intent intent = new Intent(getActivity(), CircularActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            registraLectura();
            return null;
        }
    }
    private class FavTodasAsyncTask extends AsyncTask<Void, Long, Boolean> {
        private String idUsuario;

        public FavTodasAsyncTask(String idUsuario) {
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
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
            Intent intent = new Intent(getActivity(),CircularActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            registraLectura();
            return null;
        }
    }


}
