package com.tife.tifescienticcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.tife.tifescienticcalculator.feature.basic.BasicFragment
import com.tife.tifescienticcalculator.feature.combinatorics.CombinatoricsFragment
import com.tife.tifescienticcalculator.feature.matrix.MatrixFragment
import com.tife.tifescienticcalculator.feature.scientific.ScientificFragment
import com.tife.tifescienticcalculator.feature.statistics.StatisticsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val prefs by lazy { getSharedPreferences("tife", MODE_PRIVATE) }
    private val tag = "Lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate")
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            showMode(menuItem.itemId)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // restore the mode chosen before a rotation/process death, else the last saved preference
        val startId = savedInstanceState?.getInt(KEY_MODE)
            ?: prefs.getInt(KEY_MODE, R.id.nav_basic)
        if (savedInstanceState == null) showMode(startId)
        navView.setCheckedItem(startId)
        supportActionBar?.title = titleFor(startId)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun showMode(itemId: Int) {
        val fragment: Fragment = when (itemId) {
            R.id.nav_scientific -> ScientificFragment()
            R.id.nav_matrix -> MatrixFragment()
            R.id.nav_combinatorics -> CombinatoricsFragment()
            R.id.nav_statistics -> StatisticsFragment()
            else -> BasicFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        supportActionBar?.title = titleFor(itemId)
        navView.setCheckedItem(itemId)
    }

    private fun titleFor(itemId: Int): String = getString(
        when (itemId) {
            R.id.nav_scientific -> R.string.title_scientific
            R.id.nav_matrix -> R.string.nav_matrix
            R.id.nav_combinatorics -> R.string.nav_combinatorics
            R.id.nav_statistics -> R.string.nav_statistics
            else -> R.string.nav_basic
        }
    )

    private fun currentModeId(): Int = navView.checkedItem?.itemId ?: R.id.nav_basic

    override fun onStart() { super.onStart(); Log.d(tag, "onStart") }
    override fun onResume() { super.onResume(); Log.d(tag, "onResume") }

    override fun onPause() {
        super.onPause(); Log.d(tag, "onPause")
        prefs.edit().putInt(KEY_MODE, currentModeId()).apply()
    }

    override fun onStop() { super.onStop(); Log.d(tag, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(tag, "onDestroy") }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MODE, currentModeId())
    }

    companion object {
        private const val KEY_MODE = "mode"
    }
}
