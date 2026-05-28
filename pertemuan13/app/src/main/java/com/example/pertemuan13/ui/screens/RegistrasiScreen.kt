package com.example.pertemuan13.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan13.data.Siswa
import com.example.pertemuan13.viewmodel.FilterType
import com.example.pertemuan13.viewmodel.SiswaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrasiScreen(viewModel: SiswaViewModel) {
    val siswaList by viewModel.siswaList.collectAsState()
    val siswaCount by viewModel.siswaCount.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }

    // Show snackbar
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }

    // Delete confirmation dialog
    showDeleteDialog?.let { siswa ->
        AlertDialog(
            onDismissRequest = { viewModel.cancelDelete() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Konfirmasi Hapus",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus data siswa \"${siswa.nama}\"? Tindakan ini tidak dapat dibatalkan.",
                    textAlign = TextAlign.Center,
                    color = Color(0xFF546E7A)
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDelete() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.cancelDelete() },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Batal")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF1565C0),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        containerColor = Color(0xFFF0F4F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ─── Header ───────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 28.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Registrasi Siswa",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$siswaCount siswa terdaftar",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // ─── Search Bar ───────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Cari siswa...", color = Color(0xFF9E9E9E)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF1976D2)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearSearch() }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Hapus pencarian",
                                        tint = Color(0xFF757575)
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color(0xFFB0BEC5),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filter chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterType.values().forEach { filter ->
                            val isSelected = selectedFilter == filter
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.onFilterChange(filter) },
                                label = {
                                    Text(
                                        text = filter.label,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF1976D2),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color(0xFF546E7A)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = Color(0xFFB0BEC5),
                                    selectedBorderColor = Color(0xFF1976D2)
                                )
                            )
                        }
                    }
                }
            }

            // ─── Form Input ───────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (formState.isEditMode) Icons.Default.Edit else Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (formState.isEditMode) "Edit Data Siswa" else "Input Data Siswa",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFF1A237E)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nama field
                        OutlinedTextField(
                            value = formState.nama,
                            onValueChange = { viewModel.onNamaChange(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Nama") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (formState.namaError != null) Color(0xFFE53935) else Color(0xFF1976D2)
                                )
                            },
                            isError = formState.namaError != null,
                            supportingText = {
                                formState.namaError?.let {
                                    Text(it, color = Color(0xFFE53935), fontSize = 12.sp)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { emailFocusRequester.requestFocus() }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1976D2),
                                unfocusedBorderColor = Color(0xFFB0BEC5),
                                errorBorderColor = Color(0xFFE53935)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Email field
                        OutlinedTextField(
                            value = formState.email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(emailFocusRequester),
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = if (formState.emailError != null) Color(0xFFE53935) else Color(0xFF1976D2)
                                )
                            },
                            isError = formState.emailError != null,
                            supportingText = {
                                formState.emailError?.let {
                                    Text(it, color = Color(0xFFE53935), fontSize = 12.sp)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { phoneFocusRequester.requestFocus() }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1976D2),
                                unfocusedBorderColor = Color(0xFFB0BEC5),
                                errorBorderColor = Color(0xFFE53935)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nomor HP field
                        OutlinedTextField(
                            value = formState.nomorHp,
                            onValueChange = { viewModel.onNomorHpChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(phoneFocusRequester),
                            label = { Text("Nomor HP") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = if (formState.nomorHpError != null) Color(0xFFE53935) else Color(0xFF1976D2)
                                )
                            },
                            isError = formState.nomorHpError != null,
                            supportingText = {
                                formState.nomorHpError?.let {
                                    Text(it, color = Color(0xFFE53935), fontSize = 12.sp)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.submitSiswa()
                                }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1976D2),
                                unfocusedBorderColor = Color(0xFFB0BEC5),
                                errorBorderColor = Color(0xFFE53935)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit button
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.submitSiswa()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (formState.isEditMode) Color(0xFF388E3C) else Color(0xFF1976D2)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                        ) {
                            Icon(
                                imageVector = if (formState.isEditMode) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (formState.isEditMode) "Update Siswa" else "Tambah Siswa",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        // Cancel edit button
                        AnimatedVisibility(visible = formState.isEditMode) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        focusManager.clearFocus()
                                        viewModel.cancelEdit()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF757575)
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp, Color(0xFFB0BEC5)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Batal Edit",
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ─── Daftar Siswa Header ──────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Daftar Siswa",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A237E)
                    )
                    if (siswaList.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF1976D2), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${siswaList.size}",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // ─── Empty state ──────────────────────────────────────────
            if (siswaList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (searchQuery.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.PersonOff,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = Color(0xFFB0BEC5)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "Tidak ada hasil ditemukan" else "Belum ada siswa terdaftar",
                            color = Color(0xFF90A4AE),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        if (searchQuery.isEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tambahkan siswa pertama Anda di form di atas",
                                color = Color(0xFFB0BEC5),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }

            // ─── Siswa List ───────────────────────────────────────────
            items(
                items = siswaList,
                key = { it.id }
            ) { siswa ->
                SiswaCard(
                    siswa = siswa,
                    onEdit = { viewModel.setEditMode(siswa) },
                    onDelete = { viewModel.requestDelete(siswa) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SiswaCard(
    siswa: Siswa,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val avatarColors = listOf(
        Color(0xFF1976D2),
        Color(0xFF388E3C),
        Color(0xFF7B1FA2),
        Color(0xFFF57C00),
        Color(0xFFD32F2F),
        Color(0xFF0288D1),
        Color(0xFF00796B)
    )
    val avatarColor = avatarColors[siswa.id % avatarColors.size]
    val initial = siswa.nama.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    val dateStr = dateFormat.format(Date(siswa.tanggalDaftar))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(avatarColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    color = avatarColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info (Compact & Single Line Details)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = siswa.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A237E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF90A4AE)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = siswa.email,
                        fontSize = 11.sp,
                        color = Color(0xFF607D8B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 1.dp)
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF90A4AE)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = siswa.nomorHp,
                        fontSize = 11.sp,
                        color = Color(0xFF607D8B),
                        maxLines = 1
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = Color(0xFFB0BEC5)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateStr,
                        fontSize = 10.sp,
                        color = Color(0xFFB0BEC5),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Buttons (Properly separated side by side)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE3F2FD))
                        .clickable { onEdit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(10.dp)) // GUARANTEED SPACE
                
                // Delete Button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFEBEE))
                        .clickable { onDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
