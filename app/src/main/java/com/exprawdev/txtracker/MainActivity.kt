
package com.exprawdev.txtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Wallet(
    val address: String,
    val coin: String,
    val symbol: String,
    val amount: String,
    val price: String,
    val label: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TxTrackerApp()
        }
    }
}

@Composable
fun TxTrackerApp() {
    val bg = Color(0xFF080D16)
    val card = Color(0xFF111A29)
    val green = Color(0xFF00D69A)

    var wallets by remember { mutableStateOf(listOf<Wallet>()) }
    var showDialog by remember { mutableStateOf(false) }
    var timezone by remember { mutableStateOf("Asia/Jakarta") }
    var language by remember { mutableStateOf("Indonesia") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {
        Text(
            "TX TRACKER",
            color = green,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "CRYPTO WALLET MONITOR",
            color = Color.Gray,
            fontSize = 10.sp
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MY WALLETS", color = Color.White, fontWeight = FontWeight.Bold)

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = green
                )
            ) {
                Text("+ ADD", color = Color.Black)
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(wallets) { wallet ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = card
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                wallet.coin + " (" + wallet.symbol + ")",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(wallet.label, color = green, fontSize = 11.sp)
                        }

                        Text(
                            wallet.address,
                            color = Color.Gray,
                            fontSize = 11.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Jumlah: ${wallet.amount} ${wallet.symbol}",
                            color = Color.White,
                            fontSize = 12.sp
                        )

                        Text(
                            "Harga beli: ${wallet.price}",
                            color = Color.White,
                            fontSize = 12.sp
                        )

                        TextButton(
                            onClick = {
                                wallets = wallets - wallet
                            }
                        ) {
                            Text("HAPUS", color = Color.Red)
                        }
                    }
                }
            }
        }

        Text("TIME ZONE", color = Color.Gray, fontSize = 10.sp)

        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(timezone, color = Color.White)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf(
                    "UTC",
                    "Asia/Jakarta",
                    "Asia/Makassar",
                    "Asia/Jayapura",
                    "Asia/Singapore",
                    "Asia/Tokyo"
                ).forEach { zone ->
                    DropdownMenuItem(
                        text = { Text(zone) },
                        onClick = {
                            timezone = zone
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(
            "Bahasa: $language",
            color = Color.Gray,
            fontSize = 11.sp
        )
    }

    if (showDialog) {
        AddWalletDialog(
            onDismiss = { showDialog = false },
            onAdd = {
                wallets = wallets + it
                showDialog = false
            }
        )
    }
}

@Composable
fun AddWalletDialog(
    onDismiss: () -> Unit,
    onAdd: (Wallet) -> Unit
) {
    var address by remember { mutableStateOf("") }
    var coin by remember { mutableStateOf("") }
    var symbol by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("Manual") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Wallet / Coin") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(address, { address = it }, label = { Text("Wallet Address") })
                OutlinedTextField(coin, { coin = it }, label = { Text("Nama Coin") })
                OutlinedTextField(symbol, { symbol = it }, label = { Text("Simbol Coin") })
                OutlinedTextField(amount, { amount = it }, label = { Text("Jumlah Coin") })
                OutlinedTextField(price, { price = it }, label = { Text("Harga Beli") })

                var expanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(label)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf(
                            "Manual",
                            "Insider",
                            "Sniper",
                            "Bundler",
                            "Whale",
                            "Team",
                            "Other"
                        ).forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    label = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (address.isNotBlank() && coin.isNotBlank()) {
                        onAdd(
                            Wallet(
                                address,
                                coin,
                                symbol,
                                amount,
                                price,
                                label
                            )
                        )
                    }
                }
            ) {
                Text("SIMPAN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("BATAL")
            }
        }
    )
}
