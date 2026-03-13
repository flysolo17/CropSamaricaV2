package com.potatodevs.cropsamarica.models.pests

import com.potatodevs.cropsamarica.models.rice.RiceStage


fun LocalizeText.locale(
    languageCode : String
): String {
    return if (languageCode == "en") {
        this.en
    } else {
        this.tl
    }
}
data class LocalizeText(
    val en : String = "",
    val tl : String = ""
)
fun String.localize() : LocalizeText {
    return LocalizeText(en = this, tl = this)
}

fun List<String>.localize() : List<LocalizeText> {
    return this.map { LocalizeText(en = it, tl = it) }
}



data class Prevention(
    val title: String = "",
    val text: List<String> = emptyList()
)


data class PestInformation(
    val title: String = "",
    val description: String = "",
    val symptoms: List<String> = emptyList(),
    val prevention: List<Prevention> = emptyList()
)


data class LocalizePestInformation(
    val en: PestInformation = PestInformation(),
    val tl: PestInformation = PestInformation()
)



data class PestAndDisease(
    val id: String = "",
    val images: List<String> = emptyList(),
    val stages: List<RiceStage> = emptyList(),
    val information: LocalizePestInformation = LocalizePestInformation()
)



fun PestAndDiseaseOld.toNew() : PestAndDisease {
    return PestAndDisease(
        id = this.id,
        images = this.images,
        stages = this.stages,
        information = LocalizePestInformation(
            en = PestInformation(
                title = this.title.en,
                description = this.description.en,
                symptoms = this.symptoms.map { it.en },
                prevention = this.prevention.map {
                    Prevention(
                        title = it.title.en,
                        text = it.text.map { it.en }
                    )
                }
            ),
            tl = PestInformation(
                title = this.title.tl,
                description = this.description.tl,
                symptoms = this.symptoms.map { it.tl },
                prevention = this.prevention
                    .map {
                        Prevention(
                            title = it.title.tl,
                            text = it.text.map { it.tl }
                        )
                    }
            )
        )
    )
}

