package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.data.Item
import com.example.myapplication.databinding.FragmentAddDistanceBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AddDistanceFragment : Fragment() {

    private var _binding: FragmentAddDistanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: DistanceViewModel by activityViewModels {
        DistanceViewModelFactory(
            (activity?.application as DistanceApplication).database
                .itemDao()
        )
    }

    lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddDistanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDailyTotal()

        binding.addButton.setOnClickListener {
            addNewItem()
            updateDailyTotal()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> clearAllItems()
            R.id.settings_nav -> loadSettings()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(binding.mileageAmount.text.toString().toInt())
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewItem(binding.mileageAmount.text.toString().toInt())
            binding.mileageAmount.setText("")
            hideKeyboardFrom(context, binding.root)
        }
    }

    private fun clearAllItems(): Boolean {
        ClearWarningDialogFragment().show(childFragmentManager, "warningDialog")
        return false
    }

    private fun updateDailyTotal() {
        viewModel.getDailyTotal().observe(this.viewLifecycleOwner) { total ->
            binding.dailyTotalAmount.text = total.toString()
        }
        viewModel.getAnnualTotal().observe(this.viewLifecycleOwner) { total ->
            binding.annualTotalAmount.text = total.toString()
        }
    }

    private fun loadSettings(): Boolean {
        findNavController().navigate(R.id.action_add_distance_nav_to_settings_nav)
        return true
    }

    private fun hideKeyboardFrom(context: Context?, view: View) {
        context?.let {
            val imm: InputMethodManager =
                it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}