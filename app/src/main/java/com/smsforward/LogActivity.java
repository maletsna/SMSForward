package com.smsforward;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class LogActivity extends AppCompatActivity {
    public static String logText;

    public static void logSms(String sender, String recipient, String message, String ruleName, boolean isOut, Context context) {
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        String localizedDate = "[" + DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME) + "] ";

        if (!isOut) {
            sb.append(localizedDate + "Incoming message from " + sender + ":\n");
            sb.append(localizedDate + message + "\n");
        } else {
            sb.append(localizedDate + "Forwarding message to " + recipient + " using rule " + ruleName + "\n");
        }
        logText = logText == null ? sb.toString() : logText.concat(sb.toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        TextView tv = findViewById(R.id.logTextView);
        tv.setText(logText);
    }
}