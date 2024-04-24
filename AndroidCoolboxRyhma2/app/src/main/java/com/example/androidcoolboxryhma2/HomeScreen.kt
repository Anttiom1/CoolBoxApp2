package com.example.androidcoolboxryhma2

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcoolboxryhma2.viewmodel.GraphScreenViewModel
import com.example.androidcoolboxryhma2.viewmodel.HomeViewModel


@Composable
fun ConfirmLogout(
    loading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    color: Color
){
    val context = LocalContext.current

    LaunchedEffect(key1 = error){
        error?.let {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = color),
                onClick = {onConfirm()},
                enabled = !loading) {
                Text("Logout")
            }
        },
        icon = { Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Logout")},
        text = {
            Box(modifier = Modifier.fillMaxWidth()){
                when{
                    loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    else -> Text("Are you sure you want to logout?")
                }
            }
        },
        title = { Text("Logout")},
        dismissButton = {
            TextButton(onClick = {onDismiss()}) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit,
    goToGraph: () -> Unit,
    logOut: () -> Unit)
{
    val vm: HomeViewModel = viewModel()
    val vM: GraphScreenViewModel = viewModel()

    LaunchedEffect(key1 = vm.logoutState.value.logoutDone) {
        if (vm.logoutState.value.logoutDone) {
            vm.setLogout(false)
            logOut()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onMenuClick() }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(
                            id = R.string.Menu
                        ))

                }},
                title = { stringResource(id = R.string.Home) },
                actions = {
                    IconButton(onClick = { vm.setConfirmLogout(true) }) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(
                            id = R.string.Logout
                        ))
                    }
                }
            )
    }) {
        when {
            vm.logoutState.value.logout -> ConfirmLogout(
                loading = vm.logoutState.value.loading,
                error = vm.logoutState.value.error,
                onDismiss = { vm.setConfirmLogout(false) },
                onConfirm = { vm.logout() },
                color = Color.Red
            )

            else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .height(100.dp)
                ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(50.dp),
                    text = "CoolBox",
                    fontSize = 32.sp
                )

                Row(
                    modifier = Modifier
                        .height(100.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally), text = stringResource(id = R.string.Temperature)
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text("In: ${vM._indoorTemperatureState.value.list.lastOrNull()?.value}°C")
                            Text("Out: ${vM.temperatureState.value.list.lastOrNull()?.value}°C")

                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally), text = stringResource(id = R.string.ElectricityConsumption)
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text("Weekly: ${vM._energyStateWeekly.value.list.lastOrNull()?.totalConsumedAmount}")
                                Text("Daily: ${vM.energyState.value.list.lastOrNull()?.totalConsumedAmount}")
                            }
                        }
                    }
                }
            }
        }
    }
}


