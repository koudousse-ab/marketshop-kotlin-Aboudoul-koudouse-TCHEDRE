package com.marketshop.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val appVersion = getAppVersion(context)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Profil") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Informations utilisateur
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nom: ${state.name}", style = MaterialTheme.typography.titleMedium)
                    Text("Email: ${state.email}")
                }
            }

            // Modification du profil
            if (state.isEditing) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Nom complet") },
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
                Button(
                    onClick = { viewModel.toggleEditing() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enregistrer")
                }
            } else {
                Button(
                    onClick = { viewModel.toggleEditing() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Modifier le profil")
                }
            }

            // Section Thème : Switch clair/sombre
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mode sombre", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = state.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() }
                    )
                }
            }

            // Section À propos
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("À propos", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("MarketShop - Application de mini e-commerce")
                    Text("Version $appVersion")
                    Text("© 2025 - Tous droits réservés")
                }
            }

            // Déconnexion
            Button(
                onClick = {
                    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    prefs.edit().clear().apply()
                    scope.launch {
                        snackbarHostState.showSnackbar("Déconnecté")
                        navController.navigate("login") {
                            popUpTo("catalogue") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Se déconnecter")
            }

            Divider()

            // Vider les données
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

// Fonction pour récupérer la version de l'application
fun getAppVersion(context: Context): String {
    return try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName ?: "1.0"
    } catch (e: PackageManager.NameNotFoundException) {
        "1.0"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MarketShopTheme {
        ProfileScreen(navController = rememberNavController())
    }
}
