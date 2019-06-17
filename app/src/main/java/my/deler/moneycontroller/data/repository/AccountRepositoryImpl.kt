package my.deler.moneycontroller.data.repository

import android.accounts.Account
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(): AccountRepository {
    override var account = Account("empty", "none")
}