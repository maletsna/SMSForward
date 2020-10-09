package com.smsforward;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 1;
    public static List<Rule> rules;
    private List<String> mobileArray = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            rules = JsonManager.loadRules(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (rules != null && !rules.isEmpty()) {
            for (Rule rule : rules) {
                mobileArray.add(rule.getName());
            }
        }

        adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //           smsSendMessage(view);
                //            Snackbar.make(view, "SMS sent", Snackbar.LENGTH_LONG)
                //                   .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class).putExtra(getString(R.string.extraRuleName), adapter.getItem(position));
                startActivity(intent);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(intent);

            }
        });
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }
    }
    public void smsSendMessage(View view) {
        PendingIntent sentIntent = null, deliveryIntent = null;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage
                ("", null, "",
                        sentIntent, deliveryIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mobileArray.clear();
        for (Rule rule: rules) {
            mobileArray.add(rule.getName());
        }
        adapter.notifyDataSetChanged();
    }
}