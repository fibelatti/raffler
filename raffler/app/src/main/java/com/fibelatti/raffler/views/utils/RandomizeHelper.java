package com.fibelatti.raffler.views.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fibelatti on 12/08/16.
 */
public class RandomizeHelper {
    public static List<Integer> getRandomIndexesInRange(int quantity, int limit) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i <= limit; i++) {
            list.add(i);
        }

        Collections.shuffle(list);

        if (quantity > limit) {
            return list;
        }

        List<Integer> chosenNumbers = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            chosenNumbers.add(list.get(i));
        }

        return chosenNumbers;
    }
}
