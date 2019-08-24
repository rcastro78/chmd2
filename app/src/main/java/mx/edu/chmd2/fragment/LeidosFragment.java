package mx.edu.chmd2.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

public class LeidosFragment extends Fragment {
    ListView lstCirculares;
    static String METODO_DEL="eliminarCircular.php";
    static String METODO_NOLEER="noleerCircular.php";
    static String METODO_FAV="favCircular.php";
    String rsp="";
    ArrayList<Circular> circulares = new ArrayList<>();
    CircularesAdapter adapter = null;
    static String METODO="getCircularesLeidas.php";
    static String BASE_URL;
    static String RUTA;
    SharedPreferences sharedPreferences;
    ArrayList<String> seleccionados = new ArrayList<>();
    ImageView imgMoverFavSeleccionados,imgMoverNoLeidos,imgEliminarSeleccionados;
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

        View v = inflater.inflate(R.layout.fragment_circulares_leidas, container, false);
        lstCirculares = v.findViewById(R.id.lstCirculares);
        imgMoverFavSeleccionados = v.findViewById(R.id.imgMoverFavSeleccionados);
        imgMoverNoLeidos = v.findViewById(R.id.imgMoverNoLeidas);
        imgEliminarSeleccionados = v.findViewById(R.id.imgEliminarSeleccionados);


        imgMoverNoLeidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionados = adapter.getSeleccionados();
                if(seleccionados.size()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¡Advertencia!");
                    builder.setMessage("¿Estás seguro que quieres marcar estas circulares como no leídas?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            for (int i = 0; i < seleccionados.size(); i++) {
                                Circular c = (Circular) adapter.getItem(Integer.parseInt(seleccionados.get(i)));
                                new NoLeerAsyncTask(c.getIdCircular(),"1660").execute();

                            }

                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(getActivity(),"Debes seleccionar al menos una circular para utilizar esta opción",Toast.LENGTH_LONG).show();
                }

            }
        });


        imgMoverFavSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seleccionados = adapter.getSeleccionados();
                if(seleccionados.size()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¡Advertencia!");
                    builder.setMessage("¿Estás seguro que quieres marcar estas las circulares como favoritas?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            for (int i = 0; i < seleccionados.size(); i++) {
                                Circular c = (Circular) adapter.getItem(Integer.parseInt(seleccionados.get(i)));
                                new FavAsyncTask(c.getIdCircular(),"1660").execute();

                            }

                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(getActivity(),"Debes seleccionar al menos una circular para utilizar esta opción",Toast.LENGTH_LONG).show();
                }

            }
        });





        imgEliminarSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionados = adapter.getSeleccionados();
                if(seleccionados.size()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("¡Advertencia!");
                    builder.setMessage("¿Estás seguro que quieres eliminar estas circulares?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            for (int i = 0; i < seleccionados.size(); i++) {
                                Circular c = (Circular) adapter.getItem(Integer.parseInt(seleccionados.get(i)));
                                new EliminaAsyncTask(c.getIdCircular(),"1660").execute();

                            }

                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(getActivity(),"Debes seleccionar al menos una circular para utilizar esta opción",Toast.LENGTH_LONG).show();
                }

            }
        });

      return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        circulares.clear();
    }






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
            Intent intent = new Intent(getActivity(),CircularActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(getActivity(),CircularActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(getActivity(),CircularActivity.class);
            startActivity(intent);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            registraLectura();
            return null;
        }
    }

}
