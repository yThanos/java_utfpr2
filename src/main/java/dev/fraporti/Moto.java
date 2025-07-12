package dev.fraporti;

/**
 * @author vitor.rosmann on 11/07/2025
 */
public class Moto extends Thread {
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
