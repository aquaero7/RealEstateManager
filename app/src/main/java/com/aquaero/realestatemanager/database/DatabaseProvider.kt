package com.aquaero.realestatemanager.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.dao.PropertyDao

data object DatabaseProvider {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    /* Config 1

    private var instance: AppDatabase? = null

    fun getInstance(/*context: Context*/): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class) {
                if (instance == null) {
                    instance = buildRoomDB(/*context*/)
                }
            }
        }
        return instance!!
    }

    private fun buildRoomDB(/*context: Context*/) =
        Room.databaseBuilder(
            // context = context.applicationContext,
            context = context,
            klass = AppDatabase::class.java,
            name = null
        )
            .fallbackToDestructiveMigration()
            .build()

    */

    /* Config 2

    fun provideAppDatabase(): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = null
        )
            .fallbackToDestructiveMigration()
            .build()

    fun providePropertyDao(appDatabase: AppDatabase): PropertyDao = appDatabase.propertyDao

    */

}