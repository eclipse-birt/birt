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

package org.eclipse.birt.chart.model.layout.impl;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
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
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Block</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getChildren
 * <em>Children</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getBounds
 * <em>Bounds</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getAnchor
 * <em>Anchor</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getStretch
 * <em>Stretch</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getInsets
 * <em>Insets</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getRow
 * <em>Row</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getColumn
 * <em>Column</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getRowspan
 * <em>Rowspan</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getColumnspan
 * <em>Columnspan</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getMinSize
 * <em>Min Size</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getBackground
 * <em>Background</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getTriggers
 * <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getWidthHint
 * <em>Width Hint</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getHeightHint
 * <em>Height Hint</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.BlockImpl#getCursor
 * <em>Cursor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BlockImpl extends EObjectImpl implements Block {

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<Block> children;

	/**
	 * The cached value of the '{@link #getBounds() <em>Bounds</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBounds()
	 * @generated
	 * @ordered
	 */
	protected Bounds bounds;

	/**
	 * The default value of the '{@link #getAnchor() <em>Anchor</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAnchor()
	 * @generated
	 * @ordered
	 */
	protected static final Anchor ANCHOR_EDEFAULT = Anchor.NORTH_LITERAL;

	/**
	 * The cached value of the '{@link #getAnchor() <em>Anchor</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAnchor()
	 * @generated
	 * @ordered
	 */
	protected Anchor anchor = ANCHOR_EDEFAULT;

	/**
	 * This is true if the Anchor attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean anchorESet;

	/**
	 * The default value of the '{@link #getStretch() <em>Stretch</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStretch()
	 * @generated
	 * @ordered
	 */
	protected static final Stretch STRETCH_EDEFAULT = Stretch.HORIZONTAL_LITERAL;

	/**
	 * The cached value of the '{@link #getStretch() <em>Stretch</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStretch()
	 * @generated
	 * @ordered
	 */
	protected Stretch stretch = STRETCH_EDEFAULT;

	/**
	 * This is true if the Stretch attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean stretchESet;

	/**
	 * The cached value of the '{@link #getInsets() <em>Insets</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInsets()
	 * @generated
	 * @ordered
	 */
	protected Insets insets;

	/**
	 * The default value of the '{@link #getRow() <em>Row</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRow()
	 * @generated
	 * @ordered
	 */
	protected static final int ROW_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getRow() <em>Row</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRow()
	 * @generated
	 * @ordered
	 */
	protected int row = ROW_EDEFAULT;

	/**
	 * This is true if the Row attribute has been set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean rowESet;

	/**
	 * The default value of the '{@link #getColumn() <em>Column</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getColumn()
	 * @generated
	 * @ordered
	 */
	protected static final int COLUMN_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getColumn() <em>Column</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getColumn()
	 * @generated
	 * @ordered
	 */
	protected int column = COLUMN_EDEFAULT;

	/**
	 * This is true if the Column attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean columnESet;

	/**
	 * The default value of the '{@link #getRowspan() <em>Rowspan</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRowspan()
	 * @generated
	 * @ordered
	 */
	protected static final int ROWSPAN_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getRowspan() <em>Rowspan</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRowspan()
	 * @generated
	 * @ordered
	 */
	protected int rowspan = ROWSPAN_EDEFAULT;

	/**
	 * This is true if the Rowspan attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean rowspanESet;

	/**
	 * The default value of the '{@link #getColumnspan() <em>Columnspan</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getColumnspan()
	 * @generated
	 * @ordered
	 */
	protected static final int COLUMNSPAN_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getColumnspan() <em>Columnspan</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getColumnspan()
	 * @generated
	 * @ordered
	 */
	protected int columnspan = COLUMNSPAN_EDEFAULT;

	/**
	 * This is true if the Columnspan attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean columnspanESet;

	/**
	 * The cached value of the '{@link #getMinSize() <em>Min Size</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMinSize()
	 * @generated
	 * @ordered
	 */
	protected Size minSize;

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
	 * The cached value of the '{@link #getBackground() <em>Background</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBackground()
	 * @generated
	 * @ordered
	 */
	protected Fill background;

	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * This is true if the Visible attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean visibleESet;

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
	 * The default value of the '{@link #getWidthHint() <em>Width Hint</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWidthHint()
	 * @generated
	 * @ordered
	 */
	protected static final double WIDTH_HINT_EDEFAULT = -1.0;

	/**
	 * The cached value of the '{@link #getWidthHint() <em>Width Hint</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWidthHint()
	 * @generated
	 * @ordered
	 */
	protected double widthHint = WIDTH_HINT_EDEFAULT;

	/**
	 * This is true if the Width Hint attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean widthHintESet;

	/**
	 * The default value of the '{@link #getHeightHint() <em>Height Hint</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeightHint()
	 * @generated
	 * @ordered
	 */
	protected static final double HEIGHT_HINT_EDEFAULT = -1.0;

	/**
	 * The cached value of the '{@link #getHeightHint() <em>Height Hint</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getHeightHint()
	 * @generated
	 * @ordered
	 */
	protected double heightHint = HEIGHT_HINT_EDEFAULT;

	/**
	 * This is true if the Height Hint attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean heightHintESet;

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
	protected BlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LayoutPackage.Literals.BLOCK;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Block> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<Block>(Block.class, this, LayoutPackage.BLOCK__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Bounds getBounds() {
		return bounds;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetBounds(Bounds newBounds, NotificationChain msgs) {
		Bounds oldBounds = bounds;
		bounds = newBounds;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__BOUNDS,
					oldBounds, newBounds);
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
	public void setBounds(Bounds newBounds) {
		if (newBounds != bounds) {
			NotificationChain msgs = null;
			if (bounds != null)
				msgs = ((InternalEObject) bounds).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__BOUNDS, null, msgs);
			if (newBounds != null)
				msgs = ((InternalEObject) newBounds).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__BOUNDS, null, msgs);
			msgs = basicSetBounds(newBounds, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__BOUNDS, newBounds, newBounds));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Anchor getAnchor() {
		return anchor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAnchor(Anchor newAnchor) {
		Anchor oldAnchor = anchor;
		anchor = newAnchor == null ? ANCHOR_EDEFAULT : newAnchor;
		boolean oldAnchorESet = anchorESet;
		anchorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__ANCHOR, oldAnchor, anchor,
					!oldAnchorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetAnchor() {
		Anchor oldAnchor = anchor;
		boolean oldAnchorESet = anchorESet;
		anchor = ANCHOR_EDEFAULT;
		anchorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__ANCHOR, oldAnchor,
					ANCHOR_EDEFAULT, oldAnchorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetAnchor() {
		return anchorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Stretch getStretch() {
		return stretch;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStretch(Stretch newStretch) {
		Stretch oldStretch = stretch;
		stretch = newStretch == null ? STRETCH_EDEFAULT : newStretch;
		boolean oldStretchESet = stretchESet;
		stretchESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__STRETCH, oldStretch, stretch,
					!oldStretchESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetStretch() {
		Stretch oldStretch = stretch;
		boolean oldStretchESet = stretchESet;
		stretch = STRETCH_EDEFAULT;
		stretchESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__STRETCH, oldStretch,
					STRETCH_EDEFAULT, oldStretchESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetStretch() {
		return stretchESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Insets getInsets() {
		return insets;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetInsets(Insets newInsets, NotificationChain msgs) {
		Insets oldInsets = insets;
		insets = newInsets;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__INSETS,
					oldInsets, newInsets);
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
	public void setInsets(Insets newInsets) {
		if (newInsets != insets) {
			NotificationChain msgs = null;
			if (insets != null)
				msgs = ((InternalEObject) insets).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__INSETS, null, msgs);
			if (newInsets != null)
				msgs = ((InternalEObject) newInsets).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__INSETS, null, msgs);
			msgs = basicSetInsets(newInsets, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__INSETS, newInsets, newInsets));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getRow() {
		return row;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRow(int newRow) {
		int oldRow = row;
		row = newRow;
		boolean oldRowESet = rowESet;
		rowESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__ROW, oldRow, row, !oldRowESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetRow() {
		int oldRow = row;
		boolean oldRowESet = rowESet;
		row = ROW_EDEFAULT;
		rowESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__ROW, oldRow, ROW_EDEFAULT,
					oldRowESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetRow() {
		return rowESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setColumn(int newColumn) {
		int oldColumn = column;
		column = newColumn;
		boolean oldColumnESet = columnESet;
		columnESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__COLUMN, oldColumn, column,
					!oldColumnESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetColumn() {
		int oldColumn = column;
		boolean oldColumnESet = columnESet;
		column = COLUMN_EDEFAULT;
		columnESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__COLUMN, oldColumn,
					COLUMN_EDEFAULT, oldColumnESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetColumn() {
		return columnESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getRowspan() {
		return rowspan;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRowspan(int newRowspan) {
		int oldRowspan = rowspan;
		rowspan = newRowspan;
		boolean oldRowspanESet = rowspanESet;
		rowspanESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__ROWSPAN, oldRowspan, rowspan,
					!oldRowspanESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetRowspan() {
		int oldRowspan = rowspan;
		boolean oldRowspanESet = rowspanESet;
		rowspan = ROWSPAN_EDEFAULT;
		rowspanESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__ROWSPAN, oldRowspan,
					ROWSPAN_EDEFAULT, oldRowspanESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetRowspan() {
		return rowspanESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getColumnspan() {
		return columnspan;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setColumnspan(int newColumnspan) {
		int oldColumnspan = columnspan;
		columnspan = newColumnspan;
		boolean oldColumnspanESet = columnspanESet;
		columnspanESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__COLUMNSPAN, oldColumnspan,
					columnspan, !oldColumnspanESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetColumnspan() {
		int oldColumnspan = columnspan;
		boolean oldColumnspanESet = columnspanESet;
		columnspan = COLUMNSPAN_EDEFAULT;
		columnspanESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__COLUMNSPAN, oldColumnspan,
					COLUMNSPAN_EDEFAULT, oldColumnspanESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetColumnspan() {
		return columnspanESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Size getMinSize() {
		return minSize;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetMinSize(Size newMinSize, NotificationChain msgs) {
		Size oldMinSize = minSize;
		minSize = newMinSize;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.BLOCK__MIN_SIZE, oldMinSize, newMinSize);
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
	public void setMinSize(Size newMinSize) {
		if (newMinSize != minSize) {
			NotificationChain msgs = null;
			if (minSize != null)
				msgs = ((InternalEObject) minSize).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__MIN_SIZE, null, msgs);
			if (newMinSize != null)
				msgs = ((InternalEObject) newMinSize).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__MIN_SIZE, null, msgs);
			msgs = basicSetMinSize(newMinSize, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__MIN_SIZE, newMinSize,
					newMinSize));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__OUTLINE,
					oldOutline, newOutline);
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
	public void setOutline(LineAttributes newOutline) {
		if (newOutline != outline) {
			NotificationChain msgs = null;
			if (outline != null)
				msgs = ((InternalEObject) outline).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__OUTLINE, null, msgs);
			if (newOutline != null)
				msgs = ((InternalEObject) newOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__OUTLINE, null, msgs);
			msgs = basicSetOutline(newOutline, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__OUTLINE, newOutline,
					newOutline));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getBackground() {
		return background;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetBackground(Fill newBackground, NotificationChain msgs) {
		Fill oldBackground = background;
		background = newBackground;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					LayoutPackage.BLOCK__BACKGROUND, oldBackground, newBackground);
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
	public void setBackground(Fill newBackground) {
		if (newBackground != background) {
			NotificationChain msgs = null;
			if (background != null)
				msgs = ((InternalEObject) background).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__BACKGROUND, null, msgs);
			if (newBackground != null)
				msgs = ((InternalEObject) newBackground).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__BACKGROUND, null, msgs);
			msgs = basicSetBackground(newBackground, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__BACKGROUND, newBackground,
					newBackground));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		boolean oldVisibleESet = visibleESet;
		visibleESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__VISIBLE, oldVisible, visible,
					!oldVisibleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetVisible() {
		boolean oldVisible = visible;
		boolean oldVisibleESet = visibleESet;
		visible = VISIBLE_EDEFAULT;
		visibleESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__VISIBLE, oldVisible,
					VISIBLE_EDEFAULT, oldVisibleESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetVisible() {
		return visibleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new EObjectContainmentEList<Trigger>(Trigger.class, this, LayoutPackage.BLOCK__TRIGGERS);
		}
		return triggers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getWidthHint() {
		return widthHint;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWidthHint(double newWidthHint) {
		double oldWidthHint = widthHint;
		widthHint = newWidthHint;
		boolean oldWidthHintESet = widthHintESet;
		widthHintESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__WIDTH_HINT, oldWidthHint,
					widthHint, !oldWidthHintESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetWidthHint() {
		double oldWidthHint = widthHint;
		boolean oldWidthHintESet = widthHintESet;
		widthHint = WIDTH_HINT_EDEFAULT;
		widthHintESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__WIDTH_HINT, oldWidthHint,
					WIDTH_HINT_EDEFAULT, oldWidthHintESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetWidthHint() {
		return widthHintESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getHeightHint() {
		return heightHint;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setHeightHint(double newHeightHint) {
		double oldHeightHint = heightHint;
		heightHint = newHeightHint;
		boolean oldHeightHintESet = heightHintESet;
		heightHintESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__HEIGHT_HINT, oldHeightHint,
					heightHint, !oldHeightHintESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetHeightHint() {
		double oldHeightHint = heightHint;
		boolean oldHeightHintESet = heightHintESet;
		heightHint = HEIGHT_HINT_EDEFAULT;
		heightHintESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.BLOCK__HEIGHT_HINT, oldHeightHint,
					HEIGHT_HINT_EDEFAULT, oldHeightHintESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetHeightHint() {
		return heightHintESet;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__CURSOR,
					oldCursor, newCursor);
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
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__CURSOR, null, msgs);
			if (newCursor != null)
				msgs = ((InternalEObject) newCursor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - LayoutPackage.BLOCK__CURSOR, null, msgs);
			msgs = basicSetCursor(newCursor, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.BLOCK__CURSOR, newCursor, newCursor));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case LayoutPackage.BLOCK__CHILDREN:
			return ((InternalEList<?>) getChildren()).basicRemove(otherEnd, msgs);
		case LayoutPackage.BLOCK__BOUNDS:
			return basicSetBounds(null, msgs);
		case LayoutPackage.BLOCK__INSETS:
			return basicSetInsets(null, msgs);
		case LayoutPackage.BLOCK__MIN_SIZE:
			return basicSetMinSize(null, msgs);
		case LayoutPackage.BLOCK__OUTLINE:
			return basicSetOutline(null, msgs);
		case LayoutPackage.BLOCK__BACKGROUND:
			return basicSetBackground(null, msgs);
		case LayoutPackage.BLOCK__TRIGGERS:
			return ((InternalEList<?>) getTriggers()).basicRemove(otherEnd, msgs);
		case LayoutPackage.BLOCK__CURSOR:
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
		case LayoutPackage.BLOCK__CHILDREN:
			return getChildren();
		case LayoutPackage.BLOCK__BOUNDS:
			return getBounds();
		case LayoutPackage.BLOCK__ANCHOR:
			return getAnchor();
		case LayoutPackage.BLOCK__STRETCH:
			return getStretch();
		case LayoutPackage.BLOCK__INSETS:
			return getInsets();
		case LayoutPackage.BLOCK__ROW:
			return getRow();
		case LayoutPackage.BLOCK__COLUMN:
			return getColumn();
		case LayoutPackage.BLOCK__ROWSPAN:
			return getRowspan();
		case LayoutPackage.BLOCK__COLUMNSPAN:
			return getColumnspan();
		case LayoutPackage.BLOCK__MIN_SIZE:
			return getMinSize();
		case LayoutPackage.BLOCK__OUTLINE:
			return getOutline();
		case LayoutPackage.BLOCK__BACKGROUND:
			return getBackground();
		case LayoutPackage.BLOCK__VISIBLE:
			return isVisible();
		case LayoutPackage.BLOCK__TRIGGERS:
			return getTriggers();
		case LayoutPackage.BLOCK__WIDTH_HINT:
			return getWidthHint();
		case LayoutPackage.BLOCK__HEIGHT_HINT:
			return getHeightHint();
		case LayoutPackage.BLOCK__CURSOR:
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
		case LayoutPackage.BLOCK__CHILDREN:
			getChildren().clear();
			getChildren().addAll((Collection<? extends Block>) newValue);
			return;
		case LayoutPackage.BLOCK__BOUNDS:
			setBounds((Bounds) newValue);
			return;
		case LayoutPackage.BLOCK__ANCHOR:
			setAnchor((Anchor) newValue);
			return;
		case LayoutPackage.BLOCK__STRETCH:
			setStretch((Stretch) newValue);
			return;
		case LayoutPackage.BLOCK__INSETS:
			setInsets((Insets) newValue);
			return;
		case LayoutPackage.BLOCK__ROW:
			setRow((Integer) newValue);
			return;
		case LayoutPackage.BLOCK__COLUMN:
			setColumn((Integer) newValue);
			return;
		case LayoutPackage.BLOCK__ROWSPAN:
			setRowspan((Integer) newValue);
			return;
		case LayoutPackage.BLOCK__COLUMNSPAN:
			setColumnspan((Integer) newValue);
			return;
		case LayoutPackage.BLOCK__MIN_SIZE:
			setMinSize((Size) newValue);
			return;
		case LayoutPackage.BLOCK__OUTLINE:
			setOutline((LineAttributes) newValue);
			return;
		case LayoutPackage.BLOCK__BACKGROUND:
			setBackground((Fill) newValue);
			return;
		case LayoutPackage.BLOCK__VISIBLE:
			setVisible((Boolean) newValue);
			return;
		case LayoutPackage.BLOCK__TRIGGERS:
			getTriggers().clear();
			getTriggers().addAll((Collection<? extends Trigger>) newValue);
			return;
		case LayoutPackage.BLOCK__WIDTH_HINT:
			setWidthHint((Double) newValue);
			return;
		case LayoutPackage.BLOCK__HEIGHT_HINT:
			setHeightHint((Double) newValue);
			return;
		case LayoutPackage.BLOCK__CURSOR:
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
		case LayoutPackage.BLOCK__CHILDREN:
			getChildren().clear();
			return;
		case LayoutPackage.BLOCK__BOUNDS:
			setBounds((Bounds) null);
			return;
		case LayoutPackage.BLOCK__ANCHOR:
			unsetAnchor();
			return;
		case LayoutPackage.BLOCK__STRETCH:
			unsetStretch();
			return;
		case LayoutPackage.BLOCK__INSETS:
			setInsets((Insets) null);
			return;
		case LayoutPackage.BLOCK__ROW:
			unsetRow();
			return;
		case LayoutPackage.BLOCK__COLUMN:
			unsetColumn();
			return;
		case LayoutPackage.BLOCK__ROWSPAN:
			unsetRowspan();
			return;
		case LayoutPackage.BLOCK__COLUMNSPAN:
			unsetColumnspan();
			return;
		case LayoutPackage.BLOCK__MIN_SIZE:
			setMinSize((Size) null);
			return;
		case LayoutPackage.BLOCK__OUTLINE:
			setOutline((LineAttributes) null);
			return;
		case LayoutPackage.BLOCK__BACKGROUND:
			setBackground((Fill) null);
			return;
		case LayoutPackage.BLOCK__VISIBLE:
			unsetVisible();
			return;
		case LayoutPackage.BLOCK__TRIGGERS:
			getTriggers().clear();
			return;
		case LayoutPackage.BLOCK__WIDTH_HINT:
			unsetWidthHint();
			return;
		case LayoutPackage.BLOCK__HEIGHT_HINT:
			unsetHeightHint();
			return;
		case LayoutPackage.BLOCK__CURSOR:
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
		case LayoutPackage.BLOCK__CHILDREN:
			return children != null && !children.isEmpty();
		case LayoutPackage.BLOCK__BOUNDS:
			return bounds != null;
		case LayoutPackage.BLOCK__ANCHOR:
			return isSetAnchor();
		case LayoutPackage.BLOCK__STRETCH:
			return isSetStretch();
		case LayoutPackage.BLOCK__INSETS:
			return insets != null;
		case LayoutPackage.BLOCK__ROW:
			return isSetRow();
		case LayoutPackage.BLOCK__COLUMN:
			return isSetColumn();
		case LayoutPackage.BLOCK__ROWSPAN:
			return isSetRowspan();
		case LayoutPackage.BLOCK__COLUMNSPAN:
			return isSetColumnspan();
		case LayoutPackage.BLOCK__MIN_SIZE:
			return minSize != null;
		case LayoutPackage.BLOCK__OUTLINE:
			return outline != null;
		case LayoutPackage.BLOCK__BACKGROUND:
			return background != null;
		case LayoutPackage.BLOCK__VISIBLE:
			return isSetVisible();
		case LayoutPackage.BLOCK__TRIGGERS:
			return triggers != null && !triggers.isEmpty();
		case LayoutPackage.BLOCK__WIDTH_HINT:
			return isSetWidthHint();
		case LayoutPackage.BLOCK__HEIGHT_HINT:
			return isSetHeightHint();
		case LayoutPackage.BLOCK__CURSOR:
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
		result.append(" (anchor: "); //$NON-NLS-1$
		if (anchorESet)
			result.append(anchor);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", stretch: "); //$NON-NLS-1$
		if (stretchESet)
			result.append(stretch);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", row: "); //$NON-NLS-1$
		if (rowESet)
			result.append(row);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", column: "); //$NON-NLS-1$
		if (columnESet)
			result.append(column);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", rowspan: "); //$NON-NLS-1$
		if (rowspanESet)
			result.append(rowspan);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", columnspan: "); //$NON-NLS-1$
		if (columnspanESet)
			result.append(columnspan);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", visible: "); //$NON-NLS-1$
		if (visibleESet)
			result.append(visible);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", widthHint: "); //$NON-NLS-1$
		if (widthHintESet)
			result.append(widthHint);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", heightHint: "); //$NON-NLS-1$
		if (heightHintESet)
			result.append(heightHint);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @param bRecursive
	 * @return
	 */
	public final Enumeration<Block> children(boolean bRecursive) {
		Vector<Block> v = new Vector<Block>();
		collectChildren(v, bRecursive);
		return v.elements();
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @param v
	 * @param bRecursive
	 */
	private final void collectChildren(Vector<Block> v, boolean bRecursive) {
		EList<Block> el = getChildren();
		for (int iC = 0; iC < el.size(); iC++) {
			v.add(el.get(iC));
			if (bRecursive) {
				((BlockImpl) el.get(iC)).collectChildren(v, bRecursive);
			}
		}
	}

	public Size getPreferredSize(IDisplayServer xs, Chart cm, RunTimeContext rtc) throws ChartException {
		return new SizeImpl(100, 100);
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isPlot() {
		return false;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isLegend() {
		return false;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isText() {
		return false;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isCustom() {
		return true;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public boolean isTitle() {
		return false;
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @param bl
	 */
	public final void add(Block bl) {
		getChildren().add(bl);
	}

	/**
	 * 
	 * Note: Manually written
	 * 
	 * @param bl
	 */
	public final void remove(Block bl) {
		getChildren().remove(bl);
	}

	/**
	 * A convenience method to create an initialized 'Block' instance
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public static Block create() {
		final Block bl = LayoutFactory.eINSTANCE.createBlock();
		((BlockImpl) bl).initialize();
		return bl;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initialize() {
		final LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		setOutline(lia);

		final Bounds bo = AttributeFactory.eINSTANCE.createBounds();
		((BoundsImpl) bo).set(0, 0, 0, 0);
		setBounds(bo);

		final Insets i = AttributeFactory.eINSTANCE.createInsets();
		((InsetsImpl) i).set(3, 3, 3, 3);
		setInsets(i);

		setRow(-1);
		setColumn(-1);
		setRowspan(-1);
		setColumnspan(-1);
		// setStretch(Stretch.NONE_LITERAL);

		setVisible(true);
	}

	/**
	 * A convenience method to create an initialized 'Block' instance
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public static Block createDefault() {
		final Block bl = LayoutFactory.eINSTANCE.createBlock();
		((BlockImpl) bl).initDefault();
		return bl;
	}

	/**
	 * Resets all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initDefault() {
		final LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		setOutline(lia);

		final Bounds bo = BoundsImpl.createDefault(0, 0, 0, 0);
		setBounds(bo);

		final Insets i = InsetsImpl.createDefault(3, 3, 3, 3);
		setInsets(i);

		row = -1;
		column = -1;
		rowspan = -1;
		columnspan = -1;
		visible = true;
	}

	/**
	 * @generated
	 */
	public Block copyInstance() {
		BlockImpl dest = new BlockImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Block src) {

		// children

		if (src.getChildren() != null) {
			EList<Block> list = getChildren();
			for (Block element : src.getChildren()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getBounds() != null) {
			setBounds(src.getBounds().copyInstance());
		}

		if (src.getInsets() != null) {
			setInsets(src.getInsets().copyInstance());
		}

		if (src.getMinSize() != null) {
			setMinSize(src.getMinSize().copyInstance());
		}

		if (src.getOutline() != null) {
			setOutline(src.getOutline().copyInstance());
		}

		if (src.getBackground() != null) {
			setBackground(src.getBackground().copyInstance());
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

		anchor = src.getAnchor();

		anchorESet = src.isSetAnchor();

		stretch = src.getStretch();

		stretchESet = src.isSetStretch();

		row = src.getRow();

		rowESet = src.isSetRow();

		column = src.getColumn();

		columnESet = src.isSetColumn();

		rowspan = src.getRowspan();

		rowspanESet = src.isSetRowspan();

		columnspan = src.getColumnspan();

		columnspanESet = src.isSetColumnspan();

		visible = src.isVisible();

		visibleESet = src.isSetVisible();

		widthHint = src.getWidthHint();

		widthHintESet = src.isSetWidthHint();

		heightHint = src.getHeightHint();

		heightHintESet = src.isSetHeightHint();

	}

} // BlockImpl
