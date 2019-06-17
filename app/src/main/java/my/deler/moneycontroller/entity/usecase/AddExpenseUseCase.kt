package my.deler.moneycontroller.entity.usecase

import my.deler.moneycontroller.entity.entities.ExpenseEntity
import my.deler.moneycontroller.data.repository.SheetsRepository
import my.deler.moneycontroller.entity.entities.toData
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(private val sheetsRepository: SheetsRepository) : UseCase<ExpenseEntity, Unit>() {

    override suspend fun execute(params: ExpenseEntity?) {
        params?.let {
            sheetsRepository.addExpense(it.toData())
        }
    }
}