package com.aquaero.realestatemanager

import com.aquaero.realestatemanager.data.fakeAgents

//TODO HERE : Report Java methods from initial project converted to functions...

fun agentsToStrings(): MutableSet<String?> {
    // val agentsSet: MutableSet<String?> = mutableSetOf()
    val agentsSet: MutableSet<String?> = mutableSetOf("Not assigned...") // Empty field for unassigned agent case
    for (agent in fakeAgents) {
        agentsSet.add(agent.toString())
    }
    return agentsSet
}