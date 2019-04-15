package com.elegion.myfirstapplication

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AuthFragment : Fragment() {

    private var mLogin: AutoCompleteTextView? = null
    private var mPassword: EditText? = null
    private var mEnter: Button? = null
    private var mRegister: Button? = null
    private var mSharedPreferencesHelper: SharedPreferencesHelper? = null

    private var mLoginedUsersAdapter: ArrayAdapter<String>? = null

    private val mOnEnterClickListener = View.OnClickListener {
        if (isEmailValid && isPasswordValid) {
            if (mSharedPreferencesHelper!!.login(User(
                            mLogin!!.text.toString(),
                            mPassword!!.text.toString()))) {
                val startProfileIntent = Intent(activity, ProfileActivity::class.java)
                startProfileIntent.putExtra(ProfileActivity.USER_KEY,
                        User(mLogin!!.text.toString(), mPassword!!.text.toString()))
                startActivity(startProfileIntent)
                activity.finish()
            } else {
                showMessage(R.string.login_error)
            }
        } else {
            showMessage(R.string.input_error)
        }

        for (user in mSharedPreferencesHelper!!.users) {
            if (user.login.equals(mLogin!!.text.toString(), ignoreCase = true) && user.password == mPassword!!.text.toString()) {
                break
            }
        }
    }

    private val mOnRegisterClickListener = View.OnClickListener {
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                .addToBackStack(RegistrationFragment::class.java!!.getName())
                .commit()
    }

    private val mOnLoginFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            mLogin!!.showDropDown()
        }
    }

    private val isEmailValid: Boolean
        get() = !TextUtils.isEmpty(mLogin!!.text) && Patterns.EMAIL_ADDRESS.matcher(mLogin!!.text).matches()

    private val isPasswordValid: Boolean
        get() = !TextUtils.isEmpty(mPassword!!.text)

    private fun showMessage(@StringRes string: Int) {
        Toast.makeText(activity, string, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fr_auth, container, false)

        mSharedPreferencesHelper = SharedPreferencesHelper(activity)

        mLogin = v.findViewById(R.id.etLogin)
        mPassword = v.findViewById(R.id.etPassword)
        mEnter = v.findViewById(R.id.buttonEnter)
        mRegister = v.findViewById(R.id.buttonRegister)

        mEnter!!.setOnClickListener(mOnEnterClickListener)
        mRegister!!.setOnClickListener(mOnRegisterClickListener)
        mLogin!!.onFocusChangeListener = mOnLoginFocusChangeListener

        mLoginedUsersAdapter = ArrayAdapter(
                activity,
                android.R.layout.simple_dropdown_item_1line,
                mSharedPreferencesHelper!!.successLogins
        )
        mLogin!!.setAdapter<ArrayAdapter<String>>(mLoginedUsersAdapter)

        return v
    }

    companion object {

        fun newInstance(): AuthFragment {
            val args = Bundle()

            val fragment = AuthFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
