package com.poid.marveldemoapp.data.local.entity

import androidx.room.*

@Entity(tableName = "table_comics")
data class ComicsEntity(
    @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
    @ColumnInfo(name = "column_title") val title: String?,
    @ColumnInfo(name = "column_description") val description: String?,
    @ColumnInfo(name = "column_prices") val prices: String?,
    @ColumnInfo(name = "column_thumbnail_url") val thumbnailUrl: String?,
    @ColumnInfo(name = "column_is_starred") val isStarred: Boolean? = false
)

@Entity(tableName = "fts_table_comics")
@Fts4(contentEntity = ComicsEntity::class)
data class ComicsEntityFTS(
    @ColumnInfo(name = "column_title") val title: String?,
    @ColumnInfo(name = "column_description") val description: String?
)