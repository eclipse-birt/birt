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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Dial</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This type defines the basic elements that are
 * expected in a dial chart. This can further be extended for special dial chart
 * types. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getStartAngle
 * <em>Start Angle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getStopAngle <em>Stop
 * Angle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getRadius
 * <em>Radius</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getFill
 * <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getDialRegions
 * <em>Dial Regions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getMajorGrid <em>Major
 * Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getMinorGrid <em>Minor
 * Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getScale
 * <em>Scale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#isInverseScale
 * <em>Inverse Scale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.Dial#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial()
 * @model extendedMetaData="name='Dial' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Dial extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Start Angle</b></em>' attribute. The default
	 * value is <code>"0"</code>. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Specifies the start angle of the dial. <!-- end-model-doc
	 * -->
	 *
	 * @return the value of the '<em>Start Angle</em>' attribute.
	 * @see #isSetStartAngle()
	 * @see #unsetStartAngle()
	 * @see #setStartAngle(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_StartAngle()
	 * @model default="0" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='StartAngle'"
	 * @generated
	 */
	double getStartAngle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStartAngle <em>Start
	 * Angle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Start Angle</em>' attribute.
	 * @see #isSetStartAngle()
	 * @see #unsetStartAngle()
	 * @see #getStartAngle()
	 * @generated
	 */
	void setStartAngle(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStartAngle <em>Start
	 * Angle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetStartAngle()
	 * @see #getStartAngle()
	 * @see #setStartAngle(double)
	 * @generated
	 */
	void unsetStartAngle();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStartAngle <em>Start
	 * Angle</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Start Angle</em>' attribute is set.
	 * @see #unsetStartAngle()
	 * @see #getStartAngle()
	 * @see #setStartAngle(double)
	 * @generated
	 */
	boolean isSetStartAngle();

	/**
	 * Returns the value of the '<em><b>Stop Angle</b></em>' attribute. The default
	 * value is <code>"180"</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc --> Specifies the stop angle of the dial. <!--
	 * end-model-doc -->
	 *
	 * @return the value of the '<em>Stop Angle</em>' attribute.
	 * @see #isSetStopAngle()
	 * @see #unsetStopAngle()
	 * @see #setStopAngle(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_StopAngle()
	 * @model default="180" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='StopAngle'"
	 * @generated
	 */
	double getStopAngle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStopAngle <em>Stop
	 * Angle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Stop Angle</em>' attribute.
	 * @see #isSetStopAngle()
	 * @see #unsetStopAngle()
	 * @see #getStopAngle()
	 * @generated
	 */
	void setStopAngle(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStopAngle <em>Stop
	 * Angle</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetStopAngle()
	 * @see #getStopAngle()
	 * @see #setStopAngle(double)
	 * @generated
	 */
	void unsetStopAngle();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getStopAngle <em>Stop
	 * Angle</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Stop Angle</em>' attribute is set.
	 * @see #unsetStopAngle()
	 * @see #getStopAngle()
	 * @see #setStopAngle(double)
	 * @generated
	 */
	boolean isSetStopAngle();

	/**
	 * Returns the value of the '<em><b>Radius</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the radius of the dial. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Radius</em>' attribute.
	 * @see #isSetRadius()
	 * @see #unsetRadius()
	 * @see #setRadius(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_Radius()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='Radius'"
	 * @generated
	 */
	double getRadius();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getRadius
	 * <em>Radius</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Radius</em>' attribute.
	 * @see #isSetRadius()
	 * @see #unsetRadius()
	 * @see #getRadius()
	 * @generated
	 */
	void setRadius(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getRadius
	 * <em>Radius</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetRadius()
	 * @see #getRadius()
	 * @see #setRadius(double)
	 * @generated
	 */
	void unsetRadius();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getRadius
	 * <em>Radius</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @return whether the value of the '<em>Radius</em>' attribute is set.
	 * @see #unsetRadius()
	 * @see #getRadius()
	 * @see #setRadius(double)
	 * @generated
	 */
	boolean isSetRadius();

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Specifies the border line style. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_LineAttributes()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='LineAttributes'"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getLineAttributes
	 * <em>Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Line Attributes</em>' containment
	 *              reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
	void setLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Fill</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the background fill style. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Fill</em>' containment reference.
	 * @see #setFill(Fill)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_Fill()
	 * @model containment="true" extendedMetaData="kind='element' name='Fill'"
	 * @generated
	 */
	Fill getFill();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getFill <em>Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Fill</em>' containment reference.
	 * @see #getFill()
	 * @generated
	 */
	void setFill(Fill value);

	/**
	 * Returns the value of the '<em><b>Dial Regions</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.DialRegion}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Defines a
	 * set of areas for a range of values within a dial displayed as filled sections
	 * extending across the dial between the start and end positions. <!--
	 * end-model-doc -->
	 *
	 * @return the value of the '<em>Dial Regions</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_DialRegions()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='DialRegions'"
	 * @generated
	 */
	EList<DialRegion> getDialRegions();

	/**
	 * Returns the value of the '<em><b>Major Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * Defines the major grid associated with the dial. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Major Grid</em>' containment reference.
	 * @see #setMajorGrid(Grid)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_MajorGrid()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='MajorGrid'"
	 * @generated
	 */
	Grid getMajorGrid();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getMajorGrid <em>Major
	 * Grid</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Major Grid</em>' containment
	 *              reference.
	 * @see #getMajorGrid()
	 * @generated
	 */
	void setMajorGrid(Grid value);

	/**
	 * Returns the value of the '<em><b>Minor Grid</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * Defines the minor grid associated with the dial. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Minor Grid</em>' containment reference.
	 * @see #setMinorGrid(Grid)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_MinorGrid()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='MinorGrid'"
	 * @generated
	 */
	Grid getMinorGrid();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getMinorGrid <em>Minor
	 * Grid</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Minor Grid</em>' containment
	 *              reference.
	 * @see #getMinorGrid()
	 * @generated
	 */
	void setMinorGrid(Grid value);

	/**
	 * Returns the value of the '<em><b>Scale</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Defines the
	 * scale for the dial. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Scale</em>' containment reference.
	 * @see #setScale(Scale)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_Scale()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Scale'"
	 * @generated
	 */
	Scale getScale();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getScale <em>Scale</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Scale</em>' containment reference.
	 * @see #getScale()
	 * @generated
	 */
	void setScale(Scale value);

	/**
	 * Returns the value of the '<em><b>Inverse Scale</b></em>' attribute. The
	 * default value is <code>"false"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> Defines if invert the scale during
	 * rendering(right to left) <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Inverse Scale</em>' attribute.
	 * @see #isSetInverseScale()
	 * @see #unsetInverseScale()
	 * @see #setInverseScale(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_InverseScale()
	 * @model default="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='InverseScale'"
	 * @generated
	 */
	boolean isInverseScale();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#isInverseScale
	 * <em>Inverse Scale</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Inverse Scale</em>' attribute.
	 * @see #isSetInverseScale()
	 * @see #unsetInverseScale()
	 * @see #isInverseScale()
	 * @generated
	 */
	void setInverseScale(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#isInverseScale
	 * <em>Inverse Scale</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #isSetInverseScale()
	 * @see #isInverseScale()
	 * @see #setInverseScale(boolean)
	 * @generated
	 */
	void unsetInverseScale();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#isInverseScale
	 * <em>Inverse Scale</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Inverse Scale</em>' attribute is set.
	 * @see #unsetInverseScale()
	 * @see #isInverseScale()
	 * @see #setInverseScale(boolean)
	 * @generated
	 */
	boolean isSetInverseScale();

	/**
	 * Returns the value of the '<em><b>Label</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Defines the
	 * properties for grid labels. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Label</em>' containment reference.
	 * @see #setLabel(Label)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_Label()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Label'"
	 * @generated
	 */
	Label getLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getLabel <em>Label</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Label</em>' containment reference.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Format Specifier</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Format specifier for grid label. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Format Specifier</em>' containment reference.
	 * @see #setFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getDial_FormatSpecifier()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='FormatSpecifier'"
	 * @generated
	 */
	FormatSpecifier getFormatSpecifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.component.Dial#getFormatSpecifier
	 * <em>Format Specifier</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Format Specifier</em>' containment
	 *              reference.
	 * @see #getFormatSpecifier()
	 * @generated
	 */
	void setFormatSpecifier(FormatSpecifier value);

	/**
	 * @generated
	 */
	@Override
	Dial copyInstance();

} // Dial
