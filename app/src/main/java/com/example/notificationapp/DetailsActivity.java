package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.textViewTitleDetails)
    TextView textViewTitle;
    @BindView(R.id.textViewMessageDetails)
    TextView textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setIntentValues();
    }

    private void setIntentValues(){
        if(getIntent()!=null){
            textViewTitle.setText(getIntent().getStringExtra("title"));
            textViewMessage.setText(getIntent().getStringExtra("message"));
        }
    }
}
