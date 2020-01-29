package mx.edu.chmd1;

import android.app.Application;
import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bitly.Bitly;
import com.bitly.Error;
import com.bitly.Response;

import java.util.Arrays;

public class AppCHMD extends Application {

    public static final String TAG = AppCHMD.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppCHMD mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("chmd.db").create();
        ActiveAndroid.initialize(dbConfiguration);

        Bitly.initialize(this, "e8bb7ba58c708d041c59b1ba04616b0668bdf53b", Arrays.asList("chmd.edu.mx","chmd.edu.mx"), Arrays.asList("yourscheme"), new Bitly.Callback() {
            @Override
            public void onResponse(Response response) {
                // response provides a Response object which contains the full URL information
                // response includes a status code
                // Your custom logic goes here...
            }

            @Override
            public void onError(Error error) {
                // error provides any errors in retrieving information about the URL
                // Your custom logic goes here...
            }
        });


    }

    public static synchronized AppCHMD getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }


}


