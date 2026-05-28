package com.example.pertemuan13.data

import kotlinx.coroutines.flow.Flow

class SiswaRepository(private val siswaDao: SiswaDao) {

    fun getAllSiswa(): Flow<List<Siswa>> = siswaDao.getAllSiswa()

    fun searchSiswa(query: String): Flow<List<Siswa>> = siswaDao.searchSiswa(query)

    fun searchByNama(query: String): Flow<List<Siswa>> = siswaDao.searchByNama(query)

    fun searchByEmail(query: String): Flow<List<Siswa>> = siswaDao.searchByEmail(query)

    fun searchByNomorHp(query: String): Flow<List<Siswa>> = siswaDao.searchByNomorHp(query)

    fun getSiswaCount(): Flow<Int> = siswaDao.getSiswaCount()

    suspend fun insertSiswa(siswa: Siswa) = siswaDao.insertSiswa(siswa)

    suspend fun updateSiswa(siswa: Siswa) = siswaDao.updateSiswa(siswa)

    suspend fun deleteSiswa(siswa: Siswa) = siswaDao.deleteSiswa(siswa)
}
