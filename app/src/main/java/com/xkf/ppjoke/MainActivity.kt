package com.xkf.ppjoke

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xkf.ppjoke.base.AppConfig
import com.xkf.ppjoke.databinding.ActivityMainBinding
import com.xkf.ppjoke.ui.login.UserManager
import com.xkf.ppjoke.utils.NavGraphBuilder

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = findNavController(R.id.nav_host_fragment)
        NavGraphBuilder.build(this, navController, fragment?.id ?: 0)

        viewBinding.navView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val destConfig = AppConfig.destinationMap
        for (destination in destConfig.values) {
            if (destination.id == item.itemId && destination.needLogin && !UserManager.isLogin()) {
                UserManager.login(this).observe(this, Observer {
                    viewBinding.navView.selectedItemId = item.itemId
                })
                return false
            }
        }

        navController.navigate(item.itemId)
        return !TextUtils.isEmpty(item.title)
    }
}