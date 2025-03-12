package com.amineaytac.lunessa.ui.auth.signup

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.databinding.FragmentSignupBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.yagmurerdogan.toasticlib.Toastic
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = Firebase.auth
        userInfo()
        fetchRemoteConfig()
        setupPasswordToggle()
    }

    private fun fetchRemoteConfig() {
        val config = Firebase.remoteConfig
        config.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val webClientId = config.getString("default_web_client_id")
                if (webClientId.isNotEmpty()) {
                    setupGoogleSignIn(webClientId)
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

    private fun setupGoogleSignIn(webClientId: String) = with(binding) {
        oneTapClient = Identity.getSignInClient(requireContext())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        ivSignupGoogle.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    googleSignInLauncher.launch(
                        IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    )
                }
                .addOnFailureListener {
                    Toastic.toastic(
                        context = requireContext(),
                        message = getString(R.string.failed_signin),
                        duration = Toastic.LENGTH_SHORT,
                        type = Toastic.WARNING,
                        isIconAnimated = true
                    ).show()
                }
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    firebaseAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                            }
                        }
                }
            }
        }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return emailRegex.matches(email)
    }

    private fun setupPasswordToggle() = with(binding) {

        setupToggleForPasswordField(tvSignupPassword, etSignupPassword)
        setupToggleForPasswordField(tvSignupConfirmPassword, etSignupConfirmPassword)
    }

    private fun setupToggleForPasswordField(
        textInputLayout: TextInputLayout,
        editText: TextInputEditText
    ) {
        var isPasswordVisible = true

        textInputLayout.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                textInputLayout.endIconDrawable =
                    ContextCompat.getDrawable(editText.context, R.drawable.ic_eye_open)
            } else {
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                textInputLayout.endIconDrawable =
                    ContextCompat.getDrawable(editText.context, R.drawable.ic_eye)
            }
            editText.setSelection(editText.text?.length ?: 0)
        }
    }

    private fun userInfo() = with(binding) {
        btnSignup.setOnClickListener {
            val getEmail = etSignupMail.text.toString()
            val getPassword = etSignupPassword.text.toString()
            val confirmPassword = etSignupConfirmPassword.text.toString()

            if (getEmail.isNotEmpty() && isValidEmail(getEmail) && getPassword.length >= 6 && getPassword == confirmPassword) {
                signUp(getEmail, getPassword)
            } else {
                val message = when {
                    getPassword != confirmPassword -> getString(R.string.mismatch_password)
                    getEmail.isNotEmpty() && isValidEmail(getEmail) && getPassword.length < 6 -> getString(
                        R.string.must_six
                    )

                    getEmail.isEmpty() && getPassword.isEmpty() -> getString(R.string.fill_inthe_blank)
                    else -> getString(R.string.missing_mailPass)
                }
                Toastic.toastic(
                    context = requireContext(), message = message, duration = Toastic.LENGTH_SHORT,
                    type = Toastic.INFO, isIconAnimated = true
                ).show()
            }
        }
    }

    private fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                Toastic.toastic(
                    context = requireContext(),
                    message = getString(R.string.signup_success),
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.SUCCESS,
                    isIconAnimated = true
                ).show()
            }
            .addOnFailureListener {
                Toastic.toastic(
                    context = requireContext(),
                    message = getString(R.string.signup_failed),
                    duration = Toastic.LENGTH_SHORT,
                    type = Toastic.WARNING,
                    isIconAnimated = true
                ).show()
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
        }
    }
}