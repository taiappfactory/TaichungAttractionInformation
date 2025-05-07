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
import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel

class AttractionFragment : Fragment() {
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
        viewModel.getAttractionData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val attractionData by viewModel.attractionState.collectAsState()

                AttractionScreen(attractionData) // 你自己的 Composable Function
            }
        }
    }
}


@Composable
fun AttractionScreen(flowerDataItem: List<AttractionDataResponseItem>) {
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
                            model = item.photoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(150.dp)
                                .height(150.dp)
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "景點中文名稱：${item.name}", fontSize = 18.sp)
                        Text(text = "景點英文名稱：${item.englishName}", fontSize = 18.sp)
                        Text(text = "景點特色簡述(中文)：${item.introChinese}", fontSize = 18.sp)
                        Text(text = "景點特色簡述(英文)：${item.introEnglish}", fontSize = 18.sp)
                        Text(text = "景點服務電話：${item.tel}", fontSize = 18.sp)
                        Text(text = "景點中文地址：${item.address}", fontSize = 18.sp)
                        Text(text = "景點英文地址：${item.englishAddress}", fontSize = 18.sp)
                        Text(text = "開放時間：${item.openTime}", fontSize = 18.sp)
                        Text(text = "開放時間備註：${item.openTimeNote}", fontSize = 18.sp)
                        Text(text = "開放時間英文備註：${item.openTimeEnglishNote}", fontSize = 18.sp)
                        Text(text = "網址：${item.url}", fontSize = 18.sp)
                        Text(text = "地圖服務連結：${item.mapServiceUrl}", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}