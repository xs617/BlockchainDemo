package com.example.webj3demo

class MyTransaction {
    val fee: Int? = null
    val locktime: Long? = null
    val sigops: Int? = null
    val size: Int? = null
    val status: Status? = null
    val txid: String? = null
    val version: Long? = null
    val vin: List<Vin?>? = null
    val vout: List<Vout?>? = null
    val weight: Int? = null
}

class Status {
    val block_hash: String? = null
    val block_height: Int? = null
    val block_time: Long? = null
    val confirmed: Boolean? = null
}

class Vin {
    val is_coinbase: Boolean? = null
    val prevout: Prevout? = null
    val scriptsig: String? = null
    val scriptsig_asm: String? = null
    val sequence: Long? = null
    val txid: String? = null
    val vout: Int? = null
    val witness: List<String?>? = null
}

class Vout {
    val scriptpubkey: String? = null
    val scriptpubkey_address: String? = null
    val scriptpubkey_asm: String? = null
    val scriptpubkey_type: String? = null
    val value: Long? = null
}

class Prevout {
    val scriptpubkey: String? = null
    val scriptpubkey_address: String? = null
    val scriptpubkey_asm: String? = null
    val scriptpubkey_type: String? = null
    val value: Long? = null
}