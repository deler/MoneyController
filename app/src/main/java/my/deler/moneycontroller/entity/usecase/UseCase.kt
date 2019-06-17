package my.deler.moneycontroller.entity.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<PARAMS, VALUE> {
    protected abstract suspend fun execute(params: PARAMS?): VALUE

    suspend fun run(params: PARAMS? = null): VALUE = withContext(Dispatchers.IO) {
        execute(params)
    }
}
