package com.example.webj3demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webj3demo.ui.theme.Webj3DemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Webj3DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Greeting("eth转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    ETHWalletDemo(NetworkType.ETH_SEPOLIA).transaction()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("eth智能合约转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    ETHWalletDemo(NetworkType.ETH_SEPOLIA).transactionSmartContract()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("BNB转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    ETHWalletDemo(NetworkType.BNB_TESTNET).transaction()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("BNB智能合约转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    ETHWalletDemo(NetworkType.BNB_TESTNET).transactionSmartContract()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("polygon转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    ETHWalletDemo(NetworkType.POLYGON_AMOY).transaction()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("polygon智能合约转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    ETHWalletDemo(NetworkType.POLYGON_AMOY).transactionSmartContract()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("创建钱包", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    MyWallet.createWallet(this@MainActivity)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("创建钱包的btc地址", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    BTCWalletDemo.createBtcAddress()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        Greeting("btc转账", Modifier.clickable {
                            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                                try {
                                    BTCWalletDemo.transaction()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                    }

                }
            }
        }
    }

}

@Composable
fun Greeting(content: String, modifier: Modifier = Modifier) {
    Text(
        text = content,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Webj3DemoTheme {
        Greeting("Android")
    }
}