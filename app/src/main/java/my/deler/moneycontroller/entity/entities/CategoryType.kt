package my.deler.moneycontroller.entity.entities

enum class CategoryType {
    UNKNOWN, WHO, EXPENSES, INCOMING;


    companion object {
        fun from(name: String) = when (name) {
            "Кто" -> WHO
            "Категория Расходов" -> EXPENSES
            "Категория Доходов" -> INCOMING
            else -> UNKNOWN
        }
    }
}