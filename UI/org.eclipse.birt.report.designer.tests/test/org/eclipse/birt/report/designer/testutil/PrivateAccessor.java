/*
 * The JUnit-addons Software License, Version 1.0
 *     (based on the Apache Software License, Version 1.1)
 *
 * Copyright (c) 2002-2003 Vladimir R. Bossicard.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by Vladimir R.
 *        Bossicard as well as other contributors
 *        (http://junit-addons.sourceforge.net/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The name "JUnit-addons" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact
 *    vbossica@users.sourceforge.net.
 *
 * 5. Products derived from this software may not be called "JUnit-addons"
 *    nor may "JUnit-addons" appear in their names without prior written
 *    permission of the project managers.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ======================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals.  For more information on the JUnit-addons Project, please
 * see <http://junit-addons.sourceforge.net/>.
 */

package org.eclipse.birt.report.designer.testutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class to bypass the Java modifiers security and access protected and
 * private fields and methods.
 * 
 * <h4>Note</h4>
 * 
 * When specifying parameter types, use <code>int.class</>,
 * <code>double.class</>, <code>short.class</>, <code>long.class</>,
 * <code>float.class</> and <code>char.class</> for primitive types.
 * <p>
 *
 * Example: To set the value of an object <code>obj</> to 100 via the method
 * <code>setValue()</>:
 * <pre>
 * PrivateAccessor.invoke( obj, &quot;setValue&quot;, new Class[]{ int.class },
 * new Object[]{ new Integer( 100 ) } );
 * </pre>
 *
 * @version $Revision: #2 $ $Date: 2005/01/27 $
 * @author <a href="mailto:vbossica@users.sourceforge.net">Vladimir R.
 *         Bossicard</a>
 */
public class PrivateAccessor {

	private PrivateAccessor() {
	}

