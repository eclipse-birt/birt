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
import org.eclipse.swt.graphics.Font;
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
public class SpinnerTime extends Composite implements ActionListener, IPropertyChangeListener {

	private Button up = null;

	private Button down = null;

	private Label label = null;

	private SpinnerTimeText hour = null;

	private SpinnerTimeText min = null;

	private SpinnerTimeText sec = null;

	private Label firstLabel = null;

	private Timer timer = null;

	private Label lastLabel = null;

	static SpinnerTimeText defaultText = null;

	private int value = 1;

	private static final int DELAY = 150;

	private ArrayList listenerList = new ArrayList();

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * 
	 * @param parent
	 * @param style
	 */
	public SpinnerTime(Composite parent, int style) {
		this(parent, style, 0, 0, 0);
	}

	/**
	 * Constructs a new instance of this class given its parent , a style and time
	 * 
	 * @param parent
	 * @param style
	 * @param hour
	 * @param min
	 * @param sec
	 */
	public SpinnerTime(Composite parent, int style, int hour, int min, int sec) {
		super(parent, style | SWT.BORDER);
		defaultText = null;
		initComponents();
		this.hour.setValue(String.valueOf(hour));
		this.min.setValue(String.valueOf(min));
		this.sec.setValue(String.valueOf(sec));

		setLayout(new SpinnerTimeLayout());
		initActions();

		setSize(86, 28);
		timer = new Timer(DELAY, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		buttonAction(value, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#checkWidget()
	 */
	protected void checkWidget() {
		// System.out.println("checkThis");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.
	 * jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		firePropertyListener(event);
	}

	private void initComponents() {
		hour = new SpinnerTimeText(this, SWT.NONE, 24, IPropertyEventConstants.HOUR_CHANGE_EVENT);
		firstLabel = new Label(this, SWT.NONE);
		min = new SpinnerTimeText(this, SWT.NONE, 60, IPropertyEventConstants.MIN_CHANGE_EVENT);
		lastLabel = new Label(this, SWT.NONE);
		sec = new SpinnerTimeText(this, SWT.NONE, 60, IPropertyEventConstants.SECOND_CHANGE_EVENT);

		// Font font = new Font( Display.getCurrent( ), "Dialog", 10, SWT.BOLD
		// ); //$NON-NLS-1$
		Font font = FontManager.getFont("Dialog", 10, SWT.BOLD); //$NON-NLS-1$

		firstLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lastLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		firstLabel.setFont(font);
		lastLabel.setFont(font);

		firstLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		lastLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

		firstLabel.setText(":"); //$NON-NLS-1$
		lastLabel.setText(":"); //$NON-NLS-1$

		up = new Button(this, SWT.ARROW | SWT.UP);
		down = new Button(this, SWT.ARROW | SWT.DOWN);
		label = new Label(this, SWT.NONE);

		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		// added by gao 2004.07.08
		// font.dispose( );
	}

	private void initActions() {
		up.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				value = 1;
				timer.start();

			}

			public void mouseUp(MouseEvent e) {
				timer.stop();
				buttonAction(1, true);
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
				buttonAction(-1, true);
			}

		});

		hour.addPropertyChangeListener(this);
		min.addPropertyChangeListener(this);
		sec.addPropertyChangeListener(this);
	}

	private SpinnerTimeText getDefaultText() {
		if (defaultText == null) {
			defaultText = hour;
		}

		return defaultText;
	}

	private void buttonAction(int increat, boolean needFocus) {
		int value;
		try {
			value = Integer.parseInt(getDefaultText().getValue());
		} catch (Exception e) {
			return;
		}

		value = value + increat;
		SpinnerTimeText text = getDefaultText();

		text.setValue(String.valueOf(value));
		if (needFocus) {
			text.forceFocus();
		}
		text.selectAll();

	}

	private int transValue(int value, int limit) {
		if (limit == 0) {
			return value;
		}
		value = value % limit;
		if (value < 0) {
			value = value + limit;
		}
		return value;
	}

	/**
	 * Gets time information.
	 * 
	 * @return SpinnerTimeInfo
	 */
	public SpinnerTimeInfo getTimeInfo() {
		return new SpinnerTimeInfo(Integer.parseInt(hour.getValue()), Integer.parseInt(min.getValue()),
				Integer.parseInt(sec.getValue()));
	}

