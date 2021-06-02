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
import android.widget.Toast;

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

    String patientId;
    String doctorId;

    Button makeAppointment;
    Button cancelAppointment;

    HashMap patientHashMap;
    int i=0;

    Map<String, Object> editDoctorMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_doctor_meeting, container, false);

        close = view.findViewById(R.id.doctor_meeting_close_btn);
        doctorTitle = view.findViewById(R.id.doctor_title);
        doctorSubTitle = view.findViewById(R.id.doctor_sub_title);
        makeAppointment = view.findViewById(R.id.appoint_btn);
        cancelAppointment = view.findViewById(R.id.cancel_app_btn);

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
        patientId = Model.instance.getCurrentUserId();

        makeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.isPatientExist(doctorId, patientId, new Model.SuccessListener() {
                    @Override
                    public void onComplete(boolean result) {
                        if(result){
                            Toast.makeText(getActivity(), "You're already in the line!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            editDoctorMap = new HashMap<>();
                            editDoctorMap.put("isAvailable",false);

                            patientId = Model.instance.getCurrentUserId();
                            Model.instance.getPatientData(patientId, new Model.PatientListener() {
                                @Override
                                public void onComplete(Patient patient) {
                                    Model.instance.getDoctorData(doctorId, new Model.DoctorListener() {
                                        @Override
                                        public void onComplete(Doctor doctor) {
                                            patientArrayList = doctor.getPatientList();
                                            if (patientArrayList.size() == 0 && doctor.getCurrentPatient()==null) {
                                                Toast.makeText(getActivity(), "You're first in line, you're welcome to see a doctor",
                                                        Toast.LENGTH_SHORT).show();
                                                editDoctorMap.put("currentPatient",patient);
                                                Model.instance.updateDoctorAvailable(doctorId, editDoctorMap, new Model.SuccessListener() {
                                                    @Override
                                                    public void onComplete(boolean result) {
                                                        if (result) {
                                                            Navigation.findNavController(view).popBackStack();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Something went wrong.....",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                            } else {
                                                patient.setWaiting(true);
                                                patientArrayList.add(patient);
                                                editDoctorMap.put("patientList", patientArrayList);
                                                Model.instance.updateDoctorAvailable(doctorId, editDoctorMap, new Model.SuccessListener() {
                                                    @Override
                                                    public void onComplete(boolean result) {
                                                        if (result) {
                                                            Navigation.findNavController(view).popBackStack();
                                                        } else {
                                                            Toast.makeText(getActivity(), "Something went wrong.....",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });


            }
        });

        cancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientHashMap = new HashMap();
                Model.instance.getDoctorWaitingList(doctorId, new Model.ListListener() {
                    @Override
                    public void onComplete(ArrayList result) {
                        patientArrayList = result;
                        if(patientArrayList.size()==0){
                            Toast.makeText(getActivity(), "You're not in the line!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            for(Patient patient : patientArrayList){
                                if(patient.getId().equals(patientId)){
                                    patientArrayList.remove(patient);
                                    i--;
                                    break;

                                }else{
                                    i++;

                                }
                            }
                            if(i==patientArrayList.size() && i!=0){
                                Toast.makeText(getActivity(), "You're not in the line!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(patientArrayList.size()==0){
                                    patientHashMap.put("isAvailable",true);
                                }
                                patientHashMap.put("patientList",patientArrayList);
                                Model.instance.updateDoctorAvailable(doctorId, patientHashMap, new Model.SuccessListener() {
                                    @Override
                                    public void onComplete(boolean result) {
                                        if(result){
                                            Toast.makeText(getActivity(), "You Have Been Removed",
                                                    Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(view).popBackStack();
                                        }
                                        else{
                                            Toast.makeText(getActivity(), "Something went wrong.....",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        }

                    }
                });
            }
        });


        return view;
    }
}