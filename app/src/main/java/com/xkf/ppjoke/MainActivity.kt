package com.xkf.ppjoke

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
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
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSystemBar()

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = findNavController(R.id.nav_host_fragment)
        NavGraphBuilder.build(this, navController, fragment?.id ?: 0)

        viewBinding.navView.setOnNavigationItemSelectedListener(this)
    }

    private fun setSystemBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.WHITE
        val decor = window.decorView
        var ui: Int = decor.systemUiVisibility
        ui = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        decor.systemUiVisibility = ui
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