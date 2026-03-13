package com.potatodevs.cropsamarica.models

import kotlinx.serialization.Serializable
import java.util.Date



data class User(
    val id : String = "",
    val name : String = "",
    val location : String = "",
    val phone : String = "",
    var profile : String ? = null,
    val createdAt : Date = Date()
)

enum class Municipality(
    val displayName: String,
    val barangays: List<String>
) {
    SAN_JOSE(
        displayName = "San Jose",
        barangays = listOf(
            "Ansiray",
            "Bagong Sikat",
            "Balikyas",
            "Barahan",
            "Bato",
            "Bayotbot",
            "Bubog",
            "Buri",
            "Camburay",
            "Caminawit",
            "Central (Poblacion 1)",
            "Cruz Roja",
            "Dagupan",
            "Gantal",
            "Ilisong",
            "Iling Proper",
            "Labangan",
            "La Curva",
            "Magbay",
            "Magsikap",
            "Mangarin",
            "Mapaya",
            "Monteclaro",
            "Murtha",
            "Pag-asa",
            "Pawican",
            "Poblacion 2",
            "Poblacion 3",
            "Poblacion 4",
            "Poblacion 5",
            "Poblacion 6",
            "Poblacion 7",
            "Poblacion 8",
            "San Agustin",
            "San Roque",
            "Santa Cruz",
            "Santo Niño",
            "Tanyag",
            "Villaflor"
        )
    ),

    MAGSAYSAY(
        displayName = "Magsaysay",
        barangays = listOf(
            "Alibog",
            "Caguray",
            "Calawag",
            "Gapasan",
            "Laste",
            "Lourdes",
            "Nicolas",
            "Paclolo",
            "Poblacion",
            "Purnaga",
            "Santa Teresa",
            "Sibalat",
            "Rizal"
        )
    ),

    RIZAL(
        displayName = "Rizal",
        barangays = listOf(
            "Adela",
            "Aguas",
            "Magsikap",
            "Malawaan",
            "Manoot",
            "Pitogo",
            "Rizal",
            "Rumbang",
            "Salvacion",
            "San Pedro",
            "Santo Niño"
        )
    ),

    CALINTAAN(
        displayName = "Calintaan",
        barangays = listOf(
            "Concepcion",
            "Iriron",
            "Malpalon",
            "New Dagupan",
            "Poblacion",
            "Poypoy",
            "Tanyag"
        )
    )
}