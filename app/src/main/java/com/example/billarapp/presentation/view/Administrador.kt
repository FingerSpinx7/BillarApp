package com.example.billarapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminScreen(mesas: List<String>, onMesaClick: (String) -> Unit, onPoolClick: () -> Unit, onCarambolaClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0E1D)) // Fondo oscuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mesas",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF7FD238), // Verde
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 24.sp
            )

            LazyColumn(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mesas) { mesa ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height(60.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E2C) // Color de la tarjeta
                        ),
                        onClick = { onMesaClick(mesa) }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = mesa,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }

        // Menú inferior con estilo personalizado
        BottomAppBar(
            containerColor = Color(0xFF1E1E2C),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onPoolClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = null
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "POOL",
                            tint = Color(0xFF7FD238),
                            modifier = Modifier.size(24.dp)
                        )
                        Text("POOL", color = Color(0xFF7FD238), fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = onCarambolaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    elevation = null
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "CARAMBOLA",
                            tint = Color(0xFF7FD238),
                            modifier = Modifier.size(24.dp)
                        )
                        Text("CARAMBOLA", color = Color(0xFF7FD238), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}