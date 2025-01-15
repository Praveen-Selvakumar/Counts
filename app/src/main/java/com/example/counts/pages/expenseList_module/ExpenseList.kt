package com.example.counts.pages.expenseList_module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.counts.R
import com.example.counts.local_db.CountModel
import com.example.counts.local_db.RoomDB
import com.example.counts.pages.expense_module.AddExpense


class ExpenseList : ComponentActivity() {

    var viewModel: ExpenseListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            pageContent(this@ExpenseList)
        }
        initView()

    }

    fun initView() {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this).get(ExpenseListViewModel::class.java)
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBar() {
        TopAppBar(
            title = {
                Text(text = "Expense List", style = TextStyle(Color.White, fontSize = 18.sp))
            },
            navigationIcon = {
                IconButton(onClick = {
                    navigateUpTo(Intent(this@ExpenseList, AddExpense::class.java))
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow), // Replace with your drawable resource
                        contentDescription = "Menu Icon"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Green // Background color
            )

        )
    }


    @Composable
    fun pageContent(context: Context) {
        var totalCount = 0.0f;
        var debitedAmount = 0.0f;
        var creditedAmount = 0.0f;
        //ADD DATA
        val db = RoomDB.getInstance(context) as RoomDB
        var dao = db.userDao()

        val mainList = mutableStateListOf<List<CountModel>>(dao.getExpense())



        Column {
            topAppBar()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = colorResource(id = R.color.white)
                    )
            ) {

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 60.dp,
                        end = 16.dp,
                        bottom = 100.dp
                    )
                ) {

                    viewModel?.getExpenseList(dao)
                        ?.observe(this@ExpenseList, Observer { countsList ->
                            countsList?.let { list ->
                                if (!list.isNullOrEmpty()) {
                                    list.size.run {
                                        items(this) { i ->
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

                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(top = 5.dp),
                                                            horizontalArrangement = Arrangement.SpaceEvenly
                                                        ) {
                                                            Text(
                                                                text = list.get(i).currentDate.toString(),
                                                                modifier = Modifier.padding(top = 5.dp),
                                                                color = Color.Black,
                                                                textAlign = TextAlign.Center,
                                                                fontWeight = FontWeight.Bold,
                                                                style = androidx.compose.ui.text.TextStyle(
                                                                    fontSize = 15.sp,
                                                                    fontFamily = FontFamily(Font(R.font.poppins_regular))
                                                                )
                                                            )

                                                            ClickableImage {
                                                                dao.deleteSpecificExpense(list.get(i).id)
                                                                Toast.makeText(
                                                                    this@ExpenseList,
                                                                    "image deleted",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }

                                                    }
                                                    Column(
                                                        modifier = Modifier.padding(
                                                            15.dp
                                                        )
                                                    ) {
                                                        var type =
                                                            if (list.get(i).action == 0) "Debited from" else "Credited to"

                                                        Text(text = " Why : ${list.get(index = i).why?.let { it }}")
                                                        Spacer(Modifier.height(2.5.dp))
                                                        Text(text = "  How Much :  ₹ ${list.get(i).amount}  is ${type} your account")
                                                    }

                                                }
                                            }

                                        }
                                    }
                                }
                            }


                        })

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


    @Composable
    fun ClickableImage(onClick: () -> Unit) {
        val image: Painter = painterResource(id = R.drawable.delete)

        Image(
            painter = image,
            contentDescription = "Clickable Image",
            modifier = Modifier
                .clickable { onClick() }
                .size(25.dp), // Set size as needed
            colorFilter = null // Optional: Set a color filter if required
        )
    }


}

