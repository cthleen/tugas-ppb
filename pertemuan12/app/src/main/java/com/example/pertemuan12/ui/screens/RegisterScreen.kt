package com.example.pertemuan12.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan12.ui.AuthResult
import com.example.pertemuan12.ui.AuthViewModel

private val RegPrimaryBlue  = Color(0xFF2563EB)
private val RegBackgroundGray = Color(0xFFF1F5F9)
private val RegCardWhite    = Color(0xFFFFFFFF)
private val RegTextDark     = Color(0xFF1E293B)
private val RegTextGray     = Color(0xFF64748B)
private val RegBorderGray   = Color(0xFFCBD5E1)

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context  = LocalContext.current
    val email    by viewModel.registerEmail.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val result   by viewModel.registerResult.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
        when (result) {
            is AuthResult.Success -> {
                Toast.makeText(context, "Akun berhasil dibuat! Silakan login 🎉", Toast.LENGTH_SHORT).show()
                viewModel.resetRegisterResult()
                onRegisterSuccess()
            }
            is AuthResult.Error -> {
                Toast.makeText(context, (result as AuthResult.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetRegisterResult()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFDBEAFE), RegBackgroundGray)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RegCardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Create an account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RegTextDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Email Field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Email",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = RegTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = viewModel::onRegisterEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "test@gmail.com",
                                color = RegTextGray,
                                fontSize = 14.sp
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RegPrimaryBlue,
                            unfocusedBorderColor = RegBorderGray,
                            focusedContainerColor = RegCardWhite,
                            unfocusedContainerColor = RegCardWhite
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Password",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = RegTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = viewModel::onRegisterPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Enter your password",
                                color = RegTextGray,
                                fontSize = 14.sp
                            )
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible)
                                        "Hide password"
                                    else
                                        "Show password",
                                    tint = RegTextGray
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RegPrimaryBlue,
                            unfocusedBorderColor = RegBorderGray,
                            focusedContainerColor = RegCardWhite,
                            unfocusedContainerColor = RegCardWhite
                        )
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Create Account Button
                Button(
                    onClick = viewModel::onRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RegPrimaryBlue)
                ) {
                    Text(
                        text = "Create account",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Navigate to Login
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = RegTextGray, fontSize = 13.sp)) {
                            append("Already have an account? ")
                        }
                        withStyle(
                            SpanStyle(
                                color = RegPrimaryBlue,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        ) {
                            append("Log in")
                        }
                    },
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}
