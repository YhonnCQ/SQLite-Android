package com.example.sqlite.screens.person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.example.sqlite.Person
import com.example.sqlite.PersonDao
import com.example.sqlite.PersonReaderDbHelper
import com.example.sqlite.ui.theme.SQLiteTheme

class PersonFragment : Fragment() {

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
                    var persons by remember { mutableStateOf(personDao.getPerson()) }
                    PersonScreen(
                        persons = persons,
                        navigationAddPerson = {
                            navigationAddPerson()
                        },
                        navigationEditPerson = { Person ->
                            navigationEditPerson(Person)
                        },
                        deletePerson = { Person ->
                            deletePerson(Person)
                            persons = personDao.getPerson()
                        }
                    )
                }
            }
        }
    }

    private fun navigationEditPerson(person: Person) {
        val action = PersonFragmentDirections.actionPersonFragmentToEditPersonFragment(person)
        findNavController().navigate(action)
    }

    private fun navigationAddPerson() {
        val action = PersonFragmentDirections.actionPersonFragmentToAddPersonFragment()
        findNavController().navigate(action)
    }

    private fun deletePerson(person: Person) {
        personDao.deletePerson(person)
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
fun PersonScreen(
    persons: List<Person>,
    navigationAddPerson: () -> Unit,
    navigationEditPerson: (Person) -> Unit,
    deletePerson: (Person) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Personas")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigationAddPerson()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { PaddingValues ->
        LazyColumn(modifier = Modifier.padding(PaddingValues)) {
            items(items = persons) { person ->
                CardPerson(person, navigationEditPerson, deletePerson)
            }
        }
    }
}

@Composable
fun CardPerson(
    person: Person,
    navigationEditPerson: (Person) -> Unit,
    deletePerson: (Person) -> Unit
) {
    var menuState by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
                Column() {
                    Text(text = person.name)
                    Text(text = person.age.toString())
                }
            }
            Row() {
                IconButton(onClick = { menuState = !menuState }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                }
                DropdownMenu(
                    expanded = menuState,
                    onDismissRequest = { menuState = false }
                ) {
                    DropdownMenuItem(onClick = { navigationEditPerson(person) }) {
                        Text(text = "Editar")
                    }
                    DropdownMenuItem(onClick = {
                        deletePerson(person)
                        menuState = false
                    }) {
                        Text(text = "Eliminar")
                    }
                }
            }
        }
    }
}