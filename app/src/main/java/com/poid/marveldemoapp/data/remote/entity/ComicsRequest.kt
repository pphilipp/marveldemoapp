package com.poid.marveldemoapp.data.remote.entity

const val ORDER_BY_TITLE = "title"
const val ORDER_BY_FOC_DATE = "focDate"
const val ORDER_BY_ON_SALE_DATE = "onsaleDate"
const val ORDER_BY_ISSUE_NUMBER = "issueNumber"

data class ComicsRequest(
    val limit: Int,
    val orderBy: String? = ORDER_BY_TITLE,
    val dateRange: String? = ""
)