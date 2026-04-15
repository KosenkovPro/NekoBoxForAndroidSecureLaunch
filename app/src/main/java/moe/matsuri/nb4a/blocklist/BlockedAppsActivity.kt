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

    private lateinit var binding: LayoutBlockedAppsBinding
    private val adapter = BlockedAdapter()
    private val items = mutableListOf<BlockedApp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBlockedAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setTitle(R.string.blocked_apps_title)
            setDisplayHomeAsUpEnabled(true)
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
            Toast.makeText(
                this,
                R.string.blocked_apps_cleared,
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

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
