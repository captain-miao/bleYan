package com.dahuo.learn.lbe.bluetoothletutorial.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yan Lu
 * @since 2015-10-08
 */
public class BleCommandInfo extends Model {

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

    public static String[] queryAllCommands() {
        List<String> commands = new ArrayList<>();
        List<BleCommandInfo> commandInfos = new Select()
                .from(BleCommandInfo.class)
                .orderBy("command asc")
                .execute();
        if (commandInfos != null) {
            for (BleCommandInfo info : commandInfos) {
                commands.add(info.command);
            }
        }

        return commands.toArray(new String[commands.size()]);
    }
}
