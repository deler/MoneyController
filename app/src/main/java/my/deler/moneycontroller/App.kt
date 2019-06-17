package my.deler.moneycontroller

import android.app.Application
import my.deler.moneycontroller.utils.di.DI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.init(applicationContext)
    }
}