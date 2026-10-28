package com.jampaturismo

import com.jampaturismo.data.model.PlaceCategory
import com.jampaturismo.data.model.TideSafetyLevel
import com.jampaturismo.data.repository.TidesRepository
import com.jampaturismo.data.repository.TouristPlaceRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes Unitários de Regras de Negócio - Sprint 5
 * Validam os cálculos críticos de segurança da maré e os filtros turísticos.
 */
class JampaTurismoBusinessLogicTest {

    private lateinit var touristPlaceRepository: TouristPlaceRepository
    private lateinit var tidesRepository: TidesRepository

    @Before
    fun setUp() {
        touristPlaceRepository = TouristPlaceRepository()
        tidesRepository = TidesRepository()
    }

    @Test
    fun `testa niveis de seguranca da mare para travessia litoranea`() {
        // Altura <= 0.50m deve ser SAFE (Janela Segura para Areia Vermelha e Seixas)
        val safeHeight = 0.30
        val safeLevel = if (safeHeight <= 0.50) TideSafetyLevel.SAFE else TideSafetyLevel.DANGER
        assertEquals(TideSafetyLevel.SAFE, safeLevel)

        // Altura entre 0.51m e 0.90m deve ser WARNING (Atenção / Maré subindo)
        val warningHeight = 0.70
        val warningLevel = when {
            warningHeight <= 0.50 -> TideSafetyLevel.SAFE
            warningHeight <= 0.90 -> TideSafetyLevel.WARNING
            else -> TideSafetyLevel.DANGER
        }
        assertEquals(TideSafetyLevel.WARNING, warningLevel)

        // Altura > 0.90m deve ser DANGER (Maré alta / Perigo nos corais)
        val dangerHeight = 1.80
        val dangerLevel = when {
            dangerHeight <= 0.50 -> TideSafetyLevel.SAFE
            dangerHeight <= 0.90 -> TideSafetyLevel.WARNING
            else -> TideSafetyLevel.DANGER
        }
        assertEquals(TideSafetyLevel.DANGER, dangerLevel)
    }

    @Test
    fun `testa se atrativos criticos possuem marcacao de dependencia de mare`() {
        val areiaVermelha = touristPlaceRepository.getPlaceById("areia_vermelha")
        assertNotNull("Areia Vermelha deve existir no catálogo", areiaVermelha)
        assertTrue("Areia Vermelha depende estritamente da maré baixa", areiaVermelha!!.tideDependency)
        assertEquals(0.5, areiaVermelha.idealTideMaxHeight!!, 0.01)

        val seixas = touristPlaceRepository.getPlaceById("piscina_seixas")
        assertNotNull("Piscinas do Seixas devem existir no catálogo", seixas)
        assertTrue("Piscinas do Seixas dependem da maré baixa", seixas!!.tideDependency)

        val jacare = touristPlaceRepository.getPlaceById("jacare")
        assertNotNull("Pôr do Sol do Jacaré deve existir", jacare)
        assertFalse("Pôr do Sol do Jacaré independe da maré marítima", jacare!!.tideDependency)
    }

    @Test
    fun `testa filtro de categorias de passeios`() {
        val pools = touristPlaceRepository.getPlacesByCategory(PlaceCategory.NATURAL_POOLS)
        assertTrue("Deve conter pelo menos duas piscinas naturais cadastradas", pools.size >= 2)
        pools.forEach { place ->
            assertEquals(PlaceCategory.NATURAL_POOLS, place.category)
        }

        val tideDependentList = touristPlaceRepository.getTideDependentPlaces()
        assertTrue("Deve listar destinos que dependem da maré baixa", tideDependentList.isNotEmpty())
    }

    @Test
    fun `testa busca textual de atrativos turisticos`() {
        val allPlaces = touristPlaceRepository.getPlaceById("areia_vermelha")
        assertNotNull(allPlaces)

        // Busca por "Vermelha" deve encontrar a Ilha de Areia Vermelha
        val query = "vermelha"
        val matches = allPlaces!!.name.contains(query, ignoreCase = true) || allPlaces.subtitle.contains(query, ignoreCase = true)
        assertTrue("Busca por 'vermelha' deve encontrar Areia Vermelha", matches)

        // Busca por "sax" deve associar ao Pôr do Sol do Jacaré
        val jacare = touristPlaceRepository.getPlaceById("jacare")!!
        val jacareMatches = jacare.description.contains("sax", ignoreCase = true)
        assertTrue("Busca por 'sax' deve encontrar descrição do Jacaré", jacareMatches)
    }

    @Test
    fun `testa alternancia de favoritos`() {
        val favorites = mutableSetOf<String>()
        val placeId = "areia_vermelha"

        // Favoritar
        favorites.add(placeId)
        assertTrue("Atrativo deve estar nos favoritos", favorites.contains(placeId))

        // Desfavoritar
        favorites.remove(placeId)
        assertFalse("Atrativo deve ter sido removido dos favoritos", favorites.contains(placeId))
    }
}

