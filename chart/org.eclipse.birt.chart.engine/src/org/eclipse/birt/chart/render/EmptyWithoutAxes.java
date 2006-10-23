/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;

/**
 * This class implements an empty renderer for ChartWithoutAxes type.
 */
public final class EmptyWithoutAxes extends BaseRenderer
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/render" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#renderSeries(org.eclipse.birt.chart.device.IPrimitiveRenderer,
	 *      org.eclipse.birt.chart.model.layout.Plot,
	 *      org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void renderSeries( IPrimitiveRenderer ipr, Plot p,
			ISeriesRenderingHints isrh ) throws ChartException
	{
		// NOTE: NO-OP IMPL
		logger.log( ILogger.INFORMATION,
				Messages.getString( "info.render.series", //$NON-NLS-1$
						new Object[]{
								getClass( ).getName( ),
								new Integer( iSeriesIndex + 1 ),
								new Integer( iSeriesCount )
						},
						getRunTimeContext( ).getULocale( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#compute(org.eclipse.birt.chart.model.attribute.Bounds,
	 *      org.eclipse.birt.chart.model.layout.Plot,
	 *      org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	public void compute( Bounds bo, Plot p, ISeriesRenderingHints isrh )
			throws ChartException
	{
		// NOTE: This method is not used by the Empty renderer
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#renderLegendGraphic(org.eclipse.birt.chart.device.IPrimitiveRenderer,
	 *      org.eclipse.birt.chart.model.layout.Legend,
	 *      org.eclipse.birt.chart.model.attribute.Fill,
	 *      org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	public void renderLegendGraphic( IPrimitiveRenderer ipr, Legend lg,
			Fill fPaletteEntry, Bounds bo ) throws ChartException
	{
		final LineAttributes lia = LineAttributesImpl.create( ColorDefinitionImpl.GREY( ),
				LineStyle.SOLID_LITERAL,
				1 );

		// COMPUTE THE FRONT FACE ONLY
		Location[] loaFrontFace = null;
		loaFrontFace = new Location[4];
		loaFrontFace[0] = LocationImpl.create( bo.getLeft( ), bo.getTop( ) );
		loaFrontFace[1] = LocationImpl.create( bo.getLeft( ), bo.getTop( )
				+ bo.getHeight( ) );
		loaFrontFace[2] = LocationImpl.create( bo.getLeft( ) + bo.getWidth( ),
				bo.getTop( ) + bo.getHeight( ) );
		loaFrontFace[3] = LocationImpl.create( bo.getLeft( ) + bo.getWidth( ),
				bo.getTop( ) );

		// RENDER THE PLANE (INTERNALLY EXTRUDED IF NECESSARY)
		renderPlane( ipr,
				StructureSource.createLegend( lg ),
				loaFrontFace,
				fPaletteEntry,
				lia,
				getModel( ).getDimension( ),
				3 * getDeviceScale( ),
				false );
	}
}