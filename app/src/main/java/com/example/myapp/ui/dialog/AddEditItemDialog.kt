package com.example.myapp.ui.dialog

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.myapp.R
import com.example.myapp.data.model.ShoppingItem
import com.example.myapp.databinding.DialogEditItemBinding

class AddEditItemDialog(
    private val context: Context,
    private val item: ShoppingItem? = null
) {
    fun show(onSave: (name: String, quantity: Int, note: String) -> Unit) {
        val binding = DialogEditItemBinding.inflate(LayoutInflater.from(context))
        
        // Настраиваем поля ввода программно
        binding.editTextItemName.inputType = InputType.TYPE_CLASS_TEXT
        binding.editTextQuantity.inputType = InputType.TYPE_CLASS_NUMBER
        binding.editTextNote.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        
        // Заполняем поля, если редактируем существующий товар
        item?.let {
            binding.editTextItemName.setText(it.name)
            binding.editTextQuantity.setText(it.quantity.toString())
            binding.editTextNote.setText(it.note)
        }

        AlertDialog.Builder(context)
            .setTitle(if (item == null) R.string.add_item else R.string.edit_item)
            .setView(binding.root)
            .setPositiveButton(if (item == null) R.string.add else R.string.save) { _, _ ->
                val name = binding.editTextItemName.text.toString()
                val quantity = binding.editTextQuantity.text.toString().toIntOrNull() ?: 1
                val note = binding.editTextNote.text.toString()

                if (name.isNotBlank()) {
                    onSave(name, quantity, note)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .apply {
                // Показываем диалог
                show()
                
                // Открываем клавиатуру
                binding.editTextItemName.requestFocus()
            }
    }
} 