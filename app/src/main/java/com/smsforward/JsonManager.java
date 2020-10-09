package com.smsforward;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {
    public static List<Rule> loadRules(Context context) throws IOException, JSONException {
        File file = new File(context.getFilesDir(), "storage.json");
        FileReader fileReader;
        List<Rule> rules = new ArrayList<Rule>();
        try {
            fileReader = new FileReader(file);
        } catch (java.io.FileNotFoundException e) {
            return rules;
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String response = stringBuilder.toString();
        JSONArray arr = new JSONArray(response);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            Rule r = new Rule(o.getBoolean("forwardAll"), o.getString("from"), o.getString("to"), o.getString("name"));
            rules.add(r);
        }
        fileReader.close();
        return rules;
    }

    public static void saveRules(List<Rule> rules, Context context) {
        JSONArray arr = new JSONArray();
        for (Rule r : rules) {
            JSONObject o = new JSONObject();
            try {
                o.put("forwardAll", r.isForwardAll());
                o.put("from", r.getFrom());
                o.put("to", r.getTo());
                o.put("name", r.getName());
                arr.put(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String userString = arr.toString();
        File file = new File(context.getFilesDir(), "storage.json");
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