	/**
	 * Sets the time information.
	 * 
	 * @param info
	 */
	public void setTimeInfo(SpinnerTimeInfo info) {
		// may be fire event?
		int hourValue = transValue(info.getHour(), hour.limit);
		hour.value = String.valueOf(hourValue);
		hour.setText(String.valueOf(hourValue));

		int minValue = transValue(info.getMin(), min.limit);
		min.value = String.valueOf(minValue);
		min.setText(String.valueOf(minValue));

		int secValue = transValue(info.getSec(), sec.limit);
		sec.value = String.valueOf(secValue);
		sec.setText(String.valueOf(secValue));

	}

	/**
	 * Store the time information.
	 * 
	 * @author gao To change the template for this generated type comment go to
	 *         Window - Preferences - Java - Code Generation - Code and Comments
	 */
	public static class SpinnerTimeInfo {

		private int hour;

		private int min;

		private int sec;

		public SpinnerTimeInfo(int hour, int min, int sec) {
			this.hour = hour;
			this.min = min;
			this.sec = sec;
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public int getSec() {
			return sec;
		}

		public void setSec(int sec) {
			this.sec = sec;
		}
	}

}

class SpinnerTimeLayout extends Layout {

	protected Point computeSize(Composite composite, int wHint, int hHint, boolean changed) {

		return new Point(85, 28);

	}

	protected void layout(Composite composite, boolean changed) {
		Control[] children = composite.getChildren();
		children[0].setBounds(0, 0, 21, 25);
		children[1].setBounds(21, 0, 2, 25);
		children[2].setBounds(23, 0, 21, 25);
		children[3].setBounds(44, 0, 2, 25);
		children[4].setBounds(46, 0, 21, 25);

		children[5].setBounds(67, 0, 16, 12);
		children[6].setBounds(67, 13, 16, 12);
		children[7].setBounds(67, 12, 17, 3);
	}

}

class SpinnerTimeText extends Text {

	String value = "0"; //$NON-NLS-1$

	int limit;

	private String chnageName = ""; //$NON-NLS-1$

	private ArrayList listenerList = new ArrayList();

	public SpinnerTimeText(Composite parent, int style, int limit, String name) {
		super(parent, style);
		setTextLimit(2);
		this.limit = limit;
		this.chnageName = name;
		// Font font = new Font( Display.getCurrent( ), "Dialog", 10, SWT.BOLD
		// ); //$NON-NLS-1$
		// setFont( font );

		setFont(FontManager.getFont("Dialog", 10, SWT.BOLD)); //$NON-NLS-1$

		addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				// System.out.println( "lost" );
				setValue(getText());
			}

			public void focusGained(FocusEvent e) {
				// System.out.println( "gain" );
				SpinnerTime.defaultText = SpinnerTimeText.this;
				// setSelection(0,1);
			}

		});

		addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					setValue(getText());
				}
			}

		});

	}

	protected void checkWidget() {
		// System.out.println("checkTxext");
	}

	protected void checkSubclass() {

	}

	public String getValue() {
		return value;
	}

	public void setChangeName(String name) {
		this.chnageName = name;
	}

	public String getChangeName() {
		return chnageName;
	}

	public void setValue(String va) {
		int intValue;
		try {
			intValue = Integer.parseInt(va);
		} catch (Exception e) {
			// System.out.println( value );
			setText(value);
			return;
		}
		intValue = intValue % limit;
		if (intValue < 0) {
			intValue = intValue + limit;
		}

		PropertyChangeEvent event = new PropertyChangeEvent(new Object(), getChangeName(),
				Integer.valueOf(Integer.parseInt(value)), Integer.valueOf(intValue));

		firePropertyListener(event);

		value = String.valueOf(intValue);
		// System.out.println("value==" + getChangeName());

		// System.out.println("display==" + getDisplay());
		setText(value);

	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.add(listener);

	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.remove(listener);
	}

	public void firePropertyListener(PropertyChangeEvent e) {
		int size = listenerList.size();
		for (int i = 0; i < size; i++) {
			IPropertyChangeListener listener = (IPropertyChangeListener) listenerList.get(i);
			listener.propertyChange(e);
		}
	}

}