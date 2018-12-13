package cn.weseewe.android.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Basic implements Serializable {

    @SerializedName("location")
    public String loc;

    @SerializedName("cid")
    public String cid;

}
