package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.EditField
import com.aquaero.realestatemanager.NonEditField
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.CACHE_ADDRESS
import com.aquaero.realestatemanager.model.CACHE_PROPERTY
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.repository.CacheRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing CacheRepository
 */
class CacheRepositoryTest {
    private lateinit var repository: CacheRepository

    private lateinit var unassigned: String
    private lateinit var itemPhotos: MutableList<Photo>
    private lateinit var itemPois: MutableList<Poi>
    private lateinit var property: Property
    private lateinit var address: Address
    private lateinit var stringType: String
    private lateinit var stringAgent: String
    private lateinit var photo1: Photo
    private lateinit var photo2: Photo
    private lateinit var photo3: Photo
    private lateinit var photos: MutableList<Photo>

    @Before
    fun setup() {
        repository = CacheRepository()

        property = Property(
            9, "", null, null, null, null,
            null, null, null, null, null,
            9
        )
        address = Address(
            9, null, null, null, null, null,
            null, null, null, null
        )

        unassigned = ""
        itemPhotos = mutableListOf()
        itemPois = mutableListOf()
        stringType = "stringType"
        stringAgent = "StringAgent"
        photo1 = Photo(1, "uri1", "label1", 1)
        photo2 = Photo(2, "uri2", "label2", 2)
        photo3 = Photo(3, "uri3", "label3", 3)
        photos = mutableListOf(photo1, photo2)
    }

    @After
    fun teardown() {}

    private fun launchTestsForCacheItem(
        cacheClass: Class<*>,
        item: String,
        value: Any?,
        field: KProperty<*>,
    ) {
        val initialValue = when (field) {
            Property::typeId -> TypeEnum.UNASSIGNED.key
            Property::agentId -> 1L
            Property::propertyId -> 0L
            Address::addressId -> 0L
            else -> null
        }
        val function: KFunction<*> = when (cacheClass) {
            Property::class.java -> CacheRepository::updateCachePropertyItem
            Address::class.java -> CacheRepository::updateCacheAddressItem
            else -> return  // Should not be reached
        }
        val getter: KFunction<*> = when (cacheClass) {
            Property::class.java -> CacheRepository::getCacheProperty
            Address::class.java -> CacheRepository::getCacheAddress
            else -> return  // Should not be reached
        }

        // Check test initial conditions
        assertEquals(initialValue, field.call(getter.call(repository)))
        // Function under test
        function.call(repository, item, value)
        // Verifications ans assertions
        assertEquals(value, field.call(getter.call(repository)))
    }


    /**
     * Testing initCache() and clearCache()
     */
    @Test
    fun testInitAndClearCache() {
        repository.clearCache(unassigned = unassigned)

        // Function under test initCache() with default values
        repository.initCache(
            unassigned, null, null, null, null, itemPhotos, itemPois
        )

        // Verifications ans assertions
        assertEquals(CACHE_PROPERTY, repository.getCacheProperty())
        assertEquals(null, repository.getInitialAddress())
        assertEquals(CACHE_ADDRESS, repository.getCacheAddress())
        assertEquals(unassigned, repository.forTestingOnly_getCacheStringType())
        assertEquals(unassigned, repository.forTestingOnly_getCacheStringAgent())
        assertEquals(itemPois, repository.getInitialItemPois())
        assertEquals(itemPois, repository.getCacheItemPois())
        assertEquals(itemPhotos, repository.getInitialItemPhotos())
        var cacheItemPhotosStateFlow = repository.forTestingOnly_getCacheItemPhotosStateFlow()
        assertEquals(itemPhotos, cacheItemPhotosStateFlow.value)


        repository.clearCache(unassigned = unassigned)
        // Function under test initCache() with provided values
        repository.initCache(
            unassigned, property, stringType, stringAgent, address, itemPhotos, itemPois
        )

        // Verifications ans assertions
        assertEquals(property, repository.getCacheProperty())
        assertEquals(address, repository.getInitialAddress())
        assertEquals(address, repository.getCacheAddress())
        assertEquals(stringType, repository.forTestingOnly_getCacheStringType())
        assertEquals(stringAgent, repository.forTestingOnly_getCacheStringAgent())
        assertEquals(itemPois, repository.getInitialItemPois())
        assertEquals(itemPois, repository.getCacheItemPois())
        assertEquals(itemPhotos, repository.getInitialItemPhotos())
        cacheItemPhotosStateFlow = repository.forTestingOnly_getCacheItemPhotosStateFlow()
        assertEquals(itemPhotos, cacheItemPhotosStateFlow.value)


        // No field reset
        // Function under test clearCache()
        repository.clearCache(unassigned = unassigned)

        // Verifications ans assertions
        assertEquals(CACHE_PROPERTY, repository.getCacheProperty())
        assertEquals(null, repository.getInitialAddress())
        assertEquals(CACHE_ADDRESS, repository.getCacheAddress())
        assertEquals(unassigned, repository.forTestingOnly_getCacheStringType())
        assertEquals(unassigned, repository.forTestingOnly_getCacheStringAgent())
        assertEquals(emptyList<Poi>(), repository.getInitialItemPois())
        assertEquals(emptyList<Poi>(), repository.getCacheItemPois())
        assertEquals(emptyList<Photo>(), repository.getInitialItemPhotos())
        cacheItemPhotosStateFlow = repository.forTestingOnly_getCacheItemPhotosStateFlow()
        assertEquals(emptyList<Photo>(), cacheItemPhotosStateFlow.value)
    }

