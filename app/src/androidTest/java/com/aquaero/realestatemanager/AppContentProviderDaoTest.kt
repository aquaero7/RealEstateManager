package com.aquaero.realestatemanager

import android.content.Context
import android.database.Cursor
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Partial tests (only DAO function called by ContentProvider are tested,
 * not ContentProvider functions)
 */
@RunWith(AndroidJUnit4::class)
class AppContentProviderDaoTest {

    private lateinit var context: Context
    private lateinit var testDatabase: AppDatabase
    private lateinit var typeDao: TypeDao
    private lateinit var agentDao: AgentDao
    private lateinit var propertyDao: PropertyDao
    private lateinit var addressDao: AddressDao
    private lateinit var photoDao: PhotoDao
    private lateinit var poiDao: PoiDao
    private lateinit var propertyPoiJoinDao: PropertyPoiJoinDao


    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        initDatabase()
    }

    @After
    fun tearDown() {
        runBlocking {
            withContext(Dispatchers.IO) {
                testDatabase.clearAllTables()
                testDatabase.close()
            }
        }
    }

    @Test
    fun getPropertiesWhenNoneIsInserted() {
        val cursor: Cursor

        // Initialize cursor and make asynchronous DAO query
        runBlocking {
            cursor = withContext(Dispatchers.IO) { propertyDao.getPropertiesWithCursor() }
        }

        // Test assertions
        assertNotNull(cursor)
        assertEquals(0, cursor.count)

        // Close cursor
        cursor.close()
    }

    @Test
    fun getPropertiesWhenSomeAreInserted() {
        val dao = propertyDao
        val (cursor, saleDates) = initTest(dao = dao) { propertyDao.getPropertiesWithCursor() }
        val (saleDate1, saleDate2, saleDate3) = saleDates

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(saleDate1, cursorFields.second)     // assertNull(cursorSaleDate)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(saleDate2, cursorFields.second)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(saleDate3, cursorFields.second)     // assertNull(cursorSaleDate)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getForSaleProperties() {
        val dao = propertyDao
        val (cursor, saleDates) = initTest(dao = dao) { propertyDao.getPropertiesForSaleWithCursor() }
        val saleDate1 = saleDates.first
        val saleDate3 = saleDates.third

        // Test assertions
        assertNotNull(cursor)
        assertEquals(2, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)
                assertEquals(saleDate1, cursorFields.second)     // assertNull(cursorSaleDate)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
                assertEquals(saleDate3, cursorFields.second)     // assertNull(cursorSaleDate)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getSoldProperties() {
        val dao = propertyDao
        val (cursor, saleDates) = initTest(dao = dao) { propertyDao.getSoldPropertiesWithCursor() }
        val saleDate2 = saleDates.second

        // Test assertions
        assertNotNull(cursor)
        assertEquals(1, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First and only row
                val cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)
                assertEquals(saleDate2, cursorFields.second)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getAddressesWhenNoneIsInserted() {
        val cursor: Cursor

        // Initialize cursor and make asynchronous DAO query
        runBlocking {
            cursor = withContext(Dispatchers.IO) { addressDao.getAddressesWithCursor() }
        }

        // Test assertions
        assertNotNull(cursor)
        assertEquals(0, cursor.count)

        // Close cursor
        cursor.close()
    }

    @Test
    fun getAddressesWhenSomeAreInserted() {
        val dao = addressDao
        val cursor = initTest(dao = dao) { addressDao.getAddressesWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }

    @Test
    fun getPhotosWhenNoneIsInserted() {
        val cursor: Cursor

        // Initialize cursor and make asynchronous DAO query
        runBlocking {
            cursor = withContext(Dispatchers.IO) { photoDao.getPhotosWithCursor() }
        }

        // Test assertions
        assertNotNull(cursor)
        assertEquals(0, cursor.count)

        // Close cursor
        cursor.close()
    }

    @Test
    fun getPhotosWhenSomeAreInserted() {
        val dao = photoDao
        val cursor = initTest(dao = dao) { photoDao.getPhotosWithCursor() }.first

        // Test assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)

        if (cursor.moveToFirst()) {
            do {
                // First row
                var cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(1L, cursorFields.first)

                // Second row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(2L, cursorFields.first)

                // Third row
                cursor.moveToNext()
                cursorFields = cursorFields(dao = dao, cursor = cursor)

                assertEquals(3L, cursorFields.first)
            } while (cursor.moveToNext())
        } else {
            fail("Cursor is empty")
        }

        // Close cursor
        cursor.close()
    }





    /**/

    private fun cursorFields(dao: Any, cursor: Cursor): Pair<Long?, String?> {
        var first: Long? = null
        var second: String? = null
        when (dao) {
            propertyDao -> {
                first = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                second = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))
            }
            addressDao -> {
                first = cursor.getLong(cursor.getColumnIndex(AddressKey.ADDRESS_ID))
            }
            photoDao -> {
                first = cursor.getLong(cursor.getColumnIndex(PhotoKey.PHOTO_ID))
            }
            agentDao -> {
                first = cursor.getLong(cursor.getColumnIndex(AgentKey.AGENT_ID))
            }
            typeDao -> {
                first = cursor.getLong(cursor.getColumnIndex(TypeKey.TYPE_ID))
            }
            poiDao -> {
                first = cursor.getLong(cursor.getColumnIndex(PoiKey.POI_ID))
            }
            propertyPoiJoinDao -> {
                first = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                second = cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID))
            }
        }

        return Pair(first = first, second = second)
    }

    private fun initTest(dao: Any, query: () -> Cursor): Pair<Cursor, Triple<String?, String?, String?>> {

        // Initialize entities
        val (typePr1, agentPr1, property1) = initProperties()
        val (typePr2, agentPr2, property2) = initProperties(propertyId = 2L)
        val (typePr3, agentPr3, property3) = initProperties(propertyId = 3L)
        val saleDate1 = property1.saleDate
        val saleDate2 = property2.saleDate
        val saleDate3 = property3.saleDate
        // Initialize addresses
        val address1 = initAddresses()
        val address2 = initAddresses(addressId = 2L)
        val address3 = initAddresses(addressId = 3L)
        // Initialize photos
        val photo1 = initPhotos().second
        val photo2 = initPhotos(photoId = 2L).second
        val (triple, photo3) = initPhotos(photoId = 3L)
        val (type, agent, properties) = triple

        val cursor: Cursor
        var strings: Triple<String?, String?, String?> = Triple(first = null, second = null, third = null)
        runBlocking {
            // Initialize cursor and make asynchronous query
            when (dao) {
                propertyDao -> {
                    // Populate database with properties
                    populateDatabaseWithProperty(type = typePr1, agent = agentPr1, property = property1)
                    populateDatabaseWithProperty(type = typePr2, agent = agentPr2, property = property2)
                    populateDatabaseWithProperty(type = typePr3, agent = agentPr3, property = property3)
                    strings = Triple(first = saleDate1, second = saleDate2, third = saleDate3)
                }
                addressDao -> {
                    // Populate database with addresses
                    populateDatabaseWithAddress(address = address1)
                    populateDatabaseWithAddress(address = address2)
                    populateDatabaseWithAddress(address = address3)
                }
                photoDao -> {
                    // Populate database with photos
                    populateDatabaseWithPhoto(type = type, agent = agent, properties = properties, photo = photo1)
                    populateDatabaseWithPhoto(type = type, agent = agent, properties = properties, photo = photo2)
                    populateDatabaseWithPhoto(type = type, agent = agent, properties = properties, photo = photo3)
                }
                agentDao -> {

                }
                typeDao -> {

                }
                poiDao -> {

                }
                propertyPoiJoinDao -> {

                }
            }
            cursor = withContext(context = Dispatchers.IO) {
                query()
            }
        }

        return Pair(first = cursor, second = strings)
    }

    private fun initDatabase() {
        testDatabase = Room
            .inMemoryDatabaseBuilder(context = context, klass =  AppDatabase::class.java)
//            .allowMainThreadQueries()
            .build()
        typeDao = testDatabase.typeDao
        agentDao = testDatabase.agentDao
        propertyDao = testDatabase.propertyDao
        addressDao = testDatabase.addressDao
        photoDao = testDatabase.photoDao
        poiDao = testDatabase.poiDao
        propertyPoiJoinDao = testDatabase.propertyPoiJoinDao
    }

    private fun initProperties(propertyId: Long = 1L): Triple<Type, Agent, Property> {
        val saleDate1 = null
        val saleDate2 = "2024-03-01"
        val saleDate3 = null
        val type1 = Type(typeId = TypeEnum.FLAT.key)
        val type2 = Type(typeId = TypeEnum.DUPLEX.key)
        val type3 = Type(typeId = TypeEnum.HOUSE.key)
        val agent1 = Agent(agentId = 1L, firstName = "firstName1", lastName = "lastName1")
        val agent2 = Agent(agentId = 2L, firstName = "firstName2", lastName = "lastName2")
        val agent3 = Agent(agentId = 3L, firstName = "firstName3", lastName = "lastName3")

        val property1 =
            Property(
                propertyId = 1L, typeId = type1.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate1, agentId = agent1.agentId
            )
        val property2 =
            Property(
                propertyId = 2L, typeId = type2.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate2, agentId = agent2.agentId
            )
        val property3 =
            Property(
                propertyId = 3L, typeId = type3.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate3, agentId = agent3.agentId
            )

        return when (propertyId) {
            1L -> Triple(first = type1, second = agent1, third = property1)
            2L -> Triple(first = type2, second = agent2, third = property2)
            3L -> Triple(first = type3, second = agent3, third = property3)
            else -> Triple(first = type1, second = agent1, third = property1)
        }
    }

    private fun initAddresses(addressId: Long = 1L): Address {
        val address1 = Address(1L, null, null, null,
            null, null, null, null, null, null)

        val address2 = Address(2L, null, null, null,
            null, null, null, null, null, null)

        val address3 = Address(3L, null, null, null,
            null, null, null, null, null, null)

        return when (addressId) {
            1L -> address1
            2L -> address2
            3L -> address3
            else -> address1
        }
    }

    private fun initPhotos(photoId: Long = 1L): Pair<Triple<Type, Agent, List<Property>>, Photo> {
        val type = Type(typeId = TypeEnum.HOUSE.key)
        val agent = Agent(agentId = 1L, firstName = "firstName1", lastName = "lastName1")
        val property1 =
            Property(
                propertyId = 1L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )
        val property2 =
            Property(
                propertyId = 2L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )

        val photo1 = Photo(1L, "", "photo1", 1L)
        val photo2 = Photo(2L, "", "photo2", 2L)
        val photo3 = Photo(3L, "", "photo3", 1L)

        return when (photoId) {
            1L -> Pair(Triple(type, agent, listOf(property1, property2)), photo1)
            2L -> Pair(Triple(type, agent, listOf(property1, property2)), photo2)
            3L -> Pair(Triple(type, agent, listOf(property1, property2)), photo3)
            else -> Pair(Triple(type, agent, listOf(property1, property2)), photo1)
        }
    }

    private suspend fun populateDatabaseWithProperty(type: Type, agent: Agent, property: Property) {
        withContext(Dispatchers.IO) {
            typeDao.upsertType(type = type)
            agentDao.upsertAgent(agent = agent)
            propertyDao.upsertProperty(property = property)
        }
    }

    private suspend fun populateDatabaseWithAddress(address: Address) {
        withContext(Dispatchers.IO) {
            addressDao.upsertAddress(address = address)
        }
    }

    private suspend fun populateDatabaseWithPhoto(
        type: Type, agent: Agent, properties: List<Property>, photo: Photo
    ) {
        withContext(Dispatchers.IO) {
            typeDao.upsertType(type = type)
            agentDao.upsertAgent(agent = agent)
            properties.forEach { propertyDao.upsertProperty(it) }
            photoDao.upsertPhoto(photo = photo)
        }
    }

    private suspend fun populateDatabaseWithAgent(agent: Agent) {
        withContext(Dispatchers.IO) {
            agentDao.upsertAgent(agent = agent)
        }
    }

    private suspend fun populateDatabaseWithType(type: Type) {
        withContext(Dispatchers.IO) {
            typeDao.upsertType(type = type)
        }
    }

    private suspend fun populateDatabaseWithPoi(poi: Poi) {
        withContext(Dispatchers.IO) {
            poiDao.upsertPoi(poi = poi)
        }
    }

    private suspend fun populateDatabaseWithPropertyPoiJoin(propertyPoiJoin: PropertyPoiJoin) {
        withContext(Dispatchers.IO) {
            propertyPoiJoinDao.upsertPropertyPoiJoin(propertyPoiJoin = propertyPoiJoin)
        }
    }

}