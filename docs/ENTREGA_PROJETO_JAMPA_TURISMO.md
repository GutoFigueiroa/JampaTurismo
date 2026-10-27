# PROPOSTA DE PROJETO DE APLICAÇÃO MOBILE

**INSTITUIÇÃO:** Centro Universitário de João Pessoa – UNIPE  
**CURSO:** Análise e Desenvolvimento de Sistemas (ADS) – 4º Período  
**DISCIPLINA:** Aplicação para Dispositivos Móveis  
**TURNO:** Tarde  
**PROFESSOR:** Leandro Melo  

---

## 1. IDENTIFICAÇÃO DO GRUPO E PROJETO

* **Nome do Projeto:** **JAMPA TURISMO** (Monitoramento Inteligente de Marés, Clima e Roteiros Litorâneos)
* **Líder do Grupo:** Fernando Augusto Lustoza Figueirôa

### Integrantes:
1. **Fernando Augusto Lustoza Figueirôa** (Líder)
2. **Matheus Henrique dos Santos Silva**
3. **Matheus Henrique do Egíto**
4. **Esdras de Almeida Oliveira**
5. **Murillo Lopes**

---

## 2. OBJETIVO DO APLICATIVO

### 2.1 Problema que Resolve
João Pessoa e o litoral paraibano possuem alguns dos maiores atrativos naturais do Brasil, tais como o banco de areia de **Areia Vermelha** (Cabedelo), as **Piscinas Naturais do Seixas** (extremo oriental das Américas) e os recifes de **Picãozinho** (Tambaú). 

No entanto, todos esses passeios dependem criticamente de dois fatores dinâmicos:
1. **A tábua de maré:** os bancos de areia e corais só emergem e ficam acessíveis durante a **maré baixa (idealmente $\le$ 0.5 metros)**. Turistas e moradores frequentemente perdem o passeio ou enfrentam situações de risco ao serem surpreendidos pela subida rápida da maré.
2. **As condições meteorológicas e de vento:** chuvas e ventos fortes alteram a visibilidade da água e a segurança da navegação das embarcações (catamarãs e lanchas).

Atualmente, o usuário precisa consultar tabelas estáticas da Marinha (focadas em navegação portuária e difíceis de interpretar para leigos) e cruzar manualmente com aplicativos convencionais de clima, sem uma recomendação integrada.

### 2.2 Solução Proposta
O **JAMPA TURISMO** atua como um **painel inteligente de bordo para o turista e morador**, cruzando em tempo real os dados da tábua de maré do Porto de Cabedelo com as previsões meteorológicas locais para entregar:
* Um indicador visual da **"Janela Segura"** (tempo restante de maré baixa liberada para travessia).
* Recomendações automáticas dos melhores passeios para o momento exato do dia.
* Catálogo de roteiros categorizados.
* Mapa interativo com suporte a GPS e rotas.

### 2.3 Público-Alvo
* Turistas nacionais e internacionais que visitam a Grande João Pessoa e o litoral norte/sul da Paraíba.
* Moradores locais e praticantes de esportes náuticos (SUP, caiaque, mergulho livre).
* Guias de turismo e operadores náuticos que necessitam de consulta ágil das condições da maré.

---

## 3. FUNCIONALIDADES DO APLICATIVO (Mínimo de 5)

1. **Dashboard Inteligente de Roteiro Pré-Programado (Tela Inicial):**
   * Cruzamento algorítmico do nível da maré atual e previsão do tempo para gerar uma recomendação personalizada (ex: *"Hoje é dia perfeito para Areia Vermelha!"*).
   * Contador regressivo de tempo de janela segura de maré baixa.

2. **Tábua da Maré Técnica e Didática:**
   * Visualização dos picos diários de preamar (maré alta) e baixa-mar (maré baixa) com base na estação oceanográfica do Porto de Cabedelo (PB).
   * Indicador com alturas em metros e horários exatos.

