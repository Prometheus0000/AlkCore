package net.alkalus.api.objects.misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import net.alkalus.core.util.data.ArrayUtils;
import net.alkalus.core.util.data.StringUtils;
import net.alkalus.core.util.reflect.ReflectionUtils;


public class ReflectionCache implements Cloneable {

	public final Map<String, CachedClass> mCachedClasses;
	public final Map<String, CachedMethod> mCachedMethods;
	public final Map<String, CachedField> mCachedFields;

	public ReflectionCache() {
		mCachedClasses = new LinkedHashMap<String, CachedClass>();
		mCachedMethods = new LinkedHashMap<String, CachedMethod>();
		mCachedFields = new LinkedHashMap<String, CachedField>();
	}
	
	public ReflectionCache(Map<String, CachedClass> aCachedClasses, Map<String, CachedMethod> aCachedMethods, Map<String, CachedField> aCachedFields) {
		mCachedClasses = ArrayUtils.cloneMap(aCachedClasses);
		mCachedMethods = ArrayUtils.cloneMap(aCachedMethods);
		mCachedFields = ArrayUtils.cloneMap(aCachedFields);
	}

	@Override
	public ReflectionCache clone()  {
		return new ReflectionCache(mCachedClasses, mCachedMethods, mCachedFields);
	}

	private class CachedReflectiveObject<V> {

		private final boolean STATIC;
		private final V OBJECT;

		public CachedReflectiveObject(V aObject, boolean isStatic) {
			OBJECT = aObject;
			STATIC = isStatic;
		}

		public V get() {
			return OBJECT;
		}

		public boolean isStatic() {
			return STATIC;
		}

	}
	
	public class CachedClass extends CachedReflectiveObject {

		private final boolean SYNTHETIC;
		private final boolean INNER_CLASS;
		private final boolean ANONYMOUS;
		
		public CachedClass(Class aClass, boolean isStatic) {
			super(aClass, isStatic);			
			SYNTHETIC = aClass.isSynthetic();
			INNER_CLASS = aClass.isMemberClass();
			ANONYMOUS = aClass.isAnonymousClass();
		}
		
		public boolean isSynthetic() {
			return SYNTHETIC;
		}
		
		public boolean isInnerClass() {
			return INNER_CLASS;
		}
		
		public boolean isAnonymousClass() {
			return ANONYMOUS;
		}
		
		
	}
	
	public class CachedMethod extends CachedReflectiveObject {
		public CachedMethod(Method aMethod, boolean isStatic) {
			super(aMethod, isStatic);
		}

	}
	
	public class CachedField extends CachedReflectiveObject {
		public CachedField(Field aMethod, boolean isStatic) {
			super(aMethod, isStatic);
		}
	}

	private boolean cacheClass(Class aClass) {		
		if (aClass == null) {
			return false;
		}		
		CachedClass y = mCachedClasses.get(aClass.getCanonicalName());
		if (y == null) {
			boolean aStatic = Modifier.isStatic(aClass.getModifiers());
			mCachedClasses.put(aClass.getCanonicalName(), new CachedClass(aClass, aStatic));
			return true;
		}		
		return false;
	}

	private boolean cacheMethod(Class aClass, Method aMethod) {		
		if (aMethod == null) {
			return false;
		}		
		boolean isStatic = Modifier.isStatic(aMethod.getModifiers());		
		CachedMethod y = mCachedMethods.get(aClass.getName()+"."+aMethod.getName()+"."+ArrayUtils.toString(aMethod.getParameterTypes()));
		if (y == null) {
			mCachedMethods.put(aClass.getName()+"."+aMethod.getName()+"."+ArrayUtils.toString(aMethod.getParameterTypes()), new CachedMethod(aMethod, isStatic));
			return true;
		}		
		return false;
	}

	private boolean cacheField(Class aClass, Field aField) {		
		if (aField == null) {
			return false;
		}		
		boolean isStatic = Modifier.isStatic(aField.getModifiers());
		CachedField y = mCachedFields.get(aClass.getName()+"."+aField.getName());
		if (y == null) {
			mCachedFields.put(aClass.getName()+"."+aField.getName(), new CachedField(aField, isStatic));
			return true;
		}		
		return false;
	}


