package com.petbackend.thbao.annotation;

import com.petbackend.thbao.exceptions.InvalidDateException;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class BirthDateProcessor {
    public static void validateBirthDay(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field[] = clazz.getDeclaredFields();
        for (Field item : field){
            if (item.isAnnotationPresent(BirthDate.class)){
                item.setAccessible(true);
                LocalDate birthDay = (LocalDate) item.get(object);
                BirthDate birth = item.getAnnotation(BirthDate.class);
                LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
                if (birthDay == null){
                    throw new InvalidDateException("Ngày tháng năm sinh không được bỏ trống");
                }
                if (!birthDay.isBefore(eighteenYearsAgo)){
                    throw new InvalidDateException("Bạn chưa đủ độ tuổi để thực hiện đăng kí");
                }
            }
        }
    }
}
