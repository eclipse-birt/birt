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

package org.eclipse.birt.chart.model.layout;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Plot</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Plot represent the rectangular area in a chart,
 * inside where the graph of its datapoints is displayed. It extends to the
 * client area of a chart's block excluding the title and legend. Plot is a
 * sub-type of Block, and exists as a child block of the chart's block in a
 * chart's model.
 * <p xmlns="http://www.birt.eclipse.org/ChartModelLayout">
 * Besides the general approach of using Chart.getBlock( ).getChildren( ) we can
 * also access it using the convenient method: Chart.getPlot( );
 * </p>
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing
 * <em>Horizontal Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing
 * <em>Vertical Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.Plot#getClientArea <em>Client
 * Area</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getPlot()
 * @model extendedMetaData="name='Plot' kind='elementOnly'"
 * @generated
 */
public interface Plot extends Block {

	/**
	 * Returns the value of the '<em><b>Horizontal Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the horizontal spacing between elements in the plot.
	 * (e.g. Axes, Client Area). <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * The horizontal spacing between elements in the plot.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Horizontal Spacing</em>' attribute.
	 * @see #isSetHorizontalSpacing()
	 * @see #unsetHorizontalSpacing()
	 * @see #setHorizontalSpacing(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getPlot_HorizontalSpacing()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getHorizontalSpacing();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing
	 * <em>Horizontal Spacing</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Horizontal Spacing</em>' attribute.
	 * @see #isSetHorizontalSpacing()
	 * @see #unsetHorizontalSpacing()
	 * @see #getHorizontalSpacing()
	 * @generated
	 */
	void setHorizontalSpacing(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing
	 * <em>Horizontal Spacing</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #isSetHorizontalSpacing()
	 * @see #getHorizontalSpacing()
	 * @see #setHorizontalSpacing(int)
	 * @generated
	 */
	void unsetHorizontalSpacing();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getHorizontalSpacing
	 * <em>Horizontal Spacing</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Horizontal Spacing</em>' attribute is
	 *         set.
	 * @see #unsetHorizontalSpacing()
	 * @see #getHorizontalSpacing()
	 * @see #setHorizontalSpacing(int)
	 * @generated
	 */
	boolean isSetHorizontalSpacing();

	/**
	 * Returns the value of the '<em><b>Vertical Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> Gets the horizontal spacing between elements in the plot.
	 * <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * The vertical spacing between elements in the plot.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Vertical Spacing</em>' attribute.
	 * @see #isSetVerticalSpacing()
	 * @see #unsetVerticalSpacing()
	 * @see #setVerticalSpacing(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getPlot_VerticalSpacing()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getVerticalSpacing();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing
	 * <em>Vertical Spacing</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Vertical Spacing</em>' attribute.
	 * @see #isSetVerticalSpacing()
	 * @see #unsetVerticalSpacing()
	 * @see #getVerticalSpacing()
	 * @generated
	 */
	void setVerticalSpacing(int value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing
	 * <em>Vertical Spacing</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #isSetVerticalSpacing()
	 * @see #getVerticalSpacing()
	 * @see #setVerticalSpacing(int)
	 * @generated
	 */
	void unsetVerticalSpacing();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getVerticalSpacing
	 * <em>Vertical Spacing</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Vertical Spacing</em>' attribute is
	 *         set.
	 * @see #unsetVerticalSpacing()
	 * @see #getVerticalSpacing()
	 * @see #setVerticalSpacing(int)
	 * @generated
	 */
	boolean isSetVerticalSpacing();

	/**
	 * Returns the value of the '<em><b>Client Area</b></em>' containment reference.
	 * <!-- begin-user-doc --> Gets the client area for the plot. This is the region
	 * in which the data values will be plotted. <!-- end-user-doc --> <!--
	 * begin-model-doc --> Element "ClientArea" represents the valid rectangular
	 * area of a plot block to lay out its content. It extends the whole block
	 * excluding the margin of the block, which is defined by "Insets". It also
	 * holds a group of properties to specify how this area will be rendered,
	 * including its background, outline, visibility... and so on.
	 * <p xmlns="http://www.birt.eclipse.org/ChartModelLayout">
	 * For chart with axes it represents the area within axes. <br/>
	 * For chart without axes it will be split into cells, with each cell for one
	 * single series.
	 * </p>
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Client Area</em>' containment reference.
	 * @see #setClientArea(ClientArea)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getPlot_ClientArea()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='ClientArea'"
	 * @generated
	 */
	ClientArea getClientArea();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.layout.Plot#getClientArea <em>Client
	 * Area</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Client Area</em>' containment
	 *              reference.
	 * @see #getClientArea()
	 * @generated
	 */
	void setClientArea(ClientArea value);

	/**
	 * @generated
	 */
	@Override
	Plot copyInstance();

} // Plot
