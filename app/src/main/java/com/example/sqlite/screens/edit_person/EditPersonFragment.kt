package com.example.sqlite.screens.edit_person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sqlite.Person
import com.example.sqlite.PersonDao
import com.example.sqlite.PersonReaderDbHelper
import com.example.sqlite.ui.theme.SQLiteTheme

class EditPersonFragment : Fragment() {

    private val args: EditPersonFragmentArgs by navArgs()
    private val personDao: PersonDao by lazy { PersonDao(PersonReaderDbHelper(requireContext())) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MyApp {
                    val person by remember { mutableStateOf(args.person) }
                    EditPersonScreen(
                        person = person,
                        editPerson = { Person ->
                            updatePerson(Person)
                        },
                        navigationPerson = {
                            navigationPerson()
                        }
                    )
                }
            }
        }
    }

    private fun updatePerson(person: Person) {
        personDao.updatePerson(person)
    }

    private fun navigationPerson() {
        val action = EditPersonFragmentDirections.actionEditPersonFragmentToPersonFragment()
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
fun EditPersonScreen(person: Person, editPerson: (Person) -> Unit, navigationPerson: () -> Unit) {
    var name by remember { mutableStateOf(person.name) }
    var age by remember { mutableStateOf(person.age.toString()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Editar Persona")
                },
                actions = {
                    IconButton(onClick = {
                        editPerson(Person(person.id, name, age.toInt()))
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "add")
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