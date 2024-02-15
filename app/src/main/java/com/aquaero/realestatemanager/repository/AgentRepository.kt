package com.aquaero.realestatemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.AgentEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AgentRepository(private val agentDao: AgentDao) {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    /** Room: Database CRUD */

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

    @SuppressLint("DiscouragedApi")
    fun getStringAgentsFromRoom(): Flow<MutableList<String>> {
        return agentDao.getAgents()
            .map { agents ->
                agents.map {
                    val reference = it.firstName
                    if (reference != AgentEnum.UNASSIGNED.key) {
                        it.toString()
                    } else {
                        val resourceId = context.resources.getIdentifier(
                            reference,
                            "string",
                            context.packageName
                        )
                        if (resourceId != 0) context.getString(resourceId) else reference
                    }
                }.toMutableList()
            }
    }

    @SuppressLint("DiscouragedApi")
    fun getStringAgentsOrderedByNameFromRoom(): Flow<MutableList<String>> {
        return agentDao.getAgentsOrderedByName()
            .map { agents ->
                agents.map {
                    val reference = it.firstName
                    if (reference != AgentEnum.UNASSIGNED.key) {
                        it.toString()
                    } else {
                        val resourceId = context.resources.getIdentifier(
                            reference,
                            "string",
                            context.packageName
                        )
                        if (resourceId != 0) context.getString(resourceId) else reference
                    }
                }.toMutableList()
            }
    }

    /***/


    fun agentFromId(agentId: Long, agents: MutableList<Agent>): Agent? {
        return agents.find { it.agentId == agentId }
    }

    fun stringAgent(agentId: Long, agents: MutableList<Agent>, stringAgents: MutableList<String>): String {
        /*
        val agent = agents.first { it.agentId == agentId }
        val agentIndex = agents.indexOf(agent)
        return if (agentIndex != -1 && stringAgents.isNotEmpty()) stringAgents.elementAt(agentIndex) else agent.toString()
        */
        val agent = agents.find { it.agentId == agentId }
        val agentIndex = agent?.let { agents.indexOf(agent) } ?: 0
        return if (agentIndex != -1 && stringAgents.isNotEmpty()) stringAgents.elementAt(agentIndex) else agent.toString()
    }



    /** FAKE AGENTS */
    /*
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
    */


}

