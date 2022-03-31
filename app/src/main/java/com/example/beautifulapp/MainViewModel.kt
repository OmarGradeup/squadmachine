package com.example.beautifulapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

private val squadList = listOf(
    "Nucleus", "Sigma", "Qubit", "Electrons", "Momentum", "Photon", "Quantum",
    "Delta"
)

    private val membersList = listOf(
        "Omar", "Prashant", "Sanchit", "Sanjeev", "Ankit", "Abhishek", "Faheem",
        "Ankit Raj", "Sakshi Pruthi", "Aditya Mathur", "Moghira", "Abhilash",
        "Gauri Advani", "Gunjit", "Nitin Bhatia",
        "Vaibhav", "Vatsal", "Yogesh", "Ricky", "Tushar"
    )

    private val teamMap = HashMap<String, String>()
    fun buildTeamMap(){
        teamMap.apply {
            this["Omar"] = "Sigma"
            this["Prashant"] = "Nucleus"
            this["Sanchit"] = "Qubit"
            this["Sanjeev"] = "Electrons"
            this["Ankit"] = "Sigma"
            this["Abhishek"] = "Photon"
            this["Faheem"] = "Photon"
            this["Ankit Raj"] = "Momentum"
            this["Sakshi Pruthi"] = "Qubit"
            this["Aditya Mathur"] = "Nucleus"
            this["Moghira"] = "Momentum"
            this["Abhilash"] = "Delta"
            this["Gauri Advani"] = "Quantum"
            this["Gunjit"] = "Nucleus"
            this["Nitin Bhatia"] = "Nucleus"
            this["Vaibhav"] = "Sigma"
            this["Yogesh"] = "Qubit"
            this["Ricky"] = "Nucleus"
            this["Tushar"] = "Sigma"
        }
    }

    fun getSquadName(): String {
        return squadList.random()
    }

    fun getMemberName(): String {
        return membersList.random()
    }

    fun checkGeneratedResult(squadName: String, memberName: String): Boolean {
        return teamMap[memberName].equals(squadName)
    }
}