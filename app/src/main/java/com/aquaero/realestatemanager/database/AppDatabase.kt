package com.aquaero.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.model.ADDRESS_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.PHOTO_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import java.util.concurrent.Executors

@Database(
    entities = [
        Property::class, Agent::class, Photo::class, Address::class, Poi::class, PropertyPoiJoin::class
    ],
    version = 1,
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {

    abstract val propertyDao: PropertyDao
    abstract val agentDao: AgentDao
    abstract val photoDao: PhotoDao
    abstract val addressDao: AddressDao
    abstract val poiDao: PoiDao
    abstract val propertyPoiJoinDao: PropertyPoiJoinDao

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
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            getInstance().propertyDao.prepopulateWithProperties(emptyList()  /* PROPERTY_PREPOPULATION_DATA */)
                            getInstance().agentDao.prepopulateWithAgents(AGENT_PREPOPULATION_DATA)
                            getInstance().photoDao.prepopulateWithPhotos(PHOTO_PREPOPULATION_DATA)
                            getInstance().addressDao.prepopulateWithAddresses(ADDRESS_PREPOPULATION_DATA)
                            getInstance().poiDao.prepopulateWithPois(POI_PREPOPULATION_DATA)
                            getInstance().propertyPoiJoinDao.prepopulateWithPropertyPoiJoins(emptyList() /* PROPERTY_POI_JOIN_PREPOPULATION_DATA */)
                        }
                    }
                })
                // Built database
                .build()
    }

}