package com.example.fromdeskhelper.domain

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.resolveVariables
import com.example.fromdeskhelper.CategoriasQuery
import com.example.fromdeskhelper.core.di.NetworkModule
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
import com.example.fromdeskhelper.ComprobateUserQuery.Data
import com.example.fromdeskhelper.test.CategoriasQuery_TestBuilder.Data
import io.mockk.every
import io.mockk.verify
import javax.inject.Inject
import android.util.Log

//La comprobacion de datos solamente sirve para los caoss de uso falseando el contenido del servidor
//SOlo comprueba los casos de uso de la aplicacion
var Logger = "TOKEN_FRIST"
class ComprobationUserUseCaseTest @Inject constructor(
    private val loginApollo: ComprobationUseCase,
){

    lateinit var comprobationUserUseCase:ComprobationUseCase
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
//        comprobationUserUseCase= ComprobationUserUseCase(apolloclient)
    }

    @Test
    fun `Comprobate token APOLLO`()= runBlocking{
        //given
        var resultado = loginApollo()
        Log.i(Logger,resultado.toString())
        //then
    }
}


