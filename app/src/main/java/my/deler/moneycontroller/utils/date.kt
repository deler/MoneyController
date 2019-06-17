package my.deler.moneycontroller.utils

import android.icu.text.SimpleDateFormat
import java.util.*

fun Date.format(): String {
    val format = SimpleDateFormat("dd/MM/yyy")
    return format.format(this)
}

fun Date.month(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH)+1
}