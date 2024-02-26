package com.gunay.countrieslistkotlin.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gunay.countrieslistkotlin.model.Country

@Database (entities = arrayOf(Country::class), version = 1)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao() : CountryDao

    //Singleton

    //herhangibir değişken @Voletile olarak tanımlandığında diğer thread'lere'de görünür hale gelir
    //coroutine gibi farklı threadlerle vesaire kullanacağımız için @Volatile tanımlamalıyız
    companion object{

        @Volatile private var instance : CountryDatabase? = null
        //synchronized iki thread'den aynı anda instance oluşturmaya çalışır ise farklı zamanlarda buna izin verir
        //yani aynı anda iki instance oluşmasını engeller(aynı anda 2 thread ulaşamıyor)aynı anda sadece tek bir thread'de işlem yapılabilir)
        //lock --> synchronized İŞLEMİNİN KİTLENİP KİTLENMİYECEĞİNİ KONTROL EDEN DEĞİŞKEN
        //also --> bunu üzerine bunlarıda yap
        //insatnce eğer varsa bişey yapılmadan instance döner eğer yok ise makeDatabese fonksiyonu çağrılarak databese oluşturulur ve bunu instanceye eşitler ve
        // bu işlem senkronize yapılır :synchronized
        private val lock = Any()
        //buradaki = işareti döndürmek return yapmak anlamında
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: makeDatabese(context).also {
                instance = it
            }
        }

        //sayfa yan döndü kapandı gibi durumlarda context kaybolacağından database durdurulmasın diye uygulamanın contextti verilir :applicationContext
        private fun makeDatabese(context: Context) = Room.databaseBuilder(
            context.applicationContext,CountryDatabase::class.java,"countrydatabase"
        ).build()

    }



}

/*Normalde bir fonksiyonun geri dönüş değerini belirlemek için fun functionName(): ReturnType şeklinde tanımlama yaparız
ve fonksiyonun sonunda geri dönüş değerini return anahtar kelimesiyle döndürürüz. Ancak invoke fonksiyonu için durum biraz farklıdır.

invoke fonksiyonu, özel bir fonksiyondur ve sınıfın nesneleri doğrudan çağrılabilir hale getirilir. Bu nedenle,
invoke fonksiyonu için geri dönüş tipini belirtmek için normal fun tanımlamasını
kullanabiliriz. Ancak geri dönüş değerini belirlemek için return anahtar kelimesini kullanmamıza gerek
yoktur. = işareti ile tanımladığımız değer, otomatik olarak fonksiyonun geri dönüş değeri olarak kabul edilir.*/