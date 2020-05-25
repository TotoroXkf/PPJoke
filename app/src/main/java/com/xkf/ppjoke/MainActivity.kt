package com.xkf.ppjoke

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xkf.ppjoke.utils.NavGraphBuilder
import com.xkf.ppjoke.view.AppBottomBarView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = findNavController(R.id.nav_host_fragment)
        NavGraphBuilder.build(this, navController, fragment?.id ?: 0)
        
        val navView: AppBottomBarView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(this)
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navController.navigate(item.itemId)
        return !TextUtils.isEmpty(item.title)
    }
}