	/**
	 * Returns a cached {@link Class} object.
	 * @param aClassCanonicalName - The canonical name of the underlying class.
	 * @return - Valid, {@link Class} object, or {@link null}.
	 */
	public Class getClass(String aClassCanonicalName) {
		CachedClass y = mCachedClasses.get(aClassCanonicalName);
		Class z;
		if (y == null) {
			z = getClass_Internal(aClassCanonicalName);
			if (z != null) {
				AcLog.WARNING("Caching Class: "+aClassCanonicalName);
				cacheClass(z);
			}
		}
		else {
			z = (Class) y.get();
		}
		return z;
	}



	/**
	 * Returns a cached {@link Method} object. Wraps {@link #getMethod(Class, String, Class...)}.
	 * @param aObject - Object containing the Method.
	 * @param aMethodName - Method's name in {@link String} form.
	 * @param aTypes - Class Array of Types for {@link Method}'s constructor.
	 * @return - Valid, non-final, {@link Method} object, or {@link null}.
	 */
	public Method getMethod(Object aObject, String aMethodName, Class[] aTypes) {
		return getMethod(aObject.getClass(), aMethodName, aTypes);
	}
	
	
	/**
	 * Returns a cached {@link Method} object.
	 * @param aClass - Class containing the Method.
	 * @param aMethodName - Method's name in {@link String} form.
	 * @param aTypes - Varags Class Types for {@link Method}'s constructor.
	 * @return - Valid, non-final, {@link Method} object, or {@link null}.
	 */
	public Method getMethod(Class aClass, String aMethodName, Class... aTypes) {
		String aMethodKey = ArrayUtils.toString(aTypes);
		//AcLog.WARNING("Looking up method in cache: "+(aClass.getName()+"."+aMethodName + "." + aMethodKey));
		CachedMethod y = mCachedMethods.get(aClass.getName()+"."+aMethodName + "." + aMethodKey);
		if (y == null) {
			Method u = getMethod_Internal(aClass, aMethodName, aTypes);
			if (u != null) {
				AcLog.WARNING("Caching Method: "+aMethodName + "." + aMethodKey);
				cacheMethod(aClass, u);
				return u;
			} else {
				return null;
			}
		} else {
			return (Method) y.get();
		}
	}



	/**
	 * Returns a cached {@link Field} object.
	 * @param aClass - Class containing the Method.
	 * @param aFieldName - Field name in {@link String} form.
	 * @return - Valid, non-final, {@link Field} object, or {@link null}.
	 */
	public Field getField(final Class aClass, final String aFieldName) {
		CachedField y = mCachedFields.get(aClass.getName()+"."+aFieldName);
		if (y == null) {
			Field u;
			try {
				u = getField_Internal(aClass, aFieldName);
				if (u != null) {
					AcLog.WARNING("Caching Field '"+aFieldName+"' from "+aClass.getName());
					cacheField(aClass, u);
					return u;
				}
			} catch (NoSuchFieldException e) {
			}
			return null;

		} else {
			return (Field) y.get();
		}
	}
	
