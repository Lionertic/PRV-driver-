package com.example.lionertic.main.AsyncTask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Routes extends AsyncTask<String, Void, String> {

    Context context;

    public Routes(Context cnt) {
        context=cnt;
    }

    public void route(JSONObject jObject, int c, int dr) {

        JSONArray jRoutes, jLegs;
        ArrayList points = new ArrayList();
        LatLng latLng;
        try {
            jRoutes = jObject.getJSONArray("paths");
            jObject = jRoutes.getJSONObject(0);
            jObject = jObject.getJSONObject("points");
            jRoutes = jObject.getJSONArray("coordinates");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = jRoutes.getJSONArray(i);
                latLng = new LatLng(jLegs.getDouble(1), jLegs.getDouble(0));
                points.add(latLng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        PolylineOptions lineOptions =  new PolylineOptions();

        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(c);
        lineOptions.geodesic(true);
        if(Maps.dri[dr]!=null)
            Maps.dri[dr].remove();
        Maps.dri[dr]=Maps.mMap.addPolyline(lineOptions);
    }
    @Override
    protected String doInBackground(final String... urls) {
        // Create URL object
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null ;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.ROUTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.e("09876543",response);
                            JSONObject jsonObject = new JSONObject(response);
                            route(jsonObject.getJSONObject(urls[0]),Color.GREEN,0);
                            MainActivity.progressDialog.dismiss();
                            if (!(urls[1].equals("")))
                                route(jsonObject.getJSONObject(urls[1]),Color.RED,1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("asdfghjkl","asdfghjkl"+error.toString()+urls[0]);

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key1", urls[0]);
                if(!urls[1].equals(""))
                    params.put("key2", urls[1]);
                params.put("lat", Double.toString(Maps.mCurrentLocation.getLatitude()));
                params.put("lon", Double.toString(Maps.mCurrentLocation.getLongitude()));

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
