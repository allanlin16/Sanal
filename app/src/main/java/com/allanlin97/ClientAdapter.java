package com.allanlin97;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.allanlin97.ui.building.BuildingFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private ArrayList<ClientItem> clientList;

    public class ClientViewHolder extends  RecyclerView.ViewHolder {

        TextView cardViewname;
        TextView cardViewEmail;
        TextView cardViewPhone;
        TextView cardViewAddress;
        ImageView cardVieweditDeleteButton;
        RequestQueue requestQueue;

        public ClientViewHolder(final View itemView) {
            super(itemView);
            cardViewname = itemView.findViewById(R.id.clientName);
            cardViewEmail = itemView.findViewById(R.id.clientEmail);
            cardViewPhone = itemView.findViewById(R.id.clientPhoneNumber);
            cardViewAddress = itemView.findViewById(R.id.clientAddress);

            requestQueue = Volley.newRequestQueue(itemView.getContext());
            cardVieweditDeleteButton =  itemView.findViewById(R.id.menuButton);
            cardVieweditDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(itemView.getContext(), v);

                    /** Adding menu items to the popumenu */
                    popup.getMenuInflater().inflate(R.menu.edit_delete_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // TODO Auto-generated method stub
                            switch (item.getItemId()) {
                                case R.id.edit:

                                    final View dialogView = View.inflate(itemView.getContext(),R.layout.update_client,null);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                                    alertDialogBuilder.setMessage("Update Client");

                                    // set up the edit text
                                    final EditText name =  dialogView.findViewById(R.id.editName);
                                    final EditText address = dialogView.findViewById(R.id.editAddress);
                                    final EditText phone =  dialogView.findViewById(R.id.editPhone);
                                    final EditText email =  dialogView.findViewById(R.id.editEmail);


                                    alertDialogBuilder.setPositiveButton("Update",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                    RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                                                    JSONObject object = new JSONObject();
                                                    try {
                                                        //input your API parameters
                                                        object.put("client_name", name.getText().toString());
                                                        object.put("client_address",address.getText().toString());
                                                        object.put("client_phone",phone.getText().toString());
                                                        object.put("client_email",email.getText().toString());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    // Enter the correct url for your api service site
                                                    String url = "https://alin.scweb.ca/SanalAPI/api/client/"+clientList.get(getAdapterPosition()).getId();
                                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    Toast.makeText(itemView.getContext(), "Client Updated!", Toast.LENGTH_LONG).show();
                                                                    cardViewname.setText(name.getText().toString());
                                                                    cardViewAddress.setText(address.getText().toString());
                                                                    cardViewPhone.setText(phone.getText().toString());
                                                                    cardViewEmail.setText(email.getText().toString());

                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            error.printStackTrace();
                                                        }
                                                    });
                                                    requestQueue.add(jsonObjectRequest);
                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.setView(dialogView);
                                    alertDialog.show();


                                    break;

                                case R.id.delete:
                                    Long client_id = clientList.get(getAdapterPosition()).getId();
                                    RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                                    JSONObject object = new JSONObject();
                                    removeAt(getAdapterPosition());
                                    String url = "https://alin.scweb.ca/SanalAPI/api/client/"+client_id;
                                    System.out.println("deleted" + client_id);
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Toast.makeText(itemView.getContext(), "Client Deleted!", Toast.LENGTH_LONG).show();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });
                                    requestQueue.add(jsonObjectRequest);
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
                    final AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    final BuildingFragment myFragment = new BuildingFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putString("clientName", clientList.get(getAdapterPosition()).getClientName());
                    bundle.putLong("clientId", clientList.get(getAdapterPosition()).getId());
                    //System.out.println("" + clientList.get(getAdapterPosition()).getId());

                    myFragment.setArguments(bundle);

                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, myFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    public ClientAdapter(ArrayList<ClientItem> ClientList) {
        clientList = ClientList;
    }

    //
    public void removeAt(int position) {
        clientList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, clientList.size());
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

        holder.cardVieweditDeleteButton.setImageResource(item.getImageResource());
        holder.cardViewname.setText(item.getClientName());
        holder.cardViewEmail.setText(item.getClientEmail());
        holder.cardViewPhone.setText(item.getClientPhoneNumber());
        holder.cardViewAddress.setText(item.getClientAddress());
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

}
