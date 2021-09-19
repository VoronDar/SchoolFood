package com.astery.ccabp.model.cloud_database;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astery.ccabp.model.cloud_database.pogo.PostParent;
import com.astery.ccabp.model.transport_preferences.TransportPreferences;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import static com.astery.ccabp.model.cloud_database.CloudDatabase.prepare;
import static com.astery.ccabp.model.cloud_database.CloudUtils.thereIsNoNet;

public class CloudAuth {
    private final Context context;
    private final Signable signable;

    private final String domen = "https://schoolfood51.ru";
    private final String API = "Cirv4xUtnNLKql7N9mVlxlXrQ7KpFx61HovEMj99";

    public CloudAuth(Context context, Signable signable) {
        this.context = context;
        this.signable = signable;
    }

    public void signIn(String email, String password){
        PostParent pp = new PostParent("", "", email, password);
        String json = new Gson().toJson(pp);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url= domen + "/signIn/" + API + "/" + json;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.equals("f")) {
                        signable.onFailure();
                    }
                    else {
                        try {
                            JSONObject json1 = new JSONObject(response);
                            TransportPreferences.setParentId(context, Integer.parseInt(json1.getString("id")));
                            TransportPreferences.setName(context, (json1.getString("name")));
                            TransportPreferences.setSecondName(context, (json1.getString("lastName")));
                            signable.onSuccess();
                        } catch (JSONException e) {
                            thereIsNoNet(context);
                            signable.onError();
                        }
                    }

                }, error -> {
            if (error.getMessage() != null)
                Log.i("flask", "signIn: "+ error.getMessage());
            else
                Log.i("flask", "signIn: unexpected error");
            signable.onError();});

        queue.add(stringRequest);
    }

    public  void signUp(String email, String password, String name, String lastName){

        lastName= prepare(lastName);
        name = prepare(name);

        PostParent pp = new PostParent(name, lastName, email, password);
        String json = new Gson().toJson(pp);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url=domen + "/signUp/" + API + "/" + json;

        Log.i("flask", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("f")) {
                        signable.onFailure();
                    }
                    else {
                        TransportPreferences.setParentId(context, Integer.parseInt(response));
                        signable.onSuccess();
                    }

                }, error -> {
                    thereIsNoNet(context);
                    signable.onError();
        });

        queue.add(stringRequest);
    }


    public interface Signable{
        void onFailure();
        void onSuccess();
        void onError();
    }
}
