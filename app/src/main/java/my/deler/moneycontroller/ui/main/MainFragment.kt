package my.deler.moneycontroller.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import my.deler.moneycontroller.R
import my.deler.moneycontroller.databinding.FragmentMainBinding
import my.deler.moneycontroller.ui.BaseFragment
import my.deler.moneycontroller.ui.main.additem.AddItemFragmentArgs
import my.deler.moneycontroller.ui.main.additem.AddItemType
import my.deler.moneycontroller.ui.viewmodel.MainViewModel
import my.deler.moneycontroller.ui.viewmodel.ProgressViewModel
import my.deler.moneycontroller.ui.viewmodel.ViewModelFactory
import my.deler.moneycontroller.utils.RC_OPEN_DOCUMENT

class MainFragment : BaseFragment() {
    lateinit var binding: FragmentMainBinding

    lateinit var viewModel: MainViewModel
    lateinit var progressViewModel: ProgressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!, ViewModelFactory(activity!!)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.viewModel = viewModel
        binding.apply {
            selectFileButton.setOnClickListener {
                val pickerIntent = createFilePickerIntent()
                startActivityForResult(pickerIntent, RC_OPEN_DOCUMENT)
            }
            addExpenseButton.setOnClickListener {
                val args = AddItemFragmentArgs(AddItemType.EXPENSE).toBundle()
                findNavController().navigate(R.id.addItemFragment, args)
            }
            addIncomingButton.setOnClickListener {
                val args = AddItemFragmentArgs(AddItemType.INCOMING).toBundle()
                findNavController().navigate(R.id.addItemFragment, args)
            }

            executePendingBindings()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        when (requestCode) {
            RC_OPEN_DOCUMENT -> if (resultCode == Activity.RESULT_OK && resultData != null) {
                val uri = resultData.data
                uri?.let {
                    openFileFromFilePicker(it)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, resultData)
    }

    private fun createFilePickerIntent(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/vnd.google-apps.spreadsheet"

        return intent
    }

    private fun openFileFromFilePicker(uri: Uri) {
        Log.d(TAG, "Opening " + uri.path)
    }

    companion object {
        const val TAG = "MainFragment"
    }
}