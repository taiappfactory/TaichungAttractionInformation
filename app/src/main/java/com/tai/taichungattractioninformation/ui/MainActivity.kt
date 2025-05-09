package com.tai.taichungattractioninformation.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.annotation.IdRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.tai.taichungattractioninformation.R
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel
import java.util.Locale

class MainActivity : FragmentActivity() {
    private val containerId = View.generateViewId()
    private lateinit var viewModel: FlowerAndAttractionViewModel

    override fun onResume() {
        super.onResume()

        viewModel.languageState.value.let { lang ->
            updateLocale(this, lang)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FlowerAndAttractionViewModel::class.java]

        setContent {
            val language by viewModel.languageState.collectAsState()
            MainScreen(viewModel, language, containerId) { navigateToTabFragment(it) }
        }
    }

    private fun navigateToTabFragment(@IdRes destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(containerId) as? NavHostFragment
        val navController = navHostFragment?.navController
        navController?.navigate(destinationId)
    }

    // 動態切換 App 的語言（Locale）設定
    private fun updateLocale(context: Context, language: String) {
        val locale = Locale(language) // 建立新的 Locale 物件，例如 "en"、"zh"、"ja"
        Locale.setDefault(locale)     // 設定新的預設語言，影響全域資源取得

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)      // 將新語言設到目前的 Configuration 上

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        // 更新 context 的資源設定，讓 UI 馬上反映語言變更
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: FlowerAndAttractionViewModel,
    language: String,
    containerId: Int,
    navigateToTabFragment: (Int) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }  // 使用 var，這樣才能重新賦值

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar(
                                title = { Text(stringResource(id = R.string.app_title)) },
                                actions = {
                                    TextButton(
                                        onClick = { viewModel.toggleLanguage() },
                                        modifier = Modifier.padding(end = 8.dp),
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = Color(0xFFD4B8B4)
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(
                                                id = if (language == "zh") R.string.switch_to_english
                                                else R.string.switch_to_chinese
                                            ),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color(0xFF8E6E57),
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )

                            TabRow(selectedTabIndex = selectedTabIndex) {
                                Tab(
                                    selected = selectedTabIndex == 0,
                                    onClick = {
                                        selectedTabIndex = 0
                                        navigateToTabFragment(R.id.fragment_flower)  // 導航到花卉 Fragment
                                    },
                                    text = { Text(stringResource(id = R.string.tab_flower)) }
                                )
                                Tab(
                                    selected = selectedTabIndex == 1,
                                    onClick = {
                                        selectedTabIndex = 1
                                        navigateToTabFragment(R.id.fragment_attraction)  // 導航到景點 Fragment
                                    },
                                    text = { Text(stringResource(id = R.string.tab_attraction)) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    // Compose 使用 Fragment 需使用 AndroidView 來顯示
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        factory = { context ->
                            FragmentContainerView(context).apply {
                                id = containerId
                            }
                        },
                        update = { view ->
                            val fragmentManager = (view.context as FragmentActivity).supportFragmentManager
                            val existingFragment = fragmentManager.findFragmentById(view.id)
                            if (existingFragment == null) {
                                val navHostFragment = NavHostFragment.create(R.navigation.nav_graph)
                                fragmentManager.beginTransaction()
                                    .replace(view.id, navHostFragment)
                                    .setPrimaryNavigationFragment(navHostFragment)
                                    .commitNow()
                            }
                        }
                    )
                }
            }
        }
    }
}