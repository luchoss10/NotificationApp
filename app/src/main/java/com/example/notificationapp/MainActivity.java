package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //Atajo files CTRL + SHIFT + N open Files

    @BindView(R.id.editTextTittle)
    EditText editTextTittle;
    @BindView(R.id.editTextMessage)
    EditText editTextMessage;
    @BindView(R.id.switchImportance)
    Switch swirchImportance;
    @BindView(R.id.buttonSend)
    Button buttonSend;

    @BindString(R.string.switch_notification_off) String switchTextOff;
    @BindString(R.string.switch_notification_on)String switchTextOn;

    private boolean isHighImportance =  false;
    private NotificationHandler notificationHandler;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//Right after setContentView
        notificationHandler = new NotificationHandler(this);
    }

    @OnClick(R.id.buttonSend)
    public void click(){
        sendNotification();
    }

    @OnCheckedChanged(R.id.switchImportance)
    public void changedImportance(CompoundButton buttonView, boolean isChecked){
        isHighImportance =  isChecked;
        swirchImportance.setText((isChecked) ? switchTextOn : switchTextOff);
    }

    private void sendNotification(){
        String title =  editTextTittle.getText().toString();
        String message = editTextMessage.getText().toString();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)){
            Notification.Builder nb = notificationHandler.createNotification(title, message, isHighImportance);
            notificationHandler.getManager().notify(++counter,nb.build());
            notificationHandler.publishNotificationSummaryGruop(isHighImportance);
        }
        else {
            Toast.makeText(this, "Writte a message and Tittle for the notification", Toast.LENGTH_LONG).show();
        }
    }
}
