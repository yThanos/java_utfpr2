package dev.fraporti;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vitor.rosmann on 11/07/2025
 */
public class Chegada {
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
