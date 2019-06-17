package my.deler.moneycontroller.ui

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseFragment : Fragment(), CoroutineScope by MainScope() {
    override fun onDestroy() {
        super.onDestroy()
        cancel() // cancel is extension on CoroutineScope
    }
}