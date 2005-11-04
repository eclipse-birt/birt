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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public interface ISelectDataCustomizeUI
{

	public static final int ORTHOGONAL_SERIES = 1;
	public static final int GROUPING_SERIES = 2;

	/**
	 * Creates left binding area
	 * 
	 * @param parent
	 *            composite parent
	 */
	public void createLeftBindingArea( Composite parent );

	public void createRightBindingArea( Composite parent );

	public void createBottomBindingArea( Composite parent );

	public void refreshLeftBindingArea( );

	public void refreshRightBindingArea( );

	public void refreshBottomBindingArea( );

	public void selectLeftBindingArea( boolean selected, Object data );

	public void selectRightBindingArea( boolean selected, Object data );

	public void selectBottomBindingArea( boolean selected, Object data );

	public void dispose( );

	/**
	 * 
	 * @param areaType
	 *            <code>ORTHOGONAL_SERIES</code>,
	 *            <code>GROUPING_SERIES</code>
	 * @param seriesdefinition
	 * @param builder
	 * @param oContext
	 * @param sTitle
	 * @return UI component
	 */
	public ISelectDataComponent getAreaComponent( int areaType,
			SeriesDefinition seriesdefinition, IUIServiceProvider builder,
			Object oContext, String sTitle );

	/**
	 * Gets custom preview table which is used for data preview
	 * 
	 * @return custom preview table
	 */
	public Object getCustomPreviewTable( );

	public void layoutAll( );

}
