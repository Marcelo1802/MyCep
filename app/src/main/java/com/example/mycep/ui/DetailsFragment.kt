package com.example.mycep.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mycep.R
import com.example.mycep.databinding.FragmentDetailsBinding
import com.example.mycep.model.endereço

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnVoltar.setOnClickListener {
            findNavController().navigate(R.id.action_detailsFragment_to_navigation_home)
        }


        // Recuperando o objeto Usuario
        val usuario = arguments?.getSerializable("detalhes") as? endereço

        // Verificando e usando os dados do usuario
        usuario?.let {
            binding.tvCard.text = it.logradouro
            binding.tvCepCard.text = it.cep
            binding.tvName.text = it.nome
            binding.imgCard.setImageResource(it.img)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}