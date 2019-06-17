package my.deler.moneycontroller.entity.entities

import my.deler.moneycontroller.data.models.CategoryData
import my.deler.moneycontroller.data.models.ExpenseData
import my.deler.moneycontroller.data.models.IncomingData
import my.deler.moneycontroller.utils.format

fun CategoryData.toEntity() = CategoryItemEntity(CategoryType.from(category), value)

fun ExpenseEntity.toData() = ExpenseData(
    month,
    date.format(),
    amount,
    categoryItem.value,
    who.value,
    comment
)

fun IncomingEntity.toData() = IncomingData(
    month,
    date.format(),
    amount,
    categoryItem.value,
    who.value,
    comment
)