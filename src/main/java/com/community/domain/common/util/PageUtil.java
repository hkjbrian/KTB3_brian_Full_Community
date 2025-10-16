package com.community.domain.common.util;

import java.util.List;

public class PageUtil {

    public static <T> List<T> paginate(List<T> source, int page, int size) {
        if (source.isEmpty()) {
            return List.of();
        }
        int normalizedSize = Math.max(size, 0);
        int normalizedPage = Math.max(page, 0);
        int fromIndex = normalizedPage * normalizedSize;
        if (fromIndex >= source.size()) {
            return List.of();
        }
        int toIndex = Math.min(source.size(), fromIndex + normalizedSize);
        return source.subList(fromIndex, toIndex);
    }
}
