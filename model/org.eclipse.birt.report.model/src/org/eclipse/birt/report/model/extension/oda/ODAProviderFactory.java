/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.extension.oda;

import java.util.List;

import org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * This class is used to wrap the ODA code. The
 * <code>ODABaseProviderFactory</code> is the real object cached in the this
 * class to create the <code>ODAProvider</code>. This class must be initialized
 * firstly to set the <code>baseFactory</code>.
 */
public class ODAProviderFactory implements IODAProviderFactory, IODAFilterExprProvider {

	/**
	 * Factory used to create the ODAProvider instance.
	 */
	private static volatile IODAProviderFactory baseFactory = null;

	/**
	 * The only one ODAProviderFactory instance.
	 */
	private static volatile ODAProviderFactory instance = null;

	private static volatile IODAFilterExprProvider filterProvider = null;

	/**
	 * Returns the ODAProviderFactory instance.
	 * 
	 * @return ODAProviderFactory instance.
	 */

	public static ODAProviderFactory getInstance() {
		if (instance == null)
			instance = new ODAProviderFactory();
		return instance;
	}

	protected static IODAProviderFactory getODAProvider() {
		if (baseFactory != null) {
			return baseFactory;
		}

		synchronized (ODAProviderFactory.class) {
			if (baseFactory == null) {
				try {
					Class clazz = Class.forName("org.eclipse.birt.report.model.plugin.ODABaseProviderFactory");
					baseFactory = (IODAProviderFactory) clazz.newInstance();
				} catch (Exception ex) {
				}
			}
		}
		return baseFactory;
	}

	protected static IODAFilterExprProvider getFilterProvider() {
		if (filterProvider != null) {
			return filterProvider;
		}
		synchronized (ODAProviderFactory.class) {
			if (filterProvider == null) {
				try {
					Class clazz = Class
							.forName("org.eclipse.birt.report.model.api.filterExtension.ODAFilterExprProvider");
					filterProvider = (IODAFilterExprProvider) clazz.newInstance();
				} catch (Exception ex) {
				}
			}
		}
		return filterProvider;
	}

	/**
	 * Returns the ODAProvider based on the element and the extension Id.
	 * 
	 * @param element     the ODA element.
	 * @param extensionID The extension Id used to create the corresponding ODA
	 *                    element definition.
	 * @return the ODA provider instance.
	 */
	public ODAProvider createODAProvider(DesignElement element, String extensionID) {
		IODAProviderFactory provider = getODAProvider();
		if (provider != null)
			return provider.createODAProvider(element, extensionID);

		return null;
	}

	public IFilterExprDefinition createFilterExprDefinition() {
		IODAProviderFactory provider = getODAProvider();
		if (provider != null)
			return provider.createFilterExprDefinition();

		return null;
	}

	public IFilterExprDefinition createFilterExprDefinition(String birtFilterExpr) {
		IODAProviderFactory provider = getODAProvider();
		if (provider != null)
			return provider.createFilterExprDefinition(birtFilterExpr);

		return null;
	}

	/**
	 * Set the base factory for this class. This method should be called before any
	 * other operation.
	 * 
	 * @param base The real factory class used to create the ODA provider.
	 */

	public synchronized static void initeTheFactory(IODAProviderFactory base) {
		if (baseFactory != null)
			return;

		baseFactory = base;
	}

	/**
	 * Set the base factory for this class. This method should be called before any
	 * other operation.
	 * 
	 * @param base The real factory class used to create the ODA provider.
	 */

	public synchronized static void initFilterExprFactory(IODAFilterExprProvider provider) {
		if (filterProvider != null)
			return;

		filterProvider = provider;
	}

	/**
	 * Singleton instance release method.
	 */
	public static void releaseInstance() {
		baseFactory = null;
		instance = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #getMappedFilterExprDefinitions(java.lang.String, java.lang.String)
	 */
	public List<IFilterExprDefinition> getMappedFilterExprDefinitions(String odaDatasetExtensionId,
			String odaDataSourceExtensionId) {
		IODAFilterExprProvider provider = getFilterProvider();
		if (provider != null) {
			return provider.getMappedFilterExprDefinitions(odaDatasetExtensionId, odaDataSourceExtensionId);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #supportOdaExtensionFilters()
	 */
	public boolean supportOdaExtensionFilters() {
		IODAFilterExprProvider provider = getFilterProvider();
		if (provider != null) {
			return provider.supportOdaExtensionFilters();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #supportODAFilterPushDown(java.lang.String, java.lang.String)
	 */
	public boolean supportODAFilterPushDown(String dataSourceExtId, String dataSetExtId) {
		IODAFilterExprProvider provider = getFilterProvider();
		if (provider != null) {
			return provider.supportODAFilterPushDown(dataSourceExtId, dataSetExtId);
		}
		return false;
	}
}
