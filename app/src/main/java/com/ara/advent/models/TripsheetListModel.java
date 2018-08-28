package com.ara.advent.models;

/**
 * Created by Sharp Kumar on 13-05-2018.
 */

public class TripsheetListModel {
    String VehiName, VehiId;
    String pickupTime;
    String trip_booking_reporting_time;

    public String getPickup_place() {
        return pickup_place;
    }

    public void setPickup_place(String pickup_place) {
        this.pickup_place = pickup_place;
    }

    String pickup_place;
    String tripBooking_id, tripBooking_no, tripBooking_date, customer_name, customerMultiContact_name, tripBookingReport_to, tripcustomer_startingkm, tripcustomer_startingtime, cus_mobNo, cus_add;

    public TripsheetListModel(String vehiName, String vehiId, String pickupTime, String trip_booking_reporting_time, String pickup_place, String tripBooking_id, String tripBooking_no, String tripBooking_date, String customer_name, String customerMultiContact_name, String tripBookingReport_to, String tripcustomer_startingkm, String tripcustomer_startingtime, String cus_mobNo, String cus_add) {
        VehiName = vehiName;
        VehiId = vehiId;
        this.pickupTime = pickupTime;
        this.trip_booking_reporting_time = trip_booking_reporting_time;
        this.pickup_place = pickup_place;
        this.tripBooking_id = tripBooking_id;
        this.tripBooking_no = tripBooking_no;
        this.tripBooking_date = tripBooking_date;
        this.customer_name = customer_name;
        this.customerMultiContact_name = customerMultiContact_name;
        this.tripBookingReport_to = tripBookingReport_to;
        this.tripcustomer_startingkm = tripcustomer_startingkm;
        this.tripcustomer_startingtime = tripcustomer_startingtime;
        this.cus_mobNo = cus_mobNo;
        this.cus_add = cus_add;
    }

    public String getTrip_booking_reporting_time() {
        return trip_booking_reporting_time;
    }

    public void setTrip_booking_reporting_time(String trip_booking_reporting_time) {
        this.trip_booking_reporting_time = trip_booking_reporting_time;
    }

    public TripsheetListModel() {
    }

    public String getVehiName() {
        return VehiName;
    }

    public void setVehiName(String vehiName) {
        VehiName = vehiName;
    }

    public String getVehiId() {
        return VehiId;
    }

    public void setVehiId(String vehiId) {
        VehiId = vehiId;
    }

    public String getCus_mobNo() {
        return cus_mobNo;
    }

    public void setCus_mobNo(String cus_mobNo) {
        this.cus_mobNo = cus_mobNo;
    }

    public String getCus_add() {
        return cus_add;
    }

    public void setCus_add(String cus_add) {
        this.cus_add = cus_add;
    }

    public String getTripBooking_id() {

        return tripBooking_id;
    }

    public void setTripBooking_id(String tripBooking_id) {
        this.tripBooking_id = tripBooking_id;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getTripBooking_no() {
        return tripBooking_no;
    }

    public void setTripBooking_no(String tripBooking_no) {
        this.tripBooking_no = tripBooking_no;
    }

    public String getTripBooking_date() {
        return tripBooking_date;
    }

    public void setTripBooking_date(String tripBooking_date) {
        this.tripBooking_date = tripBooking_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomerMultiContact_name() {
        return customerMultiContact_name;
    }

    public void setCustomerMultiContact_name(String customerMultiContact_name) {
        this.customerMultiContact_name = customerMultiContact_name;
    }

    public String getTripBookingReport_to() {
        return tripBookingReport_to;
    }

    public void setTripBookingReport_to(String tripBookingReport_to) {
        this.tripBookingReport_to = tripBookingReport_to;
    }

    public String getTripcustomer_startingkm() {
        return tripcustomer_startingkm;
    }

    public void setTripcustomer_startingkm(String tripcustomer_startingkm) {
        this.tripcustomer_startingkm = tripcustomer_startingkm;
    }

    public String getTripcustomer_startingtime() {
        return tripcustomer_startingtime;
    }

    public void setTripcustomer_startingtime(String tripcustomer_startingtime) {
        this.tripcustomer_startingtime = tripcustomer_startingtime;
    }
}
