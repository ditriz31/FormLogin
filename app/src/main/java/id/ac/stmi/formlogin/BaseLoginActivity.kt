package id.ac.stmi.formlogin

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged

/**
 * Logika Form Login yang dipakai bersama oleh ketiga versi layout
 * (LinearLayout, RelativeLayout, ConstraintLayout). Semua layout memakai ID view yang sama.
 */
abstract class BaseLoginActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    private enum class FieldState { DEFAULT, VALID, ERROR }

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvPasswordTooltip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutRes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvPasswordTooltip = findViewById(R.id.tvPasswordTooltip)

        etEmail.doAfterTextChanged { validateEmail(showErrorWhenEmpty = false) }
        etPassword.doAfterTextChanged { validatePassword(showErrorWhenEmpty = false) }

        findViewById<Button>(R.id.btnLogin).setOnClickListener { onLoginClicked() }
        findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            showToast(getString(R.string.forgot_password_clicked))
        }
        findViewById<TextView>(R.id.tvSignUp).setOnClickListener {
            showToast(getString(R.string.sign_up_clicked))
        }
    }

    private fun onLoginClicked() {
        val emailValid = validateEmail(showErrorWhenEmpty = true)
        val passwordValid = validatePassword(showErrorWhenEmpty = true)
        if (emailValid && passwordValid) {
            showToast(getString(R.string.login_success, etEmail.text.toString().trim()))
        } else {
            showToast(getString(R.string.login_invalid))
        }
    }

    private fun validateEmail(showErrorWhenEmpty: Boolean): Boolean {
        val email = etEmail.text.toString().trim()
        val valid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val state = when {
            valid -> FieldState.VALID
            email.isEmpty() && !showErrorWhenEmpty -> FieldState.DEFAULT
            else -> FieldState.ERROR
        }
        applyState(etEmail, state)
        return valid
    }

    private fun validatePassword(showErrorWhenEmpty: Boolean): Boolean {
        val password = etPassword.text.toString()
        val valid = password.length >= MIN_PASSWORD_LENGTH
        val state = when {
            valid -> FieldState.VALID
            password.isEmpty() && !showErrorWhenEmpty -> FieldState.DEFAULT
            else -> FieldState.ERROR
        }
        applyState(etPassword, state)
        tvPasswordTooltip.visibility = if (state == FieldState.ERROR) View.VISIBLE else View.GONE
        return valid
    }

    /** Mengubah warna border dan ikon di kanan input sesuai status validasi. */
    private fun applyState(editText: EditText, state: FieldState) {
        @DrawableRes val background: Int
        @DrawableRes val icon: Int
        when (state) {
            FieldState.DEFAULT -> {
                background = R.drawable.bg_input_default
                icon = 0
            }
            FieldState.VALID -> {
                background = R.drawable.bg_input_valid
                icon = R.drawable.ic_check_circle
            }
            FieldState.ERROR -> {
                background = R.drawable.bg_input_error
                icon = R.drawable.ic_error
            }
        }
        editText.setBackgroundResource(background)
        editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
