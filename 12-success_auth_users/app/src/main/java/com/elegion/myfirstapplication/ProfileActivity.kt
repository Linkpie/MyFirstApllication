package com.elegion.myfirstapplication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    private var mPhoto: AppCompatImageView? = null
    private var mLogin: TextView? = null
    private var mPassword: TextView? = null
    private var mSharedPreferencesHelper: SharedPreferencesHelper? = null
    private var mUser: User? = null

    private val mOnPhotoClickListener = View.OnClickListener { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_profile)

        mSharedPreferencesHelper = SharedPreferencesHelper(this)

        mPhoto = findViewById(R.id.ivPhoto)
        mLogin = findViewById(R.id.tvEmail)
        mPassword = findViewById(R.id.tvPassword)

        val bundle = intent.extras
        mUser = bundle!!.get(USER_KEY) as User
        mLogin!!.text = mUser!!.login
        mPassword!!.text = mUser!!.password

        mPhoto!!.setOnClickListener(mOnPhotoClickListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionLogout -> {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }
}
