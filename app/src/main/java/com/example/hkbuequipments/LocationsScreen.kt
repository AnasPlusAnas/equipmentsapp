package com.example.hkbuequipments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hkbuequipments.components.LocationCard

@Composable
fun LocationsScreen(navController: NavController) {
    val locations = listOf(
        "Street", "Place", "Center", "Drive", "Avenue",
        "Trail", "Park", "Road", "Plaza", "Hill",
        "Parkway", "Crossing", "Pass", "Court", "Circle",
        "Lane", "Terrace", "Alley", "Point", "Way"
    )

    // List of Locations
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(locations) { location ->
            LocationCard(
                location = location,
                onClick = {
                    navController.navigate("locationEquipments/$location")
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
        }
    }
}
