/*
 *************************************************************************
 * Copyright (c) 2009, 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.impl.ProviderUtil;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.connectivity.oda.spec.BaseQuery;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification.ParameterIdentifier;
import org.eclipse.datatools.connectivity.oda.spec.ValidationContext;
import org.eclipse.datatools.connectivity.oda.spec.ValueExpression;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ExtensionContributor;
import org.eclipse.datatools.connectivity.oda.spec.manifest.ResultExtensionExplorer;
import org.eclipse.datatools.connectivity.oda.spec.result.AggregateExpression;
import org.eclipse.datatools.connectivity.oda.spec.result.FilterExpression;
import org.eclipse.datatools.connectivity.oda.spec.result.ResultProjection;
import org.eclipse.datatools.connectivity.oda.spec.result.ResultSetSpecification;
import org.eclipse.datatools.connectivity.oda.spec.result.SortSpecification;
import org.eclipse.datatools.connectivity.oda.spec.util.QuerySpecificationHelper;
import org.eclipse.datatools.connectivity.oda.spec.util.ValidatorUtil;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;

/**
 * Internal helper class to locate the appropriate ODA QuerySpecification
 * factory and to provide general utilities to edit and access the content of a
 * query spec.
 */
public class QuerySpecHelper {
	QuerySpecificationHelper m_specFactoryHelper;

	/**
	 * Constructor for a specialized helper of the specified data source and data
	 * set types.
	 * 
	 * @param odaDataSourceId
	 * @param odaDataSetId
	 */
	public QuerySpecHelper(String odaDataSourceId, String odaDataSetId) {
		ExtensionContributor[] contributors = null;
		try {
			contributors = ResultExtensionExplorer.getInstance().getContributorsOfDataSet(odaDataSourceId,
					odaDataSetId);
		} catch (IllegalArgumentException ex) {
			// ignore and use default factory helper
		} catch (OdaException ex) {
			// ignore and use default factory helper
		}

		ExtensionContributor resultSetContributor = null;
		if (contributors != null && contributors.length > 0)
			resultSetContributor = contributors[0]; // use the first one found
		m_specFactoryHelper = new QuerySpecificationHelper(resultSetContributor);
	}

	/**
	 * Constructor for a specialized helper of the specified ODA dynamicResultSet
	 * extension.
	 * 
	 * @param dynamicResultSetExtnId may be null
	 */
	public QuerySpecHelper(String dynamicResultSetExtnId) {
		m_specFactoryHelper = new QuerySpecificationHelper(dynamicResultSetExtnId);
	}

	/**
	 * Gets the specialized factory helper for creating query specification
	 * instances.
	 * 
	 * @return
	 */
	public QuerySpecificationHelper getFactoryHelper() {
		return m_specFactoryHelper;
	}

	/**
	 * Sets the specified input ParameterHint and corresponding value in the
	 * specified QuerySpecification.
	 * 
	 * @param querySpec  a QuerySpecification to which the input parameter value is
	 *                   set
	 * @param paramHint  an input QuerySpecification; must contain either the native
	 *                   parameter name and/or position
	 * @param inputValue input parameter value
	 * @throws DataException if specified ParameterHint is invalid
	 */
	public static void setParameterValue(QuerySpecification querySpec, ParameterHint paramHint, Object inputValue)
			throws DataException {
		if (querySpec == null || paramHint == null)
			return; // nothing to set

		boolean hasNativeName = PreparedStatement.hasValue(paramHint.getNativeName());
		boolean hasParamPos = (paramHint.getPosition() > 0);
		if (!paramHint.isInputMode() || !(hasNativeName || hasParamPos)) {
			String errorCode = paramHint.isInputMode() ? ResourceConstants.PARAMETER_NAME_CANNOT_BE_EMPTY_OR_NULL
					: ResourceConstants.CANNOT_FIND_IN_PARAMETER;
			Object errMsgArg = paramHint.isInputMode() ? null
					: (hasNativeName ? paramHint.getNativeName() : Integer.valueOf(paramHint.getPosition()));
			throw ExceptionHandler.newException(errorCode, errMsgArg, new IllegalArgumentException());
		}

		ParameterIdentifier paramIdentifier = null;
		if (hasNativeName) {
			paramIdentifier = hasParamPos
					? querySpec.new ParameterIdentifier(paramHint.getNativeName(), paramHint.getPosition())
					: querySpec.new ParameterIdentifier(paramHint.getNativeName());
		} else
			paramIdentifier = querySpec.new ParameterIdentifier(paramHint.getPosition());

		querySpec.setParameterValue(paramIdentifier, inputValue);
	}

