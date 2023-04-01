package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.util.toEditable

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val preferencesName = "com.example.distancetracker.settings"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            activity?.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

        val initialCost = sharedPreferences?.getInt(getString(R.string.initial_cost_key), 0)
        binding.initialCostText.text = initialCost?.toEditable()

        val subsequentCost = sharedPreferences?.getInt(getString(R.string.subsequent_cost_key), 0)
        binding.subsequentCostText.text = subsequentCost?.toEditable()

        val breakpointAmount =
            sharedPreferences?.getInt(getString(R.string.initial_breakpoint_key), 0)
        binding.breakpointText.text = breakpointAmount?.toEditable()

        binding.initialCostText.doAfterTextChanged {
            if (!it.toString().isBlank()) {
                var prefEditor = sharedPreferences?.edit()
                prefEditor?.putInt(getString(R.string.initial_cost_key), it.toString().toInt())
                prefEditor?.commit()
            }
        }

        binding.subsequentCostText.doAfterTextChanged {
            if (!it.toString().isBlank()) {
                var prefEditor = sharedPreferences?.edit()
                prefEditor?.putInt(getString(R.string.subsequent_cost_key), it.toString().toInt())
                prefEditor?.commit()
            }
        }

        binding.breakpointText.doAfterTextChanged {
            if (!it.toString().isBlank()) {
                var prefEditor = sharedPreferences?.edit()
                prefEditor?.putInt(
                    getString(R.string.initial_breakpoint_key),
                    it.toString().toInt()
                )
                prefEditor?.commit()
            }
        }
    }


}