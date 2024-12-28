package com.example.avengers_tracker

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.avengers_tracker.data.model.ExpenseEntity
import com.example.avengers_tracker.ui.theme.InterFont
import com.example.avengers_tracker.viewmodel.AddExpenseViewModel
import com.example.avengers_tracker.viewmodel.AddViewModelFactory
import com.example.avengers_tracker.widgets.ExpenseTextView
import kotlinx.coroutines.launch

@Composable
fun AddExpense(navController: NavController, expenseEntity: ExpenseEntity? = null) {

    val viewModel =
        AddViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)

    val coroutineScope = rememberCoroutineScope()
    val title = remember { mutableStateOf(expenseEntity?.title ?: "") }
    val amount = remember { mutableStateOf(expenseEntity?.amount?.toString() ?: "") }
    val category = remember { mutableStateOf(expenseEntity?.category ?: "") }

    Surface(modifier = Modifier.fillMaxSize()) {
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
                    .fillMaxSize()
                    .alpha(0.2f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Navigate back"
                    )
                }
                ExpenseTextView(
                    text = if (expenseEntity != null) "Edit Expense" else "Add Expense",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.Center)
                )
                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            DataForm(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                initialTitle = title.value,
                initialAmount = amount.value,
                initialCategory = category.value,
                onAddExpenseClick = { newExpense ->
                    coroutineScope.launch {
                        if (expenseEntity == null) {
                            viewModel.addExpense(newExpense) // Add new expense
                        } else {
                            viewModel.updateExpense(newExpense.copy(id = expenseEntity.id)) // Update existing expense
                        }
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}


@Composable
fun DataForm(
    modifier: Modifier = Modifier,
    initialTitle: String = "",
    initialAmount: String = "",
    initialType: String = "Expense",
    initialCategory: String = "Others",
    onAddExpenseClick: (ExpenseEntity) -> Unit

) {
    val title = remember { mutableStateOf(initialTitle) }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("Expense") }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFECF5EF).copy(alpha = 0.1f)) // Grey with transparency
            .alpha(0.9f)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        ExpenseTextView(
            color = Color.White,
            text = "Title",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White)
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Amount
        ExpenseTextView(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = "Amount",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White)
        )

        // Date
        Spacer(modifier = Modifier.size(12.dp))
        ExpenseTextView(
            color = Color.White,
            text = "Date",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = if (date.value == 0L) "" else Utils.formatDateToHumanReadableForm(date.value),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            textStyle = TextStyle(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.White,
                disabledBorderColor = Color.Gray,
                disabledPlaceholderColor = Color.Gray,

                )
        )

        Spacer(modifier = Modifier.size(12.dp))

        // Category
        ExpenseTextView(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = "Category",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))
        ExpenseDropDown(
            listOf("GooglePay", "Paypal", "PhonePay", "Cash", "PayTM", "Visa"),
            onItemSelected = { category.value = it }
        )

        Spacer(modifier = Modifier.size(12.dp))

        // Type (Income, Expense)
        ExpenseTextView(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            text = "Type",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))
        ExpenseDropDown(
            listOf("Expense", "Income"),
            onItemSelected = { selectedType -> type.value = selectedType }
        )

        Spacer(modifier = Modifier.size(12.dp))

        // Button - Save
        Button(
            onClick = {
                // Validation
                val amountValue = amount.value.toDoubleOrNull()
                when {
                    title.value.isEmpty() -> {
                        errorMessage.value = "Title cannot be empty"
                        Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                    }

                    amountValue == null || amountValue <= 0 -> {
                        errorMessage.value = "Amount must be a positive number"
                        Toast.makeText(
                            context,
                            "Amount must be a positive number",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        val model = ExpenseEntity(
                            id = null,
                            title = title.value,
                            amount = amountValue,
                            date = Utils.formatDateToHumanReadableForm(date.value),
                            category = category.value,
                            type = type.value
                        )
                        Log.d("AddExpense", "Expense Type: ${type.value}")
                        onAddExpenseClick(model)
                    }
                }
            },
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5B625B) // Turquoise color
            )
        ) {
            ExpenseTextView(
                text = "Save",
                color = Color.White,
                fontSize = 18.sp,
            )
        }

        if (dateDialogVisibility.value) {
            ExpenseDatePickerDialog(
                onDateSelected = {
                    date.value = it
                    dateDialogVisibility.value = false
                },
                onDismiss = {
                    dateDialogVisibility.value = false
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            ExpenseTextView(text = "Confirm")
        }
    }, dismissButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            ExpenseTextView(text = "Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            textStyle = TextStyle(fontFamily = InterFont, color = Color.White),
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,


                ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },

            )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { }) {
            listOfItems.forEach {
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    selectedItem.value = it
                    onItemSelected(selectedItem.value)
                    expanded.value = false
                })
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddExpensePreview() {
    AddExpense(rememberNavController())

}