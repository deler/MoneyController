package my.deler.moneycontroller.ui.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


class ViewModelFactory(activity: FragmentActivity) : ViewModelProvider.NewInstanceFactory() {
    private val progressViewModel = ViewModelProviders.of(activity).get(ProgressViewModel::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        AddItemViewModel::class.java -> AddItemViewModel(progressViewModel) as T
        MainViewModel::class.java -> MainViewModel(progressViewModel) as T
        else -> super.create(modelClass)
    }
}