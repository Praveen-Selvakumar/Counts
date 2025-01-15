package com.example.counts.Test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class ScreenTest : ComponentActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnDynamicExample()
        }
    }


    @Composable
    fun LazyColumnDynamicExample() {
        // Mutable list managed using remember
        val items = remember { mutableStateListOf("Item 1", "Item 2", "Item 3") }

        Column {
            Button(
                onClick = { items.add("New Item ${items.size + 1}") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Add Item")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                /*items(items) { item ->
                    ListItem(item = item)
                }*/
            }
        }
    }

}