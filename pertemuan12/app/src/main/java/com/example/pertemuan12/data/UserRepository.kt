package com.example.pertemuan12.data

class UserRepository(private val userDao: UserDao) {

    suspend fun register(email: String, password: String) {
        val user = User(email = email, password = password)
        userDao.registerUser(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
