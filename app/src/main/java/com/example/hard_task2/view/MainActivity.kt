package com.example.hard_task2.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hard_task2.R
import com.example.hard_task2.databinding.ActivityMainBinding
import com.example.hard_task2.model.City
import com.example.hard_task2.model.Country
import com.example.hard_task2.util.LocaleHelper
import com.example.hard_task2.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    lateinit var mainViewModel: MainViewModel
    private lateinit var binding:ActivityMainBinding
    private var codeAdapter: ArrayAdapter<String>?= null
    private var countryList = mutableListOf<Country>()
    private var countryCodeList= mutableListOf<String>()
    private var countryNameList= mutableListOf<String>()
    private var countryListAdapter :ArrayAdapter<String>?=null
    private var cityList = mutableListOf<City>()
    private var cityListAdapter :ArrayAdapter<City>?=null
    var lng =""

    val countryObserver = Observer<List<Country>> { it ->

        if (it != null) {
            countryList.clear()
            countryNameList.clear()
            countryCodeList.clear()
            countryList.add(Country(-1,resources.getString(R.string.country),resources.getString(R.string.country),"","","",true,0))
            countryCodeList.add(resources.getString(R.string.code))
            countryNameList.add(resources.getString(R.string.country))
            countryList.addAll(it)
        }
        it?.forEach {
            countryCodeList.add(it.Code)
            if(lng == "en"){
                countryNameList.add(it.NameEn)
            }
            else{
                countryNameList.add(it.NameAr)
            }
            countryListAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countryNameList)
            codeAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countryCodeList)
            codeAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countryListAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.editCode.adapter = codeAdapter
            binding.editCountry.adapter = countryListAdapter
        }

    }

    val cityObserver = Observer<List<City>>{
        if (it != null) {
            cityList.clear()
            cityList.add(0,City(resources.getString(R.string.city),"-1"))
            cityList.addAll(it)

            cityListAdapter?.notifyDataSetChanged()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        lng = mainViewModel.getLanguage()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countryCodeList.add(resources.getString(R.string.code))
        countryNameList.add(resources.getString(R.string.country))
        cityList.add(0,City(resources.getString(R.string.city),"-1"))

        binding.editCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if(countryList[position].CountryId!= -1)
                    mainViewModel.getStateList(countryList[position].CountryId)
                else{
                    cityList.clear()
                    cityList.add(0,City(resources.getString(R.string.city),"-1"))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        mainViewModel.cityLiveData.observe(this, cityObserver)
        cityListAdapter = ArrayAdapter<City>(this, android.R.layout.simple_spinner_item,cityList)
        cityListAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editCity.adapter = cityListAdapter

        mainViewModel.countryLiveData.observe(this, countryObserver)
        mainViewModel.getCountryList()



        binding.btnChangeLanguage.setOnClickListener {
            changeLanguage()
        }
        binding.textTcMessage.setOnClickListener { showDialog() }

    }


    override fun onDestroy() {
        mainViewModel.countryLiveData.removeObserver(countryObserver)
        mainViewModel.cityLiveData.removeObserver(cityObserver)
        super.onDestroy()
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_webview, null, false)

        val webView = view.findViewById<WebView>(R.id.webView)
        webView.loadUrl("https://www.termsfeed.com/blog/sample-terms-and-conditions-template/")
        webView.webViewClient = MyWebViewClient()
        webView.settings.javaScriptEnabled = true
        builder.setView(view)
        builder.setPositiveButton(resources.getString(R.string.close)){ dialogInterface, i -> dialogInterface.dismiss() }

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.black))

    }

    fun changeLanguage(){
        if(lng=="en"){
            mainViewModel.setLanguage("ar")
            LocaleHelper.setLocale(this,"ar")

        }else{
            mainViewModel.setLanguage("en")
            LocaleHelper.setLocale(this,"en")
        }

        recreate()
    }


    override fun attachBaseContext(newBase: Context?) {
        val lng = newBase?.getSharedPreferences("preferences_task2", Context.MODE_PRIVATE)?.getString("lang", "en")?:"en"
        super.attachBaseContext(LocaleHelper.setLocale(newBase!!,lng))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val lng = mainViewModel.getLanguage()
        LocaleHelper.onAttach(this,lng)
        super.onConfigurationChanged(newConfig)
    }



}

private class MyWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
            view?.loadUrl(url)
        }
        return true
    }
}