/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.impl.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * POJO Data Set from a customer class which has a default construct and
 * "next()" method and optionally has "open(Object appContext)" and "close()"
 * method
 */
public class PojoDataSetFromCustomClass implements IPojoDataSet {

	private static Logger logger = Logger.getLogger(PojoDataSetFromCustomClass.class.getName());
	@SuppressWarnings("unchecked")
	private Class dataSetClass;
	private Object instance; // POJO Data Set Class instance
	private Method openMethod; // optional
	private Method nextMethod; // must
	private Method closeMethod; // optional

	@SuppressWarnings("unchecked")
	public PojoDataSetFromCustomClass(Class dataSetClass) throws OdaException {
		assert dataSetClass != null;
		this.dataSetClass = dataSetClass;
		try {
			openMethod = this.dataSetClass.getMethod(Constants.OPEN_METHOD_NAME,
					new Class[] { Object.class, Map.class });
		} catch (SecurityException e) {
			logger.log(Level.WARNING, "failed to locate open(Object appContext) method ", //$NON-NLS-1$
					e);
		} catch (NoSuchMethodException e) {
			logger.log(Level.WARNING, "failed to locate open(Object appContext) method ", //$NON-NLS-1$
					e);
		}

		try {
			nextMethod = this.dataSetClass.getMethod(Constants.NEXT_METHOD_NAME, (Class[]) null);
			if (nextMethod.getReturnType().isPrimitive()) {
				throw new OdaException(Messages.getString("PojoDataSetFromCustomerClass.WrongReturnTypeForNextMethod")); //$NON-NLS-1$
			}
		} catch (SecurityException e) {
			throw new OdaException(e);
		} catch (NoSuchMethodException e) {
			throw new OdaException(e);
		}

		try {
			closeMethod = this.dataSetClass.getMethod(Constants.CLOSE_METHOD_NAME, (Class[]) null);
		} catch (SecurityException e) {
			logger.log(Level.WARNING, "failed to locate close( ) method ", e); //$NON-NLS-1$
		} catch (NoSuchMethodException e) {
			logger.log(Level.WARNING, "failed to locate close( ) method ", e); //$NON-NLS-1$
		}

		try {
			instance = dataSetClass.newInstance();
		} catch (InstantiationException e) {
			throw new OdaException(e);
		} catch (IllegalAccessException e) {
			throw new OdaException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#close()
	 */
	public void close() throws OdaException {
		if (closeMethod != null) {
			try {
				closeMethod.invoke(instance);
			} catch (IllegalArgumentException e) {
				throw new OdaException(e);
			} catch (IllegalAccessException e) {
				throw new OdaException(e);
			} catch (InvocationTargetException e) {
				throw new OdaException(e);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#next()
	 */
	public Object next() throws OdaException {
		try {
			return nextMethod.invoke(instance);
		} catch (IllegalArgumentException e) {
			throw new OdaException(e);
		} catch (IllegalAccessException e) {
			throw new OdaException(e);
		} catch (InvocationTargetException e) {
			throw new OdaException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.api.IPojoDataSet#open(java.lang.Object,Map<
	 * String, Object>)
	 */
	public void open(Object appContext, Map<String, Object> dataSetParamValues) throws OdaException {
		if (openMethod != null) {
			try {
				openMethod.invoke(instance, new Object[] { appContext, dataSetParamValues });
			} catch (IllegalArgumentException e) {
				throw new OdaException(e);
			} catch (IllegalAccessException e) {
				throw new OdaException(e);
			} catch (InvocationTargetException e) {
				throw new OdaException(e);
			}
		}
	}
}
