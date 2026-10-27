package com.jampaturismo.data.repository

import com.jampaturismo.data.model.PlaceCategory
import com.jampaturismo.data.model.TouristPlace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TouristPlaceRepository {

    private val placesList = listOf(
        TouristPlace(
            id = "areia_vermelha",
            name = "Ilha de Areia Vermelha",
            subtitle = "Cabedelo • Parque Estadual Marinho",
            category = PlaceCategory.NATURAL_POOLS,
            description = "Banco de areia avermelhada que só emerge na maré baixa. Cercado de corais e piscinas mornas de água cristalina.",
            tideDependency = true,
            idealTideMaxHeight = 0.5,
            latitude = -7.0270,
            longitude = -34.8239,
            travelTip = "Acesse apenas com maré ≤ 0.5m. Embarque pela praia do Poço ou Camboinha.",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80"
        ),
        TouristPlace(
            id = "piscina_seixas",
            name = "Piscinas Naturais do Seixas",
            subtitle = "Ponto mais oriental das Américas",
            category = PlaceCategory.NATURAL_POOLS,
            description = "Aquário natural com rica vida marinha localizado no extremo oriental do continente.",
            tideDependency = true,
            idealTideMaxHeight = 0.4,
            latitude = -7.1511,
            longitude = -34.7932,
            travelTip = "Catamarãs saem da praia da Penha e Seixas 1h30 antes do pico da maré baixa.",
            imageUrl = "https://images.unsplash.com/photo-1544551763-46a013bb70d5?auto=format&fit=crop&w=600&q=80"
        ),
        TouristPlace(
            id = "picaozinho",
            name = "Picãozinho",
            subtitle = "Tambaú • Formações de Corais",
            category = PlaceCategory.NATURAL_POOLS,
            description = "Passeio clássico em frente à praia de Tambaú, com corais preservados ideais para mergulho livre com snorkel.",
            tideDependency = true,
            idealTideMaxHeight = 0.5,
            latitude = -7.1130,
            longitude = -34.8080,
            travelTip = "Leve snorkel e sapatilha aquática para proteger os pés nos corais.",
            imageUrl = "https://images.unsplash.com/photo-1510414842594-a61752d38524?auto=format&fit=crop&w=600&q=80"
        ),
        TouristPlace(
            id = "jacare",
            name = "Pôr do Sol do Jacaré",
            subtitle = "Cabedelo • Rio Paraíba",
            category = PlaceCategory.SUNSET,
            description = "O famoso espetáculo do saxofonista Jurandy do Sax tocando o Bolero de Ravel ao entardecer.",
            tideDependency = false,
            idealTideMaxHeight = null,
            latitude = -7.0392,
            longitude = -34.8569,
            travelTip = "Chegue antes das 16h30 para pegar um bom lugar no píer ou nos catamarãs.",
            imageUrl = "https://images.unsplash.com/photo-1495616811223-4d98c6e9c869?auto=format&fit=crop&w=600&q=80"
        ),
        TouristPlace(
            id = "coqueirinho",
            name = "Praia de Coqueirinho",
            subtitle = "Conde • Litoral Sul",
            category = PlaceCategory.BEACH,
            description = "Praia cercada por coqueirais, cânions avermelhados e mar calmo com enseada.",
            tideDependency = false,
            idealTideMaxHeight = null,
            latitude = -7.3195,
            longitude = -34.7972,
            travelTip = "Passeios de buggy levam aos mirantes do Dedo de Deus e das Tartarugas.",
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80"
        ),
        TouristPlace(
            id = "centro_historico",
            name = "Centro Histórico & São Francisco",
            subtitle = "Varadouro • Conjunto Barroco",
            category = PlaceCategory.CULTURE,
            description = "Uma das cidades mais antigas do Brasil. Igreja de São Francisco com rica talha dourada e azulejaria portuguesa.",
            tideDependency = false,
            idealTideMaxHeight = null,
            latitude = -7.1158,
            longitude = -34.8856,
            travelTip = "Ideal para o período da manhã ou dias nublados. Visite também a Casa da Pólvora.",
            imageUrl = "https://images.unsplash.com/photo-1548625361-125032541300?auto=format&fit=crop&w=600&q=80"
        )
    )

    private val _places = MutableStateFlow(placesList)
    val places: Flow<List<TouristPlace>> = _places.asStateFlow()

    fun getPlaceById(id: String): TouristPlace? {
        return _places.value.find { it.id == id }
    }

    fun getPlacesByCategory(category: PlaceCategory): List<TouristPlace> {
        return _places.value.filter { it.category == category }
    }

    fun getTideDependentPlaces(): List<TouristPlace> {
        return _places.value.filter { it.tideDependency }
    }
}

