package com.example.fromdeskhelper.ui.View.fragment

//import com.example.cafeclientes.R
//XDDDD

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.fromdeskhelper.R
import com.example.fromdeskhelper.data.Privilegies
import com.example.fromdeskhelper.data.Providers
import com.example.fromdeskhelper.data.Result
import com.example.fromdeskhelper.data.model.Controller.ConnectionController
import com.example.fromdeskhelper.databinding.FragmentLoginBinding
import com.example.fromdeskhelper.ui.View.ViewModel.LOG_CLASS
import com.example.fromdeskhelper.ui.View.activity.LoginActivity
import com.example.fromdeskhelper.ui.View.activity.MainActivity
import com.example.fromdeskhelper.ui.login.LoggedInUserView
import com.example.fromdeskhelper.ui.View.ViewModel.LoginViewModel
import com.example.fromdeskhelper.util.MessageSnackBar
//import com.example.fromdeskhelper.ui.login.LoginViewModelFactory
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.Types

//private const val USER_PREFERENCES_NAME = "user_preferences"
val LOGINIT="LOGINFRAGMENT"
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    protected lateinit var baseActivity: LoginActivity
    protected lateinit var contextFragment: Context
    private val callbackManager = CallbackManager.Factory.create();

    private val GOOGLE_SIN_IN = 100
    private val loginViewModel: LoginViewModel by viewModels();

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LoginActivity) {
            this.baseActivity = context
        }
        this.contextFragment = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
//        loginViewModel()
        super.onCreate(savedInstanceState)
    }

    private fun blurbackground(view: BlurView?,float:Float) {
        val radius = float
        val decorView: View =  activity?.window!!.decorView
        val rootView = binding.containerGrant as ViewGroup
        val windowBackground = decorView.background
        view?.setupWith(rootView)
            ?.setFrameClearDrawable(windowBackground)
            ?.setBlurAlgorithm(RenderScriptBlur(context))
            ?.setBlurRadius(radius)
            ?.setBlurAutoUpdate(true)
            ?.setHasFixedTransformationMatrix(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
        activity?.setTitle("Inicia Session")

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loadingProgressBar = binding.loading
//        loadingProgressBar.visibility=View.INVISIBLE

        blurbackground(binding.fondBackground,1f)
        blurbackground(binding.fondEdit,0.5f)


//        binding.connectWithFbButton.setFragment(this)
        loginViewModel();
        loginViewModel.usuarioLoginProvider.observe(viewLifecycleOwner, Observer {
            MessageSnackBar(view = view, "Se registro Correctamente", Color.GREEN)
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            Animatoo.animateZoom(baseActivity);
            baseActivity.finish()
        })


        loginViewModel.usuarioLoginProviderError.observe(viewLifecycleOwner, Observer {
            try {
                it as Result.Error
                throw (it.exception as FirebaseAuthException)
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                MessageSnackBar(view = view, "Contraseña Incorrecta", Color.RED)
            } catch (e: FirebaseAuthInvalidUserException) {
                MessageSnackBar(view = view, "Usuario no encontrado, Registrese", Color.RED)
            } catch (e: FirebaseAuthException) {
                MessageSnackBar(view = view, it.toString(), Color.RED)
            }
        })

//        loginViewModel.registerSusessfull.observe(viewLifecycleOwner, Observer {
//            if(it){
//                MessageSnackBar(view = view,"Se registro Correctamente", Color.GREEN)
//            }else{
//                MessageSnackBar(view = view,"Ocurrio un Error", Color.RED)
//            }
//        })

        loginViewModel.erroremailregister.observe(viewLifecycleOwner, Observer {
            MessageSnackBar(view = view, "El email no existe Decea registrarlo?", Color.RED)
        })
        loginViewModel.errorDesconosido.observe(viewLifecycleOwner, Observer {
            MessageSnackBar(view = view, it.message.toString(), Color.RED)
        })
        binding.Bregister.setOnClickListener {
            MessageSnackBar(view = view, "Crea tu cuenta", Color.YELLOW)
            Navigation.findNavController(view).navigate(
                R.id.action_loginFragment_to_registerFragment,
                Bundle(),
                null
            )
        }
        val googleConf =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                .requestIdToken(getString(R.string.app_id))
                .build()
//        val googleCliente = GoogleSignIn.getClient(baseActivity, googleConf)
//        googleCliente.signOut()
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            MessageSnackBar(view,result.resultCode.toString(),Color.GREEN)
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
//                val data: Intent? = result.data
//                doSomeOperations()
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    MessageSnackBar(view,"Se inicio session GOOGLE",Color.WHITE)
                                    AppMainInitApp(
                                        it.result.user?.email.toString(),
                                        Providers.GOOGLE,
                                        it.result.user
                                    )
                                } else {
                                    MessageSnackBar(view,"No se completo",Color.RED)
                                }
                            }
                    }

                } catch (e: ApiException) {
                    throw  e
                    Log.e(LOGINIT,e.stackTrace.toString())
                    MessageSnackBar(view,"Ubo un error intentlo Nuevamente",Color.GRAY)
                }
            }else{
                MessageSnackBar(view,"Se cancelo",Color.GRAY)
            }
        }
        binding.BGoogle.setOnClickListener {
            val googleCliente = GoogleSignIn.getClient(baseActivity, googleConf)
            googleCliente.signOut()
            resultLauncher.launch(googleCliente.signInIntent)
//            startActivityForResult(googleCliente.signInIntent, GOOGLE_SIN_IN)
        }

        //Facebooock
