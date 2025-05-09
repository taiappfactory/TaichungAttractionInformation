package com.tai.taichungattractioninformation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tai.taichungattractioninformation.R
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel

class FlowerFragment : Fragment() {
    private lateinit var viewModel: FlowerAndAttractionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FlowerAndAttractionViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()

        // 取得花景區資料
        viewModel.getFlowerData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Fragment 使用 Compose 需使用 ComposeView 來顯示
        return ComposeView(requireContext()).apply {
            setContent {
                val flowerData by viewModel.flowerState.collectAsState()

                FlowerScreen(flowerData, viewModel) // 你自己的 Composable Function
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FlowerScreen(flowerDataItem: List<FlowerDataResponseItem>, viewModel: FlowerAndAttractionViewModel) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val searchKeyword by viewModel.searchKeyword.collectAsState()
        val filteredData by viewModel.getFilteredData().collectAsState()

        var expanded by remember { mutableStateOf(false) }
        val flowerTypes = flowerDataItem.map { it.flowerType }.distinct().filter { it.isNotEmpty() }

        Column(modifier = Modifier.fillMaxSize()) {
            // 搜尋欄
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = { viewModel.updateSearchKeyword(it) },
                label = { Text("搜尋花種") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "選擇花種")
                    }
                },
                singleLine = true
            )

            // 花種選單
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                flowerTypes.forEach { flower ->
                    DropdownMenuItem(
                        text = { Text(flower) },
                        onClick = {
                            viewModel.updateSearchKeyword(flower)
                            expanded = false
                        }
                    )
                }
            }

            LazyColumn {
                items(filteredData) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFBFADA1),
                            contentColor = Color.White
                        )
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            if (item.flowerType.isNotEmpty()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_flower_type,
                                            item.flowerType
                                        ),
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_location,
                                            item.location
                                        ),
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_area,
                                            item.city,
                                            item.district
                                        ),
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_viewing_period,
                                            item.viewingPeriod
                                        ),
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
