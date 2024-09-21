package com.example.quizapp.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.AuthState
import com.example.quizapp.AuthViewModel
import com.example.quizapp.R


@Composable
fun SignUpPage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val authState = authViewModel.authState.observeAsState()
    val context=LocalContext.current
    LaunchedEffect(authState.value ) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> {
                Unit
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(painter = painterResource(id = R.drawable.loginicon), contentDescription = "Login Image", modifier = Modifier.size(200.dp))

        Text(text = "Welcome!", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text="Sign Up with an account")
        OutlinedTextField(value =email , onValueChange ={
            email=it
        }, label = {
            Text(text = "Email address")
        })
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(value = password, onValueChange ={
            password=it
        }, label ={
            Text(text = "Password")
        }  , visualTransformation = PasswordVisualTransformation() )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.signup(email,password)
        }) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text ="\tor\n Sign up with")
        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Image(
                painter = painterResource(id = R.drawable.google)
                , contentDescription ="Google"
                , modifier = Modifier
                    .size(60.dp)
                    .clickable { })
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick ={
            navController.navigate("login")
        }){
            Text("Already have an account? Sign IN")
        }
    }
}

