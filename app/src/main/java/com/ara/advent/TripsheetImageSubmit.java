package com.ara.advent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ara.advent.http.HttpCaller;
import com.ara.advent.http.HttpRequest;
import com.ara.advent.http.HttpResponse;
import com.ara.advent.models.OncallTsModel;
import com.ara.advent.utils.AppConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ara.advent.utils.AppConstants.MY_CAMERA_REQUEST_CODE;

public class TripsheetImageSubmit extends AppCompatActivity {

    private static final String TAG = "Oncall TripSheet";
    private static final int REQUEST_TAKE_IMAGE_ONE = 1;
    private static final int REQUEST_TAKE_IMAGE_ONE_BACK = 11;
    private static final int REQUEST_TAKE_IMAGE_TWO = 2;
    private static final int REQUEST_TAKE_IMAGE_THREE = 3;
    private static final int REQUEST_TAKE_IMAGE_FOUR = 4;

    @BindView(R.id.textview_tripnosubmit)
    TextView tripno;
    @BindView(R.id.textview_tripdatesubmit)
    TextView tripdate;
    @BindView(R.id.textview_customersubmit)
    TextView customer;
    @BindView(R.id.OncallTripsheet_layout)
    ScrollView OntripLayout;
    @BindView(R.id.OnSubmit)
    Button OnSubmit_button;
    @BindView(R.id.input_cameraImage1)
    ImageView input_image1;
    @BindView(R.id.input_cameraImage2)
    ImageView input_image2;
    @BindView(R.id.input_cameraImage3)
    ImageView input_image3;
    @BindView(R.id.input_cameraImag4)
    ImageView input_image4;
    @BindView(R.id.ParkingAmount)
    EditText parkingAmount;
    @BindView(R.id.PermitAmount)
    EditText permitAmount;
    @BindView(R.id.TollAmount)
    EditText tollgateAmount;
    @BindView(R.id.input_cameraImage1back)
    ImageView input_image1back;
    @BindView(R.id.closedate)
    TextView closedate;

