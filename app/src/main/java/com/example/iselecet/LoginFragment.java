package com.example.iselecet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iselecet.model.Model;


public class LoginFragment extends Fragment {


    View view;

    Button signUp;
    Button login;
    EditText email;
    EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        signUp = view.findViewById(R.id.login_signup_btn);
        login = view.findViewById(R.id.login_signin_btn);
        email = view.findViewById(R.id.login_email_input);
        password = view.findViewById(R.id.login_pass_input);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                if (userEmail.equals("") && userPassword.equals("")) {
                    Toast.makeText(getActivity(), "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                }
                if(!isValidEmail(userEmail)){
                    Toast.makeText(getActivity(), "Please Enter Validate Email", Toast.LENGTH_SHORT).show();
                }
                else{
                    Model.instance.logInFB(userEmail, userPassword, new Model.SuccessListener() {
                        @Override
                        public void onComplete(boolean result) {
                            if(result){
                                String user_id = Model.instance.getCurrentUserId();
                                Model.instance.getUserRole(user_id, new Model.StringListener() {
                                    @Override
                                    public void onComplete(String data) {
                                        if(data.equals("Doctor")){
                                            Navigation.findNavController(view).navigate(R.id.action_login_to_doctorView);
                                        }
                                        if(data.equals("Patient")){
                                            Navigation.findNavController(view).navigate(R.id.action_login_to_patientView);
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getActivity(), "User Failed To Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_signUp);
            }
        });

        return view;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}