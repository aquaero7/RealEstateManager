package com.aquaero.realestatemanager.repository

import com.aquaero.realestatemanager.model.Agent

class AgentRepository() {

    /**
     * 'agentsSet' is nor a variable but a lambda.
     * The function is straightly assigned to the variable
     * to avoid the creation of a function, then a variable,
     * and then the assignment of the function to the variable
     */
    val agentsSet: () -> MutableSet<String?> = fun(): MutableSet<String?> {
        // val agentsSet: MutableSet<String?> = mutableSetOf()
        val agentsSet: MutableSet<String?> = mutableSetOf("Not assigned...") // Empty field for unassigned agent case
        fakeAgents.forEach { agentsSet.add(it.toString()) }
        return agentsSet
    }



}


/**
 * FAKE AGENTS
 */

val fakeAgents = listOf(
    Agent(
        0,
        "N0000000",
        "F0000000"
    ),
    Agent(
        1,
        "N1111111",
        "F1111111"
    ),
    Agent(
        2,
        "N2222222",
        "F2222222"
    )
)
