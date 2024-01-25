package com.aquaero.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Agent
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun prepopulateWithAgents(agents: List<Agent>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertAgent(agent: Agent)

    @Delete
    suspend fun deleteAgent(agent: Agent)

    @Query("SELECT * FROM agent WHERE agentId = :aId")
    fun getAgent(aId: Long): Flow<Agent>

    @Query("SELECT * FROM agent")
    fun getAgents(): Flow<MutableList<Agent>>

    @Query("SELECT * FROM agent ORDER BY agentId ASC")
    fun getAgentsOrderedById(): Flow<MutableList<Agent>>

    @Query("SELECT * FROM agent ORDER BY lastName + firstName ASC")
    fun getAgentsOrderedByName(): Flow<MutableList<Agent>>

}