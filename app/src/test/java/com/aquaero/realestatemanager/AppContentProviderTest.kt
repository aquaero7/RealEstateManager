package com.aquaero.realestatemanager

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.provider.AppContentProvider
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
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppContentProviderTest {

    private lateinit var testContext: Context
    private lateinit var contentResolver: ContentResolver

    private var testDatabase: AppDatabase? = null
    private lateinit var typeDao: TypeDao
    private lateinit var agentDao: AgentDao
    private lateinit var propertyDao: PropertyDao

    @Before
    fun setUp() {
        testContext = ApplicationProvider.getApplicationContext()
        Robolectric.setupContentProvider(AppContentProvider::class.java)
        contentResolver = testContext.contentResolver
    }

    @After
    fun tearDown() {
        testDatabase?.close()
    }

    @Test
    fun getItemsWhenNoItemInserted() {

        // Initialize uri for query
        val (uri, projection) = initUriForQuery()

        runBlocking {
            // Initialize cursor and make asynchronous query
            val cursor: Cursor? = cursorAndQuery(uri, projection)

            // Test assertions
            assertNotNull(cursor)
            assertEquals(0, cursor?.count)

            // Close cursor
            cursor?.close()
        }
    }

    @Test
    fun getItemsWhenOneItemInserted() {

        // Initialize database and entities
        initDatabase()
        val (type1, agent1, property1) = initEntities()
        val saleDate1 = property1.saleDate

        // Initialize uri for query
        val (uri, projection) = initUriForQuery()

        runBlocking {

            // Populate database
            populateDatabase(type1, agent1, property1)

            // Initialize cursor and make asynchronous query
            val cursor: Cursor? = cursorAndQuery(uri, projection)

            // Test assertions
            assertNotNull(cursor)
            assertEquals(1, cursor?.count)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val cursorPropId = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                    val cursorSaleDate = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))

                    assertEquals(1, cursorPropId)
                    assertEquals(saleDate1, cursorSaleDate)
//                    assertNull(saleDate)
                } while (cursor.moveToNext())
            } else {
                fail("Cursor is empty")
            }

            // Close cursor
            cursor?.close()
        }
    }



    /**/

    private fun initUriForQuery(): Pair<Uri, Array<String>> {
        val uri = Uri.parse(
            "${Path.CONTENT}://${BuildConfig.APPLICATION_ID}.${Path.CONTENT_PROVIDER}/${AppContentProvider.TABLE_NAME}"
        )
        val projection = arrayOf(PropertyKey.PROPERTY_ID, PropertyKey.SALE_DATE)

        return Pair(uri, projection)
    }

    private suspend fun cursorAndQuery(uri: Uri, projection: Array<String>): Cursor? =
        withContext(Dispatchers.IO) {
            contentResolver.query(uri, projection, null, null, null)
        }


    private fun initDatabase() {
        testDatabase = AppDatabase.getInstance(testContext)
        typeDao = testDatabase!!.typeDao
        agentDao = testDatabase!!.agentDao
        propertyDao = testDatabase!!.propertyDao
    }

    private fun initEntities(): Triple<Type, Agent, Property> {
        val saleDate = "2024-03-01"
        val type1 = Type(TypeEnum.UNASSIGNED.key)
        val agent1 = Agent(1, "firstName1", "name1")

        val property1 =
            Property(
                1, TypeEnum.UNASSIGNED.key, null, null, null, null,
                null, null, null, null, saleDate, 1
            )

        return Triple(type1, agent1, property1)
    }

    private suspend fun populateDatabase(type: Type, agent: Agent, property: Property) {
        typeDao.upsertType(type)
        agentDao.upsertAgent(agent)
        propertyDao.upsertProperty(property)
    }

}