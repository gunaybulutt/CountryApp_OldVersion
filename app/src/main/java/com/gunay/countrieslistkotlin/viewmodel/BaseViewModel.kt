package com.gunay.countrieslistkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//thread islemleri icin applicationContext vermek sorunları en aza indirir
// (yan cevirme - fragment kapanması gibi durumlarda lifecycle de applicaition context uygulama kapanana
// kadar kapanmaz o nedenle kullanmak onemli)
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    //feedViewModel da launch ederken calıştırılacağı thread'in ayarlanması once job yapılacak
    //job hangi thread'te olursa daha sonra main thread'e gececek deniyor burada
    override val coroutineContext: CoroutineContext
        //oncelikle verilen isi (job) yap sonra ise main thread'e dön anlamına geliyor
        get() = job + Dispatchers.Main


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

/*get() metodu, coroutineContext özelliğini özelleştirmek için kullanılır. coroutineContext özelliği, CoroutineScope arayüzünden gelir ve bu arayüz,
bir iş parçacığında çalışan işlemleri yönetmek için gereken coroutine bağlamını sağlar.

get() metodu, coroutineContext özelliğine değer atandığında çalıştırılır ve bu özelliğin dönmesi gereken
değeri belirler. Bu örnekte, job + Dispatchers.Main ifadesi, iki coroutine bağlamını (job ve Dispatchers.Main) birleştirir. job,
ViewModel için bir Job öğesidir ve Dispatchers.Main, ana iş parçacığında işlemleri yürütmek için kullanılan bir dispatcher'dır.

Sonuç olarak, get() metodu, ViewModel'in coroutine bağlamını yapılandırmak için kullanılır. Bu, ViewModel
içindeki işlemlerin hangi iş parçacığında çalışacağını belirlemeye yardımcı olur ve iş parçacıklarını yönetmeyi kolaylaştırır.*/






























