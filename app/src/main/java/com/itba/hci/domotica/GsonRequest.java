package com.itba.hci.domotica;

import android.opengl.GLException;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GsonRequest<T1, T2> extends Request<T2> {
    private final Gson gson = new Gson();
    private final T1 data;
    private final String token;
    private final Class<T2> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T2> listener;

    public GsonRequest(int method, String url, T1 data, String token, Class<T2> clazz, Map<String, String> headers,
                       Response.Listener<T2> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.data = data;
        this.token = token;
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T2 response) {
        listener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return data != null ? gson.toJson(data).getBytes() : super.getBody();
    }

    @Override
    protected Response<T2> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (token != null) {

                JSONObject jsonObject = new JSONObject(json);

                if (clazz == Boolean.class) {
                    json = (new Boolean(jsonObject.getBoolean(token))).toString();
                } else if (clazz == Integer.class) {
                    json = (new Integer(jsonObject.getInt(token))).toString();
                } else if (clazz == Double.class) {
                    json = (new Double(jsonObject.getDouble(token))).toString();
                } else if (clazz == Long.class) {
                    json = (new Long(jsonObject.getLong(token))).toString();
                } else if (Collection.class.isAssignableFrom(clazz)) {
                    json = jsonObject.getJSONArray(token).toString();
                }else {
                    json = jsonObject.getJSONObject(token).toString();
                }
            }
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}