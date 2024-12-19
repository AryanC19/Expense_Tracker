package com.example.avengers_tracker

import android.text.Layout
import android.widget.GridLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.avengers_tracker.ui.theme.InterFont
import com.example.avengers_tracker.ui.theme.Zinc
import com.example.avengers_tracker.widgets.ExpenseTextView

@Composable
fun AddExpense() {
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                ExpenseTextView(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)

                )

                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )


            }

            DataForm(modifier = Modifier
                .padding(top = 60.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}

@Composable
fun DataForm(modifier: Modifier) {

    val name = remember {
        mutableStateOf("")
    }

    val amount = remember {
        mutableStateOf("")
    }

    val date = remember {
        mutableStateOf(0L)
    }

    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())


    ) {
        ExpenseTextView(
            text = "Title",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.size(16.dp))


        ExpenseTextView(
            text = "Amount",
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            value = amount.value,
            onValueChange = {
                amount.value = it
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.size(12.dp))

        //date calendar

        ExpenseTextView(
            text = "Date",
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            value = if (date.value == 0L) "" else Utils.formatDateToHumanReadableForm(date.value),
            onValueChange = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false

        )

        Spacer(modifier = Modifier.size(12.dp))

        //dropdown list
        ExpenseTextView(
            text = "Category",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))
        ExpenseDropDown(
            listOf("Netflix", "Paypal", "Salary", "Upwork"),
            onItemSelected = {
                category.value = it
            })

        Spacer(modifier = Modifier.size(12.dp))


        //type
        ExpenseTextView(
            text = "Type",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.size(8.dp))
        ExpenseDropDown(
            listOf("Expense", "Income"),
            onItemSelected = {
                category.value = it
            })

        Spacer(modifier = Modifier.size(12.dp))


        //button Add expense


        Button(
            onClick = {}, modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            ExpenseTextView(
                text = "Add Expense",
                color = Color.White,
                fontSize = 14.sp,

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
            textStyle = TextStyle(fontFamily = InterFont, color = Color.Black),
            readOnly = true,
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
    AddExpense()

}