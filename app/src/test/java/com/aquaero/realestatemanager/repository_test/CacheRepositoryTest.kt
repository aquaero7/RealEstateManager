package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.CACHE_ADDRESS
import com.aquaero.realestatemanager.model.CACHE_PROPERTY
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.repository.CacheRepository
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Field

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

        unassigned = "unassigned"
        itemPhotos = mutableListOf()
        itemPois = mutableListOf()
        stringType = "stringType"
        stringAgent = "StringAgent"

    }

    @After
    fun teardown() {

    }

    /**
     * Gets the private repository fields by reflection
     */
    private fun getRepoField(fieldName: String): Any? {
        val field: Field = CacheRepository::class.java.getDeclaredField(fieldName)
        field.isAccessible = true

        return field.get(repository)
    }

    /**
     * Sets the private repository fields by reflection
     */
    private fun setRepoField(fieldName: String, fieldValue: Any?) {
        val field: Field = CacheRepository::class.java.getDeclaredField(fieldName)
        field.isAccessible = true

        field.set(repository, fieldValue)
    }

    private fun resetAllRepoFields() {
        val fields = arrayOf(
            "cacheProperty", "initialAddress", "cacheAddress", "cacheStringType", "cacheStringAgent",
            "initialItemPois", "cacheItemPois", "initialItemPhotos"
        )
        fields.forEach {
            when (it) {
                "cacheProperty" -> setRepoField(it, CACHE_PROPERTY.copy())
                "cacheAddress" -> setRepoField(it, CACHE_ADDRESS.copy())
                "initialItemPois", "cacheItemPois" -> setRepoField(it, mutableListOf<Poi>())
                "initialItemPhotos" -> setRepoField(it, mutableListOf<Photo>())
                "cacheStringType", "cacheStringAgent" -> setRepoField(it, "")
                "initialAddress" -> setRepoField(it, null)
                else -> setRepoField(it, null)
            }
        }
    }


    /**
     * Testing initCache() and clearCache()
     */
    @Suppress("UNCHECKED_CAST")
    @Test
    fun testInitCache() {
        resetAllRepoFields()
        // Function under test initCache() with default values
        repository.initCache(
            unassigned, null, null, null, null, itemPhotos, itemPois
        )
        // Verifications ans assertions
        assertEquals(CACHE_PROPERTY, getRepoField("cacheProperty"))
        assertEquals(null, getRepoField("initialAddress"))
        assertEquals(CACHE_ADDRESS, repository.getCacheAddress())
        assertEquals(unassigned, getRepoField("cacheStringType"))
        assertEquals(unassigned, getRepoField("cacheStringAgent"))
        assertEquals(itemPois, repository.getInitialItemPois())
        assertEquals(itemPois, repository.getCacheItemPois())
        assertEquals(itemPhotos, repository.getInitialItemPhotos())
        var cacheItemPhotosStateFlow =
            getRepoField("_cacheItemPhotosFlow") as MutableStateFlow<MutableList<Photo>>
        assertEquals(itemPhotos, cacheItemPhotosStateFlow.value)

        resetAllRepoFields()
        // Function under test initCache() with provided values
        repository.initCache(
            unassigned, property, stringType, stringAgent, address, itemPhotos, itemPois
        )
        // Verifications ans assertions
        assertEquals(property, getRepoField("cacheProperty"))
        assertEquals(address, getRepoField("initialAddress"))
        assertEquals(address, repository.getCacheAddress())
        assertEquals(stringType, getRepoField("cacheStringType"))
        assertEquals(stringAgent, getRepoField("cacheStringAgent"))
        assertEquals(itemPois, repository.getInitialItemPois())
        assertEquals(itemPois, repository.getCacheItemPois())
        assertEquals(itemPhotos, repository.getInitialItemPhotos())
        cacheItemPhotosStateFlow =
            getRepoField("_cacheItemPhotosFlow") as MutableStateFlow<MutableList<Photo>>
        assertEquals(itemPhotos, cacheItemPhotosStateFlow.value)

        // No field reset
        // Function under test clearCache()
        repository.clearCache(unassigned)
        // Verifications ans assertions
        assertEquals(CACHE_PROPERTY, getRepoField("cacheProperty"))
        assertEquals(null, getRepoField("initialAddress"))
        assertEquals(CACHE_ADDRESS, repository.getCacheAddress())
        assertEquals(unassigned, getRepoField("cacheStringType"))
        assertEquals(unassigned, getRepoField("cacheStringAgent"))
        assertEquals(mutableListOf<Poi>(), repository.getInitialItemPois())
        assertEquals(mutableListOf<Poi>(), repository.getCacheItemPois())
        assertEquals(mutableListOf<Photo>(), repository.getInitialItemPhotos())
        cacheItemPhotosStateFlow =
            getRepoField("_cacheItemPhotosFlow") as MutableStateFlow<MutableList<Photo>>
        assertEquals(mutableListOf<Photo>(), cacheItemPhotosStateFlow.value)
    }

    @Test
    fun testCacheData() {
        // Check and set test initial conditions
        assertEquals(CACHE_PROPERTY, getRepoField("cacheProperty"))
        assertEquals(CACHE_ADDRESS, repository.getCacheAddress())
        assertEquals(mutableListOf<Poi>(), repository.getCacheItemPois())
        setRepoField("cacheProperty", property)
        setRepoField("cacheAddress", address)
        setRepoField("cacheItemPois", itemPois)

        // Function under test
        val response = repository.cacheData()

        // Verifications ans assertions
        assertEquals(property, response.first)
        assertEquals(address, response.second)
        assertEquals(itemPois, response.third)
    }

    @Test
    fun testUpdateCacheAddressWithLatLng() {
        // Check and set test initial conditions
        assertEquals(null, repository.getCacheAddress().latitude)
        assertEquals(null, repository.getCacheAddress().longitude)

        // Function under test
        repository.updateCacheAddress(1.1, 1.2)

        // Verifications ans assertions
        assertEquals(1.1, repository.getCacheAddress().latitude)
        assertEquals(1.2, repository.getCacheAddress().longitude)
    }

    @Test
    fun testUpdateCacheAddressWithId() {

        

    }




}