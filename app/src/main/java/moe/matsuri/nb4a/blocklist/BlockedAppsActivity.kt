package moe.matsuri.nb4a.blocklist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.databinding.LayoutBlockedAppsBinding
import io.nekohasekai.sagernet.databinding.LayoutBlockedAppsItemBinding
import io.nekohasekai.sagernet.ui.ThemedActivity
import io.nekohasekai.sagernet.utils.PackageCache

/**
 * Экран «Пакеты»:
 *  - в режиме блокировки запуска VPN (`EXTRA_SHOW_EMPTY=false`) показывает
 *    только установленные приложения, попавшие в блок-лист;
 *  - в режиме «из меню» (`EXTRA_SHOW_EMPTY=true`) показывает **весь** список
 *    (и не установленные тоже), добавляет строку ввода с кнопками
 *    «Проверить» и «Добавить».
 *
 *  Тап по карточке приложения из [AccreditedBlocklist.defaults] запрашивает
 *  административный пароль ([AdminPassword]); по пользовательскому
 *  пакету — сразу открывает системные настройки.
 */
class BlockedAppsActivity : ThemedActivity() {

    companion object {
        const val EXTRA_SHOW_EMPTY = "show_empty"
    }

    private lateinit var binding: LayoutBlockedAppsBinding
    private val adapter = BlockedAdapter()
    private val allItems = mutableListOf<BlockedApp>()
    private val items = mutableListOf<BlockedApp>()
    private var showEmpty = false
    private var selectedCategory: PackageCategory = PackageCategory.ALL

    private lateinit var userStore: UserPackagesStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBlockedAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showEmpty = intent.getBooleanExtra(EXTRA_SHOW_EMPTY, false)
        userStore = UserPackagesStore(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setTitle(
                if (showEmpty) R.string.menu_packages
                else R.string.blocked_apps_title
            )
            setDisplayHomeAsUpEnabled(true)
        }

        if (showEmpty) {
            binding.hintText.setText(R.string.blocked_apps_hint_menu)
            binding.inputRow.visibility = View.VISIBLE
            binding.buttonsRow.visibility = View.VISIBLE
            binding.listHeaderRow.visibility = View.VISIBLE

            binding.checkButton.setOnClickListener { onCheckClicked() }
            binding.addButton.setOnClickListener { onAddClicked() }
            setupCategoryFilter()
        }

        binding.list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.list.itemAnimator = DefaultItemAnimator()
        binding.list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // ---------- Category filter ----------

    private fun setupCategoryFilter() {
        binding.categoryFilterLayout.visibility = View.VISIBLE
        val categories = PackageCategory.entries.toTypedArray()
        val labels = categories.map { it.label }
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, labels)
        binding.categoryFilter.setAdapter(filterAdapter)
        binding.categoryFilter.setText(PackageCategory.ALL.label, false)

