package com.example.squad.driver.Service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.squad.driver.CONSTANTS;
import com.example.squad.driver.Fragments.Maps;
import com.example.squad.driver.MainActivity;
import com.example.squad.driver.R;
import com.example.squad.driver.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RouteService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;
    String query;
    Intent mapIntent;
    public RouteService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        query=intent.getExtras().getString("query");
        Uri gmmIntentUri = Uri.parse(query);
        mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.setPackage("com.google.android.apps.maps");
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getBaseContext(),query, Toast.LENGTH_LONG).show();

        //Inflate the chat head layout we created
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.float_route, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //Drag and move chat head using user's touch action.
        ImageView redirect = mChatHeadView.findViewById(R.id.redirect_btn);
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("route",true);
                startActivity(i);
            }
        });
        final ImageView chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.

                            //close the service and remove the chat heads
                            stopSelf();
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
        Log.e("routeservlog","check");
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.context)
                .setTitle("REQ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendReply(1);
                        mWindowManager.addView(mChatHeadView, params);
                        getBaseContext().startActivity(mapIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendReply(-1);
                    }
                }).setCancelable(false)
                ;
        MainActivity.alertDialog=builder.create();
        MainActivity.alertDialog
                .getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        MainActivity.alertDialog.show();

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (MainActivity.alertDialog.isShowing()) {
                    MainActivity.alertDialog.dismiss();
                }
            }
        };

        MainActivity.alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 40000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
        }
        catch (Exception e){

        }
    }

    void sendReply(final int reply){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                CONSTANTS.ANS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                params.put("ans", Integer.toString(reply));
                params.put("key", MainActivity.KEY);
                return params;
            }
        };
        RequestHandler.getInstance(MainActivity.context).addToRequestQueue(stringRequest);

    }
}
