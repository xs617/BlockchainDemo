package com.example.webj3demo

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class MyGsonConvert : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return MyResponseBodyConverter()
    }
}

class MyResponseBodyConverter: Converter<ResponseBody, Any>{
    override fun convert(value: ResponseBody): Any? {
         return null
    }

}