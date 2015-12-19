package com.github.captain_miao.android.bluetoothletutorial.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

/**
 * @author Yan Lu
 * @since 2015-10-08
 */
public class FavouriteInfo extends Model {

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String address;

    @Column()
    public String name;

    @Column()
    public boolean isFavourite;


    public FavouriteInfo() {
    }

    public FavouriteInfo(String address, String name, boolean isFavourite) {
        this.address = address;
        this.name = name == null ? "" : name;
        this.isFavourite = isFavourite;
    }



    public static FavouriteInfo getFavourite(String mac) {
        FavouriteInfo favouriteInfo = new Select()
                .from(FavouriteInfo.class)
                .where("address =?", mac)
                .executeSingle();

        if(favouriteInfo == null) {
            favouriteInfo = new FavouriteInfo("", "", false);
        }
        return favouriteInfo;
    }
}
