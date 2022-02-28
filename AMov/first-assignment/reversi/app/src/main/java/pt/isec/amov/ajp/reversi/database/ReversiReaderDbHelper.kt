package pt.isec.amov.ajp.reversi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import pt.isec.amov.ajp.reversi.R

class ReversiReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ReversiReader.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ReversiReaderContract.ReversiLocalUser.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${ReversiReaderContract.ReversiLocalUser.COLUMN_NAME_NAME} TEXT NOT NULL DEFAULT" + R.string.default_local_user_name + "," +
                    "${ReversiReaderContract.ReversiLocalUser.COLUMN_NAME_PICTURE} BLOB)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ReversiReaderContract.ReversiLocalUser.TABLE_NAME}"
    }

    object ReversiReaderContract {
        object ReversiLocalUser : BaseColumns {
            const val TABLE_NAME = "local_user"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_PICTURE = "picture"
        }
    }
}