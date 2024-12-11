package com.example.webj3demo

import android.util.Log
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthCall
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger


class ETHWalletDemo(val networkType: NetworkType) {
    val usdtContractAddress = AddressManager.getUSDTContractAddress(networkType)
    val chainAddress = AddressManager.getNodeAddress(networkType)
    val chainId = AddressManager.getChainId(networkType)
    val web3j = Web3j.build(HttpService(chainAddress))

    fun transaction() {
        try {
            val result = web3j.web3ClientVersion().sendAsync().get().web3ClientVersion
            Log.e("wjr", "version :  $result")
            // 通过私钥创建 Credentials 对象
            val credentials: Credentials = Credentials.create(MyWallet.myKey)
            // 获取账户地址
            val fromAddress: String = credentials.address
            // 获取账户余额
            val balance: EthGetBalance =
                web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.PENDING).send()
            val ethBalance = Convert.fromWei(balance.balance.toString(), Convert.Unit.ETHER)
            val amount = BigDecimal(0.001)
            val valueInWei: BigDecimal = Convert.toWei(amount, Convert.Unit.ETHER)
            val credential44 = Bip44WalletUtils.loadBip44Credentials("", MyWallet.otherM)
            val toAddress = credential44.address
            Log.e("wjr", "address : $fromAddress : $toAddress : ${balance.balance} : $ethBalance")
//            val toAddress = "0x980e77e6ae3efb8d4889c84c8644611a087d192e"

            val ethGetTransactionCount =
                web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send()
            val nonce = ethGetTransactionCount.transactionCount
            var gas = web3j.ethGasPrice().send()
            val rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                Contract.GAS_PRICE.multiply(BigInteger("2")),
                Contract.GAS_LIMIT,
                toAddress,
                valueInWei.toBigInteger()
            )
            Log.d("wjr", "Transaction nonce: $nonce ：gas ${gas.gasPrice}")
            val signedMessage =
                TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
            val hexValue = Numeric.toHexString(signedMessage)
            val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()
            Log.d("wjr", "Transaction Hash: ${ethSendTransaction.transactionHash}")
            Log.d(
                "wjr",
                "Transaction error: ${ethSendTransaction.error.code} ${ethSendTransaction.error.message}"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun transactionSmartContract() {
        val credentials: Credentials = Credentials.create(MyWallet.myM)
        val credential44 = Bip44WalletUtils.loadBip44Credentials("", MyWallet.otherM)
        val toAddress = credential44.address
        val amount = BigDecimal(0.3)
        val valueInWei: BigDecimal = Convert.toWei(amount, Convert.Unit.ETHER)
        Log.e("wjr", "SmartContract address ${credentials.address} $toAddress")
//        ///授权智能合约额度
//        usdtApprove(web3j, credentials, valueInWei.toBigInteger())
//        usdtTransferFrom(web3j, credentials, toAddress, valueInWei.toBigInteger())
//        ///使用智能合约转账
//        usdtTransfer(web3j, credentials, toAddress, valueInWei.toBigInteger())

        ///直击使用生成的类
        usdtBalanceOf(web3j, credentials)
        genTransaction(web3j, credentials, toAddress, valueInWei.toBigInteger())
    }

    fun genTransaction(
        web3j: Web3j,
        credentials: Credentials,
        toAddress: String,
        value: BigInteger
    ) {
        try {
            val contractAbi =
                AddressManager.getContract(web3j, networkType, usdtContractAddress, credentials)
            val result = contractAbi?.transfer(toAddress, value)?.send()
            Log.e("wjr", "genTransaction  $result")
            usdtBalanceOf(web3j, credentials)
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

    fun usdtSubscribe(web3j: Web3j, credentials: Credentials) {
        val contractAbi = AddressManager.getContract(
            web3j, networkType, usdtContractAddress, credentials
        )
        contractAbi?.transferEventFlowable(
            DefaultBlockParameterName.LATEST,
            DefaultBlockParameterName.LATEST,
        )?.subscribe {
            Log.e("wjr", "subscribe even ${it}")
        }
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

    fun contractView(
        web3j: Web3j,
        fromAddress: String,
        contractAddress: String,
        data: String
    ): EthCall? {
        try {
            val transaction =
                Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
            val ethCall =
                web3j.ethCall(transaction, DefaultBlockParameterName.PENDING).send();
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
            Contract.GAS_PRICE,
            Contract.GAS_LIMIT,
            contractAddress,
            data
        )
        Log.e("wjr", "contractTransact data $data")
        try {
            // 发送交易
            val signedMessage =
                TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
            val hexValue = Numeric.toHexString(signedMessage)
            val ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send()
            Log.e("wjr", "contractView result ${ethSendTransaction.transactionHash}")
            Log.e(
                "wjr",
                "contractView error ${ethSendTransaction.error.code} ${ethSendTransaction.error.message}"
            )
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