    @Test
    fun testCacheData() {
        // Check and set test initial conditions
        assertEquals(CACHE_PROPERTY, repository.getCacheProperty())
        assertEquals(CACHE_ADDRESS, repository.getCacheAddress())
        assertEquals(emptyList<Poi>(), repository.getCacheItemPois())
        repository.forTestingOnly_setCacheProperty(property)
        repository.forTestingOnly_setCacheAddress(address)
        repository.forTestingOnly_setCacheItemPois(itemPois)

        // Function under test
        val response = repository.cacheData()

        // Verifications ans assertions
        assertEquals(property, response.first)
        assertEquals(address, response.second)
        assertEquals(itemPois, response.third)
    }

    @Test
    fun testUpdateCacheAddressWithLatLng() {
        // Check test initial conditions
        assertEquals(null, repository.getCacheAddress().latitude)
        assertEquals(null, repository.getCacheAddress().longitude)

        // Function under test
        repository.updateCacheAddress(lat = 1.1, lng = 1.2)

        // Verifications ans assertions
        assertEquals(1.1, repository.getCacheAddress().latitude)
        assertEquals(1.2, repository.getCacheAddress().longitude)
    }

    @Test
    fun testUpdateCacheAddressWithId() {
        // Check test initial conditions
        assertEquals(null, repository.getCacheProperty().addressId)
        // Function under test
        repository.updateCacheAddress(addressId = 1L)
        // Verifications ans assertions
        assertEquals(1L, repository.getCacheProperty().addressId)
    }

    @Test
    fun testUpdateCacheItemPois() {
        /* Adding a POI */
        // Check test initial conditions
        assertEquals(emptyList<Poi>(), repository.getCacheItemPois())
        // Function under test
        repository.updateCacheItemPois(poiItem = "poi", isSelected = true)
        // Verifications ans assertions
        assertEquals(mutableListOf(Poi("poi")), repository.getCacheItemPois())

        /* Removing a POI that is not in the list */
        // Function under test
        repository.updateCacheItemPois(poiItem = "otherPoi", isSelected = false)
        // Verifications ans assertions
        assertEquals(mutableListOf(Poi("poi")), repository.getCacheItemPois())

        /* Removing a POI that is in the list */
        // Function under test
        repository.updateCacheItemPois(poiItem = "poi", isSelected = false)
        // Verifications ans assertions
        assertEquals(emptyList<Poi>(), repository.getCacheItemPois())
    }

    @Test
    fun testUpdateCacheItemPhotosWithList() {
        // Check test initial conditions
        assertEquals(emptyList<Photo>(), repository.getCacheItemPhotos())
        assertEquals(emptyList<Photo>(), repository.forTestingOnly_getCacheItemPhotosStateFlow().value)
        // Function under test
        repository.updateCacheItemPhotos(itemPhotos = photos)
        // Verifications ans assertions
        assertEquals(photos, repository.getCacheItemPhotos())
        assertEquals(photos, repository.forTestingOnly_getCacheItemPhotosStateFlow().value)
    }

    @Test
    fun testUpdateCacheItemPhotosWithIndex() {
        // Set and check test initial conditions
        repository.updateCacheItemPhotos(itemPhotos = photos)
        var result = repository.getCacheItemPhotos()
        assertEquals(2, result.size)
        assertEquals(photo1, result[0])
        assertEquals(photo2, result[1])
        assertEquals(
            mutableListOf(photo1, photo2),
            repository.forTestingOnly_getCacheItemPhotosStateFlow().value
        )

        // Function under test
        repository.updateCacheItemPhotos(photoIndex = 1, photoToUpdate = photo3)

        // Verifications ans assertions
        result = repository.getCacheItemPhotos()
        assertEquals(2, result.size)
        assertEquals(photo1, result[0])
        assertEquals(photo3, result[1])
        assertEquals(
            mutableListOf(photo1, photo3),
            repository.forTestingOnly_getCacheItemPhotosStateFlow().value
        )
    }

