package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Agent(
    @PrimaryKey(autoGenerate = true)
    val agentId: Long,
    val lastName: String,
    val firstName: String
) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}

val AGENT_PREPOPULATION_DATA = listOf(
    Agent(
        agentId = 1,
        lastName = "N1111111",
        firstName = "F1111111"
    ),
    Agent(
        agentId = 2,
        lastName = "N2222222",
        firstName = "F2222222"
    ),
    Agent(
        agentId = 3,
        lastName = "N3333333",
        firstName = "F3333333"
    )
)
