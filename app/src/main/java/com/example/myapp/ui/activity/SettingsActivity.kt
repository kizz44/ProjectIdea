package com.example.myapp.ui.activity

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapp.R
import com.example.myapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupThemeSelection()
        setupVersionInfo()
    }

    private fun setupActionBar() {
        val toolbar = binding.appBar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.settings)
        }
    }

    private fun setupThemeSelection() {
        val radioGroup = findViewById<RadioGroup>(R.id.themeRadioGroup)
        val rbLight = findViewById<RadioButton>(R.id.rbLight)
        val rbDark = findViewById<RadioButton>(R.id.rbDark)

        // Устанавливаем текущую тему
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> rbLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> rbDark.isChecked = true
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLight -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.rbDark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun setupVersionInfo() {
        val versionTextView = findViewById<TextView>(R.id.versionTextView)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        versionTextView.text = getString(R.string.version_format, versionName, 
            packageManager.getPackageInfo(packageName, 0).longVersionCode.toInt())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 