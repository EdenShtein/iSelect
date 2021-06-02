package com.example.iselecet;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.iselecet.model.user.adapters.DoctorAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PatientViewFragment extends Fragment {

   View view;
    public RecyclerView doctor_list;
    SwipeRefreshLayout swipeRefreshLayout;
    DoctorAdapter doctorAdapter;
    ImageView signout;
    ImageView availableSort;



    CardView list_card;

    AlertDialog.Builder alertBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_patient_view, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        doctor_list = view.findViewById(R.id.main_rv);
        signout = view.findViewById(R.id.main_signout);
        availableSort = view.findViewById(R.id.main_sort);
        doctor_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        doctor_list.setLayoutManager(layoutManager);
        doctorAdapter = new DoctorAdapter();
        doctor_list.setAdapter(doctorAdapter);

        list_card = view.findViewById(R.id.card_list);

        alertBuilder = new AlertDialog.Builder(getActivity());

        Model.instance.getAllDoctors(new Model.ListListener<Doctor>() {
            @Override
            public void onComplete(ArrayList<Doctor> result) {
                doctorAdapter.setDoctorData(result);
                doctor_list.setAdapter(doctorAdapter);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.mainfeed_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Model.instance.getAllDoctors(new Model.ListListener<Doctor>() {
                    @Override
                    public void onComplete(ArrayList<Doctor> result) {
                        doctorAdapter.setDoctorData(result);
                        doctor_list.setAdapter(doctorAdapter);
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

        availableSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.getAllDoctors(new Model.ListListener<Doctor>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(ArrayList<Doctor> result) {
                        Collections.sort(result, new Comparator<Doctor>(){

                            @Override
                            public int compare(Doctor doctor1, Doctor doctor2){

                                boolean b1 = doctor1.getAvailable();
                                boolean b2 = doctor2.getAvailable();

                                return (b1 != b2) ? (b1) ? -1 : 1 : 0;
                            }
                        });
                        doctorAdapter.setDoctorData(result);
                        doctor_list.setAdapter(doctorAdapter);
                    }

                });

            }
        });

        doctorAdapter.setOnItemClickListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Doctor doctor, View view) {
                PatientViewFragmentDirections.ActionPatientViewToDoctorMeeting action =
                        PatientViewFragmentDirections.actionPatientViewToDoctorMeeting(doctor.getId(),doctor.getFullName(),doctor.getEmail());
                Navigation.findNavController(view).navigate(action);
            }
        });



        return view;
    }
}