package com.example.mycep.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mycep.R
import com.example.mycep.model.AddressResponse
import com.example.mycep.data.RetrofitClient
import com.example.mycep.databinding.FragmentAddressBinding
import com.example.mycep.model.SharedViewModel
import com.example.mycep.model.endereço
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressFragment : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    private fun initClick() {
        binding.btnBuscar.setOnClickListener {
            val cep = binding.txtCep.text.toString()

            if (cep.isNotEmpty()) {
                searchCep(cep)
            } else {
                Toast.makeText(requireContext(), "Por favor, insira um CEP.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveAddress(addressResponse: AddressResponse) {
        binding.btnSalvar.setOnClickListener {
            val nome = binding.nameCardd.text.toString() // Nome vindo do fragmento
            val imagemResId =
                R.drawable.ic_favorito // Imagem fixa ou você pode mudar conforme necessário

            val endereco = endereço(
                logradouro = addressResponse.logradouro,
                cep = addressResponse.cep,
                nome = nome,
                img = imagemResId
            )

            sharedViewModel.addAddress(requireContext(), endereco)
            Toast.makeText(requireContext(), "Dados salvos com sucesso.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchCep(cep: String) {
        RetrofitClient.instance.getAddressByCep(cep).enqueue(object : Callback<AddressResponse> {
            override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {
                if (response.isSuccessful) {
                    val address = response.body()
                    if (address != null) {
                        val addressText = """
                            CEP: ${address.cep}
                            Logradouro: ${address.logradouro}
                            Complemento: ${address.complemento}
                            Bairro: ${address.bairro}
                            Localidade: ${address.localidade}
                            UF: ${address.uf}
                            DDD: ${address.ddd}
                        """.trimIndent()
                        binding.txtEnd.text = addressText

                        // Passar o addressResponse para o método de salvar
                        saveAddress(address)
                    }
                } else {
                    Toast.makeText(requireContext(), "Erro ao buscar o CEP.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("ViaCep", t.message ?: "Erro desconhecido")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
