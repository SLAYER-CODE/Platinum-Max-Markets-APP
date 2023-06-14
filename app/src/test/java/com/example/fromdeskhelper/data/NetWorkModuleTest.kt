package com.example.fromdeskhelper.data

import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import javax.inject.Inject

var LOG_CLASS="Network Modulue Conection"

@HiltAndroidTest
class NetWorkModuleTest {
    @Inject
    lateinit var apollo: ApolloClient

}