    @Test
    fun testUpdateCacheItemPhotosAddRemove() {
        /* Adding a photo */
        // Check test initial conditions
        assertEquals(emptyList<Photo>(), repository.getCacheItemPhotos())
        assertEquals(emptyList<Photo>(), repository.forTestingOnly_getCacheItemPhotosStateFlow().value)
        // Function under test
        repository.updateCacheItemPhotos(photoToAdd = photo3, photoToRemove = null)
        // Verifications ans assertions
        assertEquals(mutableListOf(photo3), repository.getCacheItemPhotos())
        assertEquals(mutableListOf(photo3), repository.forTestingOnly_getCacheItemPhotosStateFlow().value)

        /* Removing a photo that is not in the list */
        // Function under test
        repository.updateCacheItemPhotos(photoToAdd = null, photoToRemove = photo2)
        // Verifications ans assertions
        assertEquals(mutableListOf(photo3), repository.getCacheItemPhotos())
        assertEquals(mutableListOf(photo3), repository.forTestingOnly_getCacheItemPhotosStateFlow().value)

        /* Removing a photo that is in the list */
        // Function under test
        repository.updateCacheItemPhotos(photoToAdd = null, photoToRemove = photo3)
        // Verifications ans assertions
        assertEquals(emptyList<Photo>(), repository.getCacheItemPhotos())
        assertEquals(emptyList<Photo>(), repository.forTestingOnly_getCacheItemPhotosStateFlow().value)
    }

    @Test
    fun testUpdateCacheItemPhotosWithId() {
        // Set and check test initial conditions
        repository.updateCacheItemPhotos(itemPhotos = photos)
        var result = repository.getCacheItemPhotos()
        assertEquals(photos, result)
        assertEquals(photos, repository.forTestingOnly_getCacheItemPhotosStateFlow().value)
        assertEquals(2, result.size)
        assertEquals(1L, result[0].propertyId)
        assertEquals(2L, result[1].propertyId)

        // Function under test
        repository.updateCacheItemPhotos(propertyId = 9L)

        // Verifications ans assertions
        result = repository.getCacheItemPhotos()
        assertEquals(photos, result)
        assertEquals(photos, repository.forTestingOnly_getCacheItemPhotosStateFlow().value)
        assertEquals(2, result.size)
        assertEquals(9L, result[0].propertyId)
        assertEquals(9L, result[1].propertyId)
    }

    @Test
    fun testUpdateCachePropertyItem() {
        val cacheClass = Property::class.java

        launchTestsForCacheItem(cacheClass, DropdownMenuCategory.TYPE.name, "type", Property::typeId)
        launchTestsForCacheItem(cacheClass, DropdownMenuCategory.AGENT.name, 9L, Property::agentId)
        launchTestsForCacheItem(cacheClass, EditField.PRICE.name, 9, Property::price)
        launchTestsForCacheItem(cacheClass, EditField.SURFACE.name, 9, Property::surface)
        launchTestsForCacheItem(cacheClass, EditField.ROOMS.name, 9, Property::nbOfRooms)
        launchTestsForCacheItem(cacheClass, EditField.BATHROOMS.name, 9, Property::nbOfBathrooms)
        launchTestsForCacheItem(cacheClass, EditField.BEDROOMS.name, 9, Property::nbOfBedrooms)
        launchTestsForCacheItem(
            cacheClass, EditField.DESCRIPTION.name, "description", Property::description
        )
        launchTestsForCacheItem(
            cacheClass, EditField.REGISTRATION_DATE.name, "registrationDate", Property::registrationDate
        )
        launchTestsForCacheItem(cacheClass, EditField.SALE_DATE.name, "saleDate", Property::saleDate)
        launchTestsForCacheItem(cacheClass, NonEditField.ADDRESS_ID.name, 9L, Property::addressId)
        launchTestsForCacheItem(cacheClass, NonEditField.PROPERTY_ID.name, 9L, Property::propertyId)
    }

    @Test
    fun testUpdateCacheAddressItem() {
        val cacheClass = Address::class.java

        launchTestsForCacheItem(
            cacheClass, EditField.STREET_NUMBER.name, "streetNumber", Address::streetNumber
        )
        launchTestsForCacheItem(
            cacheClass, EditField.STREET_NAME.name, "streetName", Address::streetName
        )
        launchTestsForCacheItem(cacheClass, EditField.ADD_INFO.name, "addInfo", Address::addInfo)
        launchTestsForCacheItem(cacheClass, EditField.CITY.name, "city", Address::city)
        launchTestsForCacheItem(cacheClass, EditField.STATE.name, "state", Address::state)
        launchTestsForCacheItem(cacheClass, EditField.ZIP_CODE.name, "zipCode", Address::zipCode)
        launchTestsForCacheItem(cacheClass, EditField.COUNTRY.name, "country", Address::country)
    }

}