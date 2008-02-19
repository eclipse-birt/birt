/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.attributes;

import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartBindingPage;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartFilterProviderDelegate;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartShareCrosstabFiltersHandleProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartShareFiltersHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

/**
 * ChartReportItemPropertyEditUIImpl
 */
public class ChartPageGenerator extends AbstractPageGenerator
{

	protected PreviewPage highlightsPage;
	protected FormPage filterPage;
	protected BindingPage bindingPage;
	protected FormPage sortingPage;

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			String title = tabFolder.getSelection( ).getText( );
			if ( title.equals( BINDINGTITLE ) )
			{
				bindingPage = new ChartBindingPage( );
				setPageInput( bindingPage );

				refresh( tabFolder, bindingPage, true );
				item.setControl( bindingPage.getControl( ) );
				itemMap.put( item, bindingPage );
			}
			else if ( title.equals( FILTERTITLE ) )
			{
				Object handle = null;
				if ( input instanceof List)
				{
					handle = ((List)input).get(0);
				}
				else
				{
					handle = input;
				}
				AbstractFormHandleProvider provider = null;
				if ( handle instanceof ReportItemHandle
						&& ((ReportItemHandle) handle ).getCube( ) != null )
				{
					// Sharing crosstab/multi-view
					if ( ChartReportItemUtil.isChildOfMultiViewsHandle( (DesignElementHandle) handle ) 
							|| ((ReportItemHandle)handle).getDataBindingReference( ) != null )
					{
						provider = new ChartShareCrosstabFiltersHandleProvider();
					}
					else
					{
						provider = new CrosstabFilterHandleProvider( );
					}
				}
				else
				{
					// sharing table/multi-view
					if ( ChartReportItemUtil.isChildOfMultiViewsHandle( (DesignElementHandle) handle ) )
					{
						provider = new ChartShareFiltersHandleProvider();
					}
					else
					{
						provider = new FilterHandleProvider( );
					}
				}
				
				IFormProvider providerDelegate = new ChartFilterProviderDelegate( provider );
				filterPage = new FormPage( FormPropertyDescriptor.FULL_FUNCTION,
						providerDelegate,
						true,
						true );
				setPageInput( filterPage );
				refresh( tabFolder, filterPage, true );
				item.setControl( filterPage.getControl( ) );
				itemMap.put( item, filterPage );
			}
		}
		else if ( itemMap.get( item ) != null )
		{
			setPageInput( itemMap.get( item ) );
			refresh( tabFolder, itemMap.get( item ), false );
		}
	}

	public void createTabItems( List input )
	{
		super.createTabItems( input );
		this.input = input;
		basicPage.setInput( input );
		addSelectionListener( this );
		basicPage.refresh( );
		createTabItems( );
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}

	protected void createTabItems( )
	{
		createTabItem( BINDINGTITLE, ATTRIBUTESTITLE );
		createTabItem( FILTERTITLE, BINDINGTITLE );
	}

	public void createControl( Composite parent, Object input )
	{
		setCategoryProvider( ChartCategoryProviderFactory.getInstance( )
				.getCategoryProvider( input ) );
		super.createControl( parent, input );
		createTabItems( (List) input );
	}
}
