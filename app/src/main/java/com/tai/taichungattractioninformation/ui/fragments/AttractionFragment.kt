package com.tai.taichungattractioninformation.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.tai.taichungattractioninformation.R
import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.ui.WebViewActivity
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
        // Fragment 使用 Compose 需使用 ComposeView 來顯示
        return ComposeView(requireContext()).apply {
            setContent {
                val attractionData by viewModel.attractionState.collectAsState()
                val language by viewModel.languageState.collectAsState()


                AttractionScreen(attractionData, language) // 你自己的 Composable Function
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AttractionScreen(attractionDataItem: List<AttractionDataResponseItem>, language: String) {
    val context = LocalContext.current

    LazyColumn {
        items(attractionDataItem) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBFADA1),
                    contentColor = Color.White
                )
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxHeight()) {
                    AsyncImage(
                        model = item.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    val name = if (language == "zh") item.name else item.englishName
                    val intro = if (language == "zh") item.introChinese else item.introEnglish
                    val address = if (language == "zh") item.address else item.englishAddress
                    val openTimeNote = if (language == "zh") item.openTimeNote else item.openTimeEnglishNote

                    Text(stringResource(R.string.label_name, name), fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.label_intro, intro), fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.label_tel, item.tel), fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.label_address, address), fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.label_open_time, item.openTime), fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(stringResource(R.string.label_open_time_note, openTimeNote), fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))

                    // 景點官網
                    Text(
                        text = stringResource(R.string.label_official_web),
                        color = Color.Blue,
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                val intent = Intent(context, WebViewActivity::class.java)
                                intent.putExtra("url", item.url)
                                context.startActivity(intent)
                            }
                    )


                    Spacer(Modifier.height(8.dp))

                    // Google 地圖
                    Text(
                        text = stringResource(R.string.label_google_map),
                        color = Color.Blue,
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.mapServiceUrl))
                                context.startActivity(intent)
                            }
                    )

                }
            }
        }
    }
}