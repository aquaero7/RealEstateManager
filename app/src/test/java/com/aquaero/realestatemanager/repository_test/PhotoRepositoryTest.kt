package com.aquaero.realestatemanager.repository_test

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.never
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.InputStream
import kotlin.properties.Delegates

//@RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
/**
 * Testing PhotoRepository
 */
class PhotoRepositoryTest {
    private lateinit var photoDao: PhotoDao
    private lateinit var repository: PhotoRepository
    private lateinit var spyRepository: PhotoRepository
    private lateinit var context: Context

    private var photoId1 by Delegates.notNull<Long>()
    private var photoId2 by Delegates.notNull<Long>()
    private lateinit var photo1: Photo
    private lateinit var photo2: Photo
    private lateinit var photos: MutableList<Photo>
    private lateinit var photosOrderedById: MutableList<Photo>
    private lateinit var photo1Flow: Flow<Photo>
    private lateinit var photosFlow: Flow<MutableList<Photo>>
    private lateinit var photosOrderedByIdFlow: Flow<MutableList<Photo>>
    private lateinit var path: String
    private lateinit var file: File
    private lateinit var uri: Uri
    private lateinit var cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>
    private lateinit var camPermLauncher: ManagedActivityResultLauncher<String, Boolean>

    @Before
    fun setup() {
        photoDao = mock(PhotoDao::class.java)
        context = mock(Context::class.java)
        photo1 = mock(Photo::class.java)
        photo2 = mock(Photo::class.java)
        cameraLauncher = mock()
        camPermLauncher = mock()

        repository = PhotoRepository(photoDao)
        spyRepository = spy(repository)

        photoId1 = 1L
        photoId2 = 2L
        photos = mutableListOf(photo2, photo1)
        photosOrderedById = mutableListOf(photo1, photo2)
        photo1Flow = flowOf(photo1)
        photosFlow = flowOf(photos)
        photosOrderedByIdFlow = flowOf(photosOrderedById)
        path = "com.aquaero.realestatemanager.fileprovider"
        val fullPath = "content://$path"
        val fileName = "test_image.jpg"
        file = File(fileName)
        uri = Uri.parse("$fullPath/$fileName")

        // Setup mocks behaviour
        doReturn(photo1Flow).`when`(photoDao).getPhoto(photoId1)
        doReturn(photosFlow).`when`(photoDao).getPhotos()
        doReturn(photosOrderedByIdFlow).`when`(photoDao).getPhotosOrderedById()
        doReturn(photosFlow).`when`(photoDao).getPhotosOrderedByLabel()
        doReturn(photoId2).`when`(photo2).photoId
        doReturn(file).`when`(spyRepository).createImageFile(context)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertPhotoInRoom() = runBlocking {
        // Function under test
        repository.upsertPhotoInRoom(photo1)

        // Verification
        verify(photoDao).upsertPhoto(photo1)
    }

    @Test
    fun testDeletePhotoFromRoom() = runBlocking {
        // Function under test
        repository.deletePhotoFromRoom(photo1)

        // Verification
        verify(photoDao).deletePhoto(photo1)
    }

    @Test
    fun testUpsertPhotosInRoom() = runBlocking {
        // Function under test
        repository.upsertPhotosInRoom(photos)

        // Verification
        verify(photoDao).upsertPhotos(photos)
    }

    @Test
    fun testDeletePhotosFromRoom() = runBlocking {
        // Function under test
        repository.deletePhotosFromRoom(photos)

        // Verification
        verify(photoDao).deletePhotos(photos)
    }

    @Test
    fun testGetPhotoFromRoom() {
        // Function under test
        val result = repository.getPhotoFromRoom(photoId1)

        // Verification and assertion
        verify(photoDao).getPhoto(photoId1)
        assertEquals(photo1Flow, result)
    }

    @Test
    fun testGetPhotosFromRoom() {
        // Function under test
        val result = repository.getPhotosFromRoom()

        // Verification and assertion
        verify(photoDao).getPhotos()
        assertEquals(photosFlow, result)
    }

    @Test
    fun testGetPhotosOrderedByIdFromRoom() {
        // Function under test
        val result = repository.getPhotosOrderedByIdFromRoom()

        // Verification and assertion
        verify(photoDao).getPhotosOrderedById()
        assertEquals(photosOrderedByIdFlow, result)
    }

    @Test
    fun testGetPhotosOrderedByLabelFromRoom() {
        // Function under test
        val result = repository.getPhotosOrderedByLabelFromRoom()

        // Verification and assertion
        verify(photoDao).getPhotosOrderedByLabel()
        assertEquals(photosFlow, result)
    }

    @Test
    fun testPhotoFromId() {
        // Function under test
        val result = repository.photoFromId(photoId2, photos)

        // Assertion
        assertEquals(photo2, result)
    }

    @Test
    fun testCreateImageFile() {
        // Function under test
        val result = repository.createImageFile(context)

        // Assertions
        assertNotNull(result)
        assert(result.exists())
        assert(result.name.startsWith("IMG_"))
        assert(result.extension == "jpg")
    }

    @Test
    fun testGetPhotoUri() {
        mockStatic(FileProvider::class.java).use { mockStatic ->
            // Setup static mock behaviour
            mockStatic.`when`<Uri> {
                FileProvider.getUriForFile(context, path, file)
            }.thenReturn(uri)

            // Function under test
            val result = spyRepository.getPhotoUri(context)

            // Verification and assertion
            assertEquals(uri, result)
        }
    }

    @Test
    fun testOnShootPhotoMenuItemClick() {
        /* Test when camera permission is granted */

        // Setup camera permission
        `when`(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA))
            .thenReturn(PackageManager.PERMISSION_GRANTED)

        // Function under test
        repository.onShootPhotoMenuItemClick(context, uri, cameraLauncher, camPermLauncher)

        // Verifications
        verify(cameraLauncher).launch(uri)
        verify(camPermLauncher, never()).launch(Manifest.permission.CAMERA)


        // Reset mocks
        reset(cameraLauncher, camPermLauncher)


        /* Test when camera permission is denied */

        // Setup camera permission
        `when`(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA))
            .thenReturn(PackageManager.PERMISSION_DENIED)

