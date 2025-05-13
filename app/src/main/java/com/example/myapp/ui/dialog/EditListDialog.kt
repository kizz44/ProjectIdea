package com.example.myapp.ui.dialog

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.myapp.R
import com.example.myapp.data.model.ShoppingList
import com.example.myapp.databinding.DialogEditListBinding

class EditListDialog(
    private val context: Context,
    private val list: ShoppingList? = null
) {
    fun show(onSave: (name: String) -> Unit) {
        val binding = DialogEditListBinding.inflate(LayoutInflater.from(context))
        
        // Настраиваем поле ввода для поддержки русского языка
        binding.editTextListName.inputType = InputType.TYPE_CLASS_TEXT
        
        // Заполняем поле, если редактируем существующий список
        list?.let {
            binding.editTextListName.setText(it.name)
        }

        AlertDialog.Builder(context)
            .setTitle(if (list == null) R.string.new_list else R.string.rename_list)
            .setView(binding.root)
            .setPositiveButton(if (list == null) R.string.add else R.string.save) { _, _ ->
                val name = binding.editTextListName.text.toString()
                if (name.isNotBlank()) {
                    onSave(name)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .apply {
                // Показываем диалог
                show()
                
                // Открываем клавиатуру
                binding.editTextListName.requestFocus()
            }
    }
} 