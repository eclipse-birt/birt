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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Test class for test methods of BasAttributePageTest.
 * 
 *  
 */
public class BaseAttributePageTest extends TestCase
{

	/**
	 * Testcase for test setCategoryProvider() method
	 */
	public void testSetCategoryProvider( )
	{
		BaseAttributePage page = new BaseAttributePage( new Shell( ), SWT.NULL );
		page.setCategoryProvider( null );
		assertEquals( 0, page.categoryList.getItemCount( ) );
		final String[] labels = new String[]{
				"1", "2"
		};
		page.setCategoryProvider( new ICategoryProvider( ) {

			public String[] getCategorylabels( )
			{
				return labels;
			}

			public void createCategoryPanes( Composite parent, List input )
			{
			}

			public Control getCategoryPane( String category )
			{
				return null;
			}
		} );
		assertEquals( labels.length, page.categoryList.getItemCount( ) );
		assertEquals( 0, page.categoryList.getSelectionIndex( ) );
	}

	private class TestElelemnt extends DesignElement
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.model.core.DesignElement#apply(org.eclipse.birt.model.elements.ElementVisitor)
		 */
		public void apply( ElementVisitor arg0 )
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.model.core.DesignElement#getElementName()
		 */
		public String getElementName( )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.model.core.DesignElement#getHandle(org.eclipse.birt.model.elements.ReportDesign)
		 */
		public DesignElementHandle getHandle( ReportDesign arg0 )
		{
			return null;
		}

		public List getListener( )
		{
			return this.listeners;
		}

	}
}