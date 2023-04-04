package com.example.myapplication


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class ClearWarningDialogFragment : DialogFragment() {

    private val viewModel: DistanceViewModel by activityViewModels {
        DistanceViewModelFactory(
            (activity?.application as DistanceApplication).database
                .itemDao()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.clear_verification)
                .setPositiveButton(R.string.yes,
                DialogInterface.OnClickListener { dialog, id ->
                    viewModel.clearAllItems()
                })
                .setNegativeButton(R.string.no,
                DialogInterface.OnClickListener { dialog, id ->
                    // TODO: enter stuff
                })

            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }

    private fun sendResult(code: Int) {
        val intent = Intent()
        intent.putExtra(getString(R.string.delete_warning_key), code)
        targetFragment
    }
}