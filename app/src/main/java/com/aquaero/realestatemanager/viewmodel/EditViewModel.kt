package com.aquaero.realestatemanager.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.aquaero.realestatemanager.ApplicationRoot
import com.aquaero.realestatemanager.repository.AgentRepository

class EditViewModel(
    private val agentRepository: AgentRepository
): ViewModel() {

    private val context: Context by lazy { ApplicationRoot.getContext() }

    val agentSet = agentRepository.agentsSet

}