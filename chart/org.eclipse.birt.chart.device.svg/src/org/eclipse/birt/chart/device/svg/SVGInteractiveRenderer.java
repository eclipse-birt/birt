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

package org.eclipse.birt.chart.device.svg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.util.ScriptUtil;
import org.eclipse.birt.chart.device.svg.i18n.Messages;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AccessibilityValue;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.util.EList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ibm.icu.util.ULocale;

/**
 * This is an internal class used by SVGRendererImpl to add interactivity in the
 * SVG output
 */
public class SVGInteractiveRenderer
{

	private Map labelPrimitives = new Hashtable( );
	private List scripts = new Vector( );
	/**
	 * Element that represents the hot spot layer
	 */
	protected Element hotspotLayer;
	private Map componentPrimitives = new Hashtable( );
	private IUpdateNotifier _iun;
	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.svg/trace" ); //$NON-NLS-1$
	SVGGraphics2D svg_g2d;
	private ULocale locale;
	private List cacheEvents = new ArrayList( );

	public SVGInteractiveRenderer( ULocale locale )
	{
		if ( locale == null )
			this.locale = ULocale.getDefault( );
		else
			this.locale = locale;
	}

	public void setIUpdateNotifier( IUpdateNotifier iun )
	{
		this._iun = iun;
	}

	public void setSVG2D( SVGGraphics2D svg2D )
	{
		this.svg_g2d = svg2D;
	}

