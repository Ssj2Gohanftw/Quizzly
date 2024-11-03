package com.example.quizapp.pages
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.model.AuthState
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.R
//Composable for Sign up page for Students
@Composable
fun SignUpPage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){
    //State Variables for the sign up page
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    var name by remember {
        mutableStateOf("")
    }
    //Observing the authState of the viewModel
    val authState = authViewModel.authState.observeAsState()
    val context=LocalContext.current//Getting the context of the app
    //LaunchedEffect to handle the authState of the viewModel
    LaunchedEffect(authState.value ) {
        //if user is authenticated,he is taken to the home page
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("currentscreen")
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
        Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Login Image", modifier = Modifier.size(200.dp))

        Text(text = "Welcome!", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text="Create an account")
        OutlinedTextField(leadingIcon = {
            Icon(imageVector = Icons.Sharp.Person, contentDescription = "Name")
        },value =name , onValueChange ={
            name=it
        }, label = {
            Text(text = "Name")
        })
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(leadingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = "Email Account")
        },value =email , onValueChange ={
            email=it
        }, label = {
            Text(text = "Email address")
        })
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(leadingIcon ={
            Icon(imageVector = Icons.Filled.Lock, contentDescription = "Password Icon")
        } , value = password, onValueChange ={
            password=it
        }, label ={
            Text(text = "Password")
        }  , visualTransformation = if(!passwordVisible)PasswordVisualTransformation() else VisualTransformation.None,trailingIcon = {
            val eye= if(passwordVisible){
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = eye, contentDescription = "Toggle Password Visibility")
            }
        })
        Spacer(modifier = Modifier.height(16.dp))
        //On clicking this button,a new user account is created with the given details
        Button(onClick = {
            authViewModel.signup(name,email,password)
        },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            , modifier = Modifier.padding(16.dp)
        ){
            Text(text = "Sign Up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment =Alignment.CenterVertically)
        {
            HorizontalDivider(
                modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = Color.White,
                thickness = 1.dp)

            Text(text = "or",fontSize=14.sp,color= Color.White,)

            HorizontalDivider(
                modifier= Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = Color.White,
                thickness = 1.dp)
        }
        Text(text ="Sign up with")
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
        //Button to navigate to the login page
        TextButton(onClick ={
            navController.navigate("login"){
                popUpTo("signup"){
                    inclusive=true
                }
            }
        }){
            Text("Already have an account?", fontSize = 15.sp)
            Text(text = "Sign In",
                color= Color(0xFF007BFF),
                fontSize = 18.sp
            )
        }
    }
}

