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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Font Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getSize <em>Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isBold <em>Bold</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isItalic <em>Italic</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isStrikethrough <em>Strikethrough</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isUnderline <em>Underline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isWordWrap <em>Word Wrap</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getAlignment <em>Alignment</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getRotation <em>Rotation</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class FontDefinitionImpl extends EObjectImpl implements FontDefinition
{

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getName()
     * @generated @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getName()
     * @generated @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getSize() <em>Size</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getSize()
     * @generated @ordered
     */
    protected static final float SIZE_EDEFAULT = 0.0F;

    /**
     * The cached value of the '{@link #getSize() <em>Size</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #getSize()
     * @generated @ordered
     */
    protected float size = SIZE_EDEFAULT;

    /**
     * This is true if the Size attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean sizeESet = false;

    /**
     * The default value of the '{@link #isBold() <em>Bold</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #isBold()
     * @generated @ordered
     */
    protected static final boolean BOLD_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isBold() <em>Bold</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @see #isBold()
     * @generated @ordered
     */
    protected boolean bold = BOLD_EDEFAULT;

    /**
     * This is true if the Bold attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean boldESet = false;

    /**
     * The default value of the '{@link #isItalic() <em>Italic</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isItalic()
     * @generated @ordered
     */
    protected static final boolean ITALIC_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isItalic() <em>Italic</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isItalic()
     * @generated @ordered
     */
    protected boolean italic = ITALIC_EDEFAULT;

    /**
     * This is true if the Italic attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean italicESet = false;

    /**
     * The default value of the '{@link #isStrikethrough() <em>Strikethrough</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isStrikethrough()
     * @generated @ordered
     */
    protected static final boolean STRIKETHROUGH_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStrikethrough() <em>Strikethrough</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isStrikethrough()
     * @generated @ordered
     */
    protected boolean strikethrough = STRIKETHROUGH_EDEFAULT;

    /**
     * This is true if the Strikethrough attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean strikethroughESet = false;

    /**
     * The default value of the '{@link #isUnderline() <em>Underline</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isUnderline()
     * @generated @ordered
     */
    protected static final boolean UNDERLINE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUnderline() <em>Underline</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isUnderline()
     * @generated @ordered
     */
    protected boolean underline = UNDERLINE_EDEFAULT;

    /**
     * This is true if the Underline attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean underlineESet = false;

    /**
     * The default value of the '{@link #isWordWrap() <em>Word Wrap</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isWordWrap()
     * @generated @ordered
     */
    protected static final boolean WORD_WRAP_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isWordWrap() <em>Word Wrap</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isWordWrap()
     * @generated @ordered
     */
    protected boolean wordWrap = WORD_WRAP_EDEFAULT;

    /**
     * This is true if the Word Wrap attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean wordWrapESet = false;

    /**
     * The cached value of the '{@link #getAlignment() <em>Alignment</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getAlignment()
     * @generated @ordered
     */
    protected TextAlignment alignment = null;

    /**
     * The default value of the '{@link #getRotation() <em>Rotation</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getRotation()
     * @generated @ordered
     */
    protected static final double ROTATION_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getRotation() <em>Rotation</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getRotation()
     * @generated @ordered
     */
    protected double rotation = ROTATION_EDEFAULT;

    /**
     * This is true if the Rotation attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean rotationESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected FontDefinitionImpl()
    {
        super();
    }

    /**
     * A convenient constructor that allows partial definition of member variables.
     * 
     * NOTE: Manually written
     * 
     * @param sName
     * @param dSize
     */
    public FontDefinitionImpl(String sName, float fSize)
    {
        super();
        setName(sName);
        setSize(fSize);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected EClass eStaticClass()
    {
        return AttributePackage.eINSTANCE.getFontDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getName()
    {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setName(String newName)
    {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public float getSize()
    {
        return size;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setSize(float newSize)
    {
        float oldSize = size;
        size = newSize;
        boolean oldSizeESet = sizeESet;
        sizeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__SIZE, oldSize,
                size, !oldSizeESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetSize()
    {
        float oldSize = size;
        boolean oldSizeESet = sizeESet;
        size = SIZE_EDEFAULT;
        sizeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__SIZE, oldSize,
                SIZE_EDEFAULT, oldSizeESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetSize()
    {
        return sizeESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isBold()
    {
        return bold;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setBold(boolean newBold)
    {
        boolean oldBold = bold;
        bold = newBold;
        boolean oldBoldESet = boldESet;
        boldESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__BOLD, oldBold,
                bold, !oldBoldESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetBold()
    {
        boolean oldBold = bold;
        boolean oldBoldESet = boldESet;
        bold = BOLD_EDEFAULT;
        boldESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__BOLD, oldBold,
                BOLD_EDEFAULT, oldBoldESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetBold()
    {
        return boldESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isItalic()
    {
        return italic;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setItalic(boolean newItalic)
    {
        boolean oldItalic = italic;
        italic = newItalic;
        boolean oldItalicESet = italicESet;
        italicESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__ITALIC, oldItalic,
                italic, !oldItalicESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetItalic()
    {
        boolean oldItalic = italic;
        boolean oldItalicESet = italicESet;
        italic = ITALIC_EDEFAULT;
        italicESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__ITALIC,
                oldItalic, ITALIC_EDEFAULT, oldItalicESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetItalic()
    {
        return italicESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isStrikethrough()
    {
        return strikethrough;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setStrikethrough(boolean newStrikethrough)
    {
        boolean oldStrikethrough = strikethrough;
        strikethrough = newStrikethrough;
        boolean oldStrikethroughESet = strikethroughESet;
        strikethroughESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__STRIKETHROUGH,
                oldStrikethrough, strikethrough, !oldStrikethroughESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetStrikethrough()
    {
        boolean oldStrikethrough = strikethrough;
        boolean oldStrikethroughESet = strikethroughESet;
        strikethrough = STRIKETHROUGH_EDEFAULT;
        strikethroughESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__STRIKETHROUGH,
                oldStrikethrough, STRIKETHROUGH_EDEFAULT, oldStrikethroughESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetStrikethrough()
    {
        return strikethroughESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isUnderline()
    {
        return underline;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setUnderline(boolean newUnderline)
    {
        boolean oldUnderline = underline;
        underline = newUnderline;
        boolean oldUnderlineESet = underlineESet;
        underlineESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__UNDERLINE,
                oldUnderline, underline, !oldUnderlineESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetUnderline()
    {
        boolean oldUnderline = underline;
        boolean oldUnderlineESet = underlineESet;
        underline = UNDERLINE_EDEFAULT;
        underlineESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__UNDERLINE,
                oldUnderline, UNDERLINE_EDEFAULT, oldUnderlineESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetUnderline()
    {
        return underlineESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isWordWrap()
    {
        return wordWrap;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setWordWrap(boolean newWordWrap)
    {
        boolean oldWordWrap = wordWrap;
        wordWrap = newWordWrap;
        boolean oldWordWrapESet = wordWrapESet;
        wordWrapESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__WORD_WRAP,
                oldWordWrap, wordWrap, !oldWordWrapESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetWordWrap()
    {
        boolean oldWordWrap = wordWrap;
        boolean oldWordWrapESet = wordWrapESet;
        wordWrap = WORD_WRAP_EDEFAULT;
        wordWrapESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__WORD_WRAP,
                oldWordWrap, WORD_WRAP_EDEFAULT, oldWordWrapESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetWordWrap()
    {
        return wordWrapESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public TextAlignment getAlignment()
    {
        return alignment;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetAlignment(TextAlignment newAlignment, NotificationChain msgs)
    {
        TextAlignment oldAlignment = alignment;
        alignment = newAlignment;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                AttributePackage.FONT_DEFINITION__ALIGNMENT, oldAlignment, newAlignment);
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
    public void setAlignment(TextAlignment newAlignment)
    {
        if (newAlignment != alignment)
        {
            NotificationChain msgs = null;
            if (alignment != null)
                msgs = ((InternalEObject) alignment).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - AttributePackage.FONT_DEFINITION__ALIGNMENT, null, msgs);
            if (newAlignment != null)
                msgs = ((InternalEObject) newAlignment).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - AttributePackage.FONT_DEFINITION__ALIGNMENT, null, msgs);
            msgs = basicSetAlignment(newAlignment, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__ALIGNMENT,
                newAlignment, newAlignment));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public double getRotation()
    {
        return rotation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setRotation(double newRotation)
    {
        double oldRotation = rotation;
        rotation = newRotation;
        boolean oldRotationESet = rotationESet;
        rotationESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__ROTATION,
                oldRotation, rotation, !oldRotationESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetRotation()
    {
        double oldRotation = rotation;
        boolean oldRotationESet = rotationESet;
        rotation = ROTATION_EDEFAULT;
        rotationESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__ROTATION,
                oldRotation, ROTATION_EDEFAULT, oldRotationESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetRotation()
    {
        return rotationESet;
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
                case AttributePackage.FONT_DEFINITION__ALIGNMENT:
                    return basicSetAlignment(null, msgs);
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
            case AttributePackage.FONT_DEFINITION__NAME:
                return getName();
            case AttributePackage.FONT_DEFINITION__SIZE:
                return new Float(getSize());
            case AttributePackage.FONT_DEFINITION__BOLD:
                return isBold() ? Boolean.TRUE : Boolean.FALSE;
            case AttributePackage.FONT_DEFINITION__ITALIC:
                return isItalic() ? Boolean.TRUE : Boolean.FALSE;
            case AttributePackage.FONT_DEFINITION__STRIKETHROUGH:
                return isStrikethrough() ? Boolean.TRUE : Boolean.FALSE;
            case AttributePackage.FONT_DEFINITION__UNDERLINE:
                return isUnderline() ? Boolean.TRUE : Boolean.FALSE;
            case AttributePackage.FONT_DEFINITION__WORD_WRAP:
                return isWordWrap() ? Boolean.TRUE : Boolean.FALSE;
            case AttributePackage.FONT_DEFINITION__ALIGNMENT:
                return getAlignment();
            case AttributePackage.FONT_DEFINITION__ROTATION:
                return new Double(getRotation());
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
            case AttributePackage.FONT_DEFINITION__NAME:
                setName((String) newValue);
                return;
            case AttributePackage.FONT_DEFINITION__SIZE:
                setSize(((Float) newValue).floatValue());
                return;
            case AttributePackage.FONT_DEFINITION__BOLD:
                setBold(((Boolean) newValue).booleanValue());
                return;
            case AttributePackage.FONT_DEFINITION__ITALIC:
                setItalic(((Boolean) newValue).booleanValue());
                return;
            case AttributePackage.FONT_DEFINITION__STRIKETHROUGH:
                setStrikethrough(((Boolean) newValue).booleanValue());
                return;
            case AttributePackage.FONT_DEFINITION__UNDERLINE:
                setUnderline(((Boolean) newValue).booleanValue());
                return;
            case AttributePackage.FONT_DEFINITION__WORD_WRAP:
                setWordWrap(((Boolean) newValue).booleanValue());
                return;
            case AttributePackage.FONT_DEFINITION__ALIGNMENT:
                setAlignment((TextAlignment) newValue);
                return;
            case AttributePackage.FONT_DEFINITION__ROTATION:
                setRotation(((Double) newValue).doubleValue());
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
            case AttributePackage.FONT_DEFINITION__NAME:
                setName(NAME_EDEFAULT);
                return;
            case AttributePackage.FONT_DEFINITION__SIZE:
                unsetSize();
                return;
            case AttributePackage.FONT_DEFINITION__BOLD:
                unsetBold();
                return;
            case AttributePackage.FONT_DEFINITION__ITALIC:
                unsetItalic();
                return;
            case AttributePackage.FONT_DEFINITION__STRIKETHROUGH:
                unsetStrikethrough();
                return;
            case AttributePackage.FONT_DEFINITION__UNDERLINE:
                unsetUnderline();
                return;
            case AttributePackage.FONT_DEFINITION__WORD_WRAP:
                unsetWordWrap();
                return;
            case AttributePackage.FONT_DEFINITION__ALIGNMENT:
                setAlignment((TextAlignment) null);
                return;
            case AttributePackage.FONT_DEFINITION__ROTATION:
                unsetRotation();
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
            case AttributePackage.FONT_DEFINITION__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case AttributePackage.FONT_DEFINITION__SIZE:
                return isSetSize();
            case AttributePackage.FONT_DEFINITION__BOLD:
                return isSetBold();
            case AttributePackage.FONT_DEFINITION__ITALIC:
                return isSetItalic();
            case AttributePackage.FONT_DEFINITION__STRIKETHROUGH:
                return isSetStrikethrough();
            case AttributePackage.FONT_DEFINITION__UNDERLINE:
                return isSetUnderline();
            case AttributePackage.FONT_DEFINITION__WORD_WRAP:
                return isSetWordWrap();
            case AttributePackage.FONT_DEFINITION__ALIGNMENT:
                return alignment != null;
            case AttributePackage.FONT_DEFINITION__ROTATION:
                return isSetRotation();
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
        result.append(" (name: ");
        result.append(name);
        result.append(", size: ");
        if (sizeESet)
            result.append(size);
        else
            result.append("<unset>");
        result.append(", bold: ");
        if (boldESet)
            result.append(bold);
        else
            result.append("<unset>");
        result.append(", italic: ");
        if (italicESet)
            result.append(italic);
        else
            result.append("<unset>");
        result.append(", strikethrough: ");
        if (strikethroughESet)
            result.append(strikethrough);
        else
            result.append("<unset>");
        result.append(", underline: ");
        if (underlineESet)
            result.append(underline);
        else
            result.append("<unset>");
        result.append(", wordWrap: ");
        if (wordWrapESet)
            result.append(wordWrap);
        else
            result.append("<unset>");
        result.append(", rotation: ");
        if (rotationESet)
            result.append(rotation);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
     * NOTE: Manually written
     * 
     * @param sName
     * @param fSize
     * @param bBold
     * @param bItalic
     * @param bUnderline
     * @param bStrikethrough
     * @param bWordWrap
     * @param dRotation
     * @param ta
     * @return
     */
    public static final FontDefinition create(String sName, float fSize, boolean bBold, boolean bItalic,
        boolean bUnderline, boolean bStrikethrough, boolean bWordWrap, double dRotation, TextAlignment ta)
    {
        final FontDefinition fd = AttributeFactory.eINSTANCE.createFontDefinition();
        fd.setName(sName);
        fd.setSize(fSize);
        fd.setBold(bBold);
        fd.setItalic(bItalic);
        fd.setUnderline(bUnderline);
        fd.setStrikethrough(bStrikethrough);
        fd.setRotation(dRotation);
        fd.setAlignment(ta);

        return fd;
    }

} //FontDefinitionImpl
