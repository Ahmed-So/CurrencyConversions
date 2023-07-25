package com.example.currencyconversions.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.currencyconversions.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {

    fun Fragment.showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.observeLoadingState(loading: StateFlow<Boolean?>) {
        val loadingDialog = Dialog(requireContext())
        loadingDialog.setCancelable(false)
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setContentView(R.layout.loading_dialog)

        lifecycleScope.launch {
            loading.collect {
                if (it != null) {
                    if (it) {
                        if (!loadingDialog.isShowing)
                            loadingDialog.show()
                    } else {
                        loadingDialog.dismiss()
                    }
                }
            }
        }
    }
}