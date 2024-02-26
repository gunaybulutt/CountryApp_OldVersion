package com.gunay.countrieslistkotlin.service

import com.gunay.countrieslistkotlin.model.Country
import io.reactivex.Single
import retrofit2.http.GET

interface countryAPI {

    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
    //atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    //veri alıs sekli single yanı tek sefer alma metodu eger surekli almak isteseydik observable yapardik
    fun getCountries(): Single<List<Country>>


}