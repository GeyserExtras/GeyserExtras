package dev.letsgoaway.geyserextras.core.utils;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;

// From Hurricane
public final class ReflectionAPI {
    private static boolean staticFinalModificationBlocked;
    private static final Unsafe unsafe;

    static {
        try {
            Field.class.getDeclaredField("modifiers");
        }  catch (NoSuchFieldException ex) {
            staticFinalModificationBlocked = true;
        }

        boolean isJava16 = Float.parseFloat(System.getProperty("java.class.version")) >= 60;
        if (isJava16) {
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (Unsafe) unsafeField.get(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            unsafe = null;
        }
    }

    public static Field getField(Class clazz, String fieldname) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldname);
        } catch (NoSuchFieldException ignored) {}
        return field;
    }

    public static boolean hasField(Class clazz, String fieldname) {
        return getField(clazz, fieldname) != null;
    }

    public static Field getFieldAccessible(Class clazz, String fieldname) {
        Field field = getField(clazz, fieldname);
        if (field != null) field.setAccessible(true);
        return field;
    }

    public static void setFieldNotFinal(Field field) {
        int modifiers = field.getModifiers();
        if (!Modifier.isFinal(modifiers)) return;

        if (staticFinalModificationBlocked) {
            try {
                Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
                getDeclaredFields0.setAccessible(true);
                Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
                for (Field classField : fields) {
                    if ("modifiers".equals(classField.getName())) {
                        classField.setAccessible(true);
                        classField.set(field, modifiers & ~Modifier.FINAL);
                        break;
                    }
                }
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        } else {
            setValuePrintException(Field.class, field, "modifiers", modifiers & ~Modifier.FINAL);
        }
    }

    private static void setJava16Field(Object object, Field field, Object result) {
        boolean isStatic = Modifier.isStatic(field.getModifiers());
        long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);
        if (isStatic) {
            unsafe.putObject(unsafe.staticFieldBase(field), offset, result);
        } else {
            unsafe.putObject(object, offset, result);
        }
    }

    public static <E> Constructor<E> getEmptyConstructor(Class<E> clazz) {
        try {
            return getConstructor(clazz, Object.class.getDeclaredConstructor());
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> Constructor<E> getConstructor(Class<E> clazz, Constructor<? super E> superConstructor) {
        return (Constructor<E>) ReflectionFactory.getReflectionFactory().newConstructorForSerialization(clazz, superConstructor);
    }

    public static <E> E getEmptyObject(Class<E> clazz) {
        Constructor<E> constructor = getEmptyConstructor(clazz);
        try {
            return clazz.cast(constructor.newInstance());
        } catch (InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <O, E extends O> E copyAndExtendObject(O object, Class<E> clazz) {
        if (!object.getClass().isAssignableFrom(clazz)) throw new IllegalArgumentException(clazz.getName() + " is not compatible to " + object.getClass().getName());

        E copy = getEmptyObject(clazz);

        Class<?> current = object.getClass();
        do {
            for (Field f : current.getDeclaredFields()) {
                int modifiers = f.getModifiers();
                if (Modifier.isStatic(modifiers)) continue;
                if (Modifier.isFinal(modifiers)) setFieldNotFinal(f);
                if (!f.isAccessible()) f.setAccessible(true);
                try {
                    f.set(copy, f.get(object));
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        } while (((current = current.getSuperclass()) != Object.class));

        return copy;
    }

    public static <E> E invokeMethod(Object object, Method method, Class<E> returnType, Object... params) {
        try {
            return (E) method.invoke(object, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E invokeMethod(Method method, Class<E> returnType, Object... params) {
        return invokeMethod(null, method, returnType, params);
    }

    public static <E> E invokeMethod(Object object, Method method, Object... params) {
        try {
            return (E) method.invoke(object, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E invokeMethod(Method method, Object... params) {
        return invokeMethod(null, method, params);
    }

    public static Method getMethod(Class clazz, String methodname, Class<?>... params) throws NoSuchMethodException {
        if (clazz == null) throw new NullPointerException();
        NoSuchMethodException exception = null;
        while (clazz != null) {
            try {
                Method method = clazz.getDeclaredMethod(methodname, params);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ex) {
                exception = ex;
            }
            clazz = clazz.getSuperclass();
        }
        if (exception != null) {
            throw exception;
        } else {
            throw new IllegalStateException();
        }
    }

    public static Method getMethodPrintException(Class clazz, String methodname, Class<?>... params) {
        try {
            return getMethod(clazz, methodname, params);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Method getMethodOrNull(Class clazz, String methodname, Class<?>... params) {
        try {
            return getMethod(clazz, methodname, params);
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static <E> E getValue(Class clazz, Object object, String fieldname) throws NoSuchFieldException, IllegalAccessException {
        return (E) getFieldAccessible(clazz, fieldname).get(object);
    }

    public static <E> E getValue(Object object, String fieldname) throws NoSuchFieldException, IllegalAccessException {
        return getValue(object.getClass(), object, fieldname);
    }

    public static <E> E getValue(Class clazz, String fieldname) throws NoSuchFieldException, IllegalAccessException {
        return getValue(clazz, null, fieldname);
    }

    public static <E> E getValue(Field field) throws IllegalAccessException {
        return (E) field.get(null);
    }

    public static <E> E getValue(Object object, Field field) throws IllegalAccessException {
        return (E) field.get(object);
    }

    public static <E> E getValuePrintException(Class clazz, Object object, String fieldname) {
        try {
            return getValue(clazz, object, fieldname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E getValuePrintException(Object object, String fieldname) {
        try {
            return getValue(object, fieldname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E getValuePrintException(Class clazz, String fieldname) {
        try {
            return getValue(clazz, fieldname);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E getValuePrintException(Field field) {
        try {
            return (E) field.get(null);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E getValuePrintException(Object object, Field field) {
        try {
            return (E) field.get(object);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <E> E getValueIgnoreException(Class clazz, Object object, String fieldname) {
        try {
            return getValue(clazz, object, fieldname);
        } catch (Exception ignored) {}
        return null;
    }

    public static <E> E getValueIgnoreException(Object object, String fieldname) {
        try {
            return getValue(object, fieldname);
        } catch (Exception ignored) {}
        return null;
    }

    public static <E> E getValueIgnoreException(Class clazz, String fieldname) {
        try {
            return getValue(clazz, fieldname);
        } catch (Exception ignored) {}
        return null;
    }

    public static <E> E getValueIgnoreException(Field field) {
        try {
            return (E) field.get(null);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public static <E> E getValueIgnoreException(Object object, Field field) {
        try {
            return (E) field.get(object);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public static void setValue(Class clazz, Object object, String fieldname, Object value, boolean isFinal) throws NoSuchFieldException, IllegalAccessException {
        Field field = getFieldAccessible(clazz, fieldname);
        if (isFinal) setFieldNotFinal(field);
        field.set(object, value);
    }

    public static void setValue(Class clazz, Object object, String fieldname, Object value) throws NoSuchFieldException, IllegalAccessException {
        setValue(clazz, object, fieldname, value, false);
    }

    public static void setValue(Object object, String fieldname, Object value) throws NoSuchFieldException, IllegalAccessException {
        setValue(object.getClass(), object, fieldname, value, false);
    }

    public static void setValue(Class clazz, String fieldname, Object value) throws NoSuchFieldException, IllegalAccessException {
        setValue(clazz, null, fieldname, value, false);
    }

    public static void setValue(Field field, Object value) throws IllegalAccessException {
        field.set(null, value);
    }

    public static void setValue(Object object, Field field, Object value) throws IllegalAccessException {
        field.set(object, value);
    }

    public static void setFinalValue(Class clazz, Object object, String fieldname, Object value) throws NoSuchFieldException, IllegalAccessException {
        setValue(clazz, object, fieldname, value, true);
    }

    public static void setFinalValue(Object object, String fieldname, Object value) throws NoSuchFieldException, IllegalAccessException {
        setValue(object.getClass(), object, fieldname, value, true);
    }

    public static void setFinalValue(Class clazz, String fieldname, Object value) throws NoSuchFieldException, IllegalAccessException {
        setValue(clazz, null, fieldname, value, true);
    }

    public static void setFinalValue(Field field, Object value) throws IllegalAccessException {
        if (unsafe != null) {
            setJava16Field(null, field, value);
        } else {
            setFieldNotFinal(field);
            field.set(null, value);
        }
    }

    public static void setFinalValue(Object object, Field field, Object value) throws IllegalAccessException {
        setFieldNotFinal(field);
        field.set(object, value);
    }

    public static void setValuePrintException(Class clazz, Object object, String fieldname, Object value) {
        try {
            setValue(clazz, object, fieldname, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setValuePrintException(Object object, String fieldname, Object value) {
        try {
            setValue(object, fieldname, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setValuePrintException(Class clazz, String fieldname, Object value) {
        try {
            setValue(clazz, fieldname, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setValuePrintException(Field field, Object value) {
        try {
            field.set(null, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public static void setValuePrintException(Object object, Field field, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public static void setFinalValuePrintException(Class clazz, Object object, String fieldname, Object value) {
        try {
            setFinalValue(clazz, object, fieldname, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFinalValuePrintException(Object object, String fieldname, Object value) {
        try {
            setFinalValue(object, fieldname, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFinalValuePrintException(Class clazz, String fieldname, Object value) {
        try {
            setFinalValue(clazz, fieldname, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setFinalValuePrintException(Field field, Object value) {
        setFieldNotFinal(field);
        try {
            field.set(null, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public static void setFinalValuePrintException(Object object, Field field, Object value) {
        setFieldNotFinal(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public static void setValueIgnoreException(Class clazz, Object object, String fieldname, Object value) {
        try {
            setValue(clazz, object, fieldname, value);
        } catch (Exception ignored) {}
    }

    public static void setValueIgnoreException(Object object, String fieldname, Object value) {
        try {
            setValue(object, fieldname, value);
        } catch (Exception ignored) {}
    }

    public static void setValueIgnoreException(Class clazz, String fieldname, Object value) {
        try {
            setValue(clazz, fieldname, value);
        } catch (Exception ignored) {}
    }

    public static void setValueIgnoreException(Field field, Object value) {
        try {
            field.set(null, value);
        } catch (IllegalAccessException ignored) {}
    }

    public static void setValueIgnoreException(Object object, Field field, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException ignored) {}
    }

    public static void setFinalValueIgnoreException(Class clazz, Object object, String fieldname, Object value) {
        try {
            setFinalValue(clazz, object, fieldname, value);
        } catch (Exception ignored) {}
    }

    public static void setFinalValueIgnoreException(Object object, String fieldname, Object value) {
        try {
            setFinalValue(object, fieldname, value);
        } catch (Exception ignored) {}
    }

    public static void setFinalValueIgnoreException(Class clazz, String fieldname, Object value) {
        try {
            setFinalValue(clazz, fieldname, value);
        } catch (Exception ignored) {}
    }

    public static void setFinalValueIgnoreException(Field field, Object value) {
        setFieldNotFinal(field);
        try {
            field.set(null, value);
        } catch (IllegalAccessException ignored) {}
    }

    public static void setFinalValueIgnoreException(Object object, Field field, Object value) {
        setFieldNotFinal(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException ignored) {}
    }
}