package com.smsforward;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    public void onCheckboxClicked(View view) {
        TextView tv = findViewById(R.id.fromText);
        tv.setEnabled(!((CheckBox) view).isChecked());

    }
    public void onClickSave(View view) {
        TextView from = findViewById(R.id.fromText);
        TextView to = findViewById(R.id.toText);
        TextView ruleName = findViewById(R.id.ruleName);
        CheckBox forwardAll = findViewById(R.id.forwardAllCB);
        MainActivity.rules.add(new Rule(forwardAll.isChecked(), from.getText().toString(), to.getText().toString(), ruleName.getText().toString()));
        Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
        startActivity(intent);
    }
}