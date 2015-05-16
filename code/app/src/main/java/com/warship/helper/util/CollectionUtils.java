package com.warship.helper.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Usage: Util for collections.
 *
 * @author zhulantian@wandoujia.com.
 */
public class CollectionUtils {

  /**
   * replace all elements from start with elements in source.
   * Some adapter use this method to append newPart to originData from position.
   */
  public static <T> List<T> replaceFromPosition(List<T> origin, List<T> newPart, int pos) {
    if (origin == null || origin.isEmpty()) {
      return newPart;
    }
    if (newPart == null || newPart.isEmpty()) {
      return origin;
    }
    if (pos > origin.size()) {// append to end of list
      pos = origin.size();
    }
    List<T> result = new ArrayList<T>(origin);// avoid operation about origin list
    int newSize = pos + newPart.size();
    result.addAll(pos, newPart);
    while (result.size() > newSize) {
      result.remove(result.size() - 1); // remove last until size is right.
    }
    return result;
  }

  /**
   * append the newPart to the origin from pos.
   */
  public static <T> List<T> appendFromPosition(List<T> origin, List<T> newPart, int pos) {
    if (origin == null || origin.isEmpty()) {
      List<T> result = new ArrayList<T>();
      if (newPart != null) {
        result.addAll(newPart); // clone
      }
      return result;
    }
    if (newPart == null || newPart.isEmpty()) {
      return origin;
    }
    if (pos > origin.size()) {// append to end of list
      pos = origin.size();
    }
    List<T> result = new ArrayList<T>(origin);// avoid operation about origin list
    result.addAll(pos, newPart);
    return result;
  }

  public static <T> boolean isEmpty(Collection<T> list) {
    return list == null || list.isEmpty();
  }

  public static <T> ArrayList<T> copyFrom(List<T> list) {
    if (list == null) {
      return null;
    }
    ArrayList<T> result = new ArrayList<T>(list);
    return result;
  }

  public static <T> boolean equals(List<T> a, List<T> b, Comparator<T> comparator) {
    if (a == b) {
      return true;
    }
    int size;
    if (a != null && b != null && (size = a.size()) == b.size()) {
      for (int i = 0; i < size; i++) {
        if (comparator.compare(a.get(i), b.get(i)) != 0) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}
