package com.example.lionertic.main.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import com.example.lionertic.main.R;
import com.example.lionertic.main.RequestHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AsyncTask<String, Void, Void> {

    Context context;
    Activity activity;

    public SignIn(Context cnt,Activity act){
        context=cnt;
        activity=act;
    }

    @Override
    protected Void doInBackground(final String... strings) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success")==1) {
                                Toast.makeText(context, "Successful!!", Toast.LENGTH_LONG).show();
                                SharedPreferences sd = context.getSharedPreferences("KEY", context.MODE_PRIVATE);
                                sd.edit().putString("KEY", jsonObject.getString("KEY")).commit();
                                MainActivity.KEY=jsonObject.getString("KEY");
                                activity.setTitle("Maps");
                                Maps m = new Maps();
                                FragmentManager fm = ((FragmentActivity)activity).getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.fragment, m).commit();
                            }
                            else{
                                Toast.makeText(context, "User Doesn't Exist", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Log.e("qwertyuiop","asdfghjkl"+e.toString());
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

    }
}

