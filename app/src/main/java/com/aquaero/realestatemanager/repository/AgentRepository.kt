package com.aquaero.realestatemanager.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.R
import com.aquaero.realestatemanager.model.Agent

class AgentRepository() {

    private val context: Context by lazy { ApplicationRoot.getContext() }


    fun agentFromId(agentId: Long): Agent? {
        fakeAgents.forEach {
            if (it.agentId == agentId) return it
        }
        return null
    }


    /**
     * 'agentsSet' is nor a variable but a lambda.
     * The function is straightly assigned to the variable
     * to avoid the creation of a function, then a variable,
     * and then the assignment of the function to the variable
     */
    val agentsSet: () -> MutableSet<String?> = fun(): MutableSet<String?> {
        // val agentsSet: MutableSet<String?> = mutableSetOf()
        val agentsSet: MutableSet<String?> = mutableSetOf(context.getString(R.string.not_assigned)) // Empty field for unassigned agent case
        fakeAgents.forEach { agentsSet.add(it.toString()) }
        return agentsSet
    }

    // ...




    //
    /**
     * FAKE AGENTS
     */

    val fakeAgents = listOf(
        Agent(
            agentId = 0,
            agentName = "N0000000",
            agentFirstName = "F0000000"
        ),
        Agent(
            agentId = 1,
            agentName = "N1111111",
            agentFirstName = "F1111111"
        ),
        Agent(
            agentId = 2,
            agentName = "N2222222",
            agentFirstName = "F2222222"
        )
    )
    //
}

