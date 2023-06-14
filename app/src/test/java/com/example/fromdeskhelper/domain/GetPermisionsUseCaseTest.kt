package com.example.fromdeskhelper.domain

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.ComprobateUserQuery
import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.Repositorys.ComprobateLoginRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
var LOG_CLASS:String="CASEUSECONNECT";
class GetPermisionsUseCaseTest{
    @RelaxedMockK
    private lateinit var loginRepositoy:ComprobateLoginRepository
    lateinit var getPermisionsUseCase:GetPermisionsUseCase
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getPermisionsUseCase= GetPermisionsUseCase(loginRepositoy,null)
    }

    @Test
    fun funcer(){
        var cliente = ApolloClient.Builder().serverUrl("http://192.168.0.17:2016/graphql").build()
        runBlocking {
            Log.i(LOG_CLASS,cliente.query(ComprobateUserQuery("")).execute().data.toString() )
        }
    }

    @Test
    fun `Comprueba token`()= runBlocking{
        //given
        var Logger = "TOKEN_TEST"
        coEvery { loginRepositoy.comprobate("New") } returns Privilegies.NULL
        //whe
        val resultado = getPermisionsUseCase("New")
        if(resultado==Privilegies.NULL) {
            Log.d(Logger,"Devolvio Null")
        }else{
            Log.d(Logger,"Se implanto una respuesta")
        }
        //then
        coVerify(exactly = 1){loginRepositoy.comprobate("New")}
    }

}