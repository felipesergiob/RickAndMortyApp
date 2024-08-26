package com.example.rickandmortyapp.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.viewmodel.CharacterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LaunchScreenFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launch_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                characterViewModel.fetchAllCharacters()
            }

            characterViewModel.charactersList.observe(viewLifecycleOwner) { characters ->
                if (characters.isNotEmpty()) {
                    findNavController().navigate(R.id.action_launchScreenFragment_to_homeFragment)
                }
            }
        }
    }
}
