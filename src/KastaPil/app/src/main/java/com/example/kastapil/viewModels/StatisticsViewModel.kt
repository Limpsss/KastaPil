package com.example.kastapil.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kastapil.models.DataManager
import com.example.kastapil.models.DatabaseUserStructure
import com.example.kastapil.models.UserData
import kotlinx.coroutines.launch

class StatisticsViewModelFactory(private val userData: UserData?) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = StatisticsViewModel(userData) as T
}

class StatisticsViewModel(userData: UserData?) : ViewModel() {
    private val dataManager: DataManager?
    private var dbData: DatabaseUserStructure? = null

    init {
        if (userData != null) {
            dataManager = DataManager(userData)
            viewModelScope.launch {
                dbData = dataManager.getDatabaseData()
            }
        }
        else {
            dataManager = null
            dbData = null
        }
    }

    fun getDartsThrown(): Int? {
        return dbData?.dartsThrown
    }

    fun getMostHitNumber(): Pair<String, Int>? {
        val data = dbData?.hitMapCounter?.maxByOrNull { it.value }
        return data?.toPair()
    }

    fun getMostDoubleOut(): Pair<String, Int>? {
        val data = dbData?.doubleOutCounter?.maxByOrNull { it.value }
        return data?.toPair()
    }

    fun getMostSingleOut(): Pair<String, Int>? {
        val data = dbData?.singleOutCounter?.maxByOrNull { it.value }
        return data?.toPair()
    }
}