	/**
	 * Groups the svg drawing instructions that represents this primitive
	 * events. Each group is assigned an id that identifies the source object of
	 * the primitive event
	 * 
	 * @param pre
	 *            primitive render event
	 * @param drawText
	 *            TODO
	 */
	protected void groupPrimitive( PrimitiveRenderEvent pre, boolean drawText )
	{
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "exception.missing.component.interaction", getULocale( ) ) ); //$NON-NLS-1$
			return;
		}

		// For now only group series elements
		if ( pre.getSource( ) instanceof StructureSource )
		{
			StructureSource sourceObject = (StructureSource) pre.getSource( );
			Series seDT = (Series) getElementFromSource( sourceObject,
					StructureType.SERIES );
			if ( seDT != null )
			{
				String groupIdentifier = null;
				try
				{
					// Depending on legend coloring, we group differently
					if ( isColoredByCategories( )
							&& getElementFromSource( sourceObject,
									StructureType.SERIES_DATA_POINT ) != null )
					{
						seDT = findCategorySeries( seDT );
						groupIdentifier = String.valueOf( seDT.hashCode( ) );
						// Group by categories
						DataPointHints dph = (DataPointHints) getElementFromSource( sourceObject,
								StructureType.SERIES_DATA_POINT );
						groupIdentifier += "index"; //$NON-NLS-1$
						groupIdentifier += dph.getIndex( );
					}
					else
					{
						seDT = findDesignTimeSeries( seDT );
						groupIdentifier = String.valueOf( seDT.hashCode( ) );
					}
				}
				catch ( ChartException e )
				{
					logger.log( e );
					return;
				}
				
				if ( drawText )
				{
					String id = Integer.toString( pre.hashCode( ) );
					List components = (List) labelPrimitives.get( seDT );
					if ( components == null )
					{
						components = new ArrayList( );
						labelPrimitives.put( seDT, components );
					}

					components.add( id );

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element outerGroup = svg_g2d.createElement( "g" ); //$NON-NLS-1$
					svg_g2d.pushParent( outerGroup );

					Element primGroup = svg_g2d.createElement( "g" ); //$NON-NLS-1$
					outerGroup.appendChild( primGroup );
					svg_g2d.pushParent( primGroup );
					primGroup.setAttribute( "id", groupIdentifier + "_" + id ); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute( "style", "visibility:visible;" ); //$NON-NLS-1$ //$NON-NLS-2$
					outerGroup.setAttribute( "id", groupIdentifier + "_" + id + "_g" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					outerGroup.setAttribute( "style", "visibility:visible;" ); //$NON-NLS-1$ //$NON-NLS-2$
				}
				else
				// Non-text
				{
					String id = Integer.toString( pre.hashCode( ) );
					List components = (List) componentPrimitives.get( seDT );
					if ( components == null )
					{
						components = new ArrayList( );
						componentPrimitives.put( seDT, components );
					}

					// May have to group drawing instructions that come from
					// the same primitive render events.
					String idTemp = id;
					if ( components.size( ) > 0 )
					{
						idTemp = id + "@" + components.size( ); //$NON-NLS-1$
					}

					components.add( idTemp );

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element primGroup = svg_g2d.createElement( "g" ); //$NON-NLS-1$
					svg_g2d.pushParent( primGroup );
					primGroup.setAttribute( "id", groupIdentifier + "_" + idTemp ); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute( "style", "visibility:visible;" ); //$NON-NLS-1$ //$NON-NLS-2$

					svg_g2d.setDeferStrokColor( primGroup );
				}
			}
			else
			{
				Object designObject = null;
				// check to see if this is the title block
				if ( getElementFromSource( sourceObject, StructureType.TITLE ) != null )
				{
					designObject = sourceObject.getSource( );
				}
				else if ( getElementFromSource( sourceObject,
						StructureType.CHART_BLOCK ) != null )
				{
					designObject = sourceObject.getSource( );
				}
				else if ( getElementFromSource( sourceObject,
						StructureType.PLOT ) != null )
				{
					designObject = sourceObject.getSource( );
				}
				else if ( getElementFromSource( sourceObject,
						StructureType.AXIS ) != null )
				{
					designObject = sourceObject.getSource( );
				}
				if ( designObject != null )
				{
					String groupIdentifier = String.valueOf( designObject.hashCode( ) );
					String id = Integer.toString( pre.hashCode( ) );
					List components = (List) componentPrimitives.get( designObject );
					if ( components == null )
					{
						components = new ArrayList( );
						componentPrimitives.put( designObject, components );
					}

					// May have to group drawing instructions that come from
					// the same primitive render events.
					String idTemp = id;
					if ( components.size( ) > 0 )
					{
						idTemp = id + "@" + components.size( ); //$NON-NLS-1$
					}

					components.add( idTemp );

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element primGroup = svg_g2d.createElement( "g" ); //$NON-NLS-1$
					svg_g2d.pushParent( primGroup );
					primGroup.setAttribute( "id", groupIdentifier + "_" + idTemp ); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute( "style", "visibility:visible;" ); //$NON-NLS-1$ //$NON-NLS-2$	
					svg_g2d.setDeferStrokColor( primGroup );
				}
			}
		}
	}

	/**
	 * UnGroups the svg drawing instructions that represents this primitive
	 * events.
	 * 
	 * @param pre
	 *            primitive render event
	 * @param drawText
	 *            TODO
	 */
	protected void ungroupPrimitive( PrimitiveRenderEvent pre, boolean drawText )
	{
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "exception.missing.component.interaction", getULocale( ) ) ); //$NON-NLS-1$
			return;
		}

		// For now only ungroup series elements
		if ( pre.getSource( ) instanceof StructureSource )
		{
			StructureSource sourceObject = (StructureSource) pre.getSource( );
			final Series series = (Series) getElementFromSource( sourceObject,
					StructureType.SERIES );
			if ( series != null )
			{
				if ( drawText )
				{
					svg_g2d.popParent( );
					svg_g2d.popParent( );
				}
				else
				{
					svg_g2d.setDeferStrokColor( null );
					svg_g2d.popParent( );
				}
			}
			else
			{
				// check to see if this is the title block
				if ( ( getElementFromSource( sourceObject, StructureType.TITLE ) != null )
						|| ( getElementFromSource( sourceObject,
								StructureType.AXIS ) != null )
						|| ( getElementFromSource( sourceObject,
								StructureType.CHART_BLOCK ) != null )
						|| ( getElementFromSource( sourceObject,
								StructureType.PLOT ) != null ) )
				{
					svg_g2d.setDeferStrokColor( null );
					svg_g2d.popParent( );
				}
			}

		}
	}

	/**
	 * Helper function that will determine if the source object is a series
	 * component of the chart.
	 * 
	 * @param src
	 *            StructureSource that is stored in the primitive render event.
	 * @return true if the object or its parent is a series component.
	 */
	private Object getElementFromSource( StructureSource src, StructureType type )
	{
		if ( src instanceof WrappedStructureSource )
		{
			WrappedStructureSource wss = (WrappedStructureSource) src;
			while ( wss != null )
			{
				if ( wss.getType( ) == type )
				{
					return wss.getSource( );
				}
				if ( wss.getParent( ).getType( ) == type )
				{
					return wss.getParent( ).getSource( );
				}
				if ( wss.getParent( ) instanceof WrappedStructureSource )
					wss = (WrappedStructureSource) wss.getParent( );
				else
					wss = null;
			}
		}
		else if ( src.getType( ) == type )
			return src.getSource( );
		return null;
	}

	/**
	 * Locates a category design-time series corresponding to a given cloned
	 * run-time series.
	 * 
	 * @param seDT
	 *            runtime Series
	 * @return category Series
	 */
	private Series findCategorySeries( Series seDT )
	{
		final Chart cmDT = _iun.getDesignTimeModel( );
		if ( cmDT instanceof ChartWithAxes )
		{
			return (Series) ( (SeriesDefinition) ( (ChartWithAxes) cmDT ).getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 ) ).getRunTimeSeries( ).get( 0 );
		}
		else
		{
			return (Series) ( (SeriesDefinition) ( (ChartWithoutAxes) cmDT ).getSeriesDefinitions( )
					.get( 0 ) ).getRunTimeSeries( ).get( 0 );
		}
	}

	/**
	 * Prepare event handling
	 */
	public void prepareInteractiveEvent( Element elm, InteractionEvent ie,
			Trigger[] triggers )
	{
		// Cache events to make sure the groups are complete
		cacheEvents.add( new CacheEvent( elm,
				ie.getStructureSource( ),
				triggers ) );
	}

	/**
	 * Process events that have been prepared and apply them to the SVG Elements
	 */
	public void addInteractivity( )
	{
		for ( Iterator iter = cacheEvents.iterator( ); iter.hasNext( ); )
		{
			CacheEvent cEvent = ( (CacheEvent) iter.next( ) );
			addEventHandling( cEvent.getElement( ),
					cEvent.getSource( ),
					cEvent.getTriggers( ) );
		}
	}

	/**
	 * Add event handling to the hotspot
	 */
	private void addEventHandling( Element elm, StructureSource src,
			Trigger[] triggers )
	{
		if ( elm != null )
		{
			// Need to check if we have a url redirect trigger. We handle the
			// interaction differently
			boolean redirect = false;
			for ( int x = 0; x < triggers.length; x++ )
			{
				Trigger tg = triggers[x];
				if ( tg.getAction( ).getType( ).getValue( ) == ActionType.URL_REDIRECT )
				{
					redirect = true;
					break;
				}
			}
			if ( redirect )
			{
				Element aLink = svg_g2d.createElement( "a" ); //$NON-NLS-1$
				Element group = svg_g2d.createElement( "g" ); //$NON-NLS-1$
				group.appendChild( elm );
				// Create empty href
				aLink.setAttribute( "xlink:href", "" ); //$NON-NLS-1$ //$NON-NLS-2$
				aLink.appendChild( group );
				elm = group;
				hotspotLayer.appendChild( aLink );
			}
			else
				hotspotLayer.appendChild( elm );

			for ( int x = 0; x < triggers.length; x++ )
			{
				Trigger tg = triggers[x];

				String scriptEvent = getJsScriptEvent( tg.getCondition( )
						.getValue( ) );
				if ( scriptEvent != null )
				{

					switch ( tg.getAction( ).getType( ).getValue( ) )
					{
						case ActionType.SHOW_TOOLTIP :
							String tooltipText = ( (TooltipValue) tg.getAction( )
									.getValue( ) ).getText( );
							// make sure the tooltip text is not empty
							if ( ( tooltipText != null )
									&& ( tooltipText.trim( ).length( ) > 0 ) )
							{
								Element title = svg_g2d.dom.createElement( "title" ); //$NON-NLS-1$
								title.appendChild( svg_g2d.dom.createTextNode( tooltipText ) );
								elm.appendChild( title );
								// on mouse over is actually two events to show
								// the tooltip
								if ( scriptEvent.equals( "onmouseover" ) ) {//$NON-NLS-1$
									elm.setAttribute( "onmouseout", "TM.remove()" ); //$NON-NLS-1$ //$NON-NLS-2$
									elm.setAttribute( "onmousemove", "TM.show(evt)" ); //$NON-NLS-1$ //$NON-NLS-2$
								}
								else
									elm.setAttribute( scriptEvent,
											"TM.toggleToolTip(evt)" ); //$NON-NLS-1$ 
							}
							break;
						case ActionType.URL_REDIRECT :
							URLValue urlValue = ( (URLValue) tg.getAction( )
									.getValue( ) );
							// See if this is an internal anchor link
							if ( urlValue.getBaseUrl( ).startsWith( "#" ) ) { //$NON-NLS-1$
								elm.setAttribute( scriptEvent,
										"top.document.location.hash='" + urlValue.getBaseUrl( ) + "';" ); //$NON-NLS-1$ //$NON-NLS-2$ 
								elm.setAttribute( "style", "cursor:pointer" ); //$NON-NLS-1$ //$NON-NLS-2$
							}
							// check if this is a javascript call
							else if ( urlValue.getBaseUrl( ).startsWith( "javascript:" ) ) //$NON-NLS-1$
							{
								elm.setAttribute( scriptEvent, urlValue.getBaseUrl( ) );  
								elm.setAttribute( "style", "cursor:pointer" ); //$NON-NLS-1$ //$NON-NLS-2$
							}
							else
							{
								String target = urlValue.getTarget( );
								if ( target == null )
									target = "null"; //$NON-NLS-1$
								elm.setAttribute( scriptEvent,
										"redirect('" + target + "','" + urlValue.getBaseUrl( ) + "')" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

							}
							break;

						case ActionType.TOGGLE_VISIBILITY :
						case ActionType.TOGGLE_DATA_POINT_VISIBILITY :
						case ActionType.HIGHLIGHT :
							addJSCodeOnElement( src,
									tg,
									elm,
									scriptEvent,
									tg.getAction( ).getType( ).getValue( ) );
							break;

						case ActionType.INVOKE_SCRIPT :

							// lets see if we need to add accessibility
							if ( tg.getCondition( )
									.equals( TriggerCondition.ACCESSIBILITY_LITERAL ) )
							{
								AccessibilityValue accessValue = ( (AccessibilityValue) tg.getAction( )
										.getValue( ) );
								if ( accessValue.getText( ) != null )
								{
									Element title = svg_g2d.createElement( "title" ); //$NON-NLS-1$
									title.appendChild( svg_g2d.dom.createTextNode( accessValue.getText( ) ) );
									elm.appendChild( title );
								}
								if ( accessValue.getAccessibility( ) != null )
								{
									Element description = svg_g2d.createElement( "desc" ); //$NON-NLS-1$
									description.appendChild( svg_g2d.dom.createTextNode( accessValue.getAccessibility( ) ) );
									elm.appendChild( description );
								}

							}
							else
							{
								// Add categoryData, valueData, valueSeriesName
								// in callback
								String script = ( (ScriptValue) tg.getAction( )
										.getValue( ) ).getScript( );

								String callbackFunction = "callback" //$NON-NLS-1$
										+ Math.abs( script.hashCode( ) )
										+ "(evt," + src.getSource( ).hashCode( );//$NON-NLS-1$ 

								if ( StructureType.SERIES_DATA_POINT.equals( src.getType( ) ) )
								{
									final DataPointHints dph = (DataPointHints) src.getSource( );
									callbackFunction += ","; //$NON-NLS-1$
									callbackFunction = ScriptUtil.script( callbackFunction,
											dph );
								}
								callbackFunction += ");"; //$NON-NLS-1$
								elm.setAttribute( scriptEvent, callbackFunction );
								setCursor( elm );
								if ( !( scripts.contains( script ) ) )
								{
									svg_g2d.addScript( "function callback" + Math.abs( script.hashCode( ) ) + "(evt,source,categoryData,valueData,valueSeriesName)" + "{" + script + "}" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
									scripts.add( script );
								}
							}
							break;
					}
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

		Series seDT = null;

		final Chart cmRT = _iun.getRunTimeModel( );
		final Chart cmDT = _iun.getDesignTimeModel( );

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
				// locate indexes for axis/seriesdefinition/series in runtime
				// model
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
				// TODO change ResourceBundle to use ICU class
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						org.eclipse.birt.chart.device.extension.i18n.Messages.getResourceBundle( getULocale( ) ) );
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
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						org.eclipse.birt.chart.device.extension.i18n.Messages.getResourceBundle( getULocale( ) ) );
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

	private String getJsScriptEvent( int condition )
	{
		switch ( condition )
		{
			case TriggerCondition.MOUSE_HOVER :
				return "onmouseover"; //$NON-NLS-1$
			case TriggerCondition.MOUSE_CLICK :
				return "onclick"; //$NON-NLS-1$
			case TriggerCondition.ONCLICK :
				return "onclick"; //$NON-NLS-1$
			case TriggerCondition.ONDBLCLICK :
				return "onclick"; //$NON-NLS-1$
			case TriggerCondition.ONMOUSEDOWN :
				return "onmousedown"; //$NON-NLS-1$
			case TriggerCondition.ONMOUSEUP :
				return "onmouseup"; //$NON-NLS-1$
			case TriggerCondition.ONMOUSEOVER :
				return "onmouseover"; //$NON-NLS-1$
			case TriggerCondition.ONMOUSEMOVE :
				return "onmousemove"; //$NON-NLS-1$
			case TriggerCondition.ONMOUSEOUT :
				return "onmouseout"; //$NON-NLS-1$
			case TriggerCondition.ONFOCUS :
				return "onfocusin"; //$NON-NLS-1$
			case TriggerCondition.ONBLUR :
				return "onfocusout"; //$NON-NLS-1$
			case TriggerCondition.ONKEYDOWN :
				return "onkeydown"; //$NON-NLS-1$
			case TriggerCondition.ONKEYPRESS :
				return "onkeypress"; //$NON-NLS-1$
			case TriggerCondition.ONKEYUP :
				return "onkeyup"; //$NON-NLS-1$
			case TriggerCondition.ONLOAD :
				return "onload"; //$NON-NLS-1$
		}
		return null;

	}

	protected void setCursor( Element currentElement )
	{
		String style = currentElement.getAttribute( "style" ); //$NON-NLS-1$
		if ( style == null )
		{
			style = ""; //$NON-NLS-1$
		}
		currentElement.setAttribute( "style", style + "cursor:pointer;" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void clear( )
	{
		labelPrimitives.clear( );
		componentPrimitives.clear( );
		scripts.clear( );
	}

	public Node getHotspotLayer( )
	{
		return hotspotLayer;
	}

	public void createHotspotLayer( Document dom )
	{
		hotspotLayer = dom.createElement( "g" ); //$NON-NLS-1$
		hotspotLayer.setAttribute( "id", "hotSpots" ); //$NON-NLS-1$ //$NON-NLS-2$ 
		hotspotLayer.setAttribute( "style", "fill-opacity:0.01;fill:#FFFFFF;" ); //$NON-NLS-1$ //$NON-NLS-2$ 
	}

	private ULocale getULocale( )
	{
		return locale;
	}

	private boolean isColoredByCategories( )
	{
		return this._iun.getRunTimeModel( ).getLegend( ).getItemType( ) == LegendItemType.CATEGORIES_LITERAL;
	}

	private void addJSCodeOnElement( StructureSource src, Trigger tg,
			Element elm, String scriptEvent, int type )
	{
		final Series seRT = (Series) getElementFromSource( src,
				StructureType.SERIES );
		if ( seRT != null )
		{
			Series seDT = null;
			String groupIdentifier = null;

			// Create Group identifiers. Differs for color by categories or
			// series
			if ( isColoredByCategories( ) )
			{
				seDT = findCategorySeries( seRT );
				final int baseIndex = ( (DataPointHints) src.getSource( ) ).getIndex( );
				StringBuffer sb = new StringBuffer( );
				sb.append( "'" );//$NON-NLS-1$
				sb.append( seDT.hashCode( ) );
				sb.append( "index" ); //$NON-NLS-1$
				sb.append( baseIndex );
				sb.append( "'" );//$NON-NLS-1$
				groupIdentifier = sb.toString( );
			}
			else
			{
				try
				{
					seDT = findDesignTimeSeries( seRT );
				}
				catch ( ChartException e )
				{
					logger.log( e );
					return;
				}
				groupIdentifier = String.valueOf( seDT.hashCode( ) );
			}
			boolean includeLabels = false;
			boolean includeGraphics = false;

			String jsFunction = null;
			switch ( type )
			{
				case ActionType.TOGGLE_VISIBILITY :
					jsFunction = "toggleVisibility(evt, "; //$NON-NLS-1$
					includeLabels = true;
					includeGraphics = true;
					break;
				case ActionType.TOGGLE_DATA_POINT_VISIBILITY :
					jsFunction = "toggleLabelsVisibility(evt, "; //$NON-NLS-1$
					includeLabels = true;
					includeGraphics = false;
					break;
				case ActionType.HIGHLIGHT :
					jsFunction = "highlight(evt, "; //$NON-NLS-1$
					includeLabels = true;
					includeGraphics = true;
					break;
			}
			if ( jsFunction == null )
			{
				assert false;
				return;
			}

			if ( includeGraphics || includeLabels )
			{
				StringBuffer sb = new StringBuffer( );
				sb.append( groupIdentifier );

				sb.append( ",new Array(" ); //$NON-NLS-1$

				List labelComponents = (List) labelPrimitives.get( seDT );
				List components = (List) componentPrimitives.get( seDT );

				Iterator iter = null;
				// Apply action to graphics
				if ( includeGraphics && components != null )
				{
					iter = components.iterator( );
					appendArguments( sb, iter );
					if ( includeLabels && labelComponents != null )
					{
						sb.append( "), new Array(" ); //$NON-NLS-1$
					}
				}
				// Apply action to labels
				if ( includeLabels && labelComponents != null )
				{
					iter = labelComponents.iterator( );
					appendArguments( sb, iter );
				}

				sb.append( ")" ); //$NON-NLS-1$

				elm.setAttribute( scriptEvent, jsFunction
						+ sb.toString( )
						+ ")" ); //$NON-NLS-1$		

				if ( tg.getCondition( ).getValue( ) == TriggerCondition.ONMOUSEOVER )
				{
					elm.setAttribute( "onmouseout", //$NON-NLS-1$
							jsFunction + sb.toString( ) + ")" ); //$NON-NLS-1$		
				}
				setCursor( elm );
			}
		}
		else
		{
			// the source is not a series object. It may be a title, plot area
			// or axis
			Object designObject = null;
			// check to see if this is the title block
			if ( getElementFromSource( src, StructureType.TITLE ) != null )
			{
				designObject = src.getSource( );
			}
			else if ( getElementFromSource( src, StructureType.PLOT ) != null )
			{
				designObject = src.getSource( );
			}
			else if ( getElementFromSource( src, StructureType.CHART_BLOCK ) != null )
			{
				designObject = src.getSource( );
			}
			else if ( getElementFromSource( src, StructureType.AXIS ) != null )
			{
				designObject = src.getSource( );
			}
			if ( designObject != null )
			{
				String jsFunction = null;
				switch ( type )
				{
					case ActionType.TOGGLE_VISIBILITY :
						jsFunction = "toggleVisibility(evt, "; //$NON-NLS-1$
						break;
					case ActionType.HIGHLIGHT :
						jsFunction = "highlight(evt, ";//$NON-NLS-1$
						break;
				}
				if ( jsFunction == null )
				{
					assert false;
					return;
				}

				List components = (List) componentPrimitives.get( designObject );

				Iterator iter = null;
				// Apply action to graphics
				if ( components != null )
				{
					String groupIdentifier = String.valueOf( designObject.hashCode( ) );
					StringBuffer sb = new StringBuffer( );
					sb.append( groupIdentifier );

					sb.append( ",new Array(" ); //$NON-NLS-1$
					iter = components.iterator( );
					appendArguments( sb, iter );

					sb.append( ")" ); //$NON-NLS-1$

					elm.setAttribute( scriptEvent, jsFunction
							+ sb.toString( )
							+ ")" ); //$NON-NLS-1$		

					if ( tg.getCondition( ).getValue( ) == TriggerCondition.ONMOUSEOVER )
					{
						elm.setAttribute( "onmouseout", //$NON-NLS-1$
								jsFunction + sb.toString( ) + ")" ); //$NON-NLS-1$		
					}
					setCursor( elm );
				}
			}

		}
	}

	private void appendArguments( StringBuffer sb, Iterator iter )
	{
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				sb.append( "'" ).append( iter.next( ) ).append( "'" ); //$NON-NLS-1$ //$NON-NLS-2$
				if ( iter.hasNext( ) )
					sb.append( "," ); //$NON-NLS-1$
			}
		}
	}
}
