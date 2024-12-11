package com.example.webj3demo

import android.content.Context
import android.util.Log
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.core.Transaction
import org.bitcoinj.params.TestNet3Params
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils
import java.io.File
import java.security.SecureRandom

object MyWallet {
    val random = SecureRandom()
    val myKey = "0xdd65cb11a90f5efb9e530590d30d75d4b4dd537335e7003dcd5dcaeab9b2d4e1"
    val myM = "elbow vendor okay actress merry apart virus must vehicle worry round drill"
    val otherM = "happy lizard actual scorpion surround north random metal fetch burden canal novel"
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
        Log.e(
            "wjr",
            "credential39 : ${credential39.address} : ${credential39.ecKeyPair.privateKey} : ${credential39.ecKeyPair.publicKey}"
        )
        Log.e(
            "wjr",
            "credential44 : ${credential44.address} : ${credential44.ecKeyPair.privateKey} : ${credential44.ecKeyPair.publicKey}"
        )
    }

    fun hexStringToByteArray(hex: String): ByteArray {
        val byteArray = ByteArray(hex.length / 2)
        for (i in byteArray.indices) {
            byteArray[i] =
                ((hex[i * 2].digitToInt(16) shl 4) + hex[i * 2 + 1].digitToInt(16)).toByte()
        }
        return byteArray
    }
}