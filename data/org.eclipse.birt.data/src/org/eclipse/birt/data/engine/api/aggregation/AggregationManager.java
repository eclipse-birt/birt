/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.api.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.api.aggregation.AggrFunctionWrapper.ParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 *
 */

public class AggregationManager {

	private static final String ELEMENT_AGGREGATION_FACTORY = "AggregationFactory";//$NON-NLS-1$
	private static final String ATTRIBUTE_AGGREGATION_CLASS = "aggregationClass";//$NON-NLS-1$
	private static final String EXTENSION_POINT = "org.eclipse.birt.data.aggregation";//$NON-NLS-1$
	private static final String ELEMENT_AGGREGATIONS = "Aggregations";//$NON-NLS-1$
	private static final String ELEMENT_AGGREGATION = "Aggregation";//$NON-NLS-1$

	private static final String ELEMENT_UIINFO = "UIInfo";//$NON-NLS-1$
	private static final String ATTRIBUTE_PARAMETER_META_INFO = "parameterMetaInfo";//$NON-NLS-1$
	private static final String ATTRIBUTE_TEXT_DATA = "textData";//$NON-NLS-1$

	private static AggregationManager instance;
	public static Map aggrMap;

	// log instance
	private static Logger logger = Logger.getLogger(AggregationManager.class.getName());

	/**
	 * allowed aggregation function names in x-tab
	 */
	private static String[] xTabAggrNames = { "SUM", "AVE", "MAX", "MIN", "FIRST", "LAST", "COUNT", "COUNTDISTINCT",
			"MEDIAN", "MODE", "STDDEV", "VARIANCE", "RANGE",
			// "RANK",
			// "RUNNINGSUM"
	};

	public static final int AGGR_TABULAR = 0;
	public static final int AGGR_XTAB = 1;
	public static final int AGGR_MEASURE = 2;

	/**
	 * allowed aggregation function names in cube measure.
	 */
	private static String[] measureAggrNames = { "SUM", //$NON-NLS-1$
			"MAX", //$NON-NLS-1$
			"MIN", //$NON-NLS-1$
			"FIRST", //$NON-NLS-1$
			"LAST", //$NON-NLS-1$
			"COUNT" //$NON-NLS-1$
//			"RANGE", //$NON-NLS-1$
//			"COUNTDISTINCT"//$NON-NLS-1$	// Temporarily remove count distinct aggregation function.
	};

	private static List allAggrNames = new ArrayList();

	/**
	 * Return a shared instance of AggregationManager.
	 *
	 * @return
	 * @throws DataException
	 */
	public static AggregationManager getInstance() throws DataException {
		if (instance == null) {
			synchronized (AggregationManager.class) {
				if (instance == null) {
					aggrMap = new HashMap();
					instance = new AggregationManager();
				}
			}
		}

		return instance;
	}

	/**
	 *
	 */
	private AggregationManager() throws DataException {
		populateAggregations();
	}

	/**
	 *
	 * @throws DataException
	 */
	private void populateAggregations() throws DataException {
		IExtensionRegistry extReg = Platform.getExtensionRegistry();
		IExtensionPoint extPoint = extReg.getExtensionPoint(EXTENSION_POINT);

		if (extPoint == null) {
			return;
		}

		IExtension[] exts = extPoint.getExtensions();
		if (exts == null) {
			return;
		}

		for (int e = 0; e < exts.length; e++) {
			IConfigurationElement[] configElems = exts[e].getConfigurationElements();
			if (configElems == null) {
				continue;
			}

			for (int i = 0; i < configElems.length; i++) {
				if (configElems[i].getName().equals(ELEMENT_AGGREGATIONS)) {
					IConfigurationElement[] subElems = configElems[i].getChildren(ELEMENT_AGGREGATION_FACTORY);
					populateFactoryAggregations(subElems);
					subElems = configElems[i].getChildren(ELEMENT_AGGREGATION);
					populateDeprecatedAggregations(subElems);
				}
			}
		}

	}

