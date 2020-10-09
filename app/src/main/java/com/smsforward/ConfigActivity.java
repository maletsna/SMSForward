package com.smsforward;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        String itemName = getIntent().getStringExtra(getString(R.string.extraRuleName));
        if (itemName != null && !itemName.isEmpty()) {
            for (Rule rule : MainActivity.rules) {
                if (rule.getName().equals(itemName)) {
                    TextView from = findViewById(R.id.fromText);
                    TextView to = findViewById(R.id.toText);
                    TextView ruleName = findViewById(R.id.ruleName);
                    CheckBox forwardAll = findViewById(R.id.forwardAllCB);
                    from.setText(rule.getFrom());
                    to.setText(rule.getTo());
                    ruleName.setText(itemName);
                    forwardAll.setChecked(rule.isForwardAll());
                    break;
                }
            }
        }
    }

    public void onCheckboxClicked(View view) {
        TextView tv = findViewById(R.id.fromText);
        tv.setEnabled(!((CheckBox) view).isChecked());
        tv.setText("");
        tv.setError(null);

    }
    public void onClickSave(View view) {
        TextView from = findViewById(R.id.fromText);
        TextView to = findViewById(R.id.toText);
        TextView ruleName = findViewById(R.id.ruleName);
        CheckBox forwardAll = findViewById(R.id.forwardAllCB);
        if (from.getText().toString().isEmpty() && !forwardAll.isChecked()) {
            from.setError(getString(R.string.errNoSender));
        }
        if (to.getText().toString().isEmpty()) {
            to.setError(getString(R.string.errNoRecipient));
        }
        if (ruleName.getText().toString().isEmpty()) {
            ruleName.setError(getString(R.string.errNoRuleName));
        } else if (getIntent().getStringExtra(getString(R.string.extraRuleName)) == null) {
            for (Rule rule : MainActivity.rules) {
                if (rule.getName().equals(ruleName.getText().toString())) {
                    ruleName.setError(getString(R.string.errNonUniqueName));
                    break;
                }
            }
        }
        if (from.getError() == null && to.getError() == null && ruleName.getError() == null) {
            String itemName = getIntent().getStringExtra(getString(R.string.extraRuleName));
            if (itemName == null) {
                MainActivity.rules.add(new Rule(forwardAll.isChecked(), from.getText().toString(), to.getText().toString(), ruleName.getText().toString()));
            } else {
                for (Rule rule : MainActivity.rules) {
                    if (rule.getName().equals(itemName)) {
                        rule.setForwardAll(forwardAll.isChecked());
                        rule.setFrom(from.getText().toString());
                        rule.setTo(to.getText().toString());
                        rule.setName(ruleName.getText().toString());
                        break;
                    }
                }
            }
            JsonManager.saveRules(MainActivity.rules, getApplicationContext());
            Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}