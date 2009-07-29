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

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartCubeFilterConditionBuilder;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.jface.dialogs.Dialog;


/**
 * The filter provider is used for cube set, it works against setting filters in property page.
 * 
 * @since 2.3
 */
public class ChartCubeFilterHandleProvider extends
		ChartFilterProviderDelegate
{
	private ChartWizardContext context = null;
	
	public void setContext( ChartWizardContext context )
	{
		this.context = context;
	}
	
	public ChartCubeFilterHandleProvider(
			AbstractFilterHandleProvider baseProvider )
	{
		super( baseProvider );
		setModelAdapter( new ChartCubeFilterModelProvider( ) );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.IFormHandleProvider#doAddItem(int)
	 */
	public boolean doAddItem( int pos ) throws SemanticException
	{
		// return modelAdapter.doAddItem( input.get( 0 ), pos );
		Object item = getContentInput( ).get( 0 );
		if ( item instanceof DesignElementHandle )
		{
			ChartCubeFilterConditionBuilder dialog = new ChartCubeFilterConditionBuilder( UIUtil.getDefaultShell( ),
					FilterConditionBuilder.DLG_TITLE_NEW,
					FilterConditionBuilder.DLG_MESSAGE_NEW );
			dialog.setTipsForCube( Messages.getString( "ChartCubeFilterConditionBuilder.Information" ) ); //$NON-NLS-1$
			dialog.setDesignHandle( (DesignElementHandle) item, context );
			dialog.setInput( null );
			dialog.setBindingParams( getBindingParams( ) );
			if ( item instanceof ReportItemHandle )
			{
				dialog.setReportElement( (ReportItemHandle) item );
			}
			else if ( item instanceof GroupHandle )
			{
				dialog.setReportElement( (ReportItemHandle) ( (GroupHandle) item ).getContainer( ) );
			}
			if ( dialog.open( ) == Dialog.CANCEL )
			{
				return false;
			}

		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.IFormHandleProvider#doEditItem(int)
	 */
	public boolean doEditItem( int pos )
	{

		Object item = getContentInput( ).get( 0 );
		if ( item instanceof DesignElementHandle )
		{
			DesignElementHandle element = (DesignElementHandle) item;
			PropertyHandle propertyHandle = element.getPropertyHandle( ChartReportItemUtil.PROPERTY_CUBE_FILTER );
			FilterConditionElementHandle filterHandle = (FilterConditionElementHandle) ( propertyHandle.getListValue( ).get( pos ) );
			if ( filterHandle == null )
			{
				return false;
			}

			ChartCubeFilterConditionBuilder dialog = new ChartCubeFilterConditionBuilder( UIUtil.getDefaultShell( ),
					FilterConditionBuilder.DLG_TITLE_EDIT,
					FilterConditionBuilder.DLG_MESSAGE_EDIT );
			dialog.setDesignHandle( (DesignElementHandle) item, context );
			dialog.setInput( filterHandle );
			dialog.setBindingParams( getBindingParams( ) );
			if ( item instanceof ReportItemHandle )
			{
				dialog.setReportElement( (ReportItemHandle) item );
			}
			else if ( item instanceof GroupHandle )
			{
				dialog.setReportElement( (ReportItemHandle) ( (GroupHandle) item ).getContainer( ) );
			}
			if ( dialog.open( ) == Dialog.CANCEL )
			{
				return false;
			}

		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider#isEditable()
	 */
	public boolean isEditable( )
	{
		if ( ( (ReportItemHandle) DEUtil.getInputFirstElement( getInput( ) ) ).getCube( ) != null )
		{
			return true;
		}

		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider#getConcreteFilterProvider()
	 */
	public IFormProvider getConcreteFilterProvider( )
	{
		if ( input == null ) {
			return this;
		}

		return ChartFilterProviderDelegate.createFilterProvider( input,
				getInput( ) );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider#needRefreshed(org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public boolean needRefreshed( NotificationEvent event )
	{
		if ( event instanceof PropertyEvent )
		{
			String propertyName = ( (PropertyEvent) event ).getPropertyName( );
			if (ChartReportItemUtil.PROPERTY_CUBE_FILTER.equals( propertyName ))
			{
				return true;
			}
		}
		
		return super.needRefreshed( event );
	}
}