	/**
	 * Returns a cached {@link Field} object.
	 * @param aInstance - {@link Object} to get the field instance from.
	 * @param aFieldName - Field name in {@link String} form.
	 * @return - Valid, non-final, {@link Field} object, or {@link null}.
	 */
	public <T> T getField(final Object aInstance, final String aFieldName) {
		try {
			Object oo = getField(aInstance.getClass(), aFieldName).get(aInstance);
			if (oo != null) {
				@SuppressWarnings("unchecked")
				T op = (T) oo;
				if (op != null) {
					return op;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}
		return null;
	}
	
	
	/*
	 * Internal Magic that probably should not get exposed.
	 */	


	private Field getField_Internal(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
		try {
			AcLog.WARNING("Field: Internal Lookup: "+fieldName);
			Field k = clazz.getDeclaredField(fieldName);
			ReflectionUtils.makeFieldAccessible(k);
			//AcLog.WARNING("Got Field from Class. "+fieldName+" did exist within "+clazz.getCanonicalName()+".");
			return k;
		} catch (final NoSuchFieldException e) {
			AcLog.WARNING("Field: Internal Lookup Failed: "+fieldName);
			final Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				AcLog.WARNING("Unable to find field '"+fieldName+"'");	
				//AcLog.WARNING("Failed to get Field from Class. "+fieldName+" does not existing within "+clazz.getCanonicalName()+".");
				throw e;
			}
			AcLog.WARNING("Method: Recursion Lookup: "+fieldName+" - Checking in "+superClass.getName());
			//AcLog.WARNING("Failed to get Field from Class. "+fieldName+" does not existing within "+clazz.getCanonicalName()+". Trying super class.");
			return getField_Internal(superClass, fieldName);
		}
	}	

	/**
	 * if (isPresent("com.optionaldependency.DependencyClass")) || 
	 * This block will never execute when the dependency is not present. There is
	 * therefore no more risk of code throwing NoClassDefFoundException.
	 */
	public boolean isClassPresent(final String className) {
		try {
			Class.forName(className);
			return true;
		} catch (final Throwable ex) {
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	private Method getMethod_Internal(Class<?> aClass, String aMethodName, Class... aTypes) {
		Method m = null;
		try {
			AcLog.WARNING("Method: Internal Lookup: "+aMethodName);
			m = aClass.getDeclaredMethod(aMethodName, aTypes);	
			if (m != null) {
				m.setAccessible(true);
				int modifiers = m.getModifiers();
				Field modifierField = m.getClass().getDeclaredField("modifiers");
				modifiers = modifiers & ~Modifier.FINAL;
				modifierField.setAccessible(true);
				modifierField.setInt(m, modifiers);
			}
		}
		catch (Throwable t) {
			AcLog.WARNING("Method: Internal Lookup Failed: "+aMethodName);
			try {
				m = getMethodRecursively(aClass, aMethodName);
			} catch (NoSuchMethodException e) {
				AcLog.WARNING("Unable to find method '"+aMethodName+"'");	
				e.printStackTrace();
				dumpClassInfo(aClass);
			}
		}
		return m;
	}

	private Method getMethodRecursively(final Class<?> clazz, final String aMethodName) throws NoSuchMethodException {
		try {
			AcLog.WARNING("Method: Recursion Lookup: "+aMethodName);
			Method k = clazz.getDeclaredMethod(aMethodName);
			ReflectionUtils.makeMethodAccessible(k);
			return k;
		} catch (final NoSuchMethodException e) {
			final Class<?> superClass = clazz.getSuperclass();
			if (superClass == null || superClass == Object.class) {
				throw e;
			}
			return getMethod_Internal(superClass, aMethodName);
		}
	}	
	
	private void dumpClassInfo(Class aClass) {
		AcLog.WARNING("We ran into an error processing reflection in "+aClass.getName()+", dumping all data for debugging.");	
		// Get the methods
	    Method[] methods = aClass.getDeclaredMethods();
	    Field[] fields = aClass.getDeclaredFields();
	    Constructor[] consts = aClass.getDeclaredConstructors(); 

		AcLog.WARNING("Dumping all Methods.");	
	    for (Method method : methods) {
		      System.out.println(method.getName()+" | "+StringUtils.getDataStringFromArray(method.getParameterTypes()));
		    }
		AcLog.WARNING("Dumping all Fields.");	
	    for (Field f : fields) {
		      System.out.println(f.getName());
		    }
		AcLog.WARNING("Dumping all Constructors.");	
	    for (Constructor c : consts) {
		      System.out.println(c.getName()+" | "+c.getParameterCount()+" | "+StringUtils.getDataStringFromArray(c.getParameterTypes()));
		    }
	}

	private Class<?> getNonPublicClass(final String className) {
		Class<?> c = null;
		c = getClass(className);
		if (c != null) {
			// In our case we need to use
			Constructor<?> constructor = null;
			try {
				constructor = c.getDeclaredConstructor();
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			// note: getConstructor() can return only public constructors
			// so we needed to search for any Declared constructor

			// now we need to make this constructor accessible
			if (null != constructor) {
				constructor.setAccessible(true);// ABRACADABRA!

				try {
					final Object o = constructor.newInstance();
					if (o != null) {
						return (Class<?>) o;
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private Class<?> getClass_Internal(String string) {
		if (ReflectionUtils.doesClassExist(string)) {
			try {
				return Class.forName(string);
			}
			catch (ClassNotFoundException e) {
				return getNonPublicClass(string);
			}
		}
		return null;
	}
	
	
	
	
}
