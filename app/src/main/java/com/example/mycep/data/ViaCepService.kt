package com.example.mycep.data

import com.example.mycep.model.AddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    fun getAddressByCep(@Path("cep") cep: String): Call<AddressResponse>
}