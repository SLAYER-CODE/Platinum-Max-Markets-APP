package com.example.fromdeskhelper.domain.Client

import com.example.fromdeskhelper.BrandsQuery
import com.example.fromdeskhelper.ProductsPreviewOffertsQuery
import com.example.fromdeskhelper.ProductsPreviewQuery
import com.example.fromdeskhelper.SloganImagesGetQuery
import com.example.fromdeskhelper.data.Network.ServicesGraph
import javax.inject.Inject

class CallClientServerUseCase @Inject constructor(
    private val cliente: ServicesGraph
){
    suspend fun PreviewSloganUser(): SloganImagesGetQuery.Data?{
        return cliente.GetSloganRroles()?.data
    }

    suspend fun OffertsProduct(): ProductsPreviewOffertsQuery.Data?{
        return cliente.GetOffertsProducts()?.data
    }
    suspend fun BrandsProduct(): BrandsQuery.Data?{
        return cliente.GetBrands()?.data
    }
}
