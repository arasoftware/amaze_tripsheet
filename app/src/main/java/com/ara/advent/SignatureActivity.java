package com.ara.advent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;

public class SignatureActivity extends AppCompatActivity {
    @BindView(R.id.scrol)
    ScrollView container;
    @BindView(R.id.textview_tripno)
    TextView trino;
    @BindView(R.id.textview_tripdate)
    TextView tripdate;
    @BindView(R.id.textview_customer)
    TextView customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
    }
}
