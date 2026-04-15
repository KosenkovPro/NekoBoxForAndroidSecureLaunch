package moe.matsuri.nb4a.blocklist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.nekohasekai.sagernet.R
import io.nekohasekai.sagernet.databinding.LayoutBlockedAppsBinding
import io.nekohasekai.sagernet.databinding.LayoutBlockedAppsItemBinding
import io.nekohasekai.sagernet.ui.ThemedActivity

/**
 * Экран, открывающийся вместо запуска VPN, когда сканер находит установленные
 * приложения из [AccreditedBlocklist]. Каждая карточка — с красной рамкой;
 * тап открывает системные настройки приложения, где пользователь может
 * удалить его. Стрелка в тулбаре возвращает на главный экран.
 */
class BlockedAppsActivity : ThemedActivity() {

    companion object {
        /** Если `true`, экран остаётся открытым даже с пустым списком
         *  (вариант «открыто из меню»). По умолчанию — `false`:
         *  экран используется как блокировка запуска VPN и сам закрывается,
         *  когда совпадений не осталось. */
        const val EXTRA_SHOW_EMPTY = "show_empty"
    }

    private lateinit var binding: LayoutBlockedAppsBinding
    private val adapter = BlockedAdapter()
    private val items = mutableListOf<BlockedApp>()
    private var showEmpty = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBlockedAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showEmpty = intent.getBooleanExtra(EXTRA_SHOW_EMPTY, false)

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

    private fun refresh() {
        val found = BlockedAppsScanner.scan(this, AccreditedBlocklist.defaults)
        if (found.isEmpty()) {
            if (showEmpty) {
                items.clear()
                adapter.notifyDataSetChanged()
                binding.list.visibility = android.view.View.GONE
                binding.emptyView.visibility = android.view.View.VISIBLE
                return
            }
            Toast.makeText(
                this,
                R.string.blocked_apps_cleared,
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        binding.list.visibility = android.view.View.VISIBLE
        binding.emptyView.visibility = android.view.View.GONE
        items.clear()
        items.addAll(found)
        adapter.notifyDataSetChanged()
    }

    private fun openAppDetails(packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
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

    private inner class BlockedAdapter : RecyclerView.Adapter<BlockedViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedViewHolder {
            val itemBinding = LayoutBlockedAppsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return BlockedViewHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: BlockedViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }

    private inner class BlockedViewHolder(
        private val itemBinding: LayoutBlockedAppsItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(app: BlockedApp) {
            itemBinding.title.text = app.label
            itemBinding.desc.text = app.packageName
            if (app.icon != null) {
                itemBinding.itemicon.setImageDrawable(app.icon)
            } else {
                itemBinding.itemicon.setImageResource(android.R.drawable.sym_def_app_icon)
            }
            itemBinding.root.setOnClickListener {
                openAppDetails(app.packageName)
            }
        }
    }
}
