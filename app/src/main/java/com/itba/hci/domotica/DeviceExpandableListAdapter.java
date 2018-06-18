package com.itba.hci.domotica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.VolleyError;


public class DeviceExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // nombreDeDisp
    private HashMap<String, Device> listDataChild; // nombreDeDisp -> dispositivo
    private String requestTag;

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
        ImageView headerImage = (ImageView) view.findViewById(R.id.header_img);
        TextView header = (TextView) view.findViewById(R.id.lblListHeader);
        header.setText(getGroup(i).toString());
        Bitmap bitmap = null;
        Device device = listDataChild.get(listDataHeader.get(i));
        if(device == null) headerImage.setImageResource(R.drawable.ic_launcher_foreground);
        else {
            switch (device.getType()) {
                case "ac":
                    headerImage.setImageResource(R.drawable.aireacondicionado);
                    break;
                case "blind":
                    headerImage.setImageResource(R.drawable.persiana);
                    break;
                case "door":
                    headerImage.setImageResource(R.drawable.puerta);
                    break;
                case "lamp":
                    headerImage.setImageResource(R.drawable.lampara);
                    break;
                case "oven":
                    headerImage.setImageResource(R.drawable.horno);
                    break;
                case "refrigerator":
                    headerImage.setImageResource(R.drawable.heladera);
                    break;
                default:
                    headerImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

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

        switch (device.getType()){
            // Devices
            case "ac": view = prepareAC(device, inflater); break;
            case "blind": view = prepareBlind(device, inflater); break;
            case "door": view = prepareDoor(device, inflater); break;
            case "lamp": view = prepareLamp(device, inflater); break;
            case "oven": view = prepareOven(device, inflater); break;
            case "refrigerator": view = prepareRefigerator(device, inflater); break;
            default: view = inflater.inflate(R.layout.default_item_content,null);
        }

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private View prepareAC(final Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.ac_content,null);

        String requestTag = Api.getInstance(context).getAcState(device,new Response.Listener<AcState>() {
            @Override
            public void onResponse(AcState response) {
                //response contiene los datos.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        // state
        final Switch state = view.findViewById(R.id.ac_state);
        state.setChecked(true); //todo: el boolean hay que sacarlo de la api

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                String check;
                if(isChecked){
                    check = "turnOn";
                }else {
                    check = "turnOff";
                }
                String requestTag = Api.getInstance(context).deviceAction(device,check,"[]",new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        state.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });
            }
        });


        // temperature
        SeekBar temperature = view.findViewById(R.id.ac_temperature_seek_bar);
        final int[] lastTemperature = new int[1];
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                tempText.setText(String.valueOf(progress));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastTemperature[0] = seekBar.getProgress();
            }
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

                String body = "["+ String.valueOf(seekBar.getProgress()) + "]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setTemperature",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastTemperature[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastTemperature[0]);
                        TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                        tempText.setText(String.valueOf(lastTemperature[0]));
                    }
                });
            }
        });


        // Mode
        final Spinner modeSpinner = (Spinner) view.findViewById(R.id.ac_mode_bar);
        int lastModeSpinner;
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("tag", modeSpinner.getChildAt(position).toString());
                Log.d("tag", modeSpinner.getSelectedItem().toString());
                String body = "[]";
                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setMode",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastModeSpinner = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        modeSpinner.setSelection(lastModeSpinner);

                        Log.e("tag", error.toString());
                    }
                });
                */
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
        final Spinner vSwingSpinner = (Spinner) view.findViewById(R.id.vertical_swing_bar);
        vSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int lastvSwingSpinner;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar vertical swing de ac en la api
                Log.d("tag", vSwingSpinner.getChildAt(position).toString());
                Log.d("tag", vSwingSpinner.getSelectedItem().toString());
                String body = "[]";
                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setVerticalSwing",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastvSwingSpinner = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        vSwingSpinner.setSelection(lastvSwingSpinner);
                        Log.e("tag", error.toString());
                    }
                });
                */
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
        final Spinner hSwingSpinner = (Spinner) view.findViewById(R.id.horizontal_swing_bar);
        int lasthSwingSpinner;

        hSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar horizontal swing de ac en la api
                Log.d("tag", hSwingSpinner.getChildAt(position).toString());
                Log.d("tag", hSwingSpinner.getSelectedItem().toString());
                String body = "[]";
                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setHorizontalSwing",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lasthSwingSpinner = position;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        hSwingSpinner.setSelection(lasthSwingSpinner);
                        Log.e("tag", error.toString());
                    }
                });
                */
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
        int lastFanSpeed;
        fanSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar fan speed de ac en la api
            Log.d("tag", hSwingSpinner.getChildAt(position).toString());
            Log.d("tag", hSwingSpinner.getSelectedItem().toString());
            String body = "[]";
                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setFanSpeed",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lasthFanSpeed = position;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        hSwingSpinner.setSelection(lasthFanSpeed);
                        Log.e("tag", error.toString());
                    }
                });
                */
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

    private View prepareBlind(final Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.blind_content,null);


        String requestTag = Api.getInstance(context).getBlindState(device,new Response.Listener<BlindState>() {
            @Override
            public void onResponse(BlindState response) {
                //response contiene los datos.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());

            }
        });

        final Switch blind = view.findViewById(R.id.blind_switch);
        blind.setChecked(true); //todo: el boolean hay que sacarlo de la api

        blind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();

                String check;
                if(isChecked){
                    check = "up";
                }else {
                    check = "down";
                }
                String requestTag = Api.getInstance(context).deviceAction(device,check,"[]",new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        blind.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });

            }
        });

        return view;
    }

    private View prepareDoor(final Device device, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.door_content,null);

        String requestTag = Api.getInstance(context).getDoorState(device,new Response.Listener<DoorState>() {
            @Override
            public void onResponse(DoorState response) {
                //response contiene los datos.

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        final Switch doorSwitch = view.findViewById(R.id.door_switch);
        doorSwitch.setChecked(true); //todo: el boolean hay que sacarlo de la api

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                //todo: actualizar la api
                String check;
                if(isChecked){
                    check = "open";
                }else {
                    check = "close";
                }
                String requestTag = Api.getInstance(context).deviceAction(device,check,"[]",new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        doorSwitch.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });

                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });


        final Switch lock = view.findViewById(R.id.door_lock_switch);
        lock.setChecked(true); //todo: el boolean hay que sacarlo de la api

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                //todo: actualizar la api
                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
                String check;
                if(isChecked){
                    check = "lock";
                }else {
                    check = "unlock";
                }
                String requestTag = Api.getInstance(context).deviceAction(device,check,"[]",new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        lock.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });

            }
        });

        return view;
    }

    private View prepareLamp(final Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.lamp_content,null);

        String requestTag = Api.getInstance(context).getLampState(device,new Response.Listener<LampState>() {
            @Override
            public void onResponse(LampState response) {
                //response contiene los datos.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        Switch state = view.findViewById(R.id.lamp_switch);
        state.setChecked(true); //todo: el boolean hay que sacarlo de la api

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api

                String check;
                if(isChecked){
                    check = "turnOn";
                }else {
                    check = "turnOff";
                }
                String requestTag = Api.getInstance(context).deviceAction(device,check,"[]",new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        //state.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });

                Toast.makeText(context,""+isChecked,Toast.LENGTH_SHORT).show();
            }
        });


        SeekBar brightness = view.findViewById(R.id.lamp_brightness_seekBar);
        final int[] lastBrightness = new int[1];

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.brightness_text);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastBrightness[0] = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                String body = "["+ String.valueOf(seekBar.getProgress()) + "]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setBrightness",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastBrightness[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastBrightness[0]);
                        TextView tempText = (TextView) view.findViewById(R.id.brightness_text);
                        tempText.setText(String.valueOf(lastBrightness[0]));
                    }
                });
            }
        });

        ((Button) view.findViewById(R.id.red_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Red);
                String body = "["+ color +"]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setColor",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.brown_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Red);
                String body = "["+ color +"]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setColor",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.blue_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Blue);
                String body = "["+ color +"]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setColor",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.green_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Green);
                String body = "["+ color +"]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setColor",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.grey_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Grey);
                String body = "["+ color +"]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setColor",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.purple_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Purple);
                String body = "["+ color +"]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setColor",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                    }
                });
            }
        });
        return view;
    }

    private View prepareOven(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.oven_content, null);

        String requestTag = Api.getInstance(context).getOvenState(device, new Response.Listener<OvenState>() {
            @Override
            public void onResponse(OvenState response) {
                //response contiene los datos.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        Switch state = view.findViewById(R.id.oven_switch);
        state.setChecked(true); //todo: el boolean hay que sacarlo de la api

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo: actualizar la api
                String check;
                if (isChecked) {
                    check = "turnOn";
                } else {
                    check = "turnOff";
                }
                String requestTag = Api.getInstance(context).deviceAction(device, check, "[]", new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        //state.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });
            }
        });


        SeekBar temperature = view.findViewById(R.id.oven_temperature_seek_bar);
        final int[] lastTemperature = new int[1];
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.oven_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastTemperature[0] = seekBar.getProgress();
            }
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                String body = "["+ String.valueOf(seekBar.getProgress()) + "]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setTemperature",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastTemperature[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastTemperature[0]);
                        TextView tempText = (TextView) view.findViewById(R.id.oven_temperature);
                        tempText.setText(String.valueOf(lastTemperature[0]));
                    }
                });
            }
        });


        final Spinner heatDirSpinner = (Spinner) view.findViewById(R.id.heatDirSpinner);
        int lastheatDirSpinner;
        heatDirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar heat dir de oven en la api
                Log.d("tag", heatDirSpinner.getChildAt(position).toString());
                Log.d("tag", heatDirSpinner.getSelectedItem().toString());
                String body = "[]";

                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setHeat",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastheatDirSpinner = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        modeSpinner.setSelection(lastheatDirSpinner);

                        Log.e("tag", error.toString());
                    }
                });
                */
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


        final Spinner grillTypeSpinner = (Spinner) view.findViewById(R.id.grill_type_spinner);
        int lastgrillTypeSpinner;
        grillTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar la api
                Log.d("tag", grillTypeSpinner.getChildAt(position).toString());
                Log.d("tag", grillTypeSpinner.getSelectedItem().toString());
                String body = "[]";

                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setGrill",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastgrillTypeSpinner = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        modeSpinner.setSelection(lastgrillTypeSpinner);

                        Log.e("tag", error.toString());
                    }
                });
                */
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


        final Spinner convectionSpinner = (Spinner) view.findViewById(R.id.convection_spinner);
        int lastconvectionSpinner;
        convectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar fan speed de ac en la api
                Log.d("tag", convectionSpinner.getChildAt(position).toString());
                Log.d("tag", convectionSpinner.getSelectedItem().toString());
                String body = "[]";

                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setConvection",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastconvectionSpinner = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        modeSpinner.setSelection(lastconvectionSpinner);

                        Log.e("tag", error.toString());
                    }
                });
                */
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

    private View prepareRefigerator(final Device device, LayoutInflater inflater){
        final View view = inflater.inflate(R.layout.refrigerator_content,null);

        String requestTag = Api.getInstance(context).getRefrigeratorState(device,new Response.Listener<RefrigeratorState>() {
            @Override
            public void onResponse(RefrigeratorState response) {
                //response contiene los datos.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        SeekBar temperature = view.findViewById(R.id.refrigerator_temperature_seekbar);
        final int[] lastTemperature = new int[1];
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.refrigerator_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastTemperature[0] = seekBar.getProgress();
            }
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                String body = "["+ String.valueOf(seekBar.getProgress()) + "]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setTemperature",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastTemperature[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastTemperature[0]);
                        TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                        tempText.setText(String.valueOf(lastTemperature[0]));
                    }
                });
            }
        });


        SeekBar freezerTemperature = view.findViewById(R.id.refrigerator_freezer_temperature_seekbar);
        final int[] lastfreezerTemperature = new int[1];

        freezerTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.refrigerator_freezer_temperature);
                tempText.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastfreezerTemperature[0] = seekBar.getProgress();
            }
            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                String body = "["+ String.valueOf(seekBar.getProgress()) + "]";
                String requestTag = Api.getInstance(context).deviceAction(device,"setFreezerTemperature",body,new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastfreezerTemperature[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastfreezerTemperature[0]);
                        TextView tempText = (TextView) view.findViewById(R.id.refrigerator_freezer_temperature);
                        tempText.setText(String.valueOf(lastfreezerTemperature[0]));
                    }
                });
            }
        });


        final Spinner refrigeratorModeSpinner = (Spinner) view.findViewById(R.id.refrigerator_mode_spinner);
        int lastrefrigerator;
        refrigeratorModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo: actualizar modo de refrigerator en la api
                Log.d("tag", refrigeratorModeSpinner.getChildAt(position).toString());
                Log.d("tag", refrigeratorModeSpinner.getSelectedItem().toString());
                String body = "[]";
                /*
                String requestTag = Api.getInstance(context).deviceAction(device,"setMode",body,new Response.Listener<Boolean>() {

                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        lastrefrigerator = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //todo Agregar SnackBar, que no se cambio
                        modeSpinner.setSelection(lastrefrigerator);

                        Log.e("tag", error.toString());
                    }
                });
                */
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

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
