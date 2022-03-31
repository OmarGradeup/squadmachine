package com.example.beautifulapp

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction.Companion.Go
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.lottie.compose.*
import com.example.beautifulapp.ui.theme.BeautifulAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            viewModel.buildTeamMap()
            val context = LocalContext.current
            var squadName by remember { mutableStateOf("") }
            var isEnabled by remember { mutableStateOf(true) }
            var memberName by remember { mutableStateOf("") }
            var greetingText by remember { mutableStateOf("Welcome to the Squad Machine.") }
            var wins by remember { mutableStateOf(0f) }
            var attempts by remember { mutableStateOf(0f) }
            var isPlaying by remember {
                mutableStateOf(false)
            }
            Column(modifier = Modifier.fillMaxSize()) {
                val gradient =
                    Brush.horizontalGradient(listOf(Color(0xFF28D8A3), Color(0xFF00BEB2)))
                TopBar(
                    userName = "Squad Machine Game",
                    modifier = Modifier
                        .padding(10.dp)
                )



                Spacer(modifier = Modifier.height(40.dp))
                Greeting(name = greetingText)
                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    CardWithText(placeHolderText = "Squad", selectedText = squadName)
                    Spacer(modifier = Modifier.width(12.dp))
                    CardWithText(placeHolderText = "Member Name", selectedText = memberName)
                }

                GradientButton(
                    text = "Go",
                    gradient = gradient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(CircleShape),
                    isEnabled = isEnabled
                ) {
                    if(attempts == 10f) {
                        isEnabled = false
                        greetingText = "Your trials are exhausted."
                        return@GradientButton
                    }

                    val timer = object : CountDownTimer(1000, 50) {
                        override fun onTick(millisUntilFinished: Long) {
                            squadName = viewModel.getSquadName()
                            memberName = viewModel.getMemberName()
                        }

                        override fun onFinish() {
                            val isMatch = viewModel.checkGeneratedResult(squadName, memberName)

                            attempts += 1
                            if (isMatch) {
                                wins += 1
                                Toast.makeText(
                                    context,
                                    "Congrats! It is a match.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isPlaying = true
                            } else {
                                isPlaying = false
                            }
                        }
                    }
                    timer.start()



                }

                var speed by remember {
                    mutableStateOf(1f)
                }
                val composition by rememberLottieComposition(
                    LottieCompositionSpec
                        .RawRes(R.raw.clap)
                )
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    isPlaying = isPlaying,
                    speed = speed,
                    restartOnPlay = true

                )
                LottieAnimation(
                    composition,
                    progress,
                    modifier = Modifier.size(400.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AttemptsCount(wins = wins, attempts = attempts)
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }

}

@Composable
fun CardWithText(placeHolderText: String, selectedText: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .height(60.dp)
                .width(150.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = placeHolderText,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = selectedText,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit = { },
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
        enabled = isEnabled,
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .then(modifier),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = Color.White
            )
        }
    }
}

@Composable
fun TopBar(userName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .weight(0.1f)
        )
        RoundImage(
            image = painterResource(id = R.drawable.avatar),
            modifier = Modifier
                .size(40.dp)
                .weight(0.1f)
        )
        Text(
            text = userName,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.7f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_dotmenu),
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .weight(0.1f)
        )
    }

}

@Composable
fun RoundImage(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Image(
        painter = image,
        contentDescription = "profile pic",
        modifier = modifier
            .padding(
                horizontal = 4.dp
            )
            .clip(CircleShape)
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
    )

}

@Composable
fun AttemptsCount(wins: Float, attempts: Float) {
    var percentage = 0f
    var percentageString = "0"
    if (attempts > 0) {
        percentage = (wins / attempts) * 100
        percentageString = percentage.toString().substringBefore(".")
    }
    Text(
        text = "Win Percentage $percentageString%",
        fontSize = 16.sp,
        color = Color.Gray
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BeautifulAppTheme {
        CardWithText(placeHolderText = "Member Name", selectedText = "Omar")
    }
}