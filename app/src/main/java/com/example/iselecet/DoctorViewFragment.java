package com.example.iselecet;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.iselecet.model.Model;
import com.example.iselecet.model.user.Doctor;
import com.example.iselecet.model.user.Patient;
import com.example.iselecet.model.user.adapters.DoctorAdapter;
import com.example.iselecet.model.user.adapters.PatientAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class DoctorViewFragment extends Fragment {

   View view;

    public RecyclerView patient_list;
    SwipeRefreshLayout swipeRefreshLayout;
    PatientAdapter patientAdapter;

    String doctor_id;

    ImageView signout;

    ArrayList<Patient> patientsList;

    AlertDialog.Builder alertBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doctor_view, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        alertBuilder = new AlertDialog.Builder(getActivity());
        patientsList = new ArrayList<>();

        patient_list = view.findViewById(R.id.main_rv);
        signout = view.findViewById(R.id.main_signout);
        patient_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        patient_list.setLayoutManager(layoutManager);
        patientAdapter = new PatientAdapter();
        patient_list.setAdapter(patientAdapter);

        swipeRefreshLayout = view.findViewById(R.id.mainfeed_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        doctor_id = Model.instance.getCurrentUserId();

        Model.instance.getDoctorWaitingList(doctor_id, new Model.ListListener() {
            @Override
            public void onComplete(ArrayList result) {
                patientAdapter.setPatientData(result);
                patient_list.setAdapter(patientAdapter);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Model.instance.getDoctorWaitingList(doctor_id, new Model.ListListener() {
                    @Override
                    public void onComplete(ArrayList result) {
                        patientAdapter.setPatientData(result);
                        patient_list.setAdapter(patientAdapter);
                    }
                });


                swipeRefreshLayout.setRefreshing(false);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBuilder.setMessage("Are You Sure You Want To Sign Out?")
                        .setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Model.instance.signOutFB();
                                Navigation.findNavController(view).navigate(R.id.action_patientView_to_login);
                            }
                        });
                AlertDialog alert = alertBuilder.create();
                alert.setTitle("Sign Out");
                alert.show();
            }
        });



        return view;
    }
}