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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BaseAttributePage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.core.internal.runtime.AdapterManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;

/**
 * CategoryPageGenerator provides default implementation of ICategoryProvider.
 */
public class CategoryPageGenerator extends DefaultPageGenerator
{

	/**
	 * A <code>Composite<code> contains all category-like attribute pages.
	 */
	protected BaseAttributePage basicPage;

	private ICategoryProviderFactory factory;

	public CategoryPageGenerator( )
	{
		if ( factory == null )
		{
			factory = (ICategoryProviderFactory) AdapterManager.getDefault( )
					.getAdapter( this, ICategoryProviderFactory.class );
			if ( factory == null )
			{
				factory = CategoryProviderFactory.getInstance( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.IPageGenerator#createTabItems(org.eclipse.swt.widgets.TabFolder,
	 *      java.util.List)
	 */
	public void createTabItems( CTabFolder tabFolder, List input )
	{
		if ( basicPage == null )
		{
			super.createTabItems( tabFolder, input );
			tabFolder.setLayout( new FillLayout( ) );

			basicPage = new BaseAttributePage( );
			basicPage.buildUI( tabFolder  );
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( Messages.getString( "CategoryPageGenerator.TabItem.Attributes" ) ); //$NON-NLS-1$
			tabItem.setControl( basicPage.getControl( ) );

			basicPage.setCategoryProvider( factory.getCategoryProvider( input ) );
		}
		basicPage.setInput( input );
		basicPage.refresh( );
	}
}