package my.deler.moneycontroller.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import my.deler.moneycontroller.R
import my.deler.moneycontroller.databinding.ActivityMainBinding
import my.deler.moneycontroller.ui.BaseActivity
import my.deler.moneycontroller.ui.viewmodel.ProgressViewModel


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var progressViewModel: ProgressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewModel = ViewModelProviders.of(this).get(ProgressViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.progressViewModel = progressViewModel

        binding.apply {
            setSupportActionBar(toolbar)

            navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)

            executePendingBindings()
        }

        binding.executePendingBindings()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    companion object {
        const val TAG = "MainActivity"
    }
}
