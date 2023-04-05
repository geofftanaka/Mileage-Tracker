package com.example.myapplication


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult

class DeleteWarningDialogFragment : DialogFragment() {
    internal lateinit var listener: DeleteWarningListener

    interface DeleteWarningListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = this.parentFragment as DeleteWarningListener
//            listener = context as DeleteWarningListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() + "must implement listener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_verification)
                .setPositiveButton(R.string.yes,
                DialogInterface.OnClickListener { dialog, id ->
                    listener.onDialogPositiveClick(this)
                })
                .setNegativeButton(R.string.no,
                DialogInterface.OnClickListener { dialog, id ->
                    listener.onDialogNegativeClick(this)
                })

            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }
}