package com.itba.hci.domotica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // nombreDeDisp

    private HashMap<String, Device> listDataChild; // nombreDeDisp -> dispositivo

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, Device> listDataChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1; //solo tiene un hijo, la instancia de Device
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listDataChild.get(listDataHeader.get(i));
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_header,null); //item_header lo que muestra la lista de dispositivos

        TextView header = (TextView) view.findViewById(R.id.lblListHeader);
        header.setText(getGroup(i).toString());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater == null) return view;

        Device device = listDataChild.get(listDataHeader.get(i));

        switch (listDataChild.get(listDataHeader.get(i)).getType()){
            case "ac": view = prepareAC(device, inflater); break;
            case "alarm": view = prepareAlarm(device, inflater); break;
            case "blind": view = prepareBlind(device, inflater); break;
            case "door": view = prepareDoor(device, inflater); break;
            case "lamp": view = prepareLamp(device, inflater); break;
            case "oven": view = prepareOven(device, inflater); break;
            case "refrigerator": view = prepareRefigerator(device, inflater); break;
            case "timer": view = prepareTimer(device, inflater); break;
            default: view = inflater.inflate(R.layout.default_item_content,null);
        }


        return view;
    }

    private View prepareAC(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.ac_content,null);



        Spinner spinner = view.findViewById(R.id.add_device_type_spinner);
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        List<String> selectionList = new ArrayList<>();
        /*selectionList.add(context.getResources().getString(R.string.set_ac_mode_option1));
        selectionList.add(context.getResources().getString(R.string.set_ac_mode_option2));
        selectionList.add(context.getResources().getString(R.string.set_ac_mode_option3));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, selectionList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        return view;
    }

    private View prepareAlarm(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.alarm_content,null);



        return view;
    }

    private View prepareBlind(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.blind_content,null);



        return view;
    }

    private View prepareDoor(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.door_content,null);

        Switch doorSwitch = view.findViewById(R.id.door_switch);
        doorSwitch.setChecked(true); //el boolean hay que sacarlo de la api

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private View prepareLamp(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.lamp_content,null);



        return view;
    }

    private View prepareOven(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.oven_content,null);



        return view;
    }

    private View prepareRefigerator(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.refrigerator_content,null);



        return view;
    }

    private View prepareTimer(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.timer_content,null);



        return view;
    }

    /*
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_header,null);
        }
        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i,i1);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.default_item_content,null);
        }

        TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return view;
    }
    */

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
