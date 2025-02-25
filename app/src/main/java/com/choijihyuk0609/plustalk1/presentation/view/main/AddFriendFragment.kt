package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.Friend
import com.choijihyuk0609.plustalk1.data.model.FriendAdapter
import com.choijihyuk0609.plustalk1.data.model.FriendSearchRequest
import com.choijihyuk0609.plustalk1.data.model.FriendSearchResponse
import com.choijihyuk0609.plustalk1.data.model.OnRecyclerItemClickListener
import com.choijihyuk0609.plustalk1.databinding.FragmentAddFriendBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendFragment : Fragment(), OnRecyclerItemClickListener {
    //implementing variables
    private lateinit var searchEditText: EditText
    private var friendSearchQuery: String? = null
    private lateinit var binding: FragmentAddFriendBinding
    private var datas: MutableList<Friend> = mutableListOf( )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_friend, container, false
        )
        Log.d("kkang", "In AddFriendFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init adapter for recycler
        val adapter = FriendAdapter(datas, requireContext(), object : OnRecyclerItemClickListener {
            override fun onRecyclerItemClick(email: String) {
                Log.d("kkang", "Clicked on: $email")
            }
        })
        binding.frAddfriendRecyclerView.adapter = adapter
        binding.frAddfriendRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        //controlling actionbar
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.menu_fragment_friend_searching_friend, menu)

                val searchItem = menu.findItem(R.id.action_item_searching_friend)
                val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

                searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        Log.d("kkang", "${p0}")
                        if (p0 != null){
                            searchFriend(p0)
                        } else {
                            Toast.makeText(requireContext(), "Please fill in the blank", Toast.LENGTH_SHORT).show()
                        }
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        Log.d("kkang", "Text Changed: $p0") // 구현 추가
                        return true
                    }
                })

                Log.d("kkang", "menu prepared")
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_item_searching_friend -> {
                        Log.d("kkang", "CLICKED!")
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
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        Log.e("kkang", "KEYBOARD IS HIDDEN!!!!")
        //regenerate original FriendList from SearchFriendList
    }

    override fun onRecyclerItemClick(email: String) {
        // Handle the click event here
        Toast.makeText(requireContext(), "Clicked on email: $email", Toast.LENGTH_SHORT).show()
        // You can navigate to another screen or take further actions here
    }

    private fun searchFriend(query: String) {
        val request = FriendSearchRequest(memberEmail = query)
        MainActivity.RetrofitInstance.apiService.searchFriend(request).enqueue(object :
            Callback<FriendSearchResponse> {
            override fun onResponse(
                call: Call<FriendSearchResponse>,
                response: Response<FriendSearchResponse>
            ) {
                Log.e("kkang", "requestCode ${response.body( )?.status}")
                Log.e("kkang", "requestCode ${response.body( )?.data?.email}")
                if (response.isSuccessful) {
                    val friendSearchResponse = response.body()
                    Log.d("kkang", "Response Body: $friendSearchResponse")

                    if (friendSearchResponse != null) {
                        val friendData = friendSearchResponse.data
                        Log.d("kkang", "friendData: $friendData")

                        if (friendData != null) {
                            val friend = Friend(
                                email = friendData.email ?: "", // ✅ null 방지
                                name = friendData.name ?: "",
                                profileImageUrl = friendData.profileImageUrl ?: ""
                            )
                            datas.clear()
                            datas.add(friend)
                            Log.d("kkang", "$datas")
                            binding.frAddfriendRecyclerView.adapter?.notifyDataSetChanged()
                        } else {
                            Log.e("kkang", "friendSearchResponse.data is null")
                            Log.e("kkang", "requestCode ${friendSearchResponse.status}")
                        }
                    } else {
                        Log.e("kkang", "response.body() is null")
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