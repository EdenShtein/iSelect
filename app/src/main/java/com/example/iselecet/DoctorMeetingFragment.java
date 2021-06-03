package com.example.iselecet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.iselecet.model.user.adapters.PatientAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Map<String, Object> editDoctorMap;

    public RecyclerView patient_list;
    SwipeRefreshLayout swipeRefreshLayout;
    PatientAdapter patientAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_doctor_meeting, container, false);

        patient_list = view.findViewById(R.id.main_rv);
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
                                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                                showNotification(getContext(),"You're first in line, you're welcome to see a doctor"
                                                ,intent);
                                                Date date = new Date();
                                                patient.setArrivedAt(formatter.format(date));
                                                editDoctorMap.put("currentPatient",patient);
                                                editDoctorMap.put("isAvailable",false);
                                                Model.instance.updateDoctor(doctorId, editDoctorMap, new Model.SuccessListener() {
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
                                                Date date = new Date();
                                                patient.setArrivedAt(formatter.format(date));
                                                patientArrayList.add(patient);
                                                editDoctorMap.put("patientList", patientArrayList);
                                                Model.instance.updateDoctor(doctorId, editDoctorMap, new Model.SuccessListener() {
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
                                Model.instance.updateDoctor(doctorId, patientHashMap, new Model.SuccessListener() {
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


        Model.instance.getDoctorWaitingList(doctorId, new Model.ListListener() {
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
                Model.instance.getCurrentPatient(doctorId, new Model.HashMapListener() {
                    @Override
                    public void onComplete(HashMap map) {
                        String arrivedAt = (String)map.get("arrivedAt");
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date1 = new Date(); //Time of system
                        try{
                        Date date2 = formatter.parse(arrivedAt); //the time that the current user arrived
                            long difference_In_Time
                                    = date1.getTime() - date2.getTime();
                            long difference_In_Minutes
                                    = (difference_In_Time
                                    / (1000 * 60))
                                    % 60;
                            editDoctorMap = new HashMap<>();
                            if(difference_In_Minutes>10){

                                Model.instance.getDoctorWaitingList(doctorId, new Model.ListListener() {
                                    @Override
                                    public void onComplete(ArrayList result) {
                                        if(result.size()==0){
                                            editDoctorMap.put("currentPatient",null);
                                            editDoctorMap.put("isAvailable",true);
                                            Model.instance.updateDoctor(doctorId, editDoctorMap, new Model.SuccessListener() {
                                                @Override
                                                public void onComplete(boolean result) {

                                                }
                                            });
                                        }
                                        else{
                                            Patient current = (Patient)result.get(0);
                                            editDoctorMap.put("currentPatient",current);
                                            result.remove(0);
                                            if(result.size()==0){
                                                editDoctorMap.put("patientList",null);
                                            }
                                            editDoctorMap.put("patientList",result);
                                            Model.instance.updateDoctor(doctorId, editDoctorMap, new Model.SuccessListener() {
                                                @Override
                                                public void onComplete(boolean result) {

                                                }
                                            });
                                        }
                                    }
                                });

                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
                Model.instance.getDoctorWaitingList(doctorId, new Model.ListListener() {
                    @Override
                    public void onComplete(ArrayList result) {
                        patientAdapter.setPatientData(result);
                        patient_list.setAdapter(patientAdapter);
                    }
                });


                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void showNotification(Context context, String message, Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.iselect_logo)
                .setContentTitle("iSelect")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(1, notificationBuilder.build()); //


    }
}