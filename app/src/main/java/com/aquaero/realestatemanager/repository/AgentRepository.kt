package com.aquaero.realestatemanager.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.model.Agent

class AgentRepository() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    /**
     * Temp data used as a cache for property creation ou update
     */

    private var agentValue by mutableStateOf("")


    fun agentFromId(agentId: Long): Agent? {
        fakeAgents.forEach {
            if (it.agentId == agentId) return it
        }
        return null
    }

    fun onAgentValueChanged(value: String) {
        agentValue = value

        Log.w("AgentRepository", "New value is: $value")
        Toast.makeText(context, "New value is: $value", Toast.LENGTH_SHORT).show()
    }


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

    // ...




    //
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
    //
}

