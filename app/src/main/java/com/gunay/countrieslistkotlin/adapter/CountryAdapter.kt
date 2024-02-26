package com.gunay.countrieslistkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gunay.countrieslistkotlin.databinding.ItemCountryBinding
import com.gunay.countrieslistkotlin.model.Country
import com.gunay.countrieslistkotlin.util.downloadFromUrl
import com.gunay.countrieslistkotlin.util.placeholderProgressBar
import com.gunay.countrieslistkotlin.view.FeedFragmentDirections

class CountryAdapter(val countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(val binding: ItemCountryBinding): RecyclerView.ViewHolder(binding.root) {

    }

    //item_country.xml ile adapter'un birbirine bağlanması işlemi burada yapılır. Bu işlemi yaparken yardımcı sınıf olarak CountryViewHolder sınıfı kullanılır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.name.text = countryList[position].countryName
        holder.binding.region.text = countryList[position].countryRegion
        holder.binding.imageView.downloadFromUrl(countryList[position].imageUrl, placeholderProgressBar(holder.binding.imageView.context))

        holder.binding.view.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
    }


    fun updateCountryList(newCountryList: List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}