
package com.choijihyuk0609.plustalk1.presentation.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.activity.result.PickVisualMediaRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.AppDatabase
import com.choijihyuk0609.plustalk1.data.model.ImageAdapter
import com.choijihyuk0609.plustalk1.data.model.ImageItem
import com.choijihyuk0609.plustalk1.data.repository.ImageRepository
import com.choijihyuk0609.plustalk1.data.repository.ImageViewModelFactory
import com.choijihyuk0609.plustalk1.presentation.viewmodel.ImageViewModel
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.core.view.MenuProvider

class ImageFragment : Fragment(R.layout.fragment_image) {

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageViewModel: ImageViewModel

    // Register the image picker launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                Log.d("ImageFragment", "Image URI: $it")
                Toast.makeText(requireContext(), "Image Selected: $it", Toast.LENGTH_SHORT).show()

                val imageItem = ImageItem(tag = "Image ${imageAdapter.itemCount + 1}", imgUri = it)

                // Save the image URI into the Room database
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        imageViewModel.insertImage(imageItem)
                        Log.d("ImageFragment", "Data successfully saved to Room: $imageItem")
                    } catch (e: Exception) {
                        Log.e("ImageFragment", "Error saving data to Room: ${e.message}")
                    }
                }
            }
        }

    // Permission launcher for requesting READ_EXTERNAL_STORAGE or READ_MEDIA_IMAGES
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with image picking
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                // Permission denied, show a message
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel and other components
        val database = AppDatabase.getDatabase(requireContext())
        val imageItemDao = database.imageItemDao()
        val imageRepository = ImageRepository(imageItemDao)
        imageViewModel = ViewModelProvider(
            this, ImageViewModelFactory(requireActivity().application, imageRepository)
        ).get(ImageViewModel::class.java)

        // Observe LiveData for image updates
        imageViewModel.images.observe(viewLifecycleOwner) { images ->
            Log.d("ImageFragment", "Images loaded from Room: $images")
            imageAdapter.updateImages(images)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.imageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        imageAdapter = ImageAdapter(
            images = emptyList(),
            onItemClick = { imageItem ->
                Toast.makeText(requireContext(), "Clicked: ${imageItem.tag}", Toast.LENGTH_SHORT).show()
            },
            imageViewModel = imageViewModel,
            requireContext()
        )
        recyclerView.adapter = imageAdapter

        // Set up the menu
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fragment_image_adding_image, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_item_adding_image -> {
                        checkAndRequestPermission()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    // Check and request the required permission
    private fun checkAndRequestPermission() {
        // Check for permissions based on SDK version
        when {
            // For Android 13 and above (READ_MEDIA_IMAGES)
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted, proceed with image picking
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            // For older Android versions (READ_EXTERNAL_STORAGE)
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted, proceed with image picking
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            else -> {
                // Request permission if not granted
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}

//퍼미션 관련 왜 로컬 이미지 접근이 안되었었나 체크
/*package com.example.plustalk1.presentation.view.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.activity.result.PickVisualMediaRequest
import com.example.plustalk1.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plustalk1.data.model.AppDatabase
import com.example.plustalk1.data.model.ImageAdapter
import com.example.plustalk1.data.model.ImageItem
import com.example.plustalk1.data.repository.ImageRepository
import com.example.plustalk1.data.repository.ImageViewModelFactory
import com.example.plustalk1.presentation.viewmodel.ImageViewModel
import kotlinx.coroutines.launch

class ImageFragment : Fragment(R.layout.fragment_image) {

    private val imageList = mutableListOf<ImageItem>() // List to store ImageItem objects
    private lateinit var imageAdapter: ImageAdapter // Adapter to bind data to RecyclerView
    private lateinit var imageViewModel: ImageViewModel

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                Log.d("ImageFragment", "Image URI: $it")
                Toast.makeText(requireContext(), "Image Selected: $it", Toast.LENGTH_SHORT).show()

                val imageItem = ImageItem(tag = "Image ${imageList.size + 1}", imgUri = it)

                // Save the image URI into the Room database
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        imageViewModel.insertImage(imageItem) // Use ViewModel to insert image
                        Log.d("ImageFragment", "Data successfully saved to Room: $imageItem")
                    } catch (e: Exception) {
                        Log.e("ImageFragment", "Error saving data to Room: ${e.message}")
                    }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = AppDatabase.getDatabase(requireContext())
        val imageItemDao = database.imageItemDao()

        val imageRepository = ImageRepository(imageItemDao)

        imageViewModel = ViewModelProvider(this, ImageViewModelFactory(requireActivity().application, imageRepository)).get(ImageViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.imageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty list
        imageAdapter = ImageAdapter(
            images = imageList, // Pass the empty list initially
            onItemClick = { imageItem ->
                Toast.makeText(requireContext(), "Clicked: ${imageItem.tag}", Toast.LENGTH_SHORT).show()
            },
            imageViewModel = imageViewModel,
            requireContext()
        )
        recyclerView.adapter = imageAdapter

        // Observe the LiveData for images and update the list when data is available
        imageViewModel.images.observe(viewLifecycleOwner) { images ->
            Log.d("ImageFragment", "Images loaded from Room: $images") // Add this log
            imageList.clear()
            imageList.addAll(images)
            imageAdapter.notifyDataSetChanged()
        }

        // Register the menu options
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fragment_image_adding_image, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_item_adding_image -> {
                        pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}*/

