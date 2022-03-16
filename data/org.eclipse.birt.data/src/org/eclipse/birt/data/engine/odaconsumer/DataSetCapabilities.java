/*
 *****************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Information about the data set capabilities.
 */
public class DataSetCapabilities extends ExceptionHandler {
	private IDataSetMetaData m_dsMetaData;

	// trace logging variable
	private static String sm_className = DataSetCapabilities.class.getName();

	DataSetCapabilities(IDataSetMetaData dsMetaData) {
		super(sm_className);
		final String methodName = "DataSetCapabilities"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, dsMetaData);

		assert (dsMetaData != null);
		m_dsMetaData = dsMetaData;

		getLogger().exiting(sm_className, methodName, this);
	}

	/**
	 * Returns the mode supported by this data set for dynamic sorting
	 * functionality.
	 *
	 * @return the dynamic sorting mode supported by the data set.
	 */
	public int getSortMode() {
		return m_dsMetaData.getSortMode();
	}

	/**
	 * Returns whether a <code>Statement</code> can simultaneously get multiple
	 * result sets.
	 *
	 * @return true if a <code>Statement</code> can get multiple result sets
	 *         simultaneously; false otherwise.
	 * @throws DataException if data source error occurs.
	 */
	public boolean supportsMultipleOpenResults() throws DataException {
		final String methodName = "supportsMultipleOpenResults"; //$NON-NLS-1$

		try {
			return m_dsMetaData.supportsMultipleOpenResults();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_MULTIPLE_OPEN_RESULTS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName,
					"Cannot determine support of multiple open result sets.  Default to false.", ex); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * Returns whether a <code>Statement</code> can support getting multiple result
	 * sets.
	 *
	 * @return true if a <code>Statement</code> can support getting multiple result
	 *         sets; false otherwise.
	 * @throws DataException if data source error occurs.
	 */
	public boolean supportsMultipleResultSets() throws DataException {
		final String methodName = "supportsMultipleResultSets"; //$NON-NLS-1$
		try {
			return m_dsMetaData.supportsMultipleResultSets();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_MULTIPLE_RESULT_SETS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName,
					"Cannot determine support of multiple result sets.  Default to false.", ex); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * Returns whether a <code>Statement</code> can support named result sets.
	 *
	 * @return true if a <code>Statement</code> can support named result sets; false
	 *         otherwise.
	 * @throws DataException if data source error occurs.
	 */
	public boolean supportsNamedResultSets() throws DataException {
		final String methodName = "supportsNamedResultSets"; //$NON-NLS-1$
		try {
			return m_dsMetaData.supportsNamedResultSets();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_NAMED_RESULT_SETS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName,
					"Cannot determine support of named result sets.  Default to false.", ex); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * Returns whether a <code>Statement</code> can support named parameters.
	 *
	 * @return true if a <code>Statement</code> can support named parameters; false
	 *         otherwise.
	 * @throws DataException if data source error occurs.
	 */
	public boolean supportsNamedParameters() throws DataException {
		final String methodName = "supportsNamedParameters"; //$NON-NLS-1$
		try {
			return m_dsMetaData.supportsNamedParameters();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_NAMED_PARAMETERS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName,
					"Cannot determine support of named parameters. Default to false.", ex); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * Returns whether a <code>Statement</code> can support input parameters.
	 *
	 * @return true if a <code>Statement</code> can support input parameters; false
	 *         otherwise.
	 * @throws DataException if data source error occurs.
	 */
	public boolean supportsInParameters() throws DataException {
		final String methodName = "supportsInParameters"; //$NON-NLS-1$
		try {
			return m_dsMetaData.supportsInParameters();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_IN_PARAMETERS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName,
					"Cannot determine support of input parameters. Default to false.", ex); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * Returns whether a <code>Statement</code> can support output parameters.
	 *
	 * @return true if a <code>Statement</code> can support output parameters; false
	 *         otherwise.
	 * @throws DataException if data source error occurs.
	 */
	public boolean supportsOutParameters() throws DataException {
		final String methodName = "supportsOutParameters"; //$NON-NLS-1$
		try {
			return m_dsMetaData.supportsOutParameters();
		} catch (OdaException ex) {
			throwException(ex, ResourceConstants.CANNOT_DETERMINE_SUPPORT_FOR_OUT_PARAMETERS, methodName);
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName,
					"Cannot determine support of output parameters. Default to false.", ex); //$NON-NLS-1$
		}
		return false;
	}

}