	/**
	 * Updates the specified validation context with the effective connection
	 * context for online validation of a {@link QuerySpecification}.
	 * 
	 * @param validationContext the validation context to be updated with a
	 *                          connection context for online validation of a query
	 *                          specification
	 * @param connProperties    data source connection properties
	 * @param appContext        an application context provided by an ODA consumer
	 *                          application; it may contain externalized connection
	 *                          properties info, which would override those of
	 *                          connProperties if exists; may be null
	 * @throws DataException
	 * @since 2.5.2
	 */
	public static void setValidationConnectionContext(ValidationContext validationContext, Properties connProperties,
			Map<?, ?> appContext) throws DataException {
		if (validationContext == null)
			throw new IllegalArgumentException("ValidationContext"); //$NON-NLS-1$

		// gets the effective connection properties to set in the validation context
		Properties effectiveProps = getEffectiveProperties(connProperties, appContext);

		// if a set of effective properties was provided successfully,
		// update the profile store file path property with the
		// full path of the resolved profile store file, if referenced;
		// so the QuerySpecification can use the effective properties directly, and
		// no need to resolve the profile store file path again
		if (effectiveProps != null && !effectiveProps.equals(connProperties)) {
			String profileStoreResolvedPath = effectiveProps
					.getProperty(ConnectionProfileProperty.TRANSIENT_PROFILE_STORE_RESOLVED_PATH_PROP_KEY);
			if (profileStoreResolvedPath != null && profileStoreResolvedPath.length() > 0) {
				effectiveProps.setProperty(ConnectionProfileProperty.PROFILE_STORE_FILE_PATH_PROP_KEY,
						profileStoreResolvedPath);
			}
		}

		if (validationContext.getConnection() != null)
			validationContext.getConnection().setProperties(effectiveProps);
		else
			validationContext.setConnection(validationContext.new Connection(effectiveProps));
	}

	private static Properties getEffectiveProperties(Properties connProperties, Map<?, ?> appContext)
			throws DataException {
		// use a consumer profile provider service to get the appropriate connection
		// properties to apply
		appContext = ConnectionManager.addProfileProviderService(appContext);
		try {
			return ProviderUtil.getEffectiveProperties(connProperties, appContext);
		} catch (OdaException ex) {
			throw ExceptionHandler.newException(ResourceConstants.CANNOT_OPEN_CONNECTION, ex);
		}
	}

	/**
	 * Creates a transient connection profile instance that contains the specified
	 * connection properties info.
	 * 
	 * @param odaDataSourceId an ODA data source id, as specified in an
	 *                        oda.dataSource extension, for use as the connection
	 *                        profile id
	 * @param connProperties  data source connection properties
	 * @param appContext      an application context provided by an ODA consumer
	 *                        application; it may contain externalized connection
	 *                        properties info, which would override those of
	 *                        connProperties if exists; may be null
	 * @return a new instance of transient connection profile; it is the
	 *         responsibility of the client to manage its connection state to avoid
	 *         having a live connection remain open
	 * @throws OdaException
	 */
	@SuppressWarnings("unused")
	private static IConnectionProfile createTransientProfile(String odaDataSourceId, Properties connProperties,
			Map<?, ?> appContext) throws DataException {
		Properties effectiveProps = getEffectiveProperties(connProperties, appContext);

		// creates a transient connection profile instance based on specified connection
		// info
		IConnectionProfile connProfile;
		try {
			connProfile = OdaProfileExplorer.getInstance().createTransientProfile(odaDataSourceId, effectiveProps);
		} catch (OdaException ex) {
			throw ExceptionHandler.newException(ResourceConstants.CANNOT_OPEN_CONNECTION, ex);
		}
		return connProfile;
	}

