package com.example.lionertic.main.AsyncTask;


import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lionertic.main.CONSTANTS;
import com.example.lionertic.main.Fragments.Maps;
import com.example.lionertic.main.MainActivity;
import com.example.lionertic.main.RequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SaveLoca extends AsyncTask<Location, Void, String> {

    Context context;

    public SaveLoca(Context cnt) {
        context=cnt;
    }

    @Override
    protected String doInBackground(final Location... urls) {
        // Create URL object
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage()+"its nothing", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", Double.toString(urls[0].getLatitude()));
                params.put("lon", Double.toString(urls[0].getLongitude()));
                params.put("key", MainActivity.KEY);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    return null;
    }

    @Override
    protected void onPostExecute(String earthquake) {

    }
}

