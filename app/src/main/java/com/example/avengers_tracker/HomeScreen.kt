package com.example.avengers_tracker

import android.text.Layout
import android.view.Surface
import android.widget.GridLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.avengers_tracker.data.model.ExpenseEntity
import com.example.avengers_tracker.ui.theme.Zinc
import com.example.avengers_tracker.viewmodel.HomeViewModel
import com.example.avengers_tracker.viewmodel.HomeViewModelFactory
import com.example.avengers_tracker.widgets.ExpenseTextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson

@Composable
fun HomeScreen(navController: NavController) {
    val user = Firebase.auth.currentUser

    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)



    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, searchBar, add, backgroundImage) = createRefs()

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


            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }) {
                Column {
                    ExpenseTextView(
                        text = "Avengers Tracker",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    ExpenseTextView(
                        text = "Welcome, ${user?.displayName ?: "User"}",

                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }
                IconButton(
                    onClick = {
                        Firebase.auth.signOut() // Sign out the user
                        navController.navigate("/login") { // Navigate to the login screen
                            popUpTo(0) // Clear the backstack
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Sign Out",
                        modifier = Modifier.size(24.dp) // Adjust size as needed
                    )
                }

            }

            //to store the changing fields
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                },
                balance = balance, income = income, expense = expense
            )


            //Search Box

            SearchBar(
                onSearch = { query ->
                    viewModel.updateSearchQuery(query)
                },
                modifier = Modifier.constrainAs(searchBar) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(searchBar.bottom)  // Changed from card.bottom to searchBar.bottom
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                list = if (viewModel.filteredExpenses.collectAsState().value.isEmpty()) {
                    state.value
                } else {
                    viewModel.filteredExpenses.collectAsState().value
                },
                viewModel = viewModel,
                navController = navController
            )

            Image(
                painter = painterResource(id = R.drawable.ic_add),

                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        navController.navigate("/add")
                    }
                    .constrainAs(add) {
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                    .background(Color.Green, shape = CircleShape)
                    .size(48.dp)
                    .clip(CircleShape)

            )


        }

    }
}

@Composable
fun CardItem(
    modifier: Modifier,
    balance: String,
    income: String,
    expense: String,

    ) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF7C7C80).copy(alpha = 0.3f))
            .padding(16.dp)


    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                ExpenseTextView(
                    text = "Total Balance",
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(8.dp))
                ExpenseTextView(
                    text = balance,
                    fontSize = 20.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),

            ) {

            Image(
                painter = painterResource(id = R.drawable.ic_shield),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize() // Ensures the image fills the box
                    .align(Alignment.Center)
            )

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income,
                color = Color.Green
            )

            Spacer(modifier = Modifier.size(8.dp))

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.ic_expense,
                color = Color.Red
            )

        }

    }
}

@Composable
fun TransactionList(
    modifier: Modifier = Modifier,
    list: List<ExpenseEntity>,
    navController: NavController,

    viewModel: HomeViewModel
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Box(modifier = modifier.fillMaxWidth()) {
                ExpenseTextView(
                    text = "Recent Transactions",
                    fontSize = 20.sp,
                )
                ExpenseTextView(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        items(list) { item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getItemIcon(item),
                date = item.date.toString(),
                color = if (item.type == "Income") Color.Green else Color.Red,
                onDelete = {
                    viewModel.deleteExpense(item)
                },
                onEdit = {
                    val expenseEntityJson = Gson().toJson(item) // Using Gson for JSON serialization
                    navController.navigate("/add?expenseEntity=$expenseEntityJson")
                }


            )
        }
    }
}


@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 100f // Threshold for swipe action
    val animatedOffsetX = animateFloatAsState(targetValue = offsetX)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp)) // Add rounded corners
            .background(Color.Gray.copy(alpha = 0.3f)) // Transparent grey background
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX = (offsetX + dragAmount).coerceIn(-300f, 0f) // Restrict movement
                }
            }
    ) {
        // Background with Delete and Edit icons
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray) // Background color for the action row
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Yellow
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }

        // Foreground with transaction details
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.value.toInt(), 0) } // Apply animation
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .shadow(8.dp, shape = RoundedCornerShape(50.dp)) // Increased shadow size
                .clip(RoundedCornerShape(50.dp)) // Larger rounded corners
        )
        {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(51.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(6.dp))
                ExpenseTextView(text = date, fontSize = 13.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            ExpenseTextView(
                text = amount,
                fontSize = 20.sp,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, image: Int, color: Color) {
    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(
            text = amount,
            fontSize = 20.sp,
            color = color,

            )
    }
}


@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(50.dp))
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()

                .padding(horizontal = 16.dp, vertical = 8.dp)

                .background(Color.Gray.copy(alpha = 0.1f)),
            shape = RoundedCornerShape(50.dp),

            placeholder = { ExpenseTextView("Search transactions...", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.White,
                disabledBorderColor = Color.White,
                disabledPlaceholderColor = Color.White,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchText)
                    isSearchActive = true
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            singleLine = true
        )
    }
}

@Composable
@Preview
fun PreviewHomeScreen() {
    HomeScreen(rememberNavController())
}