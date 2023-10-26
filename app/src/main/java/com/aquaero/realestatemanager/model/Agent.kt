package com.aquaero.realestatemanager.model

data class Agent(
    val agentId: Long,
    val agentName: String,
    val agentFirstName: String
) {
    override fun toString(): String {
        return "$agentFirstName $agentName"
    }
}
