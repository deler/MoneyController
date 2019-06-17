package my.deler.moneycontroller.utils.di

import dagger.Binds
import dagger.Module
import my.deler.moneycontroller.data.repository.AccountRepository
import my.deler.moneycontroller.data.repository.AccountRepositoryImpl
import my.deler.moneycontroller.data.repository.SheetsRepository
import my.deler.moneycontroller.data.repository.SheetsRepositoryImpl

@Suppress("unused")
@Module
abstract class SheetsModule {

    @Binds
    abstract fun provideAccountRepository(accountRepository: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun provideSheetsRepository(sheetsRepository: SheetsRepositoryImpl): SheetsRepository
}