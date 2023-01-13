package com.sample.crm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ListUtil. 2022/1/19 下午 01:40
 *
 * @author Sero
 * @version 1.0.0
 **/
public final class ListUtil {

    private ListUtil() {}

    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> List<T> synchronizedList() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    @SafeVarargs
    public static <T> List<T> asList(T... element) {
        if (element.length == 1) {
            return Collections.singletonList(element[0]);
        }
        return Arrays.stream(element).toList();
    }

}
