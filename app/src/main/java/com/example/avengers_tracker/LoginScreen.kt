package com.example.avengers_tracker

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.avengers_tracker.ui.theme.InterFont
import com.example.avengers_tracker.widgets.ExpenseTextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(navController: NavController) {
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val clientId = stringResource(id = R.string.client_id)
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            scope.launch {
                try {
                    Firebase.auth.signInWithCredential(credential).await()
                    user = Firebase.auth.currentUser
                    navController.navigate("/home") // Use the correct route
                    // Navigate after login success
                } catch (e: Exception) {
                    errorMessage = e.message
                } finally {
                    isLoading = false
                }
            }
        } catch (e: ApiException) {
            errorMessage = e.message
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),

            ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (nameRow, list, card, topBar, add, backgroundImage) = createRefs()
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.ic_avenger),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(backgroundImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxSize() // Ensure the image covers the full screen

                        .alpha(0.2f) // Optional: Reduce opacity to make it a background

                )





                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    ExpenseTextView(
                        fontFamily = InterFont,
                        text = "Welcome Agent,",
                        fontSize = 30.sp, // Large font size
                        color = Color.White,
                        modifier = Modifier
                            .padding(40.dp),
                        textAlign = TextAlign.Center


                    )





                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        // Welcome Text

                        if (user == null) {
                            ElevatedButton(
                                onClick = {
                                    isLoading = true
                                    errorMessage = null
                                    val gso =
                                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestIdToken(clientId)
                                            .requestEmail()
                                            .build()
                                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                    googleSignInClient.signOut() // Sign out to clear previous account
                                    googleSignInClient.revokeAccess() // Revoke access to force account choice
                                    launcher.launch(googleSignInClient.signInIntent)
                                },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color(0xFF2F3333)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(vertical = 16.dp)
                            ) {


                                Text(
                                    text = "Sign in with Google",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_google),
                                    contentDescription = "Google",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            LaunchedEffect(user) {
                                navController.navigate("/home") {
                                    popUpTo(0) // Clear the backstack
                                }
                            }
                        }

                        errorMessage?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}
