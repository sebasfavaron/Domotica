package com.itba.hci.domotica;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavRoutineExpandableListAdapter extends BaseExpandableListAdapter{

    private Context context;
    private List<String> listDataHeader; // nombreDeDisp
    private HashMap<String, Routine> listDataChild; // nombreDeRut -> dispositivo

    public FavRoutineExpandableListAdapter(Context context, HashMap<String, Routine> listDataChild) {
        this.context = context;
        setList(listDataChild);

    }

    public void setList(HashMap<String,Routine> routineHashMap){
        int MAX = 5;

        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();

        //todo: filtrar para que sean los favoritos y no haya mas que MAX
        for(String deviceName : routineHashMap.keySet()){
            if(listDataChild.size() >= MAX) break;
            //if(es favorito)
            listDataChild.put(deviceName,routineHashMap.get(deviceName));
        }

        listDataHeader.addAll(listDataChild.keySet());
        notifyDataSetChanged();
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
        if(inflater == null) {
            //todo: error nuestro que hay que notificarle al usuario. No se va a ver la lista
            return view;
        }

        view = inflater.inflate(R.layout.item_header,null); //item_header es la cosa antes de expandir

        TextView header = (TextView) view.findViewById(R.id.lblListHeader);
        header.setText(getGroup(i).toString());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater == null) {
            //todo: error nuestro que hay que notificarle al usuario. No se va a ver el elemento
            return view;
        }

        Routine routine = listDataChild.get(listDataHeader.get(i));

        return prepareRoutine(routine, inflater);
    }

    private View prepareRoutine(Routine routine, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.routine_content,null);

        TextView title = view.findViewById(R.id.routine_title);
        title.setText(routine.getName());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
