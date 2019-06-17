package my.deler.moneycontroller.utils

import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import my.deler.moneycontroller.entity.entities.CategoryItemEntity
import java.util.*
import androidx.databinding.Observable as DBObservable


class ObservableString(string: String) : ObservableField<String>(string) {

    @Suppress("RedundantOverride")
    override fun set(value: String) {
        super.set(value)
    }

    override fun get(): String {
        return super.get()!!
    }
}


class ObservableDate(date: Date) : ObservableField<Date>(date) {

    @Suppress("RedundantOverride")
    override fun set(value: Date) {
        super.set(value)
    }

    override fun get(): Date {
        return super.get()!!
    }
}


class ObservableCategoryItemEntity(entity: CategoryItemEntity) : ObservableField<CategoryItemEntity>(entity) {

    @Suppress("RedundantOverride")
    override fun set(value: CategoryItemEntity) {
        super.set(value)
    }

    override fun get(): CategoryItemEntity {
        return super.get()!!
    }
}

fun <T> ObservableField<T>.observable(): Observable<T> {
    val field = this
    return Observable.create { emitter ->
        field.get()?.let { emitter.onNext(it) }

        val callback = object : androidx.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: DBObservable?, propertyId: Int) {
                field.get()?.let {
                    emitter.onNext(it)
                }
            }
        }

        field.addOnPropertyChangedCallback(callback)
        emitter.setDisposable(Disposables.fromAction { field.removeOnPropertyChangedCallback(callback) })
    }
}

fun <T> ObservableList<T>.observable(): Observable<List<T>> {
    val field = this
    return Observable.create { emitter ->
        if (field.isNotEmpty()) emitter.onNext(field)

        val callback = object : ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                emitter.onNext(field)
            }

            override fun onItemRangeMoved(
                sender: ObservableList<T>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                emitter.onNext(field)
            }

            override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                emitter.onNext(field)
            }

            override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                emitter.onNext(field)
            }

            override fun onChanged(sender: ObservableList<T>?) {
                emitter.onNext(field)
            }
        }

        field.addOnListChangedCallback(callback)
        emitter.setDisposable(Disposables.fromAction { field.removeOnListChangedCallback(callback) })
    }
}