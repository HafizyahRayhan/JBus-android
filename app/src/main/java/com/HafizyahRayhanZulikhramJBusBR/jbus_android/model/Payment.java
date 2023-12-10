package com.HafizyahRayhanZulikhramJBusBR.jbus_android.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import androidx.annotation.NonNull;
public class Payment extends Invoice {

    public Timestamp departureDate;
    public List<String> busSeat;
    public int busId;

    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        return dateFormat.format(this.departureDate.getTime()) + "\t\t" +"Seat: "+busSeat+"Status: "+status;
    }

}
