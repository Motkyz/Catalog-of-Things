package com.example.catalogofthings.presenter.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import coil3.load
import coil3.request.crossfade
import com.example.catalogofthings.R

class FullscreenImageDialog : DialogFragment() {

    companion object {
        private const val ARG_BYTES = "image_bytes"

        fun newInstance(bytes: ByteArray) = FullscreenImageDialog().apply {
            arguments = Bundle().apply {
                putByteArray(ARG_BYTES, bytes)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenTransparentDialog)    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
            isClickable = true
            isFocusable = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bytes = arguments?.getByteArray(ARG_BYTES) ?: return
        val imageView = view as ImageView

        imageView.load(bytes) {
            crossfade(200)
        }

        imageView.setOnClickListener { dismiss() }
    }
}