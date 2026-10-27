# ROTEIRO DE APRESENTAÇÃO DO PROJETO – JAMPA TURISMO

**Instituição:** UNIPE – Centro Universitário de João Pessoa  
**Disciplina:** Aplicação para Dispositivos Móveis  
**Professor:** Leandro Melo  
**Tema:** JAMPA TURISMO (Kotlin + Jetpack Compose)  
**Tempo Estimado:** 8 a 10 minutos (1 a 2 minutos por integrante)

---

## ESTRUTURA E ORDEM DAS FALAS

### 🎤 1. Abertura & Visão Geral (1m30s)
**Responsável:** Fernando Augusto Lustoza Figueirôa (Líder do Grupo)
* **O que falar:**
  1. Cumprimentar o professor Leandro e a turma.
  2. Apresentar o problema: *"Em João Pessoa, os passeios mais cobiçados como Areia Vermelha, Picãozinho e Piscinas do Seixas dependem de maré baixa $\le 0.5$m. Turistas muitas vezes perdem passeios ou correm riscos ao serem pegos pela maré subindo."*
  3. Apresentar o **Jampa Turismo**: um painel inteligente que cruza tábua de maré e clima para dar recomendações automáticas de segurança e roteiros.
  4. Destacar a stack: **Kotlin 2.0 nativo com Jetpack Compose e Material 3**.
* **O que mostrar na tela:** Abrir a **Aba Início** mostrando o Hero Banner que avisa em tempo real: *"Condições Ideais para Areia Vermelha!"* e o contador da Janela Segura.

---

### 🎤 2. Monitoramento de Marés & Janela Segura (1m30s)
**Responsável:** Matheus Henrique do Egíto
* **O que falar:**
  1. Explicar a importância da estação oceanográfica de referência: o **Porto de Cabedelo (PB)**.
  2. Demonstrar o indicador de nível atual em metros e a lista de picos (preamar e baixa-mar).
  3. Apresentar as **Cores Semânticas de Alerta**:
     * 🟢 **Turquesa:** Janela Segura liberada ($\le 0.5$m).
     * 🟡 **Âmbar:** Maré virando (alerta para retorno das embarcações).
     * 🔴 **Coral:** Maré alta / perigo para travessia a pé ou barcos pequenos.
* **O que mostrar na tela:** Clicar na **Aba Marés**, rolando pelos horários de pico e pelas regras de segurança de embarque.

---

### 🎤 3. Meteorologia Náutica & Integração API (1m30s)
**Responsável:** Esdras de Almeida Oliveira
* **O que falar:**
  1. Explicar o consumo da **Open-Meteo Weather API** via **Retrofit 2 e Coroutines**.
  2. Destacar que a API fornece dados sem chaves pagas e com suporte a fuso horário local (`America/Fortaleza`).
  3. Apresentar as métricas cruciais para o turismo costeiro: **Vento** (essencial para calmaria da água), **Índice UV** (proteção solar) e horário do **Pôr do Sol** (sincronizado com a Praia do Jacaré).
  4. Mencionar a arquitetura com **fallback offline**: o app não trava mesmo se a conexão falhar na praia.
* **O que mostrar na tela:** Mostrar os cards de clima, vento e índice UV.

---

### 🎤 4. Catálogo de Roteiros & UI com Imagens (1m30s)
**Responsável:** Murillo Lopes
* **O que falar:**
  1. Apresentar a **Aba Explorar**: catálogo de atrativos categorizados (*Piscinas Naturais*, *Praias*, *Pôr do Sol*, *Histórico*).
  2. Explicar o uso da biblioteca **Coil** para carregamento assíncrono e cache de imagens.
  3. Mostrar os chips de dependência de maré (ex: *Maré $\le 0.5$m*).
  4. Tocar em um card para abrir o **Modal BottomSheet de Detalhes** com dicas práticas e sapatilhas.
* **O que mostrar na tela:** Filtrar por categorias (ex: "Piscinas Naturais"), clicar no card da "Ilha de Areia Vermelha" para exibir o BottomSheet com o botão "Traçar Rota".

---

### 🎤 5. Mapa Interativo com Google Maps SDK & GPS (1m30s)
**Responsável:** Matheus Henrique dos Santos Silva
* **O que falar:**
  1. Explicar a integração do **Google Maps Compose SDK** e **Google Play Services Location**.
  2. Demonstrar os marcadores interativos espalhados pelo litoral paraibano.
  3. Explicar o cálculo de distância em km: o app calcula em tempo real a distância entre a posição do usuário e o ponto turístico selecionado.
  4. Mostrar a integração de rota: ao clicar em "Traçar Rota", aciona um `Intent` nativo abrindo o aplicativo do Google Maps para navegação GPS curva a curva.
* **O que mostrar na tela:** Clicar na **Aba Mapa**, interagir com os marcadores e selecionar um ponto para exibir o card inferior.

---

### 🎤 6. Encerramento & Considerações Técnicas (1m)
**Responsável:** Fernando Augusto (Líder)
* **O que falar:**
  1. Resumir o aprendizado do grupo com a arquitetura **MVVM**, reatividade com **StateFlow**, desacoplamento por componentes e boas práticas de Git.
  2. Abrir para perguntas do professor Leandro Melo.

---

## 💡 DICAS PARA UMA NOTA MÁXIMA
1. **Tenha o app instalado em um celular físico** ou no emulador Android do Android Studio pronto antes da sua vez de apresentar.
2. Cada integrante deve saber explicar a sua parte no código (`HomeScreen.kt`, `TidesScreen.kt`, `WeatherScreen.kt`, `PlacesScreen.kt`, `MapScreen.kt`).
3. Destaque que o projeto foi planejado e executado em Sprints organizadas.