    String a;
    OncallTsModel oncallTsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheetimagesubmit);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        OntripLayout.setFocusableInTouchMode(true);
        OntripLayout.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        oncallTsModel = new OncallTsModel();

        SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
        String id = sharedPreferences1.getString("uid", "");
        Log.e(TAG, "id -- " + id);

        SharedPreferences sharedPreferences = getSharedPreferences("submit", MODE_PRIVATE);
        a = sharedPreferences.getString("tripsheetid", "");
        String b = sharedPreferences.getString("tripsheetno", "");
        String c = sharedPreferences.getString("tripsheetDate", "");
        String d = sharedPreferences.getString("tripsheetcustomername", "");
        String e = sharedPreferences.getString("closetime", "");
        String f = sharedPreferences.getString("closekm", "");
        String g = sharedPreferences.getString("totkm", "");
        String h = sharedPreferences.getString("tottime", "");
        String i = sharedPreferences.getString("closedate", "");
        tripno.setText(b);
        tripdate.setText(c);
        closedate.setText(i);
        customer.setText(d);
        oncallTsModel.setClosingtime(e);
        oncallTsModel.setClosingkilometer(f);
        oncallTsModel.setTrip_Id(a);
        oncallTsModel.setTotalTime(h);
        oncallTsModel.setTotalkilometer(g);
        oncallTsModel.setUserid(id);
        Log.e(TAG, "total kilometer " + g);
        oncallTsModel.setClosingDate(i);
        if (!isNetworkAvailable()) {
            showSnackbar("Something went wrong ,Please check your network connection");
        }
        OnSubmit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSubmit_buttonMethod();
            }
        });

        input_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionForCamera(REQUEST_TAKE_IMAGE_ONE);
            }
        });

        input_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionForCamera(REQUEST_TAKE_IMAGE_TWO);
            }
        });
        input_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionForCamera(REQUEST_TAKE_IMAGE_THREE);
            }
        });

        input_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionForCamera(REQUEST_TAKE_IMAGE_FOUR);
            }
        });
        input_image1back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionForCamera(REQUEST_TAKE_IMAGE_ONE_BACK);
            }
        });
    }

    private void requestPermissionForCamera(int request) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                showSnackbar("This App needs Camera", true);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_CAMERA_REQUEST_CODE);
            }
        } else {
            dispatchTakePictureIntent(request);
        }
    }

    private void showSnackbar(String message, final boolean finishApp) {
        final Snackbar snackbar = Snackbar.make(OntripLayout, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.text_ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                if (finishApp)
                    finish();

            }
        });
        snackbar.show();
    }

    private void OnSubmit_buttonMethod() {

        if (!formValid()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isNetworkAvailable()) {
            showSnackbar("PLease Check Your Netwok Connection");
            return;
        }
        oncallTsModel.setPark_amount(parkingAmount.getText().toString());
        oncallTsModel.setPemit_amount(permitAmount.getText().toString());
        oncallTsModel.setToll_amout(tollgateAmount.getText().toString());

        pushToLog();
        PushtoServer();
    }

    private void pushToLog() {
        Log.e(TAG, "Objet oncall ts model " + oncallTsModel);
    }

    private void PushtoServer() {
        Log.e(TAG, "object ioncall ts model " + oncallTsModel);
        HttpRequest httpRequest = new HttpRequest(AppConstants.ONCALLTRIPSHEETURL);
        httpRequest.setRequestBody(oncallTsModel.multipartRequest());
        try {
            new HttpCaller(this, "Processing...") {
                @Override
                public void onResponse(HttpResponse response) {
                    super.onResponse(response);

                    if (response.getStatus() == HttpResponse.ERROR) {
                        Log.e(TAG, "Error response when pushto server " + response.getMesssage());
                        onBookingFailure(response);
                    } else {

                        Log.e(TAG, "Booking Ride Success------" + response.getMesssage());
                        SharedPreferences sharedPreferences = getSharedPreferences("Oncall", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("OncallBooked", "success");
                        editor.commit();
                        startActivity(new Intent(TripsheetImageSubmit.this, MainActivity.class));
                        finish();
                    }
                }
            }.execute(httpRequest);

        } catch (Exception execption) {
            Log.e(TAG, "Exception on submit dat to server" + execption);
        }
    }

    private void onBookingFailure(HttpResponse response) {
        if (response != null) {
            showSnackbar("Something went wrong.Please check your network connnection");
        }
    }


    private void dispatchTakePictureIntent(int REQUEST_TAKE_PHOTO) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(REQUEST_TAKE_PHOTO);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, ex.getMessage(), ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ara.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String tempImage = "temp.png";
        if (requestCode == REQUEST_TAKE_IMAGE_ONE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(oncallTsModel.getTrip_front());
            int n = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
            int a = imageBitmap.getHeight();
            int b = imageBitmap.getWidth();
            Log.e(TAG, "1.Height = " + a + "width = " + b + "total resolution = " + n);
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 512, n, true);
            compressImageFile(scaled, oncallTsModel.getTrip_front());
            input_image1.setImageBitmap(scaled);
            Log.e(TAG, "image bitmap One" + oncallTsModel.getTrip_front());

        } else if (requestCode == REQUEST_TAKE_IMAGE_TWO && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(oncallTsModel.getParking_image());
            int n = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
            int a = imageBitmap.getHeight();
            int b = imageBitmap.getWidth();
            Log.e(TAG, "1.Height = " + a + "width = " + b + "total resolution = " + n);
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 512, n, true);
            compressImageFile(scaled, oncallTsModel.getParking_image());
            input_image2.setImageBitmap(scaled);
            Log.e(TAG, "image bitmap two" + oncallTsModel.getParking_image());

        } else if (requestCode == REQUEST_TAKE_IMAGE_THREE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(oncallTsModel.getPermit_image());
            int n = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
            int a = imageBitmap.getHeight();
            int b = imageBitmap.getWidth();
            Log.e(TAG, "1.Height = " + a + "width = " + b + "total resolution = " + n);
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 512, n, true);
            compressImageFile(scaled, oncallTsModel.getPermit_image());
            input_image3.setImageBitmap(scaled);
            Log.e(TAG, "image bitmap three" + oncallTsModel.getPermit_image());

        } else if (requestCode == REQUEST_TAKE_IMAGE_FOUR && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(oncallTsModel.getToll_image());
            int n = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
            int a = imageBitmap.getHeight();
            int b = imageBitmap.getWidth();
            Log.e(TAG, "1.Height = " + a + "width = " + b + "total resolution = " + n);
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 512, n, true);
            compressImageFile(scaled, oncallTsModel.getToll_image());
            input_image4.setImageBitmap(scaled);
            Log.e(TAG, "image bitmap four" + oncallTsModel.getToll_image());

        } else if (requestCode == REQUEST_TAKE_IMAGE_ONE_BACK && resultCode == RESULT_OK) {

            Bitmap imageBitmap = BitmapFactory.decodeFile(oncallTsModel.getTripsheet_back());
            int n = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
            int a = imageBitmap.getHeight();
            int b = imageBitmap.getWidth();
            Log.e(TAG, "1.Height = " + a + "width = " + b + "total resolution = " + n);
            Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 512, n, true);
            compressImageFile(scaled, oncallTsModel.getTripsheet_back());
            input_image1back.setImageBitmap(scaled);
            Log.e(TAG, "image bitmap One back " + oncallTsModel.getTripsheet_back());
        }
    }


    public void compressImageFile(Bitmap bitmap, String fileName) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 12, outputStream);


            File file = new File(fileName);
            file.deleteOnExit();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] array = outputStream.toByteArray();
            fileOutputStream.write(array);
            fileOutputStream.close();

        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage(), exception);
        }

    }


    private File createImageFile(int REQUEST) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" +
                "" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        String mCurrentPhotoPath;
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        if (REQUEST == 1) {
            oncallTsModel.setTrip_front(mCurrentPhotoPath);
        } else if (REQUEST == 2) {
            oncallTsModel.setParking_image(mCurrentPhotoPath);
        } else if (REQUEST == 3) {
            oncallTsModel.setPermit_image(mCurrentPhotoPath);
        } else if (REQUEST == 4) {
            oncallTsModel.setToll_image(mCurrentPhotoPath);
        } else if (REQUEST == 11) {
            oncallTsModel.setTripsheet_back(mCurrentPhotoPath);
        }

        return image;
    }

    public boolean formValid() {

        boolean isValid = true;
/*

        if (!parkingAmount.getText().toString().isEmpty()) {
            if (oncallTsModel.getParking_image() == null) {
                showSnackbar("Take Parking bill's photo", false);
                return false;
            }
            parkingAmount.setError(null);
            isValid = true;
        }
        if (!permitAmount.getText().toString().isEmpty()) {
            if (oncallTsModel.getPermit_image() == null) {
                showSnackbar("Take Permit bill's Photo.", false);
                return false;
            }
            permitAmount.setError(null);
            isValid = true;
        }
        if (!tollgateAmount.getText().toString().isEmpty()) {
            if (oncallTsModel.getToll_image() == null) {
                showSnackbar("Take Tollgate Photo.", false);
                return false;
            }
            tollgateAmount.setError(null);
            isValid = true;
        }
*/

        if (oncallTsModel.getTrip_front() == null) {
            showSnackbar("Take Trip sheet Front Side photo.", false);
            return false;
        }
        if (oncallTsModel.getTripsheet_back() == null) {
            showSnackbar("Take Trip sheet backside photo.", false);
            return false;
        }


        return isValid;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void showSnackbar(String message) {
        final Snackbar snackbar = Snackbar.make(OntripLayout, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.text_ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TripsheetImageSubmit.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
