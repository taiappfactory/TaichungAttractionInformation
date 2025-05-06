package com.tai.taichungattractioninformation.ui

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.annotation.IdRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import com.tai.taichungattractioninformation.R

class MainActivity : FragmentActivity() {
    private val containerId = View.generateViewId()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var selectedTabIndex by remember { mutableIntStateOf(0) }

            Scaffold(
                topBar = {
                    Column {
                        TopAppBar(title = { Text("台中資訊") })
                        TabRow(selectedTabIndex = selectedTabIndex) {
                            Tab(
                                selected = selectedTabIndex == 0,
                                onClick = {
                                    selectedTabIndex = 0
                                    navigateToTabFragment(R.id.fragment_flower)
                                },
                                text = { Text("賞花") }
                            )
                            Tab(
                                selected = selectedTabIndex == 1,
                                onClick = {
                                    selectedTabIndex = 1
                                    navigateToTabFragment(R.id.fragment_attraction)
                                },
                                text = { Text("海域遊憩景點") }
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
