package com.example.finalproductos.data.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiProvider  @Inject constructor(){
        var wifiModel:List<WifiModel> = emptyList();

}