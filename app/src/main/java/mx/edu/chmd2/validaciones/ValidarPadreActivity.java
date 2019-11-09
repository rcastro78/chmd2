package mx.edu.chmd2.validaciones;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.edu.chmd2.AppCHMD;
import mx.edu.chmd2.InicioActivity;
import mx.edu.chmd2.PrincipalActivity;
import mx.edu.chmd2.R;

public class ValidarPadreActivity extends AppCompatActivity {
    static String VALIDAR_CUENTA="validarEmail.php";

    static String BASE_URL;
    static String RUTA;
    TextView lblMensaje;
    SharedPreferences sharedPreferences;
    String correo,existe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_padre);
        lblMensaje = findViewById(R.id.lblMensaje);
        BASE_URL = this.getString(R.string.BASE_URL);
        RUTA = this.getString(R.string.PATH);
        sharedPreferences = this.getSharedPreferences(this.getString(R.string.SHARED_PREF), 0);
        correo = sharedPreferences.getString("correoRegistrado","");
        validarCuenta(correo);
    }


    public void validarCuenta(final String email){

        JsonArrayRequest req = new JsonArrayRequest(BASE_URL+RUTA+VALIDAR_CUENTA+"?correo="+email,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                                    JSONObject jsonObject = (JSONObject) response
                                        .get(0);
                                    existe = jsonObject.getString("existe");


                        }catch (JSONException e)
                        {
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(),
                                    "Error",
                                    Toast.LENGTH_LONG).show();
                        }
                        //TODO: Cambiarlo a 0 para pruebas
                        // if (existe.equalsIgnoreCase("1")) {
                        if (existe.equalsIgnoreCase("1") || existe.equalsIgnoreCase("0")) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("cuentaValida",1);
                            editor.putString("email",email);
                            editor.commit();

                            lblMensaje.setText("Cuenta de correo validada correctamente");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(ValidarPadreActivity.this, PrincipalActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, 3000);

                        }else{
                            lblMensaje.setText("Cuenta de correo no registrada");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("cuentaValida",0);
                            editor.commit();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(ValidarPadreActivity.this, InicioActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, 3000);
                        }


                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("ERROR", "Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppCHMD.getInstance().addToRequestQueue(req);
    }


}










