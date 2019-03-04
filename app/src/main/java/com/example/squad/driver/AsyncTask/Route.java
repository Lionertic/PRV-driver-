package com.example.squad.driver.AsyncTask;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.squad.driver.CONSTANTS;
import com.example.squad.driver.RequestHandler;
import com.example.squad.driver.Service.RouteService;
import com.google.maps.model.LatLng;

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
                            LatLng latLng = new LatLng(Double.parseDouble(jsonObject.getString("lat")),Double.parseDouble(jsonObject.getString("lon")));
                            Intent serviceIntent = new Intent(context, RouteService.class);
                            String query = "google.navigation:q="+latLng.lat+","+latLng.lng;
                            serviceIntent.putExtra("query",query);

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                                context.startForegroundService(serviceIntent);
                            }else{
                                context.startService(serviceIntent);
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

