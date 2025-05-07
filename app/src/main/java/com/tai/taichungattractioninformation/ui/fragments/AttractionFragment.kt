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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.ui.WebViewActivity
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel
import androidx.core.net.toUri

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
                val language by viewModel.languageState.collectAsState()


                AttractionScreen(attractionData, language) // 你自己的 Composable Function
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AttractionScreen(flowerDataItem: List<AttractionDataResponseItem>, language: String) {
    // 使用 LazyColumn 顯示資料
    LazyColumn {
        items(flowerDataItem) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFBFADA1),
                    contentColor = Color.White
                )
            ) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxHeight()
                ) {
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
                    val context = LocalContext.current

                    if (language == "zh") {
                        Text(text = "景點名稱：${item.name}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "景點特色簡述：${item.introChinese}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "景點服務電話：${item.tel}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "景點地址：${item.address}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "開放時間：${item.openTime}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "開放時間備註：${item.openTimeNote}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        // 景點官網文字與 URL
                        val annotatedLinkString = buildAnnotatedString {
                            val url = item.url
                            append("前往景點官網")
                            addStyle(
                                style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                                start = 0,
                                end = this.length
                            )
                            addStringAnnotation(tag = "URL", annotation = url, start = 0, end = this.length)
                        }

                        val annotatedMapString = buildAnnotatedString {
                            val mapUrl = item.mapServiceUrl
                            append("Google地圖")
                            addStyle(
                                style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                                start = 0,
                                end = this.length
                            )
                            addStringAnnotation(tag = "MAP", annotation = mapUrl, start = 0, end = this.length)
                        }

                        Text(
                            text = annotatedLinkString,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable {
                                    annotatedLinkString
                                        .getStringAnnotations("URL", 0, annotatedLinkString.length)
                                        .firstOrNull()?.let { annotation ->
                                            val intent = Intent(context, WebViewActivity::class.java)
                                            intent.putExtra("url", annotation.item)
                                            context.startActivity(intent)
                                        }
                                },
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = annotatedMapString,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable {
                                    annotatedMapString
                                        .getStringAnnotations("MAP", 0, annotatedMapString.length)
                                        .firstOrNull()?.let { annotation ->
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                            context.startActivity(intent)
                                        }
                                },
                            fontSize = 18.sp
                        )

                    } else {
                        Text(text = "Name：${item.englishName}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Introduction：${item.introEnglish}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Tel：${item.tel}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Address：${item.englishAddress}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Open Time：${item.openTime}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Open Time Note：${item.openTimeEnglishNote}", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        // 景點官網文字與 URL
                        val annotatedLinkString = buildAnnotatedString {
                            val url = item.url
                            append("Official Web")
                            addStyle(
                                style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                                start = 0,
                                end = this.length
                            )
                            addStringAnnotation(tag = "URL", annotation = url, start = 0, end = this.length)
                        }

                        val annotatedMapString = buildAnnotatedString {
                            val mapUrl = item.mapServiceUrl
                            append("GoogleMap")
                            addStyle(
                                style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline),
                                start = 0,
                                end = this.length
                            )
                            addStringAnnotation(tag = "MAP", annotation = mapUrl, start = 0, end = this.length)
                        }

                        Text(
                            text = annotatedLinkString,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable {
                                    annotatedLinkString
                                        .getStringAnnotations("URL", 0, annotatedLinkString.length)
                                        .firstOrNull()?.let { annotation ->
                                            val intent = Intent(context, WebViewActivity::class.java)
                                            intent.putExtra("url", annotation.item)
                                            context.startActivity(intent)
                                        }
                                },
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = annotatedMapString,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable {
                                    annotatedMapString
                                        .getStringAnnotations("MAP", 0, annotatedMapString.length)
                                        .firstOrNull()?.let { annotation ->
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                annotation.item.toUri())
                                            context.startActivity(intent)
                                        }
                                },
                            fontSize = 18.sp
                        )

                    }
                }
            }
        }
    }
}