	/**
	 *
	 * @param subElems
	 * @throws DataException
	 */
	private void populateFactoryAggregations(IConfigurationElement[] subElems) throws DataException {
		if (subElems == null) {
			return;
		}
		for (int j = 0; j < subElems.length; j++) {
			try {
				IAggregationFactory factory = (IAggregationFactory) subElems[j].createExecutableExtension("class");
				List functions = factory.getAggregations();
				for (Iterator itr = functions.iterator(); itr.hasNext();) {
					IAggrFunction aggrFunc = (IAggrFunction) itr.next();
					String name = aggrFunc.getName().toUpperCase();
					if (aggrMap.put(name, aggrFunc) != null) {
						throw new DataException(ResourceConstants.DUPLICATE_AGGREGATION_NAME, name);
					}
					allAggrNames.add(name);
				}
			} catch (FrameworkException exception) {
				// TODO: log this exception or provide public
				// interface for the user to get uninstantiated
				// function names
			}
		}

	}

	/**
	 *
	 * @param subElems
	 * @throws DataException
	 */
	private void populateDeprecatedAggregations(IConfigurationElement[] subElems) throws DataException {
		if (subElems == null) {
			return;
		}
		for (int j = 0; j < subElems.length; j++) {
			try {
				IAggregation aggrFunc = (IAggregation) subElems[j]
						.createExecutableExtension(ATTRIBUTE_AGGREGATION_CLASS);
				String name = aggrFunc.getName().toUpperCase();

				AggrFunctionWrapper aggrWrapper = new AggrFunctionWrapper(aggrFunc);
				populateExtendedAggrInfo(name, aggrFunc, subElems[j], aggrWrapper);

				if (aggrMap.put(name, aggrWrapper) != null) {
					throw new DataException(ResourceConstants.DUPLICATE_AGGREGATION_NAME, name);
				}
				allAggrNames.add(name);
			} catch (Exception e) {
				logger.logp(Level.WARNING, AggrFunctionWrapper.class.getName(), "populateDeprecatedAggregations",
						"Exception in aggregation extension loading.", e);
			}
		}
	}

	/**
	 * populate the extended extensions information.
	 *
	 * @param name
	 * @param aggrFunc
	 * @param elem
	 * @param aggrWrapper
	 */
	private void populateExtendedAggrInfo(String name, IAggregation aggrFunc, IConfigurationElement elem,
			AggrFunctionWrapper aggrWrapper) throws DataException {
		IConfigurationElement[] uiInfo = elem.getChildren(ELEMENT_UIINFO);
		assert (uiInfo != null && uiInfo.length == 1);
		String paramInfo = uiInfo[0].getAttribute(ATTRIBUTE_PARAMETER_META_INFO);
		String textInfo = uiInfo[0].getAttribute(ATTRIBUTE_TEXT_DATA);
		aggrWrapper.setDisplayName(textInfo);
		// populate parameters to the aggrWrapper
		List paramList = new ArrayList();
		String[] paramInfos = paramInfo.split(",");//$NON-NLS-1$
		boolean[] paramFlags = aggrFunc.getParameterDefn();
		if (paramInfos != null && paramInfos.length > 0 && paramFlags != null) {
			if (paramInfos.length != paramFlags.length) {
				throw new DataException(ResourceConstants.INCONSISTENT_AGGREGATION_ARGUMENT_DEFINITION);
			}
			// populateDataFiledParameterDefn( paramList );
			for (int k = 0; k < paramInfos.length; k++) {
				final String s = paramInfos[k].trim();
				int index = s.indexOf(' ');
				String paramName = null;
				if (index > 0) {
					paramName = s.substring(index + 1).trim();
				} else {
					paramName = paramInfos[k];
				}

				ParameterDefn paramDefn = new ParameterDefn(paramName, paramName, !paramFlags[k], true);
				paramList.add(paramDefn);
			}
		}
		IParameterDefn[] params = new IParameterDefn[paramList.size()];
		paramList.toArray(params);
		aggrWrapper.setParameterDefn(params);
	}

