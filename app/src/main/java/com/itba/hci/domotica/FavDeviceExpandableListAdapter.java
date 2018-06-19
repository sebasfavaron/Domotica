package com.itba.hci.domotica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavDeviceExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // nombreDeDisp
    private HashMap<String, Device> listDataChild; // nombreDeDisp -> dispositivo
    private String requestTag;

    public FavDeviceExpandableListAdapter(Context context, List<String> listDataHeader,
                                       HashMap<String, Device> listDataChild) {
        this.context = context;
        updateList(listDataChild);
    }

    public void updateList(HashMap<String,Device> deviceHashMap){
        int MAX = 5;

        listDataChild = new HashMap<>();
        listDataHeader = new ArrayList<>();
        Log.d("tag","updateListFav "+listDataChild.keySet().toString());

        if(deviceHashMap == null) return;

        //todo: filtrar para que sean los favoritos
        for(String deviceName : deviceHashMap.keySet()){
            if(listDataChild.size() >= MAX) break;
            //if(es favorito)
            listDataChild.put(deviceName,deviceHashMap.get(deviceName));
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            //todo: error nuestro que hay que notificarle al usuario. No se va a ver la lista
            return view;
        }

        view = inflater.inflate(R.layout.item_header, null); //item_header es la cosa antes de expandir
        ImageView headerImage = (ImageView) view.findViewById(R.id.header_img);
        TextView header = (TextView) view.findViewById(R.id.lblListHeader);
        header.setText(getGroup(i).toString());
        Bitmap bitmap = null;
        Device device = listDataChild.get(listDataHeader.get(i));
        if (device == null) headerImage.setImageResource(R.drawable.ic_launcher_foreground);
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            //todo: error nuestro que hay que notificarle al usuario. No se va a ver el elemento
            return view;
        }

        Device device = listDataChild.get(listDataHeader.get(i));

        switch (device.getType()) {
            // Devices
            case "ac":
                view = prepareAC(device, inflater);
                break;
            case "blind":
                view = prepareBlind(device, inflater);
                break;
            case "door":
                view = prepareDoor(device, inflater);
                break;
            case "lamp":
                view = prepareLamp(device, inflater);
                break;
            case "oven":
                view = prepareOven(device, inflater);
                break;
            case "refrigerator":
                view = prepareRefigerator(device, inflater);
                break;
            default:
                view = inflater.inflate(R.layout.default_item_content, null);
        }

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private View prepareAC(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.ac_content, null);

        final Switch state = view.findViewById(R.id.ac_state);

        final SeekBar temperature = view.findViewById(R.id.ac_temperature_seek_bar);
        final int[] lastTemperature = new int[1];

        final Spinner modeSpinner = (Spinner) view.findViewById(R.id.ac_mode_bar);
        final int[] lastModeSpinner = new int[1];

        final Spinner vSwingSpinner = (Spinner) view.findViewById(R.id.vertical_swing_bar);
        final int[] lastvSwingSpinner = new int[1];

        final Spinner hSwingSpinner = (Spinner) view.findViewById(R.id.horizontal_swing_bar);
        final int[] lasthSwingSpinner = new int[1];

        final Spinner fanSpeedSpinner = (Spinner) view.findViewById(R.id.fan_speed_bar);
        final int[] lastFanSpeed = new int[1];

        // state
        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                ArrayList<Object> params = new ArrayList<Object>();
                String check;
                if (isChecked) {
                    check = "turnOn";
                } else {
                    check = "turnOff";
                }
                String requestTag = Api.getInstance(context).deviceAction2(device, check, params, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        state.setChecked(!isChecked);
                    }
                });
            }
        });


        // temperature
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                tempText.setText(String.valueOf(progress + 18));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastTemperature[0] = seekBar.getProgress() + 18;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(seekBar.getProgress() + 18);
                String requestTag = Api.getInstance(context).deviceAction3(device, "setTemperature", params, new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        //Toast.makeText(context,"").show();
                        lastTemperature[0] = seekBar.getProgress() + 18;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        seekBar.setProgress(lastTemperature[0] - 18,false);
                        TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                        tempText.setText(String.valueOf(lastTemperature[0]));
                    }
                });
            }
        });


        // Mode
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(Language.spanishToEnglish(modeSpinner.getSelectedItem().toString()) );

                String requestTag = Api.getInstance(context).deviceAction4(device, "setMode", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lastModeSpinner[0] = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        // modeSpinner.setSelection(lastModeSpinner[0],false);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> modeList = new ArrayList<String>();
        modeList.add(context.getResources().getString(R.string.set_ac_mode_option1));
        modeList.add(context.getResources().getString(R.string.set_ac_mode_option2));
        modeList.add(context.getResources().getString(R.string.set_ac_mode_option3));

        final ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, modeList);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);


        // V Swing

        vSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(Language.spanishToEnglish(vSwingSpinner.getSelectedItem().toString()));
                String requestTag = Api.getInstance(context).deviceAction4(device, "setVerticalSwing", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lastvSwingSpinner[0] = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        // vSwingSpinner.setSelection(lastvSwingSpinner[0],false);
                        Log.e("tag", error.toString());
                    }
                });

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

        final ArrayAdapter<String> vSwingAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, vSwingList);
        vSwingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vSwingSpinner.setAdapter(vSwingAdapter);


        // H Swing

        hSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(Language.spanishToEnglish(hSwingSpinner.getSelectedItem().toString()));

                String requestTag = Api.getInstance(context).deviceAction4(device, "setHorizontalSwing", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lasthSwingSpinner[0] = position;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        //hSwingSpinner.setSelection(lasthSwingSpinner[0],false);
                        Log.e("tag", error.toString());
                    }
                });

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

        final ArrayAdapter<String> hSwingAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hSwingList);
        hSwingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hSwingSpinner.setAdapter(hSwingAdapter);

        // Fan speed
        fanSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(Language.spanishToEnglish(fanSpeedSpinner.getSelectedItem().toString()));
                String requestTag = Api.getInstance(context).deviceAction4(device, "setFanSpeed", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lastFanSpeed[0] = position;

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        //fanSpeedSpinner.setSelection(lastFanSpeed[0],false);
                        Log.e("tag", error.toString());
                    }
                });
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

        final ArrayAdapter<String> fanSpeedAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, fanSpeedList);
        fanSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fanSpeedSpinner.setAdapter(fanSpeedAdapter);

        final String requestTag = Api.getInstance(context).getAcState(device, new Response.Listener<AcState>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(AcState response) {
                if (response.getStatus()== null || response.getStatus().equals("off")) {

                    state.setChecked(false);

                } else {
                    state.setChecked(true);
                }
                if (response.getTemperature() != null){
                    temperature.setProgress(response.getTemperature().intValue() + 18,false);
                }
                /*
                modeSpinner.setSelection(modeAdapter.getPosition(response.getMode()),false);
                hSwingSpinner.setSelection(hSwingAdapter.getPosition(response.getHorizontalSwing()),false);
                vSwingSpinner.setSelection(vSwingAdapter.getPosition(response.getVerticalSwing()),false);
                fanSpeedSpinner.setSelection(fanSpeedAdapter.getPosition(response.getFanSpeed()),false);
                */

                //response contiene los datos.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });


        return view;
    }

    private View prepareBlind(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.blind_content, null);


        final Switch blind = view.findViewById(R.id.blind_switch);

        blind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                //Toast.makeText(context, "" + isChecked, Toast.LENGTH_SHORT).show();
                ArrayList<Object> params = new ArrayList<Object>();

                String check;
                if (isChecked) {
                    check = "up";
                } else {
                    check = "down";
                }
                String requestTag = Api.getInstance(context).deviceAction2(device, check, params, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        blind.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });

            }
        });

        String requestTag = Api.getInstance(context).getBlindState(device, new Response.Listener<BlindState>() {
            @Override
            public void onResponse(BlindState response) {
                if (response.getStatus().toLowerCase() == "closed" || response.getStatus().toLowerCase() == "closing") {
                    blind.setChecked(false);
                } else {
                    blind.setChecked(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());

            }
        });


        return view;
    }

    private View prepareDoor(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.door_content, null);


        final Switch doorSwitch = view.findViewById(R.id.door_switch);

        doorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                ArrayList<Object> params = new ArrayList<Object>();
                String check;
                if (isChecked) {
                    check = "open";
                } else {
                    check = "close";
                }
                String requestTag = Api.getInstance(context).deviceAction2(device, check, params, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        doorSwitch.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });
            }
        });


        final Switch lock = view.findViewById(R.id.door_lock_switch);

        lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                ArrayList<Object> params = new ArrayList<Object>();
                String check;
                if (isChecked) {
                    check = "lock";
                } else {
                    check = "unlock";
                }
                String requestTag = Api.getInstance(context).deviceAction2(device, check, params, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        lock.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });

            }
        });

        String requestTag = Api.getInstance(context).getDoorState(device, new Response.Listener<DoorState>() {
            @Override
            public void onResponse(DoorState response) {
                if ( response.getStatus() == null||response.getStatus() == "close") {
                    doorSwitch.setChecked(false);
                } else {
                    doorSwitch.setChecked(true);
                }
                if (response.getLock() == null ||response.getLock().toLowerCase() == "lock") {
                    doorSwitch.setChecked(true);
                } else {
                    doorSwitch.setChecked(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        return view;
    }

    private View prepareLamp(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.lamp_content, null);


        final Switch state = view.findViewById(R.id.lamp_switch);

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                ArrayList<Object> params = new ArrayList<Object>();

                String check;
                if (isChecked) {
                    check = "turnOn";
                } else {
                    check = "turnOff";
                }
                String requestTag = Api.getInstance(context).deviceAction2(device, check, params, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        state.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });
            }
        });


        final SeekBar brightness = view.findViewById(R.id.lamp_brightness_seekBar);
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
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(seekBar.getProgress());
                String requestTag = Api.getInstance(context).deviceAction3(device, "setBrightness", params, new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        //Toast.makeText(context,"").show();
                        lastBrightness[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastBrightness[0],false);
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
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(color);
                String requestTag = Api.getInstance(context).deviceAction4(device, "setColor", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.brown_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Red);
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(color);
                String requestTag = Api.getInstance(context).deviceAction4(device, "setColor", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.blue_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Blue);
                ArrayList<Object> params = new ArrayList<>();

                params.add(color);
                String requestTag = Api.getInstance(context).deviceAction4(device, "setColor", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.green_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Green);
                ArrayList<Object> params = new ArrayList<>();

                params.add(color);

                String requestTag = Api.getInstance(context).deviceAction4(device, "setColor", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.grey_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Grey);
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(color);
                String requestTag = Api.getInstance(context).deviceAction4(device, "setColor", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        ((Button) view.findViewById(R.id.purple_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") String color = context.getResources().getString(R.color.Purple);
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(color);
                String requestTag = Api.getInstance(context).deviceAction4(device, "setColor", params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        String requestTag = Api.getInstance(context).getLampState(device, new Response.Listener<LampState>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(LampState response) {
                if (response.getStatus() == null ||response.getStatus().toLowerCase().equals("on")){
                    state.setChecked(true);
                }else {
                    state.setChecked(false);
                }

                if (response.getBrightness()!= null){
                    brightness.setProgress(response.getBrightness().intValue(),false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        return view;
    }

    private View prepareOven(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.oven_content, null);


        final Switch state = view.findViewById(R.id.oven_switch);

        state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                ArrayList<Object> params = new ArrayList<Object>();

                String check;
                if (isChecked) {
                    check = "turnOn";
                } else {
                    check = "turnOff";
                }
                String requestTag = Api.getInstance(context).deviceAction2(device, check, params, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        //Toast.makeText(context,"").show();
                        //Everything is bien! mandar texto que se ha prendido o apagado;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        state.setChecked(!isChecked);
                        Log.e("tag", error.toString());

                    }
                });
            }
        });


        final SeekBar temperature = view.findViewById(R.id.oven_temperature_seek_bar);
        final int[] lastTemperature = new int[1];
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.oven_temperature);
                tempText.setText(String.valueOf(progress + 90));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastTemperature[0] = seekBar.getProgress() + 90;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(seekBar.getProgress() + 90);
                String requestTag = Api.getInstance(context).deviceAction3(device, "setTemperature", params, new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        //Toast.makeText(context,"").show();
                        lastTemperature[0] = seekBar.getProgress() + 90;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastTemperature[0] - 90,false);
                        TextView tempText = (TextView) view.findViewById(R.id.oven_temperature);
                        tempText.setText(String.valueOf(lastTemperature[0]));
                    }
                });
            }
        });


        final Spinner heatDirSpinner = (Spinner) view.findViewById(R.id.heatDirSpinner);
        final int[] lastheatDirSpinner = new int[1];
        heatDirSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(Language.spanishToEnglish(heatDirSpinner.getSelectedItem().toString()));

                String requestTag = Api.getInstance(context).deviceAction4(device, "setHeat", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(context,"").show();
                        lastheatDirSpinner[0] = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> heatDirList = new ArrayList<String>();
        heatDirList.add(context.getResources().getString(R.string.set_heat_option1));
        heatDirList.add(context.getResources().getString(R.string.set_heat_option2));
        heatDirList.add(context.getResources().getString(R.string.set_heat_option3));

        final ArrayAdapter<String> heatDirAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, heatDirList);
        heatDirAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heatDirSpinner.setAdapter(heatDirAdapter);


        final Spinner grillTypeSpinner = (Spinner) view.findViewById(R.id.grill_type_spinner);
        final int[] lastgrillTypeSpinner = new int[1];
        grillTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(Language.spanishToEnglish(grillTypeSpinner.getSelectedItem().toString()));
                String requestTag = Api.getInstance(context).deviceAction4(device, "setGrill", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lastgrillTypeSpinner[0] = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> grillTypeList = new ArrayList<String>();
        grillTypeList.add(context.getResources().getString(R.string.set_grill_option1));
        grillTypeList.add(context.getResources().getString(R.string.set_grill_option2));
        grillTypeList.add(context.getResources().getString(R.string.set_grill_option3));

        final ArrayAdapter<String> grillTypeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, grillTypeList);
        grillTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grillTypeSpinner.setAdapter(grillTypeAdapter);


        final Spinner convectionSpinner = (Spinner) view.findViewById(R.id.convection_spinner);
        final int[] lastconvectionSpinner = new int[0];
        convectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();

                params.add(Language.spanishToEnglish(convectionSpinner.getSelectedItem().toString()));

                String requestTag = Api.getInstance(context).deviceAction4(device, "setConvection", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lastconvectionSpinner[0] = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> convectionList = new ArrayList<String>();
        convectionList.add(context.getResources().getString(R.string.set_convection_option1));
        convectionList.add(context.getResources().getString(R.string.set_convection_option2));
        convectionList.add(context.getResources().getString(R.string.set_convection_option3));

        final ArrayAdapter<String> convectionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, convectionList);
        convectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        convectionSpinner.setAdapter(convectionAdapter);


        final String requestTag = Api.getInstance(context).getOvenState(device, new Response.Listener<OvenState>() {
            @Override
            public void onResponse(OvenState response) {
                if (response.getStatus() == null ||response.getStatus().toLowerCase().equals("off")){
                    state.setChecked(false);
                } else {
                    state.setChecked(true);
                }

                if (response.getTemperature()!=null){
                    temperature.setProgress(response.getTemperature() - 90,false);
                }

                /*
                heatDirSpinner.setSelection(heatDirAdapter.getPosition(response.getHeat()),false);
                lastheatDirSpinner[0] = heatDirAdapter.getPosition(response.getHeat());

                grillTypeSpinner.setSelection(grillTypeAdapter.getPosition(response.getGrill()),false);
                lastgrillTypeSpinner[0] = grillTypeAdapter.getPosition(response.getGrill());

                convectionSpinner.setSelection(convectionAdapter.getPosition(response.getConvection()),false);
                lastconvectionSpinner[0] = convectionAdapter.getPosition(response.getConvection());
                */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        return view;
    }

    private View prepareRefigerator(final Device device, LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.refrigerator_content, null);


        final SeekBar temperature = view.findViewById(R.id.refrigerator_temperature_seekbar);
        final int[] lastTemperature = new int[1];
        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.refrigerator_temperature);
                tempText.setText(String.valueOf(progress + 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastTemperature[0] = seekBar.getProgress() + 2;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(seekBar.getProgress() + 2);
                String requestTag = Api.getInstance(context).deviceAction3(device, "setTemperature", params, new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        //Toast.makeText(context,"").show();
                        lastTemperature[0] = seekBar.getProgress();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastTemperature[0] - 2,false);
                        TextView tempText = (TextView) view.findViewById(R.id.ac_temperature);
                        tempText.setText(String.valueOf(lastTemperature[0]));
                    }
                });
            }
        });


        final SeekBar freezerTemperature = view.findViewById(R.id.refrigerator_freezer_temperature_seekbar);
        final int[] lastfreezerTemperature = new int[1];

        freezerTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tempText = (TextView) view.findViewById(R.id.refrigerator_freezer_temperature);
                tempText.setText(String.valueOf(progress - 20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                lastfreezerTemperature[0] = seekBar.getProgress() - 20;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(seekBar.getProgress() - 20);
                String requestTag = Api.getInstance(context).deviceAction3(device, "setFreezerTemperature", params, new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        //Toast.makeText(context,"").show();
                        lastfreezerTemperature[0] = seekBar.getProgress() - 20;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        Log.e("tag", error.toString());
                        seekBar.setProgress(lastfreezerTemperature[0] + 20,false);
                        TextView tempText = (TextView) view.findViewById(R.id.refrigerator_freezer_temperature);
                        tempText.setText(String.valueOf(lastfreezerTemperature[0]));
                    }
                });
            }
        });


        final Spinner refrigeratorModeSpinner = (Spinner) view.findViewById(R.id.refrigerator_mode_spinner);
        final int[] lastrefrigerator = new int[1];
        refrigeratorModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(Language.spanishToEnglish(refrigeratorModeSpinner.getSelectedItem().toString()));
                String requestTag = Api.getInstance(context).deviceAction4(device, "setMode", params, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,"").show();
                        lastrefrigerator[0] = position;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, R.string.conection_error, Snackbar.LENGTH_LONG).show();
                        //refrigeratorModeSpinner.setSelection(lastrefrigerator[0],false);

                        Log.e("tag", error.toString());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> refrModeList = new ArrayList<String>();
        refrModeList.add(context.getResources().getString(R.string.set_refrigerator_mode_option1));
        refrModeList.add(context.getResources().getString(R.string.set_refrigerator_mode_option2));
        refrModeList.add(context.getResources().getString(R.string.set_refrigerator_mode_option3));

        final ArrayAdapter<String> refrModeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, refrModeList);
        refrModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refrigeratorModeSpinner.setAdapter(refrModeAdapter);

        final String requestTag = Api.getInstance(context).getRefrigeratorState(device, new Response.Listener<RefrigeratorState>() {
            @Override
            public void onResponse(RefrigeratorState response) {
                temperature.setProgress(response.getTemperature() - 2);
                /*
                lastTemperature[0] = response.getTemperature();
                freezerTemperature.setProgress(response.getFreezerTemperature() + 20,false);
                lastfreezerTemperature[0] = response.getFreezerTemperature();
                refrigeratorModeSpinner.setSelection(refrModeAdapter.getPosition(response.getMode()),false);
                lastrefrigerator[0] = refrModeAdapter.getPosition(response.getMode());
                */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", error.toString());
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
