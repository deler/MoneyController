package my.deler.moneycontroller.ui.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class ProgressViewModel : ViewModel() {
    val loadingText = ObservableField<String>()
    val isLoading = ObservableBoolean(false)
}