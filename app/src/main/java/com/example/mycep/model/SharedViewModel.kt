package com.example.mycep.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycep.R
import org.json.JSONArray
import org.json.JSONObject

class SharedViewModel : ViewModel() {

    private val _enderecos = MutableLiveData<MutableList<endereço>>()
    val enderecos: LiveData<MutableList<endereço>> get() = _enderecos

    init {
        _enderecos.value = mutableListOf()
    }

    fun getAddressesFromPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val enderecoList = mutableListOf<endereço>()

        // lista de endereços em formato JSON
        val json = sharedPreferences.getString("enderecos", "[]")
        val addressesArray = json?.let { JSONArray(it) }

        // Verifique se addressesArray não é nulo antes de iterar
        addressesArray?.let {
            for (i in 0 until it.length()) {
                val jsonObject = it.getJSONObject(i)
                val logradouro = jsonObject.optString("logradouro")
                val cep = jsonObject.optString("cep")
                val nome = jsonObject.optString("nome")
                val imagem = jsonObject.optInt("imagem", -1)

                if (logradouro.isNotEmpty()  && cep.isNotEmpty() && nome.isNotEmpty() && imagem != -1) {
                    enderecoList.add(endereço(logradouro, cep, nome, imagem))
                }
            }
        }

        _enderecos.value = enderecoList
    }

    fun addAddress(context: Context, endereco: endereço) {
        val currentList = _enderecos.value ?: mutableListOf()
        currentList.add(endereco)
        _enderecos.value = currentList

        saveAddressesToPreferences(context)
    }

    fun removeAddress(context: Context, endereco: endereço) {
        val currentList = _enderecos.value?.toMutableList() ?: mutableListOf()
        currentList.remove(endereco)
        _enderecos.value = currentList

        saveAddressesToPreferences(context)
    }

    private fun saveAddressesToPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convertendo a lista para JSON
        val jsonArray = JSONArray()
        _enderecos.value?.forEach { endereco ->
            val jsonObject = JSONObject().apply {
                put("logradouro", endereco.logradouro)
                put("cep", endereco.cep)
                put("nome", endereco.nome)
                put("imagem", endereco.img)
            }
            jsonArray.put(jsonObject)
        }
        editor.putString("enderecos", jsonArray.toString())
        editor.apply()
    }
}