//        binding.connectWithFbButton.setPermissions("email", "public_profile");
        binding.BFaceboock.setOnClickListener {

            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<com.facebook.login.LoginResult> {
                    override fun onCancel() {
                        MessageSnackBar(view,"Se Cancelo el Inicio",Color.CYAN)
                    }

                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token = it.accessToken

                            //Auth Provider Platafoorm @pwd
                            val credential = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        MessageSnackBar(view,"Se inicio con Facebook",Color.CYAN)
                                        AppMainInitApp(
                                            it.result.user?.email ?: "",
                                            Providers.FACEBOOK,
                                            it.result.user
                                        )
                                    } else {
                                        MessageSnackBar(view,"Ubo un error al iniciar",Color.RED)
                                    }
                                }
                        }
                    }

                    override fun onError(error: FacebookException?) {

                    }
                }
            )
        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                binding.loading.isEnabled = loginFormState.isDataValid
//                binding.Bregister.isEnabled = loginFormState.isDataValid

                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })


//        val authErrors = mapOf("ERROR_INVALID_CUSTOM_TOKEN" to R.string.error_login_custom_token,
//            "ERROR_CUSTOM_TOKEN_MISMATCH" to R.string.error_login_custom_token_mismatch,
//            "ERROR_INVALID_CREDENTIAL" to R.string.error_login_credential_malformed_or_expired,
//            "ERROR_INVALID_EMAIL" to R.string.error_login_invalid_email,
//            "ERROR_WRONG_PASSWORD" to R.string.error_login_wrong_password,
//            "ERROR_USER_MISMATCH" to R.string.error_login_user_mismatch,
//            "ERROR_REQUIRES_RECENT_LOGIN" to R.string.error_login_requires_recent_login,
//            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" to R.string.error_login_accounts_exits_with_different_credential,
//            "ERROR_EMAIL_ALREADY_IN_USE" to  R.string.error_login_email_already_in_use,
//            "ERROR_CREDENTIAL_ALREADY_IN_USE" to R.string.error_login_credential_already_in_use,
//            "ERROR_USER_DISABLED" to R.string.error_login_user_disabled,
//            "ERROR_USER_TOKEN_EXPIRED" to R.string.error_login_user_token_expired,
//            "ERROR_USER_NOT_FOUND" to R.string.error_login_user_not_found,
//            "ERROR_INVALID_USER_TOKEN" to R.string.error_login_invalid_user_token,
//            "ERROR_OPERATION_NOT_ALLOWED" to R.string.error_login_operation_not_allowed,
//            "ERROR_WEAK_PASSWORD" to R.string.error_login_password_is_weak)

        //Esto iniciaba session con el observer [DEPRECATED NOT MVVM]

