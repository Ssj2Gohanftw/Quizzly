package com.example.quizapp.pages

import android.graphics.drawable.Icon
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.AuthViewModel
import com.example.quizapp.R

@Composable
fun LoginPage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){
   var email by remember {
      mutableStateOf("")
   }
   var password by remember {
      mutableStateOf("")
   }
   Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   )
   {
      Image(painter = painterResource(id = R.drawable.loginicon), contentDescription = "Login Image", modifier = Modifier.size(200.dp))

      Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

      Spacer(modifier = Modifier.height(4.dp))

      Text(text="Login to your account")
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
      }  , visualTransformation = PasswordVisualTransformation() )
      Spacer(modifier = Modifier.height(16.dp))

      Button(onClick = {},
         colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
         , modifier = Modifier.padding(16.dp)
      ) {
         Text(text = "Login", color = Color.White)
      }
      Spacer(modifier = Modifier.height(32.dp))

      Text(text = "Forgot Password?", modifier = Modifier.clickable {
      })
   

      Spacer(modifier = Modifier.height(32.dp))

      Row(modifier = Modifier.fillMaxWidth(), verticalAlignment =Alignment.CenterVertically)
      {
         HorizontalDivider(
            modifier= Modifier
               .fillMaxWidth()
               .weight(1f),
            color = Color.Black,
            thickness = 1.dp)

         Text(text = "or",fontSize=14.sp,color= Color.Black,)

         HorizontalDivider(
            modifier= Modifier
               .fillMaxWidth()
               .weight(1f),
            color = Color.Black,
            thickness = 1.dp)
      }
      Text(text ="Sign in with")
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
         navController.navigate("signup"){
            popUpTo("login"){
               inclusive=true
            }
         }
      }
      ){
         Text("Don't have an account? ",color= Color.Black,fontSize = 15.sp)
         Text("Register" ,
            color= Color(0xFF007BFF), fontSize = 18.sp)
   }
   }
}
