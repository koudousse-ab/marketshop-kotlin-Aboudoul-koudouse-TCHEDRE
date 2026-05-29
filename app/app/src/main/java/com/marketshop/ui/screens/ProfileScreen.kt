package com.marketshop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Profil") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isEditing) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Nom") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    label = { Text("Téléphone") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nom: ${state.name}")
                        Text("Email: ${state.email}")
                        Text("Téléphone: ${state.phone}")
                    }
                }
            }
            Button(
                onClick = { viewModel.toggleEditing() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isEditing) "Enregistrer" else "Modifier le profil")
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mode sombre")
                Switch(
                    checked = state.isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode() }
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            "Vider toutes les données ?",
                            actionLabel = "Confirmer",
                            withDismissAction = true
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.clearUserData {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Données supprimées")
                                }
                                navController.navigate("catalogue") {
                                    popUpTo("catalogue") { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Vider mes données")
            }
        }
    }
}

@Preview(showBackground = true, name = "Profile Preview")
@Composable
fun PreviewProfileScreen() {
    MarketShopTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nom: Jean Dupont")
                    Text("Email: jean@example.com")
                    Text("Téléphone: 0612345678")
                }
            }
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Modifier") }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Mode sombre")
                Switch(checked = false, onCheckedChange = {})
            }
            Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Vider les données")
            }
        }
    }
}
