package com.example.lionertic.main.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lionertic.main.CONSTANTS;
import com.example.lionertic.main.Fragments.LogIn;
import com.example.lionertic.main.R;
import com.example.lionertic.main.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AsyncTask<String, Void, Void> {

    Context context;
    Activity activity;
    public Register(Context cnt,Activity act){
        context=cnt;
        activity=act;
    }


    @Override
    protected Void doInBackground(final String... strings) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            if(jsonObject.getInt("success")==1) {
                                SharedPreferences sd = context.getSharedPreferences("KEY", context.MODE_PRIVATE);
                                sd.edit().putString("KEY", jsonObject.getString("KEY")).commit();
                            }
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mob", strings[0]);
                params.put("pass", strings[1]);
                return params;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.setTitle("Log In");
        LogIn m = new LogIn();
        FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment, m).commit();
    }
}
