package net.bouzuya.sample5

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import net.bouzuya.sample5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        // FIXME
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = BookmarkDatabase.getDatabase(applicationContext)
                return MainViewModel(
                    BookmarkRepository(database.bookmarkDao()),
                    TagRepository(database.tagDao())
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .also { binding ->
                binding.lifecycleOwner = this
                binding.viewModel = viewModel

                binding.toolbar.let { toolbar ->
                    setSupportActionBar(toolbar)
                    toolbar.setupWithNavController(
                        findNavController(),
                        AppBarConfiguration(
                            setOf(R.id.homeFragment, R.id.tagListFragment),
                            binding.mainDrawerLayout
                        )
                    )
                }
                binding.mainNavigationView.setupWithNavController(findNavController())
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController().navigateUp()
    }

    private fun findNavController(): NavController = findNavController(R.id.nav_host_fragment)
}
