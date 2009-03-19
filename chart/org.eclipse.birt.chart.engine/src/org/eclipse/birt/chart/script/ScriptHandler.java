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

package org.eclipse.birt.chart.script;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendEntryRenderingHints;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.icu.util.ULocale;

/**
 * This class forms the basis of script handling in the charting library. It
 * creates a default scope and/or subclasses from a predefined scope. It also
 * provides convenience methods for execution of functions defined in the scope.
 * 
 * @see IChartEventHandler
 */
public final class ScriptHandler extends ScriptableObject
{

	private static final long serialVersionUID = 1L;

	private static final Map<String, Method> JAVA_FUNTION_MAP = ChartUtil.newHashMap( );

	static
	{
		// init java function name lookup table.
		Method[] ms = SecurityUtil.getMethods( IChartEventHandler.class );

		for ( int i = 0; i < ms.length; i++ )
		{
			JAVA_FUNTION_MAP.put( ms[i].getName( ), ms[i] );
		}
	}

	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String START_GENERATION = "startGeneration"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String FINISH_GENERATION = "finishGeneration"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String BEFORE_LAYOUT = "beforeLayout"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String AFTER_LAYOUT = "afterLayout"; //$NON-NLS-1$

	public static final String BEFORE_COMPUTATIONS = "beforeComputations"; //$NON-NLS-1$

	public static final String AFTER_COMPUTATIONS = "afterComputations"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String START_RENDERING = "startRendering"; //$NON-NLS-1$	
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String FINISH_RENDERING = "finishRendering"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String START_COMPUTE_SERIES = "startComputeSeries"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String FINISH_COMPUTE_SERIES = "finishComputeSeries"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String BEFORE_DRAW_ELEMENT = "beforeDrawElement"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String AFTER_DRAW_ELEMENT = "afterDrawElement"; //$NON-NLS-1$

	// not supported yet
	// public static final String ON_PREPARE = "onPrepare"; //$NON-NLS-1$

	public static final String BEFORE_DATA_SET_FILLED = "beforeDataSetFilled"; //$NON-NLS-1$

	public static final String AFTER_DATA_SET_FILLED = "afterDataSetFilled"; //$NON-NLS-1$

	public static final String BEFORE_GENERATION = "beforeGeneration"; //$NON-NLS-1$

	public static final String AFTER_GENERATION = "afterGeneration"; //$NON-NLS-1$

	public static final String BEFORE_RENDERING = "beforeRendering"; //$NON-NLS-1$

	public static final String AFTER_RENDERING = "afterRendering"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_BLOCK = "beforeDrawBlock"; //$NON-NLS-1$

	public static final String AFTER_DRAW_BLOCK = "afterDrawBlock"; //$NON-NLS-1$

	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String BEFORE_DRAW_LEGEND_ENTRY = "beforeDrawLegendEntry"; //$NON-NLS-1$

	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String AFTER_DRAW_LEGEND_ENTRY = "afterDrawLegendEntry"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_LEGEND_ITEM = "beforeDrawLegendItem"; //$NON-NLS-1$

	public static final String AFTER_DRAW_LEGEND_ITEM = "afterDrawLegendItem"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_SERIES = "beforeDrawSeries"; //$NON-NLS-1$

	public static final String AFTER_DRAW_SERIES = "afterDrawSeries"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_SERIES_TITLE = "beforeDrawSeriesTitle"; //$NON-NLS-1$

	public static final String AFTER_DRAW_SERIES_TITLE = "afterDrawSeriesTitle"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_MARKER = "beforeDrawMarker"; //$NON-NLS-1$

	public static final String AFTER_DRAW_MARKER = "afterDrawMarker"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_MARKER_LINE = "beforeDrawMarkerLine"; //$NON-NLS-1$

	public static final String AFTER_DRAW_MARKER_LINE = "afterDrawMarkerLine"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_MARKER_RANGE = "beforeDrawMarkerRange"; //$NON-NLS-1$

	public static final String AFTER_DRAW_MARKER_RANGE = "afterDrawMarkerRange"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_DATA_POINT = "beforeDrawDataPoint"; //$NON-NLS-1$

