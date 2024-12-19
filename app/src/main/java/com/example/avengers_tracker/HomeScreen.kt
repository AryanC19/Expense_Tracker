package com.example.avengers_tracker
import android.text.Layout
import android.widget.GridLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.avengers_tracker.ui.theme.Zinc

@Composable
fun HomeScreen(){
    Surface(modifier = Modifier.fillMaxSize()){
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val(nameRow,list,card,topBar) =createRefs()
            Image (painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(topBar){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }){
                Column{
                   Text(text ="Avengers Tracker",fontSize=16.sp, color = Color.White)
                    Text(text ="Welcome to the Avengers Tracker",fontSize=20.sp, color = Color.White, fontWeight = FontWeight.Bold)

                }

                Image(painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            
            CardItem(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }

            )

        }

    }
}

@Composable
fun CardItem(
    modifier: Modifier,

) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = "Total Balance",
                    fontSize=20.sp,
                  //  style = Typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = " $ 5000",
                    fontSize=20.sp,
                 //   style = Typography.headlineLarge,
                    color = Color.White,
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

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = "$ 3654",
                image = R.drawable.ic_income
            )

            Spacer(modifier = Modifier.size(8.dp))

            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = "$ 2132",
                image = R.drawable.ic_expense
            )

        }

    }
}

@Composable
fun TransactionList(
    modifier: Modifier
) {
            Column(modifier=modifier.padding(horizontal = 16.dp)) {
                Box(modifier = modifier.fillMaxWidth()) {
                    Text(
                        text = "Recent Transactions",
                        fontSize=20.sp,
                    )
                    Text(
                        text = "See All",
                        fontSize =16.sp,

                        modifier=Modifier.align(Alignment.CenterEnd)
                    )
                }
                TransactionItem(
                    title = "Netflix",
                    amount = "- $ 200",
                    icon = R.drawable.ic_netflix,
                    date = "Today",
                    color = Color.Red,

                )
                TransactionItem(
                    title = "Youtube",
                    amount = "- $ 100",
                    icon = R.drawable.ic_youtube,
                    date = "Today",
                    color = Color.Red,

                    )
                TransactionItem(
                    title = "Netflix",
                    amount = "- $ 1200",
                    icon = R.drawable.ic_starbucks,
                    date = "Today",
                    color = Color.Red,

                    )
            }

}


@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,

) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(51.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(6.dp))
                Text(text = date, fontSize = 13.sp, color = Color.Black)
            }
        }
        Text(
            text = amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )
    }
}
@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, image: Int) {
    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = title,fontSize =16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = amount,fontSize =20.sp, color = Color.White)
    }
}
@Composable
@Preview
fun PreviewHomeScreen(){
    HomeScreen()
}