/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

/**
 * The CCombo class represents a selectable user interface object that combines
 * a text field and a list and issues notificiation when an item is selected
 * from the list.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER, READ_ONLY, FLAT</dd>
 * <dt><b>Events:</b>
 * <dd>Selection</dd>
 * </dl>
 */
public final class CCombo extends Composite {

	Text text;
	List list;
	int visibleItemCount = 30;
	Shell popup;
	Button arrow;
	boolean hasFocus;
	Listener listener;
	Color foreground, background;
	Font font;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
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
	public CCombo(Composite parent, int style) {
		super(parent, style = checkStyle(style));

		int textStyle = SWT.SINGLE;
		if ((style & SWT.READ_ONLY) != 0) {
			textStyle |= SWT.READ_ONLY;
		}
		if ((style & SWT.FLAT) != 0) {
			textStyle |= SWT.FLAT;
		}
		text = new Text(this, textStyle);
		int arrowStyle = SWT.ARROW | SWT.DOWN;
		if ((style & SWT.FLAT) != 0) {
			arrowStyle |= SWT.FLAT;
		}
		arrow = new Button(this, arrowStyle);

		listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (popup == event.widget) {
					popupEvent(event);
					return;
				}
				if (text == event.widget) {
					textEvent(event);
					return;
				}
				if (list == event.widget) {
					listEvent(event);
					return;
				}
				if (arrow == event.widget) {
					arrowEvent(event);
					return;
				}
				if (CCombo.this == event.widget) {
					comboEvent(event);
				}

			}
		};

		int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
		for (int i = 0; i < comboEvents.length; i++) {
			this.addListener(comboEvents[i], listener);
		}

		int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.Modify, SWT.MouseDown, SWT.MouseUp, SWT.Traverse, SWT.FocusIn,
				SWT.FocusOut };
		for (int i = 0; i < textEvents.length; i++) {
			text.addListener(textEvents[i], listener);
		}

		int[] arrowEvents = { SWT.Selection, SWT.FocusIn, SWT.FocusOut };
		for (int i = 0; i < arrowEvents.length; i++) {
			arrow.addListener(arrowEvents[i], listener);
		}

		createPopup(null, -1);
		initAccessible();

	}

	/*
	 * public void addFocusListener (FocusListener listener) {
	 * text.addFocusListener( listener ); }
	 */

	static int checkStyle(int style) {
		int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	/**
	 * Adds an item.
	 * <p>
	 * The item is placed at the end of the list. Indexing is zero based.
	 *
	 * @param string the new item
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when the string is null
	 * @exception SWTError(ERROR_ITEM_NOT_ADDED)        when the item cannot be
	 *                                                  added
	 */
	public void add(String string) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		list.add(string);
	}

	/**
	 * Adds an item at an index.
	 * <p>
	 * The item is placed at an index in the list. Indexing is zero based.
	 *
	 * This operation will fail when the index is out of range.
	 *
	 * @param string the new item
	 * @param index  the index for the item
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when the string is null
	 * @exception SWTError(ERROR_ITEM_NOT_ADDED)        when the item cannot be
	 *                                                  added
	 */
	public void add(String string, int index) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		list.add(string, index);
	}

	/**
	 * Adds the listener to receive events.
	 * <p>
	 *
	 * @param listener the listener
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when listener is null
	 */
	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	/**
	 * Adds the listener to receive events.
	 * <p>
	 *
	 * @param listener the listener
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when listener is null
	 */
	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	void arrowEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			hasFocus = true;
			if (getEditable()) {
				text.selectAll();
			}
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			event.display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (CCombo.this.isDisposed()) {
						return;
					}
					Control focusControl = getDisplay().getFocusControl();
					if (focusControl == arrow || focusControl == list || focusControl == text) {
						return;
					}
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
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public void clearSelection() {
		checkWidget();
		text.clearSelection();
		list.deselectAll();
	}

	void comboEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (popup != null && !popup.isDisposed()) {
				list.removeListener(SWT.Dispose, listener);
				popup.dispose();
			}
			popup = null;
			text = null;
			list = null;
			arrow = null;
			break;
		case SWT.Move:
			dropDown(false);
			break;
		case SWT.Resize:
			internalLayout();
			break;
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = 0, height = 0;
		String[] items = list.getItems();
		GC gc = new GC(text);
		int spacer = gc.stringExtent(" ").x; //$NON-NLS-1$
		int textWidth = gc.stringExtent(text.getText()).x;
		for (int i = 0; i < items.length; i++) {
			textWidth = Math.max(gc.stringExtent(items[i]).x, textWidth);
		}
		gc.dispose();
		Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		Point listSize = list.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		int borderWidth = getBorderWidth();

		height = Math.max(textSize.y, arrowSize.y);
		width = Math.max(textWidth + 2 * spacer + arrowSize.x + 2 * borderWidth, listSize.x);
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		return new Point(width + 2 * borderWidth, height + 2 * borderWidth);
	}

	void createPopup(String[] items, int selectionIndex) {
		// create shell and list
		popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		int style = getStyle();
		int listStyle = SWT.SINGLE | SWT.V_SCROLL;
		if ((style & SWT.FLAT) != 0) {
			listStyle |= SWT.FLAT;
		}
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			listStyle |= SWT.RIGHT_TO_LEFT;
		}
		if ((style & SWT.LEFT_TO_RIGHT) != 0) {
			listStyle |= SWT.LEFT_TO_RIGHT;
		}
		list = new List(popup, listStyle);
		if (font != null) {
			list.setFont(font);
		}
		if (foreground != null) {
			list.setForeground(foreground);
		}
		if (background != null) {
			list.setBackground(background);
		}

		int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };
		for (int i = 0; i < popupEvents.length; i++) {
			popup.addListener(popupEvents[i], listener);
		}
		int[] listEvents = { SWT.MouseUp, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn,
				SWT.FocusOut, SWT.Dispose };
		for (int i = 0; i < listEvents.length; i++) {
			list.addListener(listEvents[i], listener);
		}

		if (items != null) {
			list.setItems(items);
		}
		if (selectionIndex != -1) {
			list.setSelection(selectionIndex);
		}
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
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public void deselect(int index) {
		checkWidget();
		list.deselect(index);
	}

	/**
	 * Deselects all items.
	 * <p>
	 *
	 * If an item is selected, it is deselected. If an item is not selected, it
	 * remains unselected.
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public void deselectAll() {
		checkWidget();
		list.deselectAll();
	}

	public void dropDown(boolean drop) {
		if (drop == isDropped()) {
			return;
		}
		if (!drop) {
			popup.setVisible(false);
			text.setFocus();
			return;
		}

		if (getShell() != popup.getParent()) {
			String[] items = list.getItems();
			int selectionIndex = list.getSelectionIndex();
			list.removeListener(SWT.Dispose, listener);
			popup.dispose();
			popup = null;
			list = null;
			createPopup(items, selectionIndex);
		}

		Point size = getSize();
		int itemCount = list.getItemCount();
		itemCount = (itemCount == 0) ? visibleItemCount : Math.min(visibleItemCount, itemCount);
		int itemHeight = list.getItemHeight() * itemCount;
		Point listSize = list.computeSize(SWT.DEFAULT, itemHeight);
		list.setBounds(1, 1, Math.max(size.x - 2, listSize.x), listSize.y);

		int index = list.getSelectionIndex();
		if (index != -1) {
			list.setTopIndex(index);
		}
		Display display = getDisplay();
		Rectangle listRect = list.getBounds();
		Rectangle parentRect = display.map(getParent(), null, getBounds());
		Point comboSize = getSize();
		Rectangle displayRect = getMonitor().getClientArea();
		int width = Math.max(comboSize.x, listRect.width + 2);
		int height = listRect.height + 2;
		int x = parentRect.x;
		int y = parentRect.y + comboSize.y;
		if (y + height > displayRect.y + displayRect.height) {
			y = parentRect.y - height;
		}
		popup.setBounds(x, y, width, height);
		popup.setVisible(true);
		list.setFocus();
	}

	@Override
	public Control[] getChildren() {
		checkWidget();
		return new Control[0];
	}

	/**
	 * Gets the editable state.
	 *
	 * @return true if the contents can be edited
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
	public boolean getEditable() {
		checkWidget();
		return text.getEditable();
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
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_CANNOT_GET_ITEM)       when the operation fails
	 */
	public String getItem(int index) {
		checkWidget();
		return list.getItem(index);
	}

	/**
	 * Gets the number of items.
	 * <p>
	 * This operation will fail if the number of items could not be queried from the
	 * OS.
	 *
	 * @return the number of items in the widget
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_CANNOT_GET_COUNT)      when the operation fails
	 */
	public int getItemCount() {
		checkWidget();
		return list.getItemCount();
	}

	/**
	 * Gets the height of one item.
	 * <p>
	 * This operation will fail if the height of one item could not be queried from
	 * the OS.
	 *
	 * @return the height of one item in the widget
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS)  when called from the wrong
	 *                                                   thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)        when the widget has been
	 *                                                   disposed
	 * @exception SWTError(ERROR_CANNOT_GET_ITEM_HEIGHT) when the operation fails
	 */
	public int getItemHeight() {
		checkWidget();
		return list.getItemHeight();
	}

	/**
	 * Gets the items.
	 * <p>
	 * This operation will fail if the items cannot be queried from the OS.
	 *
	 * @return the items in the widget
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_CANNOT_GET_ITEM)       when the operation fails
	 */
	public String[] getItems() {
		checkWidget();
		return list.getItems();
	}

	/**
	 * Gets the selection.
	 * <p>
	 *
	 * @return a point representing the selection start and end
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public Point getSelection() {
		checkWidget();
		return text.getSelection();
	}

	/**
	 * Gets the index of the selected item.
	 * <p>
	 * Indexing is zero based. If no item is selected -1 is returned.
	 *
	 * @return the index of the selected item.
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public int getSelectionIndex() {
		checkWidget();
		return list.getSelectionIndex();
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		style &= ~SWT.READ_ONLY;
		if (!text.getEditable()) {
			style |= SWT.READ_ONLY;
		}
		return style;
	}

	/**
	 * Gets the widget text.
	 * <p>
	 * If the widget has no text, an empty string is returned.
	 *
	 * @return the widget text
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public String getText() {
		checkWidget();
		return text.getText();
	}

	/**
	 * Gets the height of the combo's text field.
	 * <p>
	 * The operation will fail if the height cannot be queried from the OS.
	 *
	 * @return the height of the combo's text field.
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS)        when called from the
	 *                                                         wrong thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)              when the widget has
	 *                                                         been disposed
	 * @exception SWTError(ERROR_ERROR_CANNOT_GET_ITEM_HEIGHT) when the operation
	 *                                                         fails
	 */
	public int getTextHeight() {
		checkWidget();
		return text.getLineHeight();
	}

	/**
	 * Gets the text limit.
	 * <p>
	 *
	 * @return the text limit
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public int getTextLimit() {
		checkWidget();
		return text.getTextLimit();
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
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when string is null
	 */
	public int indexOf(String string) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return list.indexOf(string);
	}

	/**
	 * Gets the index of an item.
	 * <p>
	 * The widget is searched starting at start including the end position until an
	 * item is found that is equal to the search itenm. If no item is found, -1 is
	 * returned. Indexing is zero based.
	 *
	 * @param string the search item
	 * @param start  the starting position
	 * @return the index of the item
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when string is null
	 */
	public int indexOf(String string, int start) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		return list.indexOf(string, start);
	}

	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {
			@Override
			public void getCaretOffset(AccessibleTextEvent e) {
				e.offset = text.getCaretPosition();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(new Point(e.x, e.y));
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(new Point(location.x, location.y));
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}

			@Override
			public void getValue(AccessibleControlEvent e) {
				e.result = getText();
			}
		});
	}

	boolean isDropped() {
		return popup.getVisible();
	}

	@Override
	public boolean isFocusControl() {
		checkWidget();
		if (text.isFocusControl() || arrow.isFocusControl() || list.isFocusControl() || popup.isFocusControl()) {
			return true;
		}
		return super.isFocusControl();
	}

	void internalLayout() {
		if (isDropped()) {
			dropDown(false);
		}

		Rectangle rect = getClientArea();
		int width = rect.width;
		int height = rect.height;
		Point arrowSize = arrow.computeSize(SWT.DEFAULT, height);
		text.setBounds(0, 0, width - arrowSize.x, height);
		arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
	}

	void listEvent(Event event) {
		switch (event.type) {
		case SWT.Dispose:
			if (getShell() != popup.getParent()) {
				String[] items = list.getItems();
				int selectionIndex = list.getSelectionIndex();
				popup = null;
				list = null;
				createPopup(items, selectionIndex);
			}
			break;
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			hasFocus = true;
			if (getEditable()) {
				text.selectAll();
			}
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			event.display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (CCombo.this.isDisposed()) {
						return;
					}
					Control focusControl = getDisplay().getFocusControl();
					if (focusControl == arrow || focusControl == list || focusControl == text) {
						return;
					}
					hasFocus = false;
					Event e = new Event();
					notifyListeners(SWT.FocusOut, e);
				}
			});
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) {
				return;
			}
			dropDown(false);
			int index = list.getSelectionIndex();
			if (index == -1) {
				return;
			}
			text.setText(list.getItem(index));
			text.selectAll();
			list.setSelection(index);
			Event e = new Event();
			e.time = event.time;
			e.stateMask = event.stateMask;
			e.doit = event.doit;
			notifyListeners(SWT.Selection, e);
			event.doit = e.doit;
			break;
		}
		case SWT.Selection: {
//			int index = list.getSelectionIndex ();
//			if (index == -1) return;
//			text.setText (list.getItem (index));
//			text.selectAll ();
//			list.setSelection(index);
//			Event e = new Event();
//			e.time = event.time;
//			e.stateMask = event.stateMask;
//			e.doit = event.doit;
//			notifyListeners(SWT.Selection, e);
//			event.doit = e.doit;
//			break;
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
			if (event.character == SWT.CR) {
				dropDown(false);
				int index = list.getSelectionIndex();
				if (index == -1) {
					return;
				}
				text.setText(list.getItem(index));
				text.selectAll();
				list.setSelection(index);
				e.doit = event.doit;
				notifyListeners(SWT.Selection, e);
			}
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
				// dropDown (false);
				Event e = new Event();
				e.time = event.time;
				e.stateMask = event.stateMask;
				notifyListeners(SWT.Traverse, e);
			}

			if (event.character == SWT.BAR) {
				dropDown(true);
				Event e = new Event();
				e.time = event.time;
				e.stateMask = event.stateMask;
				// notifyListeners(SWT.DefaultSelection, e);
			}

			if (event.character == 32) {
				int oldIndex = getSelectionIndex();

				select((oldIndex + 1) % getItemCount());

				if (oldIndex != getSelectionIndex()) {
					Event e = new Event();
					e.time = event.time;
					e.stateMask = event.stateMask;
					notifyListeners(SWT.Selection, e);
				}
			}

			if (!getEditable()) {
				this.setMatchSelection(event);
			}

			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed()) {
				break;
			}
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
	 * @param c
	 * @return
	 */
	private int fintMatchItem(char c) {
		String[] items = getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].indexOf(c) == 0) {
				return i;
			}
		}
		return -1;
	}

	void popupEvent(Event event) {
		switch (event.type) {
		case SWT.Paint:
			// draw black rectangle around list
			Rectangle listRect = list.getBounds();
			Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, listRect.width + 1, listRect.height + 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDown(false);
			break;
		case SWT.Deactivate:
			dropDown(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void redraw() {
		super.redraw();
		text.redraw();
		arrow.redraw();
		if (popup.isVisible()) {
			list.redraw();
		}
	}

	@Override
	public void redraw(int x, int y, int width, int height, boolean all) {
		super.redraw(x, y, width, height, true);
	}

	/**
	 * Removes an item at an index.
	 * <p>
	 * Indexing is zero based.
	 *
	 * This operation will fail when the index is out of range or an item could not
	 * be removed from the OS.
	 *
	 * @param index the index of the item
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_ITEM_NOT_REMOVED)      when the operation fails
	 */
	public void remove(int index) {
		checkWidget();
		list.remove(index);
	}

	/**
	 * Removes a range of items.
	 * <p>
	 * Indexing is zero based. The range of items is from the start index up to and
	 * including the end index.
	 *
	 * This operation will fail when the index is out of range or an item could not
	 * be removed from the OS.
	 *
	 * @param start the start of the range
	 * @param end   the end of the range
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_ITEM_NOT_REMOVED)      when the operation fails
	 */
	public void remove(int start, int end) {
		checkWidget();
		list.remove(start, end);
	}

	/**
	 * Removes an item.
	 * <p>
	 * This operation will fail when the item could not be removed from the OS.
	 *
	 * @param string the search item
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when string is null
	 * @exception SWTError(ERROR_ITEM_NOT_REMOVED)      when the operation fails
	 */
	public void remove(String string) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		list.remove(string);
	}

	/**
	 * Removes all items.
	 * <p>
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public void removeAll() {
		checkWidget();
		text.setText(""); //$NON-NLS-1$
		list.removeAll();
	}

	/**
	 * Removes the listener.
	 * <p>
	 *
	 * @param listener the listener
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when listener is null
	 */
	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		removeListener(SWT.Modify, listener);
	}

	/**
	 * Removes the listener.
	 * <p>
	 *
	 * @param listener the listener
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when listener is null
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
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
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 */
	public void select(int index) {
		checkWidget();
		if (index == -1) {
			list.deselectAll();
			text.setText(""); //$NON-NLS-1$
			return;
		}
		if (0 <= index && index < list.getItemCount()) {
			if (index != getSelectionIndex()) {
				text.setText(list.getItem(index));
				text.selectAll();
				list.select(index);
				list.showSelection();
			}
		}
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		background = color;
		if (text != null) {
			text.setBackground(color);
		}
		if (list != null) {
			list.setBackground(color);
		}
		if (arrow != null) {
			arrow.setBackground(color);
		}
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
	public void setEditable(boolean editable) {
		checkWidget();
		text.setEditable(editable);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (popup != null) {
			popup.setVisible(false);
		}
		if (text != null) {
			text.setEnabled(enabled);
		}
		if (arrow != null) {
			arrow.setEnabled(enabled);
		}
	}

	@Override
	public boolean setFocus() {
		checkWidget();
		return text.setFocus();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.font = font;
		text.setFont(font);
		list.setFont(font);
		internalLayout();
	}

	@Override
	public void setForeground(Color color) {
		super.setForeground(color);
		foreground = color;
		if (text != null) {
			text.setForeground(color);
		}
		if (list != null) {
			list.setForeground(color);
		}
		if (arrow != null) {
			arrow.setForeground(color);
		}
	}

	/**
	 * Sets the text of an item; indexing is zero based.
	 *
	 * This operation will fail when the index is out of range or an item could not
	 * be changed in the OS.
	 *
	 * @param index  the index for the item
	 * @param string the item
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when items is null
	 * @exception SWTError(ERROR_ITEM_NOT_MODIFIED)     when the operation fails
	 */
	public void setItem(int index, String string) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		list.setItem(index, string);
	}

	/**
	 * Sets all items.
	 *
	 * @param items the array of items
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when items is null
	 * @exception SWTError(ERROR_ITEM_NOT_ADDED)        when the operation fails
	 */
	public void setItems(String[] items) {
		checkWidget();
		if (items == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (!text.getEditable()) {
			text.setText(""); //$NON-NLS-1$
		}
		list.setItems(items);
	}

	/**
	 * Sets the new selection.
	 *
	 * @param selection point representing the start and the end of the new
	 *                  selection
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when selection is null
	 */
	public void setSelection(Point selection) {
		checkWidget();
		if (selection == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		text.setSelection(selection.x, selection.y);
	}

	/**
	 * Sets the widget text.
	 *
	 * @param string the widget text
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_NULL_ARGUMENT)         when string is null
	 */
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		int index = list.indexOf(string);
		if (index == -1) {
			list.deselectAll();
			text.setText(string);
			return;
		}
		text.setText(string);
		text.selectAll();
		list.setSelection(index);
		list.showSelection();
	}

	/**
	 * Sets the text limit.
	 *
	 * @param limit new text limit
	 *
	 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS) when called from the wrong
	 *                                                  thread
	 * @exception SWTError(ERROR_WIDGET_DISPOSED)       when the widget has been
	 *                                                  disposed
	 * @exception SWTError(ERROR_CANNOT_BE_ZERO)        when limit is 0
	 */
	public void setTextLimit(int limit) {
		checkWidget();
		text.setTextLimit(limit);
	}

	@Override
	public void setToolTipText(String string) {
		checkWidget();
		super.setToolTipText(string);
		arrow.setToolTipText(string);
		text.setToolTipText(string);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			popup.setVisible(false);
		}
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
		if (count < 0) {
			return;
		}
		visibleItemCount = count;
	}

	void textEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			hasFocus = true;
			if (getEditable()) {
				text.selectAll();
			}
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			event.display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (CCombo.this.isDisposed()) {
						return;
					}
					Control focusControl = getDisplay().getFocusControl();
					if (focusControl == arrow || focusControl == list || focusControl == text) {
						return;
					}
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
				break;
			}
			// At this point the widget may have been disposed.
			// If so, do not continue.
			if (isDisposed()) {
				break;
			}

			if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
				if ((event.stateMask & SWT.ALT) != 0) {
					boolean dropped = isDropped();
					text.selectAll();
					if (!dropped) {
						setFocus();
					}
					dropDown(!dropped);
					break;
				}
				int oldIndex = getSelectionIndex();

				if (event.keyCode == SWT.ARROW_UP) {
					select(Math.max(oldIndex - 1, 0));
				} else {
					select(Math.min(oldIndex + 1, getItemCount() - 1));
				}

