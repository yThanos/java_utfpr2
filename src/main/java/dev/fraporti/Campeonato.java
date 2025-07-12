package dev.fraporti;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vitor.rosmann on 11/07/2025
 */
public class Campeonato {
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
