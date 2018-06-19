package com.itba.hci.domotica;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FavRoutineListAdapter extends ArrayAdapter<Routine> {
    private Context context;

    private ArrayList<Routine> routineList;

    public FavRoutineListAdapter(@NonNull Context context, ArrayList<Routine> list) {
        super(context, R.layout.routine_header, list);
        this.context = context;
        setList(list);
    }

    public void setList(ArrayList<Routine> newRoutineList){
        int MAX = 5;

        routineList = new ArrayList<>();

        //todo: filtrar para que sean los favoritos
        for(Routine routine : newRoutineList){
            if(routineList.size() >= MAX) break;
            //if(es favorito)
            routineList.add(routine);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater != null ? inflater.inflate(R.layout.routine_header, null) : view;

        TextView header = (TextView) view.findViewById(R.id.lblListHeader);
        header.setText(routineList.get(position).getName());

        ((Button) view.findViewById(R.id.play_routine_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: correr la rutina aca

            }
        });

        return view;
    }
}
