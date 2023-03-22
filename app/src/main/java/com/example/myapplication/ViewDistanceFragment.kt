package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Item
import com.example.myapplication.databinding.FragmentViewDistanceBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ViewDistanceFragment : Fragment() {

    private var _binding: FragmentViewDistanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: DistanceViewModel by activityViewModels {
        DistanceViewModelFactory(
            (activity?.application as DistanceApplication).database
                .itemDao()
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentViewDistanceBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val distanceAdapter = DistanceListAdapter { item -> adapterOnClick(item)}
        val recyclerView: RecyclerView = view.findViewById(R.id.distance_recycler)
        recyclerView.adapter = distanceAdapter

        viewModel.getItems().observe(this.viewLifecycleOwner) {
            it?.let {
                distanceAdapter.submitList(it as MutableList<Item>)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick(item: Item) {

    }
}