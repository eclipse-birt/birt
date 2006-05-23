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

package org.eclipse.birt.chart.device.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.chart.device.ICallBackNotifier;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.device.swt.util.SwtUtil;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ibm.icu.util.ULocale;

/**
 * SwtEventHandler
 */
class SwtEventHandler implements
		MouseListener,
		MouseMoveListener,
		MouseTrackListener,
		KeyListener
{

	private static final ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swt" ); //$NON-NLS-1$

	private final Cursor hand_cursor;

	private final LinkedHashMap lhmAllTriggers;

	private final IUpdateNotifier iun;

	private RegionAction raTooltip = null;

	private RegionAction raHighlighted = null;

	private final ULocale lcl;

	private final GC _gc;

	/**
	 * The constructor.
	 * 
	 * @param _lhmAllTriggers
	 * @param _jc
	 * @param _lcl
	 */
	SwtEventHandler( LinkedHashMap _lhmAllTriggers, IUpdateNotifier _jc,
			ULocale _lcl )
	{
		lhmAllTriggers = _lhmAllTriggers;
		iun = _jc;
		lcl = _lcl;
		hand_cursor = new Cursor( Display.getDefault( ), SWT.CURSOR_HAND );
		_gc = new GC( Display.getDefault( ) );
	}

	private final List getActionsForConditions( TriggerCondition[] tca )
	{
		if ( tca == null || tca.length == 0 )
		{
			return null;
		}

		ArrayList al = new ArrayList( );

		for ( int i = 0; i < tca.length; i++ )
		{
			ArrayList tal = (ArrayList) lhmAllTriggers.get( tca[i] );

			if ( tal != null )
			{
				al.addAll( tal );
			}
		}

		if ( al.size( ) > 0 )
		{
			return al;
		}

		return null;
	}

	private final boolean isLeftButton( MouseEvent e )
	{
		return ( e.button == 1 );
	}

	private void handleAction( List al, Object event )
	{
		handleAction( al, event, true );
	}

	private synchronized void handleAction( List al, Object event,
			boolean cleanState )
	{
		if ( al == null || event == null )
		{
			return;
		}

		RegionAction ra;
		Action ac;

		Point p = null;

		if ( event instanceof MouseEvent )
		{
			p = new Point( ( (MouseEvent) event ).x, ( (MouseEvent) event ).y );
		}

		if ( event instanceof KeyEvent )
		{
			// TODO filter key ?
		}

		boolean bFound = false;

		// POLL EACH EVENT REGISTERED
		LOOP: for ( int i = 0; i < al.size( ); i++ )
		{
			ra = (RegionAction) al.get( i );
			if ( p == null || ra.contains( p, _gc ) )
			{
				ac = ra.getAction( );
				final StructureSource src = ra.getSource( );

				switch ( ac.getType( ).getValue( ) )
				{
					case ActionType.URL_REDIRECT :
						final URLValue uv = (URLValue) ac.getValue( );
						logger.log( ILogger.INFORMATION,
								Messages.getString( "SwtEventHandler.info.redirect.url", lcl ) //$NON-NLS-1$
										+ uv.getBaseUrl( ) );
						SwtUtil.openURL( uv.getBaseUrl( ) );
						break LOOP;

					case ActionType.SHOW_TOOLTIP :

						if ( ra != raTooltip )
						{
							hideTooltip( );
						}
						raTooltip = ra;
						bFound = true;
						showTooltip( raTooltip );
						break LOOP;

					case ActionType.TOGGLE_VISIBILITY :
						if ( src.getType( ) == StructureType.SERIES
								|| src.getType( ) == StructureType.SERIES_DATA_POINT )
						{
							final Series seRT;
							if ( src.getType( ) == StructureType.SERIES )
							{
								seRT = (Series) src.getSource( );
							}
							else
							{
								seRT = (Series) ( (WrappedStructureSource) src ).getParent( )
										.getSource( );
							}
							logger.log( ILogger.INFORMATION,
									Messages.getString( "SwtEventHandler.info.toggle.visibility", //$NON-NLS-1$
											lcl )
											+ seRT );
							Series seDT = null;
							try
							{
								seDT = findDesignTimeSeries( seRT );
							}
							catch ( ChartException oosx )
							{
								logger.log( oosx );
								return;
							}
							seDT.setVisible( !seDT.isVisible( ) );
							iun.regenerateChart( );
							break LOOP;
						}
						break;

					case ActionType.TOGGLE_DATA_POINT_VISIBILITY :
						if ( src.getType( ) == StructureType.SERIES
								|| src.getType( ) == StructureType.SERIES_DATA_POINT )
						{
							final Series seRT;
							if ( src.getType( ) == StructureType.SERIES )
							{
								seRT = (Series) src.getSource( );
							}
							else
							{
								seRT = (Series) ( (WrappedStructureSource) src ).getParent( )
										.getSource( );
							}
							logger.log( ILogger.INFORMATION,
									Messages.getString( "SwtEventHandler.info.toggle.datapoint.visibility", //$NON-NLS-1$
											lcl )
											+ seRT );
							Series seDT = null;
							try
							{
								seDT = findDesignTimeSeries( seRT );
							}
							catch ( ChartException oosx )
							{
								logger.log( oosx );
								return;
							}
							seDT.getLabel( ).setVisible( !seDT.getLabel( )
									.isVisible( ) );
							iun.regenerateChart( );
							break LOOP;
						}
						break;

					case ActionType.HIGHLIGHT :
						bFound = true;

						boolean newRegion = raHighlighted == null
								|| raHighlighted.isEmpty( );

						if ( !newRegion )
						{
							if ( p == null || !raHighlighted.contains( p, _gc ) )
							{
								newRegion = true;
							}
						}

						if ( newRegion )
						{
							raHighlighted = ra.copy( );
							toggleHighlight( ra );
							break LOOP;
						}
						break;

					case ActionType.CALL_BACK :
						if ( iun instanceof ICallBackNotifier )
						{
							final CallBackValue cv = (CallBackValue) ac.getValue( );
							( (ICallBackNotifier) iun ).callback( event,
									ra.getSource( ),
									cv );
						}
						else
						{
							logger.log( ILogger.WARNING,
									Messages.getString( "SwtEventHandler.info.improper.callback.notifier", //$NON-NLS-1$
											new Object[]{
												iun
											},
											lcl ) );
						}
						break LOOP;
				}
			}
		}

		if ( !bFound && raTooltip != null )
		{
			hideTooltip( );
			raTooltip = null;
		}

		if ( ( cleanState || !bFound ) && raHighlighted != null )
		{
			raHighlighted.dispose( );
			raHighlighted = null;
		}

	}

	// private final boolean invert( Series seDT )
	// {
	// boolean changed = false;
	// for ( Iterator itr = seDT.eAllContents( ); itr.hasNext( ); )
	// {
	// Object obj = itr.next( );
	//
	// if ( obj instanceof ColorDefinition )
	// {
	// ( (ColorDefinition) obj ).invert( );
	//
	// changed = true;
	// }
	// }
	//
	// return changed;
	// }

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

		Series seDT = null;

		final Chart cmRT = iun.getRunTimeModel( );
		final Chart cmDT = iun.getDesignTimeModel( );

		if ( cmDT instanceof ChartWithAxes )
		{
			final ChartWithAxes cwaRT = (ChartWithAxes) cmRT;
			final ChartWithAxes cwaDT = (ChartWithAxes) cmDT;

			Axis[] axaBase = cwaRT.getPrimaryBaseAxes( );
			Axis axBase = axaBase[0];
			Axis[] axaOrthogonal = cwaRT.getOrthogonalAxes( axBase, true );
			EList elSD, elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = axaBase[0].getSeriesDefinitions( );
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

			if ( !bFound )
			{
				// LOCATE INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN RUN TIME
				// MODEL
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
			}

			if ( !bFound )
			{
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.OUT_OF_SYNC,
						"SwtEventHandler.info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						Messages.getResourceBundle( lcl ) );
			}

			// MAP TO INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN DESIGN TIME
			// MODEL
			axaBase = cwaDT.getPrimaryBaseAxes( );
			axBase = axaBase[0];
			axaOrthogonal = cwaDT.getOrthogonalAxes( axBase, true );
			if ( i == -1 )
			{
				elSD = axaBase[0].getSeriesDefinitions( );
			}
			else
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
			}
			sd = (SeriesDefinition) elSD.get( j );
			elSE = sd.getSeries( );
			seDT = (Series) elSE.get( k );
		}
		else if ( cmDT instanceof ChartWithoutAxes )
		{
			final ChartWithoutAxes cwoaRT = (ChartWithoutAxes) cmRT;
			final ChartWithoutAxes cwoaDT = (ChartWithoutAxes) cmDT;

			EList elSD, elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = cwoaRT.getSeriesDefinitions( );
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

			if ( !bFound )
			{
				i = 1;
				elSD = ( (SeriesDefinition) cwoaRT.getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );

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
			}

			if ( !bFound )
			{
				throw new ChartException( ChartDeviceSwtActivator.ID,
						ChartException.OUT_OF_SYNC,
						"SwtEventHandler.info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						Messages.getResourceBundle( lcl ) );
			}

			if ( i == -1 )
			{
				elSD = cwoaDT.getSeriesDefinitions( );
			}
			else
			{
				elSD = ( (SeriesDefinition) cwoaDT.getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );
			}
			sd = (SeriesDefinition) elSD.get( j );
			elSE = sd.getSeries( );
			seDT = (Series) elSE.get( k );
		}

		return seDT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE DOUBLE CLICK ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONDBLCLICK_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE DOWN ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONMOUSEDOWN_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE UP/CLICK ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.ONMOUSEUP_LITERAL,
				TriggerCondition.ONCLICK_LITERAL,
				TriggerCondition.MOUSE_CLICK_LITERAL,
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove( MouseEvent e )
	{
		// 1. CHECK FOR MOUSE-CLICK TRIGGERS
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONCLICK_LITERAL,
			TriggerCondition.ONMOUSEDOWN_LITERAL,
			TriggerCondition.MOUSE_CLICK_LITERAL
		} );

		if ( al != null )
		{
			RegionAction ra;

			// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
			boolean bFound = false;
			for ( int i = 0; i < al.size( ); i++ )
			{
				ra = (RegionAction) al.get( i );
				if ( ra.contains( e.x, e.y, _gc ) )
				{
					( (Composite) iun.peerInstance( ) ).setCursor( hand_cursor );
					bFound = true;
					break;
				}
			}

			if ( !bFound )
			{
				( (Composite) iun.peerInstance( ) ).setCursor( null );
			}
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE MOVE/OVER ONLY
		al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.ONMOUSEMOVE_LITERAL,
				TriggerCondition.ONMOUSEOVER_LITERAL
		} );

		handleAction( al, e, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed( KeyEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR MOUSE CLICKS ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONKEYDOWN_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased( KeyEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR KEY UP/PRESS ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.ONKEYUP_LITERAL,
				TriggerCondition.ONKEYPRESS_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseEnter( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseExit( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover( MouseEvent e )
	{
	}

	private final void toggleHighlight( RegionAction ra )
	{
		if ( ra == null )
		{
			return;
		}

		final StructureSource src = ra.getSource( );

		if ( src.getType( ) == StructureType.SERIES
				|| src.getType( ) == StructureType.SERIES_DATA_POINT )
		{
			final Series seRT;
			if ( src.getType( ) == StructureType.SERIES )
			{
				seRT = (Series) src.getSource( );
			}
			else
			{
				seRT = (Series) ( (WrappedStructureSource) src ).getParent( )
						.getSource( );
			}
			logger.log( ILogger.INFORMATION,
					Messages.getString( "SwtEventHandler.info.toggle.visibility", //$NON-NLS-1$
							lcl ) + seRT );
			Series seDT = null;
			SeriesDefinition sdDT = null;
			try
			{
				seDT = findDesignTimeSeries( seRT ); // LOCATE
				if ( seDT.eContainer( ) instanceof SeriesDefinition )
				{
					sdDT = (SeriesDefinition) seDT.eContainer( );
				}
			}
			catch ( ChartException oosx )
			{
				logger.log( oosx );
				return;
			}

			boolean highlight = iun.getContext( seDT ) == null;

			boolean changed = false;
			if ( seDT != null )
			{
				changed = performHighlight( seDT, highlight );
			}
			if ( sdDT != null )
			{
				for ( Iterator itr = sdDT.getSeriesPalette( )
						.getEntries( )
						.iterator( ); itr.hasNext( ); )
				{
					Object entry = itr.next( );
					if ( entry instanceof ColorDefinition )
					{
						performHighlight( (ColorDefinition) entry, highlight );
						changed = true;
					}
				}
			}

			if ( highlight )
			{
				if ( iun.getContext( seDT ) == null )
				{
					iun.putContext( seDT, Boolean.TRUE );
				}
			}
			else
			{
				iun.removeContext( seDT );
			}

			if ( changed )
			{
				iun.regenerateChart( );
			}
		}
	}

	private boolean performHighlight( Series se, boolean highlighted )
	{
		boolean changed = false;

		List lineContext;

		if ( highlighted )
		{
			lineContext = new ArrayList( );
		}
		else
		{
			Object context = iun.getContext( se );

			if ( context instanceof List )
			{
				lineContext = (List) context;
			}
			else
			{
				lineContext = new ArrayList( );
			}
		}

		int idx = 0;

		for ( Iterator itr = se.eAllContents( ); itr.hasNext( ); )
		{
			Object obj = itr.next( );

			if ( obj instanceof ColorDefinition
					&& !( ( (ColorDefinition) obj ).eContainer( ) instanceof LineAttributes ) )
			{
				performHighlight( (ColorDefinition) obj, highlighted );

				changed = true;
			}
			else if ( obj instanceof LineAttributes )
			{
				LineAttributes la = (LineAttributes) obj;

				if ( highlighted )
				{
					int[] ls = new int[5];
					ls[0] = la.getThickness( );
					ls[1] = la.getColor( ).getRed( );
					ls[2] = la.getColor( ).getGreen( );
					ls[3] = la.getColor( ).getBlue( );
					ls[4] = la.getColor( ).getTransparency( );

					lineContext.add( ls );

					la.setThickness( 3 );
					la.getColor( ).set( 255, 255, 255, 127 );

					changed = true;
				}
				else
				{
					if ( idx < lineContext.size( ) )
					{
						Object context = lineContext.get( idx );

						if ( context instanceof int[]
								&& ( (int[]) context ).length > 4 )
						{
							int[] ls = (int[]) context;

							la.setThickness( ls[0] );
							la.getColor( ).setRed( ls[1] );
							la.getColor( ).setGreen( ls[2] );
							la.getColor( ).setBlue( ls[3] );
							la.getColor( ).setTransparency( ls[4] );

							changed = true;
						}
					}
				}

				idx++;
			}
		}

		if ( highlighted && lineContext.size( ) > 0 )
		{
			iun.putContext( se, lineContext );
		}

		return changed;
	}

	private void performHighlight( ColorDefinition cd, boolean highlighted )
	{
		if ( cd != null )
		{
			if ( highlighted )
			{
				cd.setRed( ( cd.getRed( ) + 255 ) / 2 );
				cd.setGreen( ( cd.getGreen( ) + 255 ) / 2 );
				cd.setBlue( ( cd.getBlue( ) + 255 ) / 2 );
			}
			else
			{
				cd.setRed( Math.max( 0, cd.getRed( ) * 2 - 255 ) );
				cd.setGreen( Math.max( 0, cd.getGreen( ) * 2 - 255 ) );
				cd.setBlue( Math.max( 0, cd.getBlue( ) * 2 - 255 ) );
			}
		}
	}

	private final void hideTooltip( )
	{
		( (Composite) iun.peerInstance( ) ).setToolTipText( null );
	}

	private final void showTooltip( RegionAction ra )
	{
		Action ac = ra.getAction( );
		TooltipValue tv = (TooltipValue) ac.getValue( );
		String s = tv.getText( );

		( (Composite) iun.peerInstance( ) ).setToolTipText( s );
	}

	public final void dispose( )
	{
		if ( raHighlighted != null )
		{
			raHighlighted.dispose( );
			raHighlighted = null;
		}

		hand_cursor.dispose( );
		_gc.dispose( );
	}
}
