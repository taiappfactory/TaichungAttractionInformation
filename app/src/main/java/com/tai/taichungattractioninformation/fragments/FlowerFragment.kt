package com.tai.taichungattractioninformation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tai.taichungattractioninformation.R
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel

class FlowerFragment : Fragment() {
    val viewModel by viewModels<FlowerAndAttractionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                FlowerScreen(flowerData) // 你自己的 Composable Function
            }
        }
    }
}


@Composable
fun FlowerScreen(flowerData: List<FlowerDataResponseItem>) {
    // 使用 LazyColumn 顯示資料
    LazyColumn {
        Log.d("taitest", "flowerData: $flowerData")
        items(flowerData) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        val boxHeight = this.maxHeight

                        Image(
                            painter = painterResource(id = R.drawable.square),
                            contentDescription = null,
                            modifier = Modifier
                                .height(boxHeight)
                                .width(boxHeight) // 寬度設為高度，達成正方形
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "花種：${item.flowerType}", fontSize = 18.sp)
                        Text(text = "地點：${item.location}", fontSize = 18.sp)
                        Text(text = "區域：${item.city} ${item.district}", fontSize = 18.sp)
                        Text(text = "觀賞期：${item.viewingPeriod}", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}