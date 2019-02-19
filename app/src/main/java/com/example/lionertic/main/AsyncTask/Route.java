package com.example.lionertic.main.AsyncTask;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
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

public class Route extends AsyncTask<String, Void, String> {

    Context context;

    public Route(Context cnt) {
        context=cnt;
    }

    @Override
    protected String doInBackground(final String... urls) {
        // Create URL object
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.ROUTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+jsonObject.getString("lat")+","+jsonObject.getString("lon"));
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);
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
                params.put("lat", urls[0]);
                params.put("lon", urls[1]);
                params.put("id", urls[2]);
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

