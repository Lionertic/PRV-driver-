package com.example.squad.driver.AsyncTask;


import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.squad.driver.CONSTANTS;
import com.example.squad.driver.MainActivity;
import com.example.squad.driver.RequestHandler;


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
                            if(jsonObject.getInt("success")==2){
                                new Route(context).execute(Double.toString(urls[0].getLatitude()),Double.toString(urls[0].getLongitude()),jsonObject.getString("id"));
                                Log.e("qwertyuiop",jsonObject.getString("id"));
                            }
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

