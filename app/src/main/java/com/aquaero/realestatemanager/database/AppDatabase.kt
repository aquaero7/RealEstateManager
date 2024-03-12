package com.aquaero.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Property::class, Address::class, Photo::class, Agent::class, Type::class, Poi::class, PropertyPoiJoin::class
    ],
    version = 1,
    exportSchema = false,
)

abstract class AppDatabase : RoomDatabase() {

    abstract val propertyDao: PropertyDao
    abstract val addressDao: AddressDao
    abstract val photoDao: PhotoDao
    abstract val agentDao: AgentDao
    abstract val typeDao: TypeDao
    abstract val poiDao: PoiDao
    abstract val propertyPoiJoinDao: PropertyPoiJoinDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /*
        fun getInstanceForTest(context: Context): AppDatabase {
            return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()//.also { INSTANCE = it }
//            return Room.databaseBuilder(context, AppDatabase::class.java, "testdatabase").build()
        }
        */

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context = context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context = context, klass = AppDatabase::class.java, name = "database")
                // (Pre)populate database
                .addCallback(callback = object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            prepopulateDatabase(context = context)
                            // populateDatabase(context = context)   // For demo only
                        }
                    }
                })
                // Built database
                .build()

        private suspend fun prepopulateDatabase(context: Context) {
            getInstance(context = context).agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
            getInstance(context = context).typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
            getInstance(context = context).poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)
        }

        /**
         * For demo only
         */
        private suspend fun populateDatabase(context: Context) {
            getInstance(context = context).addressDao.prepopulateWithAddresses(addresses = ADDRESS_PREPOPULATION_DATA)
            getInstance(context = context).propertyDao.prepopulateWithProperties(properties = PROPERTY_PREPOPULATION_DATA)
            getInstance(context = context).photoDao.prepopulateWithPhotos(photos = PHOTO_PREPOPULATION_DATA)
            getInstance(context = context).propertyPoiJoinDao.prepopulateWithPropertyPoiJoins(propertyPoiJoins = PROPERTY_POI_JOIN_PREPOPULATION_DATA)
        }

    }

}