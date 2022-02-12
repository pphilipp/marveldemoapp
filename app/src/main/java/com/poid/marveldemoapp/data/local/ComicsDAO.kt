package com.poid.marveldemoapp.data.local

import androidx.room.*
import com.poid.marveldemoapp.data.local.entity.ComicsEntity
import com.poid.marveldemoapp.data.local.entity.ComicsEntityFTS
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicsDAO {

    @Query("SELECT * FROM table_comics")
    fun loadAllComics(): Flow<List<ComicsEntity>>

    @Query("SELECT * FROM table_comics WHERE column_is_starred=1")
    fun loadStarredComics(): Flow<List<ComicsEntity>>

    @Query(
        """
        UPDATE table_comics 
        SET column_is_starred = :isStarred 
        WHERE rowid=:comicsId
        """
    )
    fun setComicsIsStarred(comicsId: String, isStarred: Int)

    @Query(
        """
        SELECT *
        FROM table_comics
        JOIN fts_table_comics ON table_comics.column_title = fts_table_comics.column_title
        WHERE fts_table_comics MATCH :query
        """
    )
    fun searchComicsByText(query: String): Flow<List<ComicsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComics(comicsEntity: List<ComicsEntity>)

    @Delete
    fun delete(comicsEntity: ComicsEntity)

}