3. **Monitoramento Meteorológico Náutico:**
   * Exibição de temperatura atual, sensação térmica, velocidade do vento (fator determinante para a calmaria do mar), incidência de raios UV e probabilidade de chuva.
   * Horário sincronizado do Pôr do Sol (referência para o espetáculo da Praia do Jacaré).

4. **Catálogo de Roteiros e Passeios (Explorar):**
   * Listagem de destinos categorizados (*Piscinas Naturais*, *Praias*, *Pôr do Sol*, *Histórico/Cultural*).
   * Identificação imediata de passeios que possuem dependência de maré e o nível máximo seguro exigido.
   * Dicas práticas de viagem (ex: calçados apropriados, pontos de embarque de catamarã).

5. **Mapa Interativo com GPS e Rotas:**
   * Renderização dos atrativos georreferenciados na orla paraibana via Google Maps SDK.
   * Seleção de pontos com cartões explicativos e acionamento direto de navegação/traçado de rotas.

---

## 4. ESTRUTURA DE TELAS E FLUXO DE NAVEGAÇÃO

O aplicativo é estruturado em uma **Bottom Navigation Bar** moderna e persistente com 4 abas principais:

```
[ Início ]   ───────► Dashboard de recomendação + Resumo de Maré/Tempo + Roteiro do dia
[ Marés ]    ───────► Nível da água em metros + Tábua de picos do dia + Regras de segurança
[ Explorar ] ───────► Catálogo de atrativos + Filtros por categoria + Dicas de travessia
[ Mapa ]     ───────► Mapa Google Maps em tela cheia + Marcadores interativos + Traçar Rota
```

---

## 5. PROPOSTA DE LAYOUT (UI/UX)

### 5.1 Conceito e Estilo Visual
* **Estilo Clean & Modern:** Utilização de componentes baseados no **Material Design 3**, com cards arredondados (`RoundedCornerShape(16.dp a 20.dp)`), sombras suaves (`elevation`) e gradientes oceânicos sutis.
* **Hierarquia Visual:** Elementos de alto impacto imediato (Hero Cards) e micro-indicadores para facilitar a leitura rápida sob sol forte na praia.

### 5.2 Paleta de Cores

| Nome da Cor | Código Hexadecimal | Aplicação no App |
| :--- | :--- | :--- |
| **Azul Marinho Profundo** | `#0A192F` | Fundo do modo escuro, cards de destaque e cabeçalhos |
| **Azul Oceano** | `#1B3B6F` | Cor primária, botões de ação e títulos |
| **Ciano / Água** | `#00B4D8` | Cor secundária, detalhes, ícones e destaque |
| **Areia / Off-White** | `#FAF7F2` | Fundo principal do modo claro, simulando o litoral |

### 5.3 Cores Semânticas de Alerta (Segurança da Maré)

| Nível de Alerta | Cor | Hex | Critério / Significado |
| :--- | :--- | :--- | :--- |
| **Janela Segura** | Verde Turquesa | `#06D6A0` | Maré $\le$ 0.5m: Travessia para bancos de areia e piscinas liberada. |
| **Atenção** | Amarelo Âmbar | `#FFB703` | Maré entre 0.6m e 0.9m: Maré virando, momento de preparar retorno. |
| **Perigo / Retorne** | Coral / Vermelho | `#E63946` | Maré > 0.9m: Água subindo rapidamente, risco de correnteza ou cobertura do banco. |

---

## 6. TECNOLOGIAS UTILIZADAS E JUSTIFICATIVA

* **Linguagem: Kotlin 2.0+**
  * *Justificativa:* Linguagem oficial recomendada pelo Google para desenvolvimento Android, garantindo tipagem estática segura, interoperabilidade e código limpo.
* **Interface do Usuário: Jetpack Compose + Material 3**
  * *Justificativa:* Framework declarativo moderno que reduz em mais de 50% o código em relação a XML, facilitando a reatividade instantânea das marés e climas.
* **Mapas e Geolocalização: Google Maps Compose SDK & Play Services Location**
  * *Justificativa:* Precisão máxima em mapas nativos com pins customizados e GPS integrado.
