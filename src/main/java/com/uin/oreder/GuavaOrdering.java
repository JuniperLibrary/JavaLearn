package com.uin.oreder;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dingchuan
 */
public class GuavaOrdering {

  public static void main(String[] args) {
    Ordering<String> ordering = new Ordering<String>() {
      public int compare(String left, String right) {
        return Ints.compare(left.length(), right.length());
      }
    };

    Ordering.natural()
        .nullsFirst().onResultOf((Function<Object, String>) Object::toString);

    List<Integer> list = new ArrayList<>();
    list.stream().sorted(Comparator.comparing(integer -> 0)).collect(Collectors.toList());
  }
}
