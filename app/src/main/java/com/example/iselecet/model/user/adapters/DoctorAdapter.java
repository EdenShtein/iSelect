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
import com.example.iselecet.model.user.Doctor;

import java.util.LinkedList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorHolder> {

    public static List<Doctor> doctorData = new LinkedList<Doctor>();
    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Doctor getDoctors(int position)
    {
        return doctorData.get(position);
    }

    public void setDoctorData(List<Doctor> doctors) {
        this.doctorData = doctors;
        notifyDataSetChanged();
    }

    // Create GameHolder for the adapter.
    @NonNull
    @Override
    public DoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_listrow, parent, false);
        DoctorHolder holder = new DoctorHolder(view);
        return holder;
    }

    // Bind data to the adapter.
    @Override
    public void onBindViewHolder(@NonNull DoctorHolder holder, int position) {
        Doctor currentDoctor = doctorData.get(position);
        holder.bindData(currentDoctor,position);
        holder.itemView.setTag(currentDoctor);
    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Doctor doctor, View view);
    }
    //---------------DoctorHolder----------------//

    public static class DoctorHolder extends RecyclerView.ViewHolder{
        TextView doctorName;
        TextView email;
        ImageView doctorImage;
        int position;

        public DoctorHolder(@NonNull View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.main_photo);
            doctorName = itemView.findViewById(R.id.title_listorw);
            email = itemView.findViewById(R.id.subtitle_listrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(doctorData.get(position),v);
                    }
                }
            });
        }

        public void bindData(Doctor doctor, int position){
            doctorName.setText(doctor.getFullName());
            email.setText(doctor.getEmail());
            doctorImage.setImageResource(R.drawable.person_icon);
            this.position = position;
        }
    }
}
