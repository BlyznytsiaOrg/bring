package com.bobocode.bring.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pair<L, R> {

    L left;
  
    R right;
  
    public static <L, R> Pair<L, R> of(L left, R right) {
      return new Pair<>(left, right);
    }

}
