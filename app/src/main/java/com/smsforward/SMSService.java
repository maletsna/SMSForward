package com.smsforward;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.annotation.Nullable;

public class SMSService extends Service {
    private SMSreceiver mSMSreceiver;
    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        mSMSreceiver = new SMSreceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);
        LogActivity.logText = "Started SMS service and receiver\n";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSMSreceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SMSreceiver extends BroadcastReceiver {
        private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(messages[i].getMessageBody());
                    }
                    String sender = messages[0].getOriginatingAddress();
                    String message = sb.toString();
                    LogActivity.logSms(sender, null, message, null, false, context);
                    SmsManager smsManager = SmsManager.getDefault();

                    for (Rule rule : MainActivity.rules) {
                        if (rule.isForwardAll() || rule.getFrom().equals(sender)) {
                            smsManager.sendTextMessage(rule.getTo(), null, message,
                                    null, null);
                            LogActivity.logSms(null, rule.getTo(), message, rule.getName(), true, context);
                        }
                    }
                }
            }

        }

    }
}
