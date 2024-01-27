package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Agent(
    @PrimaryKey(autoGenerate = true)
    val agentId: Long,
    val firstName: String,
    val lastName: String?,
) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}

val AGENT_PREPOPULATION_DATA = listOf(
    Agent(
        agentId = 0,
        firstName = "",
        lastName = "",
    ),
    Agent(
        agentId = 1,
        firstName = "F1111111",
        lastName = "N1111111",
    ),
    Agent(
        agentId = 2,
        firstName = "F2222222",
        lastName = "N2222222",
    ),
    Agent(
        agentId = 3,
        firstName = "F3333333",
        lastName = "N3333333",
    ),
)
