package com.allanlin97gmail.sanal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        ImageView editDeleteButton;

        public ClientViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.clientName);
            email = itemView.findViewById(R.id.clientEmail);
            phone = itemView.findViewById(R.id.clientPhoneNumber);
            address = itemView.findViewById(R.id.clientAddress);

            editDeleteButton =  itemView.findViewById(R.id.menuButton);
            editDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("hi", "imagebutton presses");

                    PopupMenu popup = new PopupMenu(itemView.getContext(), v);

                    /** Adding menu items to the popumenu */
                    popup.getMenuInflater().inflate(R.menu.edit_delete_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // TODO Auto-generated method stub
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    System.out.println("asd");
                                    break;

                                case R.id.delete:
                                    System.out.println("asd");
                                    break;

                                default:
                                    break;
                            }

                            return false;
                        }
                    });
                    popup.show();
                }
            });

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

        holder.editDeleteButton.setImageResource(item.getImageResource());
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
