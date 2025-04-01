package com.cy.practice.todo.di

import com.cy.practice.todo.common.DefaultDispatcherProvider
import com.cy.practice.todo.common.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {
    @Provides
    @Singleton
    fun providesDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}



//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class DefaultDispatcher
//
//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class IoDispatcher
//
//@Retention(AnnotationRetention.BINARY)
//@Qualifier
//annotation class MainDispatcher
//
//@InstallIn(SingletonComponent::class)
//@Module
//object DispatcherModule {
//    @DefaultDispatcher
//    @Provides
//    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
//
//    @IoDispatcher
//    @Provides
//    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
//
//    @MainDispatcher
//    @Provides
//    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
//}