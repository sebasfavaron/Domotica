package com.itba.hci.domotica;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

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

        switch (listDataChild.get(listDataHeader.get(i)).getType()){
            case "ac": view = prepareAC(i, inflater); break;
            case "alarm": view = prepareAlarm(i, inflater); break;
            case "blind": view = prepareBlind(i, inflater); break;
            case "door": view = prepareDoor(i, inflater); break;
            case "lamp": view = prepareLamp(i, inflater); break;
            case "oven": view = prepareOven(i, inflater); break;
            case "refrigerator": view = prepareRefigerator(i, inflater); break;
            case "timer": view = prepareTimer(i, inflater); break;
            default: view = inflater.inflate(R.layout.item_content,null);
        }


        return view;
    }

    private View prepareAC(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.ac_content,null);



        return view;
    }

    private View prepareAlarm(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.alarm_content,null);



        return view;
    }

    private View prepareBlind(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.blind_content,null);



        return view;
    }

    private View prepareDoor(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.door_content,null);

        listDataChild.get(listDataHeader.get(i));

        return view;
    }

    private View prepareLamp(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.lamp_content,null);



        return view;
    }

    private View prepareOven(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.oven_content,null);



        return view;
    }

    private View prepareRefigerator(int i, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.refrigerator_content,null);



        return view;
    }

    private View prepareTimer(int i, LayoutInflater inflater){
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
            view = inflater.inflate(R.layout.item_content,null);
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