* **Consumo de APIs: Retrofit 2 + Gson**
  * *Justificativa:* Padrão da indústria Android para consumo de serviços RESTful assíncronos.
* **Fonte de Dados Meteorológicos e Marítimos:**
  * *Open-Meteo Weather API* (Clima sem necessidade de API key paga) e *Open-Meteo Marine API* (Marés e ondulação).
* **Arquitetura: MVVM (Model-View-ViewModel)**
  * *Justificativa:* Separação completa de responsabilidades, facilitando os testes e permitindo que os 5 desenvolvedores atuem em módulos independentes sem gerar conflitos de código.

---

## 7. DIVISÃO DETALHADA DE TAREFAS POR INTEGRANTE

| Integrante | Papel no Projeto | Módulo / Responsabilidade | Entregáveis Técnicos |
| :--- | :--- | :--- | :--- |
| **Fernando Augusto Lustoza Figueirôa** | Líder do Grupo | **Aba Início (Dashboard Inteligente)** | • Configuração da base do projeto, Git e navegação geral.<br>• Algoritmo de cruzamento maré x tempo.<br>• Hero Banner e carrossel de passeios recomendados. |
| **Matheus Henrique do Egíto** | Desenvolvedor | **Aba Marés (Tábua da Maré)** | • Módulo de monitoramento marítimo.<br>• Listagem de preamar e baixa-mar do dia.<br>• Sistema visual da "Janela Segura" com regras de segurança. |
| **Esdras de Almeida Oliveira** | Desenvolvedor | **Aba Clima / Tempo** | • Integração com API meteorológica.<br>• Indicadores de vento náutico, índice UV e chuva.<br>• Sincronização do horário do pôr do sol. |
| **Murillo Lopes** | Desenvolvedor | **Aba Explorar (Roteiro de Viagens)** | • Modelagem do catálogo de atrativos turísticos de JP.<br>• Filtro dinâmico por categorias.<br>• Cards informativos de dependência de maré e dicas. |
| **Matheus Henrique dos Santos Silva** | Desenvolvedor | **Aba Mapa (Mapa Interativo)** | • Integração com Google Maps SDK e permissões de GPS.<br>• Plotagem dos atrativos turísticos com marcadores customizados.<br>• Card flutuante e acionamento de rotas. |

---

## 8. CRONOGRAMA E STATUS DE EXECUÇÃO

* [x] **Sprint 1 (Fundação e Setup):** Setup do projeto no Android Studio com Jetpack Compose, versionamento Git, Design System completo (`Theme.kt`, `Color.kt`, `Type.kt`), cores semânticas de alerta e Bottom Navigation Bar.
* [x] **Sprint 2 (Camada de Dados e APIs):** Consumo da API Open-Meteo via Retrofit 2, cálculo dos harmônicos de maré para o Porto de Cabedelo, repositório de atrativos turísticos com fotos e ViewModels com `StateFlow`.
* [x] **Sprint 3 (Módulos Individuais):** Construção e polimento individual das 5 telas para os 5 integrantes com suporte a Compose Previews.
* [x] **Sprint 4 (Dashboard Inteligente e Integração):** Banner inteligente com recomendação algorítmica na Tela Inicial, `PlaceDetailsBottomSheet` interativo e navegação cruzada com `Intent` para rotas no Google Maps.
* [x] **Sprint 5 (Testes e Documentação):** Suíte de testes unitários com JUnit (`JampaTurismoBusinessLogicTest.kt`), tratamento de fallback offline resiliente e roteiro de apresentação em sala (`ROTEIRO_APRESENTACAO_UNIPE.md`).

---

## 9. GUIA RÁPIDO PARA EXECUÇÃO

1. Clone ou abra a pasta do projeto no **Android Studio Hedgehog / Iguana / Koala ou superior**.
2. Aguarde a sincronização do Gradle (todas as dependências do `libs.versions.toml` serão baixadas automaticamente).
3. Selecione um dispositivo físico via USB ou um emulador Android (API 26 ou superior).
4. Clique em **Run 'app'** (`Shift + F10`).

