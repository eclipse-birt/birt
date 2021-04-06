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

package org.eclipse.birt.chart.model;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Chart
 * With Axes</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> ChartWithAxes represents a chart with axes. (e.g.
 * Bar Chart, Line Chart, etc).
 * <p xmlns="http://www.birt.eclipse.org/ChartModel">
 * To create an instance use the factory method:<br/>
 * ChartWithAxesImpl.create( );
 * </p>
 * At the moment only charts with one single base axis are supported, while one
 * or more orthogonal axes can be associated with the base axis. In case of 3D
 * charts, the z-axis is defined as an ancillary axis of the base axis.
 * <p xmlns="http://www.birt.eclipse.org/ChartModel">
 * Following examples show the ways to retrieve the base axis of an instance of
 * ChartWithAxes :<br/>
 * ChartWithAxes cwa...<br/>
 * Axis axBase = cwa.getAxes( ).get( 0 );<br/>
 * or<br/>
 * Axis axBase = cwa.getAxes( ).getPrimaryBaseAxes( )[0];
 * </p>
 * <p xmlns="http://www.birt.eclipse.org/ChartModel">
 * To retrieve the primary orthogonal axis associated with the base axis
 * use:<br/>
 * <br/>
 * Axis axOrth = getPrimaryOrthogonalAxis( axBase );<br/>
 * <br/>
 * To retireve all the orthogonal axes associated with the base axis use
 * in/exclude the primary one use:<br/>
 * <br/>
 * Axis[] axOrths = getOrthogonalAxes( axBase, bIncludePrimary ); <br/>
 * To retrieve the z-axis use: Axis axAnci = getAncillaryBaseAxis( axBase );
 * </p>
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#getAxes
 * <em>Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#getWallFill <em>Wall
 * Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#getFloorFill <em>Floor
 * Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#getOrientation
 * <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#getUnitSpacing <em>Unit
 * Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#getRotation
 * <em>Rotation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#isReverseCategory
 * <em>Reverse Category</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithAxes#isStudyLayout <em>Study
 * Layout</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes()
 * @model extendedMetaData="name='ChartWithAxes' kind='elementOnly'"
 * @generated
 */
public interface ChartWithAxes extends Chart {

	/**
	 * Returns the value of the '<em><b>Axes</b></em>' containment reference list.
	 * The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.Axis}. <!-- begin-user-doc -->
	 * Gets the list of base axes for the chart. 'Base' axes are the Category axes
	 * for the chart if the chart has any. Each of these axes can contain zero or
	 * more 'Orthogonal' axes. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds all the base axes for the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Axes</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_Axes()
	 * @model type="org.eclipse.birt.chart.model.component.Axis" containment="true"
	 *        resolveProxies="false" lower="2"
	 * @generated
	 */
	EList<Axis> getAxes();

	/**
	 * Returns the value of the '<em><b>Wall Fill</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the fill to be used for the chart wall (for charts with depth).
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Wall Fill</em>' containment reference.
	 * @see #setWallFill(Fill)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_WallFill()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Fill getWallFill();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getWallFill <em>Wall
	 * Fill</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Wall Fill</em>' containment reference.
	 * @see #getWallFill()
	 * @generated
	 */
	void setWallFill(Fill value);

	/**
	 * Returns the value of the '<em><b>Floor Fill</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the fill to be used for the chart floor (for charts with depth).
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Floor Fill</em>' containment reference.
	 * @see #setFloorFill(Fill)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_FloorFill()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Fill getFloorFill();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getFloorFill <em>Floor
	 * Fill</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Floor Fill</em>' containment
	 *              reference.
	 * @see #getFloorFill()
	 * @generated
	 */
	void setFloorFill(Fill value);

