package com.example.tripplanner.repository;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.tripplanner.R;
import com.example.tripplanner.model.DestinationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DestinationDataProvider {

    private static final String DRAWABLE_RESOURCE_PREFIX = "drawable/";

    public static List<DestinationModel> loadDestinationsFromJson(Context context) {
        List<DestinationModel> destinationList = new ArrayList<>();

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.destinations);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                int price = jsonObject.getInt("price");
                String description = jsonObject.getString("description");
                int dataImage = getDrawableResourceId(context, name.toLowerCase());

                destinationList.add(new DestinationModel(name, price, description, dataImage));
            }

            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return destinationList;
    }

    @SuppressLint("DiscouragedApi")
    private static int getDrawableResourceId(Context context, String imageName) {
        return context.getResources().getIdentifier(DRAWABLE_RESOURCE_PREFIX + imageName, "drawable", context.getPackageName());
    }
}