	/**
	 * Destroy shared instance of AggregationManager.
	 *
	 */
	public static void destroyInstance() {
		instance = null;
		aggrMap = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregationManager#
	 * getAggrFunction(java.lang.String)
	 */
	public IAggrFunction getAggregation(String name) {
		return name != null ? (IAggrFunction) aggrMap.get(name.toUpperCase()) : null;
	}

	/**
	 * get a list of IAggrFunction instances for the specified type, which must be
	 * one of the values below:
	 * AggregationManager.AGGR_TABULAR,AggregationManager.AGGR_XTAB,AggregationManager.AGGR_MEASURE.
	 *
	 * @param type
	 * @return
	 */
	public List getAggregations(int type) {
		switch (type) {
		case AGGR_TABULAR:
			return getResult(allAggrNames.toArray());
		case AGGR_XTAB:
			return getResult(xTabAggrNames);
		case AGGR_MEASURE:
			return getResult(measureAggrNames);
		}
		return new ArrayList();
	}

	/**
	 *
	 * @param names
	 * @return
	 */
	private List getResult(Object[] names) {
		List list = new ArrayList();
		for (int i = 0; i < names.length; i++) {
			Object aggrFunc = aggrMap.get(names[i]);
			if (aggrFunc != null) {
				list.add(aggrFunc);
			}
		}
		return list;
	}

	/**
	 * get a list of IAggrFunction instances which contains all the aggregations
	 * function.
	 *
	 * @return
	 */
	public List getAggregations() {
		return getResult(allAggrNames.toArray());
	}
}

class AggrFunctionWrapper implements IAggrFunction {

	private IAggregation aggrFunc;
	private String displayName;
	private IParameterDefn[] parameterDefn;

	/**
	 * @param aggrFunc
	 */
	public AggrFunctionWrapper(IAggregation aggrFunc) {
		this.aggrFunc = aggrFunc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDataType()
	 */
	@Override
	public int getDataType() {
		return aggrFunc.getDataType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	@Override
	public String getDescription() {
		return "";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getName()
	 */
	@Override
	public String getName() {
		return aggrFunc.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getNumberOfPasses(
	 * )
	 */
	@Override
	public int getNumberOfPasses() {
		if (aggrFunc instanceof Aggregation) {
			return ((Aggregation) aggrFunc).getNumberOfPasses();
		}
		return 1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getParameterDefn()
	 */
	@Override
	public IParameterDefn[] getParameterDefn() {
		return this.parameterDefn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getType()
	 */
	@Override
	public int getType() {
		return aggrFunc.getType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#
	 * isDataOrderSensitive()
	 */
	@Override
	public boolean isDataOrderSensitive() {
		return false;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param parameterDefn the parameterDefn to set
	 */
	public void setParameterDefn(IParameterDefn[] parameterDefn) {
		this.parameterDefn = parameterDefn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#newAccumulator()
	 */
	@Override
	public Accumulator newAccumulator() {
		return aggrFunc.newAccumulator();
	}

	/**
	 *
	 */
	public static class ParameterDefn implements IParameterDefn {
		String name;
		String displayName;
		String description = "";//$NON-NLS-1$
		boolean isDataField;
		boolean isOptional;

		/**
		 *
		 * @param name
		 * @param displayName
		 * @param isOptional
		 * @param isDataField
		 */
		public ParameterDefn(String name, String displayName, boolean isOptional, boolean isDataField) {
			this.name = name;
			this.displayName = displayName;
			this.isDataField = isDataField;
			this.isOptional = isOptional;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#getDescription()
		 */
		@Override
		public String getDescription() {
			return description;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return displayName;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#isDataField()
		 */
		@Override
		public boolean isDataField() {
			return isDataField;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#isOptional()
		 */
		@Override
		public boolean isOptional() {
			return isOptional;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#supportDataType(
		 * int)
		 */
		@Override
		public boolean supportDataType(int dataType) {
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.api.aggregation.IParameterDefn#getName()
		 */
		@Override
		public String getName() {
			return name;
		}

	}
}
