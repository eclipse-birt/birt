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
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
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
 * Line</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getValue
 * <em>Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getLabelAnchor
 * <em>Label Anchor</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getCursor
 * <em>Cursor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MarkerLineImpl extends EObjectImpl implements MarkerLine {

	/**
	 * The cached value of the '{@link #getLineAttributes() <em>Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes lineAttributes;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected DataElement value;

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
	protected MarkerLineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.MARKER_LINE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineAttributes getLineAttributes() {
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetLineAttributes(LineAttributes newLineAttributes, NotificationChain msgs) {
		LineAttributes oldLineAttributes = lineAttributes;
		lineAttributes = newLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
	public void setLineAttributes(LineAttributes newLineAttributes) {
		if (newLineAttributes != lineAttributes) {
			NotificationChain msgs = null;
			if (lineAttributes != null)
				msgs = ((InternalEObject) lineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES, null, msgs);
			if (newLineAttributes != null)
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES, null, msgs);
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataElement getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetValue(DataElement newValue, NotificationChain msgs) {
		DataElement oldValue = value;
		value = newValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.MARKER_LINE__VALUE, oldValue, newValue);
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
	public void setValue(DataElement newValue) {
		if (newValue != value) {
			NotificationChain msgs = null;
			if (value != null)
				msgs = ((InternalEObject) value).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__VALUE, null, msgs);
			if (newValue != null)
				msgs = ((InternalEObject) newValue).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__VALUE, null, msgs);
			msgs = basicSetValue(newValue, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__VALUE, newValue,
					newValue));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
					ComponentPackage.MARKER_LINE__LABEL, oldLabel, newLabel);
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
	public void setLabel(Label newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null)
				msgs = ((InternalEObject) label).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__LABEL, null, msgs);
			if (newLabel != null)
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__LABEL, null, msgs);
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__LABEL, newLabel,
					newLabel));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Anchor getLabelAnchor() {
		return labelAnchor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabelAnchor(Anchor newLabelAnchor) {
		Anchor oldLabelAnchor = labelAnchor;
		labelAnchor = newLabelAnchor == null ? LABEL_ANCHOR_EDEFAULT : newLabelAnchor;
		boolean oldLabelAnchorESet = labelAnchorESet;
		labelAnchorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__LABEL_ANCHOR,
					oldLabelAnchor, labelAnchor, !oldLabelAnchorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLabelAnchor() {
		Anchor oldLabelAnchor = labelAnchor;
		boolean oldLabelAnchorESet = labelAnchorESet;
		labelAnchor = LABEL_ANCHOR_EDEFAULT;
		labelAnchorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.MARKER_LINE__LABEL_ANCHOR,
					oldLabelAnchor, LABEL_ANCHOR_EDEFAULT, oldLabelAnchorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLabelAnchor() {
		return labelAnchorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
					ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
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
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null)
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER, null, msgs);
			if (newFormatSpecifier != null)
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER, null, msgs);
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER,
					newFormatSpecifier, newFormatSpecifier));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this,
					ComponentPackage.MARKER_LINE__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
					ComponentPackage.MARKER_LINE__CURSOR, oldCursor, newCursor);
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
	public void setCursor(Cursor newCursor) {
		if (newCursor != cursor) {
			NotificationChain msgs = null;
			if (cursor != null)
				msgs = ((InternalEObject) cursor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__CURSOR, null, msgs);
			if (newCursor != null)
				msgs = ((InternalEObject) newCursor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.MARKER_LINE__CURSOR, null, msgs);
			msgs = basicSetCursor(newCursor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__CURSOR, newCursor,
					newCursor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case ComponentPackage.MARKER_LINE__VALUE:
			return basicSetValue(null, msgs);
		case ComponentPackage.MARKER_LINE__LABEL:
			return basicSetLabel(null, msgs);
		case ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
		case ComponentPackage.MARKER_LINE__TRIGGERS:
			return ((InternalEList<?>) getTriggers()).basicRemove(otherEnd, msgs);
		case ComponentPackage.MARKER_LINE__CURSOR:
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
		case ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES:
			return getLineAttributes();
		case ComponentPackage.MARKER_LINE__VALUE:
			return getValue();
		case ComponentPackage.MARKER_LINE__LABEL:
			return getLabel();
		case ComponentPackage.MARKER_LINE__LABEL_ANCHOR:
			return getLabelAnchor();
		case ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER:
			return getFormatSpecifier();
		case ComponentPackage.MARKER_LINE__TRIGGERS:
			return getTriggers();
		case ComponentPackage.MARKER_LINE__CURSOR:
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
		case ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.MARKER_LINE__VALUE:
			setValue((DataElement) newValue);
			return;
		case ComponentPackage.MARKER_LINE__LABEL:
			setLabel((Label) newValue);
			return;
		case ComponentPackage.MARKER_LINE__LABEL_ANCHOR:
			setLabelAnchor((Anchor) newValue);
			return;
		case ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
			return;
		case ComponentPackage.MARKER_LINE__TRIGGERS:
			getTriggers().clear();
			getTriggers().addAll((Collection<? extends Trigger>) newValue);
			return;
		case ComponentPackage.MARKER_LINE__CURSOR:
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
		case ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case ComponentPackage.MARKER_LINE__VALUE:
			setValue((DataElement) null);
			return;
		case ComponentPackage.MARKER_LINE__LABEL:
			setLabel((Label) null);
			return;
		case ComponentPackage.MARKER_LINE__LABEL_ANCHOR:
			unsetLabelAnchor();
			return;
		case ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
			return;
		case ComponentPackage.MARKER_LINE__TRIGGERS:
			getTriggers().clear();
			return;
		case ComponentPackage.MARKER_LINE__CURSOR:
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
		case ComponentPackage.MARKER_LINE__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case ComponentPackage.MARKER_LINE__VALUE:
			return value != null;
		case ComponentPackage.MARKER_LINE__LABEL:
			return label != null;
		case ComponentPackage.MARKER_LINE__LABEL_ANCHOR:
			return isSetLabelAnchor();
		case ComponentPackage.MARKER_LINE__FORMAT_SPECIFIER:
			return formatSpecifier != null;
		case ComponentPackage.MARKER_LINE__TRIGGERS:
			return triggers != null && !triggers.isEmpty();
		case ComponentPackage.MARKER_LINE__CURSOR:
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (labelAnchor: "); //$NON-NLS-1$
		if (labelAnchorESet)
			result.append(labelAnchor);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to add a marker line instance to an axis
	 * 
	 * @param ax
	 * @param de
	 */
	public static final MarkerLine create(Axis ax, DataElement de) {
		return create(ax, de, ColorDefinitionImpl.RED());
	}

	/**
	 * A convenience method provided to add a marker line instance to an axis
	 * 
	 * @param ax
	 * @param de
	 * @param lineColor
	 */
	public static final MarkerLine create(Axis ax, DataElement de, ColorDefinition lineColor) {
		final MarkerLine ml = ComponentFactory.eINSTANCE.createMarkerLine();
		ml.setLineAttributes(LineAttributesImpl.create(lineColor, LineStyle.DASHED_LITERAL, 1));
		ml.setValue(de);
		ml.setLabel(LabelImpl.create());
		ml.setLabelAnchor(ax.getOrientation().getValue() == Orientation.HORIZONTAL ? Anchor.NORTH_WEST_LITERAL
				: Anchor.NORTH_EAST_LITERAL);

		if (ax.getOrientation().getValue() == Orientation.HORIZONTAL) {
			ml.getLabel().getCaption().getFont().setRotation(90);
		}

		ax.getMarkerLines().add(ml);
		if (ax.getFormatSpecifier() != null && !ax.isCategoryAxis()) {
			ml.setFormatSpecifier(ax.getFormatSpecifier().copyInstance());
		}
		return ml;
	}

	/**
	 * A convenience method provided to add a marker line instance to an axis
	 * 
	 * @param ax
	 * @param de
	 * @param lineColor
	 */
	public static final MarkerLine createDefault(Axis ax, DataElement de, ColorDefinition lineColor) {
		final MarkerLine ml = ComponentFactory.eINSTANCE.createMarkerLine();
		((MarkerLineImpl) ml).initDefault(ax, de, lineColor);
		ax.getMarkerLines().add(ml);
		return ml;
	}

	private void initDefault(Axis ax, DataElement de, ColorDefinition lineColor) {
		lineAttributes = LineAttributesImpl.createDefault(lineColor, LineStyle.DASHED_LITERAL, 1);
		value = de;
		label = LabelImpl.createDefault();
		labelAnchor = (ax.isSetOrientation() && ax.getOrientation().getValue() == Orientation.HORIZONTAL)
				? Anchor.NORTH_WEST_LITERAL
				: Anchor.NORTH_EAST_LITERAL;

		if (ax.isSetOrientation() && ax.getOrientation().getValue() == Orientation.HORIZONTAL) {
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
	public MarkerLine copyInstance() {
		MarkerLineImpl dest = new MarkerLineImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(MarkerLine src) {

		// children

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getValue() != null) {
			setValue(src.getValue().copyInstance());
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

} // MarkerLineImpl
