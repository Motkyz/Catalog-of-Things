package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.databinding.FragmentCreateNewTagBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.viewModels.NewTagViewModel
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject


class NewTagFragment : Fragment(R.layout.fragment_create_new_tag) {
    private val binding: FragmentCreateNewTagBinding by viewBinding(FragmentCreateNewTagBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: NewTagViewModel by viewModels{viewModelFactory}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBackArrowCreateNewTag.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveNewTag.setOnClickListener {
            viewModel.createTag(
                title = binding.tagTitle.text.toString()
            )

            findNavController().popBackStack()
        }

        binding.chooseColorForCreateNewTag.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())        				// Pass Activity Instance
                .setTitle("Выбор цвета")           	// Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(-1)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    viewModel.saveColor(color)
                }
                .show()
        }

        viewModel.color.observe(viewLifecycleOwner) {
            binding.chooseColorForCreateNewTag.iconTint = ColorStateList.valueOf(it ?: -1)
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}