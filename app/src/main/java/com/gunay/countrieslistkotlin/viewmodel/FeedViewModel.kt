package com.gunay.countrieslistkotlin.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gunay.countrieslistkotlin.model.Country
import com.gunay.countrieslistkotlin.service.CountryApiService
import com.gunay.countrieslistkotlin.service.CountryDao
import com.gunay.countrieslistkotlin.service.CountryDatabase
import com.gunay.countrieslistkotlin.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application): BaseViewModel(application) {

    private val countryApiService = CountryApiService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    //nanosaniye cinsinden 10 dk
    private var refleshTime = 10 * 60 * 1000* 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refleshData(){

        /*
        val country = Country("Turkey","Asia","Ankara","TRY","Turkish","www.ss.com")
        val country2 = Country("Singapure","Asia","Singapure","SGD","English","www.ss.com")
        val country3 = Country("New Zeland","New Zeland","Auckland","NZD","English","www.ss.com")

        val countryList = arrayListOf<Country>(country,country2,country3)
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false*/

        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refleshTime){
            getDataFromSQLite()
        }else{
            getDataFromAPI()
        }

    }

    fun refleshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromSQLite(){
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries from SQLite", Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI(){
        countryLoading.value = true

        disposable.add(countryApiService.getData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                override fun onSuccess(t: List<Country>) {

                    storeInSQLite(t)
                    Toast.makeText(getApplication(),"Countries from API", Toast.LENGTH_LONG).show()

                }

                override fun onError(e: Throwable) {
                    countryLoading.value = false
                    countryError.value = true
                    e.printStackTrace()
                }

            }))
    }

    private fun showCountries(t: List<Country>){
        countries.value = t
        countryError.value = false
        countryLoading.value = false
    }

    private fun storeInSQLite(t: List<Country>){
        launch {
            //arka plandaki operator fun invoke bu kısımda çalıştırılıyor CountryDatabase(getApplication()) ve sadece tek
            // bir database oluşturulup hep onu kullanmak (bidaha oluşturulmaması) ve sadece aynı anda
            //tek bir yerden erişimi sağlandıktan (baska yerlerden veya thread aynı anda erişemez senkronize) sonra database üzerinden dao ya
            // erişiyoruz (dao nun database'e bağlantısını CountryDatabase içinde abstract fun countryDao() : CountryDao
            //şeklinde yaptığımız için direk database sınıfının kendisi üzerinden erişebiliyoruz
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            //listLong primarykey uuid listlesidir
            //toTypedArray() listedeki verileri tek tek hale getirir zaten dao kısmında vararc kullandığımız için veri tek tek girilmeli
            val listLong = dao.insetAll(*t.toTypedArray()) // -> list -> individual
            var i = 0
            //uuid işlemini modele opsionel olarak yani body içerisine tanımlayıp constructer olarak almadığımız için uuid verileri internetten gelmiyor
            //yani her bir Country nesnesi interetten alındıktan sonra burada onlara uuid leri biz veriyoruz
            while (i < t.size){
                t[i].uuid = listLong[i].toInt()
                i += 1
            }

            showCountries(t)
        }

        customPreferences.saveTime(System.nanoTime())
    }


    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }


}














