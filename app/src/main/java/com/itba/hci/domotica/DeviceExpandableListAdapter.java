package com.itba.hci.domotica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class DeviceExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // nombreDeDisp
    private HashMap<String, Device> listDataChild; // nombreDeDisp -> dispositivo

    public DeviceExpandableListAdapter(Context context, List<String> listDataHeader,
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

        Device device = listDataChild.get(listDataHeader.get(i));

        switch (listDataChild.get(listDataHeader.get(i)).getType()){
            // Devices
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

    @SuppressLint("ClickableViewAccessibility")
    private View prepareAC(Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.ac_content,null);


        // state
        Switch state = view.findViewById(R.id.ac_state);
        state.setChecked(true); //todo: el boolean hay que sacarlo de la api

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });


        // temperature
        SeekBar temperature = view.findViewById(R.id.ac_temperature_seek_bar);
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        // Mode
        Spinner modeSpinner = (Spinner) view.findViewById(R.id.ac_mode_bar);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar modo de ac en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> modeList = new ArrayList<String>();
        modeList.add(context.getResources().getString(R.string.set_ac_mode_option1));
        modeList.add(context.getResources().getString(R.string.set_ac_mode_option2));
        modeList.add(context.getResources().getString(R.string.set_ac_mode_option3));

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, modeList);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);


        // V Swing
        Spinner vSwingSpinner = (Spinner) view.findViewById(R.id.vertical_swing_bar);
        vSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar vertical swing de ac en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> vSwingList = new ArrayList<String>();
        vSwingList.add(context.getResources().getString(R.string.set_vswing_option1));
        vSwingList.add(context.getResources().getString(R.string.set_vswing_option2));
        vSwingList.add(context.getResources().getString(R.string.set_vswing_option3));
        vSwingList.add(context.getResources().getString(R.string.set_vswing_option4));
        vSwingList.add(context.getResources().getString(R.string.set_vswing_option5));

        ArrayAdapter<String> vSwingAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, vSwingList);
        vSwingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vSwingSpinner.setAdapter(vSwingAdapter);


        // H Swing
        Spinner hSwingSpinner = (Spinner) view.findViewById(R.id.horizontal_swing_bar);
        hSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar horizontal swing de ac en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> hSwingList = new ArrayList<String>();
        hSwingList.add(context.getResources().getString(R.string.set_hswing_option1));
        hSwingList.add(context.getResources().getString(R.string.set_hswing_option2));
        hSwingList.add(context.getResources().getString(R.string.set_hswing_option3));
        hSwingList.add(context.getResources().getString(R.string.set_hswing_option4));
        hSwingList.add(context.getResources().getString(R.string.set_hswing_option5));
        hSwingList.add(context.getResources().getString(R.string.set_hswing_option6));

        ArrayAdapter<String> hSwingAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hSwingList);
        hSwingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hSwingSpinner.setAdapter(hSwingAdapter);

        // Fan speed
        Spinner fanSpeedSpinner = (Spinner) view.findViewById(R.id.fan_speed_bar);
        fanSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar fan speed de ac en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> fanSpeedList = new ArrayList<String>();
        fanSpeedList.add(context.getResources().getString(R.string.set_fanspeed_option1));
        fanSpeedList.add(context.getResources().getString(R.string.set_fanspeed_option2));
        fanSpeedList.add(context.getResources().getString(R.string.set_fanspeed_option3));
        fanSpeedList.add(context.getResources().getString(R.string.set_fanspeed_option4));
        fanSpeedList.add(context.getResources().getString(R.string.set_fanspeed_option5));

        ArrayAdapter<String> fanSpeedAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, fanSpeedList);
        fanSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fanSpeedSpinner.setAdapter(fanSpeedAdapter);
        return view;
    }

    private View prepareAlarm(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.alarm_content,null);

        // capaz no lo implementamos

        return view;
    }

    private View prepareBlind(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.blind_content,null);

        Switch blind = view.findViewById(R.id.blind_switch);
        blind.setChecked(true); //todo: el boolean hay que sacarlo de la api

        blind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private View prepareDoor(Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.door_content,null);

        Switch doorSwitch = view.findViewById(R.id.door_switch);
        doorSwitch.setChecked(true); //todo: el boolean hay que sacarlo de la api

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });


        Switch lock = view.findViewById(R.id.door_lock_switch);
        lock.setChecked(true); //todo: el boolean hay que sacarlo de la api

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private View prepareLamp(Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.lamp_content,null);

        Switch state = view.findViewById(R.id.lamp_switch);
        state.setChecked(true); //todo: el boolean hay que sacarlo de la api

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });


        SeekBar brightness = view.findViewById(R.id.lamp_brightness_seekBar);
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.brightness_text);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        return view;
    }

    private View prepareOven(Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.oven_content,null);

        Switch state = view.findViewById(R.id.oven_switch);
        state.setChecked(true); //todo: el boolean hay que sacarlo de la api

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });


        SeekBar temperature = view.findViewById(R.id.oven_temperature_seek_bar);
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.oven_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        Spinner heatDirSpinner = (Spinner) view.findViewById(R.id.heatDirSpinner);
        heatDirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar heat dir de oven en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> heatDirList = new ArrayList<String>();
        heatDirList.add(context.getResources().getString(R.string.set_heat_option1));
        heatDirList.add(context.getResources().getString(R.string.set_heat_option2));
        heatDirList.add(context.getResources().getString(R.string.set_heat_option3));

        ArrayAdapter<String> heatDirAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, heatDirList);
        heatDirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heatDirSpinner.setAdapter(heatDirAdapter);


        Spinner grillTypeSpinner = (Spinner) view.findViewById(R.id.grill_type_spinner);
        grillTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> grillTypeList = new ArrayList<String>();
        grillTypeList.add(context.getResources().getString(R.string.set_grill_option1));
        grillTypeList.add(context.getResources().getString(R.string.set_grill_option2));
        grillTypeList.add(context.getResources().getString(R.string.set_grill_option3));

        ArrayAdapter<String> grillTypeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, grillTypeList);
        grillTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grillTypeSpinner.setAdapter(grillTypeAdapter);


        Spinner convectionSpinner = (Spinner) view.findViewById(R.id.convection_spinner);
        convectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar fan speed de ac en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> convectionList = new ArrayList<String>();
        convectionList.add(context.getResources().getString(R.string.set_convection_option1));
        convectionList.add(context.getResources().getString(R.string.set_convection_option2));
        convectionList.add(context.getResources().getString(R.string.set_convection_option3));

        ArrayAdapter<String> convectionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, convectionList);
        convectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        convectionSpinner.setAdapter(convectionAdapter);
        return view;
    }

    private View prepareRefigerator(Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.refrigerator_content,null);

        SeekBar temperature = view.findViewById(R.id.refrigerator_temperature_seekbar);
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.refrigerator_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        SeekBar freezerTemperature = view.findViewById(R.id.refrigerator_freezer_temperature_seekbar);
        freezerTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.refrigerator_freezer_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        Spinner refrigeratorModeSpinner = (Spinner) view.findViewById(R.id.refrigerator_mode_spinner);
        refrigeratorModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar modo de refrigerator en la api
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> refrModeList = new ArrayList<String>();
        refrModeList.add(context.getResources().getString(R.string.set_refrigerator_mode_option1));
        refrModeList.add(context.getResources().getString(R.string.set_refrigerator_mode_option2));
        refrModeList.add(context.getResources().getString(R.string.set_refrigerator_mode_option3));

        ArrayAdapter<String> refrModeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, refrModeList);
        refrModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refrigeratorModeSpinner.setAdapter(refrModeAdapter);
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