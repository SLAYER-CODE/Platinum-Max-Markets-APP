package com.example.fromdeskhelper.core.di

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
//import com.mongodb.client.MongoClient
//import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private val uri = "mongodb://tiopazhc:n2bc4cj5afk@192.168.0.17:27017"
    @Provides
    @Singleton
    fun MongoClientRetorner():MongoClient{
        val mongoclient =  MongoClientURI(uri)
        return MongoClient(mongoclient)
    }
    @Provides
    @Singleton
    fun MongoDadaBaseRetorned(Client:MongoClient):MongoDatabase{
        val mongoClient = Client.getDatabase("store")
        return mongoClient
    }
}