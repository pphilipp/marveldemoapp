package com.poid.marveldemoapp.di

import android.app.Application
import com.poid.marveldemoapp.data.local.ComicsDataBase

class DataBaseComposition(
    applicationContext: Application
) {

    private val db by lazy { ComicsDataBase.getDatabase(applicationContext) }

    val dao by lazy { db.comicsDao() }
}