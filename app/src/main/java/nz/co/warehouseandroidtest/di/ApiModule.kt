package nz.co.warehouseandroidtest.di

import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.warehouseandroidtest.Constants
import nz.co.warehouseandroidtest.data.remote.SearchApi
import nz.co.warehouseandroidtest.data.remote.UserApi
import nz.co.warehouseandroidtest.data.repository.SearchRepository
import nz.co.warehouseandroidtest.data.repository.SearchRepositoryImp
import nz.co.warehouseandroidtest.data.repository.UserRepository
import nz.co.warehouseandroidtest.data.repository.UserRepositoryImp
import nz.co.warehouseandroidtest.domain.ListUseCase
import nz.co.warehouseandroidtest.domain.ListUseCaseImpl
import nz.co.warehouseandroidtest.domain.UserUseCase
import nz.co.warehouseandroidtest.domain.UserUseCaseImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: GsonConverterFactory, okhttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.HTTP_URL_ENDPOINT)
            .addConverterFactory(gson)
            .client(okhttp)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(): GsonConverterFactory {
        return GsonConverterFactory.create(Gson())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient().newBuilder().apply {
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Ocp-Apim-Subscription-Key", Constants.SUBSCRIPTION_KEY).build()
                chain.proceed(newRequest)
            }
        }
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        client.addInterceptor(logging)

        return client.build()
    }

    @Provides
    fun providesUserApi(retrofit: Retrofit) = retrofit.create(UserApi::class.java)


    @Provides
    fun provideSearchApi(retrofit: Retrofit) = retrofit.create(SearchApi::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        fun bindUserRepo(userRepository: UserRepositoryImp): UserRepository

        @Binds
        fun bindSearchRepo(searchRepository: SearchRepositoryImp): SearchRepository

        @Binds
        fun bindUserUseCase(userUseCase: UserUseCaseImpl): UserUseCase

        @Binds
        fun bindListUseCase(listUseCase: ListUseCaseImpl): ListUseCase
    }

}