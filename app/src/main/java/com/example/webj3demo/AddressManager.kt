package com.example.webj3demo

import org.web3j.contracts.token.ERC20Interface
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

object AddressManager {

    fun getNodeAddress(type: NetworkType): String {
        return when (type) {
            NetworkType.ETH_SEPOLIA -> "https://sepolia.infura.io/v3/ed257b038fdf462ebb43a33f967277c9"
            NetworkType.BNB_TESTNET -> "https://data-seed-prebsc-1-s1.binance.org:8545/"
            NetworkType.POLYGON_AMOY -> "https://polygon-amoy.drpc.org"
            else -> ""
        }
    }

    fun getChainId(type: NetworkType): Long {
        return when (type) {
            NetworkType.ETH_SEPOLIA -> 11155111L
            NetworkType.BNB_TESTNET -> 97L
            NetworkType.POLYGON_AMOY -> 80002L
            else -> 0L
        }
    }

    fun getUSDTContractAddress(type: NetworkType): String {
        return when (type) {
            NetworkType.ETH_SEPOLIA -> "0xF4bB9F6634b7228ede7F0252771015ca193853Fa"
            NetworkType.BNB_TESTNET -> "0xF4bB9F6634b7228ede7F0252771015ca193853Fa"
            NetworkType.POLYGON_AMOY -> "0xdc171666753F655365a564504D8B13852E8F23E2"
            else -> ""
        }
    }

    fun getContract(
        web3j: Web3j,
        type: NetworkType,
        contractAddress: String,
        credentials: Credentials
    ): ERC20Interface<*, *>? {
        return when (type) {
            NetworkType.ETH_SEPOLIA -> ETHSepoliaContractAbi.load(
                contractAddress,
                web3j,
                RawTransactionManager(web3j, credentials, getChainId(type)),
                StaticGasProvider(
                    Contract.GAS_PRICE,
                    Contract.GAS_LIMIT
                )
            )

            NetworkType.BNB_TESTNET -> BNBTestnetContractAbi.load(
                contractAddress,
                web3j,
                RawTransactionManager(web3j, credentials, getChainId(type)),
                StaticGasProvider(
                    Contract.GAS_PRICE,
                    Contract.GAS_LIMIT
                )
            )

            NetworkType.POLYGON_AMOY -> PolygonAmoyContractAbi.load(
                contractAddress,
                web3j,
                RawTransactionManager(web3j, credentials, getChainId(type)),
                StaticGasProvider(
                    BigInteger.valueOf(32000000000L),
                    Contract.GAS_LIMIT
                )
            )

            else -> null
        }
    }

}

enum class NetworkType {
    ETH_SEPOLIA,
    BNB_TESTNET,
    POLYGON_AMOY
}