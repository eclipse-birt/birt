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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

/**
 * Instances of TextPageGenerator take change of creating attribute page
 * correspond to TextItem element.
 */
public class TextPageGenerator extends AbstractPageGenerator
{

	protected BindingPage bindingPage;
	protected PreviewPage highlightsPage;

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			String title = tabFolder.getSelection( ).getText( );
			if ( title.equals( BINDINGTITLE ) )
			{
				bindingPage = new BindingPage( );
				setPageInput( bindingPage );
				refresh( tabFolder, bindingPage, true );
				item.setControl( bindingPage.getControl( ) );
				itemMap.put( item, bindingPage );
			}
			else if ( title.equals( HIGHLIGHTSTITLE ) )
			{
				highlightsPage = new PreviewPage( true );
				highlightsPage.setPreview( new HighlightPropertyDescriptor( true ) );
				highlightsPage.setProvider( new HighlightDescriptorProvider( ) );
				setPageInput( highlightsPage );
				refresh( tabFolder, highlightsPage, true );
				item.setControl( highlightsPage.getControl( ) );
				itemMap.put( item, highlightsPage );
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
		addSelectionListener( this );
		createTabItems( );
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}

	protected void createTabItems( )
	{
		createTabItem( BINDINGTITLE, ATTRIBUTESTITLE );
		createTabItem( HIGHLIGHTSTITLE, BINDINGTITLE );
	}

	public void createControl( Composite parent, Object input )
	{
		super.createControl( parent, input );
		createTabItems( (List) input );
	}

}