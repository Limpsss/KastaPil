package com.example.kastapil.models

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class DataManager(private val userData: UserData) {
    companion object {
        const val USERS_KEY = "users"
        const val USERNAME_KEY = "username"
        const val DARTS_THROWN_KEY = "dartsThrown"
        const val HIT_MAP_COUNTER_KEY = "hitMapCounter"
        const val SINGLE_OUT_COUNTER_KEY = "singleOutCounter"
        const val DOUBLE_OUT_COUNTER_KEY = "doubleOutCounter"
        const val DATAMANAGER_TAG = "DataManager"
    }

    private val mDocRef = FirebaseFirestore.getInstance().document("$USERS_KEY/${userData.userId}")

    suspend fun getDatabaseData(): DatabaseUserStructure? {
        return try {
            val documentSnapshot = mDocRef.get().await()
            documentSnapshot.toObject<DatabaseUserStructure>()
        } catch (e: Exception) {
            Log.d(DATAMANAGER_TAG, "Failed to retrieve userdata: $e")
            null
        }
    }
    fun saveGameData(dartsThrown: List<DartData>) {
        // Check if user exist in database
        mDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    // If user document does not exist
                    initializeDatabase()
                    saveData(dartsThrown)
                } else {
                    saveData(dartsThrown)
                }

            }
            .addOnFailureListener {e ->
                Log.d(DATAMANAGER_TAG, "Failed to retrieve userdata: $e")
            }
    }

    private fun saveData(dartsThrown: List<DartData>) {
        mDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val dbData = documentSnapshot.toObject<DatabaseUserStructure>()!!

                dartsThrown.forEach { dart ->
                    dbData.hitMapCounter[dart.score] =
                        dbData.hitMapCounter.getOrDefault(dart.score, 0) + 1
                    if (dart.isDoubleOut) {
                        dbData.doubleOutCounter[dart.score] = dbData.doubleOutCounter.getOrDefault(dart.score, 0) + 1
                    }
                    else if (dart.isSingleOut) {
                        dbData.singleOutCounter[dart.score] = dbData.singleOutCounter.getOrDefault(dart.score, 0) + 1
                    }
                }

                val dataToSave = mutableMapOf<String, Any>()
                dataToSave[USERNAME_KEY] = userData.username ?: ""
                dataToSave[DARTS_THROWN_KEY] = dbData.dartsThrown + dartsThrown.size
                dataToSave[HIT_MAP_COUNTER_KEY] = dbData.hitMapCounter
                dataToSave[SINGLE_OUT_COUNTER_KEY] = dbData.singleOutCounter
                dataToSave[DOUBLE_OUT_COUNTER_KEY] = dbData.doubleOutCounter

                mDocRef.set(dataToSave)
                    .addOnSuccessListener {
                        Log.d(DATAMANAGER_TAG, "Successfully uploaded to database")
                    }
                    .addOnFailureListener {e ->
                        Log.d(DATAMANAGER_TAG, "Upload to database failed: $e")
                    }
            }
    }

    private fun initializeDatabase() {
        val allDartScores = initEmptyDartCounter()
        val dataToInitialize = mutableMapOf<String, Any>()
        dataToInitialize[USERNAME_KEY] = userData.username ?: ""
        dataToInitialize[DARTS_THROWN_KEY] = 0
        dataToInitialize[HIT_MAP_COUNTER_KEY] = allDartScores
        dataToInitialize[SINGLE_OUT_COUNTER_KEY] = allDartScores
        dataToInitialize[DOUBLE_OUT_COUNTER_KEY] = allDartScores

        mDocRef.set(dataToInitialize)
            .addOnSuccessListener {
                Log.d(DATAMANAGER_TAG, "initialize worked!!")
            }
            .addOnFailureListener {e ->
                Log.d(DATAMANAGER_TAG,  "The initialization failed: $e")
            }
    }

    private fun initEmptyDartCounter(): Map<String, Int> {
        val allDartScores = mutableMapOf<String, Int>()
        for (i in 1..20) {
            allDartScores["S$i"] = 0
            allDartScores["D$i"] = 0
            allDartScores["T$i"] = 0
        }
        allDartScores["S25"] = 0
        allDartScores["D25"] = 0

        return allDartScores
    }
}