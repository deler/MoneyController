package my.deler.moneycontroller.ui.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import my.deler.moneycontroller.entity.entities.CategoryItemEntity
import my.deler.moneycontroller.entity.entities.CategoryType
import my.deler.moneycontroller.entity.entities.ExpenseEntity
import my.deler.moneycontroller.entity.entities.IncomingEntity
import my.deler.moneycontroller.entity.usecase.AddExpenseUseCase
import my.deler.moneycontroller.entity.usecase.AddIncomingUseCase
import my.deler.moneycontroller.entity.usecase.GetCategoriesUseCase
import my.deler.moneycontroller.ui.main.additem.AddItemType
import my.deler.moneycontroller.utils.*
import my.deler.moneycontroller.utils.di.DI
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class AddItemViewModel(progressViewModel: ProgressViewModel) : MainViewModel(progressViewModel) {
    @Inject
    lateinit var getCategoriesUseCase: Provider<GetCategoriesUseCase>
    @Inject
    lateinit var addExpenseUseCase: Provider<AddExpenseUseCase>
    @Inject
    lateinit var addIncomingUseCase: Provider<AddIncomingUseCase>

    var exit: MutableLiveData<Boolean> = MutableLiveData(false)

    var expenseCategories: ObservableList<CategoryItemEntity> = ObservableArrayList()
    var incomingCategories: ObservableList<CategoryItemEntity> = ObservableArrayList()
    var whoCategories: ObservableList<CategoryItemEntity> = ObservableArrayList()

    var who = ObservableCategoryItemEntity(CategoryItemEntity(CategoryType.WHO, ""))
    var expense = ObservableCategoryItemEntity(CategoryItemEntity(CategoryType.EXPENSES, ""))
    var incoming = ObservableCategoryItemEntity(CategoryItemEntity(CategoryType.INCOMING, ""))

    var type = ObservableField<AddItemType>(AddItemType.EXPENSE)

    var amount = ObservableString("")
    var date = ObservableDate(Date())
    var comment = ObservableString("")

    var dateString = ObservableString(Date().format())

    init {
        DI.component.inject(this)
        getCategories()
        date.observable().subscribe {
            dateString.set(it.format())
        }.also { addDisposable(it) }
    }

    fun addClicked() {
        when (type.get()) {
            AddItemType.INCOMING -> addIncoming()
            AddItemType.EXPENSE -> addExpense()
        }
    }

    private fun getCategories() = viewModelScope.launch {
        progressViewModel.isLoading.set(true)
        val categories = getCategoriesUseCase.get().run()
        categories[CategoryType.EXPENSES]?.apply {
            expense.set(this.first())
            expenseCategories.addAll(this)
        }

        categories[CategoryType.INCOMING]?.apply {
            incoming.set(this.first())
            incomingCategories.addAll(this)
        }

        categories[CategoryType.WHO]?.apply {
            who.set(this.first())
            whoCategories.addAll(this)
        }

        progressViewModel.isLoading.set(false)
    }


    private fun addExpense() = viewModelScope.launch {
        progressViewModel.isLoading.set(true)
        addExpenseUseCase.get().run(
            ExpenseEntity(
                date.get(),
                amount.get().toDoubleOrZero(),
                expense.get(),
                who.get(),
                comment.get()
            )
        )
        progressViewModel.isLoading.set(false)
        exit.value = true
    }


    private fun addIncoming() = viewModelScope.launch {
        progressViewModel.isLoading.set(true)
        addIncomingUseCase.get().run(
            IncomingEntity(
                date.get(),
                amount.get().toDoubleOrZero(),
                incoming.get(),
                who.get(),
                comment.get()
            )
        )
        progressViewModel.isLoading.set(false)
        exit.value = true
    }

    private fun String.toDoubleOrZero(): Double = toDoubleOrNull() ?: 0.0

}