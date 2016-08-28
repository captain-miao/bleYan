package com.github.captain_miao.android.bluetoothletutorial.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;
import com.github.captain_miao.android.bluetoothletutorial.constant.BleLogLevel;

/**
 * @author Yan Lu
 * @since 2016-08-28
 */
public class BleLogInfo extends Model {

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long logTime;

    @Column()
    public String log;

    @Column()
    public int logLevel;


    public BleLogInfo() {
    }

    public BleLogInfo(String log, int logLevel) {
        this.logTime = System.currentTimeMillis();
        this.log = log == null ? "" : log;
        this.logLevel = logLevel;
    }

    public BleLogInfo(long logTime, String log, int logLevel) {
        this.logTime = logTime;
        this.log = log;
        this.logLevel = logLevel;
    }

    public static BleLogInfo getLogs(long beginTime, long endTime) {
        BleLogInfo logs = new Select()
                .from(BleLogInfo.class)
                .where("logTime >=?", beginTime)
                .and("logTime <=?", endTime)
                .executeSingle();

        if(logs == null) {
            logs = new BleLogInfo("", BleLogLevel.VERBOSE);
        }
        return logs;
    }
}