        // Function under test
        repository.onShootPhotoMenuItemClick(context, uri, cameraLauncher, camPermLauncher)

        // Verifications
        verify(cameraLauncher, never()).launch(uri)
        verify(camPermLauncher).launch(Manifest.permission.CAMERA)
    }

    @Test
    fun testOnSelectPhotoMenuItemClick() {
        val pickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> = mock()
        val requestArgumentCaptor = argumentCaptor<PickVisualMediaRequest>()
        val expectedMediaType = ActivityResultContracts.PickVisualMedia.ImageOnly

        // Function under test
        repository.onSelectPhotoMenuItemClick(pickerLauncher)

        // Verification and assertion
        verify(pickerLauncher).launch(requestArgumentCaptor.capture())
        assertEquals(expectedMediaType, requestArgumentCaptor.allValues[0].mediaType)
    }

    @Test
    fun testSaveToInternalStorage() {
        val filesDir = File("filesDir")
        whenever(context.filesDir).thenReturn(filesDir)

        val picturesDir = File(filesDir, "Pictures")
        picturesDir.mkdirs()

        val contentResolver: ContentResolver = mock()
        val inputStream: InputStream = mock()
        whenever(context.contentResolver).thenReturn(contentResolver)
        whenever(contentResolver.openInputStream(uri)).thenReturn(inputStream)
        // Simulate reading the input stream
        whenever(inputStream.read(any())).thenAnswer { invocation ->
            val buffer = invocation.getArgument<ByteArray>(0)
            buffer[0] = 1           // Simulate 1 byte of data
            return@thenAnswer 1     // Return the length read
        }.thenReturn(-1)    // Simulate the end of the stream

        val expectedOutputFile = File(context.filesDir, "Pictures/photo.jpg")
        val uris = listOf(Uri.parse(
            // Testing an uri with and without extension
            "content://uri/photo.jpg"), Uri.parse("content://uri/photo")
        )

        uris.forEach { uri ->
            // Function under test
            val result = repository.saveToInternalStorage(context, uri)

            // Verifications and assertions
            println("uri = $uri")
            println("pickerUri = $result")

            assertNotNull(result)
            verify(context.contentResolver).openInputStream(uri)
            assertTrue(expectedOutputFile.exists())
            assertEquals(expectedOutputFile.toUri(), result)
        }
    }

    @Test
    fun testItemPhotos() {
        doReturn(1L).`when`(photo1).propertyId
        doReturn(2L).`when`(photo2).propertyId
        // Function under test
        var result = repository.itemPhotos(2L, photos)
        // Verifications and assertions
        assertEquals(1, result.size)
        result.forEach {
            assertEquals(2L, it.propertyId)
        }

        doReturn(1L).`when`(photo2).propertyId
        // Function under test
        result = repository.itemPhotos(2L, photos)
        // Assertion
        assertTrue(result.isEmpty())
    }

}