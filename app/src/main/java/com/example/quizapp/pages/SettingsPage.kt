package com.example.quizapp.pages
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.quizapp.AuthState
import com.example.quizapp.AuthViewModel
import com.example.quizapp.R

@Composable
fun SettingsPage(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val currentUser = authViewModel.getCurrentUser()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

//    val pickImageLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            profileImageUri = uri
//        }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("roleSelection")
            else -> Unit
        }
    }
    // We Use LazyColumn for scrollable content
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(100.dp))
            ConstraintLayout {
                val (topImg, profile) = createRefs()
                Image(
                    painterResource(R.drawable.ic_launcher_background), null,
                    Modifier.fillMaxSize().constrainAs(topImg)
                    {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    })
                Image(
                    painterResource(R.drawable.profile), null,
                    Modifier.fillMaxSize().constrainAs(profile)
                    {
                        top.linkTo(topImg.bottom)
                        bottom.linkTo(topImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            }
            Text(text="Ethan D'Costa",
                fontSize = 25.sp,
                fontWeight =FontWeight.Bold,
                color = Color.White,
               modifier = Modifier.padding(top=16.dp))

            Text(
                text="Scorpion123",
                fontSize = 20.sp,
                fontWeight =FontWeight.Bold,
                color = Color.White,
                modifier =Modifier.padding(top=16.dp))

            Button(onClick ={authViewModel.signout()},
                modifier=Modifier.fillMaxWidth().
                padding(
                    start =32.dp,end=32.dp, top = 10.dp, bottom = 10.dp).
                height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15)
            )
            {
                Column(modifier=Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){
//                    Image(painter=painterResource(id=R.drawable.google ),contentDescription ="",modifier=Modifier.padding(end=5.dp).clickable{})
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "",
                        modifier = Modifier.padding(end = 5.dp).clickable {})
                }
                    Column(modifier=Modifier.padding(start=16.dp).weight(1f),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){
                        Text(text="Sign Out",
                            fontSize = 20.sp,
                            fontWeight =FontWeight.Bold,
                            color = Color.Black)
                    }
             }
            Button(onClick ={},
                modifier=Modifier.fillMaxWidth().
                padding(
                    start =32.dp,end=32.dp, top = 10.dp, bottom = 10.dp).
                height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(15)
            )
            {
                Column(modifier=Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){
//                    Image(painter=painterResource(id=R.drawable.google ),contentDescription ="",modifier=Modifier.padding(end=5.dp).clickable{})
                    Icon(
                        imageVector = Icons.Sharp.Assessment,
                        contentDescription = "",
                        modifier = Modifier.padding(end = 5.dp).clickable {})
                }
                Column(modifier=Modifier.padding(start=16.dp).weight(1f),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start){
                    Text(text="View Leaderboards",
                        fontSize = 20.sp,
                        fontWeight =FontWeight.Bold,
                        color = Color.Black)
                }


            }
        }
        }
}




