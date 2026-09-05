package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.AppSettingsDao
import com.example.data.dao.CallLogDao
import com.example.data.dao.ChatMessageDao
import com.example.data.dao.OrderDao
import com.example.data.dao.ServiceDao
import com.example.data.dao.UserDao
import com.example.data.model.AppSettingsEntity
import com.example.data.model.CallLogEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ServiceEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ServiceEntity::class,
        OrderEntity::class,
        AppSettingsEntity::class,
        ChatMessageEntity::class,
        CallLogEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class KhadamatiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun serviceDao(): ServiceDao
    abstract fun orderDao(): OrderDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun callLogDao(): CallLogDao

    companion object {
        @Volatile
        private var INSTANCE: KhadamatiDatabase? = null

        fun getInstance(context: Context): KhadamatiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KhadamatiDatabase::class.java,
                    "khadamati_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
