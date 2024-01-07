# Simulador-de-Provas-Programacao-Concorrente-
Programa que simula um processo de provas academicas em Java. Usando Multi-Threading

#### Classe `Docente`:
- Implementa a interface `Runnable`, representando o docente.
- O docente aguarda até que todos os alunos peguem a prova (`provaPronta.countDown()` é chamado para cada aluno).
- Quando todos os alunos pegaram a prova, o docente permite que os alunos iniciem as provas (`alunosTerminaram.await()` é chamado para esperar que todos terminem).
- Imprime mensagens sobre a distribuição de provas.

#### Classe `Aluno`:
- Implementa a interface `Runnable`, representando os alunos.
- Os alunos aguardam até que a prova esteja pronta (`provaPronta.await()`).
- Cada aluno inicia a sua prova, espera um tempo aleatório (simulando o tempo que cada aluno leva para terminar a prova) e, em seguida, registra no arquivo "folha_presenca.txt" que terminou a prova.
- Imprime mensagens indicando o início e o término da prova.

### Classe `Frequencia1` (Main):
- Define o conjunto de alunos (`String[] nomesAlunos`) e inicializa três `CountDownLatch`:
  - `provaPronta`: para sinalizar quando a prova está pronta para os alunos.
  - `alunosTerminaram`: para sinalizar quando todos os alunos terminaram a prova.
  - `alunosPegaramProva`: utilizado pelo docente para contabilizar quantos alunos pegaram a prova.
- Inicializa um `Thread` para o `Docente` e um array de `Threads` para os `Alunos`.
- Cada aluno é associado a uma `Thread` e começa a execução.
- O docente é colocado em uma `Thread` separada e também é iniciado.

### Funcionamento Resumido:
1. O docente inicia a distribuição das provas.
2. Cada aluno pega a prova após ela ser disponibilizada pelo docente.
3. Quando todos os alunos pegaram a prova, o docente permite que iniciem a resolução.
4. Cada aluno começa sua prova, simula um tempo aleatório para resolvê-la e registra o término no arquivo.
5. Após todos terminarem, a execução é encerrada.

Esse código ilustra a sincronização de threads com `CountDownLatch` para coordenar o início e o término de atividades em um ambiente multithreaded, simulando uma situação de sala de aula.