	/**
	 * Returns the value of the field on the specified object. The name parameter is
	 * a <code>String</code> specifying the simple name of the desired field.
	 * <p>
	 * 
	 * The object is first searched for any matching field. If no matching field is
	 * found, the superclasses are recursively searched.
	 * 
	 * @exception NoSuchFieldException if a field with the specified name is not
	 *                                 found.
	 */
	public static Object getField(Object object, String name) throws NoSuchFieldException {
		if (object == null) {
			throw new IllegalArgumentException("Invalid null object argument");
		}
		for (Class cls = object.getClass(); cls != null; cls = cls.getSuperclass()) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				return field.get(object);
			} catch (Exception ex) {
				/*
				 * in case of an exception, we will throw a new NoSuchFieldException object
				 */
				;
			}
		}
		throw new NoSuchFieldException("Could get value for field " + object.getClass().getName() + "." + name);
	}

	/**
	 * Returns the value of the field on the specified class. The name parameter is
	 * a <code>String</code> specifying the simple name of the desired field.
	 * <p>
	 * 
	 * The class is first searched for any matching field. If no matching field is
	 * found, the superclasses are recursively searched.
	 * 
	 * @exception NoSuchFieldException if a field with the specified name is not
	 *                                 found.
	 */
	public static Object getField(Class cls, String name) throws NoSuchFieldException {
		if (cls == null) {
			throw new IllegalArgumentException("Invalid null cls argument");
		}
		Class base = cls;
		while (base != null) {
			try {
				Field field = base.getDeclaredField(name);
				field.setAccessible(true);
				return field.get(base);
			} catch (Exception ex) {
				/*
				 * in case of an exception, we will throw a new NoSuchFieldException object
				 */
				;
			}
			base = base.getSuperclass();
		}
		throw new NoSuchFieldException("Could get value for static field " + cls.getName() + "." + name);
	}

	/**
	 * Sets the field represented by the name value on the specified object argument
	 * to the specified new value. The new value is automatically unwrapped if the
	 * underlying field has a primitive type.
	 * <p>
	 * 
	 * The object is first searched for any matching field. If no matching field is
	 * found, the superclasses are recursively searched.
	 * 
	 * @exception NoSuchFieldException if a field with the specified name is not
	 *                                 found.
	 */
	public static void setField(Object object, String name, Object value) throws NoSuchFieldException {
		if (object == null) {
			throw new IllegalArgumentException("Invalid null object argument");
		}
		for (Class cls = object.getClass(); cls != null; cls = cls.getSuperclass()) {
			try {
				Field field = cls.getDeclaredField(name);
				field.setAccessible(true);
				field.set(object, value);
				return;
			} catch (Exception ex) {
				/*
				 * in case of an exception, we will throw a new NoSuchFieldException object
				 */
				;
			}
		}
		throw new NoSuchFieldException("Could set value for field " + object.getClass().getName() + "." + name);
	}

	/**
	 * Sets the field represented by the name value on the specified class argument
	 * to the specified new value. The new value is automatically unwrapped if the
	 * underlying field has a primitive type.
	 * <p>
	 * 
	 * The class is first searched for any matching field. If no matching field is
	 * found, the superclasses are recursively searched.
	 * 
	 * @exception NoSuchFieldException if a field with the specified name is not
	 *                                 found.
	 */
	public static void setField(Class cls, String name, Object value) throws NoSuchFieldException {
		if (cls == null) {
			throw new IllegalArgumentException("Invalid null cls argument");
		}
		Class base = cls;
		while (base != null) {
			try {
				Field field = base.getDeclaredField(name);
				field.setAccessible(true);
				field.set(base, value);
				return;
			} catch (Exception ex) {
				/*
				 * in case of an exception, we will throw a new NoSuchFieldException object
				 */
				;
			}
			base = base.getSuperclass();
		}
		throw new NoSuchFieldException("Could set value for static field " + cls.getName() + "." + name);
	}

	/**
	 * Invokes the method represented by the name value on the specified object with
	 * the specified parameters. Individual parameters are automatically unwrapped
	 * to match primitive formal parameters, and both primitive and reference
	 * parameters are subject to widening conversions as necessary. The value
	 * returned by the method is automatically wrapped in an object if it has a
	 * primitive type.
	 * <p>
	 * 
	 * The object is first searched for any matching method. If no matching method
	 * is found, the superclasses are recursively searched.
	 * 
	 * @exception NoSuchMethodException if a matching method is not found or if the
	 *                                  name is " <init>"or " <clinit>".
	 */
	public static Object invoke(Object object, String name, Class parameterTypes[], Object args[]) throws Throwable {
		if (object == null) {
			throw new IllegalArgumentException("Invalid null object argument");
		}
		Class cls = object.getClass();
		while (cls != null) {
			try {
				Method method = cls.getDeclaredMethod(name, parameterTypes);
				method.setAccessible(true);
				return method.invoke(object, args);
			} catch (InvocationTargetException e) {
				/*
				 * if the method throws an exception, it is embedded into an
				 * InvocationTargetException.
				 */
				throw e.getTargetException();
			} catch (Exception ex) {
				/*
				 * in case of an exception, we will throw a new NoSuchFieldException object
				 */
				;
			}
			cls = cls.getSuperclass();
		}
		throw new NoSuchMethodException("Failed method invocation: " + object.getClass().getName() + "." + name + "()");
	}

	/**
	 * Invokes the method represented by the name value on the specified class with
	 * the specified parameters. Individual parameters are automatically unwrapped
	 * to match primitive formal parameters, and both primitive and reference
	 * parameters are subject to widening conversions as necessary. The value
	 * returned by the method is automatically wrapped in an object if it has a
	 * primitive type.
	 * <p>
	 * 
	 * The class is first searched for any matching method. If no matching class is
	 * found, the superclasses are recursively searched.
	 * 
	 * @exception NoSuchMethodException if a matching method is not found or if the
	 *                                  name is " <init>"or " <clinit>".
	 */
	public static Object invoke(Class cls, String name, Class parameterTypes[], Object args[]) throws Throwable {
		if (cls == null) {
			throw new IllegalArgumentException("Invalid null cls argument");
		}
		Class base = cls;
		while (base != null) {
			try {
				Method method = base.getDeclaredMethod(name, parameterTypes);
				method.setAccessible(true);
				return method.invoke(base, args);
			} catch (InvocationTargetException e) {
				/*
				 * if the method throws an exception, it is embedded into an
				 * InvocationTargetException.
				 */
				throw (Exception) e.getTargetException();
			} catch (Exception ex) {
				/*
				 * in case of an exception, we will throw a new NoSuchFieldException object
				 */
				;
			}
			base = base.getSuperclass();
		}
		throw new NoSuchMethodException("Failed static method invocation: " + cls.getName() + "." + name + "()");
	}

	/**
	 * Invoke the constructor with no parameters to construct an new instance of the
	 * given class
	 * 
	 * @param cls the Class object
	 * @throws Exception
	 */
	public static Object newInstance(Class cls) throws Exception {
		return newInstance(cls, new Class[0], new Object[0]);
	}

	/**
	 * Invoke the constructor with specified parameters to construct an new instance
	 * of the given class
	 * 
	 * @param cls   the Class object
	 * @param types the types of the parameters
	 * @param args  the values of the parameters
	 * @throws Exception
	 */
	public static Object newInstance(Class cls, Class[] types, Object[] args) throws Exception {
		if (cls == null) {
			throw new IllegalArgumentException("Invalid null object argument");
		}
		Constructor constructor = cls.getDeclaredConstructor(types);
		constructor.setAccessible(true);
		return constructor.newInstance(args);
	}

}
