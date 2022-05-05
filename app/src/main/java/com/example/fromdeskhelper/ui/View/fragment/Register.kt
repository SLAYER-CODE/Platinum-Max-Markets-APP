package com.example.fromdeskhelper.ui.View.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fromdeskhelper.databinding.FragmentRegisterBinding
import com.example.fromdeskhelper.ui.View.ViewModel.LoginViewModel
import com.example.fromdeskhelper.ui.View.activity.LoginActivity
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.util.MessageSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class Register : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val loginViewModel: LoginViewModel by viewModels();

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    protected lateinit var baseActivity: LoginActivity
    protected lateinit var contextFragment: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LoginActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val usernameEditText = binding.ETusername
        val passwordEditText = binding.ETpassword
        val passwordRepetEditText = binding.ETpasswordRepet

        binding.Bregister.setOnClickListener {
            loginViewModel.register(binding.ETusername.text.toString(),binding.ETpassword.text.toString())
        }
        binding.Bregister.setOnClickListener {
            binding.PBloading.visibility=View.VISIBLE;
            loginViewModel.register(usernameEditText.text.toString(),passwordEditText.text.toString())
            binding.PBloading.visibility=View.INVISIBLE;
        }
        loginViewModel.registerSusessfull.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it){
                MessageSnackBar(view,"Se registro correctamente!",Color.GREEN)
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }else{
                passwordEditText.setText("")
                passwordEditText.requestFocus()
            }
        })

        loginViewModel.erroremailregister.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            MessageSnackBar(view,"Email Ya registrado",Color.GREEN)
            usernameEditText.requestFocus()
        })
        loginViewModel.errorDesconosido.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            MessageSnackBar(view,it.message.toString(),Color.GREEN)
        })



        loginViewModel.registerFormState.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
//                binding.loading.isEnabled = loginFormState.isDataValid
//                binding.Bregister.isEnabled = loginFormState.isDataValid

                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                    passwordRepetEditText.error=getString(it)
                }
                loginFormState.repetError?.let {
                    passwordRepetEditText.error=getString(it)
                }
            })




        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChangedRegister(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString(),
                    passwordRepetEditText.text.toString()
                )
            }
        }
        passwordRepetEditText.addTextChangedListener(afterTextChangedListener)
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

//        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                loginViewModel.login(
//                    usernameEditText.text.toString(),
//                    passwordEditText.text.toString()
//                )
//            }
//            false
//        }
//


        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}