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

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Objects;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Font
 * Definition</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getName
 * <em>Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getSize
 * <em>Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isBold
 * <em>Bold</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isItalic
 * <em>Italic</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isStrikethrough
 * <em>Strikethrough</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isUnderline
 * <em>Underline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#isWordWrap
 * <em>Word Wrap</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getAlignment
 * <em>Alignment</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl#getRotation
 * <em>Rotation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FontDefinitionImpl extends EObjectImpl implements FontDefinition {

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected static final float SIZE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
	protected float size = SIZE_EDEFAULT;

	/**
	 * This is true if the Size attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean sizeESet;

	/**
	 * The default value of the '{@link #isBold() <em>Bold</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isBold()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BOLD_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBold() <em>Bold</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isBold()
	 * @generated
	 * @ordered
	 */
	protected boolean bold = BOLD_EDEFAULT;

	/**
	 * This is true if the Bold attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean boldESet;

	/**
	 * The default value of the '{@link #isItalic() <em>Italic</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isItalic()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ITALIC_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isItalic() <em>Italic</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isItalic()
	 * @generated
	 * @ordered
	 */
	protected boolean italic = ITALIC_EDEFAULT;

	/**
	 * This is true if the Italic attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean italicESet;

	/**
	 * The default value of the ' {@link #isStrikethrough() <em>Strikethrough</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isStrikethrough()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STRIKETHROUGH_EDEFAULT = false;

	/**
	 * The cached value of the ' {@link #isStrikethrough() <em>Strikethrough</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isStrikethrough()
	 * @generated
	 * @ordered
	 */
	protected boolean strikethrough = STRIKETHROUGH_EDEFAULT;

	/**
	 * This is true if the Strikethrough attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean strikethroughESet;

	/**
	 * The default value of the '{@link #isUnderline() <em>Underline</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isUnderline()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UNDERLINE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUnderline() <em>Underline</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isUnderline()
	 * @generated
	 * @ordered
	 */
	protected boolean underline = UNDERLINE_EDEFAULT;

	/**
	 * This is true if the Underline attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean underlineESet;

	/**
	 * The default value of the '{@link #isWordWrap() <em>Word Wrap</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isWordWrap()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WORD_WRAP_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWordWrap() <em>Word Wrap</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isWordWrap()
	 * @generated
	 * @ordered
	 */
	protected boolean wordWrap = WORD_WRAP_EDEFAULT;

	/**
	 * This is true if the Word Wrap attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean wordWrapESet;

	/**
	 * The cached value of the '{@link #getAlignment() <em>Alignment</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getAlignment()
	 * @generated
	 * @ordered
	 */
	protected TextAlignment alignment;

	/**
	 * The default value of the '{@link #getRotation() <em>Rotation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getRotation()
	 * @generated
	 * @ordered
	 */
	protected static final double ROTATION_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRotation() <em>Rotation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getRotation()
	 * @generated
	 * @ordered
	 */
	protected double rotation = ROTATION_EDEFAULT;

	/**
	 * This is true if the Rotation attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean rotationESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected FontDefinitionImpl() {
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
	public FontDefinitionImpl(String sName, float fSize) {
		super();
		setName(sName);
		setSize(fSize);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.FONT_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__NAME, oldName,
					name));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public float getSize() {
		return size;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setSize(float newSize) {
		float oldSize = size;
		size = newSize;
		boolean oldSizeESet = sizeESet;
		sizeESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__SIZE, oldSize, size,
					!oldSizeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetSize() {
		float oldSize = size;
		boolean oldSizeESet = sizeESet;
		size = SIZE_EDEFAULT;
		sizeESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__SIZE, oldSize,
					SIZE_EDEFAULT, oldSizeESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetSize() {
		return sizeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isBold() {
		return bold;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setBold(boolean newBold) {
		boolean oldBold = bold;
		bold = newBold;
		boolean oldBoldESet = boldESet;
		boldESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__BOLD, oldBold, bold,
					!oldBoldESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetBold() {
		boolean oldBold = bold;
		boolean oldBoldESet = boldESet;
		bold = BOLD_EDEFAULT;
		boldESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__BOLD, oldBold,
					BOLD_EDEFAULT, oldBoldESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetBold() {
		return boldESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isItalic() {
		return italic;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setItalic(boolean newItalic) {
		boolean oldItalic = italic;
		italic = newItalic;
		boolean oldItalicESet = italicESet;
		italicESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__ITALIC, oldItalic,
					italic, !oldItalicESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetItalic() {
		boolean oldItalic = italic;
		boolean oldItalicESet = italicESet;
		italic = ITALIC_EDEFAULT;
		italicESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__ITALIC, oldItalic,
					ITALIC_EDEFAULT, oldItalicESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetItalic() {
		return italicESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isStrikethrough() {
		return strikethrough;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStrikethrough(boolean newStrikethrough) {
		boolean oldStrikethrough = strikethrough;
		strikethrough = newStrikethrough;
		boolean oldStrikethroughESet = strikethroughESet;
		strikethroughESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__STRIKETHROUGH,
					oldStrikethrough, strikethrough, !oldStrikethroughESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetStrikethrough() {
		boolean oldStrikethrough = strikethrough;
		boolean oldStrikethroughESet = strikethroughESet;
		strikethrough = STRIKETHROUGH_EDEFAULT;
		strikethroughESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__STRIKETHROUGH,
					oldStrikethrough, STRIKETHROUGH_EDEFAULT, oldStrikethroughESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetStrikethrough() {
		return strikethroughESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isUnderline() {
		return underline;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setUnderline(boolean newUnderline) {
		boolean oldUnderline = underline;
		underline = newUnderline;
		boolean oldUnderlineESet = underlineESet;
		underlineESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__UNDERLINE,
					oldUnderline, underline, !oldUnderlineESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetUnderline() {
		boolean oldUnderline = underline;
		boolean oldUnderlineESet = underlineESet;
		underline = UNDERLINE_EDEFAULT;
		underlineESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__UNDERLINE,
					oldUnderline, UNDERLINE_EDEFAULT, oldUnderlineESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetUnderline() {
		return underlineESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isWordWrap() {
		return wordWrap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setWordWrap(boolean newWordWrap) {
		boolean oldWordWrap = wordWrap;
		wordWrap = newWordWrap;
		boolean oldWordWrapESet = wordWrapESet;
		wordWrapESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__WORD_WRAP,
					oldWordWrap, wordWrap, !oldWordWrapESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetWordWrap() {
		boolean oldWordWrap = wordWrap;
		boolean oldWordWrapESet = wordWrapESet;
		wordWrap = WORD_WRAP_EDEFAULT;
		wordWrapESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__WORD_WRAP,
					oldWordWrap, WORD_WRAP_EDEFAULT, oldWordWrapESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetWordWrap() {
		return wordWrapESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TextAlignment getAlignment() {
		return alignment;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetAlignment(TextAlignment newAlignment, NotificationChain msgs) {
		TextAlignment oldAlignment = alignment;
		alignment = newAlignment;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					AttributePackage.FONT_DEFINITION__ALIGNMENT, oldAlignment, newAlignment);
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
	public void setAlignment(TextAlignment newAlignment) {
		if (newAlignment != alignment) {
			NotificationChain msgs = null;
			if (alignment != null) {
				msgs = ((InternalEObject) alignment).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.FONT_DEFINITION__ALIGNMENT, null, msgs);
			}
			if (newAlignment != null) {
				msgs = ((InternalEObject) newAlignment).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - AttributePackage.FONT_DEFINITION__ALIGNMENT, null, msgs);
			}
			msgs = basicSetAlignment(newAlignment, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__ALIGNMENT,
					newAlignment, newAlignment));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getRotation() {
		return rotation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRotation(double newRotation) {
		double oldRotation = rotation;
		rotation = newRotation;
		boolean oldRotationESet = rotationESet;
		rotationESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.FONT_DEFINITION__ROTATION,
					oldRotation, rotation, !oldRotationESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetRotation() {
		double oldRotation = rotation;
		boolean oldRotationESet = rotationESet;
		rotation = ROTATION_EDEFAULT;
		rotationESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.FONT_DEFINITION__ROTATION,
					oldRotation, ROTATION_EDEFAULT, oldRotationESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetRotation() {
		return rotationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.FONT_DEFINITION__ALIGNMENT:
			return basicSetAlignment(null, msgs);
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
		case AttributePackage.FONT_DEFINITION__NAME:
			return getName();
		case AttributePackage.FONT_DEFINITION__SIZE:
			return getSize();
		case AttributePackage.FONT_DEFINITION__BOLD:
			return isBold();
		case AttributePackage.FONT_DEFINITION__ITALIC:
			return isItalic();
		case AttributePackage.FONT_DEFINITION__STRIKETHROUGH:
			return isStrikethrough();
		case AttributePackage.FONT_DEFINITION__UNDERLINE:
			return isUnderline();
		case AttributePackage.FONT_DEFINITION__WORD_WRAP:
			return isWordWrap();
		case AttributePackage.FONT_DEFINITION__ALIGNMENT:
			return getAlignment();
		case AttributePackage.FONT_DEFINITION__ROTATION:
			return getRotation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AttributePackage.FONT_DEFINITION__NAME:
			setName((String) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__SIZE:
			setSize((Float) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__BOLD:
			setBold((Boolean) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__ITALIC:
			setItalic((Boolean) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__STRIKETHROUGH:
			setStrikethrough((Boolean) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__UNDERLINE:
			setUnderline((Boolean) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__WORD_WRAP:
			setWordWrap((Boolean) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__ALIGNMENT:
			setAlignment((TextAlignment) newValue);
			return;
		case AttributePackage.FONT_DEFINITION__ROTATION:
			setRotation((Double) newValue);
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
		result.append(" (name: "); //$NON-NLS-1$
		result.append(name);
		result.append(", size: "); //$NON-NLS-1$
		if (sizeESet) {
			result.append(size);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", bold: "); //$NON-NLS-1$
		if (boldESet) {
			result.append(bold);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", italic: "); //$NON-NLS-1$
		if (italicESet) {
			result.append(italic);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", strikethrough: "); //$NON-NLS-1$
		if (strikethroughESet) {
			result.append(strikethrough);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", underline: "); //$NON-NLS-1$
		if (underlineESet) {
			result.append(underline);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", wordWrap: "); //$NON-NLS-1$
		if (wordWrapESet) {
			result.append(wordWrap);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", rotation: "); //$NON-NLS-1$
		if (rotationESet) {
			result.append(rotation);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	protected void set(FontDefinition src) {

		// children

		if (src.getAlignment() != null) {
			setAlignment(src.getAlignment().copyInstance());
		}

		// attributes

		name = src.getName();

		size = src.getSize();

		sizeESet = src.isSetSize();

		bold = src.isBold();

		boldESet = src.isSetBold();

		italic = src.isItalic();

		italicESet = src.isSetItalic();

		strikethrough = src.isStrikethrough();

		strikethroughESet = src.isSetStrikethrough();

		underline = src.isUnderline();

		underlineESet = src.isSetUnderline();

		wordWrap = src.isWordWrap();

		wordWrapESet = src.isSetWordWrap();

		rotation = src.getRotation();

		rotationESet = src.isSetRotation();

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
			boolean bUnderline, boolean bStrikethrough, boolean bWordWrap, double dRotation, TextAlignment ta) {
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

	/**
	 * Creates an empty FontDefinition instance.
	 *
	 * @return
	 */
	public static final FontDefinition createEmpty() {
		FontDefinition fd = AttributeFactory.eINSTANCE.createFontDefinition();
		fd.setAlignment(AttributeFactory.eINSTANCE.createTextAlignment());
		return fd;
	}

	public static final FontDefinition createEmptyDefault() {
		FontDefinition fd = AttributeFactory.eINSTANCE.createFontDefinition();
		((FontDefinitionImpl) fd).initDefault();
		return fd;
	}

	private void initDefault() {
		this.alignment = AttributeFactory.eINSTANCE.createTextAlignment();
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	@Override
	public FontDefinition copyInstance() {
		FontDefinitionImpl dest = new FontDefinitionImpl();

		TextAlignment tAlignment = getAlignment();
		if (tAlignment != null) {
			dest.alignment = tAlignment.copyInstance();
		}

		dest.name = getName();
		dest.size = getSize();
		dest.sizeESet = isSetSize();
		dest.bold = isBold();
		dest.boldESet = isSetBold();
		dest.italic = isItalic();
		dest.italicESet = isSetItalic();
		dest.strikethrough = isStrikethrough();
		dest.strikethroughESet = isSetStrikethrough();
		dest.underline = isUnderline();
		dest.underlineESet = isSetUnderline();
		dest.wordWrap = isWordWrap();
		dest.wordWrapESet = isSetWordWrap();
		dest.rotation = getRotation();
		dest.rotationESet = isSetRotation();
		return dest;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alignment == null) ? 0 : alignment.hashCode());
		result = prime * result + (bold ? 1231 : 1237);
		result = prime * result + (italic ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode());
		long temp;
		temp = Double.doubleToLongBits(rotation);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(size);
		result = prime * result + (strikethrough ? 1231 : 1237);
		result = prime * result + (underline ? 1231 : 1237);
		result = prime * result + (wordWrap ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof FontDefinition)) {
			return false;
		}
		FontDefinition other = (FontDefinition) obj;
		if (!Objects.equals(alignment, other.getAlignment())) {
			return false;
		}
		if (bold != other.isBold()) {
			return false;
		}
		if (italic != other.isItalic()) {
			return false;
		}
		if (name == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!name.equalsIgnoreCase(other.getName())) {
			return false;
		}
		if (Double.doubleToLongBits(rotation) != Double.doubleToLongBits(other.getRotation())) {
			return false;
		}
		if (Float.floatToIntBits(size) != Float.floatToIntBits(other.getSize())) {
			return false;
		}
		if (strikethrough != other.isStrikethrough()) {
			return false;
		}
		if (underline != other.isUnderline()) {
			return false;
		}
		if (wordWrap != other.isWordWrap()) {
			return false;
		}
		return true;
	}

} // FontDefinitionImpl
