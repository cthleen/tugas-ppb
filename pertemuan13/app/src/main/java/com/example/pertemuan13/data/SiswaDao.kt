package com.example.pertemuan13.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SiswaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiswa(siswa: Siswa)

    @Update
    suspend fun updateSiswa(siswa: Siswa)

    @Delete
    suspend fun deleteSiswa(siswa: Siswa)

    @Query("SELECT * FROM siswa ORDER BY tanggalDaftar DESC")
    fun getAllSiswa(): Flow<List<Siswa>>

    @Query("""
        SELECT * FROM siswa 
        WHERE nama LIKE '%' || :query || '%' 
           OR email LIKE '%' || :query || '%' 
           OR nomorHp LIKE '%' || :query || '%'
        ORDER BY tanggalDaftar DESC
    """)
    fun searchSiswa(query: String): Flow<List<Siswa>>

    @Query("""
        SELECT * FROM siswa 
        WHERE nama LIKE '%' || :query || '%'
        ORDER BY tanggalDaftar DESC
    """)
    fun searchByNama(query: String): Flow<List<Siswa>>

    @Query("""
        SELECT * FROM siswa 
        WHERE email LIKE '%' || :query || '%'
        ORDER BY tanggalDaftar DESC
    """)
    fun searchByEmail(query: String): Flow<List<Siswa>>

    @Query("""
        SELECT * FROM siswa 
        WHERE nomorHp LIKE '%' || :query || '%'
        ORDER BY tanggalDaftar DESC
    """)
    fun searchByNomorHp(query: String): Flow<List<Siswa>>

    @Query("SELECT COUNT(*) FROM siswa")
    fun getSiswaCount(): Flow<Int>
}
