import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.l3gacy.lib.compose.wheelview.SelectorOptions
import com.l3gacy.lib.compose.wheelview.WheelPicker
import com.l3gacy.lib.compose.wheelview.WheelView
import com.l3gacy.lib.compose.wheelview.WheelViewProperties
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {

    val list = mutableListOf<String>()
    for (value in 1..12) {
        list.add("$value")
    }

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val greeting = remember { Greeting().greet() }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.weight(1f)) {
                WheelView(
                    modifier = Modifier,
//                    itemSize = DpSize(150.dp, 25.dp),
                    selection = 5,
                    itemCount = list.size,
                    selectorOption = SelectorOptions().copy(
                        color = Color.Magenta, alpha = 1f, width = 2.dp
                    ),
                    rowOffset = 3,
                    onSelectionChanged = {
                        println("selection: $it")
                    },
                    properties = WheelViewProperties(offset = 2),
                    itemContent = {
                        Text(
                            text = list[it],
                            textAlign = TextAlign.Start,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    },
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                WheelPicker(
                    modifier = Modifier,
//                    size = DpSize(150.dp, 25.dp),
                    count = list.size,
                    rowCount = 5,
                    onScrollFinished = {
                        println("selection: $it")
                        it
                    },
                    content = {
                        Text(
                            text = list[it],
                            textAlign = TextAlign.Start,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    },
                )
            }
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(DrawableResource("compose-multiplatform.xml")), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}
