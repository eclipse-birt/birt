/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class ODAProviderFactory
		implements
			IODAProviderFactory,
			IODAFilterExprProvider
{

	/**
	 * Factory used to create the ODAProvider instance.
	 */
	private static IODAProviderFactory baseFactory = null;

	/**
	 * The only one ODAProviderFactory instance.
	 */
	private static volatile ODAProviderFactory instance = null;

	private static IODAFilterExprProvider filterProvider = null;

	/**
	 * Returns the ODAProviderFactory instance.
	 * 
	 * @return ODAProviderFactory instance.
	 */

	public static ODAProviderFactory getInstance( )
	{
		if ( instance == null )
			instance = new ODAProviderFactory( );
		return instance;
	}

	/**
	 * Returns the ODAProvider based on the element and the extension Id.
	 * 
	 * @param element
	 *            the ODA element.
	 * @param extensionID
	 *            The extension Id used to create the corresponding ODA element
	 *            definition.
	 * @return the ODA provider instance.
	 */
	public ODAProvider createODAProvider( DesignElement element,
			String extensionID )
	{
		if ( baseFactory != null )
			return baseFactory.createODAProvider( element, extensionID );

		return null;
	}

	public IFilterExprDefinition createFilterExprDefinition( )
	{
		if ( baseFactory != null )
			return baseFactory.createFilterExprDefinition( );

		return null;
	}

	public IFilterExprDefinition createFilterExprDefinition(
			String birtFilterExpr )
	{
		if ( baseFactory != null )
			return baseFactory.createFilterExprDefinition( birtFilterExpr );

		return null;
	}

	/**
	 * Set the base factory for this class. This method should be called before
	 * any other operation.
	 * 
	 * @param base
	 *            The real factory class used to create the ODA provider.
	 */

	public synchronized static void initeTheFactory( IODAProviderFactory base )
	{
		if ( baseFactory != null )
			return;

		baseFactory = base;
	}

	/**
	 * Set the base factory for this class. This method should be called before
	 * any other operation.
	 * 
	 * @param base
	 *            The real factory class used to create the ODA provider.
	 */

	public synchronized static void initFilterExprFactory(
			IODAFilterExprProvider filterProvider )
	{
		if ( ODAProviderFactory.filterProvider != null )
			return;

		ODAProviderFactory.filterProvider = filterProvider;
	}

	/**
	 * Singleton instance release method.
	 */
	public static void releaseInstance( )
	{
		baseFactory = null;
		instance = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #getMappedFilterExprDefinitions(java.lang.String, java.lang.String)
	 */
	public List<IFilterExprDefinition> getMappedFilterExprDefinitions(
			String odaDatasetExtensionId, String odaDataSourceExtensionId )
	{
		return filterProvider.getMappedFilterExprDefinitions(
				odaDatasetExtensionId, odaDataSourceExtensionId );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #supportOdaExtensionFilters()
	 */
	public boolean supportOdaExtensionFilters( )
	{
		return filterProvider.supportOdaExtensionFilters( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #supportODAFilterPushDown(java.lang.String, java.lang.String)
	 */
	public boolean supportODAFilterPushDown( String dataSourceExtId,
			String dataSetExtId )
	{
		return filterProvider.supportODAFilterPushDown( dataSourceExtId,
				dataSetExtId );
	}
}
