package com.tai.taichungattractioninformation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
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
        return ComposeView(requireContext()).apply {
            setContent {
                val flowerData by viewModel.flowerState.collectAsState()
                val language by viewModel.languageState.collectAsState()

                FlowerScreen(flowerData, language) // 你自己的 Composable Function
            }
        }
    }
}


@Composable
fun FlowerScreen(flowerDataItem: List<FlowerDataResponseItem>, language: String) {
    // 使用 LazyColumn 顯示資料
    LazyColumn {
        items(flowerDataItem) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF8E6E57),
                    contentColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val boxHeight = this.maxHeight

                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(boxHeight)
                                .height(boxHeight)
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (language == "zh") {
                            Text(text = "花種：${item.flowerType}", fontSize = 18.sp)
                            Text(text = "地點：${item.location}", fontSize = 18.sp)
                            Text(text = "區域：${item.city} ${item.district}", fontSize = 18.sp)
                            Text(text = "觀賞期：${item.viewingPeriod}", fontSize = 18.sp)
                        } else {
                            Text(text = "Flower Type：${item.flowerType}", fontSize = 18.sp)
                            Text(text = "Location：${item.location}", fontSize = 18.sp)
                            Text(text = "Area：${item.city} ${item.district}", fontSize = 18.sp)
                            Text(text = "Viewing Period：${item.viewingPeriod}", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}