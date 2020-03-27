package com.allanlin97;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<BuildingItem> buildingDetails;
    private HashMap<Long, List<String>> buildingIdExtinguisher;

    public CustomExpandableListAdapter(Context context, List<BuildingItem> buildingDetails,
                                       HashMap<Long, List<String>> buildingIdExtinguisher) {
        this.context = context;
        this.buildingDetails = buildingDetails;
        this.buildingIdExtinguisher = buildingIdExtinguisher;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.buildingIdExtinguisher.get(Long.valueOf(this.buildingDetails.get(listPosition).getId()))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);




        return convertView;
    }


    @Override
    public int getChildrenCount(int listPosition) {
        return this.buildingIdExtinguisher.get(Long.valueOf(this.buildingDetails.get(listPosition).getId())).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.buildingDetails.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.buildingDetails.size();
    }

    @Override
    public long getGroupId(int listPosition)  {
        return listPosition;
    }

    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {

        buildingDetails.get(listPosition).getBuildingName();
        buildingDetails.get(listPosition).getBuildingAddress();
        buildingDetails.get(listPosition).getBuildingCity();
        buildingDetails.get(listPosition).getBuildingPostalCode();

        String buildingD = buildingDetails.get(listPosition).getBuildingName() + " " +
                buildingDetails.get(listPosition).getBuildingAddress() + " " +
                buildingDetails.get(listPosition).getBuildingCity()+ " " +
                buildingDetails.get(listPosition).getBuildingPostalCode();

        //buildingD = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        ImageView buildingEdit = convertView.findViewById(R.id.buildingMenuButton);
        buildingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(parent.getContext(), view);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.edit_delete_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.edit:

                                final View dialogView = View.inflate(parent.getContext(),R.layout.update_building,null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent.getContext());
                                alertDialogBuilder.setMessage("Update Building");

                                // set up the edit text
                                final EditText buildingName =  dialogView.findViewById(R.id.editBuildingName);
                                final EditText buildingAddress = dialogView.findViewById(R.id.editBuildingAddress);
                                final EditText buildingCity =  dialogView.findViewById(R.id.editBuildingCity);
                                final EditText buildingPostalCode =  dialogView.findViewById(R.id.editBuildingPostalCode);


                                alertDialogBuilder.setPositiveButton("Update",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                RequestQueue requestQueue = Volley.newRequestQueue(parent.getContext());
                                                JSONObject object = new JSONObject();
                                                try {
                                                    //input your API parameters
                                                    object.put("building_name", buildingName.getText().toString());
                                                    object.put("building_address", buildingAddress.getText().toString());
                                                    object.put("building_city", buildingCity.getText().toString());
                                                    object.put("building_postalcode", buildingPostalCode.getText().toString());
                                                    object.put("user_id",6);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                // Enter the correct url for your api service site
                                                String url = "https://alin.scweb.ca/SanalAPI/api/building/"+buildingDetails.get(listPosition).getId();
                                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {
                                                                Toast.makeText(parent.getContext(), "Building Updated!", Toast.LENGTH_LONG).show();
                                                                //TODO: update the expanablelistview
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
                                RequestQueue requestQueue = Volley.newRequestQueue(parent.getContext());
                                JSONObject object = new JSONObject();
                                // TODO: remove from adapter expandableListTitle.removeAll();
                                String url = "https://alin.scweb.ca/SanalAPI/api/building/"+buildingDetails.get(listPosition).getId();
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

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
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(buildingD);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

