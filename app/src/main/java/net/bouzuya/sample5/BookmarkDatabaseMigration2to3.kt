package net.bouzuya.sample5

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class BookmarkDatabaseMigration2to3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // do nothing
    }
}
