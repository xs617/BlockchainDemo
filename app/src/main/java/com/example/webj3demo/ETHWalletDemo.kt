package com.example.webj3demo

import android.content.Context
import android.util.Log
import org.bitcoinj.core.ECKey
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.security.SecureRandom


object ETHWalletDemo {
    val random = SecureRandom()
    val usdtContractAddress = "0xF4bB9F6634b7228ede7F0252771015ca193853Fa"
    val ethNodeAddress = "https://sepolia.infura.io/v3/ed257b038fdf462ebb43a33f967277c9"
//    val ethNodeAddress = "https://mainnet.infura.io/v3/ed257b038fdf462ebb43a33f967277c9"
    val testMyKey = "0xdd65cb11a90f5efb9e530590d30d75d4b4dd537335e7003dcd5dcaeab9b2d4e1"
    val testOtherMnemonic =
        "happy lizard actual scorpion surround north random metal fetch burden canal novel"
    val web3j = Web3j.build(HttpService(ethNodeAddress))
    fun createWallet(context: Context) {
        val initialEntropy = ByteArray(16)
        random.nextBytes(initialEntropy)
        val mnemonic = MnemonicUtils.generateMnemonic(initialEntropy)

        val path =
            File(context.filesDir.absolutePath + File.separator + "store")
        if (!path.exists()) {
            path.mkdirs()
        }
        Log.e("wjr", "---- ---- mnemonic : $mnemonic")
        val wallet = WalletUtils.generateBip39WalletFromMnemonic(
            "",
            mnemonic,
            path
        )
        val credential39 = WalletUtils.loadBip39Credentials("", mnemonic)
        val credential44 = Bip44WalletUtils.loadBip44Credentials("", mnemonic)
        Log.e("wjr", "credential39 : ${credential39.address} : ${credential39.ecKeyPair.privateKey} : ${credential39.ecKeyPair.publicKey}")
        Log.e("wjr", "credential44 : ${credential44.address} : ${credential44.ecKeyPair.privateKey} : ${credential44.ecKeyPair.publicKey}")

        val seed = MnemonicUtils.generateSeed(mnemonic, "")
        val masterKeypair = Bip32ECKeyPair.generateKeyPair(seed)
        // m/44'/60'/0'/0/0
        val pairPath = intArrayOf(
            44 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
        val bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, pairPath)
        val key = ECKey.fromPrivate(bip44Keypair.privateKey)
        val addressP2PKH = org.bitcoinj.core.Address.fromKey(MainNetParams(), key, Script.ScriptType.P2PKH)
        val addressP2WPKH = org.bitcoinj.core.Address.fromKey(MainNetParams(), key, Script.ScriptType.P2WPKH)
        Log.e("wjr", "credentialBtc44 : $addressP2PKH :  $addressP2WPKH : ")
    }

    fun transactionSepoliaETH(web3j: Web3j) {
        try {
            val result = web3j.web3ClientVersion().sendAsync().get().web3ClientVersion
            Log.e("wjr", "version :  $result")
            // 通过私钥创建 Credentials 对象
            val credentials: Credentials = Credentials.create(testMyKey)
            // 获取账户地址
            val fromAddress: String = credentials.address
            // 获取账户余额
            val balance: EthGetBalance =
                web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST).send()
            val ethBalance = Convert.fromWei(balance.balance.toString(), Convert.Unit.ETHER)
            val amount = BigDecimal(0.01)
            val valueInWei: BigDecimal = Convert.toWei(amount, Convert.Unit.ETHER)
            val credential44 = Bip44WalletUtils.loadBip44Credentials("", testOtherMnemonic)
            val toAddress = credential44.address
            Log.e("wjr", "address : $fromAddress : $toAddress : ${balance.balance} : $ethBalance")
//            val toAddress = "0x980e77e6ae3efb8d4889c84c8644611a087d192e"

            val ethGetTransactionCount =
                web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send()
            val nonce = ethGetTransactionCount.transactionCount
            val rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                DefaultGasProvider.GAS_PRICE.multiply(BigInteger("2")),
                DefaultGasProvider.GAS_LIMIT,
                toAddress,
                valueInWei.toBigInteger()
            )
            Log.d("wjr", "Transaction nonce: $nonce ：gas ${DefaultGasProvider.GAS_PRICE}")
            val signedMessage =
                TransactionEncoder.signMessage(rawTransaction, 11155111, credentials)
            val hexValue = Numeric.toHexString(signedMessage)
            val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()
            Log.d("wjr", "Transaction Hash: ${ethSendTransaction.transactionHash}")
            Log.d("wjr", "Transaction error: ${ethSendTransaction.error.code} ${ethSendTransaction.error.message}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun transactionSmartContract() {
        val credentials: Credentials = Credentials.create(testMyKey)
        val credential44 = Bip44WalletUtils.loadBip44Credentials("", testOtherMnemonic)
        val toAddress = credential44.address
        val amount = BigDecimal(3)
        val valueInWei: BigDecimal = Convert.toWei(amount, Convert.Unit.ETHER)
        Log.e("wjr", "SmartContract address ${credentials.address} $toAddress")
//        ///授权智能合约额度
//        usdtApprove(web3j, credentials, valueInWei.toBigInteger())
//        usdtTransferFrom(web3j, credentials, toAddress, valueInWei.toBigInteger())
//        ///使用智能合约转账
//        usdtTransfer(web3j, credentials, toAddress, valueInWei.toBigInteger())

        ///直击使用生成的类
        genTransaction(web3j, credentials, toAddress, valueInWei.toBigInteger())
        usdtBalanceOf(web3j,credentials)
    }

