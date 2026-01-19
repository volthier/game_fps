# game_fps

## Stack (Java 25)
- **Java 25 (toolchain)**: linguagem principal, usando recursos modernos da JVM.
- **LWJGL 3.3.3**: acesso a GPU via OpenGL/GLFW.
- **JOML**: matemática 3D (vetores e matrizes).
- **Gradle**: build e gerenciamento de dependências.

## Controles
- **WASD**: movimentação
- **Mouse**: mira
- **Clique esquerdo**: atirar
- **R**: recarregar
- **1/2**: troca de armas
- **Shift**: correr
- **Esc**: sair

## Como executar
```bash
gradle run
```

## Requisitos
- **JDK 25** instalado (o Gradle Toolchain vai selecionar automaticamente).
- **Drivers OpenGL** atualizados (GPU com suporte a OpenGL 3.3+).
- Em Linux, tenha um **servidor X/Wayland** disponível para abrir a janela.

## Observação sobre Gradle Wrapper
Este repositório não inclui o `gradle-wrapper.jar` por política de não aceitar binários.
Use a instalação local do Gradle para executar o projeto.

## Estrutura
- `Game`: loop principal e orquestração
- `Window`: criação da janela OpenGL
- `Renderer`: renderização de mundo, inimigos e HUD
- `HudRenderer`: overlay 2D com status do jogador e instruções
- `Player`, `Weapon`, `Enemy`, `World`: gameplay e lógica
