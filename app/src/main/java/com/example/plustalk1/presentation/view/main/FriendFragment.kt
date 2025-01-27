package com.example.plustalk1.presentation.view.main


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plustalk1.R
import com.example.plustalk1.data.model.Friend
import com.example.plustalk1.data.model.FriendAdapter
import com.example.plustalk1.data.model.FriendListRequest
import com.example.plustalk1.data.model.FriendSearchRequest
import com.example.plustalk1.data.model.FriendSearchResponse
import com.example.plustalk1.data.model.OnRecyclerItemClickListener
import com.example.plustalk1.databinding.FragmentFriendBinding
import com.example.plustalk1.network.NetworkInterface

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FriendFragment : Fragment(), OnRecyclerItemClickListener {
    //implementing variables
    private lateinit var searchEditText: EditText
    private var isSearchVisible = false // Track search bar visibility
    private var friendSearchQuery: String? = null
    private lateinit var binding: FragmentFriendBinding
    private var datas: MutableList<Friend> = mutableListOf( )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_friend, container, false
        )
        //Initialize FriendList


        binding.frFriendRecyclerView.layoutManager = LinearLayoutManager(requireContext( ) )
        binding.frFriendRecyclerView.adapter = FriendAdapter(datas, requireContext(),this)
        binding.frFriendRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext( ),
            LinearLayoutManager.VERTICAL)
        )

        loadFriendList()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //controlling actionbar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_fragment_friend_searching_friend, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_item_searching_friend -> {
                        val searchView = menuItem.actionView as? SearchView
                        searchView?.apply {
                            // Ensure SearchView is expanded and focused
                            setIconifiedByDefault(false)
                            isFocusable = true
                            isFocusableInTouchMode = true
                            requestFocus()

                            // Show the keyboard
                            post { showKeyboard(this) }

                            // Handle text input
                            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                                override fun onQueryTextChange(newText: String?): Boolean {
                                    friendSearchQuery = newText
                                    // You can optionally filter in real-time
                                    return true
                                }

                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    friendSearchQuery = query
                                    if (!friendSearchQuery.isNullOrEmpty()) {
                                        binding.frFriendRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                                        binding.frFriendRecyclerView.adapter = FriendAdapter(datas, requireContext(), this@FriendFragment)
                                        binding.frFriendRecyclerView.addItemDecoration(
                                            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
                                        )
                                        searchFriend(friendSearchQuery!!)
                                    }
                                    return true
                                }
                            })
                        } ?: run {
                            Log.e("FriendFragment", "SearchView is null")
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    //Implementing SoftKeyboard
    private fun showKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        loadFriendList()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        Log.e("kkang", "KEYBOARD IS HIDDEN!!!!")
        //regenerate original FriendList from SearchFriendList
        loadFriendList()

    }


    override fun onRecyclerItemClick(email: String) {
        // Handle the click event here
        Toast.makeText(requireContext(), "Clicked on email: $email", Toast.LENGTH_SHORT).show()
        // You can navigate to another screen or take further actions here
    }

    //Loading Current Friend's list
    private fun loadFriendList() {
        Log.e("kkang","Function loadFriendList called!")
        // Get stored email from SharedPreferences
        val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null)

        MainActivity.RetrofitInstance.apiService.listFriend(FriendListRequest(email.toString()))
            .enqueue(object : Callback<List<Friend>> {
                override fun onResponse(call: Call<List<Friend>>, response: Response<List<Friend>>) {
                    if (response.isSuccessful) {
                        val friendList = response.body() ?: emptyList()
                        datas.clear()
                        datas.addAll(friendList) // Add all friends to the datas list
                        binding.frFriendRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.frFriendRecyclerView.adapter = FriendAdapter(datas, requireContext(), this@FriendFragment)
                        binding.frFriendRecyclerView.adapter?.notifyDataSetChanged() // Update the RecyclerView
                    } else {
                        Log.e("FriendList", "Response error: ${response.code()}")
                        Toast.makeText(requireContext(), "Failed to load friends.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Friend>>, t: Throwable) {
                    Log.e("FriendList", "Network error: ${t.localizedMessage}")
                    Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun searchFriend(query: String) {
        val request = FriendSearchRequest(memberEmail = query)
        MainActivity.RetrofitInstance.apiService.searchFriend(request).enqueue(object :
            Callback<FriendSearchResponse> {
            override fun onResponse(
                call: Call<FriendSearchResponse>,
                response: Response<FriendSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val friendSearchResponse = response.body()
                    if (friendSearchResponse != null) {
                        datas.clear( )
                        var friend : Friend = Friend(email = friendSearchResponse.email.toString( ),
                            name = "",
                            profileImageUrl = "")
                        friend.email = friendSearchResponse.email
                        datas.add(friend)
                        binding.frFriendRecyclerView.layoutManager = LinearLayoutManager(requireContext( ) )
                        binding.frFriendRecyclerView.adapter = FriendAdapter(datas, requireContext(),this@FriendFragment)
                        binding.frFriendRecyclerView.addItemDecoration(
                            DividerItemDecoration(requireContext( ),
                            LinearLayoutManager.VERTICAL)
                        )

                    }
                } else {
                    Log.e("FriendSearch", "Response error: ${response.code()}")
                    Toast.makeText(requireContext(), "Search failed. Try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FriendSearchResponse>, t: Throwable) {
                // Handle network failure
                Log.e("FriendSearch", "Network error: ${t.localizedMessage}")
                Toast.makeText(
                    requireContext(),
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}