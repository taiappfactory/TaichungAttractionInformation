package com.tai.taichungattractioninformation.ui

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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.tai.taichungattractioninformation.R
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel

class MainActivity : FragmentActivity() {
    private val containerId = View.generateViewId()
    private lateinit var viewModel: FlowerAndAttractionViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[FlowerAndAttractionViewModel::class.java]

        setContent {
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val language by viewModel.languageState.collectAsState()

            Scaffold(
                topBar = {
                    Column {
                        TopAppBar(
                            title = { if (language == "zh") Text("台中樂遊") else Text("TaiChung fun day") },
                            actions = {
                                TextButton(
                                    onClick = { viewModel.toggleLanguage() },
                                    modifier = Modifier.padding(end = 8.dp),
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = Color(0xFFD4B8B4)
                                    )
                                ) {
                                    Text(text = if (language == "zh") "English" else "中文",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF8E6E57), // 背景顏色
                                titleContentColor = MaterialTheme.colorScheme.onPrimary, // 標題文字顏色
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary // actions 內容顏色
                            )
                        )

                        TabRow(selectedTabIndex = selectedTabIndex) {
                            Tab(
                                selected = selectedTabIndex == 0,
                                onClick = {
                                    selectedTabIndex = 0
                                    navigateToTabFragment(R.id.fragment_flower)
                                },
                                text = { if (language == "zh") Text("賞花") else Text("Flower") }
                            )
                            Tab(
                                selected = selectedTabIndex == 1,
                                onClick = {
                                    selectedTabIndex = 1
                                    navigateToTabFragment(R.id.fragment_attraction)
                                },
                                text = { if (language == "zh") Text("海域遊憩景點") else Text("Attraction") }
                            )
                        }
                    }
                }
            ) { padding ->
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

    private fun navigateToTabFragment(@IdRes destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(containerId) as? NavHostFragment
        val navController = navHostFragment?.navController
        navController?.navigate(destinationId)
    }
}
