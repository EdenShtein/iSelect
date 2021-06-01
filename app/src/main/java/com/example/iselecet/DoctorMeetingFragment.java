package com.example.iselecet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iselecet.model.Model;
import com.example.iselecet.model.user.Doctor;
import com.example.iselecet.model.user.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DoctorMeetingFragment extends Fragment {

    View view;
    ImageView close;

    TextView doctorTitle;
    TextView doctorSubTitle;
    ArrayList<Patient> patientArrayList;
    String doctorId;

    Button makeAppoinment;

    Map<String, Object> editDoctorMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_doctor_meeting, container, false);

        close = view.findViewById(R.id.doctor_meeting_close_btn);
        doctorTitle = view.findViewById(R.id.doctor_title);
        doctorSubTitle = view.findViewById(R.id.doctor_sub_title);
        makeAppoinment = view.findViewById(R.id.appoint_btn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        patientArrayList = new ArrayList<>();

        doctorTitle.setText(DoctorMeetingFragmentArgs.fromBundle(getArguments()).getFullName());
        doctorSubTitle.setText(DoctorMeetingFragmentArgs.fromBundle(getArguments()).getEmail());

        doctorId = DoctorMeetingFragmentArgs.fromBundle(getArguments()).getId();

        makeAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDoctorMap = new HashMap<>();
                editDoctorMap.put("isAvailable",false);

                String patientId = Model.instance.getCurrentUserId();
                Model.instance.getPatientData(patientId, new Model.PatientListener() {
                    @Override
                    public void onComplete(Patient patient) {
                        Model.instance.getDoctorData(doctorId, new Model.DoctorListener() {
                            @Override
                            public void onComplete(Doctor doctor) {
                                patientArrayList = doctor.getPatientList();
                                patientArrayList.add(patient);
                                editDoctorMap.put("patientList",patientArrayList);
                                Model.instance.updateDoctorAvailable(doctorId, editDoctorMap, new Model.SuccessListener() {
                                    @Override
                                    public void onComplete(boolean result) {
                                        if(result){
                                            Navigation.findNavController(view).popBackStack();                        }
                                        else{

                                        }
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });

        return view;
    }
}