//        loginViewModel.loginResult.observe(viewLifecycleOwner,
//            Observer { loginResult ->
//                loginResult ?: return@Observer
////                loadingProgressBar.visibility = View.INVISIBLE
//                loginResult.error?.let {
////                    Toast.makeText(context,"Ubo un error!",Toast.LENGTH_SHORT).show()
//                    showAlert("Ubo un error ")
//                    showLoginFailed(it)
//                }
//                loginResult.success?.let {
//
//                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
//                        binding.username.text.toString(),
//                        binding.password.text.toString()
//                    ).addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            Toast.makeText(
//                                context,
//                                "Se Inicio sin ningun problema!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                            AppMainInitApp(
//                                it.result.user?.email.toString(),
//                                Providers.BASIC,
//                                it.result.user
//                            )
//                        } else {
//                            try {
//                                throw (it.exception as FirebaseAuthException)
//                            } catch (e: FirebaseAuthInvalidCredentialsException) {
//                                showAlert("Contraseña Incorrecta!")
//                            } catch (e: FirebaseAuthInvalidUserException) {
//                                binding.TEerror.visibility = View.VISIBLE
//                            } catch (e: FirebaseAuthException) {
//                                showAlert(
//                                    "Ubo un error " + it.exception?.cause!!.hashCode().toString()
//                                )
//                            }
//                        }
//
//                    }
////                    updateUiWithUser(it)
//                }
//                loadingProgressBar.visibility = View.INVISIBLE
//            })

        //Esto REGISTRABA AL USUARIO EN LA MISMA INTERFAZ[DEPRECATED NOT MVVM, AND INTERFACE]

//        loginViewModel.registerResult.observe(viewLifecycleOwner, Observer { reguisterResult ->
//            reguisterResult ?: return@Observer
//            reguisterResult.error?.let {
//                showAlert("Ubo un error de registro")
////                Toast.makeText(context,"Ubo un error de reguistro",Toast.LENGTH_LONG).show()
//            }
//            reguisterResult.success?.let {
//
//                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
//                    binding.username.text.toString(),
//                    binding.password.text.toString()
//                ).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        Toast.makeText(
//                            context,
//                            "Se reguistro sin ningun Problema!",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        AppMainInitApp(
//                            it.result.user?.email.toString(),
//                            Providers.BASIC,
//                            it.result.user
//                        )
//
//                    } else {
//                        try {
//                            throw it.exception?.cause!!
//                        } catch (e: FirebaseAuthUserCollisionException) {
//                            showAlert("El email ya esta registrado!")
//                        } catch (e: Exception) {
//                            showAlert("Ubo un error de registro Intente Nuevamente")
//                        }
//
////                        Toast.makeText(context,"Ubo un error de reguistro Iniciar denuevo",Toast.LENGTH_SHORT).show()
//                    }
//
//                }
//
//            }
//
//            loadingProgressBar.visibility = View.INVISIBLE
//        })


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }


        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        binding.login.setOnClickListener {
            disableitems(false)
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
            loadingProgressBar.visibility = View.INVISIBLE
            disableitems(true)
        }

//        registerButton.setOnClickListener {
//            disableitems(false)
//            loadingProgressBar.visibility = View.VISIBLE
//            loginViewModel.register(
//                usernameEditText.text.toString(),
//                passwordEditText.text.toString()
//            )
//            disableitems(true)
//        }


    }

//    private fun showAlert(value: String) {
//        val builder = AlertDialog.Builder(baseActivity)
//        builder.setTitle("Error")
//        builder.setMessage(value)
//        builder.setPositiveButton("Aceptar", null)
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }

    fun AppMainInitApp(email: String, provedor: Providers, user: FirebaseUser? = null) {

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.initFirebaseToken()
            var initLoginActiviti: Intent = Intent(baseActivity, MainActivity::class.java)
            startActivity(initLoginActiviti)
            Animatoo.animateZoom(baseActivity);
            baseActivity.finish()
        }
        binding.loading.visibility = View.INVISIBLE
//        Toast.makeText(context, "${email}", Toast.LENGTH_SHORT).show()
    }
//        var dato: java.sql.Connection? = null;
//
//        Toast.makeText(baseActivity, "Conectando...", Toast.LENGTH_LONG).show()
//        val con = ConnectionController()


        //Inicio de session con GoogleFirebase
//        loginViewModel.

