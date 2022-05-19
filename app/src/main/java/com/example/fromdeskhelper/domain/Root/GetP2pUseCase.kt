package com.example.fromdeskhelper.domain

import com.example.fromdeskhelper.data.P2pRepository
import com.example.fromdeskhelper.data.model.WifiModel
import javax.inject.Inject


class GetP2pUseCase @Inject constructor(private val repository: P2pRepository) {
    //suspend operator fun invoke() : List<WifiModel>?{
    //    return repository.getAllP2p();
    //}
    suspend operator fun invoke() = repository.getAllP2p()
}