package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private var expanses: Expanses = Expanses()
    private var events: Events= Events()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expanses = ExpensePreferences.loadExpenses(this)
        events = EventPreferences.loadExpenses(this)
        setContent {
            MyApplicationTheme {
                navController = rememberNavController()  // Initialize NavController

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(navController, expanses, events)  // Setup navigation
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        ExpensePreferences.saveExpenses(this, expanses)
        EventPreferences.saveExpenses(this, events)
    }
}

@Composable
fun AppNavigation(navController: NavHostController, expanses: Expanses,events: Events) {
    NavHost(
        navController = navController,
        startDestination = "main_screen"  // Must be a String route
    ) {
        composable("main_screen") {
            BlankForm(expanses,navController)
        }
        composable("first_fragment") {
            FirstFragmentUI(navController,events)
        }
        composable("second_fragment") {
            SecondFragmentUI(navController,events)
        }
    }
}


@Composable
fun BlankForm(expanses: Expanses, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }
    val expanse= remember{Expanses()}
    var texts by remember { mutableStateOf("") }
    var searched by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // To control menu visibility
    var selectedOption by remember { mutableStateOf("Select category") } // Default selection
    val categories = listOf("Food", "Travel", "Shopping", "Bills") // Options for the dropdown


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {  // Detect left swipe
                        navController.navigate("first_fragment")  // Navigate to FirstFragment
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )  {
        // Row for input fields (name and score)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            // Score input
            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text("Amount") },
                modifier = Modifier.weight(1f)
            )
            Column {
                Button(onClick = { expanded = true },shape=RectangleShape) {
                    Text(selectedOption)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedOption = category
                                expanded = false // Close dropdown after selection
                            }
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to submit the data (currently does nothing)
        Button(
            onClick = {
                val scoreInt = score.toIntOrNull()
                if (name.isNotBlank() && scoreInt != null)
                {
                    expanses.addExpanses(name,scoreInt, Date())
                    texts = expanses.allExpanses()
                    name = ""  // Clear the name field
                    score = ""

                }
                // Action for button click, can be extended later
            }
        ) {
            Text("Submit", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Display the list of students
        Column {
            Text(text = texts, fontSize = 14.sp ,maxLines = 10)

        }
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
                value = searched,
                onValueChange = { searched = it },
                label = { Text(text = "find") },
                modifier = Modifier.height(60.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
            if (searched.isNotBlank())
                {
                    result=expanse.getSpend(searched)
                    searched = ""

                }

                // Action for button click, can be extended later
            }
        ) {
            Text("Find", fontSize = 16.sp)
        }
        Column (modifier = Modifier.height(16.dp)){
            Text(text = result, fontSize = 14.sp)
        }
    }
}
@Composable
fun FirstFragmentUI(navController: NavController,event:Events) {
    var name by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf(Calendar.getInstance()) }
    var description by rememberSaveable { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }  // State to show error message
    val context = LocalContext.current  // Obtain the context to show the DatePickerDialog

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        // Left swipe - Navigate to "main_screen"
                        navController.navigate("main_screen")
                    } else if (dragAmount > 50) {
                        // Right swipe - Navigate to "second_fragment"
                        navController.navigate("second_fragment")
                    }
                }

            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Events", fontSize = 24.sp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name input
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (name.isNotEmpty()) showError = false  // Clear error if name is provided
                },
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.weight(1f)
            )

            // Date input button
            Button(onClick = {
                // Invoke DatePickerDialog (not a composable function)
                android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val newDate = Calendar.getInstance()
                        newDate.set(year, month, dayOfMonth)
                        selectedDate = newDate
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Text("Pick Date")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display selected date
        Text(text = "Selected Date: ${selectedDate.time.toFormattedString()}")

        Spacer(modifier = Modifier.height(16.dp))

        // Error message if name is empty
        if (showError && (description.isEmpty()||name.isEmpty())) {
            Text(text = "Please enter a Name and Description", color = MaterialTheme.colorScheme.error)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    if (description.isNotEmpty()) showError = false  // Clear error if name is provided
                },
                label = { Text("Description") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.weight(1f).height(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        Button(
            onClick = {
                if (name.isNotEmpty()&&description.isNotEmpty()) {
                    event.createEvent(name, selectedDate.time, description)
                    // Clear error message
                    showError = false
                } else {
                    showError = true  // Show error if name is empty
                }
            }
        ) {
            Text("Submit", fontSize = 16.sp)
        }
    }
}
@Composable
fun SecondFragmentUI(navController: NavController, events: Events) {
    var flip by remember { mutableStateOf(true) }

    val listOfEvent = remember(flip) { events.listNearEvents(flip, 10) }
    val items: List<String> = listOfEvent.map { it.tags }
    val title = if (flip) "Upcoming Events" else "Past Events"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        // Left swipe - Navigate to "main_screen"
                        navController.navigate("first_fragment")
                    } else if (dragAmount > 50) {
                        // Right swipe - Navigate to "second_fragment"
                        navController.navigate("main_screen")
                    }
                }

            }

    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Constrain LazyColumn height
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    ListItemView(item)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display button below LazyColumn
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { flip = !flip }) {
                Text("Toggle Events")
            }
        }
    }
}

@Composable
fun ThirdFragmentUI(navController: NavController, expanses: Expanses){
    val expanse= remember{Expanses()}
}

@Composable
fun ListItemView(item: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                // Action for button click
            }) {
                Text("Click")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BlankFormPreview() {
    val mockExpenses = Expanses().apply {
        addExpanses("Food", 100, Date())
        addExpanses("Travel", 200, Date())
    }
    val today = Date()
    val newDate = addDaysToDate(today, 5)
    val mockEvent =Events().apply {
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",newDate,"eadas")
        createEvent("adsdw",newDate,"eadas")
        createEvent("adsdw",newDate,"eadas")
        createEvent("adsdw",newDate,"eadas")
        createEvent("adsdw",newDate,"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")
        createEvent("adsdw",Date(),"eadas")




     }
    MyApplicationTheme {
        SecondFragmentUI(rememberNavController(),mockEvent)
    }
}