	/**
	 * Returns the value of the '<em><b>Orientation</b></em>' attribute. The default
	 * value is <code>"Horizontal"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Orientation}. <!--
	 * begin-user-doc --> Gets the orientation of the chart as a whole. This drives
	 * the way the chart will be rendered and affects the orientations of other
	 * elements throughout the chart. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies whether the chart is a vertical or horizontal chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetOrientation()
	 * @see #unsetOrientation()
	 * @see #setOrientation(Orientation)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_Orientation()
	 * @model default="Horizontal" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	Orientation getOrientation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getOrientation
	 * <em>Orientation</em>}' attribute. <!-- begin-user-doc --> Sets the
	 * orientation of the chart as a whole. This drives the way the chart will be
	 * rendered and affects the orientations of other elements throughout the chart.
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetOrientation()
	 * @see #unsetOrientation()
	 * @see #getOrientation()
	 * @generated
	 */
	void setOrientation(Orientation value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getOrientation
	 * <em>Orientation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetOrientation()
	 * @see #getOrientation()
	 * @see #setOrientation(Orientation)
	 * @generated
	 */
	void unsetOrientation();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.ChartWithAxes#getOrientation
	 * <em>Orientation</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Orientation</em>' attribute is set.
	 * @see #unsetOrientation()
	 * @see #getOrientation()
	 * @see #setOrientation(Orientation)
	 * @generated
	 */
	boolean isSetOrientation();

	/**
	 * Returns the value of the '<em><b>Unit Spacing</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the spacing between the last series element of one unit and the
	 * first series element of the next unit in the chart. This should be given as a
	 * percentage of the unit.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Unit Spacing</em>' attribute.
	 * @see #isSetUnitSpacing()
	 * @see #unsetUnitSpacing()
	 * @see #setUnitSpacing(double)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_UnitSpacing()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.birt.chart.model.attribute.Percentage"
	 *        required="true"
	 * @generated
	 */
	double getUnitSpacing();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getUnitSpacing <em>Unit
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Unit Spacing</em>' attribute.
	 * @see #isSetUnitSpacing()
	 * @see #unsetUnitSpacing()
	 * @see #getUnitSpacing()
	 * @generated
	 */
	void setUnitSpacing(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getUnitSpacing <em>Unit
	 * Spacing</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetUnitSpacing()
	 * @see #getUnitSpacing()
	 * @see #setUnitSpacing(double)
	 * @generated
	 */
	void unsetUnitSpacing();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.ChartWithAxes#getUnitSpacing <em>Unit
	 * Spacing</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Unit Spacing</em>' attribute is set.
	 * @see #unsetUnitSpacing()
	 * @see #getUnitSpacing()
	 * @see #setUnitSpacing(double)
	 * @generated
	 */
	boolean isSetUnitSpacing();

	/**
	 * Returns the value of the '<em><b>Rotation</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * Element "Rotation" of type Rotation3D is used for 3D charts, it specifies how
	 * many degrees the chart graphic is rotated around each of the three
	 * axes.<br xmlns="http://www.birt.eclipse.org/ChartModel"/>
	 * The following example will set a 3D chart to be rotated -20 degrees around
	 * x-axis, 45 degrees around y-axis and 0 degrees around z-axis:
	 * <p xmlns="http://www.birt.eclipse.org/ChartModel">
	 * ChartWithAxes cwa = ...<br/>
	 * Angle3D a3D = cwa .getRotation( ).getAngles( ) .get( 0 );<br/>
	 * a3D.set( -20, 45, 0 );<br/>
	 * </p>
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Rotation</em>' containment reference.
	 * @see #setRotation(Rotation3D)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_Rotation()
	 * @model containment="true" extendedMetaData="kind='element' name='Rotation'"
	 * @generated
	 */
	Rotation3D getRotation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#getRotation
	 * <em>Rotation</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Rotation</em>' containment reference.
	 * @see #getRotation()
	 * @generated
	 */
	void setRotation(Rotation3D value);

	/**
	 * Returns the value of the '<em><b>Reverse Category</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> The boolean attribute
	 * "ReverseCategory" specifies if the categories are in reverse order.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Reverse Category</em>' attribute.
	 * @see #isSetReverseCategory()
	 * @see #unsetReverseCategory()
	 * @see #setReverseCategory(boolean)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_ReverseCategory()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='ReverseCategory'"
	 * @generated
	 */
	boolean isReverseCategory();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#isReverseCategory
	 * <em>Reverse Category</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Reverse Category</em>' attribute.
	 * @see #isSetReverseCategory()
	 * @see #unsetReverseCategory()
	 * @see #isReverseCategory()
	 * @generated
	 */
	void setReverseCategory(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#isReverseCategory
	 * <em>Reverse Category</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetReverseCategory()
	 * @see #isReverseCategory()
	 * @see #setReverseCategory(boolean)
	 * @generated
	 */
	void unsetReverseCategory();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#isReverseCategory
	 * <em>Reverse Category</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Reverse Category</em>' attribute is
	 *         set.
	 * @see #unsetReverseCategory()
	 * @see #isReverseCategory()
	 * @see #setReverseCategory(boolean)
	 * @generated
	 */
	boolean isSetReverseCategory();

	/**
	 * Returns the value of the '<em><b>Study Layout</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The boolean
	 * attribute "StudyLayout" specifies whether multiple Y axes are laid out as a
	 * Study Chart. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Study Layout</em>' attribute.
	 * @see #isSetStudyLayout()
	 * @see #unsetStudyLayout()
	 * @see #setStudyLayout(boolean)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithAxes_StudyLayout()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element' name='StudyLayout'"
	 * @generated
	 */
	boolean isStudyLayout();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#isStudyLayout <em>Study
	 * Layout</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Study Layout</em>' attribute.
	 * @see #isSetStudyLayout()
	 * @see #unsetStudyLayout()
	 * @see #isStudyLayout()
	 * @generated
	 */
	void setStudyLayout(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#isStudyLayout <em>Study
	 * Layout</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetStudyLayout()
	 * @see #isStudyLayout()
	 * @see #setStudyLayout(boolean)
	 * @generated
	 */
	void unsetStudyLayout();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithAxes#isStudyLayout <em>Study
	 * Layout</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Study Layout</em>' attribute is set.
	 * @see #unsetStudyLayout()
	 * @see #isStudyLayout()
	 * @see #setStudyLayout(boolean)
	 * @generated
	 */
	boolean isSetStudyLayout();

	/**
	 * This method returns all base axes associated with the chart model
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	Axis[] getBaseAxes();

	/**
	 * This method returns all primary base axes associated with the chart model
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	Axis[] getPrimaryBaseAxes();

	/**
	 * This method returns all (primary and overlay) orthogonal axes for a given
	 * base axis If the primary orthogonal is requested for, it would be returned as
	 * the first element in the array
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return
	 */
	Axis[] getOrthogonalAxes(Axis axBase, boolean bIncludePrimary);

	/**
	 * This method returns the primary orthogonal axis for a given base axis
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return
	 */
	Axis getPrimaryOrthogonalAxis(Axis axBase);

	/**
	 * This method returns the ancillary base axis for a given base axis
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return
	 */
	Axis getAncillaryBaseAxis(Axis axBase);

	/**
	 * This convenience method initializes all member variables
	 * 
	 * NOTE: Manually written
	 */
	// void initialize();
	/**
	 * This method needs to be called after the chart has been populated with
	 * runtime datasets and runtime series have been associated with each of the
	 * axes.
	 * 
	 * @param iBaseOrOrthogonal
	 * @return All series associated with the specified axis types
	 */
	Series[] getSeries(int iBaseOrOrthogonal);

	/**
	 * A convenience method used to determine if the plot is transposed or not. For
	 * an orthogonal set of axes, this is determined by evaluating the 'Orientation'
	 * property. If (orientation == HORIZONTAL), the chart is transposed.
	 * 
	 * NOTE: Manually written
	 * 
	 * @return A boolean indicating if the chart is transposed or not
	 */
	boolean isTransposed();

	/**
	 * A convenience method used to specify if the plot is transposed or not. For an
	 * orthogonal set of axes, this is determined by internally setting the
	 * 'Orientation' property. If (orientation == HORIZONTAL), the chart is
	 * transposed.
	 * 
	 * NOTE: Manually written
	 */
	void setTransposed(boolean bTransposed);

	/**
	 * @generated
	 */
	ChartWithAxes copyInstance();

} // ChartWithAxes
