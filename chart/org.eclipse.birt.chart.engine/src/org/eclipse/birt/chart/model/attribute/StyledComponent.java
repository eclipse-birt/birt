/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration
 * '<em><b>Styled Component</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * This type represents the possible values for the legend item type.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStyledComponent()
 * @model
 * @generated
 */
public enum StyledComponent implements Enumerator {
	/**
	 * The '<em><b>Chart All</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #CHART_ALL
	 * @generated
	 * @ordered
	 */
	CHART_ALL_LITERAL(0, "ChartAll", "Chart_All"),
	/**
	 * The '<em><b>Chart Title</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Chart Title</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #CHART_TITLE
	 * @generated
	 * @ordered
	 */
	CHART_TITLE_LITERAL(1, "ChartTitle", "Chart_Title"),
	/**
	 * The '<em><b>Chart Background</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Chart Background</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #CHART_BACKGROUND
	 * @generated
	 * @ordered
	 */
	CHART_BACKGROUND_LITERAL(2, "ChartBackground", "Chart_Background"),
	/**
	 * The '<em><b>Plot Background</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Plot Background</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #PLOT_BACKGROUND
	 * @generated
	 * @ordered
	 */
	PLOT_BACKGROUND_LITERAL(3, "PlotBackground", "Plot_Background"),
	/**
	 * The '<em><b>Legend Background</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Legend Background</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #LEGEND_BACKGROUND
	 * @generated
	 * @ordered
	 */
	LEGEND_BACKGROUND_LITERAL(4, "LegendBackground", "Legend_Background"),
	/**
	 * The '<em><b>Legend Label</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Legend Label</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #LEGEND_LABEL
	 * @generated
	 * @ordered
	 */
	LEGEND_LABEL_LITERAL(5, "LegendLabel", "Legend_Label"),
	/**
	 * The '<em><b>Axis Title</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Axis Title</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #AXIS_TITLE
	 * @generated
	 * @ordered
	 */
	AXIS_TITLE_LITERAL(6, "AxisTitle", "Axis_Title"),
	/**
	 * The '<em><b>Axis Label</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Axis Label</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #AXIS_LABEL
	 * @generated
	 * @ordered
	 */
	AXIS_LABEL_LITERAL(7, "AxisLabel", "Axis_Label"),
	/**
	 * The '<em><b>Axis Line</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Axis Line</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #AXIS_LINE
	 * @generated
	 * @ordered
	 */
	AXIS_LINE_LITERAL(8, "AxisLine", "Axis_Line"),
	/**
	 * The '<em><b>Series Title</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Series Title</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SERIES_TITLE
	 * @generated
	 * @ordered
	 */
	SERIES_TITLE_LITERAL(9, "SeriesTitle", "Series_Title"),
	/**
	 * The '<em><b>Series Label</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Series Label</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SERIES_LABEL
	 * @generated
	 * @ordered
	 */
	SERIES_LABEL_LITERAL(10, "SeriesLabel", "Series_Label");

	/**
	 * The '<em><b>Chart All</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Chart All</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #CHART_ALL_LITERAL
	 * @model name="ChartAll" literal="Chart_All"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_ALL = 0;

	/**
	 * The '<em><b>Chart Title</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #CHART_TITLE_LITERAL
	 * @model name="ChartTitle" literal="Chart_Title"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_TITLE = 1;

	/**
	 * The '<em><b>Chart Background</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #CHART_BACKGROUND_LITERAL
	 * @model name="ChartBackground" literal="Chart_Background"
	 * @generated
	 * @ordered
	 */
	public static final int CHART_BACKGROUND = 2;

	/**
	 * The '<em><b>Plot Background</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #PLOT_BACKGROUND_LITERAL
	 * @model name="PlotBackground" literal="Plot_Background"
	 * @generated
	 * @ordered
	 */
	public static final int PLOT_BACKGROUND = 3;

	/**
	 * The '<em><b>Legend Background</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @see #LEGEND_BACKGROUND_LITERAL
	 * @model name="LegendBackground" literal="Legend_Background"
	 * @generated
	 * @ordered
	 */
	public static final int LEGEND_BACKGROUND = 4;

	/**
	 * The '<em><b>Legend Label</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #LEGEND_LABEL_LITERAL
	 * @model name="LegendLabel" literal="Legend_Label"
	 * @generated
	 * @ordered
	 */
	public static final int LEGEND_LABEL = 5;

	/**
	 * The '<em><b>Axis Title</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #AXIS_TITLE_LITERAL
	 * @model name="AxisTitle" literal="Axis_Title"
	 * @generated
	 * @ordered
	 */
	public static final int AXIS_TITLE = 6;

	/**
	 * The '<em><b>Axis Label</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #AXIS_LABEL_LITERAL
	 * @model name="AxisLabel" literal="Axis_Label"
	 * @generated
	 * @ordered
	 */
	public static final int AXIS_LABEL = 7;

	/**
	 * The '<em><b>Axis Line</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #AXIS_LINE_LITERAL
	 * @model name="AxisLine" literal="Axis_Line"
	 * @generated
	 * @ordered
	 */
	public static final int AXIS_LINE = 8;

	/**
	 * The '<em><b>Series Title</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #SERIES_TITLE_LITERAL
	 * @model name="SeriesTitle" literal="Series_Title"
	 * @generated
	 * @ordered
	 */
	public static final int SERIES_TITLE = 9;

	/**
	 * The '<em><b>Series Label</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #SERIES_LABEL_LITERAL
	 * @model name="SeriesLabel" literal="Series_Label"
	 * @generated
	 * @ordered
	 */
	public static final int SERIES_LABEL = 10;

	/**
	 * An array of all the '<em><b>Styled Component</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final StyledComponent[] VALUES_ARRAY = { CHART_ALL_LITERAL, CHART_TITLE_LITERAL,
			CHART_BACKGROUND_LITERAL, PLOT_BACKGROUND_LITERAL, LEGEND_BACKGROUND_LITERAL, LEGEND_LABEL_LITERAL,
			AXIS_TITLE_LITERAL, AXIS_LABEL_LITERAL, AXIS_LINE_LITERAL, SERIES_TITLE_LITERAL, SERIES_LABEL_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Styled Component</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<StyledComponent> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Styled Component</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static StyledComponent get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StyledComponent result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Styled Component</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static StyledComponent getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			StyledComponent result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Styled Component</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static StyledComponent get(int value) {
		switch (value) {
		case CHART_ALL:
			return CHART_ALL_LITERAL;
		case CHART_TITLE:
			return CHART_TITLE_LITERAL;
		case CHART_BACKGROUND:
			return CHART_BACKGROUND_LITERAL;
		case PLOT_BACKGROUND:
			return PLOT_BACKGROUND_LITERAL;
		case LEGEND_BACKGROUND:
			return LEGEND_BACKGROUND_LITERAL;
		case LEGEND_LABEL:
			return LEGEND_LABEL_LITERAL;
		case AXIS_TITLE:
			return AXIS_TITLE_LITERAL;
		case AXIS_LABEL:
			return AXIS_LABEL_LITERAL;
		case AXIS_LINE:
			return AXIS_LINE_LITERAL;
		case SERIES_TITLE:
			return SERIES_TITLE_LITERAL;
		case SERIES_LABEL:
			return SERIES_LABEL_LITERAL;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	StyledComponent(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
}
