package com.example.havadurumuapp

import android.app.DownloadManager
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var request= StringRequest(Request.Method.GET, "https://www.google.com",object :Response.Listener<String>{
        //    override fun onResponse(response: String?) {
        //        Toast.makeText(this@MainActivity,"CEVAP"+response,Toast.LENGTH_LONG).show()
        //    }

        //},object :Response.ErrorListener{
        //    override fun onErrorResponse(error: VolleyError?) {
        //

        //    }
    //})
     //   MySingleton.getInstance(this)?.addToRequestQueue(request)

        btnBul.setOnClickListener {
            var sehir=etSehir.text.toString()
            veriler(sehir)
        }

    }

    fun tarih() :String{
        var takvim =java.util.Calendar.getInstance().time
        var formatx=SimpleDateFormat("EEEE , MMMM yyyy",Locale("tr"))
        var tarih=formatx.format(takvim)
        return tarih
    }
    fun veriler(sehir: String){
        val url ="https://api.openweathermap.org/data/2.5/weather?q="+sehir+",tr&appid=b71e24dbe626e8de8c2f53839f85cf5d&lang=tr&units=metric"
        val havadurumuNEsne = JsonObjectRequest(Request.Method.GET,url,null,object :Response.Listener<JSONObject>{
            override fun onResponse(response: JSONObject?) {
                var main = response?.getJSONObject("main")
                var sicaklik=main?.getInt("temp")
                var sehirAd=response?.getString("name")
                var weather=response?.getJSONArray("weather")
                var aciklama = weather?.getJSONObject(0)?.getString("description")
                var icon =weather?.getJSONObject(0)?.getString("icon")
                if(icon?.last()=='d'){
                    rootLayout.background=getDrawable(R.drawable.bg)
                }else{
                    rootLayout.background=getDrawable(R.drawable.gece)

                }
                var resimdosya=resources.getIdentifier("icon_"+icon?.sonKarakterSil(),"drawable",packageName)
                tvSehir.text=sehirAd?.toUpperCase()
                tvAciklama.text=aciklama?.toUpperCase()
                tvSicaklik.text=sicaklik.toString()
                imgHava.setImageResource(resimdosya)
                tvTarih.text=tarih()
            }

        },object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {

            }

        })
        MySingleton.getInstance(this)?.addToRequestQueue(havadurumuNEsne)
    }
}

private fun String.sonKarakterSil():String{
    return this.substring(0,this.length-1)
}
