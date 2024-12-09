package com.aquaero.realestatemanager.dao_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
/**
 * Testing AddressDao
 */
class AddressDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao

    private lateinit var addressDao: AddressDao

    private lateinit var address1: Address
    private lateinit var address2: Address
    private lateinit var address3: Address
    private lateinit var addresses: List<Address>
    private lateinit var address1updated: Address

    @Before
    fun setUp() {
        // Create the database in memory
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        // Prepopulate database
        agentDao = database.agentDao
        typeDao = database.typeDao
        poiDao = database.poiDao
        prepopulateDatabase()

        addressDao = database.addressDao

        address1 = Address(
            1L, null, null, null, "CityC",
            null, null, null, null, null
        )
        address2 = Address(
            2L, null, null, null, "CityB",
            null, null, null, null, null
        )
        address3 = Address(
            3L, null, null, null, "CityA",
            null, null, null, null, null
        )
        address1updated = Address(
            1L, null, null, null, "CityCUpdated",
            null, null, null, null, null
        )

        addresses = listOf(address2, address3, address1)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun prepopulateDatabase() = runBlocking {
        agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
        typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
        poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)
    }


    /**
     * Testing upsertAddress() and getAddress()
     */
    @Test
    fun testUpsertAndGetAddress() = runBlocking {
        // Function under test (inserting address)
        addressDao.upsertAddress(address1)

        // Get the address from database to check the insertion (other function under test)
        var result = addressDao.getAddress(1L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(address1.addressId, result.addressId)
        assertEquals(address1.city, result.city)

        // Function under test (updating address)
        addressDao.upsertAddress(address1updated)

        // Get the address from database to check the update (other function under test)
        result = addressDao.getAddress(1L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(address1.addressId, result.addressId)
        assertEquals(address1updated.addressId, result.addressId)
        assertNotEquals(address1.city, result.city)
        assertEquals(address1updated.city, result.city)
    }

    /**
     * Testing prepopulateWithAddresses() and getAddresses()
     */
    @Test
    fun testPrepopulateWithAddressesAndGetAddresses() = runBlocking {
        // Function under test
        var result = addressDao.getAddresses().first()

        // Initial assertion
        assertTrue(result.isEmpty())

        // Functions under test
        addressDao.prepopulateWithAddresses(addresses)
        result = addressDao.getAddresses().first()

        // Final assertions
        assertEquals(3, result.size)
        assertTrue(result.contains(address1))
        assertTrue(result.contains(address2))
        assertTrue(result.contains(address3))
    }

    @Test
    fun testDeleteAddress() = runBlocking {
        // Prepopulate database
        addressDao.prepopulateWithAddresses(addresses)

        // Initial assertions
        var result = addressDao.getAddresses().first()
        assertEquals(3, result.size)
        assertTrue(result.contains(address2))

        // Function under test
        addressDao.deleteAddress(address2)

        // Final assertions
        result = addressDao.getAddresses().first()
        assertEquals(2, result.size)
        assertFalse(result.contains(address2))
    }

    @Test
    fun testGetAddressesOrderedById() = runBlocking {
        // Prepopulate database
        addressDao.prepopulateWithAddresses(addresses)

        // Function under test
        val result = addressDao.getAddressesOrderedById().first()

        // Assertions
        assertEquals(3, result.size)
        assertEquals(1L, result[0].addressId)
        assertEquals(2L, result[1].addressId)
        assertEquals(3L, result[2].addressId)
    }

    @Test
    fun testGetAddressesOrderedByCity() = runBlocking {
        // Prepopulate database
        addressDao.prepopulateWithAddresses(addresses)

        // Function under test
        val result = addressDao.getAddressesOrderedByCity().first()

        // Assertions (addresses should be sorted by ascending cities)
        assertEquals(3, result.size)
        assertEquals(3L, result[0].addressId)
        assertEquals("CityA", result[0].city)
        assertEquals(2L, result[1].addressId)
        assertEquals("CityB", result[1].city)
        assertEquals(1L, result[2].addressId)
        assertEquals("CityC", result[2].city)
    }


    // ContentProvider

    @Test
    fun testGetAddressesWithCursor() = runBlocking {
        // Prepopulate database
        addressDao.prepopulateWithAddresses(addresses)

        // Function under test
        val cursor = addressDao.getAddressesWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(1L, cursor.getLong(0))
        assertEquals("CityC", cursor.getString(4))
        cursor.moveToPosition(1)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("CityB", cursor.getString(4))
        cursor.moveToPosition(2)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("CityA", cursor.getString(4))

        // Close cursor
        cursor.close()
    }

}