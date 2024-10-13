package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.LatLngItem
import com.aquaero.realestatemanager.SM_KEY
import com.aquaero.realestatemanager.SM_MARKER_COLOR
import com.aquaero.realestatemanager.SM_SCALE
import com.aquaero.realestatemanager.SM_SIZE
import com.aquaero.realestatemanager.SM_TYPE
import com.aquaero.realestatemanager.SM_URL
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.repository.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import kotlin.properties.Delegates

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing AddressRepository
 */
class AddressRepositoryTest {
    private lateinit var addressDao: AddressDao
    private lateinit var repository: AddressRepository

    private var addressId1 by Delegates.notNull<Long>()
    private var addressId2 by Delegates.notNull<Long>()
    private lateinit var address1: Address
    private lateinit var address2: Address
    private lateinit var address1Flow: Flow<Address>
    private lateinit var addresses: MutableList<Address>
    private lateinit var addressesOrderedById: MutableList<Address>
    private lateinit var addressesFlow: Flow<MutableList<Address>>
    private lateinit var addressesOrderedByIdFlow: Flow<MutableList<Address>>
    private lateinit var smMarkerAddress: String
    private lateinit var stringAddress1: String

    @Before
    fun setup() {
        addressDao = mock(AddressDao::class.java)
        repository = AddressRepository(addressDao)

        addressId1 = 1L
        addressId2 = 2L
        address1 = mock(Address::class.java)
        address2 = mock(Address::class.java)
        address1Flow = flowOf(address1)
        addresses = mutableListOf(address2, address1)
        addressesOrderedById = mutableListOf(address1, address2)
        addressesFlow = flowOf(addresses)
        addressesOrderedByIdFlow = flowOf(addressesOrderedById)
        smMarkerAddress = "smMarkerAddress"
        stringAddress1 = "stringAddress1"

        // Setup mocks behaviour
        runBlocking {
            doReturn(addressId1).`when`(addressDao).upsertAddress(address1)
        }
        doReturn(address1Flow).`when`(addressDao).getAddress(addressId1)
        doReturn(addressesFlow).`when`(addressDao).getAddresses()
        doReturn(addressesOrderedByIdFlow).`when`(addressDao).getAddressesOrderedById()
        doReturn(addressesFlow).`when`(addressDao).getAddressesOrderedByCity()
        doReturn(addressId1).`when`(address1).addressId
        doReturn(addressId2).`when`(address2).addressId
        doReturn(smMarkerAddress).`when`(address1).toUrl()
        doReturn(stringAddress1).`when`(address1).toString()
        doReturn(0.1).`when`(address1).latitude
        doReturn(0.2).`when`(address1).longitude
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertAddressInRoom() = runBlocking {
        // Function under test
        val result = repository.upsertAddressInRoom(address1)

        // Verification and assertion
        verify(addressDao).upsertAddress(address1)
        assertEquals(addressId1, result)
    }

    @Test
    fun testDeleteAddressFromRoom() = runBlocking {
        // Function under test
        repository.deleteAddressFromRoom(address1)

        // Verification
        verify(addressDao).deleteAddress(address1)
    }

    @Test
    fun testGetAddressFromRoom() {
        // Function under test
        val result = repository.getAddressFromRoom(addressId1)

        // Verification and assertion
        verify(addressDao).getAddress(addressId1)
        assertEquals(address1Flow, result)
    }

    @Test
    fun testGetAddressesFromRoom() {
        // Function under test
        val result = repository.getAddressesFromRoom()

        // Verification and assertion
        verify(addressDao).getAddresses()
        assertEquals(addressesFlow, result)
    }

    @Test
    fun testGetAddressesOrderedByIdFromRoom() {
        // Function under test
        val result = repository.getAddressesOrderedByIdFromRoom()

        // Verification and assertion
        verify(addressDao).getAddressesOrderedById()
        assertEquals(addressesOrderedByIdFlow, result)
    }

    @Test
    fun testGetAddressesOrderedByCityFromRoom() {
        // Function under test
        val result = repository.getAddressesOrderedByCityFromRoom()

        // Verification and assertion
        verify(addressDao).getAddressesOrderedByCity()
        assertEquals(addressesFlow, result)
    }

    @Test
    fun testAddressFromId() {
        // Function under test
        val result = repository.addressFromId(addressId2, addresses)

        // Assertion
        assertEquals(address2, result)
    }

    @Test
    fun testThumbnailUrlFromAddressId() {
        // Function under test when the address list contains the address
        var result = repository.thumbnailUrlFromAddressId(addressId1, addresses)
        // Assertion
        var expectedResult =
            SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MARKER_COLOR + smMarkerAddress + SM_KEY
        assertEquals(expectedResult, result)


        // Function under test when the address list doesn't contain the address
        result = repository.thumbnailUrlFromAddressId(9L, addresses)
        // Assertion
        expectedResult = SM_URL + SM_SIZE + SM_SCALE + SM_TYPE + SM_MARKER_COLOR + "" + SM_KEY
        assertEquals(expectedResult, result)
    }

    @Test
    fun testStringAddress() {
        // Function under test
        val result = repository.stringAddress(addressId1, addresses)

        // Assertion
        assertEquals(stringAddress1, result)
    }

    @Test
    fun testStringLatLngItem() {
        val latLngItems = listOf(LatLngItem.LATITUDE.name, LatLngItem.LONGITUDE.name, "OTHER")
        val results = listOf(String.format("%.7f", 0.1), String.format("%.7f", 0.2), "")

        latLngItems.forEach { latLngItem ->
            // Function under test
            val result = repository.stringLatLngItem(addressId1, addresses, latLngItem)

            // Assertion
            val index = latLngItems.indexOf(latLngItem)
            assertEquals(results[index], result)
        }
    }

}