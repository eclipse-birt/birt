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

package org.eclipse.birt.report.designer.core.model.schematic;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.DimensionValue;
import org.eclipse.draw2d.geometry.Insets;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement LabelHandleAdapter responds to model LabelHandle
 */
public class LabelHandleAdapter extends ReportItemtHandleAdapter
{

	/**
	 * Constructor
	 * 
	 * @param labelHandle
	 *            The label handle.
	 * @param mark
	 */
	public LabelHandleAdapter( ReportItemHandle labelHandle, IModelAdapterHelper mark )
	{
		super( labelHandle, mark );
	}
	
	/**
	 * Get the padding of the current table.
	 * 
	 * @param retValue
	 *            The padding value of the current table.
	 * @return The padding's new value of the current table.
	 */
	public Insets getPadding( Insets retValue )
	{
		if ( retValue == null )
		{
			retValue = new Insets( );
		}
		else
		{
			retValue = new Insets( retValue );
		}

		DimensionHandle fontHandle = getHandle( ).getPrivateStyle( )
				.getFontSize( );

		int fontSize = 12;//??
		if ( fontHandle.getValue( ) instanceof String )
		{
			fontSize = Integer.valueOf( (String) DesignerConstants.fontMap.get( DEUtil.getFontSize( getHandle( ) ) ) )
					.intValue( );
		}
		else if ( fontHandle.getValue( ) instanceof DimensionValue )
		{
			DEUtil.convertToPixel( fontHandle.getValue( ), fontSize );
		}

		DimensionValue dimensionValue = (DimensionValue) getHandle( ).getProperty( Style.PADDING_TOP_PROP );
		double px = DEUtil.convertToPixel( dimensionValue, fontSize );

		dimensionValue = (DimensionValue) getHandle( ).getProperty( Style.PADDING_BOTTOM_PROP );
		double py = DEUtil.convertToPixel( dimensionValue, fontSize );

		retValue.top = (int) px;
		retValue.bottom = (int) py;

		dimensionValue = (DimensionValue) getHandle( ).getProperty( Style.PADDING_LEFT_PROP );
		px = DEUtil.convertToPixel( dimensionValue, fontSize );

		dimensionValue = (DimensionValue) getHandle( ).getProperty( Style.PADDING_RIGHT_PROP );
		py = DEUtil.convertToPixel( dimensionValue, fontSize );

		retValue.left = (int) px;
		retValue.right = (int) py;

		return retValue;
	}
	
}