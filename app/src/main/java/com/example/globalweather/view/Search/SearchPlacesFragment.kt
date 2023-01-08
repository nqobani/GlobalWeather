package com.example.globalweather.view.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.globalweather.databinding.FragmentSearchPlacesBinding

class SearchPlacesFragment : Fragment() {

    private lateinit var binding: FragmentSearchPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchPlacesBinding.inflate(layoutInflater)
        (binding.root as ComposeView).setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )
        binding.root.setContent {
            MaterialTheme {
                Text(text = "Hello Compose")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchPlacesFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}