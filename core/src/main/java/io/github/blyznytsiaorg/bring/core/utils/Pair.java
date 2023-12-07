package io.github.blyznytsiaorg.bring.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A simple generic class representing a pair of two values: left and right.
 *
 * @param <L> The type of the left value
 * @param <R> The type of the right value
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@AllArgsConstructor
@Data
public class Pair<L, R> {

    /**
     * The left value of the pair.
     */
    L left;

    /**
     * The right value of the pair.
     */
    R right;

    /**
     * Creates a new Pair object with the specified left and right values.
     *
     * @param <L>   The type of the left value
     * @param <R>   The type of the right value
     * @param left  The left value
     * @param right The right value
     * @return A new Pair object with the given left and right values
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
      return new Pair<>(left, right);
    }

}