	/**
	 * Indicates whether the specified result set specification is one of the
	 * cause(s) of the specified exception caught while preparing or executing an
	 * ODA query.
	 * 
	 * @param resultSetSpec a result set specification whose processing might have
	 *                      caused an exception
	 * @param dataEx        the exception caught while preparing or executing an ODA
	 *                      query
	 * @return true if the specified result set specification is one of the cause(s)
	 *         in the exception; false otherwise
	 */
	public static boolean isInvalidResultSetSpec(ResultSetSpecification resultSetSpec, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidResultSetSpec(resultSetSpec, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified FilterExpression is identified as one of the
	 * cause(s) of the specified exception caught while preparing or executing an
	 * ODA query.
	 * 
	 * @param filterExpr a filter expression whose processing might have caused an
	 *                   exception
	 * @param dataEx     the exception caught while preparing or executing an ODA
	 *                   query
	 * @return true if the specified FilterExpression is one of the cause(s) in the
	 *         exception; false otherwise
	 */
	public static boolean isInvalidFilterExpression(FilterExpression filterExpr, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidFilterExpression(filterExpr, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified sort specification is one of the cause(s) of
	 * the specified exception caught while preparing or executing an ODA query.
	 * 
	 * @param sortSpec a sort specification whose processing might have caused an
	 *                 exception
	 * @param dataEx   the exception caught while preparing or executing an ODA
	 *                 query
	 * @return true if the specified sort specification is one of the cause(s) in
	 *         the exception; false otherwise
	 * @see {@link #isInvalidSortKey(int, DataException)}
	 */
	public static boolean isInvalidSortSpec(SortSpecification sortSpec, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidSortSpec(sortSpec, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified sort key is one of the cause(s) of the
	 * specified exception caught while preparing or executing an ODA query.
	 * 
	 * @param sortKeySequenceOrder the sequence ordering position (1-based) of a
	 *                             sort key whose processing might have caused an
	 *                             exception
	 * @param dataEx               the exception caught while preparing or executing
	 *                             an ODA query
	 * @return true if the specified sort key is one of the cause(s) in the
	 *         exception; false otherwise
	 * @see {@link #isInvalidSortSpec(SortSpecification, DataException)}
	 */
	public static boolean isInvalidSortKey(int sortKeySequenceOrder, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidSortKey(sortKeySequenceOrder, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified result projection is one of the cause(s) of
	 * the specified exception caught while preparing or executing an ODA query.
	 * 
	 * @param resultProj a result projection whose processing might have caused an
	 *                   exception
	 * @param dataEx     the exception caught while preparing or executing an ODA
	 *                   query
	 * @return true if the specified result projection is one of the cause(s) in the
	 *         exception; false otherwise
	 * @see {@link #isInvalidAggregateExpression(AggregateExpression, DataException)}
	 */
	public static boolean isInvalidResultProjection(ResultProjection resultProj, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidResultProjection(resultProj, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified aggregate expression is one of the cause(s)
	 * of the specified exception caught while preparing or executing an ODA query.
	 * 
	 * @param aggrExpr an aggregate expression whose processing might have caused an
	 *                 exception
	 * @param dataEx   the exception caught while preparing or executing an ODA
	 *                 query
	 * @return true if the specified aggregate expression is one of the cause(s) in
	 *         the exception; false otherwise
	 * @see {@link #isInvalidResultProjection(ResultProjection, DataException)}
	 */
	public static boolean isInvalidAggregateExpression(AggregateExpression aggrExpr, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidAggregateExpression(aggrExpr, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified value expression is one of the cause(s) of
	 * the specified exception caught while preparing or executing an ODA query.
	 * 
	 * @param valueExpr a value expression whose processing might have caused an
	 *                  exception
	 * @param dataEx    the exception caught while preparing or executing an ODA
	 *                  query
	 * @return true if the specified value expression is one of the cause(s) in the
	 *         exception; false otherwise
	 */
	public static boolean isInvalidValueExpression(ValueExpression valueExpr, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidValueExpression(valueExpr, (OdaException) dataEx.getCause());
	}

	/**
	 * Indicates whether the specified BaseQuery is identified as one of the
	 * cause(s) of the specified exception caught while preparing or executing an
	 * ODA query.
	 * 
	 * @param filterExpr a {@link BaseQuery} whose processing might have caused an
	 *                   exception
	 * @param dataEx     the exception caught while preparing or executing an ODA
	 *                   query
	 * @return true if the specified BaseQuery is one of the cause(s) in the
	 *         exception; false otherwise
	 */
	public static boolean isInvalidBaseQuery(BaseQuery baseQuery, DataException dataEx) {
		if (!hasOdaException(dataEx))
			return false;

		return ValidatorUtil.isInvalidBaseQuery(baseQuery, (OdaException) dataEx.getCause());
	}

	private static boolean hasOdaException(DataException dataEx) {
		if (dataEx == null)
			return false;
		return dataEx.getCause() instanceof OdaException;
	}

}
