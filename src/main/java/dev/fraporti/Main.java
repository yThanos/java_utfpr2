package dev.fraporti;

public class Main {
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