	public static final String AFTER_DRAW_DATA_POINT = "afterDrawDataPoint"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_DATA_POINT_LABEL = "beforeDrawDataPointLabel"; //$NON-NLS-1$

	public static final String AFTER_DRAW_DATA_POINT_LABEL = "afterDrawDataPointLabel"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_FITTING_CURVE = "beforeDrawFittingCurve"; //$NON-NLS-1$

	public static final String AFTER_DRAW_FITTING_CURVE = "afterDrawFittingCurve"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_AXIS_LABEL = "beforeDrawAxisLabel"; //$NON-NLS-1$

	public static final String AFTER_DRAW_AXIS_LABEL = "afterDrawAxisLabel"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_AXIS_TITLE = "beforeDrawAxisTitle"; //$NON-NLS-1$

	public static final String AFTER_DRAW_AXIS_TITLE = "afterDrawAxisTitle"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access base category data.
	 */
	public static final String BASE_VALUE = "categoryData"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access orthogonal value data.
	 */
	public static final String ORTHOGONAL_VALUE = "valueData"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access series value data.
	 */
	public static final String SERIES_VALUE = "valueSeriesName"; //$NON-NLS-1$

	// PRE-DEFINED INSTANCES AVAILABLE FOR REUSE
	private final transient Object[] ONE_ELEMENT_ARRAY = new Object[1];

	private final transient Object[] TWO_ELEMENT_ARRAY = new Object[2];

	private final transient Object[] THREE_ELEMENT_ARRAY = new Object[3];

	private transient Scriptable scope = null;

	private transient IChartEventHandler javahandler = null;

	private transient Chart cmDesignTime = null;

	private transient Chart cmRunTime = null;

	private transient GeneratedChartState gcs = null;

	/**
	 * @deprecated locale is stored in IChartScriptContext
	 */
	private transient ULocale lcl = null;

	private transient IScriptClassLoader iscl = null;

	private transient List<String> javaScriptFunctionNamesCache = null;

