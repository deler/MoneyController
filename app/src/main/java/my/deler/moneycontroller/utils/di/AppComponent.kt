package my.deler.moneycontroller.utils.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import my.deler.moneycontroller.data.repository.AccountRepository
import my.deler.moneycontroller.ui.viewmodel.AddItemViewModel
import my.deler.moneycontroller.ui.viewmodel.MainViewModel
import javax.inject.Singleton


@Singleton
@Component(modules = [SheetsModule::class])
interface AppComponent {
    fun inject(viewModel: AddItemViewModel)
    fun inject(viewModel: MainViewModel)

    @Singleton
    fun accountRepository(): AccountRepository

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}