package com.allanlin97gmail.sanal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private ArrayList<ClientItem> clientList;

    public static class ClientViewHolder extends  RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView phone;
        TextView address;

        public ClientViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientName);
            email = itemView.findViewById(R.id.clientEmail);
            phone = itemView.findViewById(R.id.clientPhoneNumber);
            address = itemView.findViewById(R.id.clientAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BuildingActivity.class);
                    v.getContext().startActivity(intent);
                }
            });



        }

    }

    public ClientAdapter(ArrayList<ClientItem> ClientList) {
        clientList = ClientList;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        ClientViewHolder clientViewHolder = new ClientViewHolder(view);
        return clientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        ClientItem item = clientList.get(position);

        holder.name.setText(item.getClientName());
        holder.email.setText(item.getClientEmail());
        holder.phone.setText(item.getClientPhoneNumber());
        holder.address.setText(item.getClientAddress());

    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }
}
