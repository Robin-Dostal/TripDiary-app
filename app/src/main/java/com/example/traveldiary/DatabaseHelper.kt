package com.example.traveldiary

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext


class DatabaseHelper {
    private val uri = "mongodb+srv://rdostal:c5kazWLyxS92lYYI@mycluster.un5t7.mongodb.net/?retryWrites=true&w=majority&appName=MyCluster"
    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase

    suspend fun initDatabase(){
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()

        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(uri))
            .serverApi(serverApi)
            .build()

        client = MongoClient.create(mongoClientSettings)
        database = client.getDatabase("diary")
    }

    suspend fun getCollectionNames(): List<String> = withContext(Dispatchers.IO) {
        database.listCollectionNames().toList()
    }

    fun close() {
        client.close()
    }
}