/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.model.component.impl;

import java.util.Collection;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Marker
 * Range</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getFill
 * <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getStartValue
 * <em>Start Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getEndValue
 * <em>End Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getLabelAnchor
 * <em>Label Anchor</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getCursor
 * <em>Cursor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarkerRangeImpl extends EObjectImpl implements MarkerRange {

	/**
	 * The cached value of the '{@link #getOutline() <em>Outline</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOutline()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes outline;

	/**
	 * The cached value of the '{@link #getFill() <em>Fill</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getFill()
	 * @generated
	 * @ordered
	 */
	protected Fill fill;

	/**
	 * The cached value of the '{@link #getStartValue() <em>Start Value</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStartValue()
	 * @generated
	 * @ordered
	 */
	protected DataElement startValue;

	/**
	 * The cached value of the '{@link #getEndValue() <em>End Value</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEndValue()
	 * @generated
	 * @ordered
	 */
	protected DataElement endValue;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * The default value of the ' {@link #getLabelAnchor() <em>Label Anchor</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabelAnchor()
	 * @generated
	 * @ordered
	 */
	protected static final Anchor LABEL_ANCHOR_EDEFAULT = Anchor.NORTH_LITERAL;

	/**
	 * The cached value of the '{@link #getLabelAnchor() <em>Label Anchor</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabelAnchor()
	 * @generated
	 * @ordered
	 */
	protected Anchor labelAnchor = LABEL_ANCHOR_EDEFAULT;

	/**
	 * This is true if the Label Anchor attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean labelAnchorESet;

	/**
	 * The cached value of the '{@link #getFormatSpecifier() <em>Format
	 * Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier formatSpecifier;

	/**
	 * The cached value of the '{@link #getTriggers() <em>Triggers</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTriggers()
	 * @generated
	 * @ordered
	 */
	protected EList<Trigger> triggers;

	/**
	 * The cached value of the '{@link #getCursor() <em>Cursor</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCursor()
	 * @generated
	 * @ordered
	 */
	protected Cursor cursor;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MarkerRangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.MARKER_RANGE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes getOutline() {
		return outline;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetOutline(LineAttributes newOutline, NotificationChain msgs) {
		LineAttributes oldOutline = outline;
		outline = newOutline;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__OUTLINE, oldOutline, newOutline);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOutline(LineAttributes newOutline) {
		if (newOutline != outline) {
			NotificationChain msgs = null;
			if (outline != null) {
				msgs = ((InternalEObject) outline).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__OUTLINE, null, msgs);
			}
			if (newOutline != null) {
				msgs = ((InternalEObject) newOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__OUTLINE, null, msgs);
			}
			msgs = basicSetOutline(newOutline, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__OUTLINE, newOutline,
					newOutline));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Fill getFill() {
		return fill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs) {
		Fill oldFill = fill;
		fill = newFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__FILL, oldFill, newFill);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFill(Fill newFill) {
		if (newFill != fill) {
			NotificationChain msgs = null;
			if (fill != null) {
				msgs = ((InternalEObject) fill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__FILL, null, msgs);
			}
			if (newFill != null) {
				msgs = ((InternalEObject) newFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__FILL, null, msgs);
			}
			msgs = basicSetFill(newFill, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__FILL, newFill,
					newFill));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DataElement getStartValue() {
		return startValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetStartValue(DataElement newStartValue, NotificationChain msgs) {
		DataElement oldStartValue = startValue;
		startValue = newStartValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__START_VALUE, oldStartValue, newStartValue);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStartValue(DataElement newStartValue) {
		if (newStartValue != startValue) {
			NotificationChain msgs = null;
			if (startValue != null) {
				msgs = ((InternalEObject) startValue).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__START_VALUE, null, msgs);
			}
			if (newStartValue != null) {
				msgs = ((InternalEObject) newStartValue).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__START_VALUE, null, msgs);
			}
			msgs = basicSetStartValue(newStartValue, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__START_VALUE,
					newStartValue, newStartValue));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DataElement getEndValue() {
		return endValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetEndValue(DataElement newEndValue, NotificationChain msgs) {
		DataElement oldEndValue = endValue;
		endValue = newEndValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__END_VALUE, oldEndValue, newEndValue);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setEndValue(DataElement newEndValue) {
		if (newEndValue != endValue) {
			NotificationChain msgs = null;
			if (endValue != null) {
				msgs = ((InternalEObject) endValue).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__END_VALUE, null, msgs);
			}
			if (newEndValue != null) {
				msgs = ((InternalEObject) newEndValue).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__END_VALUE, null, msgs);
			}
			msgs = basicSetEndValue(newEndValue, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__END_VALUE, newEndValue,
					newEndValue));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs) {
		Label oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__LABEL, oldLabel, newLabel);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLabel(Label newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null) {
				msgs = ((InternalEObject) label).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__LABEL, null, msgs);
			}
			if (newLabel != null) {
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__LABEL, null, msgs);
			}
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__LABEL, newLabel,
					newLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Anchor getLabelAnchor() {
		return labelAnchor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLabelAnchor(Anchor newLabelAnchor) {
		Anchor oldLabelAnchor = labelAnchor;
		labelAnchor = newLabelAnchor == null ? LABEL_ANCHOR_EDEFAULT : newLabelAnchor;
		boolean oldLabelAnchorESet = labelAnchorESet;
		labelAnchorESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__LABEL_ANCHOR,
					oldLabelAnchor, labelAnchor, !oldLabelAnchorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetLabelAnchor() {
		Anchor oldLabelAnchor = labelAnchor;
		boolean oldLabelAnchorESet = labelAnchorESet;
		labelAnchor = LABEL_ANCHOR_EDEFAULT;
		labelAnchorESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.MARKER_RANGE__LABEL_ANCHOR,
					oldLabelAnchor, LABEL_ANCHOR_EDEFAULT, oldLabelAnchorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetLabelAnchor() {
		return labelAnchorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FormatSpecifier getFormatSpecifier() {
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs) {
		FormatSpecifier oldFormatSpecifier = formatSpecifier;
		formatSpecifier = newFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null) {
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER, null, msgs);
			}
			if (newFormatSpecifier != null) {
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER, null, msgs);
			}
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER,
					newFormatSpecifier, newFormatSpecifier));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<>(Trigger.class, this,
					ComponentPackage.MARKER_RANGE__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetCursor(Cursor newCursor, NotificationChain msgs) {
		Cursor oldCursor = cursor;
		cursor = newCursor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_RANGE__CURSOR, oldCursor, newCursor);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCursor(Cursor newCursor) {
		if (newCursor != cursor) {
			NotificationChain msgs = null;
			if (cursor != null) {
				msgs = ((InternalEObject) cursor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__CURSOR, null, msgs);
			}
			if (newCursor != null) {
				msgs = ((InternalEObject) newCursor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_RANGE__CURSOR, null, msgs);
			}
			msgs = basicSetCursor(newCursor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__CURSOR, newCursor,
					newCursor));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.MARKER_RANGE__OUTLINE:
			return basicSetOutline(null, msgs);
		case ComponentPackage.MARKER_RANGE__FILL:
			return basicSetFill(null, msgs);
		case ComponentPackage.MARKER_RANGE__START_VALUE:
			return basicSetStartValue(null, msgs);
		case ComponentPackage.MARKER_RANGE__END_VALUE:
			return basicSetEndValue(null, msgs);
		case ComponentPackage.MARKER_RANGE__LABEL:
			return basicSetLabel(null, msgs);
		case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
		case ComponentPackage.MARKER_RANGE__TRIGGERS:
			return ((InternalEList<?>) getTriggers()).basicRemove(otherEnd, msgs);
		case ComponentPackage.MARKER_RANGE__CURSOR:
			return basicSetCursor(null, msgs);
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
		case ComponentPackage.MARKER_RANGE__OUTLINE:
			return getOutline();
		case ComponentPackage.MARKER_RANGE__FILL:
			return getFill();
		case ComponentPackage.MARKER_RANGE__START_VALUE:
			return getStartValue();
		case ComponentPackage.MARKER_RANGE__END_VALUE:
			return getEndValue();
		case ComponentPackage.MARKER_RANGE__LABEL:
			return getLabel();
		case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
			return getLabelAnchor();
		case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
			return getFormatSpecifier();
		case ComponentPackage.MARKER_RANGE__TRIGGERS:
			return getTriggers();
		case ComponentPackage.MARKER_RANGE__CURSOR:
			return getCursor();
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
		case ComponentPackage.MARKER_RANGE__OUTLINE:
			setOutline((LineAttributes) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__FILL:
			setFill((Fill) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__START_VALUE:
			setStartValue((DataElement) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__END_VALUE:
			setEndValue((DataElement) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__LABEL:
			setLabel((Label) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
			setLabelAnchor((Anchor) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__TRIGGERS:
			getTriggers().clear();
			getTriggers().addAll((Collection<? extends Trigger>) newValue);
			return;
		case ComponentPackage.MARKER_RANGE__CURSOR:
			setCursor((Cursor) newValue);
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
		case ComponentPackage.MARKER_RANGE__OUTLINE:
			setOutline((LineAttributes) null);
			return;
		case ComponentPackage.MARKER_RANGE__FILL:
			setFill((Fill) null);
			return;
		case ComponentPackage.MARKER_RANGE__START_VALUE:
			setStartValue((DataElement) null);
			return;
		case ComponentPackage.MARKER_RANGE__END_VALUE:
			setEndValue((DataElement) null);
			return;
		case ComponentPackage.MARKER_RANGE__LABEL:
			setLabel((Label) null);
			return;
		case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
			unsetLabelAnchor();
			return;
		case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
			return;
		case ComponentPackage.MARKER_RANGE__TRIGGERS:
			getTriggers().clear();
			return;
		case ComponentPackage.MARKER_RANGE__CURSOR:
			setCursor((Cursor) null);
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
		case ComponentPackage.MARKER_RANGE__OUTLINE:
			return outline != null;
		case ComponentPackage.MARKER_RANGE__FILL:
			return fill != null;
		case ComponentPackage.MARKER_RANGE__START_VALUE:
			return startValue != null;
		case ComponentPackage.MARKER_RANGE__END_VALUE:
			return endValue != null;
		case ComponentPackage.MARKER_RANGE__LABEL:
			return label != null;
		case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
			return isSetLabelAnchor();
		case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
			return formatSpecifier != null;
		case ComponentPackage.MARKER_RANGE__TRIGGERS:
			return triggers != null && !triggers.isEmpty();
		case ComponentPackage.MARKER_RANGE__CURSOR:
			return cursor != null;
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
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (labelAnchor: "); //$NON-NLS-1$
		if (labelAnchorESet) {
			result.append(labelAnchor);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to add a marker range instance to an axis
	 *
	 * @param ax        the axis which the marker range will be created on.
	 * @param deStart   start range.
	 * @param deEnd     end range.
	 * @param fillColor fill color.
	 */
	public static final MarkerRange create(Axis ax, DataElement deStart, DataElement deEnd, Fill fillColor) {
		return create(ax, deStart, deEnd, fillColor, null);
	}

	/**
	 * A convenience method provided to add a marker range instance to an axis
	 *
	 * @param ax           the axis which the marker range will be created on.
	 * @param deStart      start range.
	 * @param deEnd        end range.
	 * @param fillColor    fill color.
	 * @param outlineColor outline color.
	 */
	public static final MarkerRange create(Axis ax, DataElement deStart, DataElement deEnd, Fill fillColor,
			ColorDefinition outlineColor) {
		final MarkerRange mr = ComponentFactory.eINSTANCE.createMarkerRange();
		final LineAttributes liaOutline = LineAttributesImpl.create(outlineColor, LineStyle.SOLID_LITERAL, 1);
		mr.setOutline(liaOutline);
		mr.setFill(fillColor);
		mr.setStartValue(deStart);
		mr.setEndValue(deEnd);
		mr.setLabel(LabelImpl.create());

		// mr.setLabelPosition(Position.INSIDE_LITERAL);
		mr.setLabelAnchor(ax.getOrientation().getValue() == Orientation.HORIZONTAL ? Anchor.NORTH_EAST_LITERAL
				: Anchor.NORTH_WEST_LITERAL);

		if (ax.getOrientation().getValue() == Orientation.VERTICAL) {
			mr.getLabel().getCaption().getFont().setRotation(90);
		}

		ax.getMarkerRanges().add(mr);
		if (ax.getFormatSpecifier() != null && !ax.isCategoryAxis()) {
			mr.setFormatSpecifier(ax.getFormatSpecifier().copyInstance());
		}
		return mr;
	}

	/**
	 * A convenience method provided to add a marker range instance to an axis
	 *
	 * @param ax           the axis which the marker range will be created on.
	 * @param deStart      start range.
	 * @param deEnd        end range.
	 * @param fillColor    fill color.
	 * @param outlineColor outline color.
	 */
	public static final MarkerRange createDefault(Axis ax, DataElement deStart, DataElement deEnd, Fill fillColor,
			ColorDefinition outlineColor) {
		final MarkerRange mr = ComponentFactory.eINSTANCE.createMarkerRange();
		((MarkerRangeImpl) mr).initDefault(ax, deStart, deEnd, fillColor, outlineColor);
		ax.getMarkerRanges().add(mr);
		return mr;
	}

	private void initDefault(Axis ax, DataElement deStart, DataElement deEnd, Fill fillColor,
			ColorDefinition outlineColor) {
		outline = LineAttributesImpl.createDefault(outlineColor, LineStyle.SOLID_LITERAL, 1);
		fill = fillColor;
		startValue = deStart;
		endValue = deEnd;
		label = LabelImpl.createDefault();

		labelAnchor = (ax.isSetOrientation() && ax.getOrientation().getValue() == Orientation.HORIZONTAL)
				? Anchor.NORTH_EAST_LITERAL
				: Anchor.NORTH_WEST_LITERAL;

		if (ax.isSetOrientation() && ax.getOrientation().getValue() == Orientation.VERTICAL) {
			try {
				ChartElementUtil.setDefaultValue(getLabel().getCaption().getFont(), "rotation", 90); //$NON-NLS-1$
			} catch (ChartException e) {
				// This should not happens in here.
			}
		}

		if (ax.getFormatSpecifier() != null && ax.isSetCategoryAxis() && !ax.isCategoryAxis()) {
			formatSpecifier = ax.getFormatSpecifier().copyInstance();
		}
	}

	/**
	 * @generated
	 */
	@Override
	public MarkerRange copyInstance() {
		MarkerRangeImpl dest = new MarkerRangeImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(MarkerRange src) {

		// children

		if (src.getOutline() != null) {
			setOutline(src.getOutline().copyInstance());
		}

		if (src.getFill() != null) {
			setFill(src.getFill().copyInstance());
		}

		if (src.getStartValue() != null) {
			setStartValue(src.getStartValue().copyInstance());
		}

		if (src.getEndValue() != null) {
			setEndValue(src.getEndValue().copyInstance());
		}

		if (src.getLabel() != null) {
			setLabel(src.getLabel().copyInstance());
		}

		if (src.getFormatSpecifier() != null) {
			setFormatSpecifier(src.getFormatSpecifier().copyInstance());
		}

		if (src.getTriggers() != null) {
			EList<Trigger> list = getTriggers();
			for (Trigger element : src.getTriggers()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getCursor() != null) {
			setCursor(src.getCursor().copyInstance());
		}

		// attributes

		labelAnchor = src.getLabelAnchor();

		labelAnchorESet = src.isSetLabelAnchor();

	}

} // MarkerRangeImpl
