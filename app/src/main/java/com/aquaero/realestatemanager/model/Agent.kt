package com.aquaero.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.CACHE_EMPTY_STRING_VALUE
import com.aquaero.realestatemanager.CACHE_LONG_ID_VALUE
import com.aquaero.realestatemanager.CACHE_NULLABLE_VALUE
import com.aquaero.realestatemanager.NEW_ITEM_ID
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

/** Theis Enum key should be set inside string resource file for formatted display and translation **/
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

val CACHE_AGENT = Agent(
    agentId = CACHE_LONG_ID_VALUE,
    firstName = CACHE_EMPTY_STRING_VALUE,
    lastName = CACHE_NULLABLE_VALUE
)