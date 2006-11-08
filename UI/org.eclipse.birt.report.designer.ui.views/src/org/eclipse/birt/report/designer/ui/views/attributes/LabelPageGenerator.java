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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * 
 */

public class LabelPageGenerator extends AbstractPageGenerator
{

	protected PreviewPage highlightsPage;

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			switch ( tabFolder.getSelectionIndex( ) )
			{
				case 1 :
					highlightsPage = new PreviewPage( true );
					highlightsPage.setPreview( new HighlightPropertyDescriptor( true ) );
					highlightsPage.setProvider( new HighlightDescriptorProvider( ) );
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
		createTabItem( 1,
				Messages.getString( "LabelPageGenerator.TabItem.Highlights" ) );
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}

}
