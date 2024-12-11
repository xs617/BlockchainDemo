package com.example.webj3demo

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import fr.acinq.bitcoin.Bitcoin.addressFromPublicKeyScript
import fr.acinq.bitcoin.Bitcoin.addressToPublicKeyScript
import fr.acinq.bitcoin.Block
import fr.acinq.bitcoin.BlockHash
import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.KeyPath
import fr.acinq.bitcoin.MnemonicCode
import fr.acinq.bitcoin.OutPoint
import fr.acinq.bitcoin.Satoshi
import fr.acinq.bitcoin.ScriptFlags
import fr.acinq.bitcoin.Transaction
import fr.acinq.bitcoin.TxId
import fr.acinq.bitcoin.TxIn
import fr.acinq.bitcoin.TxOut
import fr.acinq.bitcoin.XonlyPublicKey
import fr.acinq.bitcoin.sat
import fr.acinq.bitcoin.toSatoshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.bitcoinj.core.Base58
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Hash.sha256
import org.web3j.crypto.MnemonicUtils
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


object BTCWalletDemo {
    val networkType: NetworkType = NetworkType.BTC_TESTNET
    lateinit var api: API

    init {
        val trustAllCerts = object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustAllCerts), SecureRandom())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://mempool.space/testnet4/")
            .client(
                OkHttpClient.Builder()
//                    .sslSocketFactory(sslContext.socketFactory, trustAllCerts)
//                    .hostnameVerifier { hostname, session -> true }
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
        api = retrofit.create(API::class.java)
    }

    fun createBtcAddress() {
        val seed = MnemonicUtils.generateSeed(MyWallet.otherM, "")
        val masterKeypair = Bip32ECKeyPair.generateKeyPair(seed)
        // m/44'/60'/0'/0/0
        val legacyPairPath = intArrayOf(
            44 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
        val bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, legacyPairPath)
        val key = ECKey.fromPrivate(bip44Keypair.privateKey)

        val address = buildBTCP2PKH()
        val addressRoot = buildTapRoot(MyWallet.otherM)
        val addressP2PKH =
            org.bitcoinj.core.Address.fromKey(MainNetParams(), key, Script.ScriptType.P2PKH)
        Log.e("wjr", " P2PKH : $address")
        Log.e("wjr", " P2PKH : $addressP2PKH")
        Log.e("wjr", " TapRoot : $addressRoot")
    }

    fun buildBTCP2PKH(): String {
        val seed = MnemonicUtils.generateSeed(MyWallet.otherM, "")
        val masterKeypair = Bip32ECKeyPair.generateKeyPair(seed)
        // m/44'/60'/0'/0/0
        val legacyPairPath = intArrayOf(
            44 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
        val bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, legacyPairPath)
        val key = ECKey.fromPrivate(bip44Keypair.privateKey)

        // Step 1: 公钥进行 SHA-256 哈希
        val publicHash256 = sha256(key.pubKey)
        // Step 2: 对 SHA-256 哈希值进行 RIPEMD-160 哈希
        val publish160 = RIPEMD160Digest().let {
            it.update(publicHash256, 0, publicHash256.size)
            val result = ByteArray(it.digestSize)
            it.doFinal(result, 0)
            result
        }
        // Step 3: 加上版本字节（0x00 对于比特币主网）
        val adVersion = byteArrayOf(0x00.toByte()).plus(publish160)
        // Step 4: 计算校验和
        val checksum = sha256(sha256(adVersion)).copyOfRange(0, 4)
        // Step 5: 生成最终字节数组并进行 Base58 编码
        return Base58.encode(adVersion.plus(checksum))
    }

    fun buildTapRoot(mnemonic: String): String {
        val seed = MnemonicUtils.generateSeed(mnemonic, "")
        val masterKeypair = Bip32ECKeyPair.generateKeyPair(seed)
        // m/44'/60'/0'/0/0
        val legacyPairPath = intArrayOf(
            86 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
        val bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, legacyPairPath)
        val btcKey = ECKey.fromPrivate(bip44Keypair.privateKey)
        val hexTapTweak =
            "e80fe1639c9ca050e3af1b39c143c63e429cbceb15d940fbb5c5a1f4af57c5e9e80fe1639c9ca050e3af1b39c143c63e429cbceb15d940fbb5c5a1f4af57c5e9"
        val tapTweak = hexStringToByteArray(hexTapTweak)
        /// Tweaked Public Key Q = P + tG
        val p =
            btcKey.pubKey.copyOfRange(1, 33) // Replace this with the actual public key (33 bytes)
        val t = Sha256Hash.hash(tapTweak + p)
        val tG = ECKey.fromPrivate(t).pubKey.copyOfRange(1, 33)
        val tweakedPublicKeyQ = ECKey.fromPublicOnly(byteArrayOf(2) + p).pubKeyPoint.add(
            ECKey.fromPublicOnly(byteArrayOf(2) + tG).pubKeyPoint
        ).getEncoded(false).copyOfRange(1, 33)
        Log.e("wjr", "private Key = ${btcKey.getPrivateKeyEncoded(MainNetParams.get())}")
        try {
            return Bech32.encodeWitnessAddress("bc", 1, tweakedPublicKeyQ)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun buildTaprootWithChainHash(mnemonic: String, blockHash: BlockHash): String {
        val seed = MnemonicCode.toSeed(mnemonic, "")
        val master = DeterministicWallet.generate(seed)
        val accountKey = DeterministicWallet.derivePrivateKey(master, KeyPath("m/86'/0'/0'"))
        val key = DeterministicWallet.derivePrivateKey(accountKey, listOf(0L, 0L))
        val internalKey = XonlyPublicKey(key.publicKey)
        return internalKey.publicKey.p2trAddress(blockHash)
    }

    fun hexStringToByteArray(hex: String): ByteArray {
        val byteArray = ByteArray(hex.length / 2)
        for (i in byteArray.indices) {
            byteArray[i] =
                ((hex[i * 2].digitToInt(16) shl 4) + hex[i * 2 + 1].digitToInt(16)).toByte()
        }
        return byteArray
    }

    suspend fun transaction() {
        val mSeed = MnemonicCode.toSeed(MyWallet.myM, "")
        val mMaster = DeterministicWallet.generate(mSeed)
        val mAccountKey = DeterministicWallet.derivePrivateKey(mMaster, KeyPath("m/86'/0'/0'"))
        val mKey = DeterministicWallet.derivePrivateKey(mAccountKey, listOf(0L, 0L))
        val mInternalKey = XonlyPublicKey(mKey.publicKey)
        val myTapRoot = mInternalKey.publicKey.p2trAddress(Block.Testnet4GenesisBlock.hash)
        val otherTapRoot = "tb1pfjayymatcwdmxw89kmntzqprlqn0xmcekfkfv4g5gz8evu4smtvszl2567"
        val response = api.getAddressUtxo(myTapRoot).awaitResponse()
        Log.e("wjr", "utxo : ${response.body()}")
        val utxo = response.body()
        utxo?.let {
            val testTx = buildSignTx(utxo, mKey,otherTapRoot,0)
            val realTx = buildSignTx(utxo, mKey, otherTapRoot, testTx?.totalSize() ?: 0)
            val data =
                api.postTransaction(RequestBody.create("text/plain".toMediaType(), realTx.toString()))
            Log.e("wjr", "send : $realTx")
            val result = data.awaitResponse()
            Log.e("wjr", "send result: $result")
        }
    }

    suspend fun buildSignTx(
        utxo: List<UTXO>,
        myKey: DeterministicWallet.ExtendedPrivateKey,
        otherAddress: String,
        packageSize: Int
    ): Transaction? {
        val mInternalKey = XonlyPublicKey(myKey.publicKey)
        val myTapRoot = mInternalKey.publicKey.p2trAddress(Block.Testnet4GenesisBlock.hash)
        val total = utxo.fold(0L) { old, next ->
            old + next.value
        }
        val inputs = utxo.mapIndexed { index, item ->
            TxIn(
                OutPoint(TxId(item.txid), item.vout.toLong()),
                TxIn.SEQUENCE_FINAL
            )
        }
        val otherScript =
            addressToPublicKeyScript(Block.Testnet4GenesisBlock.hash, otherAddress).right!!
        val myScript = addressToPublicKeyScript(Block.Testnet4GenesisBlock.hash, myTapRoot).right!!
        val fee = api.getRecommendedFees().awaitResponse()
        val feeRate = (fee.body()?.fastestFee ?: 1L)
        val inList = inputs ?: listOf()
        // we want to spend
        val realOutList = listOf(
            TxOut(1000.sat(), otherScript),
            TxOut((total - 1000L - feeRate * packageSize).sat(), myScript)
        )
        val rawTx = Transaction(2, inList, realOutList, 0)
        Log.e("wjr", " base $total : fee: ${(fee.body()?.fastestFee ?: 1L) * packageSize}")
        val sigHashType = 0
        val outInput = utxo.map { TxOut(it.value.sat(), myScript) }
        val signWitness = outInput?.mapIndexed { index, txOut ->
            val sign = Transaction.signInputTaprootKeyPath(
                myKey.privateKey,
                rawTx,
                index,
                outInput,
                sigHashType,
                scriptTree = null
            )
            fr.acinq.bitcoin.Script.witnessKeyPathPay2tr(sign)
        }
        if (signWitness != null) {
            return rawTx.updateWitnesses(signWitness)
        }
        return null
    }
}




