package com.gunay.countrieslistkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gunay.countrieslistkotlin.adapter.CountryAdapter
import com.gunay.countrieslistkotlin.databinding.FragmentFeedBinding
import com.gunay.countrieslistkotlin.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refleshData()



        binding.countryList.layoutManager = LinearLayoutManager(context)
        binding.countryList.adapter = countryAdapter


        binding.swipeRefleshLayout.setOnRefreshListener {
            binding.countryList.visibility = View.GONE
            binding.countryError.visibility = View.GONE
            binding.countryLoading.visibility = View.VISIBLE
            viewModel.refleshFromAPI()
            binding.swipeRefleshLayout.isRefreshing = false
        }
        observeLiveData()
    }

    //android bu durum için this yerine yeni bir metod getirdi (viewLifecycleOwner) --1 observe işlemi için
    private fun observeLiveData(){
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(it)
            }
        })

        //error varmı yokmu onu kontrol ediyoruz
        viewModel.countryError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.countryError.visibility = View.VISIBLE
                }else{
                    binding.countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryError.visibility = View.GONE
                    binding.countryList.visibility = View.GONE
                }else{
                    binding.countryLoading.visibility = View.GONE
                }
            }
        })
    }

}












