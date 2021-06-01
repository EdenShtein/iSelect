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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iselecet.model.Model;
import com.example.iselecet.model.user.Doctor;
import com.example.iselecet.model.user.Patient;
import com.example.iselecet.model.user.User;


public class SignUpFragment extends Fragment {
    View view;

    TextView signin;
    EditText email;
    EditText password;
    EditText rePassword;
    EditText fullName;
    Button signup;
    Switch doctorSwitch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        signin=view.findViewById(R.id.signin_link);
        email=view.findViewById(R.id.signup_email_input);
        fullName=view.findViewById(R.id.signup_fullname_input);
        password=view.findViewById(R.id.signup_password_input);
        rePassword=view.findViewById(R.id.signup_repassword_input);
        signup = view.findViewById(R.id.signup_continue_btn);
        doctorSwitch = view.findViewById(R.id.signup_doctor_switch);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_signUp_to_login);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=email.getText().toString();
                String userpassword=password.getText().toString();
                String rpassword = rePassword.getText().toString();
                String fullname = fullName.getText().toString();
                Boolean switchState = doctorSwitch.isChecked();
                User user;
                String role = "Patient";
                if(switchState){
                    role = "Doctor";
                }
                if (useremail.equals("") || userpassword.equals("")) {
                    Toast.makeText(getActivity(),"Please Enter Full Data",Toast.LENGTH_SHORT).show();
                }
                if(!isValidEmail(useremail)){
                    Toast.makeText(getActivity(), "Please Enter Validate Email", Toast.LENGTH_SHORT).show();
                }
                if(!(userpassword.equals(rpassword))) {
                    Toast.makeText(getActivity(),"Password are not the same",Toast.LENGTH_SHORT).show();
                }else{
                    if(role.equals("Doctor")){
                         user = new Doctor(useremail,role,fullname);

                    }
                    else{
                        user = new Patient(useremail,role,fullname);

                    }
                    Model.instance.signUpFB(user,userpassword);
                    Navigation.findNavController(view).navigate(R.id.action_signUp_to_login);

                }


            }
        });

        return view;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}