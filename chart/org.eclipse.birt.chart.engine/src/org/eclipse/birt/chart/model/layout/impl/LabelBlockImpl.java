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

package org.eclipse.birt.chart.model.layout.impl;

import java.util.Collection;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Label Block</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LabelBlockImpl#getLabel <em>Label</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class LabelBlockImpl extends BlockImpl implements LabelBlock
{

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected Label label = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected LabelBlockImpl()
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
        return LayoutPackage.eINSTANCE.getLabelBlock();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Label getLabel()
    {
        return label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs)
    {
        Label oldLabel = label;
        label = newLabel;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                LayoutPackage.LABEL_BLOCK__LABEL, oldLabel, newLabel);
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
    public void setLabel(Label newLabel)
    {
        if (newLabel != label)
        {
            NotificationChain msgs = null;
            if (label != null)
                msgs = ((InternalEObject) label).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LABEL_BLOCK__LABEL, null, msgs);
            if (newLabel != null)
                msgs = ((InternalEObject) newLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LABEL_BLOCK__LABEL, null, msgs);
            msgs = basicSetLabel(newLabel, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LABEL_BLOCK__LABEL, newLabel, newLabel));
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
                case LayoutPackage.LABEL_BLOCK__CHILDREN:
                    return ((InternalEList) getChildren()).basicRemove(otherEnd, msgs);
                case LayoutPackage.LABEL_BLOCK__BOUNDS:
                    return basicSetBounds(null, msgs);
                case LayoutPackage.LABEL_BLOCK__INSETS:
                    return basicSetInsets(null, msgs);
                case LayoutPackage.LABEL_BLOCK__MIN_SIZE:
                    return basicSetMinSize(null, msgs);
                case LayoutPackage.LABEL_BLOCK__OUTLINE:
                    return basicSetOutline(null, msgs);
                case LayoutPackage.LABEL_BLOCK__BACKGROUND:
                    return basicSetBackground(null, msgs);
                case LayoutPackage.LABEL_BLOCK__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
                case LayoutPackage.LABEL_BLOCK__LABEL:
                    return basicSetLabel(null, msgs);
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
            case LayoutPackage.LABEL_BLOCK__CHILDREN:
                return getChildren();
            case LayoutPackage.LABEL_BLOCK__BOUNDS:
                return getBounds();
            case LayoutPackage.LABEL_BLOCK__ANCHOR:
                return getAnchor();
            case LayoutPackage.LABEL_BLOCK__STRETCH:
                return getStretch();
            case LayoutPackage.LABEL_BLOCK__INSETS:
                return getInsets();
            case LayoutPackage.LABEL_BLOCK__ROW:
                return new Integer(getRow());
            case LayoutPackage.LABEL_BLOCK__COLUMN:
                return new Integer(getColumn());
            case LayoutPackage.LABEL_BLOCK__ROWSPAN:
                return new Integer(getRowspan());
            case LayoutPackage.LABEL_BLOCK__COLUMNSPAN:
                return new Integer(getColumnspan());
            case LayoutPackage.LABEL_BLOCK__MIN_SIZE:
                return getMinSize();
            case LayoutPackage.LABEL_BLOCK__OUTLINE:
                return getOutline();
            case LayoutPackage.LABEL_BLOCK__BACKGROUND:
                return getBackground();
            case LayoutPackage.LABEL_BLOCK__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case LayoutPackage.LABEL_BLOCK__TRIGGERS:
                return getTriggers();
            case LayoutPackage.LABEL_BLOCK__LABEL:
                return getLabel();
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
            case LayoutPackage.LABEL_BLOCK__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__BOUNDS:
                setBounds((Bounds) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__ANCHOR:
                setAnchor((Anchor) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__STRETCH:
                setStretch((Stretch) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__INSETS:
                setInsets((Insets) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__ROW:
                setRow(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LABEL_BLOCK__COLUMN:
                setColumn(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LABEL_BLOCK__ROWSPAN:
                setRowspan(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LABEL_BLOCK__COLUMNSPAN:
                setColumnspan(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LABEL_BLOCK__MIN_SIZE:
                setMinSize((Size) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__OUTLINE:
                setOutline((LineAttributes) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__BACKGROUND:
                setBackground((Fill) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case LayoutPackage.LABEL_BLOCK__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case LayoutPackage.LABEL_BLOCK__LABEL:
                setLabel((Label) newValue);
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
            case LayoutPackage.LABEL_BLOCK__CHILDREN:
                getChildren().clear();
                return;
            case LayoutPackage.LABEL_BLOCK__BOUNDS:
                setBounds((Bounds) null);
                return;
            case LayoutPackage.LABEL_BLOCK__ANCHOR:
                unsetAnchor();
                return;
            case LayoutPackage.LABEL_BLOCK__STRETCH:
                unsetStretch();
                return;
            case LayoutPackage.LABEL_BLOCK__INSETS:
                setInsets((Insets) null);
                return;
            case LayoutPackage.LABEL_BLOCK__ROW:
                unsetRow();
                return;
            case LayoutPackage.LABEL_BLOCK__COLUMN:
                unsetColumn();
                return;
            case LayoutPackage.LABEL_BLOCK__ROWSPAN:
                unsetRowspan();
                return;
            case LayoutPackage.LABEL_BLOCK__COLUMNSPAN:
                unsetColumnspan();
                return;
            case LayoutPackage.LABEL_BLOCK__MIN_SIZE:
                setMinSize((Size) null);
                return;
            case LayoutPackage.LABEL_BLOCK__OUTLINE:
                setOutline((LineAttributes) null);
                return;
            case LayoutPackage.LABEL_BLOCK__BACKGROUND:
                setBackground((Fill) null);
                return;
            case LayoutPackage.LABEL_BLOCK__VISIBLE:
                unsetVisible();
                return;
            case LayoutPackage.LABEL_BLOCK__TRIGGERS:
                getTriggers().clear();
                return;
            case LayoutPackage.LABEL_BLOCK__LABEL:
                setLabel((Label) null);
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
            case LayoutPackage.LABEL_BLOCK__CHILDREN:
                return children != null && !children.isEmpty();
            case LayoutPackage.LABEL_BLOCK__BOUNDS:
                return bounds != null;
            case LayoutPackage.LABEL_BLOCK__ANCHOR:
                return isSetAnchor();
            case LayoutPackage.LABEL_BLOCK__STRETCH:
                return isSetStretch();
            case LayoutPackage.LABEL_BLOCK__INSETS:
                return insets != null;
            case LayoutPackage.LABEL_BLOCK__ROW:
                return isSetRow();
            case LayoutPackage.LABEL_BLOCK__COLUMN:
                return isSetColumn();
            case LayoutPackage.LABEL_BLOCK__ROWSPAN:
                return isSetRowspan();
            case LayoutPackage.LABEL_BLOCK__COLUMNSPAN:
                return isSetColumnspan();
            case LayoutPackage.LABEL_BLOCK__MIN_SIZE:
                return minSize != null;
            case LayoutPackage.LABEL_BLOCK__OUTLINE:
                return outline != null;
            case LayoutPackage.LABEL_BLOCK__BACKGROUND:
                return background != null;
            case LayoutPackage.LABEL_BLOCK__VISIBLE:
                return isSetVisible();
            case LayoutPackage.LABEL_BLOCK__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case LayoutPackage.LABEL_BLOCK__LABEL:
                return label != null;
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * A convenience method to create an initialized 'LabelBlock' instance
     * 
     * @return
     */
    public static Block create()
    {
        final LabelBlock lb = LayoutFactory.eINSTANCE.createLabelBlock();
        ((LabelBlockImpl) lb).initialize();
        return lb;
    }

    /**
     * Resets all member variables within this object recursively
     * 
     * Note: Manually written
     */
    protected void initialize()
    {
        super.initialize();
        setLabel(LabelImpl.create());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.layout.Block#getPreferredSize(org.eclipse.birt.chart.device.XServer,
     *      org.eclipse.birt.chart.model.Chart)
     */
    public final Size getPreferredSize(IDisplayServer xs, Chart cm)
    {
        final Text tx = getLabel().getCaption();
        final FontDefinition fd = tx.getFont();
        final BoundingBox bb = Methods.computeBox(xs, IConstants.TOP, getLabel(), 0, 0);

        final Size sz = SizeImpl.create(bb.getWidth(), bb.getHeight());
        final Insets ins = getInsets();
        sz.setHeight(sz.getHeight() + ins.getTop() + ins.getBottom());
        sz.setWidth(sz.getWidth() + ins.getLeft() + ins.getRight());
        sz.scale(72d / xs.getDpiResolution());
        return sz;
    }
} //LabelBlockImpl
