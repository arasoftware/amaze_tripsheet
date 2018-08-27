package com.ara.advent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.ara.advent.utils.AppConstants.CUSTOMER_NAME;
import static com.ara.advent.utils.AppConstants.TRIPID;

public class SignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        TextView customerAndTripNo = (TextView) findViewById(R.id.sign_customer_trip_no);

        Button signSave = (Button) findViewById(R.id.sign_save);

        final Intent intent = getIntent();
        String customerName = intent.getStringExtra(CUSTOMER_NAME);
        String tripNo = intent.getStringExtra(TRIPID);

        customerAndTripNo.setText("Customer Name: " + customerName + "   Trip No:  " + tripNo);

        signSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SignatureActivity.this, TripsheetImageSubmit.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}
