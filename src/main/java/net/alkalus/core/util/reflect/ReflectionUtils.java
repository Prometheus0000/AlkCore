package net.alkalus.core.util.reflect;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.google.common.reflect.ClassPath;

import net.alkalus.api.objects.misc.AcLog;
import net.alkalus.api.objects.misc.ReflectionCache;

public class ReflectionUtils {

    private static ReflectionCache mReflectionCache = new ReflectionCache();

    /**
     * Returns a cached {@link Class} object.
     * 
     * @param aClassCanonicalName - The canonical name of the underlying class.
     * @return - Valid, {@link Class} object, or {@link null}.
     */
    public static Class getClass(final String aClassCanonicalName) {
        return mReflectionCache.getClass(aClassCanonicalName);
    }

    /**
     * Returns a cached {@link Method} object. Wraps
     * {@link #getMethod(Class, String, Class...)}.
     * 
     * @param aObject     - Object containing the Method.
     * @param aMethodName - Method's name in {@link String} form.
     * @param aTypes      - Class Array of Types for {@link Method}'s
     *                    constructor.
     * @return - Valid, non-final, {@link Method} object, or {@link null}.
     */
    public static Method getMethod(final Object aObject, final String aMethodName,
            final Class[] aTypes) {
        return mReflectionCache.getMethod(aObject, aMethodName, aTypes);
    }

    /**
     * Returns a cached {@link Method} object.
     * 
     * @param aClass      - Class containing the Method.
     * @param aMethodName - Method's name in {@link String} form.
     * @param aTypes      - Varags Class Types for {@link Method}'s constructor.
     * @return - Valid, non-final, {@link Method} object, or {@link null}.
     */
    public static Method getMethod(final Class aClass, final String aMethodName,
            final Class... aTypes) {
        return mReflectionCache.getMethod(aClass, aMethodName, aTypes);
    }

    /**
     * Returns a cached {@link Field} object.
     * 
     * @param aClass     - Class containing the Method.
     * @param aFieldName - Field name in {@link String} form.
     * @return - Valid, non-final, {@link Field} object, or {@link null}.
     */
    public static Field getField(final Class aClass, final String aFieldName) {
        return mReflectionCache.getField(aClass, aFieldName);
    }

    /**
     * Returns a cached {@link Field} object.
     * 
     * @param aInstance  - {@link Object} to get the field instance from.
     * @param aFieldName - Field name in {@link String} form.
     * @return - Valid, non-final, {@link Field} object, or {@link null}.
     */
    public static <T> T getField(final Object aInstance,
            final String aFieldName) {
        return mReflectionCache.getField(aInstance, aFieldName);
    }

    /*
     * Utility Functions
     */

    public static boolean doesClassExist(final String classname) {
        return mReflectionCache.isClassPresent(classname);
    }

