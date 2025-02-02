package com.project.progress_tracking_system_v2.utility;

import com.project.progress_tracking_system_v2.annotation.HideFromReflection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class Utils {
    public static <T> T requireFieldsNonNull(T obj, Field... exceptional) throws IllegalAccessException {
        Objects.requireNonNull(obj);
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(HideFromReflection.class)
                    && Arrays.stream(exceptional).noneMatch(ignore -> ignore.equals(field))) {
                Objects.requireNonNull(field.get(obj));
            }
        }
        return obj;
    }
}
