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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.LineInterpolation;
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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.ExtendedMetaData;

import junit.framework.TestCase;

public class LineSeriesInterpolationTest extends TestCase {

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

	/** Both factory methods start Linear, with nothing written to the model. */
	public void testDefaultIsLinearAndUnset() {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		assertEquals(LineInterpolation.LINEAR_LITERAL, ls.getInterpolation());
		assertFalse(ls.isSetInterpolation());
		LineSeries def = (LineSeries) LineSeriesImpl.createDefault();
		assertEquals(LineInterpolation.LINEAR_LITERAL, def.getInterpolation());
		assertFalse(def.isSetInterpolation());
	}

	/** Set marks the attribute, unset restores Linear, null maps to Linear. */
	public void testSetAndUnset() {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setInterpolation(LineInterpolation.STEP_CENTER_LITERAL);
		assertTrue(ls.isSetInterpolation());
		assertEquals(LineInterpolation.STEP_CENTER_LITERAL, ls.getInterpolation());
		ls.unsetInterpolation();
		assertFalse(ls.isSetInterpolation());
		assertEquals(LineInterpolation.LINEAR_LITERAL, ls.getInterpolation());
		ls.setInterpolation(null); // EMF enum setter maps null to the default
		assertEquals(LineInterpolation.LINEAR_LITERAL, ls.getInterpolation());
	}

	/** A set value round-trips through the {@code <Interpolation>} element. */
	public void testSerializerWritesElementAndReadsItBack() throws IOException {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setInterpolation(LineInterpolation.STEP_BEFORE_LITERAL);
		String xml = write(chartWith(ls));
		assertTrue(xml, xml.contains("<Interpolation>StepBefore</Interpolation>")); //$NON-NLS-1$
		LineSeries back = valueSeries(read(xml));
		assertEquals(LineInterpolation.STEP_BEFORE_LITERAL, back.getInterpolation());
		assertTrue(back.isSetInterpolation());
	}

	/** No element is written, so a chart saved without one loads as Linear. */
	public void testUnsetSeriesWritesNoElementAndOldXmlReadsAsLinear() throws IOException {
		String xml = write(chartWith((LineSeries) LineSeriesImpl.create()));
		assertFalse(xml, xml.contains("Interpolation")); //$NON-NLS-1$
		LineSeries back = valueSeries(read(xml));
		assertEquals(LineInterpolation.LINEAR_LITERAL, back.getInterpolation());
		assertFalse(back.isSetInterpolation());
	}

	/** An unknown literal fails the load instead of falling back to Linear. */
	public void testInvalidLiteralIsRejected() throws IOException {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setInterpolation(LineInterpolation.STEP_AFTER_LITERAL);
		String xml = write(chartWith(ls)).replace("StepAfter", "Step_After"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			read(xml);
			fail("a literal outside the enumeration must not load"); //$NON-NLS-1$
		} catch (IOException expected) {
			// EMF records the unknown literal as a load error and reports it as an
			// IOWrappedException; the serializer itself does not wrap anything.
		}
	}

	/** copyInstance keeps the value and the set flag, on series and on chart. */
	public void testCopyInstanceKeepsValueAndFlag() {
		LineSeries ls = (LineSeries) LineSeriesImpl.create();
		ls.setInterpolation(LineInterpolation.STEP_AFTER_LITERAL);
		LineSeries copy = ls.copyInstance();
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, copy.getInterpolation());
		assertTrue(copy.isSetInterpolation());
		ChartWithAxes chartCopy = chartWith(ls).copyInstance();
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, valueSeries(chartCopy).getInterpolation());
	}

	/** AreaSeries derives from LineSeries, so it carries the same attribute. */
	public void testAreaSeriesInheritsTheAttribute() throws IOException {
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create();
		as.setInterpolation(LineInterpolation.STEP_AFTER_LITERAL);
		String xml = write(chartWith(as));
		assertTrue(xml, xml.contains("<Interpolation>StepAfter</Interpolation>")); //$NON-NLS-1$
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, valueSeries(read(xml)).getInterpolation());
	}

	/** The generated feature id, position and XML name of the attribute. */
	public void testTypePackageWiring() {
		EAttribute interpolation = TypePackage.Literals.LINE_SERIES__INTERPOLATION;
		assertEquals(TypePackage.LINE_SERIES__INTERPOLATION, interpolation.getFeatureID());
		assertSame(interpolation, TypePackage.eINSTANCE.getLineSeries().getEStructuralFeatures().get(7));
		assertEquals("Interpolation", ExtendedMetaData.INSTANCE.getName(interpolation)); //$NON-NLS-1$
		assertEquals(TypePackage.LINE_SERIES_FEATURE_COUNT, TypePackage.eINSTANCE.getLineSeries().getFeatureCount());
		assertEquals(TypePackage.BUBBLE_SERIES__INTERPOLATION, TypePackage.LINE_SERIES__INTERPOLATION);
		assertEquals(TypePackage.DIFFERENCE_SERIES__INTERPOLATION, TypePackage.LINE_SERIES__INTERPOLATION);
	}
}
