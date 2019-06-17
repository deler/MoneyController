package my.deler.moneycontroller.utils.di

import android.content.Context

object DI {
    lateinit var component: AppComponent
        private set

    fun init(applicationContext: Context) {
        component = DaggerAppComponent.builder()
            .context(applicationContext)
            .build()
    }
}