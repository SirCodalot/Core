package me.codalot.core.reflections;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@SuppressWarnings("all")
public class Reflection {

    protected Object object;
    protected Class clazz;

    public Reflection(Object object) {
        this.object = object;
        clazz = object.getClass();
    }

    public Field getField(String name) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    public Field getAccessibleField(String name) throws NoSuchFieldException {
        Field field = getField(name);
        field.setAccessible(true);
        return field;
    }

    public void setField(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(name);
        field.set(object, value);
    }

    public Method getMethod(String name, Class<?>... parameters) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(name, parameters);
    }

    public Method getAccessibleMethod(String name, Class<?>... parameters) throws NoSuchMethodException {
        Method method = getMethod(name, parameters);
        method.setAccessible(true);
        return method;
    }

}
