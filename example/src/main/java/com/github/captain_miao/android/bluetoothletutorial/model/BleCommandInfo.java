package com.github.captain_miao.android.bluetoothletutorial.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yan Lu
 * @since 2015-10-08
 */
public class BleCommandInfo extends Model implements CharSequence {

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String command;

    @Column()
    public String name;


    public BleCommandInfo() {
    }

    public BleCommandInfo(String name, String command) {
        this.name = name == null ? "" : name;
        this.command = command;
    }

    public static BleCommandInfo[] queryAllCommands() {
        List<BleCommandInfo> commands = new ArrayList<>();
        List<BleCommandInfo> commandInfos = new Select()
                .from(BleCommandInfo.class)
                .orderBy("command asc")
                .execute();
        if (commandInfos != null) {
            for (BleCommandInfo info : commandInfos) {
                commands.add(info);
            }
        }

        return commands.toArray(new BleCommandInfo[commands.size()]);
    }

    @Override
    public int length() {
        return toString().length();
    }

    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }


    public String getCommand() {
        return TextUtils.isEmpty(command) ? "" : command;
    }

    public String getName() {
        return TextUtils.isEmpty(name) ? "" : name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " (0X" +getCommand() + ')';
    }
}
