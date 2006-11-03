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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormWidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * WidgetUtil defines constant values to custom control size and provides common
 * layout mechanism.
 */
public class WidgetUtil
{

	/**
	 * Default height of Text control
	 */
	public final static int TEXT_HEIGHT = 12;

	/**
	 * Default width of Text control
	 */
	public final static int TEXT_WIDTH = 40;

	/**
	 * Default Height of Combo control
	 */
	public final static int COMBO_HEIGHT = 8;

	/**
	 * Default width of Combo control
	 */
	public final static int COMBO_WIDTH = 18;

	/**
	 * The number of pixels between the edge of one control and the edge of its
	 * neighbouring control.
	 */
	public final static int SPACING = 8;

	/**
	 * The height of margin.
	 */
	public final static int MARGIN_HEIGHT = 7;

	/**
	 * The width of margin.
	 */
	public final static int MARGIN_WIDTH = 7;

	/**
	 * The minimum width for a text control
	 */
	public final static int MIN_TEXT_WIDTH = 80;

	/**
	 * Creates a default GridLayout layout Manager.
	 * 
	 * @param columns
	 *            The number of columns in the grid
	 * @return A GridLayout instance.
	 */
	public static GridLayout createGridLayout( int columns )
	{
		GridLayout layout = new GridLayout( columns, false );
		layout.marginHeight = WidgetUtil.MARGIN_HEIGHT;
		layout.marginWidth = WidgetUtil.MARGIN_WIDTH;
		layout.horizontalSpacing = WidgetUtil.SPACING;
		layout.verticalSpacing = WidgetUtil.SPACING;
		return layout;
	}

	public static void setGridData( Control control, int hSpan,
			boolean grabSpace )
	{
		GridData data = new GridData( );
		data.horizontalSpan = hSpan;
		data.grabExcessHorizontalSpace = grabSpace;
		if ( control instanceof Text || control instanceof Combo )
		{
			data.widthHint = MIN_TEXT_WIDTH;
		}
		data.horizontalAlignment = GridData.FILL;
		control.setLayoutData( data );
	}

	public static void setGridData( Control control, int hSpan, int width )
	{
		GridData data = new GridData( );
		data.horizontalSpan = hSpan;
		data.widthHint = width;
		control.setLayoutData( data );
	}

	/**
	 * Creates a GridLayout layout Manager that specified spaces of each border.
	 * 
	 * @param columns
	 *            The number of columns in the grid
	 * @param space
	 *            The space.
	 * @return A GridLayout instance.
	 */
	public static GridLayout createSpaceGridLayout( int columns, int space )
	{
		GridLayout layout = new GridLayout( columns, false );
		layout.marginHeight = space;
		layout.marginWidth = space;
		layout.horizontalSpacing = space;
		layout.verticalSpacing = space;
		return layout;
	}

	public static GridLayout createSpaceGridLayout( int columns, int space,
			boolean isFormStyle )
	{
		if ( isFormStyle )
			space += 2;
		GridLayout layout = new GridLayout( columns, false );
		layout.marginHeight = space;
		layout.marginWidth = space;
		layout.horizontalSpacing = space;
		layout.verticalSpacing = space;
		return layout;
	}

	/**
	 * Creates a default FormLayout layout Manager.
	 * 
	 * @return A FormLayout instance.
	 */
	public static FormLayout createFormLayout( )
	{
		FormLayout layout = new FormLayout( );
		layout.marginHeight = WidgetUtil.MARGIN_HEIGHT;
		layout.marginWidth = WidgetUtil.MARGIN_WIDTH;
		layout.spacing = WidgetUtil.SPACING;
		return layout;
	}

	/**
	 * Creates a default FormLayout layout Manager.
	 * 
	 * @param space
	 *            The space.
	 * @return A FormLayout instance.
	 */
	public static FormLayout createSpaceFormLayout( int space )
	{
		FormLayout layout = new FormLayout( );
		layout.marginHeight = space;
		layout.marginWidth = space;
		layout.spacing = space;
		return layout;
	}

	


	public static Composite buildGridComposite( Composite parent, int hSpan,
			boolean grabSpace )
	{

		Composite composite = new Composite( parent, SWT.NONE );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = hSpan;
		data.grabExcessHorizontalSpace = grabSpace;
		composite.setLayoutData( data );

		return composite;
	}


	
	/**
	 * Creates a place-holder Label for using in GridLayout.
	 * 
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param hSpan
	 *            The number of column cells that operable-control will take up.
	 * @param grabSpace
	 *            grabSpace specifies whether the cell will be made wide enough
	 *            to fit the remaining horizontal space.
	 * @return The place-holder Label control.
	 */
	public static Label createGridPlaceholder( Composite parent, int hSpan,
			boolean grabSpace )
	{
		Label label = FormWidgetFactory.getInstance( ).createLabel( parent,
				true );
		GridData data = new GridData( );
		data.horizontalSpan = hSpan;
		data.grabExcessHorizontalSpace = grabSpace;
		label.setLayoutData( data );
		return label;
	}

	/**
	 * Creates a horizontal seperator line.
	 * 
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param hSpan
	 *            The number of column cells that operable-control will take up.
	 * @return The Label control.
	 */
	public static Label createHorizontalLine( Composite parent, int hSpan )
	{
		Label label = FormWidgetFactory.getInstance( ).createLabel( parent,
				"",
				SWT.SEPARATOR | SWT.HORIZONTAL );
		GridData data = new GridData( );
		data.horizontalSpan = hSpan;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData( data );
		return label;
	}

	/**
	 * Error processor, shows the Error message.
	 * 
	 * @param shell
	 *            the parent window.
	 * @param e
	 *            Exception object.
	 */
	public static void processError( Shell shell, Exception e )
	{
		ExceptionHandler.handle( e );
	}

	public static void setExcludeGridData( Control control, boolean exclude )
	{
		Object obj = control.getLayoutData( );
		if ( obj == null )
			control.setLayoutData( new GridData( ) );
		else if ( !( obj instanceof GridData ) )
			return;
		GridData data = (GridData) control.getLayoutData( );
		data.exclude = exclude;
		control.setLayoutData( data );
		control.setVisible( !exclude );
	}
}