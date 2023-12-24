import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

class Docente implements Runnable {
    private final String[] alunos;
    private final CountDownLatch provaPronta;
    private final CountDownLatch alunosPegaramProva;
    private final CountDownLatch alunosTerminaram;

    public Docente(String[] alunos, CountDownLatch provaPronta, CountDownLatch alunosTerminaram, CountDownLatch alunosPegaramProva) {
        this.alunos = alunos;
        this.provaPronta = provaPronta;
        this.alunosTerminaram = alunosTerminaram;
        this.alunosPegaramProva = alunosPegaramProva;
    }

    @Override
    public void run() {
        try {
            System.out.println("Prova iniciada");
            System.out.println("Docente distribuindo as provas");

            for (int i = 0; i < alunos.length; i += 2) {
                System.out.println("\nDocente colocou as provas na mesa");

                Thread.sleep(1500);
                int alunosQuePegaram = 0;
                while (alunosQuePegaram < 2) {
                    // Aguardando
                    if (provaPronta.getCount() > 0) {
                        System.out.println("Sou o aluno: " + alunos[i + alunosQuePegaram] + " peguei a minha prova");
                        alunosPegaramProva.countDown(); // aluno pegou a prova
                        alunosQuePegaram++;
                    }
                }
                Thread.sleep(3500);
            }
            //Caro professor somente por estetica dividi o tempo [5s = 1,5s + 3,5s]

            System.out.println("\nAlunos podem iniciar as provas\n");
            for (int x = 0; x < alunos.length; x++){
                provaPronta.countDown();
            }

            alunosTerminaram.await();

            System.out.println("Todos os alunos já terminaram as suas respectivas provas.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Aluno implements Runnable {
    private final String nome;
    private final CountDownLatch provaPronta;
    private final CountDownLatch alunosTerminaram;

    public Aluno(String nome, CountDownLatch provaPronta, CountDownLatch alunosTerminaram) {
        this.nome = nome;
        this.provaPronta = provaPronta;
        this.alunosTerminaram = alunosTerminaram;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(250);
            provaPronta.await();

            System.out.println("Sou aluno " + nome + " e iniciei a minha prova");


            Random random = new Random();
            int segundosAleatorios = random.nextInt(5400); //número aleatório entre 0 e 5400 segundos
            long tempoProva = segundosAleatorios * 1000L; //convertendo o valor encontrado para millisegundos
            Thread.sleep(tempoProva);


            FileWriter fileWriter = new FileWriter("folha_presenca.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Aluno: " + nome + ", ID: " + Thread.currentThread().getId() + " terminei a prova em " + tempoProva + "ms");
            printWriter.close();

            System.out.println("Sou aluno " + nome + " e terminei a minha prova");

            // aluno terminou a prova
            alunosTerminaram.countDown();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

public class Frequencia1 {
    public static void main(String[] args) {
        String[] nomesAlunos = {"Pericles", "Marco", "Naomy", "Fabricia", "Silvano", "Denis", "Rafael", "Edmilson", "Amadeu", "Cesar"};
        CountDownLatch provaPronta = new CountDownLatch(nomesAlunos.length);
        CountDownLatch alunosTerminaram = new CountDownLatch(nomesAlunos.length);
        CountDownLatch alunosPegaramProva = new CountDownLatch(nomesAlunos.length);


        Thread docenteThread = new Thread(new Docente(nomesAlunos, provaPronta, alunosTerminaram, alunosPegaramProva));
        Thread[] alunoThreads = new Thread[nomesAlunos.length];

        for (int i = 0; i < nomesAlunos.length; i++) {
            alunoThreads[i] = new Thread(new Aluno(nomesAlunos[i], provaPronta, alunosTerminaram));
            alunoThreads[i].start();
        }

        docenteThread.start();
    }
  }
