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

package org.eclipse.birt.chart.ui.swt;

import java.util.Vector;

import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Widget;

/**
 * Custom widget like Table. Comparing to SWT table, some additional features
 * are added, such as D&D support, background color and column selection.
 * 
 */
public class CustomPreviewTable extends Composite
		implements MouseListener, MouseMoveListener, ControlListener, DisposeListener, KeyListener {

	// HEIGHT OF DATA ROWS IN THE TABLE
	private static final int ROW_HEIGHT = 20;

	public static final int MOUSE_RIGHT_CLICK_TYPE = 0;
	public static final int FOCUS_IN = 1;

	/** INT HOLDING THE SPLITTER WIDTH FOR RESIZING */
	private static final int SPLITTER_WIDTH = 2;

	// COLLECTION OF HEADING LABELS
	private transient Vector<ColumnBindingInfo> fHeadings = null;
	// INDICATE WHEATHER DUMMY
	private transient boolean isDummy = true;
	// COLLECTION OF COLUMN WIDTHS AS INTEGERS
	transient Vector<Integer> columnWidths = null;
	// COLLECTION OF BUTTONS IN THE HEADER
	private transient Vector<Button> btnHeaders = null;
	// COLLECTION OF LISTENERS...LISTENING TO TABLE EVENTS
	private transient Vector<Listener> vListeners = null;

	private transient Composite cmpHeaders = null;
	private transient TableCanvas cnvCells = null;
	private transient int iHeaderAlignment = SWT.CENTER;

	/**
	 * INT HOLDING THE VERTICAL SCROLL AMOUNT...ITS VALUE REPRESENTS HOW MANY ROWS
	 * TO THE BOTTOM THE USER HAS SCROLLED...I.E. HOW MANY ROWS ARE HIDDEN TO THE
	 * TOP OF THE TABLE
	 */
	transient int iVScroll = 0;
	/**
	 * INT HOLDING THE LAST HORIZONTAL SCROLL POSITION...USED TO PREVENT USERS
	 * SCROLLING OFF THE LEFT OR RIGHT EDGES OF THE TABLE
	 */
	transient int iLastProcessedHorizontalScrollPosition = 0;
	/**
	 * INT HOLDING THE LAST VERTICAL SCROLL POSITION...USED TO PREVENT USERS
	 * SCROLLING OFF THE TOP OR BOTTOM EDGES OF THE TABLE
	 */
	transient int iLastProcessedVerticalScrollPosition = 0;

	/**
	 * INT HOLDING THE CURRENT SELECTED OR ACTIVE COLUMN...-1 INDICATES NO COLUMN IS
	 * SELECTED
	 */
	transient int iColumnIndex = -1;

	/**
	 * BOOLEAN INDICATING A DRAG OPERATION IS IN PROGRESS...USED TO DETERMINE IF
	 * CELLS ARE TO BE REDRAWN
	 */
	transient boolean bDragging = false;
	/** INT HOLDING THE INDEX OF THE COLUMN BEING RESIZED */
	private transient int iResizingColumnIndex = -1;
	/** INT HOLDING THE START LOCATION OF THE DRAG OPERATION */
	private transient int iDragStartXLocation = -1;

	/** INT HOLDING THE WIDTH HIDDEN IN THE LEFT BOUND */
	private transient int iHiddenWidth = 0;

	Listener headerButtonListener = new Listener() {

		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.Selection:
				// SELECT COLUMN IN TABLE
				cnvCells.selectColumn(btnHeaders.indexOf(event.widget));
				break;

			case SWT.FocusIn:
				// Maintain the index correct after focus in
				iColumnIndex = btnHeaders.indexOf(event.widget);

				Event newEvent = new Event();
				newEvent.widget = event.widget;
				newEvent.type = FOCUS_IN;
				newEvent.data = Integer.valueOf(iColumnIndex);
				fireEvent(newEvent);
				break;
			}
		}
	};

	TraverseListener traverseListener = new TraverseListener() {

		public void keyTraversed(TraverseEvent e) {
			if (e.character != SWT.TAB) {
				e.doit = false;
			}
		}
	};

	/**
	 * @param parent
	 * @param style
	 */
	public CustomPreviewTable(Composite parent, int style) {
		super(parent, SWT.BORDER);
		fHeadings = new Vector<ColumnBindingInfo>();
		columnWidths = new Vector<Integer>();
		btnHeaders = new Vector<Button>();
		vListeners = new Vector<Listener>();
		placeComponents();
		createDummyTable();
	}

	private void placeComponents() {
		if (cmpHeaders == null) {
			GridLayout glTable = new GridLayout();
			glTable.numColumns = 1;
			glTable.marginWidth = 1;
			glTable.marginHeight = 1;
			glTable.horizontalSpacing = 0;
			glTable.verticalSpacing = 0;

			setLayout(glTable);

			cmpHeaders = new Composite(this, SWT.NONE);
			GridData gdCmpHeaders = new GridData(GridData.FILL_HORIZONTAL);
			cmpHeaders.setLayoutData(gdCmpHeaders);

			FormLayout glHeaders = new FormLayout();
			glHeaders.marginHeight = 0;
			glHeaders.marginWidth = 0;

			cmpHeaders.setLayout(glHeaders);
			cmpHeaders.addMouseListener(this);
		} else {
			Control[] buttons = cmpHeaders.getChildren();
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].dispose();
			}
		}
		btnHeaders.clear();
		if (isDummy) {
			for (int i = 0; i < columnWidths.size(); i++) {
				addHeaderButton(iHeaderAlignment, "", //$NON-NLS-1$
						columnWidths.get(i));
			}
		} else {
			for (int i = 0; i < fHeadings.size(); i++) {
				addHeaderButton(iHeaderAlignment, fHeadings.elementAt(i), columnWidths.get(i), i);

			}
		}

		cmpHeaders.layout();

		// DISPOSE EXISTING cnvCells instance if any
		if (cnvCells != null && !cnvCells.isDisposed()) {
			cnvCells.dispose();
		}
		cnvCells = new TableCanvas(this, SWT.NONE, fHeadings.size(), new Color[] {}, this);
		cnvCells.setLayoutData(new GridData(GridData.FILL_BOTH));
		cnvCells.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		cnvCells.addMouseMoveListener(this);
		getShell().addControlListener(this);
		addDisposeListener(this);
	}

	private void addHeaderButton(int style, String sColumnHeading, int iWidth) {
		Button btnHeader = new Button(cmpHeaders, style);
		FormData fd = new FormData();
		fd.top = new FormAttachment(2);
		int i = btnHeaders.size();
		if (i == 0) {
			fd.left = new FormAttachment(0);
		} else {
			Button btnNeighbor = btnHeaders.get(i - 1);
			fd.left = new FormAttachment(btnNeighbor, SPLITTER_WIDTH);
		}
		fd.width = iWidth - SPLITTER_WIDTH;
		// fd.height = HEADER_HEIGHT;
		btnHeader.setLayoutData(fd);
		btnHeader.setText(sColumnHeading);
		btnHeader.setVisible(true);
		btnHeader.addListener(SWT.Selection, headerButtonListener);
		btnHeader.addKeyListener(this);
		btnHeader.addTraverseListener(traverseListener);
		btnHeader.addListener(SWT.FocusIn, headerButtonListener);
		btnHeader.addMouseListener(this);
		btnHeader.addMouseMoveListener(this);
		// Add drag support
		addDragListenerToHeaderButton(btnHeader);

		btnHeaders.add(btnHeader);

		// create menu
		fireMenuEvent(btnHeader, false);

		// Use this splitter to resize the column
		addHeaderSplitter();
	}

	private void addHeaderButton(int style, ColumnBindingInfo columnHeader, int iWidth, int index) {
		Button btnHeader = new Button(cmpHeaders, style);

		btnHeader.setText(columnHeader.getName());
		if (columnHeader.getImageName() != null) {
			btnHeader.setImage(UIHelper.getImage(columnHeader.getImageName()));
		}
		if (columnHeader.getTooltip() != null) {
			btnHeader.setToolTipText(columnHeader.getTooltip());
		}

		FormData fd = new FormData();
		fd.top = new FormAttachment(2);
		int i = btnHeaders.size();
		if (i == 0) {
			fd.left = new FormAttachment(0);
		} else {
			Button btnNeighbor = btnHeaders.get(i - 1);
			fd.left = new FormAttachment(btnNeighbor, SPLITTER_WIDTH);
		}
		int defaultWidth = iWidth - SPLITTER_WIDTH;
		int preferWidth = btnHeader.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		if (preferWidth > defaultWidth) {
			fd.width = preferWidth;
			columnWidths.remove(index);
			columnWidths.add(index, Integer.valueOf(preferWidth + SPLITTER_WIDTH));
		} else {
			fd.width = defaultWidth;
		}
		// fd.height = HEADER_HEIGHT;
		int h = ChartUIUtil.getImageButtonDefaultHeightByPlatform();
		if (h > 0) {
			fd.height = h;
		}
		btnHeader.setLayoutData(fd);

		btnHeader.setVisible(true);
		btnHeader.addListener(SWT.Selection, headerButtonListener);
		btnHeader.addKeyListener(this);
		btnHeader.addTraverseListener(traverseListener);
		btnHeader.addListener(SWT.FocusIn, headerButtonListener);
		btnHeader.addMouseListener(this);
		btnHeader.addMouseMoveListener(this);
		// Add drag support
		addDragListenerToHeaderButton(btnHeader);

		btnHeaders.add(btnHeader);

		// create menu
		fireMenuEvent(btnHeader, false);

		// Use this splitter to resize the column
		addHeaderSplitter();
	}

	private void addHeaderSplitter() {
		Label splitter = new Label(cmpHeaders, SWT.NONE);
		FormData fd = new FormData();
		fd.top = new FormAttachment(2);
		int i = btnHeaders.size();
		if (i == 0) {
			fd.left = new FormAttachment(0);
		} else {
			Button btnNeighbor = btnHeaders.get(i - 1);
			fd.left = new FormAttachment(btnNeighbor);
		}
		fd.width = SPLITTER_WIDTH;
		splitter.setLayoutData(fd);
		splitter.setData(Integer.valueOf(i - 1));
		splitter.addMouseListener(this);
		splitter.addMouseMoveListener(this);
	}

	// not used
	// public void addColumn( String sColumnHeading, Color clr, int iWidth )
	// {
	// columnWidths.add( Integer.valueOf( iWidth ) );
	// fHeadings.add( sColumnHeading );
	// addHeaderButton( iHeaderAlignment, sColumnHeading, iWidth );
	// cmpHeaders.layout( true );
	//
	// cnvCells.addColumn( clr );
	// cnvCells.redraw( );
	// cnvCells.updateScrollbars( );
	// }

	protected void addDragListenerToHeaderButton(Button button) {
		DragSource ds = new DragSource(button, DND.DROP_COPY);
		ds.setTransfer(new Transfer[] { SimpleTextTransfer.getInstance() });
		CustomPreviewTableDragListener dragSourceAdapter = new CustomPreviewTableDragListener(this, button.getText());
		ds.addDragListener(dragSourceAdapter);
	}

	Point getTableSize() {
		return getSize();
	}

	public void addEntry(String sText, int iIndex) throws IllegalArgumentException {
		if (fHeadings == null || iIndex >= fHeadings.size()) {
			throw new IllegalArgumentException(Messages
					.getString("CustomPreviewTable.Exception.InvalidColumnIndexNotDefined", String.valueOf(iIndex))); //$NON-NLS-1$
		}
		this.cnvCells.addEntry(sText, iIndex);
	}

	public String getColumnHeading(int iIndex) throws IllegalArgumentException {
		if (isDummy) {
			return ""; //$NON-NLS-1$
		}
		if (fHeadings == null || iIndex >= fHeadings.size()) {
			throw new IllegalArgumentException(Messages
					.getString("CustomPreviewTable.Exception.InvalidColumnIndexNotDefined", String.valueOf(iIndex))); //$NON-NLS-1$
		}
		return fHeadings.get(iIndex).getName();
	}

	/**
	 * Returns the column heading of the column that the user right-clicked on last.
	 * null if user hasn't right-clicked in the table yet. This method is for use to
	 * determine the column for which the popup-menu is to be displayed...since the
	 * menu is handled externally.
	 * 
	 * @return the last column in which the user has right-clicked.
	 */
	public String getCurrentColumnHeading() {
		return (iColumnIndex != -1) ? fHeadings.get(iColumnIndex).getName() : null;
	}

	/**
	 * Returns head object of current column, if it is sharing query, the head
	 * object should be instance of <code>ColumnBindingInfo</code>, else it is
	 * String object.
	 * 
	 * @return column head object
	 * 
	 * @since 2.3
	 */
	public Object getCurrentColumnHeadObject() {
		return (iColumnIndex != -1) ? fHeadings.get(iColumnIndex) : null;
	}

	/**
	 * Returns the index of the column that the user right-clicked on last. -1 if
	 * user hasn't right-clicked in the table yet. This method is for use to
	 * determine the column for which the popup-menu is to be displayed...since the
	 * menu is handled externally.
	 * 
	 * @return the last column in which the user has right-clicked.
	 */
	public int getCurrentColumnIndex() {
		return iColumnIndex;
	}

	public int getColumnNumber() {
		return fHeadings.size();
	}

	public void setColumnColor(int index, Color clr) {
		cnvCells.setColumnColor(index, clr);
		cnvCells.redraw();
	}

	public Color getColumnColor(int iIndex) {
		if (iIndex > cnvCells.colors.length) {
			throw new IllegalArgumentException(
					Messages.getString("CustomPreviewTable.Exception.InvalidColumnIndexSpecified")); //$NON-NLS-1$
		}
		return cnvCells.colors[iIndex];
	}

	public void clearContents() {
		btnHeaders.clear();
		columnWidths.clear();
		fHeadings.clear();
		iVScroll = 0;
		iLastProcessedHorizontalScrollPosition = 0;
		iLastProcessedVerticalScrollPosition = 0;
		iHiddenWidth = 0;
		Control[] c = this.cmpHeaders.getChildren();
		for (int i = 0; i < c.length; i++) {
			c[i].dispose();
		}
		cnvCells.clearContents();
	}

	public void setHeaderAlignment(int iAlignment) {
		this.iHeaderAlignment = iAlignment;
		for (int i = 0; i < btnHeaders.size(); i++) {
			btnHeaders.get(i).setAlignment(iHeaderAlignment);
		}
	}

	public void createDummyTable() {
		isDummy = true;

		for (int i = 0; i < 3; i++) {
			columnWidths.add(Integer.valueOf(200));
		}
		placeComponents();
		layout(true);
		// bug#245498
		WizardBase.removeException();
	}

	int getColumnWidthFor(int iIndex) {
		// TODO: TEMPORARY FIX FOR ARRAY INDEX OUT OF BOUNDS...NEED COMPONENT
		// AND COLUMN RESIZING TO BE HANDLED IN TABLE SCROLLING
		if (iIndex > columnWidths.size() - 1 || iIndex < 0) {
			return 0;
		}
		Object oTmp = null;
		oTmp = columnWidths.get(iIndex);
		return ((Integer) oTmp).intValue();
	}

	int getLeftEdgeForColumn(int iIndex) {
		int iRE = 0;
		for (int i = 0; i < iIndex; i++) {
			iRE += getColumnWidthFor(i);
		}
		return iRE;
	}

	int getRightEdgeForColumn(int iIndex) {
		int iRE = 0;
		for (int i = 0; i <= iIndex; i++) {
			iRE += getColumnWidthFor(i);
		}
		return iRE;
	}

	int getAdjustedLeftEdgeForColumn(int iIndex) {
		int iRE = getLeftEdgeForColumn(iIndex);
		return iRE - iHiddenWidth;
	}

	// public void setColumns( String[] headers )
	// {
	// clearContents( );
	// if ( headers.length == 0 )
	// {
	// createDummyTable( );
	// return;
	// }
	// int iW = cnvCells.getVisibleTableWidth( ) / headers.length;
	// if ( iW < TableCanvas.SCROLL_HORIZONTAL_STEP )
	// {
	// iW = TableCanvas.SCROLL_HORIZONTAL_STEP;
	// }
	// for ( int i = 0; i < headers.length; i++ )
	// {
	// fHeadings.add( headers[i] );
	// columnWidths.add( Integer.valueOf( iW ) );
	// }
	// placeComponents( );
	// }

	public void setColumns(ColumnBindingInfo[] headers) {
		clearContents();
		if (headers.length == 0) {
			createDummyTable();
			return;
		}
		isDummy = false;
		int iW = cnvCells.getVisibleTableWidth() / headers.length;
		if (iW < TableCanvas.SCROLL_HORIZONTAL_STEP) {
			iW = TableCanvas.SCROLL_HORIZONTAL_STEP;
		}
		for (int i = 0; i < headers.length; i++) {
			fHeadings.add(headers[i]);
			columnWidths.add(Integer.valueOf(iW));
		}
		placeComponents();
	}

	void fireMenuEvent(Widget widget, boolean doit) {
		Event e = new Event();
		e.type = MOUSE_RIGHT_CLICK_TYPE;
		e.button = 3;
		e.data = Integer.valueOf(iColumnIndex);
		e.doit = doit;

		// let the handler know which widget fires the event, then the handler
		// will bind a menu to those widgets.
		e.widget = widget;

		for (int i = 0; i < vListeners.size(); i++) {
			vListeners.get(i).handleEvent(e);
		}
	}

	void fireEvent(Event event) {
		for (int i = 0; i < vListeners.size(); i++) {
			vListeners.get(i).handleEvent(event);
		}
	}

	public void addListener(int eventType, Listener listener) {
		// ONLY ADD LISTENERS INTENDING TO LISTEN TO EVENT TYPES PROCESSED BY
		// THIS WIDGET
		if (eventType == MOUSE_RIGHT_CLICK_TYPE) {
			vListeners.add(listener);
		} else {
			super.addListener(eventType, listener);
		}
	}

	public void mouseDoubleClick(MouseEvent e) {
		if (e.widget instanceof Label) {
			// Calculates the max width of selected column and resize
			int columnIndex = ((Integer) e.widget.getData()).intValue();
			int width = cnvCells.calculateMaxColumnWidth(columnIndex, (Label) e.widget);
			columnWidths.set(columnIndex, Integer.valueOf(width + SPLITTER_WIDTH));
			((FormData) btnHeaders.get(columnIndex).getLayoutData()).width = width;
			cmpHeaders.layout();
			cnvCells.updateScrollbars();
			cnvCells.redraw();
		}
	}

	public void mouseDown(MouseEvent e) {
		if (e.button == 1) {
			if (e.widget instanceof Label) {
				bDragging = true;
				iDragStartXLocation = cnvCells.getDisplay().getCursorLocation().x;
				iResizingColumnIndex = ((Integer) (e.widget).getData()).intValue();
			}
		}
	}

	public void mouseUp(MouseEvent e) {
		if (bDragging) {
			if (iResizingColumnIndex != -1) {
				bDragging = false;
				iDragStartXLocation = -1;
				iResizingColumnIndex = -1;
			}
		}
		if (e.widget instanceof Button && e.button == 3) {
			iColumnIndex = this.btnHeaders.indexOf(e.widget);
			((Button) e.widget).setFocus();
			fireMenuEvent(e.widget, true);
		}
	}

	public void mouseMove(MouseEvent e) {
		if (bDragging || e.widget instanceof Label) {
			this.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_SIZEWE));
			if (bDragging) {
				if (iResizingColumnIndex != -1) {
					int x = cnvCells.getDisplay().getCursorLocation().x;
					int newWidth = columnWidths.get(iResizingColumnIndex) + (x - iDragStartXLocation);
					if (newWidth > 5) {
						((FormData) btnHeaders.get(iResizingColumnIndex).getLayoutData()).width += (x
								- iDragStartXLocation);
						columnWidths.set(iResizingColumnIndex, Integer.valueOf(newWidth));
						iDragStartXLocation = x;
						cmpHeaders.layout();
						cnvCells.updateScrollbars();
						cnvCells.redraw();
					}
				}
			}
		} else {
			this.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
		}
	}

	class TableCanvas extends Canvas implements PaintListener, MouseListener, SelectionListener

	{

		transient Color[] colors = null;
		private transient Vector[] cells = null;
		private transient int iSelectedRow = -1;
		private transient boolean bColumnSelection = false;
		private transient int iMaxRowIndex = 0;
		private transient boolean isFirstPaint = true;

		public static final int SCROLL_HORIZONTAL_STEP = 100;

		public TableCanvas(Composite parent, int style, int iColumns, Color[] colors, CustomPreviewTable container) {
			super(parent, style | SWT.H_SCROLL | SWT.V_SCROLL);
			addPaintListener(this);
			addMouseListener(this);
			addMouseListener(container);
			getHorizontalBar().addSelectionListener(this);
			getVerticalBar().addSelectionListener(this);

			cells = new Vector[iColumns];
			this.colors = new Color[iColumns];
			for (int i = 0; i < iColumns; i++) {
				cells[i] = new Vector();
				try {
					this.colors[i] = colors[i];
				} catch (ArrayIndexOutOfBoundsException e) {
					this.colors[i] = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
				}
			}
			updateScrollbars();
		}

		public void updateScrollbars() {
			// Add a step to show the whole table data
			int iMaxTableWidth = getMaxTableWidth() + SCROLL_HORIZONTAL_STEP;
			int iVisibleTableWidth = getVisibleTableWidth();

			int iVisibleRows = 0;

			if (this.isVisible()) {
				// UPDATE HORIZONTAL SCROLLBAR
				getHorizontalBar().setValues(iVisibleTableWidth + iHiddenWidth, iVisibleTableWidth, iMaxTableWidth,
						SCROLL_HORIZONTAL_STEP, SCROLL_HORIZONTAL_STEP, SCROLL_HORIZONTAL_STEP);

				// UPDATE VERTICAL SCROLLBAR
				int iHeight = getTableSize().y;
				if (iHeight == 0) {
					iHeight = getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
				}
				// ADJUST HEIGHT FOR PRESENCE OF HORIZONTAL SCROLLBAR
				iHeight -= super.getHorizontalBar().getSize().y;
				iVisibleRows = (int) Math.floor(iHeight / (double) ROW_HEIGHT);

				getVerticalBar().setValues(iVisibleRows + iVScroll, iVisibleRows, iMaxRowIndex + 2, 1, 1, 1);
			} else {
				getHorizontalBar().setValues(iVisibleTableWidth, iVisibleTableWidth, iMaxTableWidth,
						SCROLL_HORIZONTAL_STEP, SCROLL_HORIZONTAL_STEP, SCROLL_HORIZONTAL_STEP);
				getVerticalBar().setValues(iMaxRowIndex, iMaxRowIndex, iMaxRowIndex, 1, 1, 1);
			}
			iLastProcessedHorizontalScrollPosition = iVisibleTableWidth + iHiddenWidth;
			iLastProcessedVerticalScrollPosition = iVisibleRows + iVScroll;
		}

		private int getMaxTableWidth() {
			int max = 0;
			for (int i = 0; i < columnWidths.size(); i++) {
				max += columnWidths.get(i).intValue();
			}
			return max;
		}

		private int getVisibleTableWidth() {
			int iVisibleTableWidth = getTableSize().x;
			if (iVisibleTableWidth == 0) {
				iVisibleTableWidth = getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			}
			// Subtract additional width to display the vertical scrollbar
			return iVisibleTableWidth - 20;
		}

		/**
		 * Calculates the max width of specified column data.
		 * 
		 * @param columnIndex
		 * @param control
		 */
		private int calculateMaxColumnWidth(int columnIndex, Control control) {
			int maxWidth = 10;
			GC gc = new GC(control);
			Vector columnTexts = cells[columnIndex];
			for (int rowIndex = 0; rowIndex < columnTexts.size(); rowIndex++) {
				Object obj = columnTexts.get(rowIndex);
				if (obj != null && obj instanceof String) {
					maxWidth = Math.max(gc.textExtent((String) obj).x, maxWidth);
				}
			}
			gc.dispose();
			return maxWidth;
		}

		public void addColumn(Color clr) {
			Vector[] newCells = new Vector[cells.length + 1];
			Color[] newColors = new Color[colors.length + 1];
			for (int i = 0; i < cells.length; i++) {
				newCells[i] = cells[i];
				newColors[i] = colors[i];
			}
			newCells[cells.length] = new Vector();
			newColors[colors.length] = (clr != null) ? clr
					: Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
			cells = newCells;
			colors = newColors;
			updateScrollbars();
			super.layout();
		}

		public void addEntry(String sText, int iColumn) throws IllegalArgumentException {
			if (iColumn > cells.length) {
				throw new IllegalArgumentException(
						Messages.getString("CustomPreviewTable.Exception.InvalidColumnIndexSpecifiedOnly", //$NON-NLS-1$
								new Object[] { String.valueOf(iColumn), String.valueOf(cells.length) }));
			}
			if (sText != null && sText.indexOf('\n') > -1) {
				sText = sText.replaceAll("\n", " "); //$NON-NLS-1$ //$NON-NLS-2$
			}

			cells[iColumn].add(sText);
			if (cells[iColumn].size() > iMaxRowIndex) {
				iMaxRowIndex = cells[iColumn].size();
				updateScrollbars();
			}
		}

		public void setText(String sText, int iColumn, int iRow) throws IllegalArgumentException {
			if (iColumn > cells.length) {
				throw new IllegalArgumentException(
						Messages.getString("CustomPreviewTable.Exception.InvalidColumnIndexSpecifiedOnly", //$NON-NLS-1$
								new Object[] { String.valueOf(iColumn), String.valueOf(cells.length) }));
			}
			if (iRow < 0) {
				throw new IllegalArgumentException(
						Messages.getString("CustomPreviewTable.Exception.RowIndexGreaterThan0")); //$NON-NLS-1$
			}
			cells[iColumn].ensureCapacity(iRow);
			cells[iColumn].setElementAt(sText, iRow);
			if (cells[iColumn].size() > iMaxRowIndex) {
				iMaxRowIndex = cells[iColumn].size();
				updateScrollbars();
			}
		}

		public void selectColumn(int iIndex) {
			iColumnIndex = iIndex;
			bColumnSelection = true;
			redraw();
		}

		public void clearContents() {
			for (int i = 0; i < cells.length; i++) {
				cells[i].clear();
			}
			cells = new Vector[0];
			colors = new Color[0];
			iSelectedRow = -1;
			bColumnSelection = false;
			super.getHorizontalBar().setValues(1, 1, 1, 1, 1, 1);
			super.getVerticalBar().setValues(1, 1, 1, 1, 1, 1);
		}

		public void setColumnColor(int iColumnIndex, Color color) {
			if (iColumnIndex < colors.length) {
				colors[iColumnIndex] = color;
			}
		}

		public boolean isColumnSelected(int iIndex) {
			return iColumnIndex == iIndex;
		}

		public void paintControl(PaintEvent pe) {
			if (isFirstPaint) {
				isFirstPaint = false;
				updateScrollbars();
			}
			GC gc = pe.gc;
			Color cSelectionBack = null;
			Color cSelectionFore = null;
			// DEFAULT BACKGROUND COLOR FOR COLUMN
			Color cDefaultBack = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
			// DEFAULT TEXT COLOR FOR COLUMN
			Color cDefaultFore = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
			Color cText = Display.getCurrent().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
			Color cGrid = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			// Draw Column Borders
			// Because the splitter ends up -1, the button header starts up 0.
			// Use the blank area to draw the line
			int iXStart = -1;
			int iXEnd = -1;
			for (int iC = 0; iC < cells.length; iC++) {
				final int columnWidthOffset = CustomPreviewTable.this.iHiddenWidth;

				// CALCULATE COLUMN BOUNDARIES
				if (iC > 0) {
					iXStart += getColumnWidthFor(iC - 1);
				}
				iXEnd += getColumnWidthFor(iC);

				// OPTIMIZATION...DO NOT ATTEMPT TO RENDER A CELL FULLY OUTSIDE
				// THE VIEWABLE AREA!
				if ((iXEnd - columnWidthOffset) < 0 || (iXStart - columnWidthOffset) > this.getSize().x) {
					continue;
				}

				// TODO: OPTIMIZATION...DO NOT REPAINT COLUMNS IF RESIZING OF
				// COLUMNS AFTER THIS ONE IS CURRENTLY TAKING PLACE
				// if(bDragging && iResizingColumnIndex >= iC)
				// {
				// continue;
				// }

				// RENDER COLUMN
				Color cBack = getColorForColumn(iC);
				if (cBack == null) {
					cBack = cDefaultBack;
				}
				gc.setBackground(cBack);
				// FILL COLUMN AREA WITH BACKGROUND COLOR
				gc.fillRectangle(iXStart - columnWidthOffset, 0, getColumnWidthFor(iC), this.getSize().y);
				// RENDER VERTICAL LINES
				gc.setForeground(cGrid);
				gc.drawLine(iXStart - columnWidthOffset, 0, iXStart - columnWidthOffset, this.getSize().y);

				// RENDER CELLS
				int iYStart = 0;
				int iYEnd = 0;
				for (int iR = 0; iR <= iMaxRowIndex || iYEnd < getSize().y; iR++) {
					// CALCULATE ROW BOUNDARIES
					iYStart = iR * ROW_HEIGHT - (iVScroll * ROW_HEIGHT);
					iYEnd = iYStart + ROW_HEIGHT;

					// DRAW CELL
					gc.setForeground(cGrid);
					gc.drawLine(iXStart - columnWidthOffset, iYStart,
							iXStart - columnWidthOffset + getColumnWidthFor(iC), iYStart);
					gc.setForeground(cDefaultFore);
					// BACKUP CURRENT CLIPPING AREA AND SET NEW CLIPPING AREA
					// BASED ON CELL SIZE...THIS ENSURES THAT TEXT DOES NOT
					// OVERFLOW INTO NEIGHBORING CELLS
					Rectangle r = gc.getClipping();
					gc.setClipping(iXStart - columnWidthOffset, iYStart, getColumnWidthFor(iC), (iYEnd - iYStart));
					// SET COLORS BASED ON SELECTION STATE
					if (iC == iColumnIndex && (bColumnSelection || iR == this.iSelectedRow)) {
						cSelectionBack = gc.getBackground();
						cSelectionFore = gc.getForeground();
						gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION));
						gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
						gc.fillRectangle(iXStart - columnWidthOffset, iYStart, getColumnWidthFor(iC),
								(iYEnd - iYStart));
						gc.drawRectangle(iXStart - columnWidthOffset, iYStart, getColumnWidthFor(iC),
								(iYEnd - iYStart));
					} else {
						gc.setForeground(cText);
					}

					// RENDER TEXT
					String sContent = (cells[iC].size() > 0 && cells[iC].size() > iR)
							? (cells[iC].elementAt(iR) != null ? cells[iC].elementAt(iR).toString() : "") //$NON-NLS-1$
							: ""; //$NON-NLS-1$
					gc.drawText(sContent, iXStart - columnWidthOffset + 3, iYStart + 3);
					if (iC == iColumnIndex) {
						if (bColumnSelection || iR == this.iSelectedRow) {
							gc.setBackground(cSelectionBack);
							gc.setForeground(cSelectionFore);
						}
					}
					// RESET CLIPPING AREA
					gc.setClipping(r);
				}
				// IF THIS IS THE LAST COLUMN, DRAW A CLOSING LINE AS WELL
				if (iC == (cells.length - 1)) {
					// RENDER VERTICAL LINES
					gc.setForeground(cGrid);
					gc.drawLine(iXStart - columnWidthOffset + getColumnWidthFor(iC), 0,
							iXStart - columnWidthOffset + getColumnWidthFor(iC), this.getSize().y);
				}
			}
		}

		private Color getColorForColumn(int iIndex) {
			if (colors != null) {
				return colors[iIndex];
			}
			return null;
		}

		public void mouseDoubleClick(MouseEvent e) {
		}

		public void mouseDown(MouseEvent e) {
		}

		public void mouseUp(MouseEvent e) {
			// DO NOT PROCESS IF DRAGGING IN PROGRESS!
			if (bDragging) {
				updateScrollbars();
				return;
			}
			this.bColumnSelection = false;
			if (e.button != 3) {
				// TODO: These values will change based on scrolling or
				// resizing!
				this.iSelectedRow = e.y / ROW_HEIGHT + iVScroll;
				for (int i = 0, iTmp = 0; i < columnWidths.size(); i++) {
					iTmp = getAdjustedLeftEdgeForColumn(i);
					if (iTmp > e.x) {
						iColumnIndex = i - 1;
						break;
					}
					iColumnIndex = i;
				}

				if (iColumnIndex >= 0) {
					btnHeaders.elementAt(iColumnIndex).setFocus();
				}
				redraw();
			} else {
				iColumnIndex = -1;
				for (int i = 0, iTmp = 0; i < columnWidths.size(); i++) {
					iTmp = getAdjustedLeftEdgeForColumn(i);
					if (iTmp > e.x) {
						iColumnIndex = i - 1;
						break;
					}
					iColumnIndex = i;
				}
				Button currentButton = btnHeaders.elementAt(iColumnIndex);
				currentButton.setFocus();
				fireMenuEvent(currentButton, true);
			}
		}

		public void moveTo(int index) {
			int offset = 0;
			for (int i = 0; i <= index; i++) {
				offset += columnWidths.get(i).intValue();
			}
			if (offset > this.getVisibleTableWidth()) {
				offset -= this.getVisibleTableWidth();
				iHiddenWidth = offset;
				// APPLY PIXEL SHIFT TO RELOCATE BUTTONS
				if (btnHeaders.size() > 0) {
					// MOVE ALL BUTTONS TO THE LEFT...AS MUCH AS THE
					// SCROLLING VALUE
					Button btn = btnHeaders.get(0);
					((FormData) btn.getLayoutData()).left = new FormAttachment(0, -offset);
				}
			}

			getHorizontalBar().setSelection(offset);
			cmpHeaders.layout();

			selectColumn(index);
			iLastProcessedHorizontalScrollPosition = getHorizontalBar().getSelection();
			redraw();
		}

		public void widgetSelected(SelectionEvent e) {
			if (e.getSource() instanceof ScrollBar) {
				ScrollBar sb = (ScrollBar) e.getSource();
				if ((sb.getStyle() | SWT.H_SCROLL) == SWT.H_SCROLL) {
					if (iLastProcessedHorizontalScrollPosition == 0) {
						// Initialize the value
						iLastProcessedHorizontalScrollPosition = cnvCells.getVisibleTableWidth();
					}
					if (sb.getSelection() == iLastProcessedHorizontalScrollPosition) {
						return;
					}

					if ((sb.getStyle() | SWT.H_SCROLL) == SWT.H_SCROLL) {
						// CALCULATE PIXEL SHIFT IN BUTTON POSITION
						int iShift = 0;
						if (e.detail == SWT.ARROW_UP || e.detail == SWT.PAGE_UP) {
							// SHIFT HEADERS ONE COLUMN TO THE RIGHT
							iShift = -SCROLL_HORIZONTAL_STEP;
						} else if (e.detail == SWT.ARROW_DOWN || e.detail == SWT.PAGE_DOWN) {
							// SHIFT HEADERS ONE COLUMN TO THE LEFT
							iShift = SCROLL_HORIZONTAL_STEP;
						} else {
							iShift = sb.getSelection() - iLastProcessedHorizontalScrollPosition;
						}
						iHiddenWidth += iShift;
						// Correction for negative value or overflow
						if (iHiddenWidth < 0) {
							iShift -= iHiddenWidth;
							iHiddenWidth = 0;
						} else if (iHiddenWidth + getVisibleTableWidth() > getMaxTableWidth()) {
							int diff = getMaxTableWidth() - getVisibleTableWidth() - iHiddenWidth;
							iShift += diff;
							iHiddenWidth += diff;
						}
						// APPLY PIXEL SHIFT TO RELOCATE BUTTONS
						if (btnHeaders.size() > 0) {
							// MOVE ALL BUTTONS TO THE LEFT...AS MUCH AS THE
							// SCROLLING VALUE
							Button btn = btnHeaders.get(0);
							((FormData) btn.getLayoutData()).left = new FormAttachment(0, btn.getLocation().x - iShift);
						}
						cmpHeaders.layout();
						// UPDATE LAST HORIZONTAL SCROLL POSITION
						iLastProcessedHorizontalScrollPosition = sb.getSelection();
					}
				} else {
					if (getVerticalBar().getSelection() == iLastProcessedVerticalScrollPosition) {
						return;
					}

					if (e.detail == SWT.ARROW_UP || e.detail == SWT.PAGE_UP) {
						if (iVScroll > 0) {
							iVScroll--;
						}
					} else if (e.detail == SWT.ARROW_DOWN || e.detail == SWT.PAGE_DOWN) {
						iVScroll++;
					} else {
						iVScroll += (sb.getSelection() - iLastProcessedVerticalScrollPosition);
					}
					iLastProcessedVerticalScrollPosition = sb.getSelection();
				}
				redraw();
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	public void controlMoved(ControlEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	public void controlResized(ControlEvent e) {
		cnvCells.updateScrollbars();
		layout(true);
		cnvCells.redraw();
	}

	public void widgetDisposed(DisposeEvent e) {
		// Remove outside listeners after being disposed
		getShell().removeControlListener(this);
	}

	public void keyPressed(KeyEvent event) {
		if ((event.stateMask == SWT.CTRL) && (event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.ARROW_RIGHT)) {
			scrollTable(cnvCells.getHorizontalBar(), event);
		} else if (event.keyCode == SWT.PAGE_UP || event.keyCode == SWT.PAGE_DOWN || ((event.stateMask == SWT.CTRL)
				&& (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN))) {
			scrollTable(cnvCells.getVerticalBar(), event);
		}

	}

	public void keyReleased(KeyEvent event) {
		// do nothing
	}

	private void scrollTable(ScrollBar widget, KeyEvent event) {
		int newSelectionValue = widget.getSelection();
		if (event.keyCode == SWT.ARROW_LEFT) {
			newSelectionValue -= TableCanvas.SCROLL_HORIZONTAL_STEP;
		} else if (event.keyCode == SWT.ARROW_RIGHT) {
			newSelectionValue += TableCanvas.SCROLL_HORIZONTAL_STEP;
		} else if (event.keyCode == SWT.PAGE_UP || event.keyCode == SWT.ARROW_UP) {
			newSelectionValue -= 1;
		} else if (event.keyCode == SWT.PAGE_DOWN || event.keyCode == SWT.ARROW_DOWN) {
			newSelectionValue += 1;
		}

		if (newSelectionValue < widget.getMinimum()) {
			newSelectionValue = widget.getMinimum();
		} else if (newSelectionValue > widget.getMaximum()) {
			newSelectionValue = widget.getMaximum();
		}

		widget.setSelection(newSelectionValue);
		Event newEvent = new Event();
		newEvent.widget = widget;
		newEvent.type = SWT.Selection;
		newEvent.data = event.data;
		widget.notifyListeners(SWT.Selection, newEvent);
	}

	/**
	 * Move the selected column to specified index.
	 * 
	 * @param index
	 */
	public void moveTo(int index) {
		cnvCells.moveTo(index);
	}
}