        binding.categoryFilter.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = categories[position]
            applyFilter()
        }
    }

    // ---------- Scan / refresh ----------

    private fun refresh() {
        val scanner = BlockedAppsScanner
        val found = if (showEmpty) {
            scanner.listAll(this, AccreditedBlocklist.defaults, userStore.getAll())
        } else {
            scanner.scanInstalled(this, AccreditedBlocklist.defaults, userStore.getAll())
        }

        if (found.isEmpty()) {
            allItems.clear()
            items.clear()
            adapter.notifyDataSetChanged()
            if (showEmpty) {
                binding.list.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
                return
            }
            Toast.makeText(this, R.string.blocked_apps_cleared, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.list.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
        allItems.clear()
        allItems.addAll(found)
        applyFilter()
    }

    private fun applyFilter() {
        items.clear()
        if (selectedCategory == PackageCategory.ALL) {
            items.addAll(allItems)
        } else {
            items.addAll(allItems.filter { PackageCategory.of(it.packageName) == selectedCategory })
        }
        adapter.notifyDataSetChanged()

        if (items.isEmpty() && allItems.isNotEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.list.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.list.visibility = View.VISIBLE
        }
    }

    // ---------- Buttons ----------

    private fun readInput(): String = binding.packageInput.text?.toString()?.trim().orEmpty()

    private fun onCheckClicked() {
        // Сбросить фильтр на «Все категории»
        selectedCategory = PackageCategory.ALL
        binding.categoryFilter.setText(PackageCategory.ALL.label, false)
        // Перескaнировать весь список (обновить статусы installed/not-installed)
        PackageCache.reload()
        refresh()
        Toast.makeText(this, R.string.check_package_rescanned, Toast.LENGTH_SHORT).show()
    }

    private fun onAddClicked() {
        val pkg = readInput()
        if (pkg.isEmpty()) {
            Toast.makeText(this, R.string.add_package_hint, Toast.LENGTH_SHORT).show()
            return
        }
        userStore.add(pkg)
        binding.packageInput.setText("")
        refresh()
        Toast.makeText(
            this,
            getString(R.string.add_package_added, pkg),
            Toast.LENGTH_SHORT
        ).show()
    }

    // ---------- Item click ----------

    private fun onItemClicked(app: BlockedApp) {
        when (app.source) {
            BlocklistSource.USER -> openAppDetails(app)
            BlocklistSource.DEFAULT -> askAdminPassword { openAppDetails(app) }
        }
    }

    private fun onRemoveUserPackage(app: BlockedApp) {
        userStore.remove(app.packageName)
        refresh()
        Toast.makeText(
            this,
            getString(R.string.add_package_added, app.packageName).replace(
                getString(R.string.add_package_action),
                getString(R.string.remove_user_package)
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun askAdminPassword(onSuccess: () -> Unit) {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setHint(R.string.admin_password_hint)
        }
        val padding = (resources.displayMetrics.density * 16).toInt()
        val wrapper = LinearLayout(this).apply {
            setPadding(padding, padding / 2, padding, 0)
            addView(input, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.admin_password_title)
            .setMessage(R.string.admin_password_message)
            .setView(wrapper)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (AdminPassword.matches(input.text?.toString())) onSuccess()
                else Toast.makeText(this, R.string.admin_password_wrong, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun openAppDetails(app: BlockedApp) {
        if (!app.installed) {
            Toast.makeText(
                this,
                getString(R.string.check_package_not_installed, app.packageName),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", app.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message ?: "Cannot open application details",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun snackbarInternal(text: CharSequence) =
        com.google.android.material.snackbar.Snackbar.make(
            binding.root, text, com.google.android.material.snackbar.Snackbar.LENGTH_LONG
        )

    // ---------- Adapter ----------

    private inner class BlockedAdapter : RecyclerView.Adapter<BlockedViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            BlockedViewHolder(
                LayoutBlockedAppsItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

        override fun onBindViewHolder(holder: BlockedViewHolder, position: Int) =
            holder.bind(items[position])

        override fun getItemCount(): Int = items.size
    }

    private inner class BlockedViewHolder(
        private val itemBinding: LayoutBlockedAppsItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(app: BlockedApp) {
            val ctx = itemBinding.root.context
            itemBinding.title.text = app.label
            itemBinding.desc.text = app.packageName

            if (app.icon != null) {
                itemBinding.itemicon.setImageDrawable(app.icon)
            } else {
                itemBinding.itemicon.setImageResource(android.R.drawable.sym_def_app_icon)
            }

            val bg: Int
            val titleColor: Int
            val badge: String?

            when {
                !app.installed -> {
                    bg = R.drawable.bg_not_installed_card
                    titleColor = ctx.getColor(android.R.color.darker_gray)
                    badge = when (app.source) {
                        BlocklistSource.USER -> ctx.getString(R.string.blocked_app_badge_user_not_installed)
                        BlocklistSource.DEFAULT -> ctx.getString(R.string.blocked_app_badge_not_installed)
                    }
                }
                app.source == BlocklistSource.USER -> {
                    bg = R.drawable.bg_user_app_card
                    titleColor = ctx.getColor(R.color.material_amber_500)
                    badge = ctx.getString(R.string.blocked_app_badge_user)
                }
                else -> {
                    bg = R.drawable.bg_blocked_app_card
                    titleColor = ctx.getColor(R.color.material_red_accent_200)
                    badge = null
                }
            }

            itemBinding.itemRoot.setBackgroundResource(bg)
            itemBinding.title.setTextColor(titleColor)
            if (badge != null) {
                itemBinding.sourceBadge.visibility = View.VISIBLE
                itemBinding.sourceBadge.text = badge
                val badgeBg = if (app.source == BlocklistSource.USER)
                    R.color.material_amber_500 else android.R.color.darker_gray
                itemBinding.sourceBadge.setBackgroundColor(ctx.getColor(badgeBg))
            } else {
                itemBinding.sourceBadge.visibility = View.GONE
            }

            // RKS Global badge
            if (RksGlobal.isRks(app.packageName)) {
                itemBinding.rksBadge.visibility = View.VISIBLE
                itemBinding.rksBadge.text = RksGlobal.badge
            } else {
                itemBinding.rksBadge.visibility = View.GONE
            }

            // Кнопка «Удалить из списка» — только для пользовательских пакетов
            if (app.source == BlocklistSource.USER) {
                itemBinding.removeButton.visibility = View.VISIBLE
                itemBinding.removeButton.setOnClickListener { onRemoveUserPackage(app) }
            } else {
                itemBinding.removeButton.visibility = View.GONE
            }

            itemBinding.root.setOnClickListener { onItemClicked(app) }
        }
    }
}
