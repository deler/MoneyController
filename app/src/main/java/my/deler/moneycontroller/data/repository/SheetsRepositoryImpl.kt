package my.deler.moneycontroller.data.repository

import android.content.Context
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import my.deler.moneycontroller.data.models.CategoryData
import my.deler.moneycontroller.data.models.ExpenseData
import my.deler.moneycontroller.data.models.IncomingData
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SheetsRepositoryImpl @Inject constructor(context: Context, private val accountRepository: AccountRepository) :
    SheetsRepository {

    companion object {
        val HTTP_TRANSPORT: HttpTransport = AndroidHttp.newCompatibleTransport()
        val JSON_FACTORY: JacksonFactory = JacksonFactory.getDefaultInstance()
    }

    private val spreadsheetId = "1YQ4uhk96NdEXPUATRdmmWsF-IsrOOxJRPoeBIimbuA4"
    private val contextRef: WeakReference<Context> = WeakReference(context)

    private val credential: GoogleAccountCredential
        get() {
            val credential = GoogleAccountCredential.usingOAuth2(
                contextRef.get(),
                setOf(SheetsScopes.SPREADSHEETS)
            )
            credential.selectedAccount = accountRepository.account
            return credential
        }

    private val service: Sheets
        get() {
            return Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Google Sign In Quickstart")
                .build()
        }

    override suspend fun getCategories(): List<CategoryData> {
        val readRange = "consts!A:H"
        val response = service.spreadsheets()
            .values()
            .get(spreadsheetId, readRange)
            .setMajorDimension("COLUMNS")
            .execute()
        val values: List<MutableList<Any>>? = response.getValues()

        val categories = mutableListOf<CategoryData>()
        values?.filter { it.isNotEmpty() }?.forEach {
            val categoryName = it.removeAt(0) as String
            it.map { value -> value as String }
                .forEach { value ->
                    categories.add(CategoryData(categoryName, value))
                }
        }
        return categories
    }

    override suspend fun addExpense(expense: ExpenseData) {
        val range = "I Расходы!A:F"
        val values = listOf(
            listOf(
                expense.month,
                expense.date,
                expense.amount,
                expense.category,
                expense.who,
                expense.comment
            )
        )
        val body = ValueRange().setValues(values)
        service.spreadsheets()
            .values()
            .append(spreadsheetId, range, body)
            .setValueInputOption("USER_ENTERED")
            .execute()
    }

    override suspend fun addIncoming(incoming: IncomingData) {
        val range = "I Доходы!A:F"
        val values = listOf(
            listOf(
                incoming.month,
                incoming.date,
                incoming.amount,
                incoming.category,
                incoming.who,
                incoming.comment
            )
        )
        val body = ValueRange().setValues(values)
        service.spreadsheets()
            .values()
            .append(spreadsheetId, range, body)
            .setValueInputOption("USER_ENTERED")
            .execute()
    }
}