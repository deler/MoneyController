package my.deler.moneycontroller.utils

import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField
import my.deler.moneycontroller.entity.entities.CategoryItemEntity
import my.deler.moneycontroller.ui.main.additem.AddItemType


@BindingAdapter("android:background")
fun bindColor(view: View, type: ObservableField<AddItemType>) {
    val color = when (type.get()) {
        AddItemType.INCOMING -> "#b8ffb7"
        AddItemType.EXPENSE -> "#ffb7bc"
        else -> "#FFFFFF"
    }
    view.setBackgroundColor(Color.parseColor(color))
}


@BindingConversion
fun convertCategoryTypeToString(entity: CategoryItemEntity) = entity.value

@BindingConversion
fun convertDoubleToString(double: Double) = String.format("%.2f", double)

@BindingConversion
fun convertBooleanToVisibility(visible: Boolean) = if (visible) View.VISIBLE else View.GONE