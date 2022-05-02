package com.example.sqlite.screens.add_person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.example.sqlite.Person
import com.example.sqlite.PersonDao
import com.example.sqlite.PersonReaderDbHelper
import com.example.sqlite.ui.theme.SQLiteTheme

class AddPersonFragment : Fragment() {

    private val personDao: PersonDao by lazy { PersonDao(PersonReaderDbHelper(requireContext())) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyApp {
                    AddPersonScreen(
                        addPerson = { name: String, age: Int ->
                            addPerson(name,age)
                        },
                        navigationPerson = {
                            navigationPerson()
                        }
                    )
                }
            }
        }
    }

    private fun addPerson(name: String, age: Int) {
        personDao.insertPerson(Person(name = name, age = age))
    }

    private fun navigationPerson() {
        val action = AddPersonFragmentDirections.actionAddPersonFragmentToPersonFragment()
        findNavController().navigate(action)
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    SQLiteTheme() {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Composable
fun AddPersonScreen(addPerson: (name: String, age: Int) -> Unit, navigationPerson: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("" ) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Agregar Persona")
                },
                actions = {
                    IconButton(onClick = {
                        addPerson(name, age.toInt())
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "add")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigationPerson() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            OutlinedTextField(
                value = name,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { name = it },
                label = { Text("Nombre") }
            )
            OutlinedTextField(
                value = age,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { age = it},
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
        }
    }
}