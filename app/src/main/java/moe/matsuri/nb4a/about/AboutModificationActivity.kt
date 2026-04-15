package moe.matsuri.nb4a.about

import android.os.Bundle
import android.view.MenuItem
import io.nekohasekai.sagernet.BuildConfig
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.databinding.LayoutAboutModificationBinding
import io.nekohasekai.sagernet.ui.ThemedActivity

/**
 * Экран «О модификации»: иконка + описание модификации SecureLaunch, автор,
 * версия приложения. Открывается из drawer-меню.
 */
class AboutModificationActivity : ThemedActivity() {

    private lateinit var binding: LayoutAboutModificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutAboutModificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setTitle(R.string.menu_about_modification)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.modVersion.text = getString(
            R.string.about_modification_version,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun snackbarInternal(text: CharSequence) =
        com.google.android.material.snackbar.Snackbar.make(
            binding.root, text, com.google.android.material.snackbar.Snackbar.LENGTH_LONG
        )
}
