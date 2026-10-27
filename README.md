# 🌊 JAMPA TURISMO - Aplicativo Android (Kotlin)

Aplicativo mobile desenvolvido para a disciplina de **Aplicação para Dispositivos Móveis** do curso de ADS (4º período) do **UNIPE**, sob orientação do **Prof. Leandro Melo**.

---

## 👥 Integrantes e Responsabilidades

* **Fernando Augusto Lustoza Figueirôa (Líder)** - Aba Início (Dashboard Inteligente & Arquitetura Base)
* **Matheus Henrique do Egíto** - Aba Marés (Tábua da Maré & Indicador de Janela Segura)
* **Esdras de Almeida Oliveira** - Aba Clima (Meteorologia Náutica, UV & Vento)
* **Murillo Lopes** - Aba Explorar (Catálogo de Roteiros, Praias & Passeios)
* **Matheus Henrique dos Santos Silva** - Aba Mapa (Google Maps SDK, GPS & Marcadores)

---

## 🛠️ Tecnologias e Arquitetura

* **Linguagem:** Kotlin 2.0+
* **Interface (UI):** Jetpack Compose + Material Design 3
* **Arquitetura:** MVVM (Model-View-ViewModel)
* **Consumo de APIs:** Retrofit 2 + Gson
* **Mapas & GPS:** Google Maps Compose SDK & Google Play Services Location
* **Paleta:** Azul Marinho Profundo (`#0A192F`), Ciano/Água (`#00B4D8`), Areia (`#FAF7F2`)
* **Cores Semânticas:** Janela Segura (`#06D6A0`), Atenção (`#FFB703`), Perigo (`#E63946`)

---

## 📂 Estrutura de Pastas

```
app/src/main/java/com/jampaturismo/
├── core/
│   ├── navigation/        # Rotas da BottomNavigationBar e telas
│   ├── theme/             # Cores personalizadas, Tipografia e Tema Material 3
│   └── ui/components/     # Componentes reutilizáveis (SafeWindowBadge, etc.)
├── data/
│   ├── model/             # Modelos de Dados (Maré, Clima, Atrativos)
│   └── repository/        # Repositório de Atrativos de João Pessoa
├── features/
│   ├── home/              # Fernando - Tela Inicial / Dashboard
│   ├── tides/             # Matheus Egíto - Tábua da Maré
│   ├── weather/           # Esdras - Clima e Tempo
│   ├── places/            # Murillo - Roteiros e Passeios
│   └── map/               # Matheus Silva - Mapa Interativo Google Maps
├── JampaTurismoApp.kt     # Scaffold com a BottomBar e NavHost
└── MainActivity.kt        # Entry point da aplicação
```

---

## 🚀 Como Executar no Android Studio

1. Abra o **Android Studio**.
2. Selecione **File > Open** e aponte para a pasta deste projeto (`Projeto`).
3. Aguarde o **Gradle Sync** concluir o download das dependências.
4. Para habilitar o Google Maps, adicione sua chave de API no arquivo `AndroidManifest.xml` (ou via `local.properties`).
5. Execute no emulador Android ou em seu dispositivo físico conectado via USB.

---

## 📄 Documentação da Entrega
O documento acadêmico oficial pronto para entrega ao professor está localizado em:
`docs/ENTREGA_PROJETO_JAMPA_TURISMO.md`
