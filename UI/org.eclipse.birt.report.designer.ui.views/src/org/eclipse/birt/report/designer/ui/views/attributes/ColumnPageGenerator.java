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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * Instances of ColumnPageGenerator take change of creating attribute page
 * correspond to TableColumn element.
 */
public class ColumnPageGenerator extends AbstractPageGenerator
{

	protected PreviewPage highlightsPage;

	protected PreviewPage mapPage;

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			switch ( tabFolder.getSelectionIndex( ) )
			{
				case 1 :
					mapPage = new PreviewPage( true );
					mapPage.setPreview( new MapPropertyDescriptor( true ) );
					mapPage.setProvider( new MapPropertyDescriptorProvider( ) );
					setPageInput( mapPage );
					refresh(tabFolder,mapPage, true);
					item.setControl( mapPage.getControl( ) );
					itemMap.put( item, mapPage );
					break;
				case 2 :
					highlightsPage = new PreviewPage( true );
					highlightsPage.setPreview( new HighlightPropertyDescriptor( true ) );
					highlightsPage.setProvider( new HighlightPropertyDescriptorProvider( ) );
					setPageInput( highlightsPage );
					refresh(tabFolder,highlightsPage, true);
					item.setControl( highlightsPage.getControl( ) );
					itemMap.put( item, highlightsPage );
					break;
				default :
					break;
			}
		}
		else if ( itemMap.get( item ) != null ){
			setPageInput( itemMap.get( item ) );
			refresh(tabFolder,itemMap.get( item ), false);
		}
	}

	public void createTabItems( CTabFolder tabFolder, List input )
	{
		super.createTabItems( tabFolder, input );
		this.input = input;
		this.tabFolder = tabFolder;
		createTabItem( 1, Messages.getString( "ListPageGenerator.TabItem.map" ) );
		createTabItem( 2,
				Messages.getString( "ListPageGenerator.TabItem.Highlights" ) );
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}
}