package com.poid.marveldemoapp.core.extension

import java.math.BigInteger
import java.security.MessageDigest

fun String.toMd5Hash(): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(this.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}