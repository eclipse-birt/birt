/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

/**
 * The Spinner component base on SWT,Fetch and setting the year value
 */
public class SpinnerYear extends Composite implements ActionListener {

	private Button up = null;

	private Button down = null;

	private Text text = null;

	private Label label = null;

	private String textContend = ""; //$NON-NLS-1$

	private Timer timer = null;

	private ArrayList listenerList = new ArrayList();

	private int value = 1;

	private static final int DELAY = 150;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * 
	 * @param parent
	 * @param style
	 */
	public SpinnerYear(Composite parent, int style) {
		this(parent, style, 0);
	}

	/**
	 * Constructs a new instance of this class given its parent,a style and year
	 * value.
	 * 
	 * @param parent
	 * @param style
	 * @param year
	 */
	public SpinnerYear(Composite parent, int style, int year) {
		super(parent, style | SWT.BORDER);

		initComponents();
		initActions();
		setLayout(new SpinnerYearLayout());
		initParent();

		textContend = String.valueOf(year);
		text.setText(textContend);
		timer = new Timer(DELAY, this);
	}

	/**
	 * Deal with continue click the arrow button
	 */
	public void actionPerformed(ActionEvent evt) {
		buttonAction(value);
	}

	private void initParent() {
		setSize(65, 28);

	}

	private void initComponents() {
		up = new Button(this, SWT.ARROW | SWT.UP);
		down = new Button(this, SWT.ARROW | SWT.DOWN);
		text = new SpinnerText(this, SWT.NONE);
		label = new Label(this, SWT.NONE);

		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// Font font = new Font( Display.getCurrent( ), "Dialog", 12, SWT.BOLD
		// ); //$NON-NLS-1$
		// text.setFont( font );
		text.setFont(FontManager.getFont("Dialog", 12, SWT.BOLD)); //$NON-NLS-1$

		text.setTextLimit(5);

	}

	private void initActions() {
		text.addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				setText(text.getText());
			}

		});

		text.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					setText(text.getText());
				}
			}

		});

		up.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				value = 1;
				timer.start();

			}

			public void mouseUp(MouseEvent e) {
				timer.stop();
				buttonAction(1);
			}

		});

		down.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				value = -1;
				timer.start();

			}

			public void mouseUp(MouseEvent e) {
				// System.out.println("2222222222222");
				timer.stop();
				buttonAction(-1);
			}

		});
	}

	private void setText(String text) {
		int value;
		try {
			value = Integer.parseInt(text);
		} catch (Exception e) {
			this.text.setText(textContend);
			return;
		}

		if (value < 0) {
			this.text.setText(textContend);
			return;
		}

		// fire value
		firePropertyListener(new PropertyChangeEvent(new Object(), IPropertyEventConstants.YEAR_CHANGE_EVENT,
				Integer.valueOf(Integer.parseInt(textContend)), Integer.valueOf(value)));
		this.textContend = text;
		this.text.setText(textContend);

	}

	private void buttonAction(int increat) {
		int value;
		try {
			value = Integer.parseInt(textContend);
		} catch (Exception e) {
			return;
		}

		value = value + increat;
		setText(String.valueOf(value));
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the year value change, by sending it one of the messages defined in the
	 * IPropertyChangeListener interface.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.add(listener);

	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the year value change
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.remove(listener);
	}

	/**
	 * Fire the event when the year value change
	 * 
	 * @param e
	 */
	public void firePropertyListener(PropertyChangeEvent e) {
		int size = listenerList.size();
		for (int i = 0; i < size; i++) {
			IPropertyChangeListener listener = (IPropertyChangeListener) listenerList.get(i);
			listener.propertyChange(e);
		}
	}

	/**
	 * Sets the year value
	 * 
	 * @param year
	 */
	public void setYear(int year) {
		this.textContend = String.valueOf(year);
		this.text.setText(textContend);
	}

	/**
	 * Gets the year value
	 * 
	 * @return the year value
	 */
	public int getYear() {
		return Integer.parseInt(textContend);
	}

}

class SpinnerYearLayout extends Layout {

	protected Point computeSize(Composite composite, int wHint, int hHint, boolean changed) {

		return new Point(65, 28);

	}

	protected void layout(Composite composite, boolean changed) {
		Control[] children = composite.getChildren();
		children[0].setBounds(44, 0, 16, 12);
		children[1].setBounds(44, 13, 16, 12);
		children[2].setBounds(0, 0, 46, 25);
		children[3].setBounds(44, 12, 17, 3);
	}

}

class SpinnerText extends Text {

	public SpinnerText(Composite parent, int style) {
		super(parent, style);
	}

	protected void checkWidget() {

	}

	protected void checkSubclass() {

	}
}