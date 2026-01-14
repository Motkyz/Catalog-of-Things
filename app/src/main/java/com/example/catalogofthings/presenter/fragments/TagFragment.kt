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
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.FragmentTagBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.viewModels.TagViewModel
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject


class TagFragment : Fragment(R.layout.fragment_tag) {
    private val binding: FragmentTagBinding by viewBinding(FragmentTagBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: TagViewModel by viewModels{viewModelFactory}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagId = arguments?.getInt("id")
        viewModel.getTag(tagId ?: 0)

        binding.icBackArrowCreateNewTag.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveNewTag.setOnClickListener {
            if (viewModel.tag.value == null) {
                viewModel.createTag(
                    title = binding.tagTitle.text.toString()
                )
            } else {
                updateTag()
            }
            findNavController().popBackStack()
        }

        binding.chooseColorForCreateNewTag.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())        				// Pass Activity Instance
                .setTitle("Выбор цвета")           	// Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(viewModel.tag.value?.color ?: -1)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    viewModel.saveColor(color)
                }
                .show()
        }

        viewModel.color.observe(viewLifecycleOwner) {
            binding.chooseColorForCreateNewTag.iconTint = ColorStateList.valueOf(it ?: -1)
        }

        viewModel.tag.observe(viewLifecycleOwner) {
            if (it != null){
                updateUI(it)
            }
        }
    }

    private fun updateUI(tag : TagEntity){
        binding.tagTitle.setText(tag.title)
        binding.chooseColorForCreateNewTag.iconTint = ColorStateList.valueOf(tag.color)
        viewModel.saveColor(tag.color)
    }

    private fun updateTag(){
        val thisTag = viewModel.tag.value
        val newTag = TagEntity(
            title = binding.tagTitle.text.toString(),
            color = viewModel.color.value ?: -1
        )

        viewModel.updateTag(thisTag ?: newTag, newTag)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}