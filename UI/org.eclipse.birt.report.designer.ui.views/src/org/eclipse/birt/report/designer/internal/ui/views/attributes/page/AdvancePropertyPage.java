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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AdvancePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.AdvancePropertySection;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 *
 */
public class AdvancePropertyPage extends AttributePage
{

	private AdvancePropertyDescriptorProvider provider;
	private AdvancePropertySection propertySection;


	public void buildUI( Composite parent )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 5 ,15) );
		provider = new AdvancePropertyDescriptorProvider();
		
		propertySection = new AdvancePropertySection( provider.getDisplayName( ),
				container,
				true,
				false );
		propertySection.setProvider( provider );
		propertySection.setFillControl( true );
		addSection( PageSectionId.ADVANCE_PROPERTY, propertySection );

		createSections( );
		layoutSections( );

	}

//	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
//	{
//		Section section = getSection( PageSectionId.ADVANCE_PROPERTY );
//		if(section!=null && section instanceof AdvancePropertySection){
//			((AdvancePropertySection)section).getControl( ).elementChanged( focus, ev );
//		}
//	}

//	public void dispose( )
//	{
//		if ( input == null )
//			return;
//		Section section = getSection( PageSectionId.ADVANCE_PROPERTY );
//		if(section!=null && section instanceof AdvancePropertySection){
//			for ( int i = 0; i < DEUtil.getInputSize( input ); i++ )
//			{
//				Object obj = DEUtil.getInputElement( input, i );
//				if ( obj instanceof DesignElementHandle )
//				{
//					DesignElementHandle element = (DesignElementHandle) obj;
//					element.removeListener( ((AdvancePropertySection)section).getControl( ) );
//				}
//			}
//		}
//	}

}
