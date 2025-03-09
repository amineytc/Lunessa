package com.amineaytac.lunessa.ui.auth.login

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.databinding.FragmentLoginBinding
import com.amineaytac.lunessa.util.safeNavigateWithArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.yagmurerdogan.toasticlib.Toastic
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = Firebase.auth

        // If the user is already logged in, direct them to the home page
        firebaseAuth.currentUser?.let {
            findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)
        }

        userInfo()
        fetchRemoteConfig()
        setupPasswordToggle()

        binding.tvSigninCreateacc.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_signupFragment)
        }
    }

    // Email validation regex check
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return emailRegex.matches(email)
    }

    private fun setupPasswordToggle() = with(binding) {

        var isPasswordVisible = true

        tvSigninPassword.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                etSigninPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                tvSigninPassword.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye_open)
            } else {
                etSigninPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                tvSigninPassword.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye)
            }
            etSigninPassword.setSelection(etSigninPassword.text?.length ?: 0)
        }
    }

    private fun userInfo() {
        with(binding) {
            btnSignin.setOnClickListener {
                val getEmail = etSigninMail.text.toString()
                val getPassword = etSigninPassword.text.toString()

                when {
                    getEmail.isEmpty() || getPassword.isEmpty() -> {
                        Toastic.toastic(
                            context = requireContext(),
                            message = getString(R.string.fill_inthe_blank),
                            duration = Toastic.LENGTH_SHORT,
                            type = Toastic.INFO,
                            isIconAnimated = true
                        ).show()
                    }

                    !isValidEmail(getEmail) -> {
                        Toastic.toastic(
                            context = requireContext(),
                            message = getString(R.string.invalid_email),
                            duration = Toastic.LENGTH_SHORT,
                            type = Toastic.INFO,
                            isIconAnimated = true
                        ).show()
                    }

                    getPassword.length < 6 -> {
                        Toastic.toastic(
                            context = requireContext(),
                            message = getString(R.string.must_six),
                            duration = Toastic.LENGTH_SHORT,
                            type = Toastic.INFO,
                            isIconAnimated = true
                        ).show()
                    }

                    else -> {
                        signIn(getEmail, getPassword)
                    }
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)
            Toastic.toastic(
                context = requireContext(), message = getString(R.string.signin_success),
                duration = Toastic.LENGTH_SHORT, type = Toastic.SUCCESS, isIconAnimated = true
            ).show()
        }.addOnFailureListener {
            Toastic.toastic(
                context = requireContext(), message = getString(R.string.please_signup),
                duration = Toastic.LENGTH_SHORT, type = Toastic.INFO, isIconAnimated = true
            ).show()
        }
    }

    private fun fetchRemoteConfig() {
        val config = Firebase.remoteConfig
        config.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val webClientId = config.getString("default_web_client_id")
                if (webClientId.isNotEmpty()) {
                    signInWithGoogle(webClientId)
                } else {
                    Toastic.toastic(
                        context = requireContext(),
                        message = getString(R.string.unable_google),
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.WARNING,
                        isIconAnimated = true
                    ).show()
                }
            } else {
                Toastic.toastic(
                    context = requireContext(), message = getString(R.string.unable_google),
                    duration = Toastic.LENGTH_SHORT, type = Toastic.WARNING, isIconAnimated = true
                ).show()
            }
        }
    }

    // Google Sign-In process
    private fun firebaseAuthWithGoogle(idToken: String?) {
        if (idToken == null) {
            Toastic.toastic(
                context = requireContext(), message = getString(R.string.unable_google),
                duration = Toastic.LENGTH_SHORT, type = Toastic.WARNING, isIconAnimated = true
            ).show()
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)
                } else {
                    Toastic.toastic(
                        context = requireContext(),
                        message = getString(R.string.unable_google),
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.WARNING,
                        isIconAnimated = true
                    ).show()
                }
            }
    }

    private val googleSignInActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Toastic.toastic(
                    context = requireContext(), message = getString(R.string.unable_google),
                    duration = Toastic.LENGTH_SHORT, type = Toastic.WARNING, isIconAnimated = true
                ).show()
            }
        }

    private fun signInWithGoogle(webClientId: String) = with(binding) {
        val googleSignInClient = GoogleSignIn.getClient(
            requireContext(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()
        )
        ivSigninGoogle.setOnClickListener {
            googleSignInActivityResultLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.currentUser?.let {
            if (findNavController().currentDestination?.label == "LoginFragment" && isAdded) {
                findNavController().safeNavigateWithArgs(
                    LoginFragmentDirections.actionLoginFragment2ToHomeFragment(),
                    null
                )
            }
        }
    }
}