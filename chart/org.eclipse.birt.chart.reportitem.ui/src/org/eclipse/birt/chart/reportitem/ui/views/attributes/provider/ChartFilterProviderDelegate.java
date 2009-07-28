/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.ChartPageGenerator;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * The filter delegate will create different filter provider for common and
 * sharing query cases at runtime.
 * 
 * @since 2.3
 */
public class ChartFilterProviderDelegate extends AbstractFilterHandleProvider
{

	/* Filter provider handle. */
	private AbstractFilterHandleProvider fProvider;

	/**
	 * @param provider
	 */
	public ChartFilterProviderDelegate( AbstractFilterHandleProvider provider )
	{
		fProvider = provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify( Object element, String property )
	{
		return fProvider.canModify( element, property );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doAddItem(int)
	 */
	public boolean doAddItem( int pos ) throws Exception
	{
		return fProvider.doAddItem( pos );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doDeleteItem(int)
	 */
	public boolean doDeleteItem( int pos ) throws Exception
	{
		return fProvider.doDeleteItem( pos );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doEditItem(int)
	 */
	public boolean doEditItem( int pos )
	{
		return fProvider.doEditItem( pos );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doMoveItem(int, int)
	 */
	public boolean doMoveItem( int oldPos, int newPos ) throws Exception
	{
		return fProvider.doMoveItem( oldPos, newPos );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getColumnNames()
	 */
	public String[] getColumnNames( )
	{
		return fProvider.getColumnNames( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText( Object element, int columnIndex )
	{
		return fProvider.getColumnText( element, columnIndex );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getColumnWidths()
	 */
	public int[] getColumnWidths( )
	{
		return fProvider.getColumnWidths( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getEditors(org.eclipse.swt.widgets.Table)
	 */
	public CellEditor[] getEditors( Table table )
	{
		return fProvider.getEditors( table );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object input )
	{
		fProvider = createFilterProvider( input, fProvider.getInput( ) );

		return fProvider.getElements( input );
	}

	/**
	 * Create filter provider by specified input.
	 * 
	 * @param input
	 * @param providerInput
	 * @return the created filter provider
	 * @since 2.3
	 */
	public static AbstractFilterHandleProvider createFilterProvider(
			Object input, Object providerInput )
	{
		AbstractFilterHandleProvider currentProvider = null;

		Object handle = null;
		if ( input instanceof List<?> )
		{
			handle = ( (List<?>) input ).get( 0 );
		}
		else
		{
			handle = input;
		}

		AbstractFilterHandleProvider baseProvider = getAdapter( new ChartPageGenerator( ),
				AbstractFilterHandleProvider.class );
		if ( baseProvider == null )
		{
			baseProvider = new FilterHandleProvider( );
		}

		if ( handle instanceof ReportItemHandle
				&& ChartXTabUtil.getBindingCube( (DesignElementHandle) handle ) != null )

		{
			// It is in cube mode.
			if ( ( (ReportItemHandle) handle ).getCube( ) != null
					&& ( ChartReportItemUtil.isChildOfMultiViewsHandle( (DesignElementHandle) handle ) || ( (ReportItemHandle) handle ).getDataBindingReference( ) != null ) )
			{
				// Sharing crosstab/multi-view
				ReportItemHandle ref = ( (ReportItemHandle) handle ).getDataBindingReference( );
				if ( ChartReportItemUtil.isChartHandle( ref ) )
				{
					currentProvider = new ChartShareCubeFiltersHandleProvider( baseProvider );
				}
				else
				{
					currentProvider = new ChartShareCrosstabFiltersHandleProvider( );
				}
			}
			else
			{
				currentProvider = new ChartCubeFilterHandleProvider( baseProvider );
			}
		}

		else
		{
			// It is in table mode.
			if ( ChartReportItemUtil.isChildOfMultiViewsHandle( (DesignElementHandle) handle ) )
			{
				// Chart is in multi-view.
				currentProvider = new ChartShareFiltersHandleProvider( baseProvider );
			}
			else
			{
				currentProvider = new ChartFilterHandleProvider( baseProvider );
			}
		}

		currentProvider.setInput( input );

		return currentProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getImagePath(java.lang.Object, int)
	 */
	public Image getImage( Object element, int columnIndex )
	{
		return fProvider.getImage( element, columnIndex );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue( Object element, String property )
	{
		return fProvider.getValue( element, property );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#modify(java.lang.Object, java.lang.String,
	 * java.lang.Object)
	 */
	public boolean modify( Object data, String property, Object value )
			throws Exception
	{
		return fProvider.modify( data, property, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider
	 * #needRefreshed(org.eclipse.birt.report.model.api.activity.NotificationEvent
	 * )
	 */
	public boolean needRefreshed( NotificationEvent event )
	{
		return fProvider.needRefreshed( event );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return fProvider.getDisplayName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .AbstractFormHandleProvider#isEditable()
	 */
	public boolean isEditable( )
	{
		return fProvider.isEditable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#isAddEnable()
	 */
	public boolean isAddEnable( )
	{
		return fProvider.isAddEnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#isDeleteEnable()
	 */
	public boolean isDeleteEnable( )
	{
		return fProvider.isDeleteEnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#isDownEnable()
	 */
	public boolean isDownEnable( )
	{
		return fProvider.isDownEnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#isEditEnable()
	 */
	public boolean isEditEnable( )
	{
		return fProvider.isEditEnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#isEnable()
	 */
	public boolean isEnable( )
	{
		return fProvider.isEnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#isUpEnable()
	 */
	public boolean isUpEnable( )
	{
		return fProvider.isUpEnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#load()
	 */
	public Object load( )
	{
		return fProvider.load( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#save(java.lang.Object)
	 */
	public void save( Object value ) throws SemanticException
	{
		fProvider.save( value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#setInput(java.lang.Object)
	 */
	public void setInput( Object input )
	{
		fProvider.setInput( input );
	}

	/**
	 * @param provider
	 */
	public void setProvider( AbstractFilterHandleProvider provider )
	{
		fProvider = provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .AbstractFilterHandleProvider#getConcreteFilterProvider()
	 */
	public IFormProvider getConcreteFilterProvider( )
	{
		return fProvider.getConcreteFilterProvider( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .AbstractFormHandleProvider
	 * #needRebuilded(org.eclipse.birt.report.model.api
	 * .activity.NotificationEvent)
	 */
	public boolean needRebuilded( NotificationEvent event )
	{
		return fProvider.needRebuilded( event );
	}

	private static <T> T getAdapter( Object adaptable, Class<T> type )
	{
		return type.cast( ElementAdapterManager.getAdapter( adaptable, type ) );
	}
}
