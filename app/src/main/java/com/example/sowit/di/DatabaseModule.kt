package com.example.sowit.di

import android.content.Context
import androidx.room.Room
import com.example.sowit.data.datasource.AppDatabase
import com.example.sowit.data.datasource.dao.PlotDao
import com.example.sowit.data.datasource.dao.PointLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "plot_database"
        )
        .fallbackToDestructiveMigration(false)
        .build()
    }

    @Provides
    @Singleton
    fun providePlotDao(database: AppDatabase): PlotDao {
        return database.plotDao()
    }

    @Provides
    @Singleton
    fun providePointLocationDao(database: AppDatabase): PointLocationDao {
        return database.pointLocationDao()
    }
}