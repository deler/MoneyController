package my.deler.moneycontroller.data.models

data class IncomingData(
    val month: Int,
    val date: String,
    val amount: Double,
    val category: String,
    val who: String,
    val comment: String
)