import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.l3gacy.lib.compose.wheelpicker.WheelDatePicker
import com.l3gacy.lib.compose.wheelpicker.WheelTimePicker

@Composable
fun App() {

    val list = (1..12).map { "item $it" }

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val greeting = remember { Greeting().greet() }

        Column(
            Modifier.fillMaxWidth().statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Box(modifier = Modifier.weight(1f)) {
//                WheelView(
//                    modifier = Modifier,
////                    itemSize = DpSize(150.dp, 25.dp),
//                    selection = 5,
//                    itemCount = list.size,
//                    selectorOption = SelectorOptions().copy(
//                        color = Color.Magenta, alpha = 1f, width = 2.dp
//                    ),
//                    rowOffset = 3,
//                    onSelectionChanged = {
//                        println("selection: $it")
//                    },
//                    properties = WheelViewProperties(offset = 2),
//                    itemContent = {
//                        Text(
//                            text = list[it],
//                            textAlign = TextAlign.Start,
//                            fontSize = 17.sp,
//                            color = Color.Black
//                        )
//                    },
//                )
//            }

//            var selectedText by remember { mutableStateOf("") }
//
//            Column(
//                modifier = Modifier.weight(1f),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                WheelPicker(
//                    modifier = Modifier.background(Color.LightGray).fillMaxWidth(0.7F),
////                    size = DpSize(150.dp, 25.dp),
//                    itemCount = list.size,
//                    onItemSelected = {
//                        selectedText = "selection: $it"
//                        println(selectedText)
//                        it
//                    },
//                    content = {
//                        Text(
//                            text = list[it],
//                            textAlign = TextAlign.Start,
//                            fontSize = 17.sp,
//                            color = MaterialTheme.colors.onSurface
//                        )
//                    },
//                )
//                Text(text = selectedText)
//            }

            var selectedTime by remember { mutableStateOf("") }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WheelTimePicker(
                    modifier = Modifier.background(Color.LightGray).fillMaxWidth(0.7F),
                    endless = true,
                    onSelectedTime = { time ->
                        selectedTime = time.toString()
                        println(selectedTime)
                    }
                )
                Text(text = selectedTime)
            }

            var selectedDate by remember { mutableStateOf("") }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WheelDatePicker(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxWidth(),
                    endless = true,
                    onSelectedDate = { date ->
                        selectedDate = date.toString()
//                        println(selectedDate)
                    }
                )
                Text(text = selectedDate)
            }

//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(DrawableResource("compose-multiplatform.xml")), null)
//                    Text("Compose: $greeting")
//                }
//            }
        }
    }
}
