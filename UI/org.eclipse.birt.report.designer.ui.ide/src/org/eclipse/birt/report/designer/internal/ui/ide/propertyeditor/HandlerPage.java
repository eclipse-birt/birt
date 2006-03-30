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

package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.internal.ui.ide.util.ClassFinder;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.AttributeConstant;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class HandlerPage extends AttributePage
{

	/**
	 * @param parent
	 * @param style
	 */
	public HandlerPage( Composite parent, int style )
	{
		super( parent, style );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage#buildUI()
	 */
	protected void buildUI( )
	{
		this.setLayout( WidgetUtil.createGridLayout( 5 ) );

		WidgetUtil.buildGridControl( this,
				propertiesMap,
				ReportDesignConstants.REPORT_DESIGN_ELEMENT,
				ReportDesignHandle.EVENT_HANDLER_CLASS_PROP,
				1,
				400 );

		Button browse = new Button( this, SWT.PUSH );
		browse.setText( Messages.getString( "EventHandlerPage.Browse" ) );
		browse.setLayoutData( new GridData( ) );
		browse.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ClassFinder finder = new ClassFinder( );
				String className = null;
				if ( input != null && input.size( ) > 0 )
				{
					if ( input.get( 0 ) instanceof ExtendedItemHandle
							&& extraProperty != null )
					{
						className = (String) ( extraProperty.get( AttributeConstant.EVENT_HANDLER_CLASS_PROPERTY_KEY ) );
					}
					else if ( input.get( 0 ) instanceof DesignElementHandle )
					{
						className = EventHandlerWrapper.getEventHandlerClassName( (DesignElementHandle) input.get( 0 ) );

					}
				}
				if ( className != null )
				{
					finder.setParentClassName( className );
					GroupPropertyHandle handle = DEUtil.getMultiSelectionHandle( input )
							.getPropertyHandle( ReportDesignHandle.EVENT_HANDLER_CLASS_PROP );
					try
					{
						handle.setStringValue( finder.getFinderClassName( ) );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}
			}

		} );
		WidgetUtil.createGridPlaceholder( this, 2, true );

	}

}
