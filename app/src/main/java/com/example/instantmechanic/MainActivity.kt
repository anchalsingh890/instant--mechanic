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
    val rating: String,
    val distance: String,
    val location: String,
    val services: String,
    val status: String,
    val hours: String,
    val phone: String
)

val mechanics = listOf(
    Mechanic(
        "Sharma Auto Garage", "4.7", "1.2 km",
        "Bhopal", "Oil Change, Brake Repair, Engine Service",
        "Open", "9:00 AM - 8:00 PM", "9876543210"
    ),
    Mechanic(
        "City Car Care", "4.5", "2.5 km",
        "Bhopal", "Car Wash, Battery, AC Repair",
        "Open", "10:00 AM - 7:00 PM", "9876501234"
    ),
    Mechanic(
        "Quick Fix Motors", "4.3", "3.1 km",
        "Bhopal", "Tyre Repair, Engine Service",
        "Closed", "9:00 AM - 6:00 PM", "9876512345"
    )
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MechanicApp()
            }
        }
    }
}

@Composable
fun MechanicApp() {

    var selectedMechanic by remember {
        mutableStateOf<Mechanic?>(null)
    }

    var showRequestForm by remember {
        mutableStateOf(false)
    }

    var submitted by remember {
        mutableStateOf(false)
    }

    when {
        submitted -> {
            ConfirmationScreen {
                submitted = false
                selectedMechanic = null
            }
        }

        showRequestForm && selectedMechanic != null -> {
            RequestServiceScreen(
                mechanic = selectedMechanic!!,
                onSubmit = {
                    submitted = true
                    showRequestForm = false
                }
            )
        }

        selectedMechanic != null -> {
            MechanicDetailsScreen(
                mechanic = selectedMechanic!!,
                onBack = {
                    selectedMechanic = null
                },
                onRequest = {
                    showRequestForm = true
                }
            )
        }

        else -> {
            HomeScreen(
                onMechanicClick = {
                    selectedMechanic = it
                }
            )
        }
    }
}

@Composable
fun HomeScreen(onMechanicClick: (Mechanic) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Instant Mechanic",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Find a mechanic near you",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text("⭐ ${mechanic.rating}")
                        Text("📍 ${mechanic.location}")
                        Text("📏 ${mechanic.distance}")
                        Text("🔧 ${mechanic.services}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Status: ${mechanic.status}")
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
            .padding(16.dp)
    ) {

        Text(
            text = "Mechanic Details",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            mechanic.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Text("⭐ Rating: ${mechanic.rating}")
        Text("📍 Address: ${mechanic.location}")
        Text("🔧 Services: ${mechanic.services}")
        Text("🕐 Working Hours: ${mechanic.hours}")
        Text("📞 Phone: ${mechanic.phone}")
        Text("Status: ${mechanic.status}")

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onRequest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Request Service")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun RequestServiceScreen(
    mechanic: Mechanic,
    onSubmit: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var vehicle by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Request Service",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mechanic: ${mechanic.name}")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = vehicle,
            onValueChange = { vehicle = it },
            label = { Text("Vehicle Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = service,
            onValueChange = { service = it },
            label = { Text("Select Service") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = problem,
            onValueChange = { problem = it },
            label = { Text("Problem Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Request")
        }
    }
}

@Composable
fun ConfirmationScreen(onDone: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            "Request Confirmed! ✅",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Your vehicle service request has been submitted successfully."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}