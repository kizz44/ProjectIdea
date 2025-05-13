package com.example.myapp.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.R
import com.example.myapp.data.model.ShoppingList
import com.example.myapp.databinding.ActivityShoppingListsBinding
import com.example.myapp.databinding.DialogEditListBinding
import com.example.myapp.ui.adapter.ShoppingListAdapter
import com.example.myapp.ui.dialog.EditListDialog
import com.example.myapp.ui.viewmodel.ShoppingListViewModel

class ShoppingListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListsBinding
    private lateinit var adapter: ShoppingListAdapter
    private val viewModel: ShoppingListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupRecyclerView()
        setupViewModel()
        setupFab()
    }

    private fun setupActionBar() {
        val toolbar = binding.appBar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.shopping_lists)
        }
    }

    private fun setupRecyclerView() {
        adapter = ShoppingListAdapter(
            onItemClick = { list ->
                startActivity(ShoppingListDetailActivity.createIntent(this, list.id, list.name))
            },
            onItemLongClick = { list ->
                showListOptionsDialog(list)
            },
            onDeleteClick = { list ->
                showDeleteListDialog(list)
            }
        )

        binding.recyclerViewLists.apply {
            layoutManager = LinearLayoutManager(this@ShoppingListsActivity)
            adapter = this@ShoppingListsActivity.adapter
        }
    }

    private fun setupViewModel() {
        viewModel.allLists.observe(this) { lists ->
            adapter.submitList(lists)
        }
    }

    private fun setupFab() {
        binding.fabAddList.setOnClickListener {
            showCreateListDialog()
        }
    }

    private fun showCreateListDialog() {
        EditListDialog(this).show { name ->
            viewModel.insertList(ShoppingList(name = name))
        }
    }

    private fun showListOptionsDialog(list: ShoppingList) {
        val options = arrayOf(
            getString(R.string.rename_list),
            getString(R.string.delete_list)
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.list_options)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showRenameListDialog(list)
                    1 -> showDeleteListDialog(list)
                }
            }
            .show()
    }

    private fun showRenameListDialog(list: ShoppingList) {
        EditListDialog(this, list).show { name ->
            viewModel.updateList(list.copy(name = name))
        }
    }

    private fun showDeleteListDialog(list: ShoppingList) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_list)
            .setMessage(getString(R.string.delete_list_confirmation, list.name))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteList(list)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 