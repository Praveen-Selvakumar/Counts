package com.example.counts.pages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.counts.R
import com.example.counts.local_db.RoomDB

class ExpenseList : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            pageContent(this@ExpenseList);


        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBar() {
        SmallTopAppBar(
            colors = TopAppBarDefaults.smallTopAppBarColors(
                colorResource(id = R.color.light_blue)
            ),
            title = {
                Text(
                    "Expense List",
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    maxLines = 1,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    startActivity(
                        Intent(
                            this@ExpenseList,
                            AddExpense::class.java
                        )
                    )
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back_arrow),
                        contentDescription = "Localized description",
                    )
                }
            },
        )
    }


    @Composable
    fun pageContent(context: Context) {
        var totalCount = 0.0f;
        var debitedAmount = 0.0f;
        var creditedAmount = 0.0f;
        //ADD DATA
        var db = RoomDB.getInstance(context)
        var dao = db?.userDao()

        var expenseList = dao?.getExpense()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(id = R.color.light_blue)
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
                        .background(color = colorResource(id = R.color.light_blue))
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
                Toast.makeText(this@ExpenseList, "Expense List IS", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@ExpenseList, AddExpense::class.java))
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            topAppBar()

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 60.dp,
                    end = 16.dp,
                    bottom = 100.dp
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
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(alignment = Alignment.CenterHorizontally)
                                ) {
                                    Text(
                                        text = expenseList?.get(it)?.currentDate.toString(),
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        style = androidx.compose.ui.text.TextStyle(
                                            fontSize = 15.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_regular))
                                        )
                                    )
                                }
                                Column(
                                    modifier = Modifier.padding(
                                        15.dp
                                    )
                                ) {
                                    var type =
                                        if (expenseList.get(it).action == 0) "Debited from" else "Credited to"

                                    Text(text = " Why : ${expenseList.get(index = it).why?.let { it }}")
                                    Spacer(Modifier.height(2.5.dp))
                                    Text(text = "  How Much :  ₹ ${expenseList.get(it).amount}  is ${type} your account")
                                }

                            }
                        }

                    }
                }
            }

            dao?.getExpense()?.let { list ->
                for (amount_ in list) {
                    totalCount = totalCount + amount_.amount
                    if (amount_.action == 1) {
                        creditedAmount += amount_.amount
                    } else {
                        debitedAmount += amount_.amount
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
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        var debits = "<b>Debited amount</b> : ${debitedAmount.toString()}"
                        var credits =
                            "<b>Credited amount</b> : ${creditedAmount.toString()}"
                        Text(
                            text = Html.fromHtml(debits + " " + credits).toString(),
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        )

                    }


                }
            }

        }
    }

}

