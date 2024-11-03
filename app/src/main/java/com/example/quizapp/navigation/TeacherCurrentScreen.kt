package com.example.quizapp.navigation
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.quizapp.pages.TrQuizCreatePage
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.model.NavItem
import com.example.quizapp.pages.SettingsPage
import com.example.quizapp.pages.TrHomePage

@Composable
fun TrCurrentScreen(modifier: Modifier=Modifier, navController: NavController, authViewModel: AuthViewModel,context: Context){
    val navItemList= listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Create Quiz", Icons.Default.Add),
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
            1->"Create Quiz"
            2->"Settings"
            else->""
        }
        TrContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex=selectedIndex,
            navController=navController,
            title = title,
            authViewModel=authViewModel,
            context = context
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrContentScreen(modifier: Modifier = Modifier, selectedIndex:Int,title:String,navController: NavController, authViewModel: AuthViewModel,context: Context) {
    when (selectedIndex) {
        0 -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = title) }, // Set the title
                    )
                },
                content = { innerPadding ->
                    TrHomePage(modifier = modifier.padding(innerPadding), navController, authViewModel,context)
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
                    TrQuizCreatePage(modifier = modifier.padding(innerPadding), navController, authViewModel )

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
                SettingsPage(modifier = modifier.padding(innerPadding), navController, authViewModel, context )
            }
        )
        }


    }
}