package com.example.colorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random
import androidx.compose.runtime.collectAsState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorAppScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorAppScreen(viewModel: ColorViewModel = viewModel()) {
    val colorList = viewModel.colorList
    //val pendingSyncCount by viewModel.pendingSyncCount.collectAsState()
    val pendingSyncCount = viewModel.pendingSyncCount.value // Accessing the value directly


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(title = { Text("Color App") }, actions = {
            Text(
                "$pendingSyncCount",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { viewModel.syncColors() },
                fontSize = 18.sp
            )
            IconButton(onClick = { viewModel.syncColors() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync"
                )
            }
        })

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f)
        ) {
            items(colorList.size) { index ->
                ColorCard(colorList[index])
            }
        }

        Button(
            onClick = { viewModel.addColor() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {
            Text("Add Color")
        }
    }
}

@Composable
fun ColorCard(colorItem: ColorItem) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(android.graphics.Color.parseColor(colorItem.color)),
                shape = RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(colorItem.color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Created at\n${colorItem.createdAt}", fontSize = 12.sp)
        }
    }
}

// ViewModel
class ColorViewModel : ViewModel() {
    private val _colorList = mutableStateListOf<ColorItem>()
    val colorList = _colorList

    private val _pendingSyncCount = mutableStateOf(0)
    val pendingSyncCount = _pendingSyncCount

    fun addColor() {
        val randomColor = String.format("#%06X", 0xFFFFFF and Random.nextInt())
        _colorList.add(ColorItem(randomColor, "${System.currentTimeMillis()}"))
        _pendingSyncCount.value++
    }

    fun syncColors() {
        _pendingSyncCount.value = 0
        // Implement Firebase or Google Sheets sync here
    }
}

// Data Model
data class ColorItem(val color: String, val createdAt: String)
