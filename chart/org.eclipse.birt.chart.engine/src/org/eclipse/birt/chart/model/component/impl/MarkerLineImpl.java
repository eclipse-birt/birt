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

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Marker Line</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getAttributes <em>Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getPosition <em>Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getAssociatedLabel <em>Associated Label</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerLineImpl#getLabelPosition <em>Label Position</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MarkerLineImpl extends EObjectImpl implements MarkerLine
{

    /**
     * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAttributes()
     * @generated
     * @ordered
     */
    protected LineAttributes attributes = null;

    /**
     * The default value of the '{@link #getPosition() <em>Position</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected static final Object POSITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected Object position = POSITION_EDEFAULT;

    /**
     * The cached value of the '{@link #getAssociatedLabel() <em>Associated Label</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAssociatedLabel()
     * @generated
     * @ordered
     */
    protected Label associatedLabel = null;

    /**
     * The default value of the '{@link #getLabelPosition() <em>Label Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getLabelPosition()
     * @generated
     * @ordered
     */
    protected static final String LABEL_POSITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabelPosition() <em>Label Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getLabelPosition()
     * @generated
     * @ordered
     */
    protected String labelPosition = LABEL_POSITION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MarkerLineImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected EClass eStaticClass()
    {
        return ComponentPackage.eINSTANCE.getMarkerLine();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LineAttributes getAttributes()
    {
        return attributes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetAttributes(LineAttributes newAttributes, NotificationChain msgs)
    {
        LineAttributes oldAttributes = attributes;
        attributes = newAttributes;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_LINE__ATTRIBUTES, oldAttributes, newAttributes);
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
    public void setAttributes(LineAttributes newAttributes)
    {
        if (newAttributes != attributes)
        {
            NotificationChain msgs = null;
            if (attributes != null)
                msgs = ((InternalEObject) attributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_LINE__ATTRIBUTES, null, msgs);
            if (newAttributes != null)
                msgs = ((InternalEObject) newAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_LINE__ATTRIBUTES, null, msgs);
            msgs = basicSetAttributes(newAttributes, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__ATTRIBUTES,
                newAttributes, newAttributes));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object getPosition()
    {
        return position;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setPosition(Object newPosition)
    {
        Object oldPosition = position;
        position = newPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__POSITION, oldPosition,
                position));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Label getAssociatedLabel()
    {
        return associatedLabel;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetAssociatedLabel(Label newAssociatedLabel, NotificationChain msgs)
    {
        Label oldAssociatedLabel = associatedLabel;
        associatedLabel = newAssociatedLabel;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL, oldAssociatedLabel, newAssociatedLabel);
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
    public void setAssociatedLabel(Label newAssociatedLabel)
    {
        if (newAssociatedLabel != associatedLabel)
        {
            NotificationChain msgs = null;
            if (associatedLabel != null)
                msgs = ((InternalEObject) associatedLabel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL, null, msgs);
            if (newAssociatedLabel != null)
                msgs = ((InternalEObject) newAssociatedLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL, null, msgs);
            msgs = basicSetAssociatedLabel(newAssociatedLabel, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL,
                newAssociatedLabel, newAssociatedLabel));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getLabelPosition()
    {
        return labelPosition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setLabelPosition(String newLabelPosition)
    {
        String oldLabelPosition = labelPosition;
        labelPosition = newLabelPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_LINE__LABEL_POSITION,
                oldLabelPosition, labelPosition));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass,
        NotificationChain msgs)
    {
        if (featureID >= 0)
        {
            switch (eDerivedStructuralFeatureID(featureID, baseClass))
            {
                case ComponentPackage.MARKER_LINE__ATTRIBUTES:
                    return basicSetAttributes(null, msgs);
                case ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL:
                    return basicSetAssociatedLabel(null, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case ComponentPackage.MARKER_LINE__ATTRIBUTES:
                return getAttributes();
            case ComponentPackage.MARKER_LINE__POSITION:
                return getPosition();
            case ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL:
                return getAssociatedLabel();
            case ComponentPackage.MARKER_LINE__LABEL_POSITION:
                return getLabelPosition();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case ComponentPackage.MARKER_LINE__ATTRIBUTES:
                setAttributes((LineAttributes) newValue);
                return;
            case ComponentPackage.MARKER_LINE__POSITION:
                setPosition((Object) newValue);
                return;
            case ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL:
                setAssociatedLabel((Label) newValue);
                return;
            case ComponentPackage.MARKER_LINE__LABEL_POSITION:
                setLabelPosition((String) newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case ComponentPackage.MARKER_LINE__ATTRIBUTES:
                setAttributes((LineAttributes) null);
                return;
            case ComponentPackage.MARKER_LINE__POSITION:
                setPosition(POSITION_EDEFAULT);
                return;
            case ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL:
                setAssociatedLabel((Label) null);
                return;
            case ComponentPackage.MARKER_LINE__LABEL_POSITION:
                setLabelPosition(LABEL_POSITION_EDEFAULT);
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case ComponentPackage.MARKER_LINE__ATTRIBUTES:
                return attributes != null;
            case ComponentPackage.MARKER_LINE__POSITION:
                return POSITION_EDEFAULT == null ? position != null : !POSITION_EDEFAULT.equals(position);
            case ComponentPackage.MARKER_LINE__ASSOCIATED_LABEL:
                return associatedLabel != null;
            case ComponentPackage.MARKER_LINE__LABEL_POSITION:
                return LABEL_POSITION_EDEFAULT == null ? labelPosition != null : !LABEL_POSITION_EDEFAULT
                    .equals(labelPosition);
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String toString()
    {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (position: ");
        result.append(position);
        result.append(", labelPosition: ");
        result.append(labelPosition);
        result.append(')');
        return result.toString();
    }

    /**
     * A convenience method provided to add a marker line instance to an axis
     * 
     * @param ax
     * @param de
     */
    public static final MarkerLine create(Axis ax, DataElement de)
    {
        final MarkerLine ml = ComponentFactory.eINSTANCE.createMarkerLine();
        ml.setAttributes(LineAttributesImpl.create(ColorDefinitionImpl.RED(), LineStyle.DASHED_LITERAL, 1));
        ml.setPosition(de);
        ml.setAssociatedLabel(LabelImpl.create());
        /*ml.setAnchor(
            ax.getOrientation().getValue() == Orientation.HORIZONTAL
            ? Anchor.NORTH_EAST_LITERAL
            : Anchor.NORTH_WEST_LITERAL
        );*/
        ax.getMarkerLine().add(ml);
        //ml.setFormatSpecifier(ax.getFormatSpecifier());
        return ml;
    }
    
} //MarkerLineImpl
