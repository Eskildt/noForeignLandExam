package com.example.noforeignlandexam.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [(PlacesEntity::class)],
    version = 1,
    exportSchema = false)
abstract class DestinationsDatabase: RoomDatabase() {

    abstract fun placesDAO(): DestinationsDAO


}