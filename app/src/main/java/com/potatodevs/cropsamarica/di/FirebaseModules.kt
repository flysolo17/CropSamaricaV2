package com.potatodevs.cropsamarica.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.FunctionCallingConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.ToolConfig
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.potatodevs.cropsamarica.ai.CREATE_ANNOUNCEMENT
import com.potatodevs.cropsamarica.ai.CREATE_REMINDER
import com.potatodevs.cropsamarica.ai.CREATE_RICE_FIELD_DECLARATION

import com.potatodevs.cropsamarica.ai.SURVEY_GENERATION_DECLARATION
import com.potatodevs.cropsamarica.ai.SYSTEM_PROMPT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object FirebaseModules {

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance()


    @Provides
    @Singleton
    fun provideAyaAI() : GenerativeModel {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-2.5-flash",
                systemInstruction = content {
                    text(SYSTEM_PROMPT)
                },

                generationConfig = generationConfig {
                    temperature = 0.2f
                    topK = 40
                    topP = 0.9f
                },
                tools = listOf(
                    Tool.functionDeclarations(
                        functionDeclarations = listOf(
                            CREATE_RICE_FIELD_DECLARATION,
                            SURVEY_GENERATION_DECLARATION,
                            CREATE_ANNOUNCEMENT,
                            CREATE_REMINDER,
                        )
                    ),

                ),
                toolConfig = ToolConfig(
                    functionCallingConfig = FunctionCallingConfig.auto(),
                )
            )
        return model
    }
}