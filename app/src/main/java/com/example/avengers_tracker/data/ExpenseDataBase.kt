package com.example.avengers_tracker.data

import android.content.Context
import androidx.constraintlayout.core.motion.utils.Utils
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.avengers_tracker.data.dao.ExpenseDao
import com.example.avengers_tracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [ExpenseEntity::class], version = 2)

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
            ).addMigrations(MIGRATION_1_2).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    InitBasicData(context)
                }

                fun InitBasicData(context: Context) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(
                            ExpenseEntity(
                                4,
                                "WebFluid",
                                1000.0,
                                com.example.avengers_tracker.Utils.formatDateToHumanReadableForm(
                                    System.currentTimeMillis()
                                ),
                                "GooglePay",
                                type = "Expense"
                            )
                        )
                    }
                }
            }).build()

        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create a new table with all the expected columns (including category)
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS expense_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                category TEXT NOT NULL DEFAULT '',
                type TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Copy data from old table to new, setting category to '' by default
        database.execSQL(
            """
            INSERT INTO expense_table_new (id, title, amount, date, type)
            SELECT id, title, amount, date, type FROM expense_table
            """.trimIndent()
        )

        // Drop the old table
        database.execSQL("DROP TABLE expense_table")

        // Rename the new table to the old name
        database.execSQL("ALTER TABLE expense_table_new RENAME TO expense_table")
    }
}
