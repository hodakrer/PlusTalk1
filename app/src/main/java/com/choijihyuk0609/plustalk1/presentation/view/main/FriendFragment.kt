package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.Friend
import com.choijihyuk0609.plustalk1.data.model.FriendAdapter
import com.choijihyuk0609.plustalk1.data.model.OnRecyclerItemClickListener
import com.choijihyuk0609.plustalk1.databinding.FragmentFriendBinding
import com.choijihyuk0609.plustalk1.presentation.viewmodel.FriendViewModel

class FriendFragment : Fragment(), OnRecyclerItemClickListener {
    //implementing variables
    private lateinit var binding: FragmentFriendBinding
    private lateinit var viewModel: FriendViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendAdapter
    //private val datas = MutableList(10000) {index -> Friend(email = "$index", name = "", profileImageUrl = "") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_friend, container, false
        )


        // ViewModel을 Fragment에서 가져옴
        viewModel = ViewModelProvider(this).get(FriendViewModel::class.java)

        // Adapter 설정
        adapter = FriendAdapter(emptyList( ), requireContext( ), this)
        binding.frFriendRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.frFriendRecyclerView.adapter = adapter

        // ViewModel의 LiveData를 관찰
        viewModel.friends.observe(viewLifecycleOwner, Observer { friends ->
            if (friends.isNotEmpty()) {
                // RecyclerView 갱신
                adapter.submitList(friends) // ListAdapter의 submitList()로 리스트 갱신
            }
        })

        // SharedPreferences에서 이메일 가져오기
        val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            .getString("email", null) // 이메일이 null일 경우 추가 처리 필요
        if (email.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "이메일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        // ViewModel로 친구 목록 로드
        viewModel.loadFriends(email)

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
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, AddFriendFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    override fun onRecyclerItemClick(email: String) {
        // Handle the click event here
        Toast.makeText(requireContext(), "Clicked on email: $email", Toast.LENGTH_SHORT).show()
        // You can navigate to another screen or take further actions here
    }

}


/*
//키보드 포커스로 고민한 흔적
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
}*/

/*
//과거 프랜드 불러오는 함수
//Loading Current Friend's list
private fun loadFriendList() {
    Log.e("kkang", "Function loadFriendList called!")
    // Get stored email from SharedPreferences
    val email = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        .getString("email", null)

    RetrofitInstance.apiService.listFriend(FriendListRequest(email.toString()))
        .enqueue(object : Callback<FriendListResponse> {
            override fun onResponse(
                call: Call<FriendListResponse>,
                response: Response<FriendListResponse>
            ) {
                if (response.isSuccessful) {
                    datas.clear()
                    datas.addAll(
                        response.body()?.data ?: emptyList()
                    ) // Add all friends to the datas list
                    binding.frFriendRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.frFriendRecyclerView.adapter =
                        FriendAdapter(datas, requireContext(), this@FriendFragment)
                    binding.frFriendRecyclerView.adapter?.notifyDataSetChanged() // Update the RecyclerView
                } else {
                    Log.e("FriendList", "Response error: ${response.code()}")
                    Toast.makeText(
                        requireContext(),
                        "Failed to load friends.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<FriendListResponse>, t: Throwable) {
                Log.e("FriendList", "Network error: ${t.localizedMessage}")
                Toast.makeText(
                    requireContext(),
                    "Network error: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
}*/

