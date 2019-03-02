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

package org.eclipse.birt.chart.factory;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.script.IExternalContext;
import org.eclipse.birt.chart.script.IScriptClassLoader;
import org.eclipse.birt.chart.style.IStyleProcessor;

import com.ibm.icu.util.ULocale;

/**
 * Provides an entry point into building a chart for a given model. It is
 * implemented as a singleton and does not maintain any state information hence
 * allowing multi-threaded requests for a single generator instance.
 */
 

public interface IGenerator
{
	/**
	 * Since v2, it must be called before build( ), and should only be called
	 * once per design model.
	 * 
	 * @param model
	 *            Chart design model
	 * @param externalContext
	 *            External Context
	 * @param locale
	 *            Locale
	 * @return a runtime context used by build( )
	 * 
	 * @throws ChartException
	 * 
	 * @since 2.1
	 */
	RunTimeContext prepare( Chart model,
			IExternalContext externalContext, IScriptClassLoader iscl,
			ULocale locale ) throws ChartException;
	
	/**
	 * Binds a sql Resuset to a chart model. This is based on the assumption the
	 * column names of the resultset match exactly the data query definitions
	 * and other expressions set inside the chart model.
	 * 
	 * @param resultSet
	 *            A sql resultset that contains the data. The following methods
	 *            of the interface need to be implemented: first(), next(),
	 *            getObject(String), close()
	 * @param chart
	 *            The chart model to bind the data to
	 * @param rtc
	 *            The runtime context
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	void bindData( java.sql.ResultSet resultSet, Chart chart,
			RunTimeContext rtc ) throws ChartException;

	/**
	 * Binds data to the chart model using a row expression evaluator. The
	 * evaluator provides the ability to evaluate the expressions set in the
	 * chart on a row context.
	 * 
	 * @param expressionEvaluator
	 *            The data row expression evaluator implementation
	 * @param chart
	 *            The chart model
	 * @param rtc
	 *            The runtime context
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	void bindData( IDataRowExpressionEvaluator expressionEvaluator,
			Chart chart, RunTimeContext rtc ) throws ChartException;

	/**
	 * Binds data to the chart model using a row expression evaluator. The
	 * evaluator provides the ability to evaluate the expressions set in the
	 * chart on a row context.If the given IActionEvaluator is not null, then it
	 * will also search available expressions within the action and bind it as
	 * the user dataSets.
	 * 
	 * @param expressionEvaluator
	 *            The data row expression evaluator implementation
	 * @param iae
	 *            An IActionEvaluator instance.
	 * @param chart
	 *            The chart model
	 * @param rtc
	 *            The runtime context
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	void bindData( IDataRowExpressionEvaluator expressionEvaluator,
			IActionEvaluator iae, Chart chart, RunTimeContext rtc )
			throws ChartException;




	
	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids
	 *            A display server using which the chart may be built.
	 * @param cmDesignTime
	 *            The design time chart model (bound to a dataset).
	 * @param externalContext
	 *            An external context object.
	 * @param bo
	 *            The bounds associated with the chart being built.
	 * @param rtc
	 *            Encapsulates the runtime environment for the build process.
	 * @return An instance of a generated chart state that encapsulates built
	 *         chart information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 */
	GeneratedChartState build( IDisplayServer ids,
			Chart cmDesignTime, Bounds bo, IExternalContext externalContext,
			RunTimeContext rtc ) throws ChartException;
	
	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids
	 *            A display server using which the chart may be built.
	 * @param cmDesignTime
	 *            The design time chart model (bound to a dataset).
	 * @param externalContext
	 *            An external context object.
	 * @param bo
	 *            The bounds associated with the chart being built.
	 * @param rtc
	 *            Encapsulates the runtime environment for the build process.
	 * @param externalProcessor
	 *            An external style processor. If it's null, an implicit
	 *            processor will be used.
	 * @return An instance of a generated chart state that encapsulates built
	 *         chart information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 */
	GeneratedChartState build( IDisplayServer ids,
			Chart cmDesignTime, Bounds bo, IExternalContext externalContext,
			RunTimeContext rtc, IStyleProcessor externalProcessor )
			throws ChartException;

	
	/**
	 * Draws a previously built chart using the specified device renderer into a
	 * target output device.
	 * 
	 * @param idr
	 *            A device renderer that determines the target context on which
	 *            the chart will be rendered.
	 * @param gcs
	 *            A previously built chart that needs to be rendered.
	 * 
	 * @throws ChartException
	 */
	void render( IDeviceRenderer idr, GeneratedChartState gcs )
			throws ChartException;
	
	/**
	 * Performs a minimal rebuild of the chart if non-sizing attributes are
	 * altered or the dataset for any series has changed. However, if sizing
	 * attribute changes occur that affects the relative position of the various
	 * chart subcomponents, a re-build is required.
	 * 
	 * @param gcs
	 *            A previously built chart encapsulated in a transient
	 *            structure.
	 * 
	 * @throws ChartException
	 */
	void refresh( GeneratedChartState gcs ) throws ChartException;
}


