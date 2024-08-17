package com.example.mycep.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycep.R
import com.example.mycep.databinding.FragmentHomeBinding
import com.example.mycep.model.SharedViewModel
import com.example.mycep.model.endereço


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: Adapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = Adapter(mutableListOf(), { endereco ->
            // Lógica ao clicar no card
            // Criando o Bundle para passar o objeto
            val bundle = Bundle().apply {
                putSerializable("detalhes", endereco)
            }

            // Passando o bundle para o próximo fragmento
            findNavController().navigate(R.id.action_navigation_home_to_detailsFragment, bundle)


        }, { endereco ->
            // Lógica de exclusão
            sharedViewModel.removeAddress(requireContext(), endereco)
        })

        binding.RcCard.adapter = adapter
        binding.RcCard.layoutManager = LinearLayoutManager(requireContext())

        // Observa as mudanças na lista de endereços no ViewModel
        sharedViewModel.enderecos.observe(viewLifecycleOwner, Observer { listaEnderecos ->
            listaEnderecos?.let {
                adapter.updateList(it)
            }
        })

        // Recupera os dados do SharedPreferences através do ViewModel
        sharedViewModel.getAddressesFromPreferences(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}