	private IChartScriptContext csc;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/model" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public ScriptHandler( )
	{
		final Context cx = Context.enter( );
		try
		{
			// scope = cx.initStandardObjects();
			scope = new ImporterTopLevel( cx );
		}
		finally
		{
			Context.exit( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public final String getClassName( )
	{
		return getClass( ).getName( );
	}

	/**
	 * @return returns the scope of current JavaScript context.
	 */
	public final Scriptable getScope( )
	{
		return scope;
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility
	 *             only.
	 * @return An instance of the chart model used at design time
	 */
	public Object jsFunction_getDesignTimeModel( )
	{
		return cmDesignTime;
	}

	/**
	 * @deprecated Call IChartScriptContext.getChartInstance() instead. This is
	 *             kept for backward compatibility only.
	 * @return An instance of the chart model used at run time
	 */
	public Object jsFunction_getRunTimeModel( )
	{
		return cmRunTime;
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility
	 *             only.
	 * @return An instance of the run time model coupled with a computations and
	 *         series filled with datasets
	 */
	public Object jsFunction_getGeneratedChartState( )
	{
		return gcs;
	}

	/**
	 * @deprecated Call IChartScriptContext.getLocale() instead. This is kept
	 *             for backward compatibility only.
	 * @return The locale associated with the generation request
	 */
	public final Object jsFunction_getLocale( )
	{
		return lcl;
	}

	/**
	 * @deprecated Note used anymore. This is kept for backward compatibility
	 *             only.
	 * @param eo
	 *            An EMF generated model object to be cloned
	 * @return A cloned instance of the specified EMF generated model object
	 */
	public final Object jsFunction_clone( Object eo )
	{
		if ( !( eo instanceof EObject ) )
		{
			return null;
		}
		return EcoreUtil.copy( (EObject) eo );
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility
	 *             only.
	 * @param cm
	 */
	public final void setDesignTimeModel( Chart cmDesignTime )
	{
		this.cmDesignTime = cmDesignTime;
	}

	/**
	 * @deprecated Not used anymore. Use IChartScriptContext to store the
	 *             run-time model now. This is kept for backward compatibility
	 *             only.
	 * @param cm
	 */
	public final void setRunTimeModel( Chart cmRunTime )
	{
		this.cmRunTime = cmRunTime;
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility
	 *             only.
	 * @param gcs
	 */
	public final void setGeneratedChartState( GeneratedChartState gcs )
	{
		this.gcs = gcs;
	}

	/**
	 * @deprecated Not used anymore. Use IChartScriptContext to store the locale
	 *             now. This is kept for backward compatibility only.
	 * @param lcl
	 */
	public final void setLocale( ULocale lcl )
	{
		this.lcl = lcl;
	}

	/**
	 * Sets the script class loader. This loader is responsible to load all user
	 * defined script class.
	 * 
	 * @param value
	 */
	public final void setScriptClassLoader( IScriptClassLoader value )
	{
		iscl = value;
	}

	/**
	 * Initialize the JavaScript context using given parent scope.
	 * 
	 * @param scPrototype
	 *            Parent scope object. If it's null, use default scope.
	 */
	public final void init( Scriptable scPrototype ) throws ChartException
	{
		final Context cx = Context.enter( );
		try
		{
			if ( scPrototype == null ) // NO PROTOTYPE
			{
				// scope = cx.initStandardObjects();
				scope = new ImporterTopLevel( cx );
			}
			else
			{
				scope = cx.newObject( scPrototype );
				scope.setPrototype( scPrototype );
				// !don't reset the parent scope here.
				// scope.setParentScope( null );
			}

			// final Scriptable scopePrevious = scope;
			// !deprecated, remove this later. use script context instead.
			// registerExistingScriptableObject( this, "chart" ); //$NON-NLS-1$
			// scope = scopePrevious; // RESTORE

			// !deprecated, remove this later, use logger from script context
			// instead.
			// ADD LOGGING CAPABILITIES TO JAVASCRIPT ACCESS
			final Object oConsole = Context.javaToJS( logger, scope );
			scope.put( "logger", scope, oConsole ); //$NON-NLS-1$
		}
		catch ( RhinoException jsx )
		{
			throw convertException( jsx );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * Registers an existing scriptable object into current JavaScript context.
	 * 
	 * @param so
	 *            The existing scriptable object to be registered
	 * @param sVarName
	 *            The name of the javascript variable associated with the new
	 *            scriptable object that will be added to the scope
	 * @throws ChartException
	 */
	public final void registerExistingScriptableObject( ScriptableObject so,
			String sVarName ) throws ChartException
	{
		try
		{
			ScriptableObject.defineClass( scope, so.getClass( ) );
		}
		catch ( Exception ex )
		{
			throw convertException( ex );
		}

		final Context cx = Context.enter( );
		Scriptable soNew = null;
		try
		{
			soNew = cx.newObject( scope, so.getClassName( ), null );
		}
		catch ( RhinoException ex )
		{
			throw convertException( ex );
		}
		finally
		{
			Context.exit( );
		}
		so.setPrototype( soNew.getPrototype( ) );
		so.setParentScope( soNew.getParentScope( ) );
		scope.put( sVarName, scope, so );
	}

	/**
	 * Registers a new scriptable object into current JavaScript context.
	 * 
	 * @param clsScriptable
	 *            The class representing the new scriptable object to be
	 *            registered
	 * @param sVarName
	 *            The name of the javascript variable associated with the new
	 *            scriptable object that will be added to the scope
	 * @throws ChartException
	 */
	public final void registerNewScriptableObject(
			Class<? extends Scriptable> clsScriptable, String sVarName )
			throws ChartException
	{
		try
		{
			ScriptableObject.defineClass( scope, clsScriptable );
		}
		catch ( Exception ex )
		{
			throw convertException( ex );
		}

		final Context cx = Context.enter( );
		Scriptable soNew = null;
		try
		{
			soNew = cx.newObject( scope, clsScriptable.getName( ), null );
		}
		catch ( RuntimeException ex )
		{
			throw convertException( ex );
		}
		finally
		{
			Context.exit( );
		}
		scope.put( sVarName, scope, soNew );
	}

	/**
	 * Registers a new variable to current JavaScript context. If the name
	 * already exists, it'll be overwritten.
	 * 
	 * @param sVarName
	 * @throws ChartException
	 */
	public final void registerVariable( String sVarName, Object var )
			throws ChartException
	{
		Context.enter( );

		try
		{
			final Object oConsole = Context.javaToJS( var, scope );
			scope.put( sVarName, scope, oConsole );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * Unregister a variable from current JavaScript context.
	 * 
	 * @param sVarName
	 * @throws ChartException
	 */
	public final void unregisterVariable( String sVarName )
			throws ChartException
	{
		scope.delete( sVarName );
	}

	/**
	 * Finds the JavaScript funtion by given name.
	 * 
	 * @param sFunctionName
	 *            The name of the function to be searched for
	 * @return An instance of the function being searched for or null if it
	 *         isn't found
	 */
	private final Function getJavascriptFunction( String sFunctionName )
	{
		// TODO: CACHE PREVIOUSLY CREATED FUNCTION REFERENCES IN A HASHTABLE?

		// use names cache for quick validation to improve performance
		if ( javaScriptFunctionNamesCache == null
				|| javaScriptFunctionNamesCache.indexOf( sFunctionName ) < 0 )
		{
			return null;
		}

		Context.enter( );
		try
		{
			final Object oFunction = scope.get( sFunctionName, scope );
			if ( oFunction != Scriptable.NOT_FOUND
					&& oFunction instanceof Function )
			{
				return (Function) oFunction;
			}
			return null;
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * Call JavaScript functions with an argument array.
	 * 
	 * @param f
	 *            The function to be executed
	 * @param oaArgs
	 *            The Java object arguments passed to the function being
	 *            executed
	 */
	private final Object callJavaScriptFunction( Function f, Object[] oaArgs )
			throws ChartException

	{
		final Context cx = Context.enter( );
		Object oReturnValue = null;
		// #229402
		ClassLoader oldLoader = cx.getApplicationClassLoader( );
		ClassLoader appLader = SecurityUtil.getClassLoader( ScriptHandler.class );
		cx.setApplicationClassLoader( appLader );

		// Initialize BIRT functions, register them into current script context.
		new CoreJavaScriptInitializer( ).initialize( cx, scope );

		try
		{
			oReturnValue = f.call( cx, scope, scope, oaArgs );
		}
		catch ( RhinoException ex )
		{
			throw convertException( ex );
		}
		finally
		{
			cx.setApplicationClassLoader( oldLoader );
			Context.exit( );
		}
		return oReturnValue;
	}

	private final boolean isJavaFuntion( String name )
	{
		return JAVA_FUNTION_MAP.get( name ) != null;
	}

	private final Object callJavaFunction( String name, Object[] oaArgs )
	{
		if ( javahandler == null )
		{
			return null;
		}

		Object[] tmpArgs = new Object[3];
		if ( oaArgs.length > 0 )
		{
			tmpArgs[0] = oaArgs[0];
		}
		if ( oaArgs.length > 1 )
		{
			tmpArgs[1] = oaArgs[1];
		}
		if ( oaArgs.length > 2 )
		{
			tmpArgs[2] = oaArgs[2];
		}

		// use regular interface call instead of reflection to gain performance.
		/*
		 * if ( ScriptHandler.ON_PREPARE.equals( name ) ) {
		 * javahandler.onPrepare( (Chart) tmpArgs[0], (IChartScriptContext)
		 * tmpArgs[1] ); } else
		 */// not supported yet
		if ( ScriptHandler.BEFORE_DATA_SET_FILLED.equals( name ) )
		{
			javahandler.beforeDataSetFilled( (Series) tmpArgs[0],
					(IDataSetProcessor) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DATA_SET_FILLED.equals( name ) )
		{
			javahandler.afterDataSetFilled( (Series) tmpArgs[0],
					(DataSet) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_GENERATION.equals( name ) )
		{
			javahandler.beforeGeneration( (Chart) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.AFTER_GENERATION.equals( name ) )
		{
			javahandler.afterGeneration( (GeneratedChartState) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.BEFORE_RENDERING.equals( name ) )
		{
			javahandler.beforeRendering( (GeneratedChartState) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.AFTER_RENDERING.equals( name ) )
		{
			javahandler.afterRendering( (GeneratedChartState) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_BLOCK.equals( name ) )
		{
			javahandler.beforeDrawBlock( (Block) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.AFTER_DRAW_BLOCK.equals( name ) )
		{
			javahandler.afterDrawBlock( (Block) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY.equals( name ) )
		{
			javahandler.beforeDrawLegendEntry( (Label) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.AFTER_DRAW_LEGEND_ENTRY.equals( name ) )
		{
			javahandler.afterDrawLegendEntry( (Label) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_LEGEND_ITEM.equals( name ) )
		{
			javahandler.beforeDrawLegendItem( (LegendEntryRenderingHints) tmpArgs[0],
					(Bounds) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_LEGEND_ITEM.equals( name ) )
		{
			javahandler.afterDrawLegendItem( (LegendEntryRenderingHints) tmpArgs[0],
					(Bounds) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_SERIES.equals( name ) )
		{
			javahandler.beforeDrawSeries( (Series) tmpArgs[0],
					(ISeriesRenderer) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_SERIES.equals( name ) )
		{
			javahandler.afterDrawSeries( (Series) tmpArgs[0],
					(ISeriesRenderer) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_SERIES_TITLE.equals( name ) )
		{
			javahandler.beforeDrawSeriesTitle( (Series) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_SERIES_TITLE.equals( name ) )
		{
			javahandler.afterDrawSeriesTitle( (Series) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_MARKER_LINE.equals( name ) )
		{
			javahandler.beforeDrawMarkerLine( (Axis) tmpArgs[0],
					(MarkerLine) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_MARKER_LINE.equals( name ) )
		{
			javahandler.afterDrawMarkerLine( (Axis) tmpArgs[0],
					(MarkerLine) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_MARKER_RANGE.equals( name ) )
		{
			javahandler.beforeDrawMarkerRange( (Axis) tmpArgs[0],
					(MarkerRange) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_MARKER_RANGE.equals( name ) )
		{
			javahandler.afterDrawMarkerRange( (Axis) tmpArgs[0],
					(MarkerRange) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_DATA_POINT.equals( name ) )
		{
			javahandler.beforeDrawDataPoint( (DataPointHints) tmpArgs[0],
					(Fill) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_DATA_POINT.equals( name ) )
		{
			javahandler.afterDrawDataPoint( (DataPointHints) tmpArgs[0],
					(Fill) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL.equals( name ) )
		{
			javahandler.beforeDrawDataPointLabel( (DataPointHints) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL.equals( name ) )
		{
			javahandler.afterDrawDataPointLabel( (DataPointHints) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_FITTING_CURVE.equals( name ) )
		{
			javahandler.beforeDrawFittingCurve( (CurveFitting) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.AFTER_DRAW_FITTING_CURVE.equals( name ) )
		{
			javahandler.afterDrawFittingCurve( (CurveFitting) tmpArgs[0],
					(IChartScriptContext) tmpArgs[1] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_AXIS_LABEL.equals( name ) )
		{
			javahandler.beforeDrawAxisLabel( (Axis) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_AXIS_LABEL.equals( name ) )
		{
			javahandler.afterDrawAxisLabel( (Axis) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.BEFORE_DRAW_AXIS_TITLE.equals( name ) )
		{
			javahandler.beforeDrawAxisTitle( (Axis) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else if ( ScriptHandler.AFTER_DRAW_AXIS_TITLE.equals( name ) )
		{
			javahandler.afterDrawAxisTitle( (Axis) tmpArgs[0],
					(Label) tmpArgs[1],
					(IChartScriptContext) tmpArgs[2] );
		}
		else
		{
			assert false;
			Method mtd = JAVA_FUNTION_MAP.get( name );

			try
			{
				return SecurityUtil.invokeMethod( mtd, javahandler, oaArgs );
			}
			catch ( Exception e )
			{
				logger.log( e );
			}
		}

		return null;
	}

	/**
	 * Call JavaScript functions with one argument.
	 * 
	 * @param sh
	 * @param sFunction
	 * @param oArg1
	 */
	public static final Object callFunction( ScriptHandler sh,
			String sFunction, Object oArg1 ) throws ChartException
	{
		if ( sh == null )
		{
			return null;
		}

		if ( sh.javahandler != null && sh.isJavaFuntion( sFunction ) )
		{
			sh.ONE_ELEMENT_ARRAY[0] = oArg1;
			return sh.callJavaFunction( sFunction, sh.ONE_ELEMENT_ARRAY );
		}
		else
		{
			final Function f = sh.getJavascriptFunction( sFunction );
			if ( f != null )
			{
				sh.ONE_ELEMENT_ARRAY[0] = oArg1;
				Object oReturnValue = null;
				oReturnValue = sh.callJavaScriptFunction( f,
						sh.ONE_ELEMENT_ARRAY );

				return oReturnValue;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Call JavaScript functions with two arguments.
	 * 
	 * @param sh
	 * @param sFunction
	 * @param oArg1
	 * @param oArg2
	 */
	public static final Object callFunction( ScriptHandler sh,
			String sFunction, Object oArg1, Object oArg2 )
			throws ChartException
	{
		if ( sh == null )
		{
			return null;
		}

		if ( sh.javahandler != null && sh.isJavaFuntion( sFunction ) )
		{
			sh.TWO_ELEMENT_ARRAY[0] = oArg1;
			sh.TWO_ELEMENT_ARRAY[1] = oArg2;
			return sh.callJavaFunction( sFunction, sh.TWO_ELEMENT_ARRAY );
		}
		else
		{
			final Function f = sh.getJavascriptFunction( sFunction );
			if ( f != null )
			{
				sh.TWO_ELEMENT_ARRAY[0] = oArg1;
				sh.TWO_ELEMENT_ARRAY[1] = oArg2;
				Object oReturnValue = null;
				oReturnValue = sh.callJavaScriptFunction( f,
						sh.TWO_ELEMENT_ARRAY );

				return oReturnValue;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Call JavaScript functions with three arguments.
	 * 
	 * @param sh
	 * @param sFunction
	 * @param oArg1
	 * @param oArg2
	 * @param oArg3
	 */
	public static final Object callFunction( ScriptHandler sh,
			String sFunction, Object oArg1, Object oArg2, Object oArg3 )
			throws ChartException
	{
		if ( sh == null )
		{
			return null;
		}

		if ( sh.javahandler != null && sh.isJavaFuntion( sFunction ) )
		{
			sh.THREE_ELEMENT_ARRAY[0] = oArg1;
			sh.THREE_ELEMENT_ARRAY[1] = oArg2;
			sh.THREE_ELEMENT_ARRAY[2] = oArg3;
			return sh.callJavaFunction( sFunction, sh.THREE_ELEMENT_ARRAY );
		}
		else
		{
			final Function f = sh.getJavascriptFunction( sFunction );
			if ( f != null )
			{
				sh.THREE_ELEMENT_ARRAY[0] = oArg1;
				sh.THREE_ELEMENT_ARRAY[1] = oArg2;
				sh.THREE_ELEMENT_ARRAY[2] = oArg3;
				Object oReturnValue = null;
				oReturnValue = sh.callJavaScriptFunction( f,
						sh.THREE_ELEMENT_ARRAY );

				return oReturnValue;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Evaluates the given expression and returns the value.
	 * 
	 * @param sScriptContent
	 */
	public final Object evaluate( String sScriptContent ) throws ChartException
	{
		final Context cx = Context.enter( );
		try
		{
			return cx.evaluateString( scope, sScriptContent, "<cmd>", 1, null ); //$NON-NLS-1$
		}
		catch ( RhinoException jsx )
		{
			throw convertException( jsx );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * Register the script content for current script handler.
	 * 
	 * @param sScriptContent
	 *            This is either the JavaSciprt code content or a full class
	 *            name which has implemented
	 *            <code>IChartItemScriptHandler</code>
	 */
	public final void register( String sScriptName, String sScriptContent )
			throws ChartException
	{
		try
		{
			logger.log( ILogger.INFORMATION,
					Messages.getString( "Info.try.load.java.handler" ) ); //$NON-NLS-1$

			Class<?> handlerClass = null;

			try
			{
				handlerClass = Class.forName( sScriptContent );
			}
			catch ( ClassNotFoundException ex )
			{
				if ( iscl != null )
				{
					handlerClass = iscl.loadClass( sScriptContent,
							SecurityUtil.getClassLoader( ScriptHandler.class ) );
				}
				else
				{
					throw ex;
				}

			}

			if ( IChartEventHandler.class.isAssignableFrom( handlerClass ) )
			{
				try
				{
					javahandler = (IChartEventHandler) SecurityUtil.newClassInstance( handlerClass );
				}
				catch ( InstantiationException e )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							BirtException.ERROR,
							e );
				}
				catch ( IllegalAccessException e )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							BirtException.ERROR,
							e );
				}

				logger.log( ILogger.INFORMATION,
						Messages.getString( "Info.java.handler.loaded", //$NON-NLS-1$
								handlerClass,
								ULocale.getDefault( ) ) );
			}
			else
			{
				logger.log( ILogger.WARNING,
						Messages.getString( "Info.invalid.java.handler", //$NON-NLS-1$
								handlerClass,
								ULocale.getDefault( ) ) );
			}
		}
		catch ( ClassNotFoundException e )
		{
			// Not a Java class name, so this must be JavaScript code
			javahandler = null;

			logger.log( ILogger.INFORMATION,
					Messages.getString( "Info.try.register.javascript.content" ) ); //$NON-NLS-1$

			final Context cx = Context.enter( );
			try
			{
				cx.evaluateString( scope,
						sScriptContent,
						sScriptName == null ? "<cmd>" : sScriptName, 1, null ); //$NON-NLS-1$

				logger.log( ILogger.INFORMATION,
						Messages.getString( "Info.javascript.content.registered" ) ); //$NON-NLS-1$

				// prepare function name cache.
				Object[] objs = scope.getIds( );

				if ( objs != null )
				{
					javaScriptFunctionNamesCache = new ArrayList<String>( );
					for ( int i = 0; i < objs.length; i++ )
					{
						javaScriptFunctionNamesCache.add( String.valueOf( objs[i] ) );
					}
				}
				else
				{
					javaScriptFunctionNamesCache = null;
				}

			}
			catch ( RhinoException jsx )
			{
				throw convertException( jsx );
			}
			finally
			{
				Context.exit( );
			}
		}

	}

	/**
	 * Sets the context object of current script handler.
	 * 
	 * @param csc
	 */
	public void setScriptContext( IChartScriptContext csc )
	{
		this.csc = csc;

	}

	/**
	 * Converts general exception to more readable format.
	 * 
	 * @param ex
	 * @return
	 */
	protected ChartException convertException( Exception ex )
	{
		if ( ex instanceof RhinoException )
		{
			RhinoException e = (RhinoException) ex;
			String lineSource = e.lineSource( );
			String details = e.details( );
			String lineNumber = String.valueOf( e.lineNumber( ) );
			if ( lineSource == null )
				lineSource = "";//$NON-NLS-1$
			return new ChartException( ChartEnginePlugin.ID,
					ChartException.SCRIPT,
					"exception.javascript.error", //$NON-NLS-1$
					new Object[]{
							details, lineNumber, lineSource
					},
					Messages.getResourceBundle( csc.getULocale( ) ),
					e );
		}
		/*
		 * TODO convert those exceptions too else if ( ex instanceof
		 * IllegalAccessException ) {} else if ( ex instanceof
		 * InstantiationException ) {} else if ( ex instanceof
		 * InvocationTargetException ) { }
		 */
		else
			return new ChartException( ChartEnginePlugin.ID,
					ChartException.SCRIPT,
					ex );
	}
}