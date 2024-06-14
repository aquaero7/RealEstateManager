package com.aquaero.realestatemanager

import android.location.Location
import com.aquaero.realestatemanager.utils.MyLocationSource
import com.google.android.gms.maps.LocationSource
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class MyLocationSourceTest {

    private lateinit var myLocationSource: MyLocationSource
    private lateinit var mockListener: LocationSource.OnLocationChangedListener
    private lateinit var mockLocation: Location


    @Before
    fun setup() {
        myLocationSource = MyLocationSource()
        mockListener = mock(LocationSource.OnLocationChangedListener::class.java)
        mockLocation = mock(Location::class.java)

        myLocationSource.activate(mockListener)
    }

    @After
    fun tearDown() {
        myLocationSource.deactivate()
    }


    @Test
    fun activateWithSuccess() {
        // The listener is activated in the setup
        assertEquals(mockListener, myLocationSource.listener)
    }

    @Test
    fun deactivateWithSuccess() {
        // The listener is activated in the setup
        myLocationSource.deactivate()
        assertNull(myLocationSource.listener)
    }

    @Test
    fun listenToLocationChangeWithSuccess() {
        // The listener is activated in the setup
        myLocationSource.onLocationChanged(mockLocation)
        verify(mockListener).onLocationChanged(mockLocation)
    }

}