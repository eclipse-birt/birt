/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.chart.tests.engine.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.image.PngRendererImpl;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.LineInterpolation;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.util.ExtendedMetaData;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * Covers {@code LineSeries.interpolation}: the generated model wiring, the
 * {@code <Interpolation>} XML round trip, and that a mode set on the model
 * reaches the renderer. {@code LineStepExpanderTest} covers the geometry the
 * renderer then draws.
 */
public class LineSeriesInterpolationTest extends TestCase {

	/** Counts the segments of a value series, ignoring axes, ticks and grid. */
	private static final class SegmentCounter extends PngRendererImpl {

		int segments;

		@Override
		public void drawLine(LineRenderEvent lre) throws ChartException {
			Object source = lre.getSource();
			if (source instanceof WrappedStructureSource) {
				WrappedStructureSource ws = (WrappedStructureSource) source;
				if (ws.getType() == StructureType.SERIES_DATA_POINT && ws.getParent() != null
						&& ws.getParent().getType() == StructureType.SERIES) {
					segments++;
				}
			} else if (source instanceof StructureSource
					&& ((StructureSource) source).getType() == StructureType.SERIES) {
				segments++;
			}
			super.drawLine(lre);
		}
	}

	@Override
	protected void setUp() {
		ChartEngine.instance(new PlatformConfig()); // idempotent
	}

