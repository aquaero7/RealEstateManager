package com.aquaero.realestatemanager.repository_test

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.aquaero.realestatemanager.repository.LocationRepository
import com.aquaero.realestatemanager.utils.ConnectionState
import com.aquaero.realestatemanager.utils.GeocoderHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.properties.Delegates

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
/**
 * Testing LocationRepository
 */
class LocationRepositoryTest {
    private lateinit var repository: LocationRepository
    private lateinit var context: Context

    private var lat by Delegates.notNull<Double>()
    private var lng by Delegates.notNull<Double>()
    private lateinit var latLng: LatLng
    private lateinit var location: Location
    private lateinit var address: Address
    private lateinit var addresses: MutableList<Address>
    private lateinit var stringAddress: String
    private lateinit var geocoder: Geocoder
    private lateinit var geocoderHelper: GeocoderHelper
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequestCaptor: KArgumentCaptor<LocationRequest>
    private lateinit var locationCallbackCaptor: KArgumentCaptor<LocationCallback>
    private lateinit var geocodeListenerCaptor: KArgumentCaptor<Geocoder.GeocodeListener>

    // Created to avoid class "LOG" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        // Initialize logMock
        logMock = Mockito.mockStatic(Log::class.java)

        repository = LocationRepository()

        context = mock(Context::class.java)
        geocoder = mock(Geocoder::class.java)
        geocoderHelper = mock(GeocoderHelper::class.java)
        fusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)

        lat = 1.1
        lng = 2.2
        latLng = LatLng(lat, lng)

        locationRequestCaptor = argumentCaptor()
        locationCallbackCaptor = argumentCaptor()
        geocodeListenerCaptor = argumentCaptor()

        // Set fusedLocationProviderClient
        repository.forTestingOnly_setFusedLocationProviderClient(fusedLocationProviderClient)

        // Set locationCallback result
        location = mock(Location::class.java).apply {
            doReturn(lat).`when`(this).latitude
            doReturn(lng).`when`(this).longitude
        }
        val mockLocationResult = LocationResult.create(listOf(location))
        repository.forTestingOnly_setLocationCallbackResult(mockLocationResult)

        // Set Geocoder address and addresses
        address = mock(Address::class.java).apply {
            `when`(latitude).thenReturn(lat)
            `when`(longitude).thenReturn(lng)
        }
        addresses = mutableListOf(address)
        stringAddress = "stringAddress"

        doReturn("packageName").`when`(context).packageName
    }

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }

    /**
     * Helper to set permission response
     */
    private fun setupCheckSelfPermissionResponse(granted: Boolean) {
        if (granted) {
            `when`(
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ).thenReturn(PackageManager.PERMISSION_GRANTED)
            `when`(
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ).thenReturn(PackageManager.PERMISSION_GRANTED)
        } else {
            `when`(
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ).thenReturn(PackageManager.PERMISSION_DENIED)
            `when`(
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ).thenReturn(PackageManager.PERMISSION_DENIED)
        }
    }

    /**
     * Helper to set the value of Build.VERSION.SDK_INT
     */
    private fun setAndroidVersion(version: Int) {
        val field = Build.VERSION::class.java.getDeclaredField("SDK_INT")
        field.isAccessible = true
        field.setInt(null, version)
    }

    private fun launchTestGetLocationFromAddress(
        apiVersion: Int,
        isInternetAvailable: Boolean,
        stringAddress: String?,
        latLng: LatLng?,
    ) = runBlocking {
        // Simulate Android (API) version
        setAndroidVersion(apiVersion)

        // Simulate the latLng return
        when {
            apiVersion >= 33 && stringAddress != null -> {
                doAnswer { invocation ->
                    val callback = invocation.getArgument<(LatLng?) -> Unit>(2)
                    callback(latLng)
                    null
                }.`when`(geocoderHelper).getLatLngFromAddressAsync(eq(context), eq(stringAddress), any())
            }
            apiVersion < 33 && stringAddress != null -> {
                doReturn(latLng).`when`(geocoderHelper).getLatLngFromAddress(context, stringAddress)
            }
        }

        // Function under test
        val result = repository.getLocationFromAddress(
            geocoderHelper, context, stringAddress, isInternetAvailable
        )

        // Assertion
        val assertion: LatLng? = if (isInternetAvailable) stringAddress?.let { latLng } else null
        assertEquals(assertion , result)
    }


    @Test
    fun testCheckForConnection() {
        assertTrue(repository.checkForConnection(ConnectionState.Available))
        assertFalse(repository.checkForConnection(ConnectionState.Unavailable))
    }

    /**
     * Testing functions checkForPermissions() and areLocPermsGranted()
     */
    @Test
    fun testCheckForPermissions() {
        setupCheckSelfPermissionResponse(granted = true)
        // Function under test: checkForPermissions() and assertion
        assertTrue(repository.checkForPermissions(context))
        // Function under test: areLocPermsGranted() and assertion
        assertTrue(repository.areLocPermsGranted())

        setupCheckSelfPermissionResponse(granted = false)
        // Function under test: checkForPermissions() and assertion
        assertFalse(repository.checkForPermissions(context))
        // Function under test: areLocPermsGranted() and assertion
        assertFalse(repository.areLocPermsGranted())
    }

    @Test
    fun testStartLocationUpdates() {
        // Function under test
        repository.startLocationUpdates(context)

        // Verifications and assertions
        verify(fusedLocationProviderClient).requestLocationUpdates(
            locationRequestCaptor.capture(),
            locationCallbackCaptor.capture(),
            eq(Looper.getMainLooper())
        )
        val capturedRequest = locationRequestCaptor.allValues[0]
        assertEquals(10000L, capturedRequest.intervalMillis)
        assertEquals(Priority.PRIORITY_HIGH_ACCURACY, capturedRequest.priority)
        assertNotNull(locationCallbackCaptor.allValues[0])
    }

    @Test
    fun testStopLocationUpdates() {
        // Function under test
        repository.stopLocationUpdates(context)

        // Verification and assertion
        verify(fusedLocationProviderClient).removeLocationUpdates(locationCallbackCaptor.capture())
        assertNotNull(locationCallbackCaptor.allValues[0])
    }

    @Test
    fun testGetLocationUpdates() {
        // Function under test
        val result = repository.getLocationUpdates()

        // Assertions
        assertNotNull(result.value)
        assertEquals(location, result.value)
        assertEquals(lat, result.value?.latitude)
        assertEquals(lng, result.value?.longitude)
    }
    
    @Test
    fun testGetLocationFromAddress() {
        // Simulate Android 13+ (API 33+) and a successful latLng return
        launchTestGetLocationFromAddress(
            apiVersion = Build.VERSION_CODES.TIRAMISU, isInternetAvailable = true,
            stringAddress = stringAddress, latLng = latLng
        )

        // Simulate Android 13+ (API 33+) and a failure to return latLng
        launchTestGetLocationFromAddress(
            apiVersion = Build.VERSION_CODES.TIRAMISU, isInternetAvailable = true,
            stringAddress = stringAddress, latLng = null
        )

        // Simulate Android 12- (API 32-) and a successful latLng return
        launchTestGetLocationFromAddress(
            apiVersion = Build.VERSION_CODES.S, isInternetAvailable = true,
            stringAddress = stringAddress, latLng = latLng
        )

        // Simulate Android 12- (API 32-) and a failure to return latLng
        launchTestGetLocationFromAddress(
            apiVersion = Build.VERSION_CODES.S, isInternetAvailable = true,
            stringAddress = stringAddress, latLng = null
        )

        // Simulate a null stringAddress
        launchTestGetLocationFromAddress(
            apiVersion = Build.VERSION_CODES.TIRAMISU, isInternetAvailable = true,
            stringAddress = null, latLng = latLng
        )

        // Simulate internet unavailable
        launchTestGetLocationFromAddress(
            apiVersion = Build.VERSION_CODES.TIRAMISU, isInternetAvailable = false,
            stringAddress = stringAddress, latLng = latLng
        )
    }

    @Test
    fun testCreateAppSettingsIntent() {
        // Function under test
        val result = repository.createAppSettingsIntent(context)

        // Assertions
        assertEquals(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, result.action)
        assertEquals(Uri.fromParts("package", "packageName", null), result.data)
        assertEquals("package", result.data?.scheme)
        assertEquals("packageName", result.data?.schemeSpecificPart)
        assertNull(result.data?.fragment)
        assertEquals(Intent.FLAG_ACTIVITY_NEW_TASK, result.flags)
    }

}