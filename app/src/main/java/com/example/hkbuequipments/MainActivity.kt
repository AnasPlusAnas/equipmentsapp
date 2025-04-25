package com.example.hkbuequipments

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hkbuequipments.DataStoreInstance.DARK_MODE
import com.example.hkbuequipments.ui.theme.HKBUEquipmentsTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkMode by DataStoreInstance.getBooleanPreferences(this, DARK_MODE)
                .collectAsState(initial = false)

            val snackBarHostState = remember { SnackbarHostState() }


            val navController = rememberNavController()
            HKBUEquipmentsTheme(darkTheme = darkMode == true) {
                Scaffold(
                    containerColor = colorResource(R.color.background),
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                        Log.d("CurrentRoute", currentRoute.toString())
                        val title = when (currentRoute) {
                            "highlighted" -> "Highlighted Equipments"
                            "locations" -> "Locations"
                            "search" -> "Search"
                            "user" -> "User Profile"
                            "register" -> "Register"
                            "equipmentDetail/{equipmentId}/{isRented}" -> "Equipment Detail"
                            "locationEquipments/{location}" -> "Location Equipments"
                            else -> "HKBU Equipment App"
                        }

                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = colorResource(R.color.background)
                            ),
                            title = { Text(title, color = Color.White) },
                            navigationIcon = {
                                if (
                                    currentRoute?.startsWith("register") == true ||
                                    currentRoute?.contains("/") == true
                                ) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            tint = Color.White,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            },
                        )
                    },
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "highlighted",
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }

                    ) {
                        composable("highlighted") { HighlightedEquipmentsScreen(navController) }
                        composable("locations") { LocationsScreen(navController) }
                        composable("search") { SearchScreen(navController) }
                        composable("user") { UserScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable(
                            route = "equipmentDetail/{equipmentId}/{isRented}",
                            arguments = listOf(
                                navArgument("equipmentId") { type = NavType.StringType },
                                navArgument("isRented") {
                                    type = NavType.StringType
                                    defaultValue = "false"
                                }
                            )
                        ) { backStackEntry ->
                            val equipmentId =
                                backStackEntry.arguments?.getString("equipmentId") ?: ""
                            val isRented = backStackEntry.arguments?.getString("isRented")
                            EquipmentDetailScreen(snackBarHostState, equipmentId, isRented)
                        }
                        composable(
                            "locationEquipments/{location}",
                        ) { navBackStackEntry ->
                            LocationEquipmentsScreen(
                                navController,
                                navBackStackEntry.arguments?.getString("location") ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        "Highlighted" to Icons.TwoTone.Star,
        "Locations" to Icons.TwoTone.LocationOn,
        "Search" to Icons.TwoTone.Search,
        "User" to Icons.TwoTone.Person
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    
    // Update selectedItem based on current route
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            "highlighted" -> selectedItem = 0
            "locations" -> selectedItem = 1
            "search" -> selectedItem = 2
            "user" -> selectedItem = 3
        }
    }

    NavigationBar(
        containerColor = colorResource(R.color.background),
        contentColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, (name, icon) ->
            val selected = selectedItem == index
            NavigationBarItem(
                icon = { 
                    Icon(
                        icon, 
                        contentDescription = name,
                        modifier = Modifier.size(if (selected) 28.dp else 24.dp),
                        tint = if (selected) Color.White else Color.White.copy(alpha = 0.6f)
                    ) 
                },
                label = { 
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) Color.White else Color.White.copy(alpha = 0.6f)
                    ) 
                },
                selected = selected,
                modifier = Modifier.testTag(name),
                onClick = {
                    selectedItem = index
                    navController.navigate(name.lowercase())
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color(0xFF7B7EF6) // Slightly lighter than background for subtle effect
                )
            )
        }
    }
}
