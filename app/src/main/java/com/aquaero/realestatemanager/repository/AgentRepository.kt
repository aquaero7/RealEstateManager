package com.aquaero.realestatemanager.repository

import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.model.Agent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AgentRepository(private val agentDao: AgentDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD **/

    suspend fun upsertAgentInRoom(agent: Agent) {
        withContext(Dispatchers.IO) {
            agentDao.upsertAgent(agent)
        }
    }

    suspend fun deleteAgentFromRoom(agent: Agent) {
        withContext(Dispatchers.IO) {
            agentDao.deleteAgent(agent)
        }
    }

    suspend fun getAgentFromRoom(aId: Long): Flow<Agent> {
        return withContext(Dispatchers.IO) {
            agentDao.getAgent(aId)
        }
    }

    suspend fun getAgentsFromRoom(): Flow<MutableList<Agent>> {
        return withContext(Dispatchers.IO) {
            agentDao.getAgents()
        }
    }

    suspend fun getAgentsOrderedByIdFromRoom(): Flow<MutableList<Agent>> {
        return withContext(Dispatchers.IO) {
            agentDao.getAgentsOrderedById()
        }
    }

    suspend fun getAgentsOrderedByNameFromRoom(): Flow<MutableList<Agent>> {
        return withContext(Dispatchers.IO) {
            agentDao.getAgentsOrderedByName()
        }
    }

    /***/


    fun agentFromId(agentId: Long): Agent? {
        fakeAgents.forEach {
            if (it.agentId == agentId) return it
        }
        return null
    }

    fun stringAgent(agents: MutableList<Agent>, agentId: Long): String {
        return agents.find { it.agentId == agentId }.toString()
    }


    /**
     * 'agentsSet' is nor a variable but a lambda.
     * The function is straightly assigned to the variable
     * to avoid the creation of a function, then a variable,
     * and then the assignment of the function to the variable
     */
    val agentsSet: () -> MutableSet<String?> = fun(): MutableSet<String?> {
        // val agentsSet: MutableSet<String?> = mutableSetOf()
        val agentsSet: MutableSet<String?> = mutableSetOf(context.getString(R.string.unassigned)) // Empty field for unassigned agent case
        fakeAgents.forEach { agentsSet.add(it.toString()) }
        return agentsSet
    }

    // ...




    //
    /**
     * FAKE AGENTS
     */

    val fakeAgents = listOf(
        Agent(
            agentId = -1,
            firstName = "F1111111",
            lastName = "N1111111",
        ),
        Agent(
            agentId = -2,
            firstName = "F2222222",
            lastName = "N2222222",
        ),
        Agent(
            agentId = -3,
            firstName = "F3333333",
            lastName = "N3333333",
        ),
    )
    //
}