    fun genTransaction(
        web3j: Web3j,
        credentials: Credentials,
        toAddress: String,
        value: BigInteger
    ) {
        try {
            val contractAbi =
                ContractAbi.load(
                    usdtContractAddress,
                    web3j,
                    RawTransactionManager(web3j, credentials, 11155111),
                    StaticGasProvider(
                        DefaultGasProvider.GAS_PRICE * BigInteger("2"),
                        DefaultGasProvider.GAS_LIMIT
                    )
                )
//            val result = contractAbi.transfer(toAddress, value).send()
            val result = contractAbi.balanceOf(credentials.address).send()
            Log.e("wjr", "genTransaction  $result")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("wjr", "genTransaction error  ${e.message}")
        }
    }

    fun usdtBalanceOf(web3j: Web3j, credentials: Credentials) {
        val function = Function(
            "balanceOf",
            listOf(Address(credentials.address)),
            listOf(object : TypeReference<Uint256>() {})
        )
        val data = FunctionEncoder.encode(function)
        val result = contractView(web3j, credentials.address, usdtContractAddress, data)
        Log.e(
            "wjr",
            "usdtBalanceOf : ${BigInteger(result?.result?.substring(2, result.result.length), 16)}"
        )
    }

    fun usdtApprove(web3j: Web3j, credentials: Credentials, value: BigInteger) {
        val function = Function(
            "approve",
            listOf(Address(credentials.address), Uint256(value)),
            listOf(object : TypeReference<Bool>() {})
        )
        val data = FunctionEncoder.encode(function)
        contractTransact(web3j, credentials, usdtContractAddress, data)
    }

    fun usdtTransferFrom(
        web3j: Web3j,
        credentials: Credentials,
        toAddress: String,
        value: BigInteger
    ) {
        Log.e("wjr", "usdtApprove address ${credentials.address}")
        val function = Function(
            "transferFrom",
            listOf(Address(credentials.address), Address(toAddress), Uint256(value)),
            listOf(object : TypeReference<Bool>() {})
        )
        val data = FunctionEncoder.encode(function)
        contractTransact(web3j, credentials, usdtContractAddress, data)
    }

    fun usdtTransfer(web3j: Web3j, credentials: Credentials, toAddress: String, value: BigInteger) {
        Log.e("wjr", "usdtTransfer address ${credentials.address} $toAddress")
        val function = Function(
            "transfer",
            listOf(Address(toAddress), Uint256(value)),
            listOf(object : TypeReference<Bool>() {})
        )
        val data = FunctionEncoder.encode(function)
        contractTransact(web3j, credentials, usdtContractAddress, data)
    }

    fun contractView(web3j: Web3j, fromAddress: String, contractAddress: String, data: String) : EthCall? {
        try {
            val transaction =
                Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
            val ethCall =
                web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            Log.e("wjr", "contractView data $data")
            Log.e("wjr", "contractView result ${ethCall.result}")
            Log.e("wjr", "contractView error ${ethCall.error?.code} ${ethCall.error?.message}")
            return ethCall
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return null
    }

    fun contractTransact(
        web3j: Web3j,
        credentials: Credentials,
        contractAddress: String,
        data: String
    ) {
        val ethGetTransactionCount =
            web3j.ethGetTransactionCount(credentials.address, DefaultBlockParameterName.PENDING)
                .send()
        val nonce = ethGetTransactionCount.transactionCount
        val rawTransaction = RawTransaction.createTransaction(
            nonce,
            DefaultGasProvider.GAS_PRICE.multiply(BigInteger("2")),
            DefaultGasProvider.GAS_LIMIT,
            contractAddress,
            data
        )
        Log.e("wjr", "contractTransact data $data")
        try {
            // 发送交易
            val signedMessage =
                TransactionEncoder.signMessage(rawTransaction, 11155111, credentials)
            val hexValue = Numeric.toHexString(signedMessage)
            val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()
            Log.e("wjr", "contractView result ${ethSendTransaction.transactionHash}")
            Log.e("wjr", "contractView error ${ethSendTransaction.error.code} ${ethSendTransaction.error.message}")
            val receipt = web3j.ethGetTransactionReceipt(ethSendTransaction.transactionHash).send()

            if (receipt.result != null) {
                // 交易已被确认
            } else {
                // 交易未成功或正在等待确认
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}