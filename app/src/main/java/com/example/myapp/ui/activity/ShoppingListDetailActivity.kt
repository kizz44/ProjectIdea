package com.example.myapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.MainActivity
import com.example.myapp.R
import com.example.myapp.data.model.ShoppingItem
import com.example.myapp.databinding.ActivityShoppingListDetailBinding
import com.example.myapp.ui.adapter.ShoppingItemAdapter
import com.example.myapp.ui.dialog.AddEditItemDialog
import com.example.myapp.ui.viewmodel.ShoppingListViewModel

class ShoppingListDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListDetailBinding
    private lateinit var adapter: ShoppingItemAdapter
    private val viewModel: ShoppingListViewModel by viewModels()
    private var listId: Int = -1
    private var itemsObserver: Observer<List<ShoppingItem>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        listId = intent.getIntExtra(EXTRA_LIST_ID, -1)
        if (listId == -1) {
            finish()
            return
        }

        setupRecyclerView()
        setupViewModel()
        setupFab()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Удаляем observer при уничтожении активности
        itemsObserver?.let { observer ->
            viewModel.getItemsForList(listId).removeObserver(observer)
        }
    }

    private fun setupActionBar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = intent.getStringExtra(EXTRA_LIST_NAME) ?: getString(R.string.app_name)
        }
    }

    private fun setupRecyclerView() {
        adapter = ShoppingItemAdapter(
            onItemChecked = { item ->
                viewModel.updateItem(item)
            },
            onItemDeleted = { item ->
                showDeleteItemDialog(item)
            }
        )

        binding.recyclerViewItems.apply {
            layoutManager = LinearLayoutManager(this@ShoppingListDetailActivity)
            adapter = this@ShoppingListDetailActivity.adapter
        }
    }

    private fun setupViewModel() {
        viewModel.getItemsForList(listId).observe(this) { items ->
            adapter.submitList(items)
        }
    }

    private fun setupFab() {
        binding.fabAddItem.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun showAddItemDialog() {
        AddEditItemDialog(this).show { name, quantity, note ->
            val item = ShoppingItem(
                listId = listId,
                name = name,
                quantity = quantity,
                note = note
            )
            viewModel.insertItem(item)
        }
    }

    private fun showEditItemDialog(item: ShoppingItem) {
        AddEditItemDialog(this, item).show { name, quantity, note ->
            viewModel.updateItem(item.copy(
                name = name,
                quantity = quantity,
                note = note
            ))
        }
    }

    private fun showDeleteItemDialog(item: ShoppingItem) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_item)
            .setMessage(getString(R.string.delete_item_confirmation, item.name))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteItem(item)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_shopping_list_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_home -> {
                // Clear back stack and return to main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
                true
            }
            R.id.action_share -> {
                shareShoppingList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareShoppingList() {
        val listName = intent.getStringExtra(EXTRA_LIST_NAME) ?: return
        val items = viewModel.getItemsForListSync(listId)
        
        val shareText = buildString {
            appendLine(listName.uppercase())
            appendLine()
            
            val uncheckedItems = items.filterNot { it.isChecked }
            val checkedItems = items.filter { it.isChecked }

            if (uncheckedItems.isNotEmpty()) {
                appendLine("Нужно купить:")
                uncheckedItems.forEach { item ->
                    append("☐ ${item.name}")
                    if (item.quantity > 1) append(" x${item.quantity}")
                    if (item.note.isNotBlank()) append(" (${item.note})")
                    appendLine()
                }
                appendLine()
            }

            if (checkedItems.isNotEmpty()) {
                appendLine("Уже куплено:")
                checkedItems.forEach { item ->
                    append("☑ ${item.name}")
                    if (item.quantity > 1) append(" x${item.quantity}")
                    if (item.note.isNotBlank()) append(" (${item.note})")
                    appendLine()
                }
            }
        }

        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, listName)
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_list)))
        } catch (e: Exception) {
            Log.e("DetailActivity", "Error sharing list", e)
            Toast.makeText(this, R.string.error_sharing_list, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_LIST_ID = "extra_list_id"
        const val EXTRA_LIST_NAME = "extra_list_name"

        fun createIntent(context: Context, listId: Int, listName: String): Intent {
            return Intent(context, ShoppingListDetailActivity::class.java).apply {
                putExtra(EXTRA_LIST_ID, listId)
                putExtra(EXTRA_LIST_NAME, listName)
            }
        }
    }
} 