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

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Marker Range</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getOutline <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getFill <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getStartPosition <em>Start Position</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getEndPosition <em>End Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getAssociatedLabel <em>Associated Label</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getLabelPosition <em>Label Position</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MarkerRangeImpl extends EObjectImpl implements MarkerRange
{

    /**
     * The cached value of the '{@link #getOutline() <em>Outline</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getOutline()
     * @generated
     * @ordered
     */
    protected LineAttributes outline = null;

    /**
     * The cached value of the '{@link #getFill() <em>Fill</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getFill()
     * @generated
     * @ordered
     */
    protected Fill fill = null;

    /**
     * The default value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getStartPosition()
     * @generated
     * @ordered
     */
    protected static final Object START_POSITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getStartPosition()
     * @generated
     * @ordered
     */
    protected Object startPosition = START_POSITION_EDEFAULT;

    /**
     * The default value of the '{@link #getEndPosition() <em>End Position</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getEndPosition()
     * @generated
     * @ordered
     */
    protected static final Object END_POSITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEndPosition() <em>End Position</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getEndPosition()
     * @generated
     * @ordered
     */
    protected Object endPosition = END_POSITION_EDEFAULT;

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
    protected MarkerRangeImpl()
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
        return ComponentPackage.eINSTANCE.getMarkerRange();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LineAttributes getOutline()
    {
        return outline;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetOutline(LineAttributes newOutline, NotificationChain msgs)
    {
        LineAttributes oldOutline = outline;
        outline = newOutline;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__OUTLINE, oldOutline, newOutline);
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
    public void setOutline(LineAttributes newOutline)
    {
        if (newOutline != outline)
        {
            NotificationChain msgs = null;
            if (outline != null)
                msgs = ((InternalEObject) outline).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__OUTLINE, null, msgs);
            if (newOutline != null)
                msgs = ((InternalEObject) newOutline).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__OUTLINE, null, msgs);
            msgs = basicSetOutline(newOutline, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__OUTLINE, newOutline,
                newOutline));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Fill getFill()
    {
        return fill;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs)
    {
        Fill oldFill = fill;
        fill = newFill;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__FILL, oldFill, newFill);
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
    public void setFill(Fill newFill)
    {
        if (newFill != fill)
        {
            NotificationChain msgs = null;
            if (fill != null)
                msgs = ((InternalEObject) fill).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__FILL, null, msgs);
            if (newFill != null)
                msgs = ((InternalEObject) newFill).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__FILL, null, msgs);
            msgs = basicSetFill(newFill, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__FILL, newFill, newFill));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object getStartPosition()
    {
        return startPosition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setStartPosition(Object newStartPosition)
    {
        Object oldStartPosition = startPosition;
        startPosition = newStartPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__START_POSITION,
                oldStartPosition, startPosition));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object getEndPosition()
    {
        return endPosition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setEndPosition(Object newEndPosition)
    {
        Object oldEndPosition = endPosition;
        endPosition = newEndPosition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__END_POSITION,
                oldEndPosition, endPosition));
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
                ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL, oldAssociatedLabel, newAssociatedLabel);
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
                    - ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL, null, msgs);
            if (newAssociatedLabel != null)
                msgs = ((InternalEObject) newAssociatedLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL, null, msgs);
            msgs = basicSetAssociatedLabel(newAssociatedLabel, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__LABEL_POSITION,
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
                case ComponentPackage.MARKER_RANGE__OUTLINE:
                    return basicSetOutline(null, msgs);
                case ComponentPackage.MARKER_RANGE__FILL:
                    return basicSetFill(null, msgs);
                case ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL:
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                return getOutline();
            case ComponentPackage.MARKER_RANGE__FILL:
                return getFill();
            case ComponentPackage.MARKER_RANGE__START_POSITION:
                return getStartPosition();
            case ComponentPackage.MARKER_RANGE__END_POSITION:
                return getEndPosition();
            case ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL:
                return getAssociatedLabel();
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                setOutline((LineAttributes) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__FILL:
                setFill((Fill) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__START_POSITION:
                setStartPosition((Object) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__END_POSITION:
                setEndPosition((Object) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL:
                setAssociatedLabel((Label) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                setOutline((LineAttributes) null);
                return;
            case ComponentPackage.MARKER_RANGE__FILL:
                setFill((Fill) null);
                return;
            case ComponentPackage.MARKER_RANGE__START_POSITION:
                setStartPosition(START_POSITION_EDEFAULT);
                return;
            case ComponentPackage.MARKER_RANGE__END_POSITION:
                setEndPosition(END_POSITION_EDEFAULT);
                return;
            case ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL:
                setAssociatedLabel((Label) null);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                return outline != null;
            case ComponentPackage.MARKER_RANGE__FILL:
                return fill != null;
            case ComponentPackage.MARKER_RANGE__START_POSITION:
                return START_POSITION_EDEFAULT == null ? startPosition != null : !START_POSITION_EDEFAULT
                    .equals(startPosition);
            case ComponentPackage.MARKER_RANGE__END_POSITION:
                return END_POSITION_EDEFAULT == null ? endPosition != null : !END_POSITION_EDEFAULT.equals(endPosition);
            case ComponentPackage.MARKER_RANGE__ASSOCIATED_LABEL:
                return associatedLabel != null;
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
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
        result.append(" (startPosition: ");
        result.append(startPosition);
        result.append(", endPosition: ");
        result.append(endPosition);
        result.append(", labelPosition: ");
        result.append(labelPosition);
        result.append(')');
        return result.toString();
    }


    /**
     * A convenience method provided to add a marker range instance to an axis
     * 
     * @param ax
     * @param de
     */
    public static final MarkerRange create(Axis ax, DataElement deStart, DataElement deEnd, Fill f)
    {
        final MarkerRange mr = ComponentFactory.eINSTANCE.createMarkerRange();
        final LineAttributes liaOutline = LineAttributesImpl.create(null, LineStyle.SOLID_LITERAL, 1);
        mr.setOutline(liaOutline);
        mr.setFill(f);
        mr.setStartPosition(deStart);
        mr.setEndPosition(deEnd);
        mr.setAssociatedLabel(LabelImpl.create());
        /*mr.setAnchor(
            ax.getOrientation().getValue() == Orientation.HORIZONTAL
            ? Anchor.NORTH_EAST_LITERAL
            : Anchor.NORTH_WEST_LITERAL
        );*/
        ax.getMarkerRange().add(mr);
        //mr.setFormatSpecifier(ax.getFormatSpecifier());
        return mr;
    }
    
} //MarkerRangeImpl
