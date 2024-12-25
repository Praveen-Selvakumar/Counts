package com.example.counts

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.counts.ui.theme.JP_PracticeTheme

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity_RESP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {



        }
        Log.d(TAG, "onCreate: ")


    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
}



@Composable
fun setButton(){
    val context = LocalContext.current

    Button(onClick = {
/*
        context.startActivity(Intent(context.applicationContext , SeconfActivity::class.java))
*/
    }) {
        Text(text = "Navigate to Activity B")
    }
}

@Composable
fun setText(){
    Text(text = "Activity A", style = TextStyle(
        fontSize = 30.sp
    )
    )
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JP_PracticeTheme {
        Greeting("Android")
    }
}
