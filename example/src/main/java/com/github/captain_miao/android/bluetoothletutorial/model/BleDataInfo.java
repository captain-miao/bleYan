package com.github.captain_miao.android.bluetoothletutorial.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

/**
 * @author Yan Lu
 * @since 2016-08-28
 */
public class BleDataInfo extends Model {

    public long time;

    @Column()
    public String uuid;

    @Column()
    public String data;


    public BleDataInfo() {
    }

    public BleDataInfo(String uuid, String data) {
        this.time = System.currentTimeMillis();
        this.uuid = uuid == null ? "" : uuid;
        this.data = data == null ? "" : data;
    }

    public BleDataInfo(long time, String uuid, String data) {
        this.time = time;
        this.uuid = uuid == null ? "" : uuid;
        this.data = data == null ? "" : data;
    }

    public static BleDataInfo getDatas(long beginTime, long endTime) {
        BleDataInfo logs = new Select()
                .from(BleDataInfo.class)
                .where("time >=?", beginTime)
                .and("time <=?", endTime)
                .executeSingle();

        if(logs == null) {
            logs = new BleDataInfo("", "");
        }
        return logs;
    }
}
