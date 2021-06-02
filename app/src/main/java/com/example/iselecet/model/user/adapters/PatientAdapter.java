package com.example.iselecet.model.user.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iselecet.R;
import com.example.iselecet.model.user.Patient;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientHolder> {

    public static List<Patient> patientData = new LinkedList<Patient>();
    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Patient getPatients(int position)
    {
        return patientData.get(position);
    }

    public void setPatientData(List<Patient> patients) {
        this.patientData = patients;
        notifyDataSetChanged();
    }

    // Create PatientHolder for the adapter.
    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_listrow, parent, false);
        PatientHolder holder = new PatientHolder(view);
        return holder;
    }

    // Bind data to the adapter.
    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
        Patient currentPatient = patientData.get(position);
        holder.bindData(currentPatient,position);
        holder.itemView.setTag(currentPatient);
    }

    @Override
    public int getItemCount() {
        return patientData.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Patient patient, View view);
    }
    //---------------PatientHolder----------------//

    public static class PatientHolder extends RecyclerView.ViewHolder{
        TextView patientName;
        TextView email;
        TextView arrivedAt;
        ImageView patientImage;
        int position;

        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            patientImage = itemView.findViewById(R.id.main_photo);
            patientName = itemView.findViewById(R.id.title_listorw);
            email = itemView.findViewById(R.id.subtitle_listrow);
            arrivedAt = itemView.findViewById((R.id.subtitle_listrow2));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(patientData.get(position),v);
                    }
                }
            });
        }

        public void bindData(Patient patient, int position){
            patientName.setText(patient.getFullName());
            email.setText(patient.getEmail());
            patientImage.setImageResource(R.drawable.person_icon);
            arrivedAt.setText(patient.getArrivedAt());
            this.position = position;
        }
    }
}
