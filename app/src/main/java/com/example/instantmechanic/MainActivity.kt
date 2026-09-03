package com.example.instantmechanic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray

data class Mechanic(
    val name: String,
    val rating: String,
    val distance: String,
    val location: String,
    val services: String,
    val status: String,
    val address: String,
    val hours: String,
    val phone: String
)

val mechanicJson = """
[
 {"name":"Sharma Auto Garage","rating":"4.5","distance":"2.1 km","location":"Bhopal","services":"Oil Change, Brake Repair","status":"Open","address":"MP Nagar, Bhopal","hours":"9 AM - 8 PM","phone":"9876543210"},
 {"name":"City Car Care","rating":"4.3","distance":"3.5 km","location":"Bhopal","services":"Car Service, Battery","status":"Open","address":"Arera Colony, Bhopal","hours":"8 AM - 7 PM","phone":"9876543211"},
 {"name":"Quick Fix Motors","rating":"4.7","distance":"4.2 km","location":"Bhopal","services":"Engine Repair, Tyre Service","status":"Closed","address":"Kolar Road, Bhopal","hours":"10 AM - 6 PM","phone":"9876543212"}
]
"""

fun loadMechanics(): List<Mechanic> {
    val array = JSONArray(mechanicJson)
    return List(array.length()) { i ->
        val o = array.getJSONObject(i)
        Mechanic(
            o.getString("name"),
            o.getString("rating"),
            o.getString("distance"),
            o.getString("location"),
            o.getString("services"),
            o.getString("status"),
            o.getString("address"),
            o.getString("hours"),
            o.getString("phone")
        )
    }
}

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
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var mechanics by remember { mutableStateOf<List<Mechanic>>(emptyList()) }
    var selected by remember { mutableStateOf<Mechanic?>(null) }
    var screen by remember { mutableStateOf("home") }

    LaunchedEffect(Unit) {
        try {
            delay(800)
            mechanics = loadMechanics()
            loading = false
        } catch (e: Exception) {
            error = "Unable to load mechanic data"
            loading = false
        }
    }

    when {
        loading -> Box(Modifier.fillMaxSize().padding(24.dp)) {
            CircularProgressIndicator()
            Text("Loading mechanics...", modifier = Modifier.padding(top = 60.dp))
        }

        error != null -> Column(Modifier.padding(24.dp)) {
            Text("Error: $error")
            Button(onClick = {
                error = null
                loading = true
            }) {
                Text("Retry")
            }
        }

        screen == "home" -> HomeScreen(mechanics) {
            selected = it
            screen = "details"
        }

        screen == "details" && selected != null ->
            DetailsScreen(selected!!) { screen = "request" }

        screen == "request" && selected != null ->
            RequestScreen(selected!!) { screen = "confirmation" }

        screen == "confirmation" ->
            ConfirmationScreen { screen = "home" }
    }
}

@Composable
fun HomeScreen(
    mechanics: List<Mechanic>,
    onClick: (Mechanic) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Instant Mechanic", style = MaterialTheme.typography.headlineMedium)
        Text("Find a mechanic near you", modifier = Modifier.padding(vertical = 12.dp))

        LazyColumn {
            items(mechanics) { mechanic ->
                Card(
                    onClick = { onClick(mechanic) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(mechanic.name, style = MaterialTheme.typography.titleLarge)
                        Text("⭐ ${mechanic.rating}   •   ${mechanic.distance}")
                        Text("📍 ${mechanic.location}")
                        Text("Services: ${mechanic.services}")
                        Text("Status: ${mechanic.status}")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(mechanic: Mechanic, onRequest: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(mechanic.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("⭐ Rating: ${mechanic.rating}")
        Text("📍 Address: ${mechanic.address}")
        Text("🔧 Services: ${mechanic.services}")
        Text("🕒 Working Hours: ${mechanic.hours}")
        Text("📞 Phone: ${mechanic.phone}")

        Spacer(Modifier.height(20.dp))

        Button(onClick = onRequest) {
            Text("Request Service")
        }
    }
}

@Composable
fun RequestScreen(
    mechanic: Mechanic,
    onSubmit: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var vehicle by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Request Service", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(name, { name = it }, label = { Text("Customer Name") })
        OutlinedTextField(phone, { phone = it }, label = { Text("Phone Number") })
        OutlinedTextField(vehicle, { vehicle = it }, label = { Text("Vehicle Number") })
        OutlinedTextField(service, { service = it }, label = { Text("Select Service") })
        OutlinedTextField(problem, { problem = it }, label = { Text("Problem Description") })

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onSubmit,
            enabled = name.isNotBlank() && phone.isNotBlank()
        ) {
            Text("Submit Request")
        }
    }
}

@Composable
fun ConfirmationScreen(onHome: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("✅ Service Request Submitted!",
            style = MaterialTheme.typography.headlineSmall)

        Text(
            "Your service request has been received successfully.",
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(onClick = onHome) {
            Text("Back to Home")
        }
    }
}