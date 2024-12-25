package com.example.counts.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(CountModel::class),  version = 1)
abstract class RoomDB : RoomDatabase() {

    abstract fun userDao(): Dao

    companion object{
        private  var INSTANCE  : RoomDB? = null;

        fun getInstance(context: Context): RoomDB?{
          if (INSTANCE == null){
                   INSTANCE  =
                      Room.databaseBuilder(
                          context.applicationContext,
                          RoomDB::class.java,
                          "COUNT_DB"
                      ).allowMainThreadQueries()
                      .build()
          }
            return  INSTANCE;
        }

    }



}