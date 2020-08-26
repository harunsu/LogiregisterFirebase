package com.example.logiregisterfirebase.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logiregisterfirebase.R
import com.example.logiregisterfirebase.drawermenu.ClickListener
import com.example.logiregisterfirebase.drawermenu.NavigationItemModel
import com.example.logiregisterfirebase.drawermenu.NavigationRVAdapter
import com.example.logiregisterfirebase.drawermenu.RecyclerTouchListener
import com.example.logiregisterfirebase.fragments.*
import com.example.logiregisterfirebase.user.LoginActivity
import com.example.logiregisterfirebase.user.SessionManager
import com.example.logiregisterfirebase.user.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
        var sessionUser = User()
    }

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val walletFragment = WalletFragment()
    private val profileFragment = ProfileFragment()
    private lateinit var session: SessionManager

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter

    private var items = arrayListOf(
        NavigationItemModel(R.drawable.ic_home, "Home"),
        NavigationItemModel(R.drawable.ic_profile, "Music"),
        NavigationItemModel(R.drawable.ic_home, "Movies"),
        NavigationItemModel(R.drawable.ic_home, "Books"),
        NavigationItemModel(R.drawable.ic_profile, "Logout"),
        NavigationItemModel(R.drawable.ic_home, "Settings"),
        NavigationItemModel(R.drawable.ic_home, "Like us on facebook")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session = SessionManager(applicationContext)
        session.checkLogin()
        sessionUser = session.getUserDetails()


        //TOOLBAR
        drawerLayout = findViewById(R.id.drawer_layout)
        // Set the toolbar
        setSupportActionBar(toolbar)
        // Close the soft keyboard when you open or close the Drawer

        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.common_open_on_phone,
            R.string.app_name
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
        updateAdapter(0)

        // Setup Recyclerview's Layout
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)
        navigation_header_img.setImageResource(R.drawable.ic_dashboard)

        // Set background of Drawer
        navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        // # Home Fragment
                        val homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, homeFragment).commit()
                    }
                    1 -> {
                        // # Music Fragment
                        val musicFragment = DashboardFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, musicFragment).commit()
                    }
                    2 -> {
                        // # Movies Fragment
                        val moviesFragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, moviesFragment).commit()
                    }
                    3 -> {
                        // # Books Fragment
                        val booksFragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, booksFragment).commit()
                    }
                    4 -> {
                        // # Logout

                        if(session.LogOutUser()){
                            var intent: Intent = Intent(this@MainActivity,LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }

                    }
                    5 -> {
                        // # Settings Fragment
                        val settingsFragment = ProfileFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, settingsFragment).commit()
                    }
                    6 -> {
                        // # Open URL in browser
                        val uri: Uri = Uri.parse("https://johnc.co/fb")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                if (position != 6 && position != 4) {
                    updateAdapter(position)
                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))
        activity_main_appbarlayout.visibility=View.VISIBLE
        replaceFragment(homeFragment)
        bottom_navigation.setOnNavigationItemSelectedListener {
             when(it.itemId){
                 R.id.ic_home -> {
                     activity_main_appbarlayout.visibility=View.VISIBLE
                     replaceFragment(homeFragment)
                 }
                 R.id.ic_search -> {
                     activity_main_appbarlayout.visibility=View.GONE
                     replaceFragment(searchFragment)
                 }
                 R.id.ic_wallet -> {
                     activity_main_appbarlayout.visibility=View.GONE
                     replaceFragment(walletFragment)
                 }
                 R.id.ic_profile -> {
                     activity_main_appbarlayout.visibility=View.GONE
                     replaceFragment(profileFragment)
                 }
             }
             true
         }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {

            Log.d(TAG, "replace replaceFragment")
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }



}