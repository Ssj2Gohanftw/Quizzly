package com.example.quizapp.navigation

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.model.AuthViewModel
import com.example.quizapp.model.NavItem
import com.example.quizapp.pages.HomePage
import com.example.quizapp.pages.SettingsPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Account", Icons.Default.Settings)
    )
    var selectedIndex by remember { mutableIntStateOf(0) }
    var backPressedOnce by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Press back again to close the app", Toast.LENGTH_SHORT).show()
            coroutineScope.launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(imageVector = navItem.icon, contentDescription = "Icon") },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val title = when (selectedIndex) {
            0 -> "Home"
            1 -> "Account"
            else -> ""
        }

        // Animated transition between screens
        AnimatedContent(
            targetState = selectedIndex,
            transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "" // Smooth fade transition
        ) { targetIndex ->
            ContentScreen(
                modifier = Modifier.padding(innerPadding),
                selectedIndex = targetIndex,
                navController = navController,
                title = title,
                authViewModel = authViewModel,
                context = context
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    title: String,
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    when (selectedIndex) {
        0 -> {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = title) })
                }
            ) { innerPadding ->
                HomePage(
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(bottom = 56.dp), // Add extra padding for content above nav bar
                    navController = navController,
                    authViewModel = authViewModel,
                    context = context
                )
            }
        }
        1 -> {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text(text = title) })
                }
            ) { innerPadding ->
                SettingsPage(
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(bottom = 56.dp),
                    navController = navController,
                    authViewModel = authViewModel,
                    context = context
                )
            }
        }
    }
}