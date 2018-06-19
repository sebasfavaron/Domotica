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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class RoutineListAdapter extends ArrayAdapter<Routine> {

    private Context context;
    private ArrayList<Routine> routineList;

    public RoutineListAdapter(@NonNull Context context, ArrayList<Routine> list) {
        super(context, R.layout.routine_header, list);
        this.context = context;
        routineList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater != null ? inflater.inflate(R.layout.routine_header, null) : view;

        final String name = routineList.get(position).getName();
        TextView header = (TextView) view.findViewById(R.id.lblListHeader);
        header.setText(name);

        ((Button) view.findViewById(R.id.play_routine_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,context.getString(R.string.routine_msg)+name,Toast.LENGTH_LONG).show();
                //todo: correr la rutina aca

            }
        });

        return view;
    }
}
