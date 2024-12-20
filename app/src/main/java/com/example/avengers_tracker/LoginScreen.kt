//package com.example.avengers_tracker
//
//import android.content.Intent
//import android.util.Log
//import androidx.activity.compose.ManagedActivityResultLauncher
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.ActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavController
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ElevatedButton
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.navigation.compose.rememberNavController
//import com.example.avengers_tracker.widgets.ExpenseTextView
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.AuthResult
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//@Composable
//fun LoginScreen(navController: NavController) {
//    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
//    val launcher = rememberFireBaseAuthLauncher(
//        onAuthComplete = { result ->
//            user = result.user
//            navController.navigate("/home") // Ensure this matches the navigation graph
//        },
//        onAuthError = { exception ->
//            user = null
//            Log.e("GoogleAuth", "Authentication failed: ${exception.message}")
//        }
//    )
//
//    val context = LocalContext.current
//    val clientId = stringResource(id = R.string.client_id) // Use Web Client ID from Firebase
//
//    Surface(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
//            val (nameRow, card) = createRefs()
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
//                    .constrainAs(nameRow) {
//                        top.linkTo(parent.top)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    },
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                if (user == null) {
//                    ElevatedButton(
//                        onClick = {
//                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                                .requestIdToken(clientId)
//                                .requestEmail()
//                                .build()
//                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
//                            launcher.launch(googleSignInClient.signInIntent)
//                        },
//                        shape = RoundedCornerShape(15.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                            .padding(4.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.White,
//                            contentColor = Color.Black
//                        )
//                    ) {
//                        ExpenseTextView(
//                            text = "Sign in with Google",
//                            fontSize = 16.sp,
//                            color = Color.Black
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_google),
//                            contentDescription = null,
//                            modifier = Modifier.size(30.dp)
//                        )
//                    }
//                } else {
//                    navController.navigate("/home")
////                    ExpenseTextView(
////                        text = "Welcome ${user?.displayName}",
////                        fontSize = 24.sp,
////                        color = Color.Black
////                    )
////                    Button(onClick = {
////                        Firebase.auth.signOut()
////                        user = null
////                    }) {
////                        ExpenseTextView(
////                            text = "Sign Out",
////                            fontSize = 16.sp,
////                            color = Color.Black
////                        )
////                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun rememberFireBaseAuthLauncher(
//    onAuthComplete: (AuthResult) -> Unit,
//    onAuthError: (Exception) -> Unit
//): ManagedActivityResultLauncher<Intent, ActivityResult> {
//    val scope = rememberCoroutineScope()
//
//    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//
//        try {
//            val account = task.getResult(ApiException::class.java)!!
//            val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
//
//            scope.launch {
//                try {
//                    val authResult = Firebase.auth.signInWithCredential(credentials).await()
//                    onAuthComplete(authResult)
//                } catch (e: Exception) {
//                    onAuthError(e)
//                }
//            }
//        } catch (e: ApiException) {
//            onAuthError(e)
//        }
//    }
//}
//
//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen(
//        navController = rememberNavController()
//    )
//}