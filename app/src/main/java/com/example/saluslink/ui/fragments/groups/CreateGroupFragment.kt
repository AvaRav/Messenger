package com.example.saluslink.ui.fragments.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.saluslink.R
import com.example.saluslink.databinding.FragmentCreateGroupBinding
import com.example.saluslink.models.Group
import com.example.saluslink.utilits.replaceFragment
import com.example.saluslink.utilits.showToast
import com.example.saluslink.viewModels.CreateGroupViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class CreateGroupFragment : Fragment() {
    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateGroupViewModel by viewModels()

    private var gUri: Uri = Uri.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
    }

    // Установка визуальных компонентов
    private fun setupViews() {
        binding.createGroupButton.text = getString(R.string.create_group)
        binding.titleGroup.requestFocus()
    }

    // Установка слушателей событий
    private fun setupListeners() {
        binding.createGroupImage.setOnClickListener { addImage() }
        binding.createGroupButton.setOnClickListener { createGroup() }
    }

    // Обработка результата выбора изображения
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
            resultCode == Activity.RESULT_OK && data != null
        ) {
            gUri = CropImage.getActivityResult(data).uri
            binding.createGroupImage.setImageURI(gUri)
        }
    }

    // Добавление изображения группы
    private fun addImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(200, 200)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(requireActivity(), this)
    }

    // Создание группы
    private fun createGroup() {
        val titleGroup = binding.titleGroup.text.toString()
        val directionGroup = binding.directionGroup.text.toString()
        val themesGroup = binding.themesGroup.text.toString()
        val aboutTheTopicGroup = binding.aboutTheTopicGroup.text.toString()

        // Проверка обязательных полей
        if (titleGroup.isEmpty() || directionGroup.isEmpty() || themesGroup.isEmpty()) {
            showToast("Введите данные для создания группы. Обязательные: название, направление, тема!")
        } else {
            // Создание экземпляра группы
            val group = Group(title = titleGroup, direction = directionGroup, themes = themesGroup, aboutTheTopic = aboutTheTopicGroup)
            viewModel.createGroup(group, gUri)
        }

        // Обработка события успешного создания группы
        viewModel.groupCreated.observe(viewLifecycleOwner) { groupCreated ->
            if (groupCreated) {
                replaceFragment(GroupsFragment(), false)
            }
        }
    }

    // Очистка привязки ViewBinding при уничтожении фрагмента
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
