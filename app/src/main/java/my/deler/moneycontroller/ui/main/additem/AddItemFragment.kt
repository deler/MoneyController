package my.deler.moneycontroller.ui.main.additem

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import my.deler.moneycontroller.R
import my.deler.moneycontroller.databinding.FragmentAddItemBinding
import my.deler.moneycontroller.entity.entities.CategoryItemEntity
import my.deler.moneycontroller.ui.BaseFragment
import my.deler.moneycontroller.ui.viewmodel.AddItemViewModel
import my.deler.moneycontroller.ui.viewmodel.ViewModelFactory
import my.deler.moneycontroller.utils.AutoDisposable
import my.deler.moneycontroller.utils.ObservableCategoryItemEntity
import my.deler.moneycontroller.utils.observable
import org.angmarch.views.NiceSpinner
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.regex.Pattern


class AddItemFragment : BaseFragment() {

    private lateinit var binding: FragmentAddItemBinding

    lateinit var viewModel: AddItemViewModel

    private val args: AddItemFragmentArgs by navArgs()

    private var subscriptions = AutoDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity!!)).get(AddItemViewModel::class.java)
        viewModel.type.set(args.type)
        subscriptions = AutoDisposable()
        subscriptions.bindTo(this.lifecycle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_item, container, false)
        binding.fragment = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = if (args.type == AddItemType.INCOMING) "INCOMING" else "EXPENSE"

        binding.apply {

            amountText.filters = listOf(DecimalDigitsInputFilter(7, 2)).toTypedArray()

            viewModel?.exit?.observe(this@AddItemFragment, Observer {
                if (it) findNavController().navigateUp()
            })

            setupSpinner(whoSpinner, viewModel?.whoCategories, viewModel?.who)
            setupSpinner(expenseSpinner, viewModel?.expenseCategories, viewModel?.expense)
            setupSpinner(incomingSpinner, viewModel?.incomingCategories, viewModel?.incoming)

            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel?.date?.set(calendar.time)
            }

            dateEditText.setOnClickListener {
                val calendar = Calendar.getInstance()
                calendar.time = viewModel?.date?.get()
                DatePickerDialog(
                    context!!,
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }


            executePendingBindings()
        }
    }

    private fun setupSpinner(
        spinner: NiceSpinner,
        categories: ObservableList<CategoryItemEntity>?,
        category: ObservableCategoryItemEntity?
    ) {
        val formatter = formatter@{ it: Any? ->
            var str = SpannableString("")
            if (it is CategoryItemEntity) {
                str = SpannableString(it.value)
            }
            return@formatter str
        }

        spinner.setSelectedTextFormatter(formatter)
        spinner.setSpinnerTextFormatter(formatter)

        spinner.onSpinnerItemSelectedListener = OnSpinnerItemSelectedListener { _, _, position, _ ->
            categories?.get(position)?.let {
                category?.apply {
                    if (get() != it) {
                        set(it)
                    }
                }
            }
        }

        categories?.observable()?.subscribe { list ->
            val item = category?.get()
            if (list?.isNotEmpty() == true && item != null) {
                list.apply {
                    indexOf(item)
                        .let { if (it >= 0) it else 0 }
                        .also {
                            spinner.attachDataSource(list)
                            spinner.selectedIndex = it
                            spinner.text = formatter(item)
                        }
                }
            } else if (list?.isNotEmpty() == true) {
                spinner.attachDataSource(list)
            }
        }?.also { subscriptions.add(it) }

        category?.observable()?.subscribe {
            val item = it
            if (categories?.isNotEmpty() == true) {
                categories.apply {
                    indexOf(item)
                        .let { index -> if (index >= 0) index else 0 }
                        .also { index ->
                            if (spinner.selectedIndex != index) {
                                spinner.selectedIndex = index
                                spinner.text = formatter(item)
                            }
                        }
                }
            }
        }?.also { subscriptions.add(it) }
    }

    companion object {
        const val TAG = "AddItemFragment"
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {

        private val mPattern =
            Pattern.compile("""[0-9]{0,${digitsBeforeZero - 1}}+((\.[0-9]{0,${digitsAfterZero - 1}})?)||(\.)?""")

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {

            val matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else source
        }
    }

}

