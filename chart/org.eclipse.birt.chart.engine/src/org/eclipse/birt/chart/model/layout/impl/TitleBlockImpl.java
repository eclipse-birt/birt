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

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Title Block</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class TitleBlockImpl extends LabelBlockImpl implements TitleBlock
{

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TitleBlockImpl()
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
        return LayoutPackage.eINSTANCE.getTitleBlock();
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
                case LayoutPackage.TITLE_BLOCK__CHILDREN:
                    return ((InternalEList) getChildren()).basicRemove(otherEnd, msgs);
                case LayoutPackage.TITLE_BLOCK__BOUNDS:
                    return basicSetBounds(null, msgs);
                case LayoutPackage.TITLE_BLOCK__INSETS:
                    return basicSetInsets(null, msgs);
                case LayoutPackage.TITLE_BLOCK__MIN_SIZE:
                    return basicSetMinSize(null, msgs);
                case LayoutPackage.TITLE_BLOCK__OUTLINE:
                    return basicSetOutline(null, msgs);
                case LayoutPackage.TITLE_BLOCK__BACKGROUND:
                    return basicSetBackground(null, msgs);
                case LayoutPackage.TITLE_BLOCK__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
                case LayoutPackage.TITLE_BLOCK__LABEL:
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
            case LayoutPackage.TITLE_BLOCK__CHILDREN:
                return getChildren();
            case LayoutPackage.TITLE_BLOCK__BOUNDS:
                return getBounds();
            case LayoutPackage.TITLE_BLOCK__ANCHOR:
                return getAnchor();
            case LayoutPackage.TITLE_BLOCK__STRETCH:
                return getStretch();
            case LayoutPackage.TITLE_BLOCK__INSETS:
                return getInsets();
            case LayoutPackage.TITLE_BLOCK__ROW:
                return new Integer(getRow());
            case LayoutPackage.TITLE_BLOCK__COLUMN:
                return new Integer(getColumn());
            case LayoutPackage.TITLE_BLOCK__ROWSPAN:
                return new Integer(getRowspan());
            case LayoutPackage.TITLE_BLOCK__COLUMNSPAN:
                return new Integer(getColumnspan());
            case LayoutPackage.TITLE_BLOCK__MIN_SIZE:
                return getMinSize();
            case LayoutPackage.TITLE_BLOCK__OUTLINE:
                return getOutline();
            case LayoutPackage.TITLE_BLOCK__BACKGROUND:
                return getBackground();
            case LayoutPackage.TITLE_BLOCK__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case LayoutPackage.TITLE_BLOCK__TRIGGERS:
                return getTriggers();
            case LayoutPackage.TITLE_BLOCK__LABEL:
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
            case LayoutPackage.TITLE_BLOCK__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__BOUNDS:
                setBounds((Bounds) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__ANCHOR:
                setAnchor((Anchor) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__STRETCH:
                setStretch((Stretch) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__INSETS:
                setInsets((Insets) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__ROW:
                setRow(((Integer) newValue).intValue());
                return;
            case LayoutPackage.TITLE_BLOCK__COLUMN:
                setColumn(((Integer) newValue).intValue());
                return;
            case LayoutPackage.TITLE_BLOCK__ROWSPAN:
                setRowspan(((Integer) newValue).intValue());
                return;
            case LayoutPackage.TITLE_BLOCK__COLUMNSPAN:
                setColumnspan(((Integer) newValue).intValue());
                return;
            case LayoutPackage.TITLE_BLOCK__MIN_SIZE:
                setMinSize((Size) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__OUTLINE:
                setOutline((LineAttributes) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__BACKGROUND:
                setBackground((Fill) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case LayoutPackage.TITLE_BLOCK__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case LayoutPackage.TITLE_BLOCK__LABEL:
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
            case LayoutPackage.TITLE_BLOCK__CHILDREN:
                getChildren().clear();
                return;
            case LayoutPackage.TITLE_BLOCK__BOUNDS:
                setBounds((Bounds) null);
                return;
            case LayoutPackage.TITLE_BLOCK__ANCHOR:
                unsetAnchor();
                return;
            case LayoutPackage.TITLE_BLOCK__STRETCH:
                unsetStretch();
                return;
            case LayoutPackage.TITLE_BLOCK__INSETS:
                setInsets((Insets) null);
                return;
            case LayoutPackage.TITLE_BLOCK__ROW:
                unsetRow();
                return;
            case LayoutPackage.TITLE_BLOCK__COLUMN:
                unsetColumn();
                return;
            case LayoutPackage.TITLE_BLOCK__ROWSPAN:
                unsetRowspan();
                return;
            case LayoutPackage.TITLE_BLOCK__COLUMNSPAN:
                unsetColumnspan();
                return;
            case LayoutPackage.TITLE_BLOCK__MIN_SIZE:
                setMinSize((Size) null);
                return;
            case LayoutPackage.TITLE_BLOCK__OUTLINE:
                setOutline((LineAttributes) null);
                return;
            case LayoutPackage.TITLE_BLOCK__BACKGROUND:
                setBackground((Fill) null);
                return;
            case LayoutPackage.TITLE_BLOCK__VISIBLE:
                unsetVisible();
                return;
            case LayoutPackage.TITLE_BLOCK__TRIGGERS:
                getTriggers().clear();
                return;
            case LayoutPackage.TITLE_BLOCK__LABEL:
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
            case LayoutPackage.TITLE_BLOCK__CHILDREN:
                return children != null && !children.isEmpty();
            case LayoutPackage.TITLE_BLOCK__BOUNDS:
                return bounds != null;
            case LayoutPackage.TITLE_BLOCK__ANCHOR:
                return isSetAnchor();
            case LayoutPackage.TITLE_BLOCK__STRETCH:
                return isSetStretch();
            case LayoutPackage.TITLE_BLOCK__INSETS:
                return insets != null;
            case LayoutPackage.TITLE_BLOCK__ROW:
                return isSetRow();
            case LayoutPackage.TITLE_BLOCK__COLUMN:
                return isSetColumn();
            case LayoutPackage.TITLE_BLOCK__ROWSPAN:
                return isSetRowspan();
            case LayoutPackage.TITLE_BLOCK__COLUMNSPAN:
                return isSetColumnspan();
            case LayoutPackage.TITLE_BLOCK__MIN_SIZE:
                return minSize != null;
            case LayoutPackage.TITLE_BLOCK__OUTLINE:
                return outline != null;
            case LayoutPackage.TITLE_BLOCK__BACKGROUND:
                return background != null;
            case LayoutPackage.TITLE_BLOCK__VISIBLE:
                return isSetVisible();
            case LayoutPackage.TITLE_BLOCK__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case LayoutPackage.TITLE_BLOCK__LABEL:
                return label != null;
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * A convenience method to create an initialized 'TitleBlock' instance
     * 
     * @return
     */
    public static Block create()
    {
        final TitleBlock tb = LayoutFactory.eINSTANCE.createTitleBlock();
        ((TitleBlockImpl) tb).initialize();
        return tb;
    }
} //TitleBlockImpl
