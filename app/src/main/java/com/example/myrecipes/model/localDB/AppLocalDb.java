package com.example.myrecipes.model.localDB;


import androidx.room.Room;

import com.example.myrecipes.MyApplication;


public class AppLocalDb{
    static public AppLocalDbRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getMyContext(),
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}
