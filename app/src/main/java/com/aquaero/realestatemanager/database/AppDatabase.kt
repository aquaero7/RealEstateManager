package com.aquaero.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.model.Property
import java.util.concurrent.Executors

@Database(
    entities = [Property::class],
    version = 1,
    exportSchema = false,
)

abstract class AppDatabase: RoomDatabase() {

    abstract val propertyDao: PropertyDao

    companion object {

        private val context: Context by lazy { ApplicationRoot.getContext() }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase().also { INSTANCE = it }
            }

        private fun buildDatabase() =
            Room.databaseBuilder(context, AppDatabase::class.java, null)
                // Prepopulate database
                .addCallback(object: Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            // getInstance().propertyDao.prepopulateWithProperties(PROPERTY_PREPOPULATION_DATA)
                        }
                    }
                })
                // Built database
                .build()

    }

}