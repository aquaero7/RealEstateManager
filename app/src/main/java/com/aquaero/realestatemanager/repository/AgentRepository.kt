package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.AgentEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AgentRepository(private val agentDao: AgentDao) {


    /* Room: Database CRUD */

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

    fun getAgentFromRoom(aId: Long): Flow<Agent> {
        return agentDao.getAgent(aId)
    }

    fun getAgentsFromRoom(): Flow<MutableList<Agent>> {
        return agentDao.getAgents()
    }

    fun getAgentsOrderedByIdFromRoom(): Flow<MutableList<Agent>> {
        return agentDao.getAgentsOrderedById()
    }

    fun getAgentsOrderedByNameFromRoom(): Flow<MutableList<Agent>> {
        return agentDao.getAgentsOrderedByName()
    }

    /**/


    fun agentFromId(agentId: Long, agents: MutableList<Agent>): Agent? {
        return agents.find { it.agentId == agentId }
    }

    fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        val agent = agents.find { it.agentId == agentId }
        val agentIndex = agent?.let { agents.indexOf(it) } ?: 0  // Index 0 should correspond to "Unassigned"
        return if (agentIndex != -1 && stringAgents.isNotEmpty() && stringAgents.size == agents.size)
            stringAgents.elementAt(agentIndex) else agent?.toString() ?: AgentEnum.UNASSIGNED.key
    }

}

