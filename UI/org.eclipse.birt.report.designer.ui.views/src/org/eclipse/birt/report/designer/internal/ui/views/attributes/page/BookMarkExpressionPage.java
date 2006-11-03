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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ExpressionSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.swt.widgets.Composite;

/**
 * Bookmark expreesion page.
 * 
 */
public class BookMarkExpressionPage extends AttributePage
{

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 1 ) );

		ExpressionPropertyDescriptorProvider bookMarkProvider = new ExpressionPropertyDescriptorProvider( IReportItemModel.BOOKMARK_PROP,
				ReportDesignConstants.REPORT_ITEM );
		ExpressionSection bookMarkSection = new ExpressionSection( bookMarkProvider.getDisplayName( ),
				container,
				true );
		bookMarkSection.setProvider( bookMarkProvider );
		bookMarkSection.setWidth( 500 );
		addSection( PageSectionId.BOOKMARKEXPRESSION_BOOKMARK, bookMarkSection );
		createSections( );
		layoutSections( );

	}
	/*
	 * protected void refreshValues( Set propertiesSet ) { if
	 * (DEUtil.getInputSize(input ) > 0 ) { ReportElementHandle handle =
	 * (ReportElementHandle) DEUtil.getInputFirstElement(input );
	 * GroupPropertyHandle propertyHandle = GroupElementFactory.newGroupElement(
	 * handle.getModuleHandle( ), DEUtil.getInputElements(input ) )
	 * .getPropertyHandle( IReportItemModel.BOOKMARK_PROP ); Control[] children =
	 * bookmarkArea.getChildren( ); for ( int i = 0; i < children.length; i++ ) {
	 * children[i].setEnabled( !propertyHandle.isReadOnly( ) ); } }
	 * super.refresh( ); }
	 */

}
