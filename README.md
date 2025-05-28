# App Base 02

https://github.com/segodimor2d2/app-hierarchylist

## Tela 1 Home: 
1. Botão 
    - Criar uma lista
        - Escolha uma pasta e de permissões para salvar a lista
    - Abrir uma lista
        - Escolha  uma pasta e de permissões para abrir a lista

2. 


1. Botão Procurar Selecionar a pasta de trabalho
    - Ver a possibilidade de Salvar a pasta depois da primeira vez
    - ter um botão para esquecer a pasta e poder usar outra
2. Imprime lista de arquivos (dentro da pasta)
    - Cada Arquivo poder ter um ícone sé o arquivo/lista foi processada identificada pelo prefix do nome
3. Se clicar num arquivo, => abre o arquivo na Tela 2
4. Botão para criar um arquivo
    - Pergunta para o nome do arquivo num TextField
    - Abre a Tela 2

## Tela 2 Edit:
1. Abre um TextField como o conteúdo do arquivo
2. Botão para salvar o arquivo
3. Botão para deletar o arquivo
4. Botão para fechar a tela => retorna para a Tela 1
5. Botão para iniciar => Tela 3, inicia processo de hierarquia


## Tela 3 HierchyProcess:
a HierchyScreen é uma tela que vai mostrar repetida uma comparação de 2 itens e vai me deixar selecionar qual é a mais importante com 3 botões,
cada comparação vai os botões e apos de apresentar uma comparação o programa vai salvar a opção selecionada e vai para a próxima comparação,
eu vou ter mais 2 botões de seguinte e anterior para navegar entre as comparações.

para comparar estou pensando em:
1. Mostra duas colunas
    ColumA | ColumB
     ItemA | ItemB

2. comparação de itens com 3 botões:
    `[A>B] [A=B] [A<B]`

    - -1(A mais importante que B),
    -  0(A e B são iguais),
    -  1(A é mais importante que B)

3. Botões de para navegar entre Items
    seguinte Item >>
    anterior Item <<

4. Botão Finalizar => 
    salva a lista ordenada com prefix identificando no nome do arquivo

5. Text Feedback da opção selecionada
    `[ - ] ó [A>B] ó [A=B] ó [A<B]`





## Tela 4 Ranking:
1. mostra uma lista com os itens ordenados
2. Possibilidade de mudar a ordem dos itens
3. Botão Salvar Nova ordem
4. Botão Cancelar Nova ordem
5. Botão Voltar para a Home


---

# Estrutura

Você está usando uma **estrutura modularizada por função (ou "por recurso")**, muito comum e **recomendada em projetos Android modernos com Jetpack Compose**.

### 📁 Estrutura atual e seus significados:

```
├── MainActivity.kt                    <-- Ponto de entrada principal da aplicação
├── navigation/                        <-- Gerencia a navegação entre telas (NavGraph)
├── ui/
│   ├── screen/                        <-- Contém as telas (composables) da UI
│   └── theme/                         <-- Define o tema visual (cores, fontes, etc.)
├── util/                              <-- Funções utilitárias (e.g., leitura de arquivos, prefs)
├── viewmodel/                         <-- Contém os ViewModels (estado e lógica de UI)
```

---

### ✅ Isso se alinha com a arquitetura recomendada pela Google:

* **UI Layer (Compose Screens):** `ui/screen`
* **State Management:** `viewmodel/`
* **Navigation:** `navigation/`
* **Utilities:** `util/` (às vezes movido para `core/` ou `common/`)
* **Theme:** `ui/theme/`

---

### 📦 Nome técnico:

Essa abordagem é chamada de **"modularização por feature (ou por camada funcional)"** dentro de um único módulo. Ela é um primeiro passo antes de um projeto escalonar para **modularização real entre módulos Gradle**, como `:feature:home`, `:core:ui`, etc.

---

Se quiser, podemos continuar com melhorias incrementais, como:

* Adicionar suporte a `Hilt` ou `Koin` para injeção de dependência.
* Criar um `model/` para tipos como `MarkdownFile`.
* Separar mais logicamente `data/` e `domain/`.

---











---

Profavor analise o seguinte repositório no github:

Profavor analise os arquivos dentro de Filegrouper no seguiente path /app/src/main/java/com/Filegrouper do seguinte repositório no github:
https://github.com/segodimor2d2/app-hierarchylist


├── MainActivity.kt
├── navigation
│   └── AppNavGraph.kt
├── ui
│   ├── screen
│   │   ├── CompareScreen.kt
│   │   ├── EditScreen.kt
│   │   ├── HomeScreen.kt
│   │   └── RankingScreen.kt
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── util
│   ├── FileUtils.kt
│   └── PreferencesUtil.kt
└── viewmodel
    └── SharedViewModel.kt