//				if (oldIndex != getSelectionIndex ()) {
//					Event e = new Event();
//					e.time = event.time;
//					e.stateMask = event.stateMask;
//					notifyListeners(SWT.Selection, e);
//				}
				// At this point the widget may have been disposed.
				// If so, do not continue.
				if (isDisposed()) {
					break;
				}
			}

			if (!getEditable()) {
				boolean dropped = isDropped();
				if (!dropped) {
					setFocus();
				}
				dropDown(!dropped);
				if (!dropped) {
					setMatchSelection(event);
				}
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
			list.deselectAll();
			Event e = new Event();
			e.time = event.time;
			notifyListeners(SWT.Modify, e);
			break;
		}
		case SWT.MouseDown: {
			if ((event.button != 1) || text.getEditable()) {
				return;
			}
			boolean dropped = isDropped();
			text.selectAll();
			if (!dropped) {
				setFocus();
			}
			dropDown(!dropped);
			break;
		}
		case SWT.MouseUp: {
			if ((event.button != 1) || text.getEditable()) {
				return;
			}
			text.selectAll();
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				// The enter causes default selection and
				// the arrow keys are used to manipulate the list contents so
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

	/**
	 * @param event
	 *
	 */
	private void setMatchSelection(Event event) {
		int index = fintMatchItem(event.character);
		if (index != -1) {
			select(index);
		}
//	if (oldIndex != getSelectionIndex ()) {
//		Event e = new Event();
//		e.time = event.time;
//		e.stateMask = event.stateMask;
//		notifyListeners(SWT.Selection, e);
//	}
	}
}
