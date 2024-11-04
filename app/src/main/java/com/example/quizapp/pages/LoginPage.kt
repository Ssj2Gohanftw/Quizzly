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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.R
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.example.quizapp.model.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun LoginPage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){
   var email by remember {
      mutableStateOf("")
   }
   var password by remember {
      mutableStateOf("")
   }
   var isLoading by remember {
      mutableStateOf(false)
   }

   var resetEmailSent by remember {
      mutableStateOf(false)
   }
   var passwordVisible by remember {
      mutableStateOf(false)
   }
   val authState = authViewModel.authState.observeAsState()
   val context= LocalContext.current
   val databaseRef=FirebaseDatabase.getInstance().getReference("Students")
   LaunchedEffect(authState.value) {
      when (authState.value) {
         is AuthState.Authenticated -> {
            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            if (userEmail != null) {
               databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                     for (childSnapshot in snapshot.children) {
                        val studentEmail = childSnapshot.child("email").getValue(String::class.java)
                        if (studentEmail == userEmail) {
                           Toast.makeText(context, "Logged in as $studentEmail", Toast.LENGTH_SHORT).show()
                           break
                        }
                     }
                  }

                  override fun onCancelled(error: DatabaseError) {
                     Toast.makeText(context, "Failed to fetch user email.", Toast.LENGTH_SHORT).show()
                  }
               })
            }
            navController.navigate("currentscreen")
         }
         is AuthState.Error -> {
            Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
         }
         else -> Unit
      }
   }


   Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   )
   {
      Image(painter = painterResource(id = R.drawable.profile), contentDescription = "Login Image", modifier = Modifier.size(200.dp))

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
      }  , visualTransformation = if(!passwordVisible)PasswordVisualTransformation() else VisualTransformation.None,trailingIcon = {
         val eye= if(passwordVisible){
            Icons.Filled.Visibility
         } else {
            Icons.Filled.VisibilityOff
         }
         IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(imageVector = eye, contentDescription = "Toggle Password Visibility")
         }} )
      Spacer(modifier = Modifier.height(16.dp))

      Button(onClick = {authViewModel.login(email,password)},
         colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
         , modifier = Modifier.padding(16.dp)
      ) {
         Text(text = "Login", color = Color.White)
      }
      Spacer(modifier = Modifier.height(32.dp))

      Text(text = "Forgot Password?", modifier = Modifier.clickable {
         if (email.isNotEmpty()) {
            isLoading = true
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
               .addOnCompleteListener { task ->
                  isLoading = false
                  if (task.isSuccessful) {
                     resetEmailSent = true
                     Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                  } else {
                     Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                  }
               }
         } else {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
         }
      })
      if (isLoading) {
         Toast.makeText(context,  "Sending email...", Toast.LENGTH_SHORT).show()
      } else if (resetEmailSent) {
         Toast.makeText(context,  "Check your email for password reset instructions.", Toast.LENGTH_SHORT).show()
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
         Text("Don't have an account? ",fontSize = 15.sp)
         Text("Register" ,
            color= Color(0xFF007BFF), fontSize = 18.sp)
   }
   }
}
