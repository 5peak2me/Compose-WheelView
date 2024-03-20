import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.l3gacy.lib.compose.wheelview.WheelPicker
import com.l3gacy.lib.compose.wheelview.picker.WheelDatePicker
import com.l3gacy.lib.compose.wheelview.picker.WheelTimePicker
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {

    val list = (1..1200).map { "item $it" }

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val greeting = remember { Greeting().greet() }
        Column(Modifier.fillMaxWidth().statusBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
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
            Box(modifier = Modifier.weight(1f)) {
                WheelPicker(
                    modifier = Modifier.background(Color.LightGray).fillMaxWidth(0.7F),
//                    size = DpSize(150.dp, 25.dp),
                    count = list.size,
                    rowCount = 7,
                    onScrollFinished = {
//                        println("selection: $it")
                        it
                    },
                    content = {
                        Text(
                            text = list[it],
                            textAlign = TextAlign.Start,
                            fontSize = 17.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    },
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                WheelTimePicker(
                    modifier = Modifier.background(Color.LightGray).fillMaxWidth(0.7F),
                    onSelectedTime = { time ->
                        println("hour: ${time.hour}, minute: ${time.minute}")
                    }
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                WheelDatePicker(
                    modifier = Modifier.background(Color.LightGray).fillMaxWidth(),
                    onSelectedDate = { date ->
                        println("year: ${date.year}, month: ${date.month}, day: ${date.dayOfMonth}")
                    }
                )
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
