package com.example.pertemuan13.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan13.data.Siswa
import com.example.pertemuan13.data.SiswaDatabase
import com.example.pertemuan13.data.SiswaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FilterType(val label: String) {
    SEMUA("Semua"),
    NAMA("Nama"),
    EMAIL("Email"),
    NOMOR_HP("Nomor HP")
}

data class SiswaFormState(
    val nama: String = "",
    val email: String = "",
    val nomorHp: String = "",
    val namaError: String? = null,
    val emailError: String? = null,
    val nomorHpError: String? = null,
    val isEditMode: Boolean = false,
    val editId: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
class SiswaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SiswaRepository

    // Search & Filter state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(FilterType.SEMUA)
    val selectedFilter: StateFlow<FilterType> = _selectedFilter.asStateFlow()

    // Form state
    private val _formState = MutableStateFlow(SiswaFormState())
    val formState: StateFlow<SiswaFormState> = _formState.asStateFlow()

    // Snackbar message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Delete confirmation dialog
    private val _showDeleteDialog = MutableStateFlow<Siswa?>(null)
    val showDeleteDialog: StateFlow<Siswa?> = _showDeleteDialog.asStateFlow()

    // Siswa count
    val siswaCount: StateFlow<Int>

    // Filtered siswa list based on search query + filter
    val siswaList: StateFlow<List<Siswa>>

    init {
        val database = SiswaDatabase.getDatabase(application)
        repository = SiswaRepository(database.siswaDao())

        siswaCount = repository.getSiswaCount()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        siswaList = combine(_searchQuery, _selectedFilter) { query, filter ->
            Pair(query, filter)
        }.flatMapLatest { (query, filter) ->
            if (query.isBlank()) {
                repository.getAllSiswa()
            } else {
                when (filter) {
                    FilterType.SEMUA -> repository.searchSiswa(query)
                    FilterType.NAMA -> repository.searchByNama(query)
                    FilterType.EMAIL -> repository.searchByEmail(query)
                    FilterType.NOMOR_HP -> repository.searchByNomorHp(query)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // Form update functions
    fun onNamaChange(value: String) {
        _formState.update { it.copy(nama = value, namaError = null) }
    }

    fun onEmailChange(value: String) {
        _formState.update { it.copy(email = value, emailError = null) }
    }

    fun onNomorHpChange(value: String) {
        _formState.update { it.copy(nomorHp = value, nomorHpError = null) }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: FilterType) {
        _selectedFilter.value = filter
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    // Validate form
    private fun validateForm(): Boolean {
        val current = _formState.value
        var isValid = true
        var namaError: String? = null
        var emailError: String? = null
        var nomorHpError: String? = null

        if (current.nama.isBlank()) {
            namaError = "Nama tidak boleh kosong"
            isValid = false
        }

        if (current.email.isBlank()) {
            emailError = "Email tidak boleh kosong"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(current.email).matches()) {
            emailError = "Format email tidak valid"
            isValid = false
        }

        if (current.nomorHp.isBlank()) {
            nomorHpError = "Nomor HP tidak boleh kosong"
            isValid = false
        } else if (!current.nomorHp.all { it.isDigit() || it == '+' || it == '-' }) {
            nomorHpError = "Nomor HP hanya boleh berisi angka"
            isValid = false
        } else if (current.nomorHp.replace(Regex("[^0-9]"), "").length < 8) {
            nomorHpError = "Nomor HP minimal 8 digit"
            isValid = false
        }

        _formState.update {
            it.copy(
                namaError = namaError,
                emailError = emailError,
                nomorHpError = nomorHpError
            )
        }

        return isValid
    }

    // Add or Update siswa
    fun submitSiswa() {
        if (!validateForm()) return

        val current = _formState.value
        viewModelScope.launch {
            if (current.isEditMode) {
                val updated = Siswa(
                    id = current.editId,
                    nama = current.nama.trim(),
                    email = current.email.trim(),
                    nomorHp = current.nomorHp.trim()
                )
                repository.updateSiswa(updated)
                showSnackbar("Data siswa berhasil diperbarui")
            } else {
                val newSiswa = Siswa(
                    nama = current.nama.trim(),
                    email = current.email.trim(),
                    nomorHp = current.nomorHp.trim()
                )
                repository.insertSiswa(newSiswa)
                showSnackbar("Siswa berhasil ditambahkan")
            }
            resetForm()
        }
    }

    // Set edit mode
    fun setEditMode(siswa: Siswa) {
        _formState.value = SiswaFormState(
            nama = siswa.nama,
            email = siswa.email,
            nomorHp = siswa.nomorHp,
            isEditMode = true,
            editId = siswa.id
        )
    }

    // Cancel edit
    fun cancelEdit() {
        resetForm()
    }

    // Request delete confirmation
    fun requestDelete(siswa: Siswa) {
        _showDeleteDialog.value = siswa
    }

    // Confirm delete
    fun confirmDelete() {
        val siswa = _showDeleteDialog.value ?: return
        viewModelScope.launch {
            repository.deleteSiswa(siswa)
            showSnackbar("Data siswa berhasil dihapus")
        }
        _showDeleteDialog.value = null
    }

    // Cancel delete
    fun cancelDelete() {
        _showDeleteDialog.value = null
    }

    private fun resetForm() {
        _formState.value = SiswaFormState()
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
