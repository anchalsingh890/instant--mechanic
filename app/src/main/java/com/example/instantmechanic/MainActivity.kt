package com.example.instantmechanic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Mechanic(
    val name: String,
    val garage: String,
    val rating: String,
    val distance: String,
    val address: String,
    val services: String,
    val hours: String,
    val phone: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MechanicApp()
        }
    }
}

@Composable
fun MechanicApp() {

    var selectedMechanic by remember { mutableStateOf<Mechanic?>(null) }
    var showForm by remember { mutableStateOf(false) }

    val mechanics = listOf(
        Mechanic(
            "Raj Kumar",
            "Raj Auto Garage",
            "4.8",
            "1.2 km",
            "MP Nagar, Bhopal",
            "Oil Change, Brake Repair, Engine Service",
            "9 AM - 8 PM",
            "9876543210"
        ),
        Mechanic(
            "Amit Sharma",
            "Amit Car Care",
            "4.6",
            "2.5 km",
            "Kolar Road, Bhopal",
            "Car Service, Battery, Tyre Repair",
            "8 AM - 9 PM",
            "9876501234"
        )
    )

    if (showForm && selectedMechanic != null) {
        RequestServiceScreen(
            mechanic = selectedMechanic!!,
            onBack = { showForm = false }
        )
    } else if (selectedMechanic != null) {
        MechanicDetailsScreen(
            mechanic = selectedMechanic!!,
            onBack = { selectedMechanic = null },
            onRequest = { showForm = true }
        )
    } else {
        HomeScreen(
            mechanics = mechanics,
            onMechanicClick = { selectedMechanic = it }
        )
    }
}

@Composable
fun HomeScreen(
    mechanics: List<Mechanic>,
    onMechanicClick: (Mechanic) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Instant Mechanic",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nearby Mechanics",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn {
            items(mechanics) { mechanic ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            onMechanicClick(mechanic)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            mechanic.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text("Garage: ${mechanic.garage}")
                        Text("⭐ Rating: ${mechanic.rating}")
                        Text("📍 Distance: ${mechanic.distance}")
                        Text("Services: ${mechanic.services}")
                        Text("Open: ${mechanic.hours}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Tap to view details →"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MechanicDetailsScreen(
    mechanic: Mechanic,
    onBack: () -> Unit,
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Button(onClick = onBack) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            mechanic.garage,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Mechanic: ${mechanic.name}")
        Text("⭐ Rating: ${mechanic.rating}")
        Text("📍 Address: ${mechanic.address}")
        Text("🛠️ Services: ${mechanic.services}")
        Text("🕒 Working Hours: ${mechanic.hours}")
        Text("📞 Phone: ${mechanic.phone}")

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = onRequest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Request Service")
        }
    }
}

@Composable
fun RequestServiceScreen(
    mechanic: Mechanic,
    onBack: () -> Unit
) {

    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var vehicleNumber by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Button(onClick = onBack) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Request Service",
            style = MaterialTheme.typography.headlineSmall
        )

        Text("Mechanic: ${mechanic.name}")

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = vehicleNumber,
            onValueChange = { vehicleNumber = it },
            label = { Text("Vehicle Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = service,
            onValueChange = { service = it },
            label = { Text("Service Required") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = problem,
            onValueChange = { problem = it },
            label = { Text("Problem Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { submitted = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Request")
        }

        if (submitted) {

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "✅ Service Request Confirmed!",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Mechanic: ${mechanic.name}")
            Text("Vehicle: $vehicleNumber")
        }
    }
}