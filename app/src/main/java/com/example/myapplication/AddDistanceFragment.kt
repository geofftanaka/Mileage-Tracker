package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            addNewItem()
            updateDailyTotal()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.display_items -> transitionToDisplayList()
            R.id.clear_all -> clearAllItems()
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
        }
    }

    private fun clearAllItems() : Boolean {
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

    private fun transitionToDisplayList() : Boolean {
        findNavController().navigate(R.id.action_add_distance_nav_to_view_distance_nav)
        return true
    }
}