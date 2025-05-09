package com.tai.taichungattractioninformation.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.tai.taichungattractioninformation.R
import com.tai.taichungattractioninformation.models.RestaurantDataResponseItem
import com.tai.taichungattractioninformation.viewmodels.FlowerAndAttractionViewModel

class RestaurantFragment : Fragment() {
    private lateinit var viewModel: FlowerAndAttractionViewModel
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FlowerAndAttractionViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()

        // 取得餐廳資料
        viewModel.getRestaurantData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Fragment 使用 Compose 需使用 ComposeView 來顯示
        return ComposeView(requireContext()).apply {
            setContent {
                val restaurantData by viewModel.restaurantState.collectAsState()

                RestaurantScreen(restaurantData, viewModel, ::getCurrentLocation)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // ⚠️ 權限取得後再重新載入畫面（或執行特定定位邏輯）
            Toast.makeText(requireContext(), "定位權限已開啟，請重新點擊路線規劃", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "請開啟定位權限以使用路線規劃功能", Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentLocation(context: Context, onLocationReady: (Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ⭐ 請求權限
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationReady(location)
                } else {
                    Toast.makeText(context, "無法取得定位", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "定位失敗: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RestaurantScreen(
    restaurantDataItem: List<RestaurantDataResponseItem>,
    viewModel: FlowerAndAttractionViewModel,
    getCurrentLocation: (Context, (Location) -> Unit) -> Unit
) {
    val context = LocalContext.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        val searchKeyword by viewModel.searchKeyword.collectAsState()
        val filteredData by viewModel.getRestaurantFilteredData().collectAsState()

        var expanded by remember { mutableStateOf(false) }
        val restaurantName = restaurantDataItem.map { it.name }.distinct().filter { it.isNotEmpty() }

        Column(modifier = Modifier.fillMaxSize()) {
            // 搜尋欄
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = { viewModel.updateSearchKeyword(it) },
                label = { Text( text = stringResource(id = R.string.label_search_restaurant)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "選擇餐廳")
                    }
                },
                singleLine = true
            )

            // 花種選單
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                restaurantName.forEach { flower ->
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
                            if (item.name.isNotEmpty()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_name,
                                            item.name
                                        ),
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_tel,
                                            item.tel
                                        ),
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = stringResource(R.string.label_google_road_plan),
                                        color = Color.Blue,
                                        fontSize = 18.sp,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .clickable {
                                                getCurrentLocation(context) { userLocation ->
                                                    val gmmIntentUri = Uri.parse("google.navigation:q=${item.wsgY},${item.wsgX}&mode=d")
                                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                                        setPackage("com.google.android.apps.maps")
                                                    }

                                                    if (mapIntent.resolveActivity(context.packageManager) != null) {
                                                        context.startActivity(mapIntent)
                                                    } else {
                                                        Toast.makeText(context, "未安裝 Google 地圖 App", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                    )
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_intro,
                                            item.introduction
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
