package my.deler.moneycontroller.ui.login

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.services.sheets.v4.SheetsScopes
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import my.deler.moneycontroller.R
import my.deler.moneycontroller.ui.BaseActivity
import my.deler.moneycontroller.ui.main.MainActivity
import my.deler.moneycontroller.utils.KEY_ACCOUNT
import my.deler.moneycontroller.utils.RC_SIGN_IN
import my.deler.moneycontroller.utils.di.DI

class LoginActivity : BaseActivity() {

    private var googleSignInClient: GoogleSignInClient? = null
    private var account1: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar.isIndeterminate = true

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onResume() {
        super.onResume()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (GoogleSignIn.hasPermissions(account, Scope(SheetsScopes.SPREADSHEETS))) {
            updateUI(account)
        } else {
            signIn()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_ACCOUNT, account1)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient?.signInIntent
        signInIntent?.let {
            startActivityForResult(it, RC_SIGN_IN)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        Log.d(TAG, "handleSignInResult:" + completedTask.isSuccessful)
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.w(TAG, "handleSignInResult:error", e)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) = launch {
        account1 = account?.account
        account?.account?.let {
            DI.component.accountRepository().account = it
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    companion object {
        const val TAG = "LoginActivity"
    }
}
