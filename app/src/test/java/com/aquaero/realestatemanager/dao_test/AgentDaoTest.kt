package com.aquaero.realestatemanager.dao_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.AgentEnum
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

@RunWith(AndroidJUnit4::class)
/**
 * Testing AgentDao
 */
class AgentDaoTest {

    // Use of InstantTaskExecutor rule to manage threading
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao

    private lateinit var agent5: Agent
    private lateinit var agent6: Agent
    private lateinit var agent7: Agent
    private lateinit var agents: List<Agent>
    private lateinit var agent5updated: Agent

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

        agent5 = Agent(5L, "FirstName5", "LastName5")
        agent6 = Agent(6L, "FirstName6", "LastName6")
        agent7 = Agent(7L, "FirstName7", "LastName7")
        agent5updated = Agent(5L, "FirstName5Updated", "LastName5Updated")

        agents = listOf(agent6, agent7, agent5)
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
     * Testing upsertAgent() and getAgent()
     */
    @Test
    fun testUpsertAndGetAgent() = runBlocking {
        // Function under test (inserting agent)
        agentDao.upsertAgent(agent5)

        // Get the agent from database to check the insertion (other function under test)
        var result = agentDao.getAgent(5L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(agent5.agentId, result.agentId)
        assertEquals(agent5.firstName, result.firstName)

        // Function under test (updating agent)
        agentDao.upsertAgent(agent5updated)

        // Get the agent from database to check the update (other function under test)
        result = agentDao.getAgent(5L).first()

        // Assertions
        assertNotNull(result)
        assertEquals(agent5.agentId, result.agentId)
        assertEquals(agent5updated.agentId, result.agentId)
        assertNotEquals(agent5.firstName, result.firstName)
        assertEquals(agent5updated.firstName, result.firstName)
    }

    /**
     * Testing prepopulateWithAgents() and getAgents()
     */
    @Test
    fun testPrepopulateWithAgentsAndGetAgents() = runBlocking {
        // Function under test
        var result = agentDao.getAgents().first()

        // First assertion
        assertEquals(4, result.size)    // Pre-populated in setup
        assertFalse(result.contains(agent5))
        assertFalse(result.contains(agent6))
        assertFalse(result.contains(agent7))

        // Functions under test
        agentDao.prepopulateWithAgents(agents)  // Add 3 agents to the first three pre-populated in setup
        result = agentDao.getAgents().first()

        // New assertion
        assertEquals(7, result.size)
        assertTrue(result.contains(agent5))
        assertTrue(result.contains(agent6))
        assertTrue(result.contains(agent7))
    }

    @Test
    fun testDeleteAgent() = runBlocking {
        // Prepopulate database
        agentDao.prepopulateWithAgents(agents)  // Add 3 agents to the first three pre-populated in setup

        // Initial assertions
        var result = agentDao.getAgents().first()
        assertEquals(7, result.size)
        assertTrue(result.contains(agent6))

        // Functions under test
        agentDao.deleteAgent(agent6)

        // Final assertions
        result = agentDao.getAgents().first()
        assertEquals(6, result.size)
        assertFalse(result.contains(agent6))
    }

    @Test
    fun testGetAgentsOrderedById() = runBlocking {
        // Functions under test
        val result = agentDao.getAgentsOrderedById().first()

        // Assertions
        assertEquals(4, result.size)
        assertEquals(1L, result[0].agentId)
        assertEquals(2L, result[1].agentId)
        assertEquals(3L, result[2].agentId)
        assertEquals(4L, result[3].agentId)
    }

    @Test
    fun testGetAgentsOrderedByName() = runBlocking {
        // Update agent names
        agentDao.upsertAgent(Agent(1L, "FirstNameA", "LastNameD"))
        agentDao.upsertAgent(Agent(2L, "FirstNameB", "LastNameC"))
        agentDao.upsertAgent(Agent(3L, "FirstNameC", "LastNameB"))
        agentDao.upsertAgent(Agent(4L, "FirstNameD", "LastNameA"))

        // Functions under test
        val result = agentDao.getAgentsOrderedByName().first()

        // Assertions (agents should be sorted by ascending [last names + first names])
        assertEquals(4, result.size)
        assertEquals(4L, result[0].agentId)
        assertEquals("FirstNameD", result[0].firstName)
        assertEquals("LastNameA", result[0].lastName)
        assertEquals(3L, result[1].agentId)
        assertEquals("FirstNameC", result[1].firstName)
        assertEquals("LastNameB", result[1].lastName)
        assertEquals(2L, result[2].agentId)
        assertEquals("FirstNameB", result[2].firstName)
        assertEquals("LastNameC", result[2].lastName)
        assertEquals(1L, result[3].agentId)
        assertEquals("FirstNameA", result[3].firstName)
        assertEquals("LastNameD", result[3].lastName)
    }


    // ContentProvider

    @Test
    fun testGetAgentsWithCursor() = runBlocking {
        // Function under test
        val cursor = agentDao.getAgentsWithCursor()

        // Assertions
        assertNotNull(cursor)
        assertEquals(4, cursor.count)
        cursor.moveToPosition(0)
        assertEquals(1L, cursor.getLong(0))
        assertEquals(AgentEnum.UNASSIGNED.key, cursor.getString(1))
        assertEquals("", cursor.getString(2))
        cursor.moveToPosition(1)
        assertEquals(2L, cursor.getLong(0))
        assertEquals("F2222222", cursor.getString(1))
        assertEquals("N2222222", cursor.getString(2))
        cursor.moveToPosition(2)
        assertEquals(3L, cursor.getLong(0))
        assertEquals("F3333333", cursor.getString(1))
        assertEquals("N3333333", cursor.getString(2))
        cursor.moveToPosition(3)
        assertEquals(4L, cursor.getLong(0))
        assertEquals("F4444444", cursor.getString(1))
        assertEquals("N4444444", cursor.getString(2))

        // Close cursor
        cursor.close()
    }

}