//        GlobalScope.launch(Dispatchers.IO) {
//
//
//            val join = launch { dato = con.Conn() }
//            join.join()
//            if (join.isCompleted) {
//                if (dato != null) {
//                    val consulta: CallableStatement =
//                        dato!!.prepareCall("{? = call dbo.InitComprobation(?)}")
//
//                }
//                if (user != null) {
//
//                }
//                if (dato != null && user != null) {
//                    val consulta: CallableStatement =
//                        dato!!.prepareCall("{? = call dbo.InitComprobation(?)}")
////                    consulta.setString(1,"@holacomoestas.comxxddd")
//
////                    val VerifiToken=FirebaseAuth.getInstance().currentUser
////                    VerifiToken?.getIdToken(true).addOnCompleteListener {
////                        OnCompleteListener<GetTokenResult>(){
////                            task ->  task.result.token
////                        }
////                    }
////                    println("CODIGO DE USUARIO!!")
////                    println(user?.uid)
////                    println(FirebaseAuth.getInstance().getAccessToken(true).result.token)
//
////                    val tonkentask = user?.getIdToken(false)
////                    Tasks.await(tonkentask)
////                    val finalTokenHash = tonkentask.result.token!!.split(".")[1]
////
//
//                    val finalToken = user.uid
//                    consulta.setString(2, finalToken)
//                    consulta.registerOutParameter(1, Types.BOOLEAN)
//                    consulta.execute()
//                    if (!consulta.getBoolean(1)) {
//                        val statement: PreparedStatement =
//                            dato!!.prepareStatement("INSERT INTO Users VALUES(?,?,?,?,?)")
//                        statement.setString(1, finalToken)
//                        statement.setDate(
//                            2,
//                            java.sql.Date.valueOf(java.time.LocalDate.now().toString())
//                        )
//                        statement.setNull(3, Types.NULL)
//                        statement.setInt(4, 1)
//                        statement.setString(5, "1234567")
//                        statement.executeUpdate()
//                    }
//
//                    baseActivity.runOnUiThread {
//                        Toast.makeText(baseActivity, "Comprobando...", Toast.LENGTH_LONG).show()
//                    }
//
//                    val newComprobation: CallableStatement =
//                        dato!!.prepareCall("{? = call dbo.InitComprobationAcces(?)}")
//                    newComprobation.setString(2, finalToken)
////                    println(tonkentask.result.token.toString().split("."))
////                    println(tonkentask.result.signInProvider)
////                    println(tonkentask.result.signInSecondFactor)
//                    newComprobation.registerOutParameter(1, Types.INTEGER)
//                    newComprobation.execute()
//                    val Privilegied = newComprobation.getInt(1)
//                    if (Privilegied == Privilegies.CLIENTE.user) {
//                        baseActivity.runOnUiThread {
//                            var nuevo: Intent = Intent(baseActivity, MainActivity::class.java)
//                            startActivity(nuevo)
//                            baseActivity.finish()
//                        }
//                    } else if (Privilegied == Privilegies.ADMINISTRADOR.user) {
//                        baseActivity.runOnUiThread {
//                            var nuevo: Intent = Intent(baseActivity, MainActivity::class.java)
//                            startActivity(nuevo)
//                            baseActivity.finish()
//                        }
//                    }
//
//                } else {
//
//                    Log.i("Carlos","Carlos")
//
//                }
//            }
//        }

//        startActivity(nuevo);




    fun disableitems(bool: Boolean) {
//        binding.BFaceboock.
        binding.BFaceboock.isEnabled = bool
        binding.BGoogle.isEnabled = bool
        binding.Bregister.isEnabled = bool
        binding.login.isEnabled = bool
        binding.username.isEnabled = bool
        binding.password.isEnabled = bool
    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(com.example.fromdeskhelper.R.string.welcome) + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GOOGLE_SIN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//
//                if (account != null) {
//                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//                    FirebaseAuth.getInstance().signInWithCredential(credential)
//                        .addOnCompleteListener {
//                            if (it.isSuccessful) {
//                                AppMainInitApp(
//                                    it.result.user?.email.toString(),
//                                    Providers.GOOGLE,
//                                    it.result.user
//                                )
//                            } else {
//                                Toast.makeText(
//                                    context,
//                                    "No se completo...",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                }
//
//            } catch (e: ApiException) {
//                Toast.makeText(
//                    context,
//                    "Ubo un error intentelo denuevo " + e.message.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        }
//
//    }
}