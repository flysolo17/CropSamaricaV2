package com.potatodevs.cropsamarica.di

import com.potatodevs.cropsamarica.repositories.auth.AuthRepository
import com.potatodevs.cropsamarica.repositories.auth.AuthRepositoryImpl
import com.potatodevs.cropsamarica.repositories.pests.PestRepository
import com.potatodevs.cropsamarica.repositories.pests.PestRepositoryImpl
import com.potatodevs.cropsamarica.repositories.riceTypes.RiceTypeRepository
import com.potatodevs.cropsamarica.repositories.riceTypes.RiceTypeRepositoryImpl
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepository
import com.potatodevs.cropsamarica.repositories.ricefield.RiceFieldRepositoryImpl
import com.potatodevs.cropsamarica.repositories.survey.SurveyRepository
import com.potatodevs.cropsamarica.repositories.survey.SurveyRepositoryImpl
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepository
import com.potatodevs.cropsamarica.repositories.tasks.TaskRepositoryImpl
import com.potatodevs.cropsamarica.repositories.user.UserRepository
import com.potatodevs.cropsamarica.repositories.user.UserRepositoryImpl
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepository
import com.potatodevs.cropsamarica.repositories.weather.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository


    @Binds
    abstract fun bindPestsRepository(
        impl: PestRepositoryImpl
    ): PestRepository

    @Binds
    abstract fun bindTasksRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository


    @Binds
    abstract fun bindRiceFieldRepository(
        impl: RiceFieldRepositoryImpl
    ): RiceFieldRepository


    @Binds
    abstract fun bindRiceTypeRepository(
        impl: RiceTypeRepositoryImpl
    ): RiceTypeRepository


    @Binds
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository


    @Binds
    abstract fun bindSurveyRepository(
        impl: SurveyRepositoryImpl
    ): SurveyRepository
}




