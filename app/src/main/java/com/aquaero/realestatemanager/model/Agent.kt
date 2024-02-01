package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.NO_ITEM_ID
import com.aquaero.realestatemanager.NULL_ITEM_ID

@Entity
data class Agent(
    @PrimaryKey(autoGenerate = true)
    val agentId: Long = 0,
    val firstName: String,
    val lastName: String?,
) {
    override fun toString(): String {
//        return "$firstName $lastName"
        return firstName + (lastName?.let { " $it" } ?: "")
    }
}

/** Theis Enum key should be set inside string resource file for formatted display and translation **/
enum class AgentEnum(val key: String) {
    UNASSIGNED(key = "_unassigned_"),
}

val AGENT_PREPOPULATION_DATA = listOf(
    Agent(
        agentId = NULL_ITEM_ID,
        firstName = AgentEnum.UNASSIGNED.key,
        lastName = null,
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
).sortedBy { it.lastName + it.firstName }
