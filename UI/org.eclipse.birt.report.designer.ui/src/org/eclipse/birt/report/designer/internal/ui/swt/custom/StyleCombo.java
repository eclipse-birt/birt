/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

/**
 * The CCombo class represents a selectable user interface object that combines
 * a label filed and a table and issues notificiation when an item is selected
 * from the table.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles: </b>
 * <dd>BORDER, READ_ONLY, FLAT</dd>
 * <dt><b>Events: </b>
 * <dd>Selection</dd>
 * </dl>
 */
public final class StyleCombo extends Composite {

	int visibleItemCount = 29;

	Shell popup;

	Button arrow;

	boolean hasFocus;

	Listener listener;

	Color foreground, background;

	Font font;

	Table table;

	ImageLabel label;

	int imageHeight, imageWidth;

	IComboProvider provider;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em> 'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent a widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     </ul>
	 * 
	 * @see SWT#BORDER
	 * @see SWT#READ_ONLY
	 * @see SWT#FLAT
	 * @see Widget#getStyle()
	 */
	public StyleCombo(Composite parent, int style, IComboProvider provider) {
		super(parent, style = checkStyle(style));
		assert provider != null;
		this.provider = provider;

		label = new ImageLabel(this, SWT.NONE);
		int arrowStyle = SWT.ARROW | SWT.DOWN;
		if ((style & SWT.FLAT) != 0)
			arrowStyle |= SWT.FLAT;
		arrow = new Button(this, arrowStyle);

		listener = new Listener() {

			public void handleEvent(Event event) {
				if (popup == event.widget) {
					popupEvent(event);
					return;
				}
				if (label == event.widget) {
					labelEvent(event);
					return;
				}
				if (table == event.widget) {
					tableEvent(event);
					return;
				}
				if (arrow == event.widget) {
					arrowEvent(event);
					return;
				}
				if (StyleCombo.this == event.widget) {
					comboEvent(event);
					return;
				}
			}
		};

		int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
		for (int i = 0; i < comboEvents.length; i++)
			this.addListener(comboEvents[i], listener);

		int[] arrowEvents = { SWT.Selection, SWT.FocusIn, SWT.FocusOut };
		for (int i = 0; i < arrowEvents.length; i++)
			arrow.addListener(arrowEvents[i], listener);

		int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.Modify, SWT.MouseDown, SWT.MouseUp, SWT.Traverse, SWT.FocusIn,
				SWT.FocusOut };
		for (int i = 0; i < textEvents.length; i++)
			label.addListener(textEvents[i], listener);

