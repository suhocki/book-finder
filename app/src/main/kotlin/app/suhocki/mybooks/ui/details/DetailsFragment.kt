package app.suhocki.mybooks.ui.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.extensions.argument
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.jetbrains.anko.support.v4.withArguments
import toothpick.Scope
import toothpick.config.Module


class DetailsFragment : BaseFragment<DetailsUI>(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter =
        scope.getInstance(DetailsPresenter::class.java)

    private val bookId: String by argument(DetailsFragment.ARG_BOOK_ID)

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            object : Module() {
                init {
                    val query = scope.getInstance(FirebaseFirestore::class.java)
                        .collection(FirestoreRepository.BOOKS)
                        .whereEqualTo(BookDbo.ID, bookId)
                        .limit(1)

                    bind(Query::class.java).toInstance(query)
                }
            }
        )
    }

    override val ui by lazy { DetailsUI() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(activity as AppCompatActivity) {
            setSupportActionBar(ui.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun showBook(book: Book) {
        ui.image.setImageURI(book.productLink)
        ui.toolbarLayout.title = book.shortName
        ui.bindBook(book)
    }

    companion object {
        private const val ARG_BOOK_ID = "arg_book_id"
        fun create(bookId: String) =
            DetailsFragment().apply {
                withArguments(ARG_BOOK_ID to bookId)
            }
    }
}

