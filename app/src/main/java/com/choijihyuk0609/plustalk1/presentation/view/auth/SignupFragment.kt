package com.choijihyuk0609.plustalk1.presentation.view.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.SignupRequest
import com.choijihyuk0609.plustalk1.data.model.SignupResponse
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import com.choijihyuk0609.plustalk1.presentation.viewmodel.SignupViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var viewModel : SignupViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignupViewModel :: class.java)

        val emailInput = view.findViewById<EditText>(R.id.frSignup_emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.frSignup_passwordInput)
        val signupButton = view.findViewById<Button>(R.id.frSignup_SignupButton)

        signupButton.setOnClickListener {
            viewModel.signup(emailInput.text.toString(), passwordInput.text.toString( ) )
        }

        viewModel.signupResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SignupFragment())
                    .addToBackStack(null)
                    .commit()
            }

        })
    }
}