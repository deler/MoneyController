package my.deler.moneycontroller.ui.viewmodel

import my.deler.moneycontroller.utils.di.DI

open class MainViewModel(val progressViewModel: ProgressViewModel) : BaseViewModel() {

    init {
        DI.component.inject(this)
    }

}