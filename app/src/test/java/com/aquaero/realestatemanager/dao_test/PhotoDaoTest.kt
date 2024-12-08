package com.aquaero.realestatemanager.dao_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.TypeEnum
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

@RunWith(AndroidJUnit4::class)
/**
 * Testing PhotoDao
 */
class PhotoDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao
    private lateinit var propertyDao: PropertyDao

    private lateinit var photoDao: PhotoDao

    private lateinit var property1: Property
    private lateinit var property2: Property
    private lateinit var property3: Property

    private lateinit var photo1: Photo
    private lateinit var photo2: Photo
    private lateinit var photo3: Photo
    private lateinit var photos: List<Photo>
    private lateinit var photo1updated: Photo
    private lateinit var photosToDelete: MutableList<Photo>

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
        propertyDao = database.propertyDao
        prepopulateDatabase()

        photoDao = database.photoDao

        photo1 = Photo(1L, "uri1", "PhotoC", 1L)
        photo2 = Photo(2L, "uri2", "PhotoB", 2L)
        photo3 = Photo(3L, "uri3", "PhotoA", 3L)
        photo1updated = Photo(1L, "uri1", "PhotoCUpdated", 1L)

        photos = listOf(photo2, photo3, photo1)
        photosToDelete = mutableListOf(photo2, photo3)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun prepopulateDatabase() = runBlocking {
        agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
        typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
        poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)

        property1 = Property(
            propertyId = 1L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = 1L
        )
        property2 = Property(
            propertyId = 2L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = 2L
        )
        property3 = Property(
            propertyId = 3L, typeId = TypeEnum.UNASSIGNED.key, addressId = null, price = null,
            description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
            nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = 3L
        )
        propertyDao.prepopulateWithProperties(listOf(property1, property2, property3))
    }


    /**
     * Testing upsertPhoto() and getPhoto()
     */
    @Test
    fun testUpsertAndGetPhoto() = runBlocking {
        // Function under test (inserting photo)
        photoDao.upsertPhoto(photo1)

        // Get the photo from database to check the insertion (other function under test)
        var result = photoDao.getPhoto(1L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(photo1.photoId, result.photoId)
        assertEquals(photo1.label, result.label)

        // Function under test (updating photo)
        photoDao.upsertPhoto(photo1updated)

        // Get the photo from database to check the update (other function under test)
        result = photoDao.getPhoto(1L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(photo1.photoId, result.photoId)
        assertEquals(photo1updated.photoId, result.photoId)
        assertNotEquals(photo1.label, result.label)
        assertEquals(photo1updated.label, result.label)
    }

    /**
     * Testing prepopulateWithPhotos() and getPhotos()
     */
    @Test
    fun testPrepopulateWithPhotosAndGetPhotos() = runBlocking {
        // Function under test
        var result = photoDao.getPhotos().first()

        // First assertion
        assertTrue(result.isEmpty())

        // Functions under test
        photoDao.prepopulateWithPhotos(photos)
        result = photoDao.getPhotos().first()

        // New assertion
        assertEquals(3, result.size)
        assertTrue(result.contains(photo1))
        assertTrue(result.contains(photo2))
        assertTrue(result.contains(photo3))
    }

    @Test
    fun testDeletePhoto() = runBlocking {
        // Prepopulate database
        photoDao.prepopulateWithPhotos(photos)

        // Initial assertions
        var result = photoDao.getPhotos().first()
        assertEquals(3, result.size)
        assertTrue(result.contains(photo2))

        // Functions under test
        photoDao.deletePhoto(photo2)

        // Final assertions
        result = photoDao.getPhotos().first()
        assertEquals(2, result.size)
        assertFalse(result.contains(photo2))
    }

    /**
     * Testing upsertPhotos() and getPhotos()
     */
    @Test
    fun testUpsertAndGetPhotos() = runBlocking {
        // Function under test
        var result = photoDao.getPhotos().first()

        // First assertion
        assertTrue(result.isEmpty())

        // Functions under test (inserting photos)
        photoDao.upsertPhotos(mutableListOf(photo2, photo3, photo1))
        result = photoDao.getPhotos().first()

        // New assertion
        assertEquals(3, result.size)
        assertTrue(result.contains(photo1))
        assertTrue(result.contains(photo2))
        assertTrue(result.contains(photo3))

        // Function under test (updating photos)
        photoDao.upsertPhotos(mutableListOf(photo2, photo3, photo1updated))
        result = photoDao.getPhotos().first()

        // New assertion
        assertEquals(3, result.size)
        assertTrue(result.contains(photo1updated))
        assertTrue(result.contains(photo2))
        assertTrue(result.contains(photo3))
    }

    @Test
    fun testDeletePhotos() = runBlocking {
        // Prepopulate database
        photoDao.prepopulateWithPhotos(photos)

        // Initial assertions
        var result = photoDao.getPhotos().first()
        assertEquals(3, result.size)
        assertTrue(result.contains(photo1))
        assertTrue(result.contains(photo2))
        assertTrue(result.contains(photo3))

        // Functions under test
        photoDao.deletePhotos(photosToDelete)

        // Final assertions
        result = photoDao.getPhotos().first()
        assertEquals(1, result.size)
        assertTrue(result.contains(photo1))
        assertFalse(result.contains(photo2))
        assertFalse(result.contains(photo3))
    }

    @Test
    fun testGetPhotosOrderedById() = runBlocking {
        // Prepopulate database
        photoDao.prepopulateWithPhotos(photos)

        // Functions under test
        val result = photoDao.getPhotosOrderedById().first()

        // Assertions
        assertEquals(3, result.size)
        assertEquals(1L, result[0].photoId)
        assertEquals(2L, result[1].photoId)
        assertEquals(3L, result[2].photoId)
    }

    @Test
    fun testGetPhotosOrderedByLabel() = runBlocking {
        // Prepopulate database
        photoDao.prepopulateWithPhotos(photos)

        // Functions under test
        val result = photoDao.getPhotosOrderedByLabel().first()

        // Assertions (photos should be sorted by ascending labels)
        assertEquals(3, result.size)
        assertEquals(3L, result[0].photoId)
        assertEquals("PhotoA", result[0].label)
        assertEquals(2L, result[1].photoId)
        assertEquals("PhotoB", result[1].label)
        assertEquals(1L, result[2].photoId)
        assertEquals("PhotoC", result[2].label)
    }


    // ContentProvider

    @Test
    fun testGetPhotosWithCursor() = runBlocking {
        // Prepopulate database
        photoDao.prepopulateWithPhotos(photos)

        // Function under test
        val cursor = photoDao.getPhotosWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(3, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(1L, cursor.getLong(0))
        assertEquals("PhotoC", cursor.getString(2))
        cursor.moveToPosition(1)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("PhotoB", cursor.getString(2))
        cursor.moveToPosition(2)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("PhotoA", cursor.getString(2))

        // Close cursor
        cursor.close()
    }

}