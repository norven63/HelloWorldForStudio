package com.myAndroid.helloworld.activity.GsonDemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Norven on 2015/5/8 0008.
 */
public class GsonDemoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONObject json = new JSONObject();
        try {
            JSONArray loverJsonArray = new JSONArray();
            for (int i = 0; i < 3; i++) {
                JSONObject loverJson = new JSONObject();
                loverJson.put("name", "lover" + i);
                loverJson.put("age", i);

                loverJsonArray.put(loverJson);
            }

            JSONObject addressJson = new JSONObject();
            addressJson.put("city", "北京");
            addressJson.put("street", "abc");

            json.put("name", "nova");
            json.put("age", 12);
            json.put("address", addressJson);
            json.put("lovers", loverJsonArray);
            json.put("state", 2);
            json.put("isHero", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Person.TestEnum.class, new TypeAdapter<Person.TestEnum>() {
            @Override
            public void write(JsonWriter out, Person.TestEnum value) throws IOException {
                out.value(value.getCode() + 100);
            }

            @Override
            public Person.TestEnum read(JsonReader in) throws IOException {
                return Person.TestEnum.buildEnumFromCode(in.nextInt());
            }
        });
        Gson gson = gsonBuilder.create();
        Person person = gson.fromJson(json.toString(), Person.class);

        Log.e("test: ----> Person\n", person.toString());
        Log.e("test: ----> json\n", gson.toJson(person));
    }
}
