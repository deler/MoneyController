package my.deler.moneycontroller.entity.entities

import my.deler.moneycontroller.utils.month
import java.util.*

data class IncomingEntity(
    val date: Date,
    val amount: Double,
    val categoryItem: CategoryItemEntity,
    val who: CategoryItemEntity,
    val comment: String
) {
    val month: Int
        get() = date.month()
}