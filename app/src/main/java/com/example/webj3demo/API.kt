package com.example.webj3demo

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import java.math.BigInteger

public interface API {
    @GET("api/address/{address}/utxo")
    fun getAddressUtxo(@Path("address") address:String) : Call<List<UTXO>>

    @GET("api/address/{address}/txs")
    fun getAddressTransactions(@Path("address") address:String) : Call<List<MyTransaction>>

    @POST("api/tx")
    fun postTransaction(@Body body: RequestBody) : Call<Any>

    @GET("api/tx/{txid}/hex")
    fun getTransactionHex(@Path("txid") txid:String) : Call<String>

    @GET("api/v1/fees/recommended")
    fun getRecommendedFees() : Call<Fee>
}

data class UTXO(val txid: String, val vout: Int, val value: Long)

class Fee {
    val fastestFee: Long? = null
    val halfHourFee: Long? = null
    val hourFee: Long? = null
    val economyFee: Long? = null
    val minimumFee: Long? = null
}