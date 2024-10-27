package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.AgentEnum
import com.aquaero.realestatemanager.repository.AgentRepository
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
 * Testing AgentRepository
 */
class AgentRepositoryTest {
    private lateinit var agentDao: AgentDao
    private lateinit var repository: AgentRepository

    private var agentId1 by Delegates.notNull<Long>()
    private var agentId2 by Delegates.notNull<Long>()
    private var agentId3 by Delegates.notNull<Long>()
    private lateinit var agent1: Agent
    private lateinit var agent2: Agent
    private lateinit var agent1Flow: Flow<Agent>
    private lateinit var agents: MutableList<Agent>
    private lateinit var agentsOrderedById: MutableList<Agent>
    private lateinit var agentsFlow: Flow<MutableList<Agent>>
    private lateinit var agentsOrderedByIdFlow: Flow<MutableList<Agent>>
    private lateinit var stringAgent1: String
    private lateinit var stringAgent2: String
    private lateinit var stringAgents: MutableList<String>
    private lateinit var otherStringAgents: MutableList<String>
    private lateinit var firstName2: String
    private lateinit var lastName2: String
    private lateinit var agent2ToString: String

    @Before
    fun setup() {
        agentDao = mock(AgentDao::class.java)
        repository = AgentRepository(agentDao)

        agentId1 = 1L
        agentId2 = 2L
        agentId3 = 3L
        agent1 = mock(Agent::class.java)
        agent2 = mock(Agent::class.java)
        agent1Flow = flowOf(agent1)
        agents = mutableListOf(agent2, agent1)
        agentsOrderedById = mutableListOf(agent1, agent2)
        agentsFlow = flowOf(agents)
        agentsOrderedByIdFlow = flowOf(agentsOrderedById)
        stringAgent1 = "stringAgent1"
        stringAgent2 = "stringAgent2"
        stringAgents = mutableListOf(stringAgent2, stringAgent1)
        otherStringAgents = mutableListOf(stringAgent1)
        firstName2 = "firstName2"
        lastName2 = "lastName2"
        agent2ToString = "$firstName2 $lastName2"

        // Setup mocks behaviour
        doReturn(agent1Flow).`when`(agentDao).getAgent(agentId1)
        doReturn(agentsFlow).`when`(agentDao).getAgents()
        doReturn(agentsOrderedByIdFlow).`when`(agentDao).getAgentsOrderedById()
        doReturn(agentsFlow).`when`(agentDao).getAgentsOrderedByName()
        doReturn(agentId1).`when`(agent1).agentId
        doReturn(agentId2).`when`(agent2).agentId
        doReturn(agent2ToString).`when`(agent2).toString()
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertAgentInRoom() = runBlocking {
        // Function under test
        repository.upsertAgentInRoom(agent1)

        // Verification
        verify(agentDao).upsertAgent(agent1)
    }

    @Test
    fun testDeleteAgentFromRoom() = runBlocking {
        // Function under test
        repository.deleteAgentFromRoom(agent1)

        // Verification
        verify(agentDao).deleteAgent(agent1)
    }

    @Test
    fun testGetAgentFromRoom() {
        // Function under test
        val result = repository.getAgentFromRoom(agentId1)

        // Verification and assertion
        verify(agentDao).getAgent(agentId1)
        assertEquals(agent1Flow, result)
    }

    @Test
    fun testGetAgentsFromRoom() {
        // Function under test
        val result = repository.getAgentsFromRoom()

        // Verification and assertion
        verify(agentDao).getAgents()
        assertEquals(agentsFlow, result)
    }

    @Test
    fun testGetAgentsOrderedByIdFromRoom() {
        // Function under test
        val result = repository.getAgentsOrderedByIdFromRoom()

        // Verification and assertion
        verify(agentDao).getAgentsOrderedById()
        assertEquals(agentsOrderedByIdFlow, result)
    }

    @Test
    fun testGetAgentsOrderedByNameFromRoom() {
        // Function under test
        val result = repository.getAgentsOrderedByNameFromRoom()

        // Verification and assertion
        verify(agentDao).getAgentsOrderedByName()
        assertEquals(agentsFlow, result)
    }

    @Test
    fun testAgentFromId() {
        // Function under test
        val result = repository.agentFromId(agentId2, agents)

        // Verification and assertion
        assertEquals(agent2, result)
    }

    @Test
    fun testStringAgent() {
        // 1- Function under test when :
        // - The agent ID corresponds to an agent from agents
        // - agents and stringAgents have the same size
        var result = repository.stringAgent(agentId1, agents, stringAgents)
        // Assertion
        assertEquals(stringAgent1, result)

        // 2- Function under test when :
        // - The agent ID doesn't correspond to an agent from agents
        // - agents and stringAgents have the same size
        result = repository.stringAgent(agentId3, agents, stringAgents)
        // Assertion (By default, should return the value at index 0 from stringAgents)
        assertEquals(stringAgent2, result)

        // 3- Function under test when :
        // - The agent ID corresponds to an agent from agents
        // - agents and stringAgents have different sizes
        result = repository.stringAgent(agentId2, agents, otherStringAgents)
        // Assertion
        assertEquals(agent2ToString, result)

        // 4- Function under test when :
        // - The agent ID doesn't correspond to an agent from agents
        // - agents and stringAgents have different sizes
        result = repository.stringAgent(agentId3, agents, otherStringAgents)
        // Assertion
        assertEquals(AgentEnum.UNASSIGNED.key, result)

        // 5- Function under test when :
        // - The agent ID corresponds to an agent from agents
        // - stringAgents is empty
        result = repository.stringAgent(agentId2, agents, mutableListOf())
        // Assertion
        assertEquals(agent2ToString, result)

        // 6- Function under test when :
        // - The agent ID doesn't correspond to an agent from agents
        // - stringAgents is empty
        result = repository.stringAgent(agentId3, agents, mutableListOf())
        // Assertion
        assertEquals(AgentEnum.UNASSIGNED.key, result)
    }

}