	/** @return a chart with a text base axis and {@code ls} as value series. */
	private static ChartWithAxes chartWith(LineSeries ls) {
		ChartWithAxes cwa = ChartWithAxesImpl.create();
		Axis xAxis = cwa.getPrimaryBaseAxes()[0];
		xAxis.setType(AxisType.TEXT_LITERAL);
		Series base = SeriesImpl.create();
		base.setDataSet(TextDataSetImpl.create(new String[] { "A", "B", "C" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		SeriesDefinition sdBase = SeriesDefinitionImpl.create();
		xAxis.getSeriesDefinitions().add(sdBase);
		sdBase.getSeries().add(base);
		ls.setDataSet(NumberDataSetImpl.create(new Double[] { 5.0, 9.0, 3.0 }));
		SeriesDefinition sdValue = SeriesDefinitionImpl.create();
		cwa.getPrimaryOrthogonalAxis(xAxis).getSeriesDefinitions().add(sdValue);
		sdValue.getSeries().add(ls);
		return cwa;
	}

	/** @return the value series of a chart built by {@link #chartWith}. */
	private static LineSeries valueSeries(Chart chart) {
		Axis xAxis = ((ChartWithAxes) chart).getPrimaryBaseAxes()[0];
		return (LineSeries) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(xAxis).getSeriesDefinitions().get(0)
				.getSeries().get(0);
	}

	/** @return the chart serialized to XML. */
	private static String write(Chart chart) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SerializerImpl.instance().write(chart, bos);
		return bos.toString(StandardCharsets.UTF_8);
	}

	/** @return the chart parsed back from {@code xml}. */
	private static Chart read(String xml) throws IOException {
		return SerializerImpl.instance().read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
	}

	/** @return the value-series segments a chart in {@code mode} strokes. */
	private static int segments(LineInterpolation mode) throws ChartException {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		if (mode != null) {
			ls.setInterpolation(mode);
		}
		SegmentCounter idr = new SegmentCounter();
		RunTimeContext rtc = new RunTimeContext();
		rtc.setULocale(ULocale.ENGLISH);
		Generator gr = Generator.instance();
		GeneratedChartState gcs = gr.build(idr.getDisplayServer(), chartWith(ls),
				BoundsImpl.create(0, 0, 600, 400), null, rtc, null);
		gr.render(idr, gcs);
		return idr.segments;
	}

	/**
	 * A set value round-trips through the {@code <Interpolation>} element and
	 * survives copyInstance; an unset series writes no element, so a chart saved
	 * by an older release still loads as Linear. AreaSeries inherits both.
	 */
	public void testXmlRoundTripAndDefaults() throws IOException {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		assertEquals(LineInterpolation.LINEAR_LITERAL, ls.getInterpolation());
		assertFalse(ls.isSetInterpolation());

		String legacy = write(chartWith(ls));
		assertFalse(legacy, legacy.contains("Interpolation")); //$NON-NLS-1$
		assertFalse(valueSeries(read(legacy)).isSetInterpolation());
		assertEquals(LineInterpolation.LINEAR_LITERAL, valueSeries(read(legacy)).getInterpolation());

		ls.setInterpolation(LineInterpolation.STEP_BEFORE_LITERAL);
		assertTrue(ls.isSetInterpolation());
		assertEquals(LineInterpolation.STEP_BEFORE_LITERAL, ls.copyInstance().getInterpolation());
		String xml = write(chartWith(ls));
		assertTrue(xml, xml.contains("<Interpolation>StepBefore</Interpolation>")); //$NON-NLS-1$
		assertEquals(LineInterpolation.STEP_BEFORE_LITERAL, valueSeries(read(xml)).getInterpolation());

		ls.unsetInterpolation();
		assertFalse(ls.isSetInterpolation());
		assertEquals(LineInterpolation.LINEAR_LITERAL, ls.getInterpolation());

		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setInterpolation(LineInterpolation.STEP_AFTER_LITERAL);
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, valueSeries(read(write(chartWith(as)))).getInterpolation());
	}

	/**
	 * The classifier indices the enum was generated at, the XML spelling the
	 * factory reads and writes, and the feature id the subclasses inherit.
	 */
	public void testPackageWiring() {
		assertEquals(4, LineInterpolation.VALUES.size());
		EEnum interpolation = AttributePackage.Literals.LINE_INTERPOLATION;
		assertSame(interpolation,
				AttributePackage.eINSTANCE.getEClassifiers().get(AttributePackage.LINE_INTERPOLATION));
		assertEquals(AttributePackage.LINE_INTERPOLATION, interpolation.getClassifierID());
		assertEquals(AttributePackage.LINE_INTERPOLATION_OBJECT,
				AttributePackage.Literals.LINE_INTERPOLATION_OBJECT.getClassifierID());
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL,
				AttributeFactory.eINSTANCE.createFromString(interpolation, "StepAfter")); //$NON-NLS-1$
		assertEquals("StepAfter", //$NON-NLS-1$
				AttributeFactory.eINSTANCE.convertToString(interpolation, LineInterpolation.STEP_AFTER_LITERAL));
		try {
			AttributeFactory.eINSTANCE.createFromString(interpolation, "Step_After"); //$NON-NLS-1$
			fail("the underscore spelling is not a literal"); //$NON-NLS-1$
		} catch (IllegalArgumentException expected) {
			// expected
		}

		EAttribute feature = TypePackage.Literals.LINE_SERIES__INTERPOLATION;
		assertEquals(TypePackage.LINE_SERIES__INTERPOLATION, feature.getFeatureID());
		assertSame(feature, TypePackage.eINSTANCE.getLineSeries().getEStructuralFeatures().get(7));
		assertEquals("Interpolation", ExtendedMetaData.INSTANCE.getName(feature)); //$NON-NLS-1$
		assertEquals(TypePackage.LINE_SERIES_FEATURE_COUNT, TypePackage.eINSTANCE.getLineSeries().getFeatureCount());
		assertEquals(TypePackage.AREA_SERIES__INTERPOLATION, TypePackage.LINE_SERIES__INTERPOLATION);
		assertEquals(TypePackage.SCATTER_SERIES__INTERPOLATION, TypePackage.LINE_SERIES__INTERPOLATION);
	}

	/**
	 * Three points join with two segments. A step at either end of a pair adds
	 * one corner per pair, a centred step adds two, and an explicit Linear must
	 * render exactly like an unset attribute.
	 */
	public void testTheRendererAppliesTheInterpolation() throws Exception {
		assertEquals("unset", 2, segments(null)); //$NON-NLS-1$
		assertEquals("Linear", 2, segments(LineInterpolation.LINEAR_LITERAL)); //$NON-NLS-1$
		assertEquals("StepAfter", 4, segments(LineInterpolation.STEP_AFTER_LITERAL)); //$NON-NLS-1$
		assertEquals("StepBefore", 4, segments(LineInterpolation.STEP_BEFORE_LITERAL)); //$NON-NLS-1$
		assertEquals("StepCenter", 6, segments(LineInterpolation.STEP_CENTER_LITERAL)); //$NON-NLS-1$
	}
}