		createPopup(null, -1);
		initAccessible();
	}

	void initAccessible() {
		AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				String name = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					name = stripMnemonic(label.getText());
				}
				e.result = name;
				if (e.result == null)
					getHelp(e);
			}

			public void getKeyboardShortcut(AccessibleEvent e) {
				String shortcut = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					String text = label.getText();
					if (text != null) {
						char mnemonic = _findMnemonic(text);
						if (mnemonic != '\0') {
							shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
						}
					}
				}
				e.result = shortcut;
			}

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		};
		getAccessible().addAccessibleListener(accessibleAdapter);
		label.getAccessible().addAccessibleListener(accessibleAdapter);

		arrow.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			public void getKeyboardShortcut(AccessibleEvent e) {
				e.result = "Alt+Down Arrow"; //$NON-NLS-1$
			}

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point pt = toControl(new Point(e.x, e.y));
				e.childID = (getBounds().contains(pt)) ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(location.x, location.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}
		});

		arrow.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getDefaultAction(AccessibleControlEvent e) {
				e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	/*
	 * Return the lowercase of the first non-'&' character following an '&'
	 * character in the given string. If there are no '&' characters in the given
	 * string, return '\0'.
	 */
	char _findMnemonic(String string) {
		if (string == null)
			return '\0';
		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&')
				index++;
			if (++index >= length)
				return '\0';
			if (string.charAt(index) != '&')
				return Character.toLowerCase(string.charAt(index));
			index++;
		} while (index < length);
		return '\0';
	}

	/*
	 * Return the Label immediately preceding the receiver in the z-order, or null
	 * if none.
	 */
	Label getAssociatedLabel() {
		Control[] siblings = getParent().getChildren();
		for (int i = 0; i < siblings.length; i++) {
			if (siblings[i] == this) {
				if (i > 0 && siblings[i - 1] instanceof Label) {
					return (Label) siblings[i - 1];
				}
			}
		}
		return null;
	}

	String stripMnemonic(String string) {
		int index = 0;
		int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&'))
				index++;
			if (++index >= length)
				return string;
			if (string.charAt(index) != '&') {
				return string.substring(0, index - 1) + string.substring(index, length);
			}
			index++;
		} while (index < length);
		return string;
	}

	static int checkStyle(int style) {
		int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	/**
	 * Adds the listener to receive events.
	 * <p>
	 * 
	 * @param listener the listener
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_NULL_ARGUMENT) when listener is null
	 */
	// public void addModifyListener( ModifyListener listener )
	// {
	// checkWidget( );
	// if ( listener == null )
	// SWT.error( SWT.ERROR_NULL_ARGUMENT );
	// TypedListener typedListener = new TypedListener( listener );
	// addListener( SWT.Modify, typedListener );
	// }
	/**
	 * Adds the listener to receive events.
	 * <p>
	 * 
	 * @param listener the listener
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_NULL_ARGUMENT) when listener is null
	 */
	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	void arrowEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			if (hasFocus)
				return;
			hasFocus = true;
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			event.display.asyncExec(new Runnable() {

				public void run() {
					if (StyleCombo.this.isDisposed())
						return;
					Control focusControl = getDisplay().getFocusControl();
					if (focusControl == arrow || focusControl == label || focusControl == table)
						return;
					hasFocus = false;
					Event e = new Event();
					notifyListeners(SWT.FocusOut, e);
				}
			});
			break;
		}
		case SWT.Selection: {
			dropDown(!isDropped());
			break;
		}
		}
	}

	/**
	 * Clears the current selection.
	 * <p>
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 */
	public void clearSelection() {
		checkWidget();
		table.deselectAll();
	}

	void comboEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (popup != null && !popup.isDisposed()) {
				table.removeListener(SWT.Dispose, listener);
				popup.dispose();
			}
			disposeImages();
			popup = null;
			label = null;
			table = null;
			arrow = null;
			break;
		case SWT.Move:
			dropDown(false);
			break;
		case SWT.Resize:
			internalLayout(false);
			break;
		}
	}

	private int computeSizeWidth = -1;

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		// if ( size.x > 0 )
		// return size;
		if (wHint > -1)
			computeSizeWidth = wHint;
		int width = 0, height = 0;

		GC gc = new GC(label);
		Point labelExtent = gc.textExtent("AAAAAAAAAAAAA");//$NON-NLS-1$
		gc.dispose();

		Point labelSize = label.computeSize(labelExtent.x, labelExtent.y, changed);
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);

		int borderWidth = getBorderWidth();

		height = Math.max(hHint, Math.max(labelSize.y, arrowSize.y) + 2 * borderWidth);
		// Point tableSize = table.computeSize( wHint, SWT.DEFAULT, changed );
		// width = Math.max( wHint, Math.max( labelSize.x
		// + arrowSize.x
		// + 2
		// * borderWidth, tableSize.x + 2 ) );
		width = Math.max(computeSizeWidth, labelSize.x + arrowSize.x + 2 * borderWidth);
		return new Point(width, height);
	}

	void createPopup(Object[] items, int selectionIndex) {
		// create shell and list
		popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);

		table = new Table(popup, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION);
		new TableColumn(table, SWT.LEFT);
		if (font != null)
			table.setFont(font);
		if (foreground != null)
			table.setForeground(foreground);
		if (background != null)
			table.setBackground(background);

		label.setBackground(table.getBackground());
		label.setForeground(table.getForeground());
		label.setFont(table.getFont());

		int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };
		for (int i = 0; i < popupEvents.length; i++)
			popup.addListener(popupEvents[i], listener);
		int[] tableEvents = { SWT.MouseUp, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn,
				SWT.FocusOut, SWT.Dispose };
		for (int i = 0; i < tableEvents.length; i++)
			table.addListener(tableEvents[i], listener);
	}

	/**
	 * Deselects an item.
	 * <p>
	 * If the item at an index is selected, it is deselected. If the item at an
	 * index is not selected, it remains deselected. Indices that are out of range
	 * are ignored. Indexing is zero based.
	 * 
	 * @param index the index of the item
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 */
	public void deselect(int index) {
		checkWidget();
		label.setImage(null);
		table.deselect(index);
	}

	/**
	 * Deselects all items.
	 * <p>
	 * 
	 * If an item is selected, it is deselected. If an item is not selected, it
	 * remains unselected.
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 */
	public void deselectAll() {
		checkWidget();
		label.setImage(null);
		table.deselectAll();
	}

	void dropDown(boolean drop) {
		if (drop == isDropped())
			return;
		if (!drop) {
			popup.setVisible(false);
			label.forceFocus();
			label.setFocus();
			return;
		}

		if (getShell() != popup.getParent()) {
			Object[] items = provider.getItems();
			int selectionIndex = table.getSelectionIndex();
			table.removeListener(SWT.Dispose, listener);
			popup.dispose();
			disposeImages();
			popup = null;
			table = null;
			createPopup(items, selectionIndex);
		}

		Point size = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int itemCount = table.getItemCount();
		itemCount = (itemCount == 0) ? visibleItemCount : Math.min(visibleItemCount, itemCount);
		int itemHeight = table.getItemHeight() * itemCount;
		table.getColumn(0).setWidth(0);
		Point tableSize = table.computeSize(SWT.DEFAULT, itemHeight);
		table.setBounds(1, 1, Math.max(size.x - 2, tableSize.x), tableSize.y);
		table.getColumn(0).setWidth(table.getClientArea().width);
		int index = table.getSelectionIndex();
		if (index != -1)
			table.setTopIndex(index);

		Display display = getDisplay();
		Rectangle listRect = table.getBounds();
		Rectangle parentRect = display.map(getParent(), null, getBounds());
		Point comboSize = getSize();
		Rectangle displayRect = getMonitor().getClientArea();
		int width = Math.max(comboSize.x, listRect.width + 2);
		int height = listRect.height + 2;
		int x = parentRect.x;
		int y = parentRect.y + comboSize.y;
		if (y + height > displayRect.y + displayRect.height)
			y = parentRect.y - height;
		popup.setBounds(x, y, width, height);
		popup.setVisible(true);
		table.setFocus();
	}

	public Control[] getChildren() {
		checkWidget();
		return new Control[0];
	}

	/**
	 * Gets an item at an index.
	 * <p>
	 * Indexing is zero based.
	 * 
	 * This operation will fail when the index is out of range or an item could not
	 * be queried from the OS.
	 * 
	 * @param index the index of the item
	 * @return the item
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_CANNOT_GET_ITEM) when the operation fails
	 */
	public Object getItem(int index) {
		checkWidget();
		return provider.getDisplayItems()[index];
	}

	public Object getSelectedItem() {
		checkWidget();
		int index = table.getSelectionIndex();
		if (index == -1)
			return null;
		return getItem(index);
	}

	public void setSelectedItem(int index) {
		checkWidget();
		if (index < 0 || index > provider.getDisplayItems().length - 1)
			return;
		table.setSelection(index);
		label.setImage(table.getSelection()[0].getImage());
	}

	public void setSelectedItem(Object obj) {
		checkWidget();
		int index = Arrays.asList(provider.getDisplayItems()).indexOf(obj);
		if (index == -1)
			return;
		setSelectedItem(index);
	}

	/**
	 * Gets the number of items.
	 * <p>
	 * This operation will fail if the number of items could not be queried from the
	 * OS.
	 * 
	 * @return the number of items in the widget
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_CANNOT_GET_COUNT) when the operation fails
	 */
	public int getItemCount() {
		checkWidget();
		return table.getItemCount();
	}

	/**
	 * Gets the height of one item.
	 * <p>
	 * This operation will fail if the height of one item could not be queried from
	 * the OS.
	 * 
	 * @return the height of one item in the widget
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_CANNOT_GET_ITEM_HEIGHT) when the operation fails
	 */
	public int getItemHeight() {
		checkWidget();
		return table.getItemHeight();
	}

	/**
	 * Gets the items.
	 * <p>
	 * This operation will fail if the items cannot be queried from the OS.
	 * 
	 * @return the items in the widget
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_CANNOT_GET_ITEM) when the operation fails
	 */
	public Object[] getItems() {
		checkWidget();
		return provider.getItems();
	}

	/**
	 * Gets the index of the selected item.
	 * <p>
	 * Indexing is zero based. If no item is selected -1 is returned.
	 * 
	 * @return the index of the selected item.
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 */
	public int getSelectionIndex() {
		checkWidget();
		return table.getSelectionIndex();
	}

	public int getStyle() {
		int style = super.getStyle();
		style &= ~SWT.READ_ONLY;
		return style;
	}

	/**
	 * Gets the number of items that are visible in the drop down portion of the
	 * receiver's list.
	 * 
	 * @return the number of items that are visible
	 * 
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * 
	 * @since 3.0
	 */
	public int getVisibleItemCount() {
		checkWidget();
		return visibleItemCount;
	}

	/**
	 * Gets the index of an item.
	 * <p>
	 * The list is searched starting at 0 until an item is found that is equal to
	 * the search item. If no item is found, -1 is returned. Indexing is zero based.
	 * 
	 * @param string the search item
	 * @return the index of the item
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_NULL_ARGUMENT) when string is null
	 */
	public int indexOf(Object item) {
		checkWidget();
		if (item == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return Arrays.asList(provider.getItems()).indexOf(item);
	}

	boolean isDropped() {
		return popup.getVisible();
	}

	public boolean isFocusControl() {
		checkWidget();
		if (label.isFocusControl() || arrow.isFocusControl() || table.isFocusControl() || popup.isFocusControl()) {
			return true;
		}
		return super.isFocusControl();
	}

	void internalLayout(boolean changed) {
		if (isDropped())
			dropDown(false);

		Rectangle rect = getClientArea();
		int width = rect.width;
		int height = rect.height;
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, height, changed);
		label.setBounds(0, 0, width - arrowSize.x, height);
		arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);

		initImages();
	}

	private void initImages() {
		Point arrowSize = arrow.getSize();
		imageHeight = label.getBounds().height;
		Point size = getSize();
		imageWidth = size.x - 10 - UIUtil.getMaxStringWidth((String[]) provider.getDisplayItems(), this);
		if (provider.getItems().length > visibleItemCount)
			imageWidth -= arrowSize.x;
		disposeImages();
		for (int i = 0; i < provider.getItems().length; i++) {
			TableItem item = table.getItem(i);
			item.setImage(0, provider.getImage(provider.getItems()[i], imageWidth, imageHeight, table, this));
		}
		if (table.getSelectionCount() > 0) {
			label.setImage(table.getSelection()[0].getImage());
		}
	}

	void tableEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (getShell() != popup.getParent()) {
				Object[] items = provider.getItems();
				int selectionIndex = table.getSelectionIndex();
				disposeImages();
				popup = null;
				table = null;
				createPopup(items, selectionIndex);
			}
			break;
		case SWT.FocusIn: {
			if (hasFocus)
				return;
			hasFocus = true;
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			event.display.asyncExec(new Runnable() {

				public void run() {
					if (StyleCombo.this.isDisposed())
						return;
					Control focusControl = getDisplay().getFocusControl();
					if (focusControl == arrow || focusControl == table || focusControl == label)
						return;
					hasFocus = false;
					Event e = new Event();
					notifyListeners(SWT.FocusOut, e);
				}
			});
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1)
				return;
			dropDown(false);
			break;
		}
		case SWT.Selection: {
			int index = table.getSelectionIndex();
			if (index == -1)
				return;
			label.setImage(table.getSelection()[0].getImage());
			Event e = new Event();
			e.time = event.time;
			e.stateMask = event.stateMask;
			e.doit = event.doit;
			notifyListeners(SWT.Selection, e);
			event.doit = e.doit;
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_ESCAPE:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				event.doit = false;
				break;
			}
			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners(SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyUp, e);
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.ESC) {
				// Escape key cancels popup list
				dropDown(false);
			}
			if ((event.stateMask & SWT.ALT) != 0
					&& (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
				dropDown(false);
			}
			if (event.character == SWT.CR) {
				// Enter causes default selection
				dropDown(false);
				Event e = new Event();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners(SWT.DefaultSelection, e);
			}
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed())
				break;
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyDown, e);
			break;

		}
		}
	}

	/**
	 * Dispose the image system sources
	 */
	private void disposeImages() {
		if (table.isDisposed())
			return;
		TableItem[] treeItems = table.getItems();
		for (int i = 0; i < treeItems.length; i++) {
			if (treeItems[i].getImage() != null && !treeItems[i].getImage().isDisposed())
				treeItems[i].getImage().dispose();
		}
	}

	void popupEvent(Event event) {
		switch (event.type) {
		case SWT.Paint:
			// draw black rectangle around list
			Rectangle tableRect = table.getBounds();
			Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, tableRect.width + 1, tableRect.height + 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDown(false);
			break;
		case SWT.Deactivate:
			dropDown(false);
			break;
		}
	}

	public void redraw() {
		super.redraw();
		label.redraw();
		arrow.redraw();
		if (popup.isVisible())
			table.redraw();
	}

	public void redraw(int x, int y, int width, int height, boolean all) {
		super.redraw(x, y, width, height, true);
	}

	/**
	 * Removes the listener.
	 * <p>
	 * 
	 * @param listener the listener
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_NULL_ARGUMENT) when listener is null
	 */
	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Modify, listener);
	}

	/**
	 * Removes the listener.
	 * <p>
	 * 
	 * @param listener the listener
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_NULL_ARGUMENT) when listener is null
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	/**
	 * Selects an item.
	 * <p>
	 * If the item at an index is not selected, it is selected. Indices that are out
	 * of range are ignored. Indexing is zero based.
	 * 
	 * @param index the index of the item
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 */
	public void select(int index) {
		checkWidget();
		if (index == -1) {
			table.deselectAll();
			label.setImage(null);
			return;
		}
		setSelectedItem(index);
	}

	public void setBackground(Color color) {
		super.setBackground(color);
		background = color;
		if (label != null)
			label.setBackground(color);
		if (table != null)
			table.setBackground(color);
		if (arrow != null)
			arrow.setBackground(color);
	}

	/**
	 * Sets the editable state.
	 * 
	 * @param editable the new editable state
	 * 
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * 
	 * @since 3.0
	 * 
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (popup != null)
			popup.setVisible(false);
		if (label != null)
			label.setEnabled(enabled);
		if (arrow != null)
			arrow.setEnabled(enabled);
	}

	public boolean setFocus() {
		checkWidget();
		label.forceFocus();
		label.setFocus();
		Event e = new Event();
		notifyListeners(SWT.FocusIn, e);
		return label.isFocusControl();
	}

	public void setFont(Font font) {
		super.setFont(font);
		this.font = font;
		label.setFont(font);
		table.setFont(font);
		internalLayout(true);
	}

	public void setForeground(Color color) {
		super.setForeground(color);
		foreground = color;
		if (label != null)
			label.setForeground(color);
		if (table != null)
			table.setForeground(color);
		if (arrow != null)
			arrow.setForeground(color);
	}

	/**
	 * Sets all items.
	 * 
	 * @param items the array of items
	 * 
	 * @exception SWTError (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                     thread
	 * @exception SWTError (ERROR_WIDGET_DISPOSED) when the widget has been disposed
	 * @exception SWTError (ERROR_NULL_ARGUMENT) when items is null
	 * @exception SWTError (ERROR_ITEM_NOT_ADDED) when the operation fails
	 */
	public void setItems(Object[] items) {
		checkWidget();
		if (items == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);

		// Point p = computeSize( SWT.DEFAULT, SWT.DEFAULT );
		// internalLayout( );
		//
		// Point arrowSize = arrow.getSize( );
		// imageHeight = label.getBounds( ).height;
		// Point size = p;
		// imageWidth = size.x - 4;
		// if ( items.length > visibleItemCount )
		// imageWidth -= arrowSize.x;
		//
		// provider.setItems( items );
		// disposeImages( );
		// for ( int i = 0; i < items.length; i++ )
		// {
		// TableItem item = new TableItem( table, SWT.NONE );
		// item.setImage( 0, provider.getImage( items[i],
		// imageWidth,
		// imageHeight,
		// table,
		// this ) );
		// }
		provider.setItems(items);
		for (int i = 0; i < items.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, provider.getDisplayItems()[i].toString());
		}
	}

	public void setToolTipText(String string) {
		checkWidget();
		super.setToolTipText(string);
		arrow.setToolTipText(string);
		label.setToolTipText(string);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible)
			popup.setVisible(false);
	}

	/**
	 * Sets the number of items that are visible in the drop down portion of the
	 * receiver's list.
	 * 
	 * @param count the new number of items to be visible
	 * 
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * 
	 * @since 3.0
	 */
	public void setVisibleItemCount(int count) {
		checkWidget();
		if (count < 0)
			return;
		visibleItemCount = count;
	}

	void labelEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			if (hasFocus)
				return;
			hasFocus = true;
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			event.display.asyncExec(new Runnable() {

				public void run() {
					if (StyleCombo.this.isDisposed())
						return;
					Control focusControl = getDisplay().getFocusControl();
					if (focusControl == arrow || focusControl == table || focusControl == label)
						return;
					hasFocus = false;
					Event e = new Event();
					notifyListeners(SWT.FocusOut, e);
				}
			});
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.CR) {
				dropDown(false);
				Event e = new Event();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners(SWT.DefaultSelection, e);
			}
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed())
				break;

			if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
				if ((event.stateMask & SWT.ALT) != 0) {
					boolean dropped = isDropped();
					if (!dropped)
						setFocus();
					dropDown(!dropped);
					break;
				}

				int oldIndex = getSelectionIndex();
				if (event.keyCode == SWT.ARROW_UP) {
					select(Math.max(oldIndex - 1, 0));
				} else {
					select(Math.min(oldIndex + 1, getItemCount() - 1));
				}
				if (oldIndex != getSelectionIndex()) {
					Event e = new Event();
					e.time = event.time;
					e.stateMask = event.stateMask;
					notifyListeners(SWT.Selection, e);

				}
				// At this point the widget may have been disposed.
				// If so, do not continue.
				if (isDisposed())
					break;
			}

			// Further work : Need to add support for incremental search in
			// pop up list as characters typed in text widget

			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyDown, e);
			break;
		}
		case SWT.KeyUp: {
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyUp, e);
			break;
		}
		case SWT.Modify: {
			table.deselectAll();
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.Modify, e);
			break;
		}
		case SWT.MouseDown: {
			if (event.button != 1)
				return;
			boolean dropped = isDropped();
			if (!dropped)
				setFocus();
			dropDown(!dropped);
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1)
				return;
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				// The enter causes default selection and
				// the arrow keys are used to manipulate the list
				// contents so
				// do not use them for traversal.
				event.doit = false;
				break;
			}

			Event e = new Event();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners(SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		}
	}
}
