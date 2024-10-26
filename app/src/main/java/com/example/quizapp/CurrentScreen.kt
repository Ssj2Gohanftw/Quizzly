package com.example.quizapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.pages.ClassesPage
import com.example.quizapp.pages.HomePage
import com.example.quizapp.model.NavItem
import com.example.quizapp.pages.SettingsPage

@Composable
fun CurrentScreen(modifier: Modifier=Modifier, navController: NavController, authViewModel: AuthViewModel){
val navItemList= listOf(
    NavItem("Home", Icons.Default.Home),
    NavItem("Classes", Icons.Default.Assessment),
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
    val title= when(selectedIndex){
        0->"Home"
        1->"Classes"
        2->"Settings"
        else->""
    }
    ContentScreen(
        modifier = Modifier.padding(innerPadding),
        selectedIndex=selectedIndex,
        navController=navController,
        title = title,
        authViewModel=authViewModel
    )

}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex:Int,title:String,navController: NavController, authViewModel: AuthViewModel) {
    when (selectedIndex) {
        0 -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = title) }, // Set the title
                    )
                },
                content = { innerPadding ->
                    HomePage(modifier = modifier.padding(innerPadding), navController, authViewModel)
                }
            )
        }

        1 -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = title) },
                    )
                },
                content = { innerPadding ->
                    ClassesPage(modifier = modifier.padding(innerPadding), navController, authViewModel )

                }
            )
        }

        2 -> {Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = title) }, // Set the title
                )
            },
            content = { innerPadding ->
                SettingsPage(modifier = modifier.padding(innerPadding), navController, authViewModel)
            }
        )
        }
    }
}