porfavor me diga como eu consegui criar um arquivo na tela HomeScreen e salvar na pasta que di permições na pasta?

---

# Como Mostrar/Esconder Botões no Jetpack Compose

Para fazer com que o segundo botão apareça quando o primeiro for pressionado e o primeiro desapareça, você pode usar um estado para controlar a visibilidade. Aqui está como implementar:

```kotlin
@Composable
fun TwoButtonsScreen() {
    // Estado para controlar qual botão está visível
    var showFirstButton by remember { mutableStateOf(true) }

    Column {
        if (showFirstButton) {
            Button(
                onClick = {
                    // Quando clicado, esconde este botão e mostra o próximo
                    showFirstButton = false
                }
            ) {
                Text("Criar uma lista")
            }
        }

        if (!showFirstButton) {
            Button(
                onClick = { 
                    folderPicker.launch(null) 
                    // Se quiser voltar ao estado original depois:
                    // showFirstButton = true
                }
            ) {
                Text("Escolha uma pasta e de permissões para Criar a lista")
            }
        }
    }
}
```

## Explicação:

1. Criamos um estado `showFirstButton` que controla qual botão está visível
2. Usamos condicionais `if` para mostrar/esconder os botões com base no estado
3. Quando o primeiro botão é clicado, mudamos o estado para `false`
4. O segundo botão só aparece quando `showFirstButton` é `false`

## Alternativa mais compacta:

Se preferir um código mais conciso, pode usar `AnimatedVisibility` para transições suaves:

```kotlin
@Composable
fun TwoButtonsScreen() {
    var showFirstButton by remember { mutableStateOf(true) }

    Column {
        AnimatedVisibility(visible = showFirstButton) {
            Button(onClick = { showFirstButton = false }) {
                Text("Criar uma lista")
            }
        }

        AnimatedVisibility(visible = !showFirstButton) {
            Button(onClick = { folderPicker.launch(null) }) {
                Text("Escolha uma pasta e de permissões para Criar a lista")
            }
        }
    }
}
```

Lembre-se de importar `androidx.compose.animation.AnimatedVisibility` se usar esta versão.

---



# Usando Telescope live_grep para Renomear Strings em Múltiplos Arquivos

O `Telescope live_grep` é uma ferramenta poderosa para buscar e substituir strings em vários arquivos. Aqui está como usá-lo para renomear:

## Passo a Passo Básico

1. **Abra o live_grep**:
   ```vim
   :Telescope live_grep
   ```
   Ou use o atalho (se configurado, normalmente `<leader>fg`)

2. **Digite o termo que deseja buscar** e pressione Enter

3. **Selecione os resultados** que deseja modificar:
   - Navegue com `j/k` ou setas
   - Marque múltiplos arquivos com `<Tab>`

4. **Abra todos os selecionados**:
   Pressione `<C-q>` para abrir todos os resultados marcados na quickfix list

## Fazendo a Substituição

5. **Execute a substituição** em todos os arquivos da quickfix:
   ```vim
   :cfdo %s/termo_antigo/termo_novo/gc | update
   ```
   - O `c` no final pede confirmação para cada substituição
   - Remova o `c` se quiser substituir sem confirmação

## Método Alternativo com Telescope + Sed

1. Busque com `live_grep` como acima

2. Pressione `<C-t>` para abrir os resultados em abas

3. Use:
   ```vim
   :tabdo %s/termo_antigo/termo_novo/gc | update
   ```

## Configuração Útil (opcional)

Adicione ao seu `init.lua` para facilitar:
```lua
local actions = require('telescope.actions')
require('telescope').setup({
  defaults = {
    mappings = {
      i = {
        ['<C-s>'] = actions.send_to_qflist + actions.open_qflist,
      },
    },
  },
})
```
Isso permite pressionar `<C-s>` após a busca para enviar diretamente para a quickfix.

## Dicas

- Use `:Telescope grep_string` para buscar a palavra sob o cursor
- Combine com `nvim-spectre` para substituições mais avançadas
- Para projetos grandes, considere usar `rg` ou `ag` como backend para maior velocidade

---

:cfdo %s/termo_antigo/termo_novo/gc | update
:cfdo %s/com.reccode.ui/termo_antigo.filegrouper/gc | update
:cfdo %s/termo_antigo.filegrouper/com.reccode.filegrouper.ui./gc | update
:cfdo %s/com.reccode./com.reccode.filegrouper./gc | update
