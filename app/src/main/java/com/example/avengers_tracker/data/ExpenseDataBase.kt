package com.example.avengers_tracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.avengers_tracker.data.dao.ExpenseDao
import com.example.avengers_tracker.data.model.ExpenseEntity


@Database(entities = [ExpenseEntity::class], version = 1)

abstract class ExpenseDataBase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        private var INSTANCE: ExpenseDataBase? = null

        fun getDatabase(context: Context): ExpenseDataBase {
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            )
                .build()

        }
    }
}

//
//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            """
//            CREATE TABLE IF NOT EXISTS expense_table_new (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                title TEXT NOT NULL,
//                amount REAL NOT NULL,
//                date TEXT NOT NULL,
//                type TEXT NOT NULL
//            )
//            """.trimIndent()
//        )
//
//        // Step 2: Copy the data from the old table to the new table
//        database.execSQL(
//            """
//            INSERT INTO expense_table_new (id, title, amount, date, type)
//            SELECT id, title, amount, date, type FROM expense_table
//            """.trimIndent()
//        )
//
//        // Step 3: Drop the old table
//        database.execSQL("DROP TABLE expense_table")
//
//        // Step 4: Rename the new table to the original table name
//        database.execSQL("ALTER TABLE expense_table_new RENAME TO expense_table")
//    }
//}