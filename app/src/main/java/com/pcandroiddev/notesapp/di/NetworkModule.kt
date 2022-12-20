package com.pcandroiddev.notesapp.di

import com.pcandroiddev.notesapp.api.AuthInterceptor
import com.pcandroiddev.notesapp.api.NoteService
import com.pcandroiddev.notesapp.api.UserService
import com.pcandroiddev.notesapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserService {
        return retrofitBuilder
            .build()
            .create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): NoteService {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NoteService::class.java)
    }


}
