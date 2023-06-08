package com.example.fromdeskhelper.domain

import android.util.Log
import com.apollographql.apollo3.ApolloClient
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


class ComprobationUserUseCaseTest{
    @RelaxedMockK
    private var apolloclient: ApolloClient? = NetworkModule.providerGraphql();
    lateinit var comprobationUserUseCase:ComprobationUserUseCase
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        comprobationUserUseCase= ComprobationUserUseCase(apolloclient)
    }

    @Test
    fun `Comprueba token en APOLLO`()= runBlocking{
        //given
        coEvery {
            comprobationUserUseCase()
        } returns "Ni idea"
        //whe

        var primero = comprobationUserUseCase()
        Log.i("APollo",primero)
        //then
        coVerify(exactly = 1){
            comprobationUserUseCase()
        }
    }
}


