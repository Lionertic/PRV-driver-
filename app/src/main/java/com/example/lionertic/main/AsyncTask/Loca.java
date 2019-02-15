package com.example.lionertic.main.AsyncTask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Loca extends AsyncTask<URL, Void, String> {

    Context context;
    FusedLocationProviderClient mFusedLocationClient;
    LatLng latLng;

    public Loca(Context cnt,FusedLocationProviderClient flpd){
        context=cnt;
        mFusedLocationClient=flpd;
    }

    @Override
    protected String doInBackground(final URL... urls) {
        // Create URL object
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null ;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Maps.marker.setPosition(latLng);
                    Maps.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            CONSTANTS.INSERT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("lat", Double.toString(latLng.latitude));
                            params.put("lon", Double.toString(latLng.longitude));
                            params.put("key", MainActivity.KEY);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
                }
            }}
        );
        return null;
    }

    @Override
    protected void onPostExecute(String earthquake) {

    }
}
