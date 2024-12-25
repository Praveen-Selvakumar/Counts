package com.example.counts.Test

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.counts.R
import com.example.counts.local_db.RoomDB
import com.example.counts.pages.AddExpense

class ScreenTest {
    companion object {

        @Composable
        fun pageContent(context: Context) {
            //ADD DATA
            var db = RoomDB.getInstance(context)
            var dao = db?.userDao()

            var expenseList = dao?.getExpense()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(id = R.color.white)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(color = Color.Green)
                    ) {

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(color = Color.White)
                    ) {

                    }
                }


                IconButton(onClick = {
                    context.startActivity(Intent(context, AddExpense::class.java))
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }



                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 60.dp,
                        end = 16.dp,
                        bottom = 60.dp
                    )
                ) {
                    expenseList?.size?.let {
                        items(it) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                elevation = CardDefaults.cardElevation(5.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 15.dp, start = 15.dp)
                                    ) {
                                        Text(
                                            text = "`10/09/2024", color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            style = androidx.compose.ui.text.TextStyle(
                                                fontSize = 15.sp,
                                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                                            )
                                        )
                                    }
                                    Row {
                                        Text(
                                            text = " Why : ${expenseList.get(index = it).why?.let { it }}  \n How Much : \u20B9 ${
                                                expenseList.get(
                                                    index = it
                                                ).amount
                                            }", Modifier.padding(20.dp)
                                        )
                                    }
                                }
                            }

                        }
                    }
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(20.dp)
                        .align(alignment = Alignment.BottomCenter),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {

                }

            }
        }

        @Composable
        private fun BottomCard() {

        }

    }
}