package dev.fraporti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static class Moto extends Thread {
        private final Campeonato monitor;
        private final Chegada chegada;
        private int lap = 1;

        public Moto(String name, Chegada chegada, Campeonato monitor) {
            super(name);
            this.chegada = chegada;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            try {
                while (lap <= 10) {
                    synchronized (monitor) {
                        while (monitor.currentLap != lap) {
                            monitor.wait();
                        }
                    }

                    int pontos = chegada.cruzarLinha(lap);

                    synchronized (monitor) {
                        monitor.placar.put(this, monitor.placar.get(this) + pontos);
                        monitor.completaram++;

                        if (monitor.completaram == 10) {
                            monitor.completaram = 0;
                            monitor.currentLap++;
                            monitor.notifyAll();
                        }
                    }

                    lap++;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Chegada {
        private final Map<Integer, Integer> posicoes = new HashMap<>();

        public synchronized int cruzarLinha(int volta) {
            if(!posicoes.containsKey(volta)){
                posicoes.put(volta, 1);
            }
            int ordem = posicoes.get(volta) + 1;
            posicoes.put(volta, ordem);

            return 11 - ordem;
        }
    }

    public static class Campeonato {
        public int currentLap = 1;
        public int completaram = 0;
        public final Map<Moto, Integer> placar = new ConcurrentHashMap<>();

        public void printResult() {
            List<Map.Entry<Moto, Integer>> ranking = new ArrayList<>(placar.entrySet());
            ranking.sort((a, b) -> b.getValue() - a.getValue());

            System.out.println("\n==== Pódio ====");
            for (int i = 0; i < 3; i++) {
                System.out.printf("%s com %d pontos\n",
                        ranking.get(i).getKey().getName(), ranking.get(i).getValue());
            }

            System.out.println("\n==== Tabela de pontos ====");
            for (int i = 0; i < ranking.size(); i++) {
                System.out.printf("%s com %d pontos\n",
                        ranking.get(i).getKey().getName(), ranking.get(i).getValue());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Campeonato campeonato = new Campeonato();
        Chegada chegada = new Chegada();
        Moto[] motos = new Moto[10];

        for (int i = 0; i < motos.length; i++) {
            motos[i] = new Moto("Competidor #" + (i + 1), chegada, campeonato);
            campeonato.placar.put(motos[i], 0);
        }

        for (Moto m : motos) {
            m.start();
        }
        for (Moto m : motos) {
            m.join();
        }

        campeonato.printResult();
    }
}
