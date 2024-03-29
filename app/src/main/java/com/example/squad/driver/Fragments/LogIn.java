package com.example.squad.driver.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.squad.driver.AsyncTask.SignIn;
import com.example.squad.driver.MainActivity;
import com.example.squad.driver.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogIn extends Fragment {


    private static EditText mob,pass;
    private static int PASSWORD_LENGTH = 8;
    private View v;
    private Button su,si;
    public LogIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_log_in, container, false);
        MainActivity.progressDialog.dismiss();
        su=v.findViewById(R.id.register1);
        si=v.findViewById(R.id.login1);
        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTitle("Sign Up");
                SignUp m = new SignUp();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment, m).commit();

            }
        });
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mob=(EditText) v.findViewById(R.id.phone1);
                pass=(EditText) v.findViewById(R.id.pass1);
                String mo,pa;
                mo = mob.getText().toString().trim();
                pa = pass.getText().toString().trim();
                check(mo,pa);
            }
        });
        return v;

    }

    void check(String mob,String pass){
        if(mob.length()==10)
            if(is_Valid_Password(pass))
                new SignIn(getContext(),getActivity()).execute(mob,pass);
            else
                Toast.makeText(getContext(),"Invalid Password",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getContext(),"Wrong Number",Toast.LENGTH_LONG).show();
    }

    public static boolean is_Valid_Password(String password) {

        if (password.length() < PASSWORD_LENGTH) return false;

        int charCount = 0;
        int numCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (is_Numeric(ch)) numCount++;
            else if (is_Letter(ch)) charCount++;
            else return false;
        }
        return (charCount >= 2 && numCount >= 2);
    }
    public static boolean is_Letter(char ch) {
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z');
    }
    public static boolean is_Numeric(char ch) {
        return (ch >= '0' && ch <= '9');
    }

}
