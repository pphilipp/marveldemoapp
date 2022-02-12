package com.poid.marveldemoapp.core.base

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract fun hideProgress()
    abstract fun showProgress()

    fun showError(error: Error) {
        showToast("error - ${error.message}")
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}