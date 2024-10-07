package com.example.quizapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quizapp.pages.ClassesPage
import com.example.quizapp.pages.HomePage
import com.example.quizapp.pages.NavItem
import com.example.quizapp.pages.SettingsPage

@Composable
fun CurrentScreen(modifier: Modifier=Modifier, navController: NavController, authViewModel: AuthViewModel){
val navItemList= listOf(
    NavItem("Home", Icons.Default.Home),
    NavItem("Classes", Icons.Default.Person),
    NavItem("Account", Icons.Default.Settings)
)
var selectedIndex by remember{
    mutableIntStateOf(0)
}
Scaffold(
modifier = Modifier.fillMaxSize(),
bottomBar = {
    NavigationBar {
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = selectedIndex==index,
                onClick = {selectedIndex=index },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = "Icon")
                },
                label = {
                    Text(text=navItem.label)
                }
            )
        }
    }
}
)
{
    innerPadding->
    ContentScreen(
        modifier = Modifier.padding(innerPadding),
        selectedIndex=selectedIndex,
        navController=navController,
        authViewModel=authViewModel
    )

}
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex:Int,navController: NavController, authViewModel: AuthViewModel) {
    when (selectedIndex) {
        0 -> {
            HomePage(modifier,navController, authViewModel)
        }

        1 -> {
            ClassesPage(modifier, navController, authViewModel )
        }

        2 -> {
            SettingsPage(modifier, navController, authViewModel)
        }
    }
}
