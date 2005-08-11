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

package org.eclipse.birt.chart.device.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;

import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.emf.common.util.EList;

/**
 * Provides a reference implementation into handling events generated on a SWING
 * JComponent with a rendered chart.
 */
public final class SwingEventHandler implements
		MouseListener,
		MouseMotionListener
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swing" ); //$NON-NLS-1$

	private static final BasicStroke bs = new BasicStroke( 5,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			0,
			new float[]{
					6.0f, 4.0f
			},
			0 );

	/**
	 * 
	 */
	private ShapedAction saTooltip = null;

	/**
	 * 
	 */
	private ShapedAction saHighlighted = null;

	/**
	 * 
	 */
	private final LinkedHashMap lhmAllTriggers;

	/**
	 * 
	 */
	private final IUpdateNotifier iun;

	/**
	 * 
	 */
	private final Locale lcl;

	/**
	 * 
	 */
	SwingEventHandler( LinkedHashMap _lhmAllTriggers, IUpdateNotifier _jc,
			Locale _lcl )
	{
		lhmAllTriggers = _lhmAllTriggers;
		iun = _jc;
		lcl = _lcl;
	}

	/**
	 * 
	 */
	private final boolean isLeftButton( MouseEvent e )
	{
		return ( ( e.getButton( ) & MouseEvent.BUTTON1 ) == MouseEvent.BUTTON1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE CLICKS ONLY
		final Point p = e.getPoint( );
		final ArrayList al = (ArrayList) lhmAllTriggers.get( TriggerCondition.MOUSE_CLICK_LITERAL );
		if ( al == null )
			return;

		ShapedAction sa;
		Shape sh;
		Action ac;

		// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
		for ( int i = 0; i < al.size( ); i++ )
		{
			sa = (ShapedAction) al.get( i );
			sh = sa.getShape( );
			if ( sh.contains( p ) )
			{
				ac = sa.getAction( );
				switch ( ac.getType( ).getValue( ) )
				{
					case ActionType.URL_REDIRECT :
						final URLValue uv = (URLValue) ac.getValue( );
						logger.log( ILogger.INFORMATION,
								Messages.getString( "info.redirect.url", lcl ) //$NON-NLS-1$
										+ uv.getBaseUrl( ) );
						break;

					case ActionType.TOGGLE_VISIBILITY :
						final Series seRT = (Series) sa.getSource( );
						logger.log( ILogger.INFORMATION,
								Messages.getString( "info.toggle.visibility", //$NON-NLS-1$
										lcl ) + seRT );
						Series seDT = null;
						try
						{
							seDT = findDesignTimeSeries( seRT ); // LOCATE
							// THE
							// CORRESPONDING
							// DESIGN-TIME
							// SERIES
						}
						catch ( ChartException oosx )
						{
							logger.log( oosx );
							return;
						}
						seDT.setVisible( !seDT.isVisible( ) );
						iun.regenerateChart( );
						break;
				}
			}
		}
	}

	/**
	 * Locates a design-time series corresponding to a given cloned run-time
	 * series.
	 * 
	 * @param seRT
	 * @return
	 */
	private final Series findDesignTimeSeries( Series seRT )
			throws ChartException
	{
		final ChartWithAxes cwaRT = (ChartWithAxes) iun.getRunTimeModel( );
		final ChartWithAxes cwaDT = (ChartWithAxes) iun.getDesignTimeModel( );
		Series seDT = null;

		Axis[] axaBase = cwaRT.getPrimaryBaseAxes( );
		Axis axBase = axaBase[0];
		Axis[] axaOrthogonal = cwaRT.getOrthogonalAxes( axBase, true );
		EList elSD, elSE;
		SeriesDefinition sd;
		Series se = null;
		int i, j = 0, k = 0;
		boolean bFound = false;

		// LOCATE INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN RUN TIME MODEL
		for ( i = 0; i < axaOrthogonal.length; i++ )
		{
			elSD = axaOrthogonal[i].getSeriesDefinitions( );
			for ( j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				elSE = sd.getSeries( );
				for ( k = 0; k < elSE.size( ); k++ )
				{
					se = (Series) elSE.get( k );
					if ( seRT == se )
					{
						bFound = true;
						break;
					}
				}
				if ( bFound )
				{
					break;
				}
			}
			if ( bFound )
			{
				break;
			}
		}

		if ( !bFound )
		{
			throw new ChartException( ChartDeviceExtensionPlugin.ID,
					ChartException.OUT_OF_SYNC,
					"info.cannot.find.series", //$NON-NLS-1$
					new Object[]{
						seRT
					},
					ResourceBundle.getBundle( Messages.DEVICE_EXTENSION, lcl ) );
		}

		// MAP TO INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN DESIGN TIME MODEL
		axaBase = cwaDT.getPrimaryBaseAxes( );
		axBase = axaBase[0];
		axaOrthogonal = cwaDT.getOrthogonalAxes( axBase, true );
		elSD = axaOrthogonal[i].getSeriesDefinitions( );
		sd = (SeriesDefinition) elSD.get( j );
		elSE = sd.getSeries( );
		seDT = (Series) elSE.get( k );

		return seDT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered( MouseEvent e )
	{
	}

	/**
	 * 
	 * @param sa
	 */
	private final void showTooltip( ShapedAction sa )
	{
		Action ac = sa.getAction( );
		TooltipValue tv = (TooltipValue) ac.getValue( );
		String s = tv.getText( );

		( (JComponent) iun.peerInstance( ) ).setToolTipText( s );
	}

	/**
	 * 
	 * 
	 */
	private final void hideTooltip( )
	{
		( (JComponent) iun.peerInstance( ) ).setToolTipText( null );
	}

	/**
	 * 
	 * @param sh
	 */
	private final void toggle( Shape sh )
	{
		final Graphics2D g2d = (Graphics2D) ( (JComponent) iun.peerInstance( ) ).getGraphics( );
		final Color c = g2d.getColor( );
		final Stroke st = g2d.getStroke( );
		g2d.setXORMode( Color.white );
		g2d.setStroke( bs );
		g2d.fill( sh );
		g2d.setStroke( st );
		g2d.setColor( c );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved( MouseEvent e )
	{
		final Point p = e.getPoint( );

		// 1. CHECK FOR MOUSE-CLICK TRIGGERS
		ArrayList al = (ArrayList) lhmAllTriggers.get( TriggerCondition.MOUSE_CLICK_LITERAL );
		if ( al != null )
		{
			ShapedAction sa;
			Shape sh;

			// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
			boolean bFound = false;
			for ( int i = 0; i < al.size( ); i++ )
			{
				sa = (ShapedAction) al.get( i );
				sh = sa.getShape( );
				if ( sh.contains( p ) )
				{
					if ( sa != saHighlighted )
					{
						if ( saHighlighted != null )
						{
							toggle( saHighlighted.getShape( ) );
						}
						( (JComponent) iun.peerInstance( ) ).setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
						toggle( sh );
					}
					saHighlighted = sa;
					bFound = true;
					break;
				}
			}

			if ( !bFound && saHighlighted != null )
			{
				( (JComponent) iun.peerInstance( ) ).setCursor( Cursor.getDefaultCursor( ) );
				toggle( saHighlighted.getShape( ) );
				saHighlighted = null;
				bFound = false;
			}
		}

		// 2. CHECK FOR MOUSE-HOVER CONDITION
		al = (ArrayList) lhmAllTriggers.get( TriggerCondition.MOUSE_HOVER_LITERAL );
		if ( al != null )
		{
			ShapedAction sa;
			Shape sh;

			// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
			boolean bFound = false;
			for ( int i = 0; i < al.size( ); i++ )
			{
				sa = (ShapedAction) al.get( i );
				sh = sa.getShape( );
				if ( sh.contains( p ) )
				{
					if ( sa != saTooltip )
					{
						hideTooltip( );
					}
					saTooltip = sa;
					bFound = true;
					showTooltip( saTooltip );
					break;
				}
			}

			if ( !bFound && saTooltip != null )
			{
				hideTooltip( );
				saTooltip = null;
				bFound = false;
			}
		}
	}
}