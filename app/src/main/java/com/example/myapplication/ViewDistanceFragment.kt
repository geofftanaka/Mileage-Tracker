package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Item
import com.example.myapplication.databinding.FragmentViewDistanceBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.math.abs

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ViewDistanceFragment : Fragment(),
    DeleteWarningDialogFragment.DeleteWarningListener {

    private val THRESHOLD = 100
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)


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

        val deleteIcon = getDrawable(context!!, R.drawable.delete_icon)
        val distanceAdapter = DistanceListAdapter { item -> adapterOnClick(item) }
        val recyclerView: RecyclerView = view.findViewById(R.id.distance_recycler)
        recyclerView.adapter = distanceAdapter

        viewModel.getItems().observe(this.viewLifecycleOwner) {
            it?.let {
                distanceAdapter.submitList(it as MutableList<Item>)
            }
        }

        val swiper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val holder = viewHolder as DistanceListAdapter.DistanceViewHolder
                    val selectedItem = holder.getItem()
                    selectedItem?.let {
                        viewModel.deleteItemById(it.id)
                        distanceAdapter.notifyItemRemoved(holder.layoutPosition)
                        Snackbar.make(recyclerView, R.string.deleted, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo) {
                                viewModel.addNewItem(selectedItem.drivenDate, selectedItem.distance)
                                distanceAdapter.notifyDataSetChanged()
                            }.show()
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.bottom - itemView.top
                    val iconHeight = itemHeight - 20
                    val iconWidth = iconHeight - 10

                    // Draw the red background
                    val background = ColorDrawable(Color.RED)
                    background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                    background.draw(c)

                    // Calculate position of delete icon
                    val iconTop = itemView.top + (itemHeight - iconHeight) / 2
                    val iconMargin = (itemHeight - iconHeight) / 2
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = itemView.left + iconMargin + iconWidth
                    val iconBottom = iconTop + iconHeight

                    // Draw the delete icon
                    deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    deleteIcon?.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            })

        swiper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_view_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_nav -> loadSettings()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadSettings(): Boolean {
        findNavController().navigate(R.id.action_view_distance_nav_to_settings_nav)
        return true
    }

    private fun adapterOnClick(item: Item) {

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

//    private fun paintDrawCommand(drawCommand: DrawCommand, canvas: Canvas, dX: Float, viewItem: View) {
//        drawBackground(canvas, viewItem, dX, drawCommand.backgroundColor)
//        drawIcon(canvas, viewItem, dX, drawCommand.icon)
//    }
//
//    @JvmStatic
//    fun paintDrawCommandToStart(canvas: Canvas, viewItem: View, @DrawableRes iconResId: Int, dX: Float) {
//        val drawCommand = createDrawCommand(viewItem, dX, iconResId)
//        paintDrawCommand(drawCommand, canvas, dX, viewItem)
//    }

    private class DrawCommand internal constructor(
        internal val icon: Drawable,
        internal val backgroundColour: Int
    )

    private fun createDrawCommand(viewItem: View, dX: Float, iconResId: Int): DrawCommand {
        val context = viewItem.context
        var icon = getDrawable(context, iconResId)
        icon = DrawableCompat.wrap(icon!!).mutate()
        icon.colorFilter = PorterDuffColorFilter(
            getColor(context, R.color.white),
            PorterDuff.Mode.SRC_IN
        )
        val backgroundColor = getBackgroundColor(R.color.red, R.color.greyish_red, dX, viewItem)
        return DrawCommand(icon, backgroundColor)
    }

    private fun getBackgroundColor(
        firstColor: Int,
        secondColor: Int,
        dX: Float,
        viewItem: View
    ): Int {
        return when (willActionBeTriggered(dX, viewItem.width)) {
            true -> getColor(viewItem.context, firstColor)
            false -> getColor(viewItem.context, secondColor)
        }
    }

    private fun willActionBeTriggered(dX: Float, viewWidth: Int): Boolean {
        return abs(dX) >= viewWidth / THRESHOLD
    }
}