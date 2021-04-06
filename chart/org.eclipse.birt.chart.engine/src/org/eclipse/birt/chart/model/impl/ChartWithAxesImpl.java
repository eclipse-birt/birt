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

package org.eclipse.birt.chart.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Chart
 * With Axes</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getAxes
 * <em>Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getWallFill
 * <em>Wall Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getFloorFill
 * <em>Floor Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getOrientation
 * <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getUnitSpacing
 * <em>Unit Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getRotation
 * <em>Rotation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#isReverseCategory
 * <em>Reverse Category</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#isStudyLayout
 * <em>Study Layout</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ChartWithAxesImpl extends ChartImpl implements ChartWithAxes {

	/**
	 * The cached value of the '{@link #getAxes() <em>Axes</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAxes()
	 * @generated
	 * @ordered
	 */
	protected EList<Axis> axes;

	/**
	 * The cached value of the '{@link #getWallFill() <em>Wall Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWallFill()
	 * @generated
	 * @ordered
	 */
	protected Fill wallFill;

	/**
	 * The cached value of the '{@link #getFloorFill() <em>Floor Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFloorFill()
	 * @generated
	 * @ordered
	 */
	protected Fill floorFill;

	/**
	 * The default value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected static final Orientation ORIENTATION_EDEFAULT = Orientation.VERTICAL_LITERAL;

	/**
	 * The cached value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected Orientation orientation = ORIENTATION_EDEFAULT;

	/**
	 * This is true if the Orientation attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean orientationESet;

	/**
	 * The default value of the ' {@link #getUnitSpacing() <em>Unit Spacing</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnitSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final double UNIT_SPACING_EDEFAULT = 50.0;

	/**
	 * The cached value of the '{@link #getUnitSpacing() <em>Unit Spacing</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnitSpacing()
	 * @generated
	 * @ordered
	 */
	protected double unitSpacing = UNIT_SPACING_EDEFAULT;

	/**
	 * This is true if the Unit Spacing attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean unitSpacingESet;

	/**
	 * The cached value of the '{@link #getRotation() <em>Rotation</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRotation()
	 * @generated
	 * @ordered
	 */
	protected Rotation3D rotation;

	/**
	 * The default value of the '{@link #isReverseCategory() <em>Reverse
	 * Category</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isReverseCategory()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REVERSE_CATEGORY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReverseCategory() <em>Reverse
	 * Category</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isReverseCategory()
	 * @generated
	 * @ordered
	 */
	protected boolean reverseCategory = REVERSE_CATEGORY_EDEFAULT;

	/**
	 * This is true if the Reverse Category attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean reverseCategoryESet;

	/**
	 * The default value of the '{@link #isStudyLayout() <em>Study Layout</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStudyLayout()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STUDY_LAYOUT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isStudyLayout() <em>Study Layout</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isStudyLayout()
	 * @generated
	 * @ordered
	 */
	protected boolean studyLayout = STUDY_LAYOUT_EDEFAULT;

	/**
	 * This is true if the Study Layout attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean studyLayoutESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ChartWithAxesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.CHART_WITH_AXES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Axis> getAxes() {
		if (axes == null) {
			axes = new EObjectContainmentEList<Axis>(Axis.class, this, ModelPackage.CHART_WITH_AXES__AXES);
		}
		return axes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getWallFill() {
		return wallFill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetWallFill(Fill newWallFill, NotificationChain msgs) {
		Fill oldWallFill = wallFill;
		wallFill = newWallFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART_WITH_AXES__WALL_FILL, oldWallFill, newWallFill);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWallFill(Fill newWallFill) {
		if (newWallFill != wallFill) {
			NotificationChain msgs = null;
			if (wallFill != null)
				msgs = ((InternalEObject) wallFill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART_WITH_AXES__WALL_FILL, null, msgs);
			if (newWallFill != null)
				msgs = ((InternalEObject) newWallFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART_WITH_AXES__WALL_FILL, null, msgs);
			msgs = basicSetWallFill(newWallFill, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__WALL_FILL, newWallFill,
					newWallFill));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getFloorFill() {
		return floorFill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFloorFill(Fill newFloorFill, NotificationChain msgs) {
		Fill oldFloorFill = floorFill;
		floorFill = newFloorFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART_WITH_AXES__FLOOR_FILL, oldFloorFill, newFloorFill);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setFloorFill(Fill newFloorFill) {
		if (newFloorFill != floorFill) {
			NotificationChain msgs = null;
			if (floorFill != null)
				msgs = ((InternalEObject) floorFill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART_WITH_AXES__FLOOR_FILL, null, msgs);
			if (newFloorFill != null)
				msgs = ((InternalEObject) newFloorFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART_WITH_AXES__FLOOR_FILL, null, msgs);
			msgs = basicSetFloorFill(newFloorFill, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__FLOOR_FILL,
					newFloorFill, newFloorFill));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOrientation(Orientation newOrientation) {
		Orientation oldOrientation = orientation;
		orientation = newOrientation == null ? ORIENTATION_EDEFAULT : newOrientation;
		boolean oldOrientationESet = orientationESet;
		orientationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__ORIENTATION,
					oldOrientation, orientation, !oldOrientationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetOrientation() {
		Orientation oldOrientation = orientation;
		boolean oldOrientationESet = orientationESet;
		orientation = ORIENTATION_EDEFAULT;
		orientationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITH_AXES__ORIENTATION,
					oldOrientation, ORIENTATION_EDEFAULT, oldOrientationESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetOrientation() {
		return orientationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getUnitSpacing() {
		return unitSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUnitSpacing(double newUnitSpacing) {
		double oldUnitSpacing = unitSpacing;
		unitSpacing = newUnitSpacing;
		boolean oldUnitSpacingESet = unitSpacingESet;
		unitSpacingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__UNIT_SPACING,
					oldUnitSpacing, unitSpacing, !oldUnitSpacingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetUnitSpacing() {
		double oldUnitSpacing = unitSpacing;
		boolean oldUnitSpacingESet = unitSpacingESet;
		unitSpacing = UNIT_SPACING_EDEFAULT;
		unitSpacingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITH_AXES__UNIT_SPACING,
					oldUnitSpacing, UNIT_SPACING_EDEFAULT, oldUnitSpacingESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetUnitSpacing() {
		return unitSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Rotation3D getRotation() {
		return rotation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetRotation(Rotation3D newRotation, NotificationChain msgs) {
		Rotation3D oldRotation = rotation;
		rotation = newRotation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.CHART_WITH_AXES__ROTATION, oldRotation, newRotation);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRotation(Rotation3D newRotation) {
		if (newRotation != rotation) {
			NotificationChain msgs = null;
			if (rotation != null)
				msgs = ((InternalEObject) rotation).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART_WITH_AXES__ROTATION, null, msgs);
			if (newRotation != null)
				msgs = ((InternalEObject) newRotation).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.CHART_WITH_AXES__ROTATION, null, msgs);
			msgs = basicSetRotation(newRotation, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__ROTATION, newRotation,
					newRotation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isReverseCategory() {
		return reverseCategory;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setReverseCategory(boolean newReverseCategory) {
		boolean oldReverseCategory = reverseCategory;
		reverseCategory = newReverseCategory;
		boolean oldReverseCategoryESet = reverseCategoryESet;
		reverseCategoryESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__REVERSE_CATEGORY,
					oldReverseCategory, reverseCategory, !oldReverseCategoryESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetReverseCategory() {
		boolean oldReverseCategory = reverseCategory;
		boolean oldReverseCategoryESet = reverseCategoryESet;
		reverseCategory = REVERSE_CATEGORY_EDEFAULT;
		reverseCategoryESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITH_AXES__REVERSE_CATEGORY,
					oldReverseCategory, REVERSE_CATEGORY_EDEFAULT, oldReverseCategoryESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetReverseCategory() {
		return reverseCategoryESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isStudyLayout() {
		return studyLayout;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStudyLayout(boolean newStudyLayout) {
		boolean oldStudyLayout = studyLayout;
		studyLayout = newStudyLayout;
		boolean oldStudyLayoutESet = studyLayoutESet;
		studyLayoutESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHART_WITH_AXES__STUDY_LAYOUT,
					oldStudyLayout, studyLayout, !oldStudyLayoutESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetStudyLayout() {
		boolean oldStudyLayout = studyLayout;
		boolean oldStudyLayoutESet = studyLayoutESet;
		studyLayout = STUDY_LAYOUT_EDEFAULT;
		studyLayoutESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ModelPackage.CHART_WITH_AXES__STUDY_LAYOUT,
					oldStudyLayout, STUDY_LAYOUT_EDEFAULT, oldStudyLayoutESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetStudyLayout() {
		return studyLayoutESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.CHART_WITH_AXES__AXES:
			return ((InternalEList<?>) getAxes()).basicRemove(otherEnd, msgs);
		case ModelPackage.CHART_WITH_AXES__WALL_FILL:
			return basicSetWallFill(null, msgs);
		case ModelPackage.CHART_WITH_AXES__FLOOR_FILL:
			return basicSetFloorFill(null, msgs);
		case ModelPackage.CHART_WITH_AXES__ROTATION:
			return basicSetRotation(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelPackage.CHART_WITH_AXES__AXES:
			return getAxes();
		case ModelPackage.CHART_WITH_AXES__WALL_FILL:
			return getWallFill();
		case ModelPackage.CHART_WITH_AXES__FLOOR_FILL:
			return getFloorFill();
		case ModelPackage.CHART_WITH_AXES__ORIENTATION:
			return getOrientation();
		case ModelPackage.CHART_WITH_AXES__UNIT_SPACING:
			return getUnitSpacing();
		case ModelPackage.CHART_WITH_AXES__ROTATION:
			return getRotation();
		case ModelPackage.CHART_WITH_AXES__REVERSE_CATEGORY:
			return isReverseCategory();
		case ModelPackage.CHART_WITH_AXES__STUDY_LAYOUT:
			return isStudyLayout();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelPackage.CHART_WITH_AXES__AXES:
			getAxes().clear();
			getAxes().addAll((Collection<? extends Axis>) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__WALL_FILL:
			setWallFill((Fill) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__FLOOR_FILL:
			setFloorFill((Fill) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__ORIENTATION:
			setOrientation((Orientation) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__UNIT_SPACING:
			setUnitSpacing((Double) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__ROTATION:
			setRotation((Rotation3D) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__REVERSE_CATEGORY:
			setReverseCategory((Boolean) newValue);
			return;
		case ModelPackage.CHART_WITH_AXES__STUDY_LAYOUT:
			setStudyLayout((Boolean) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ModelPackage.CHART_WITH_AXES__AXES:
			getAxes().clear();
			return;
		case ModelPackage.CHART_WITH_AXES__WALL_FILL:
			setWallFill((Fill) null);
			return;
		case ModelPackage.CHART_WITH_AXES__FLOOR_FILL:
			setFloorFill((Fill) null);
			return;
		case ModelPackage.CHART_WITH_AXES__ORIENTATION:
			unsetOrientation();
			return;
		case ModelPackage.CHART_WITH_AXES__UNIT_SPACING:
			unsetUnitSpacing();
			return;
		case ModelPackage.CHART_WITH_AXES__ROTATION:
			setRotation((Rotation3D) null);
			return;
		case ModelPackage.CHART_WITH_AXES__REVERSE_CATEGORY:
			unsetReverseCategory();
			return;
		case ModelPackage.CHART_WITH_AXES__STUDY_LAYOUT:
			unsetStudyLayout();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ModelPackage.CHART_WITH_AXES__AXES:
			return axes != null && !axes.isEmpty();
		case ModelPackage.CHART_WITH_AXES__WALL_FILL:
			return wallFill != null;
		case ModelPackage.CHART_WITH_AXES__FLOOR_FILL:
			return floorFill != null;
		case ModelPackage.CHART_WITH_AXES__ORIENTATION:
			return isSetOrientation();
		case ModelPackage.CHART_WITH_AXES__UNIT_SPACING:
			return isSetUnitSpacing();
		case ModelPackage.CHART_WITH_AXES__ROTATION:
			return rotation != null;
		case ModelPackage.CHART_WITH_AXES__REVERSE_CATEGORY:
			return isSetReverseCategory();
		case ModelPackage.CHART_WITH_AXES__STUDY_LAYOUT:
			return isSetStudyLayout();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (orientation: "); //$NON-NLS-1$
		if (orientationESet)
			result.append(orientation);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", unitSpacing: "); //$NON-NLS-1$
		if (unitSpacingESet)
			result.append(unitSpacing);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", reverseCategory: "); //$NON-NLS-1$
		if (reverseCategoryESet)
			result.append(reverseCategory);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", studyLayout: "); //$NON-NLS-1$
		if (studyLayoutESet)
			result.append(studyLayout);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * This method returns all base axes associated with the chart model
	 * 
	 * NOTE: Manually written
	 * 
	 * @return Axis array
	 */
	public final Axis[] getBaseAxes() {
		final EList<Axis> elAxes = getAxes();
		final int iAxisCount = elAxes.size();
		final Axis[] axa = new Axis[iAxisCount];
		for (int i = 0; i < iAxisCount; i++) {
			axa[i] = elAxes.get(i);
		}
		return axa;
	}

	/**
	 * This method returns all primary base axes associated with the chart model
	 * 
	 * NOTE: Manually written
	 * 
	 * @return Axis array
	 */
	public final Axis[] getPrimaryBaseAxes() {
		final EList<Axis> elAxes = getAxes();
		final int iAxisCount = elAxes.size();
		final Axis[] axa = new Axis[iAxisCount];
		for (int i = 0; i < iAxisCount; i++) {
			axa[i] = elAxes.get(i);
		}
		return axa;
	}

	/**
	 * This method returns all (primary and overlay) orthogonal axes for a given
	 * base axis If the primary orthogonal is requested for, it would be returned as
	 * the first element in the array
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return Axis array
	 */
	public final Axis[] getOrthogonalAxes(Axis axBase, boolean bIncludePrimary) {
		final EList<Axis> elAxes = axBase.getAssociatedAxes();
		final int iAxisCount = elAxes.size();
		final int iDecrease = bIncludePrimary ? 0 : 1;
		final Axis[] axa = new Axis[iAxisCount - iDecrease];

		for (int i = 0, j = 1 - iDecrease; i < iAxisCount; i++) {
			Axis ax = elAxes.get(i);
			if (!ax.isPrimaryAxis()) {
				axa[j++] = ax;
			} else if (bIncludePrimary) {
				axa[0] = ax;
			}
		}
		return axa;
	}

	/**
	 * This method returns the primary orthogonal axis for a given base axis
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return primary orthongal axis
	 */
	public final Axis getPrimaryOrthogonalAxis(Axis axBase) {
		final EList<Axis> elAxes = axBase.getAssociatedAxes();
		final int iAxisCount = elAxes.size();
		Axis ax;
		for (int i = 0; i < iAxisCount; i++) {
			ax = elAxes.get(i);
			if (ax.isPrimaryAxis()) {
				return ax;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.ChartWithAxes#getAncillaryBaseAxis(org.eclipse.
	 * birt.chart.model.component.Axis)
	 */
	public Axis getAncillaryBaseAxis(Axis axBase) {
		final EList<Axis> elAxes = axBase.getAncillaryAxes();
		final int iAxisCount = elAxes.size();

		if (iAxisCount > 0) {
			return elAxes.get(0);
		}

		return null;
	}

	/**
	 * A convenience method to create an initialized 'ChartWithAxes' instance
	 * 
	 * Note: Manually written
	 * 
	 * @return chart model
	 */
	public static final ChartWithAxes create() {
		final ChartWithAxes cwa = ModelFactory.eINSTANCE.createChartWithAxes();
		((ChartWithAxesImpl) cwa).initialize();
		return cwa;
	}

	/**
	 * Initializes all member variables
	 * 
	 * Note: Manually written
	 */
	protected final void initialize() {
		// INITIALIZE SUPER'S MEMBERS
		super.initialize();

		// SETUP A BASE AXIS
		Axis xAxisBase = AxisImpl.create(Axis.BASE);
		xAxisBase.setTitlePosition(Position.BELOW_LITERAL);
		xAxisBase.getTitle().getCaption().setValue(Messages.getString("ChartWithAxesImpl.X_Axis.title")); //$NON-NLS-1$
		xAxisBase.getTitle().setVisible(false);
		xAxisBase.setPrimaryAxis(true);
		xAxisBase.setLabelPosition(Position.BELOW_LITERAL);
		xAxisBase.setOrientation(Orientation.HORIZONTAL_LITERAL);
		xAxisBase.getOrigin().setType(IntersectionType.MIN_LITERAL);
		xAxisBase.getOrigin().setValue(NumberDataElementImpl.create(0));
		xAxisBase.setType(AxisType.TEXT_LITERAL);

		// SETUP AN ORTHOGONAL AXIS
		Axis yAxisOrthogonal = AxisImpl.create(Axis.ORTHOGONAL);
		yAxisOrthogonal.setTitlePosition(Position.LEFT_LITERAL);
		yAxisOrthogonal.getTitle().getCaption().setValue(Messages.getString("ChartWithAxesImpl.Y_Axis.title")); //$NON-NLS-1$
		// Only title rotation, we make special process, not set its 'isSet' flag for
		// default.
		try {
			ChartElementUtil.setDefaultValue(yAxisOrthogonal.getTitle().getCaption().getFont(), "rotation", 90); //$NON-NLS-1$
		} catch (ChartException e) {
			// Do nothing.
		}
		yAxisOrthogonal.getTitle().setVisible(false);
		yAxisOrthogonal.setPrimaryAxis(true);
		yAxisOrthogonal.setLabelPosition(Position.LEFT_LITERAL);
		yAxisOrthogonal.setOrientation(Orientation.VERTICAL_LITERAL);
		yAxisOrthogonal.getOrigin().setType(IntersectionType.MIN_LITERAL);
		yAxisOrthogonal.getOrigin().setValue(NumberDataElementImpl.create(0));
		yAxisOrthogonal.setType(AxisType.LINEAR_LITERAL);

		xAxisBase.getAssociatedAxes().add(yAxisOrthogonal); // ADD THE
		// ORTHOGONAL
		// AXIS TO THE
		// BASE AXIS

		getAxes().add(xAxisBase); // ADD THE BASE AXIS TO THE CHART

		setRotation(Rotation3DImpl.create());
	}

	/**
	 * A convenience method to create an initialized 'ChartWithAxes' instance
	 * 
	 * Note: Manually written
	 * 
	 * @return chart model
	 */
	public static final ChartWithAxes createDefault() {
		final ChartWithAxes cwa = ModelFactory.eINSTANCE.createChartWithAxes();
		((ChartWithAxesImpl) cwa).initDefault();
		return cwa;
	}

	/**
	 * Initializes all member variables
	 * 
	 * Note: Manually written
	 */
	protected final void initDefault() {
		// INITIALIZE SUPER'S MEMBERS
		super.initDefault();

		// SETUP A BASE AXIS
		Axis xAxisBase = AxisImpl.createDefault(Axis.BASE);
		try {
			ChartElementUtil.setDefaultValue(xAxisBase, "titlePosition", //$NON-NLS-1$
					Position.BELOW_LITERAL);
			ChartElementUtil.setDefaultValue(xAxisBase.getTitle(), "visible", false); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(xAxisBase, "primaryAxis", true); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(xAxisBase, "labelPosition", Position.BELOW_LITERAL); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(xAxisBase, "orientation", Orientation.HORIZONTAL_LITERAL); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(xAxisBase.getOrigin(), "type", IntersectionType.MIN_LITERAL); //$NON-NLS-1$
			xAxisBase.getOrigin().setValue(null);
			ChartElementUtil.setDefaultValue(xAxisBase, "type", AxisType.TEXT_LITERAL); //$NON-NLS-1$

			// SETUP AN ORTHOGONAL AXIS
			Axis yAxisOrthogonal = AxisImpl.createDefault(Axis.ORTHOGONAL);
			ChartElementUtil.setDefaultValue(yAxisOrthogonal, "titlePosition", //$NON-NLS-1$
					Position.LEFT_LITERAL);
			ChartElementUtil.setDefaultValue(yAxisOrthogonal.getTitle().getCaption().getFont(), "rotation", 90); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(yAxisOrthogonal.getTitle(), "visible", false); //$NON-NLS-1$
			// The PrimaryAxis property isn't visual property, it should still
			// be initialized and update 'isSet' flag for first orthogonal axis.
			yAxisOrthogonal.setPrimaryAxis(true);
			ChartElementUtil.setDefaultValue(yAxisOrthogonal, "labelPosition", Position.LEFT_LITERAL); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(yAxisOrthogonal, "orientation", Orientation.VERTICAL_LITERAL); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue(yAxisOrthogonal.getOrigin(), "type", IntersectionType.MIN_LITERAL); //$NON-NLS-1$
			yAxisOrthogonal.getOrigin().setValue(null);
			ChartElementUtil.setDefaultValue(yAxisOrthogonal, "type", AxisType.LINEAR_LITERAL); //$NON-NLS-1$

			xAxisBase.getAssociatedAxes().add(yAxisOrthogonal); // ADD THE
			// ORTHOGONAL
			// AXIS TO THE
			// BASE AXIS

			getAxes().add(xAxisBase); // ADD THE BASE AXIS TO THE CHART

			setRotation(Rotation3DImpl.createDefault(new Angle3D[] { Angle3DImpl.createDefault(-20, 45, 0) }));
		} catch (ChartException e) {
		}
	}

	/**
	 * This method needs to be called after the chart has been populated with
	 * runtime datasets and runtime series have been associated with each of the
	 * series definitions.
	 * 
	 * @param iBaseOrOrthogonal
	 * @return All series associated with the specified axis types
	 */
	public final Series[] getSeries(int iBaseOrOrthogonal) {
		final ArrayList<Series> al = new ArrayList<Series>(8);
		final Axis[] axaBase = getBaseAxes();
		Axis[] axaOrthogonal;
		SeriesDefinition sd;
		EList<SeriesDefinition> el;

		for (int i = 0; i < axaBase.length; i++) {
			if ((iBaseOrOrthogonal | IConstants.BASE) == IConstants.BASE) {
				el = axaBase[i].getSeriesDefinitions();
				for (int j = 0; j < el.size(); j++) {
					sd = el.get(j);
					al.addAll(sd.getRunTimeSeries());
				}
			}
			axaOrthogonal = getOrthogonalAxes(axaBase[i], true);
			for (int j = 0; j < axaOrthogonal.length; j++) {
				if ((iBaseOrOrthogonal | IConstants.ORTHOGONAL) == IConstants.ORTHOGONAL) {
					el = axaOrthogonal[j].getSeriesDefinitions();
					for (int k = 0; k < el.size(); k++) {
						sd = el.get(k);
						al.addAll(sd.getRunTimeSeries());
					}
				}
			}
		}

		return al.toArray(new Series[al.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ChartWithAxes#isTransposed()
	 */
	public final boolean isTransposed() {
		// #199012 Do not consider isSetOrientation to keep consistent.
		return getOrientation().getValue() == Orientation.HORIZONTAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ChartWithAxes#setTransposed(boolean)
	 */
	public void setTransposed(boolean bTransposed) {
		setOrientation(bTransposed ? Orientation.HORIZONTAL_LITERAL : Orientation.VERTICAL_LITERAL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.Chart#clearSections(int)
	 */
	public final void clearSections(int iSectionType) {
		if ((iSectionType & IConstants.RUN_TIME) == IConstants.RUN_TIME) {
			final Axis[] axaBase = getBaseAxes();
			Axis[] axaOrthogonal;
			Axis axaAncillary;
			SeriesDefinition sd;
			EList<SeriesDefinition> el;

			for (int i = 0; i < axaBase.length; i++) {
				el = axaBase[i].getSeriesDefinitions();
				for (int j = 0; j < el.size(); j++) {
					sd = el.get(j);
					if (sd.getSeries().size() == sd.getRunTimeSeries().size()) {
						for (Series se : sd.getRunTimeSeries()) {
							se.getDataSets().clear();
						}
					} else {
						sd.getSeries().removeAll(sd.getRunTimeSeries());
					}
				}
				axaOrthogonal = getOrthogonalAxes(axaBase[i], true);
				for (int j = 0; j < axaOrthogonal.length; j++) {
					el = axaOrthogonal[j].getSeriesDefinitions();
					for (int k = 0; k < el.size(); k++) {
						sd = el.get(k);
						if (sd.getSeries().size() == sd.getRunTimeSeries().size()) {
							for (Series se : sd.getRunTimeSeries()) {
								se.getDataSets().clear();
							}
						} else {
							sd.getSeries().removeAll(sd.getRunTimeSeries());
						}
					}
				}

				axaAncillary = getAncillaryBaseAxis(axaBase[i]);
				if (axaAncillary != null) {
					el = axaAncillary.getSeriesDefinitions();
					for (int k = 0; k < el.size(); k++) {
						sd = el.get(k);
						if (sd.getSeries().size() == sd.getRunTimeSeries().size()) {
							// sd.getRunTimeSeries( )
							// .removeAll( ( (Series) sd.getRunTimeSeries( )
							// ).getDataSets( ) );
						} else {
							sd.getSeries().removeAll(sd.getRunTimeSeries());
						}
					}
				}
			}
		}
	}

	/**
	 * @generated
	 */
	public ChartWithAxes copyInstance() {
		ChartWithAxesImpl dest = new ChartWithAxesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(ChartWithAxes src) {

		super.set(src);

		// children

		if (src.getAxes() != null) {
			EList<Axis> list = getAxes();
			for (Axis element : src.getAxes()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getWallFill() != null) {
			setWallFill(src.getWallFill().copyInstance());
		}

		if (src.getFloorFill() != null) {
			setFloorFill(src.getFloorFill().copyInstance());
		}

		if (src.getRotation() != null) {
			setRotation(src.getRotation().copyInstance());
		}

		// attributes

		orientation = src.getOrientation();

		orientationESet = src.isSetOrientation();

		unitSpacing = src.getUnitSpacing();

		unitSpacingESet = src.isSetUnitSpacing();

		reverseCategory = src.isReverseCategory();

		reverseCategoryESet = src.isSetReverseCategory();

		studyLayout = src.isStudyLayout();

		studyLayoutESet = src.isSetStudyLayout();

	}

	@Override
	protected SeriesDefinition getBaseSeriesDefinition() {
		return getAxes().get(0).getSeriesDefinitions().get(0);
	}

	@Override
	protected List<SeriesDefinition> getOrthogonalSeriesDefinitions() {
		List<SeriesDefinition> osds = new ArrayList<SeriesDefinition>();
		for (Axis xAxis : getAxes()) {
			for (Axis yAxis : xAxis.getAssociatedAxes()) {
				osds.addAll(yAxis.getSeriesDefinitions());
			}
		}
		return osds;
	}

	@Override
	protected SeriesDefinition getAncillaryBaseSeriesDefinition() {
		Axis baseAxis = getAxes().get(0);
		if (baseAxis.getAncillaryAxes().size() > 0) {
			return baseAxis.getAncillaryAxes().get(0).getSeriesDefinitions().get(0);
		}
		return null;
	}

}