package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.UNASSIGNED_ID

@Entity
data class Agent(
    @PrimaryKey(autoGenerate = true)
    val agentId: Long = 0,
    val firstName: String,
    val lastName: String?,
) {
    override fun toString(): String {
        return firstName + (lastName?.let { " $it" } ?: "")
    }
}

/**
 * String Key for unassigned agent.
 * For formatted display and translation, this key should be set in the string resources files.
 */
enum class AgentEnum(val key: String) {
    UNASSIGNED(key = "_unassigned_"),
}

val AGENT_PREPOPULATION_DATA = listOf(
    Agent(
        agentId = UNASSIGNED_ID,
        firstName = AgentEnum.UNASSIGNED.key,
        lastName = null,
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
    Agent(
        agentId = 4,
        firstName = "F4444444",
        lastName = "N4444444",
    ),
).sortedBy { it.lastName + it.firstName }

