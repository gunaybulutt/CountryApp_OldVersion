package com.gunay.countrieslistkotlin.service


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gunay.countrieslistkotlin.model.Country

//veri tabanına erişirken kullanacağımız metodları ayarladığımız kısım
@Dao
interface CountryDao {

    //Dao - data access object - veri tabanına ulaşmak istenilen metodlar yazılır

    @Insert
    suspend fun  insetAll(vararg countries: Country) : List<Long>

    //Insert -> Insert Into
    //suspend -> coroutine, reuse & resume
    //vararg -> (multiple country object) farklı sayılardaki , sayısı tam belli olmayan zamanlarda bir tekil objeyi farklı sayılarla verebilmek için gerekli olan keyword- bir listle veriyoruz ama cuntry objesini
    // tek tek vererek veriyoruz ve bunun sonucundada mesela 15 tane country objesini teker teker verdik bize bir listle dödürür ve bu da long listesidir List<Long> bu liste uuid leri döndürür
    //List<Long> primary keys

    @Query("SELECT * FROM Country")
    suspend fun getAllCountries() : List<Country>

    // buradaki (:countryId) suspend fun içinde verilen countryId ile bağlantı
    //countryId'ye eşit olan verileri çekmek için kullanılır
    @Query("SELECT * FROM Country WHERE uuid = :countryId")
    suspend fun  getCountry(countryId : Int) : Country

    @Query("DELETE FROM Country")
    suspend fun deleteAllCountries()
}