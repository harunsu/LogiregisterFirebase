package com.example.logiregisterfirebase

import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.logiregisterfirebase.fragments.DashboardFragment
import com.example.logiregisterfirebase.fragments.HomeFragment
import com.example.logiregisterfirebase.fragments.ProfileFragment
import com.example.logiregisterfirebase.fragments.WalletFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private val homeFragment = HomeFragment()
    private val dashboardFragment = DashboardFragment()
    private val walletFragment = WalletFragment()
    private val profileFragment = ProfileFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_dashboard -> replaceFragment(dashboardFragment)
                R.id.ic_wallet -> replaceFragment(walletFragment)
                R.id.ic_profile -> replaceFragment(profileFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}