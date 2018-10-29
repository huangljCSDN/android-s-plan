package com.xsimple.im.db.datatable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pengpeng on 17/3/24.
 */

@Entity(nameInDb = "im_msg_location_new")
public class IMLocationInfo {

    @Id(autoincrement = true)
    private Long lId;

    private String name;

    private String address;

    private String latitude;

    private String longitude;

    @Keep
    public IMLocationInfo(String name, String address, String latitude, String longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Generated(hash = 2110627522)
    public IMLocationInfo(Long lId, String name, String address, String latitude,
            String longitude) {
        this.lId = lId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Generated(hash = 752905699)
    public IMLocationInfo() {
    }

    public Long getLId() {
        return this.lId;
    }

    public void setLId(Long lId) {
        this.lId = lId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
