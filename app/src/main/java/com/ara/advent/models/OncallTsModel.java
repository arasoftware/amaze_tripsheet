package com.ara.advent.models;

import android.util.Log;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.ara.advent.utils.AppConstants.CLOSINGDATE;
import static com.ara.advent.utils.AppConstants.IP;
import static com.ara.advent.utils.AppConstants.PARAM_CLOSEDATE;
import static com.ara.advent.utils.AppConstants.PARAM_CLOSING_KM_ONCALL;
import static com.ara.advent.utils.AppConstants.PARAM_CLOSIN_TIME_ONCALL;
import static com.ara.advent.utils.AppConstants.PARKIGNBILL;
import static com.ara.advent.utils.AppConstants.PARAM_TRIPSHEETFRONT;
import static com.ara.advent.utils.AppConstants.PARAM_TRIPSHEETBACK;
import static com.ara.advent.utils.AppConstants.TOLLGAETEBILL;
import static com.ara.advent.utils.AppConstants.PARAM_PERMITBILL;
import static com.ara.advent.utils.AppConstants.PARAM_PARKAMT;
import static com.ara.advent.utils.AppConstants.PARAM_PERAMT;
import static com.ara.advent.utils.AppConstants.PARAM_TOLLAMT;
import static com.ara.advent.utils.AppConstants.PARAM_TRIPSHEET_ID;
import static com.ara.advent.utils.AppConstants.TOTALKM;
import static com.ara.advent.utils.AppConstants.TOTALTIME;
import static com.ara.advent.utils.AppConstants.USERID;
import static com.ara.advent.utils.AppConstants.user;

/**
 * Created by User on 08-May-18.
 */

public class OncallTsModel {

    String userid;
    String trip_Id;
    String customer_id;
    String Def_date;
    String trip_front;
    String parking_image;
    String permit_image;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String Toll_image;

    public String getPark_amount() {
        return park_amount;
    }

    public void setPark_amount(String park_amount) {
        this.park_amount = park_amount;
    }

    public String getPemit_amount() {
        return pemit_amount;
    }

    public void setPemit_amount(String pemit_amount) {
        this.pemit_amount = pemit_amount;
    }

    public String getToll_amout() {
        return toll_amout;
    }

    public void setToll_amout(String toll_amout) {
        this.toll_amout = toll_amout;
    }

    String park_amount;
    String pemit_amount;
    String toll_amout;

    public String getTripsheet_back() {
        return tripsheet_back;
    }

    public void setTripsheet_back(String tripsheet_back) {
        this.tripsheet_back = tripsheet_back;
    }

    String tripsheet_back;
    String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTotalkilometer() {
        return totalkilometer;
    }

    public void setTotalkilometer(String totalkilometer) {
        this.totalkilometer = totalkilometer;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    String totalkilometer;
    String totalTime;
    String ClosingDate;

    public String getClosingDate() {
        return ClosingDate;
    }

    public void setClosingDate(String closingDate) {
        ClosingDate = closingDate;
    }

    public String getClosingkilometer() {
        return closingkilometer;
    }

    public void setClosingkilometer(String closingkilometer) {
        this.closingkilometer = closingkilometer;
    }

    public String getClosingtime() {
        return closingtime;
    }

    public void setClosingtime(String closingtime) {
        this.closingtime = closingtime;
    }

    String closingkilometer;
    String closingtime;

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }


    public String getToll_image() {
        return Toll_image;
    }

    public void setToll_image(String toll_image) {
        this.Toll_image = toll_image;
    }

    public String getParking_image() {
        return parking_image;
    }

    public void setParking_image(String parking_image) {
        this.parking_image = parking_image;
    }

    public String getPermit_image() {
        return permit_image;
    }

    public void setPermit_image(String permit_image) {
        this.permit_image = permit_image;
    }

    public String getTrip_Id() {
        return trip_Id;
    }

