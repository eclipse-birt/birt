/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Label;

/**
 * 
 */

public class TextRendererAdapter implements ITextRenderer
{

	protected IDisplayServer _sxs = null;

	protected TextRendererAdapter( IDisplayServer sxs )
	{
		this._sxs = sxs;
	}

	public void renderShadowAtLocation( IPrimitiveRenderer idr,
			int labelPosition, Location lo, Label la ) throws ChartException
	{
		// TODO Auto-generated method stub

	}

	public void renderTextAtLocation( IPrimitiveRenderer ipr,
			int labelPosition, Location lo, Label la ) throws ChartException
	{
		// TODO Auto-generated method stub

	}

	public void renderTextInBlock( IDeviceRenderer idr, Bounds boBlock,
			TextAlignment taBlock, Label la ) throws ChartException
	{
		// TODO Auto-generated method stub

	}

}