    public static void makeFieldAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier
                .isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    public static void makeMethodAccessible(final Method method) {
        if (!Modifier.isPublic(method.getModifiers()) || !Modifier
                .isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     * 
     * @param depth depth in the call stack (0 means current method, 1 means
     *              call method, ...)
     * @return Method name
     */
    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = new Throwable().getStackTrace();
        // System.
        // out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        return ste[depth + 1].getMethodName();
    }

    /**
     * 
     * @param aPackageName - The full {@link Package} name in {@link String}
     *                     form.
     * @return - {@link Boolean} object. True if loaded > 0 classes.
     */
    public static boolean dynamicallyLoadClassesInPackage(final String aPackageName) {
        final ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
        int loaded = 0;
        try {
            final ClassPath path = ClassPath.from(classLoader);
            for (final ClassPath.ClassInfo info : path
                    .getTopLevelClassesRecursive(aPackageName)) {
                final Class<?> clazz = Class.forName(info.getName(), true,
                        classLoader);
                if (clazz != null) {
                    loaded++;
                    AcLog.WARNING("Found " + clazz.getCanonicalName() + ". ["
                            + loaded + "]");
                }
            }
        } catch (ClassNotFoundException | IOException e) {

        }

        return loaded > 0;
    }

    public static boolean setField(final Object object, final String fieldName,
            final Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                final Field field = getField(clazz, fieldName);
                if (field != null) {
                    // Handle Static Fields
                    if (Modifier.isStatic(field.getModifiers())) {
                        ReflectionUtils.setFieldValue_Internal(null, field,
                                fieldValue);
                    } else {
                        ReflectionUtils.setFieldValue_Internal(object, field,
                                fieldValue);
                    }
                    return true;
                }
            } catch (final NoSuchFieldException e) {
                AcLog.WARNING("setField(" + object.toString() + ", " + fieldName
                        + ") failed.");
                clazz = clazz.getSuperclass();
            } catch (final Exception e) {
                AcLog.WARNING("setField(" + object.toString() + ", " + fieldName
                        + ") failed.");
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    /**
     * Allows to change the state of an immutable instance. Huh?!?
     */
    public static void setFinalFieldValue(final Class<?> clazz, final String fieldName,
            final Object newValue) throws Exception {
        final Field nameField = getField(clazz, fieldName);
        setFieldValue_Internal(clazz, nameField, newValue);
    }

    @Deprecated
    public static void setFinalStatic(final Field field, final Object newValue)
            throws Exception {
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public static void setByte(final Object clazz, final String fieldName, final byte newValue)
            throws Exception {
        final Field nameField = getField(clazz.getClass(), fieldName);
        nameField.setAccessible(true);
        int modifiers = nameField.getModifiers();
        final Field modifierField = nameField.getClass()
                .getDeclaredField("modifiers");
        modifiers = modifiers & ~Modifier.FINAL;
        modifierField.setAccessible(true);
        modifierField.setInt(nameField, modifiers);
        // Utils.LOG_INFO("O-"+(byte) nameField.get(clazz) + " | "+newValue);
        nameField.setByte(clazz, newValue);
        // Utils.LOG_INFO("N-"+(byte) nameField.get(clazz));

        /*
         * final Field fieldA = getField(clazz.getClass(), fieldName);
         * fieldA.setAccessible(true); fieldA.setByte(clazz, newValue);
         */

    }

    public static boolean invoke(final Object objectInstance, final String methodName,
            final Class[] parameters, final Object[] values) {
        if (objectInstance == null || methodName == null || parameters == null
                || values == null) {
            // AcLog.WARNING("Null value when trying to Dynamically invoke
            // "+methodName+" on an object of type:
            // "+objectInstance.getClass().getName());
            return false;
        }
        final Class<?> mLocalClass = (objectInstance instanceof Class
                ? (Class<?>) objectInstance
                : objectInstance.getClass());
        AcLog.WARNING("Trying to invoke " + methodName + " on an instance of "
                + mLocalClass.getCanonicalName() + ".");
        try {
            final Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName,
                    parameters);
            if (mInvokingMethod != null) {
                AcLog.WARNING(methodName + " was not null.");
                if ((boolean) mInvokingMethod.invoke(objectInstance, values)) {
                    AcLog.WARNING("Successfully invoked " + methodName + ".");
                    return true;
                } else {
                    AcLog.WARNING("Invocation failed for " + methodName + ".");
                }
            } else {
                AcLog.WARNING(methodName + " is null.");
            }
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            AcLog.WARNING("Failed to Dynamically invoke " + methodName
                    + " on an object of type: " + mLocalClass.getName());
        }

        AcLog.WARNING("Invoke failed or did something wrong.");
        return false;
    }

    public static boolean invokeVoid(final Object objectInstance, final String methodName,
            final Class[] parameters, final Object[] values) {
        if (objectInstance == null || methodName == null || parameters == null
                || values == null) {
            return false;
        }
        final Class<?> mLocalClass = (objectInstance instanceof Class
                ? (Class<?>) objectInstance
                : objectInstance.getClass());
        AcLog.WARNING("Trying to invoke " + methodName + " on an instance of "
                + mLocalClass.getCanonicalName() + ".");
        try {
            final Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName,
                    parameters);
            if (mInvokingMethod != null) {
                AcLog.WARNING(methodName + " was not null.");
                mInvokingMethod.invoke(objectInstance, values);
                AcLog.WARNING("Successfully invoked " + methodName + ".");
                return true;
            } else {
                AcLog.WARNING(methodName + " is null.");
            }
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            AcLog.WARNING("Failed to Dynamically invoke " + methodName
                    + " on an object of type: " + mLocalClass.getName());
        }

        AcLog.WARNING("Invoke failed or did something wrong.");
        return false;
    }

    public static Object invokeNonBool(final Object objectInstance, final String methodName,
            final Class[] parameters, final Object[] values) {
        if (objectInstance == null || methodName == null || parameters == null
                || values == null) {
            return false;
        }
        final Class<?> mLocalClass = (objectInstance instanceof Class
                ? (Class<?>) objectInstance
                : objectInstance.getClass());
        AcLog.WARNING("Trying to invoke " + methodName + " on an instance of "
                + mLocalClass.getCanonicalName() + ".");
        try {
            final Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName,
                    parameters);
            if (mInvokingMethod != null) {
                AcLog.WARNING(methodName + " was not null.");
                return mInvokingMethod.invoke(objectInstance, values);
            } else {
                AcLog.WARNING(methodName + " is null.");
            }
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            AcLog.WARNING("Failed to Dynamically invoke " + methodName
                    + " on an object of type: " + mLocalClass.getName());
        }

        AcLog.WARNING("Invoke failed or did something wrong.");
        return null;
    }

    /**
     * 
     * Set the value of a field reflectively.
     */
    private static void setFieldValue_Internal(final Object owner, final Field field,
            final Object value) throws Exception {
        makeModifiable(field);
        field.set(owner, value);
    }

    /**
     * Force the field to be modifiable and accessible.
     */
    private static void makeModifiable(final Field nameField) throws Exception {
        nameField.setAccessible(true);
        int modifiers = nameField.getModifiers();
        final Field modifierField = nameField.getClass()
                .getDeclaredField("modifiers");
        modifiers = modifiers & ~Modifier.FINAL;
        modifierField.setAccessible(true);
        modifierField.setInt(nameField, modifiers);
    }

}