    public void setTrip_Id(String trip_Id) {
        this.trip_Id = trip_Id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getDef_date() {
        return Def_date;
    }

    public void setDef_date(String def_date) {
        Def_date = def_date;
    }

    public String getTrip_front() {
        return trip_front;
    }

    public void setTrip_front(String trip_front) {
        this.trip_front = trip_front;
    }

    public MultipartBody multipartRequest() {


        Log.e("TAg", "message string id " + customer_id);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        MediaType mediaType = MediaType.parse("image/jpeg");
        builder.addFormDataPart(PARAM_TRIPSHEETFRONT, getTrip_front(),
                RequestBody.create(mediaType, new File(getTrip_front())));
        if(getPermit_image() != null) {
            builder.addFormDataPart(PARAM_PERMITBILL, getPermit_image(),
                    RequestBody.create(mediaType, new File(getPermit_image())));
        } else {
            builder.addFormDataPart(PARAM_PERMITBILL,"");
        }

        if (getParking_image() !=  null) {
            builder.addFormDataPart(PARKIGNBILL, getParking_image(),
                    RequestBody.create(mediaType, new File(getParking_image())));
        } else {
            builder.addFormDataPart(PARKIGNBILL,"");
        }

        if (getToll_image() != null) {
         builder.addFormDataPart(TOLLGAETEBILL, getToll_image(),
                    RequestBody.create(mediaType, new File(getToll_image())));
        } else {
            builder.addFormDataPart(TOLLGAETEBILL,"");
        }


        builder.addFormDataPart(PARAM_TRIPSHEETBACK, getTripsheet_back(),
                RequestBody.create(mediaType, new File(getTripsheet_back())));
        builder.addFormDataPart(PARAM_TRIPSHEET_ID, getTrip_Id());
        builder.addFormDataPart(USERID,getUserid());
        builder.addFormDataPart(PARAM_CLOSING_KM_ONCALL, closingkilometer);
        builder.addFormDataPart(PARAM_CLOSIN_TIME_ONCALL, closingtime);
        builder.addFormDataPart(TOTALKM, getTotalkilometer());
        builder.addFormDataPart(TOTALTIME, getTotalTime());
        builder.addFormDataPart(IP, getLocalIpAddress());
        builder.addFormDataPart(CLOSINGDATE,getClosingDate());
        builder.addFormDataPart(PARAM_PARKAMT, getPark_amount());
        builder.addFormDataPart(PARAM_PERAMT, getPemit_amount());
        builder.addFormDataPart(PARAM_TOLLAMT, getToll_amout());
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    @Override
    public String toString() {
        return PARAM_TRIPSHEET_ID + "-" + getTrip_Id() + "\n" +
                USERID + "-" + getUserid() + "\n" +
                PARAM_CLOSING_KM_ONCALL + "-" + closingkilometer + "\n" +
                PARAM_CLOSIN_TIME_ONCALL + "-" + closingtime + "\n" +
                TOTALKM + "-" + getTotalkilometer() + "\n" +
                TOTALTIME + "-" + getTotalTime() + "\n" +
                IP + "-" + getLocalIpAddress() + "\n"
                + PARAM_PARKAMT + "-" + getPark_amount() + "\n"
                + PARAM_PERAMT + "-" + getPemit_amount() + "\n" +
                PARAM_TOLLAMT + "-" + getToll_amout()+ "\n" +
                CLOSINGDATE + "-" +getClosingDate()+ "\n" +
                PARAM_TRIPSHEETFRONT+ "-" +getTrip_front()+ "\n" +
                PARAM_TRIPSHEETBACK+ "-" +getTripsheet_back()+ "\n" +
                PARAM_PERMITBILL+ "-" +getPermit_image()+ "\n" +
                PARKIGNBILL+ "-" +getParking_image()+ "\n" +
                TOLLGAETEBILL+ "-" +getToll_image();
    }
}
