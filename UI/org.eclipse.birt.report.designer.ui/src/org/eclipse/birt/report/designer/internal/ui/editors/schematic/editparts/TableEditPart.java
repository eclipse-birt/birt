/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.DeleteColumnCommand;
import org.eclipse.birt.report.designer.core.commands.DeleteRowCommand;
import org.eclipse.birt.report.designer.core.model.ITableAdapterHelper;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.IBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.TableElementBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportFigureUtilities;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.TableResizeEditPolice;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.TableXYLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TableFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer.TableGridLayer;
import org.eclipse.birt.report.designer.internal.ui.layout.FixTableLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.GuideLayer;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * Table EditPart,control the UI & model of table
 * </p>
 * 
 */
public class TableEditPart extends AbstractTableEditPart implements ITableAdapterHelper {

	private static final String RESIZE_COLUMN_TRANS_LABEL = Messages.getString("TableEditPart.Label.ResizeColumn"); //$NON-NLS-1$

	private static final String MERGE_TRANS_LABEL = Messages.getString("TableEditPart.Label.Merge"); //$NON-NLS-1$

	public static final String GUIDEHANDLE_TEXT = Messages.getString("TableEditPart.GUIDEHANDLE_TEXT"); //$NON-NLS-1$

	private Rectangle selectRowAndColumnRect = null;

	private int oriColumnNumber = 1;
	private int oriRowNumner = 1;

	/**
	 * Constructor
	 * 
	 * @param obj
	 */
	public TableEditPart(Object obj) {
		super(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#createGuideHandle()
	 */
	protected AbstractGuideHandle createGuideHandle() {
		TableGuideHandle handle = new TableGuideHandle(this);
		handle.setIndicatorLabel(getGuideLabel());

		INodeProvider provider = ProviderFactory.createProvider(getModel());

		handle.setIndicatorIcon(provider.getNodeIcon(getModel()));
		handle.setToolTip(ReportFigureUtilities.createToolTipFigure(provider.getNodeTooltip(getModel()),
				DesignChoiceConstants.BIDI_DIRECTION_LTR, DesignChoiceConstants.TEXT_ALIGN_LEFT));

		return handle;
	}

	public String getGuideLabel() {
		return GUIDEHANDLE_TEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportComponentEditPolicy() {

			public boolean understandsRequest(Request request) {
				if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())
						|| RequestConstants.REQ_OPEN.equals(request.getType())
						|| ReportRequest.CREATE_ELEMENT.equals(request.getType()))
					return true;
				return super.understandsRequest(request);
			}
		});
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ReportContainerEditPolicy());
		// should add highlight policy
		// installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new
		// ContainerHighlightEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new TableXYLayoutEditPolicy((XYLayout) getContentPane().getLayoutManager()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getTableAdapter().getChildren();
	}

	/**
	 * 
	 */
	protected void contentChange(Map info) {
		super.contentChange(info);
		Object action = info.get(GraphicsViewModelEventProcessor.CONTENT_EVENTTYPE);
		if (action instanceof Integer) {
			if (((Integer) action).intValue() == ContentEvent.REMOVE) {
				reselectTable();
			}
		}
	}

	private void reselectTable() {
		if (isDelete()) {
			return;
		}
		ReportRequest request = new ReportRequest(this);
		List list = new ArrayList();
		list.add(this);
		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		request.setRequestConverter(new DeferredGraphicalViewer.EditorReportRequestConvert());
		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		if (RequestConstants.REQ_OPEN.equals(request.getType())) {
			Object obj = request.getExtendedData().get(DesignerConstants.TABLE_ROW_NUMBER);
			if (obj != null) {
				int rowNum = ((Integer) obj).intValue();
				RowHandle row = (RowHandle) getRow(rowNum);
				if (row.getContainer() instanceof TableGroupHandle) {
					IAction action = new EditGroupAction(null, (TableGroupHandle) row.getContainer());
					if (action.isEnabled()) {
						action.run();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .AbstractReportEditPart#refreshFigure()
	 */
	public void refreshFigure() {
		checkHelper();
		refreshBorder(getTableAdapter().getHandle(), (BaseBorder) getFigure().getBorder());

		((SectionBorder) (getFigure().getBorder()))
				.setPaddingInsets(getTableAdapter().getPadding(getFigure().getInsets()));

		refreshBackground((DesignElementHandle) getModel());

		refreshMargin();

		for (Iterator itr = getChildren().iterator(); itr.hasNext();) {
			TableCellEditPart fg = (TableCellEditPart) itr.next();
			if (!fg.isDelete()) {
				fg.updateBlankString();
			}
		}
		layoutManagerLayout();
	}

	protected void checkHelper() {
		if (HandleAdapterFactory.getInstance().getTableHandleAdapter(getModel(), this).getModelAdaptHelper() == null) {
			peer = creatDesignElementHandleAdapter();
		}
		getTableAdapter().reload();
	}

	/**
	 * Gets the top, left, right, bottom of edit part.
	 * 
	 * @param parts
	 * @return cell edit parts.
	 */
	public TableCellEditPart[] getMinAndMaxNumber(TableCellEditPart[] parts) {
		if (parts == null || parts.length == 0) {
			return null;
		}
		int size = parts.length;
		TableCellEditPart leftTopPart = parts[0];
		TableCellEditPart leftBottomPart = parts[0];

		TableCellEditPart rightBottomPart = parts[0];
		TableCellEditPart rightTopPart = parts[0];
		for (int i = 1; i < size; i++) {
			TableCellEditPart part = parts[i];
			if (part == null) {
				continue;
			}

			if (part.getRowNumber() <= leftTopPart.getRowNumber()
					&& part.getColumnNumber() <= leftTopPart.getColumnNumber()) {
				leftTopPart = part;
			}

			if (part.getRowNumber() <= rightTopPart.getRowNumber()
					&& part.getColumnNumber() + part.getColSpan() - 1 >= leftTopPart.getColumnNumber()) {
				rightTopPart = part;
			}

			if (part.getColumnNumber() <= leftBottomPart.getColumnNumber()
					&& part.getRowNumber() + part.getRowSpan() - 1 >= leftBottomPart.getRowNumber()) {
				leftBottomPart = part;
			}

			if (part.getRowNumber() + part.getRowSpan() - 1 >= rightBottomPart.getRowNumber()
					&& part.getColumnNumber() + part.getColSpan() - 1 >= rightBottomPart.getColumnNumber()) {
				rightBottomPart = part;
			}
		}
		return new TableCellEditPart[] { leftTopPart, rightTopPart, leftBottomPart, rightBottomPart };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		TableFigure viewport = new TableFigure();
		viewport.setOpaque(false);

		innerLayers = new FreeformLayeredPane();
		createLayers(innerLayers);
		viewport.setContents(innerLayers);
		return viewport;
	}

	/**
	 * Creates the top-most set of layers on the given layered pane.
	 * 
	 * @param layeredPane the parent for the created layers
	 */
	protected void createLayers(LayeredPane layeredPane) {
		layeredPane.add(createGridLayer(), GRID_LAYER);
		layeredPane.add(getPrintableLayers(), PRINTABLE_LAYERS);
		layeredPane.add(new FreeformLayer(), HANDLE_LAYER);
		layeredPane.add(new GuideLayer(), GUIDE_LAYER);
	}

	/**
	 * Creates a {@link GridLayer grid}. Sub-classes can override this method to
	 * customize the appearance of the grid. The grid layer should be the first
	 * layer (i.e., beneath the primary layer) if it is not to cover up parts on the
	 * primary layer. In that case, the primary layer should be transparent so that
	 * the grid is visible.
	 * 
	 * @return the newly created GridLayer
	 */
	protected GridLayer createGridLayer() {
		GridLayer grid = new TableGridLayer(this);
		grid.setOpaque(false);
		return grid;
	}

	/**
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeColumn(int start, int end, int value) {
		resizeColumn(start, end, value, false);
	}

	/**
	 * Resets size of column.
	 * 
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeColumn(int start, int end, int value, boolean isResetEnd) {
		Object startColumn = getColumn(start);
		ColumnHandleAdapter startAdapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(startColumn);

		Object endColumn = getColumn(end);
		ColumnHandleAdapter endAdapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(endColumn);
		int startWidth = 0;
		int endWidth = 0;

		startWidth = TableUtil.caleVisualWidth(this, startColumn);
		endWidth = TableUtil.caleVisualWidth(this, endColumn);

		try {
			getTableAdapter().transStar(RESIZE_COLUMN_TRANS_LABEL);
			startAdapt.setWidth(startWidth + value);
			if (isResetEnd) {
				endAdapt.setWidth(endWidth - value);
			}
			getTableAdapter().transEnd();
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Selects the columns
	 * 
	 * @param numbers
	 */
	public void selectColumn(int[] numbers) {
		selectColumn(numbers, true);
	}

	/**
	 * Selects the columns
	 * 
	 * @param numbers
	 */
	public void selectColumn(int[] numbers, boolean notofyToMedia) {
		if (numbers == null || numbers.length == 0) {
			return;
		}
		ArrayList list = new ArrayList();
		int size = numbers.length;
		int width = 0;

		int minColumnnumber = numbers[0];
		for (int i = 0; i < size; i++) {
			if (minColumnnumber > numbers[i]) {
				minColumnnumber = numbers[i];
			}
			width = width + TableUtil.caleVisualWidth(this, getColumn(numbers[i]));
			list.add(new DummyColumnEditPart(getColumn(numbers[i])));
		}
		for (int i = 0; i < size; i++) {
			int rowNumber = getTableAdapter().getRowCount();
			for (int j = 0; j < rowNumber; j++) {
				AbstractCellEditPart part = getCell(j + 1, numbers[i]);
				if (part != null) {
					list.add(part);
				}
			}
		}

		int x = TableUtil.caleX(this, minColumnnumber);

		Rectangle rect = new Rectangle(x, 0, width, TableUtil.getTableContentsHeight(this));

		setSelectRowAndColumnRect(rect);
		if (notofyToMedia) {
			getViewer().setSelection(new StructuredSelection(list));
		} else {
			if (getViewer() instanceof DeferredGraphicalViewer)
				((DeferredGraphicalViewer) getViewer()).setSelection(new StructuredSelection(list), notofyToMedia);
		}

		setSelectRowAndColumnRect(null);
	}

	/**
	 * Resize the row.
	 * 
	 * @param start
	 * @param end
	 * @param value
	 */
	public void resizeRow(int start, int end, int value) {
		Object row = getRow(start);
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(row);
		int rowHeight = 0;
		if (adapt.isCustomHeight()) {
			rowHeight = adapt.getHeight();
		} else {
			rowHeight = TableUtil.caleVisualHeight(this, row);
		}
		try {
			adapt.setHeight(rowHeight + value);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	public void selectRow(int[] numbers) {
		selectRow(numbers, true);
	}

	/**
	 * Selects rows
	 * 
	 * @param numbers
	 */
	public void selectRow(int[] numbers, boolean notofyToMedia) {
		if (numbers == null || numbers.length == 0) {
			return;
		}
		ArrayList list = new ArrayList();
		int size = numbers.length;
		int height = 0;
		int minRownumber = numbers[0];

		// add row object in the list first
		for (int i = 0; i < size; i++) {

			if (minRownumber > numbers[i]) {
				minRownumber = numbers[i];
			}
			height = height + TableUtil.caleVisualHeight(this, getRow(numbers[i]));
			list.add(new DummyRowEditPart(getRow(numbers[i])));
		}

		for (int i = 0; i < size; i++) {
			int columnNumber = getTableAdapter().getColumnCount();
			for (int j = 0; j < columnNumber; j++) {
				AbstractCellEditPart part = getCell(numbers[i], j + 1);

				if (part != null) {
					list.add(part);
				}
			}
		}

		int y = TableUtil.caleY(this, minRownumber);

		Rectangle rect = new Rectangle(0, y, TableUtil.getTableContentsWidth(this), height);

		setSelectRowAndColumnRect(rect);
		if (notofyToMedia) {
			getViewer().setSelection(new StructuredSelection(list));
		} else {
			if (getViewer() instanceof DeferredGraphicalViewer)
				((DeferredGraphicalViewer) getViewer()).setSelection(new StructuredSelection(list), notofyToMedia);
		}
		setSelectRowAndColumnRect(null);
	}

	/**
	 * Get mini height of row.
	 * 
	 * @param rowNumber
	 * @return the minimum height of row.
	 */
	public int getMinHeight(int rowNumber) {
		if (isFixLayout()) {
			return TableUtil.getMinHeight(this, rowNumber);
		} else {
			return Math.max(TableUtil.getMinHeight(this, rowNumber), getTableAdapter().getMinHeight(rowNumber));
		}
	}

	/**
	 * Get mini width of column.
	 * 
	 * @param columnNumber
	 * @return the minimum height of column.
	 */
	public int getMinWidth(int columnNumber) {
		if (isFixLayout()) {
			return TableUtil.getMinWidth(this, columnNumber);
		} else {
			return Math.max(TableUtil.getMinWidth(this, columnNumber), getTableAdapter().getMinWidth(columnNumber));
		}
	}

	/**
	 * @return the table adapter
	 */
	public TableHandleAdapter getTableAdapter() {
		return (TableHandleAdapter) getModelAdapter();
	}

	/**
	 * Get all rows list
	 * 
	 * @return all rows list.
	 */
	public List getRows() {
		return getTableAdapter().getRows();
	}

	/**
	 * @param number a row position
	 * @return a specific row.
	 */
	public Object getRow(int number) {
		return getTableAdapter().getRow(number);
	}

	/**
	 * @param number a column position
	 * @return a specific column.
	 */
	public Object getColumn(int number) {
		return getTableAdapter().getColumn(number);
	}

	/**
	 * Gets all columns list
	 * 
	 * @return all columns list.
	 */
	public List getColumns() {
		return getTableAdapter().getColumns();
	}

	/**
	 * Gets the rows count
	 * 
	 * @return row count
	 */
	public int getRowCount() {
		return getTableAdapter().getRowCount();
	}

	/**
	 * Gets the columns count
	 * 
	 * @return column count
	 */
	public int getColumnCount() {
		return getTableAdapter().getColumnCount();
	}

	/**
	 * @return select bounds
	 */
	public Rectangle getSelectBounds() {
		if (getSelectRowAndColumnRect() != null) {
			return getSelectRowAndColumnRect();
		}
		List list = TableUtil.getSelectionCells(this);
		int size = list.size();
		TableCellEditPart[] parts = new TableCellEditPart[size];
		list.toArray(parts);

		TableCellEditPart[] caleNumber = getMinAndMaxNumber(parts);
		TableCellEditPart minRow = caleNumber[0];
		TableCellEditPart maxColumn = caleNumber[3];

		Rectangle min = minRow.getBounds().getCopy();
		Rectangle max = maxColumn.getBounds().getCopy();

		return min.union(max);
	}

	/**
	 * @return selected row and column area
	 */
	public Rectangle getSelectRowAndColumnRect() {
		return selectRowAndColumnRect;
	}

	/**
	 * Set selected row and column area.
	 * 
	 * @param selectRowAndColumnRect
	 */
	public void setSelectRowAndColumnRect(Rectangle selectRowAndColumnRect) {
		this.selectRowAndColumnRect = selectRowAndColumnRect;
	}

	/**
	 * Gets data set, which is biding on table.
	 * 
	 */
	public Object getDataSet() {
		return getTableAdapter().getDataSet();
	}

	/**
	 * Get the cell on give position.
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 */
	public AbstractCellEditPart getCell(int rowNumber, int columnNumber) {
		Object cell = getTableAdapter().getCell(rowNumber, columnNumber);
		return (TableCellEditPart) getViewer().getEditPartRegistry().get(cell);
	}

	/**
	 * Delete specified row.
	 * 
	 * @param numbers
	 */
	public void deleteRow(int[] numbers) {
		try {
			getTableAdapter().deleteRow(numbers);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Delete specified column
	 * 
	 * @param numbers
	 */
	public void deleteColumn(int[] numbers) {
		try {
			getTableAdapter().deleteColumn(numbers);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * inserts a row after the row number
	 * 
	 * @param rowNumber
	 */
	public void insertRow(int rowNumber) {
		insertRow(-1, rowNumber);
	}

	/**
	 * Inserts a single row at give position.
	 * 
	 * @param relativePos     The relative position to insert the new row.
	 * @param originRowNumber The row number of the original row.
	 */
	public void insertRow(final int relativePos, final int originRowNumber) {
		final RowHandleAdapter adapter = HandleAdapterFactory.getInstance()
				.getRowHandleAdapter(getRow(originRowNumber));
		try {
			getTableAdapter().insertRow(relativePos, originRowNumber);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				// reLayout();
				selectRow(new int[] { adapter.getRowNumber() });
			}
		});
	}

	/**
	 * Inserts multi rows( or a single row ) at give position.
	 * 
	 * @author Liu sanyong
	 * 
	 * @version 1.0 2005.4.22
	 * 
	 * @param relativePos The direction to indicate inserting rows above or below.
	 * @param rowNumbers  The row numbers of the origin selected rows.
	 */
	public void insertRows(final int relativePos, final int[] rowNumbers) {
		int rowCount = rowNumbers.length;
		try {
			if (relativePos < 0) { // insert above.
				getTableAdapter().insertRows(-rowCount, rowNumbers[0]);
			} else {// insert below.
				getTableAdapter().insertRows(rowCount, rowNumbers[rowCount - 1]);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		// no need to relayout.
	}

	/**
	 * Inserts a row after the row number
	 * 
	 * @param columnNumber
	 */
	public void insertColumn(int columnNumber) {
		insertColumn(-1, columnNumber);
	}

	/**
	 * Inserts a single column at give position.
	 * 
	 * @param relativePos     The relative position to insert the new column.
	 * @param originColNumber The column number of the original column.
	 */
	public void insertColumn(final int relativePos, final int originColNumber) {
		final ColumnHandleAdapter adapter = HandleAdapterFactory.getInstance()
				.getColumnHandleAdapter(getColumn(originColNumber));
		try {
			getTableAdapter().insertColumn(relativePos, originColNumber);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				// reLayout();
				selectColumn(new int[] { adapter.getColumnNumber() });
			}
		});
	}

	/**
	 * Inserts multi columns( or a single column ) at give position.
	 * 
	 * @author Liu sanyong
	 * 
	 * @version 1.0 2005.4.22
	 * 
	 * @param relativePos The direction to indicate inserting rows above or below.
	 * @param colNumbers  The column numbers of the origin selected column(s).
	 */
	// TODO move the logic to tableHandle adapt
	public void insertColumns(final int relativePos, final int[] colNumbers) {
		int colCount = colNumbers.length;
		try {
			if (relativePos < 0) { // insert left.
				getTableAdapter().insertColumns(-colCount, colNumbers[0]);

			} else {// insert right.
				getTableAdapter().insertColumns(colCount, colNumbers[colCount - 1]);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		// no need to relayout.
	}

	/**
	 * merge the selection cell
	 */
	public void merge() {
		List selections = TableUtil.getSelectionCells(this);
		if (selections.size() == 1) {
			return;
		}
		int size = selections.size();
		TableCellEditPart[] parts = new TableCellEditPart[size];
		selections.toArray(parts);

		TableCellEditPart[] caleNumber = getMinAndMaxNumber(parts);
		TableCellEditPart minRow = caleNumber[0];

		TableCellEditPart maxRow = caleNumber[2];
		TableCellEditPart maxColumn = caleNumber[3];

		TableCellEditPart cellPart = caleNumber[0];
		ArrayList list = new ArrayList();
		// first is the contain cell(minrow, minColumn)
		for (int i = 0; i < size; i++) {
			if (selections.get(i) != cellPart) {
				list.add(selections.get(i));
			}
		}

		int rowSpan = maxRow.getRowNumber() - minRow.getRowNumber() + maxRow.getRowSpan();
		int colSpan = maxColumn.getColumnNumber() - maxRow.getColumnNumber() + maxColumn.getColSpan();

		getTableAdapter().transStar(MERGE_TRANS_LABEL);
		try {
			MergeContent(cellPart, list);
		} catch (ContentException e) {
			ExceptionHandler.handle(e);
		}
		cellPart.setRowSpan(rowSpan);
		cellPart.setColumnSpan(colSpan);

		removeMergeList(list);
		getTableAdapter().reload();
		getTableAdapter().transEnd();
		getViewer().setSelection(new StructuredSelection(cellPart));
	}

	// TODO move logic to adapt
	private void MergeContent(TableCellEditPart cellPart, List list) throws ContentException {
		CellHandle cellHandle = (CellHandle) cellPart.getModel();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			CellHandle handle = (CellHandle) (((TableCellEditPart) list.get(i)).getModel());

			List chList = handle.getSlot(CellHandle.CONTENT_SLOT).getContents();
			for (int j = 0; j < chList.size(); j++) {
				DesignElementHandle contentHandle = (DesignElementHandle) chList.get(j);
				// handle.getSlot( CellHandle.CONTENT_SLOT ).move(
				// contentHandle,
				// cellHandle,
				// CellHandle.CONTENT_SLOT );

				try {
					DesignElementHandle copy = contentHandle.copy().getHandle(cellHandle.getModule());
					handle.getSlot(CellHandle.CONTENT_SLOT).drop(contentHandle);
					cellHandle.getModuleHandle().rename(copy);
					cellHandle.getSlot(CellHandle.CONTENT_SLOT).add(copy);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}
			}
		}
	}

	/**
	 * not use?
	 * 
	 * @param list
	 */
	private void removeMergeList(ArrayList list) {

		int size = list.size();
		for (int i = 0; i < size; i++) {
			remove((TableCellEditPart) list.get(i));
		}
	}

	/**
	 * not use?
	 * 
	 * @param cellPart
	 */
	public void remove(TableCellEditPart cellPart) {
		try {
			getTableAdapter().removeChild(cellPart.getModel());
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.core.model.IModelAdaptHelper#
	 * getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		Dimension retValue = getFigure().getParent().getClientArea().getSize();
		Rectangle rect = getBounds();

		if (rect.width > 0) {
			retValue.width = rect.width;
		}
		if (rect.height > 0) {
			retValue.height = rect.height;
		}
		return retValue;
	}

	private void layoutManagerLayout() {
		((TableLayout) getContentPane().getLayoutManager()).markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.core.facade.ITableAdaptHelper#
	 * caleVisualWidth(int)
	 */
	public int caleVisualWidth(int columnNumber) {
		assert columnNumber > 0;
		return TableUtil.caleVisualWidth(this, getColumn(columnNumber));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.core.facade.ITableAdaptHelper#
	 * caleVisualHeight(int)
	 */
	public int caleVisualHeight(int rowNumber) {
		return TableUtil.caleVisualHeight(this, getRow(rowNumber));
	}

	/**
	 * Determines if selected cells can be merged.
	 * 
	 * @return true if merge success, else false.
	 */
	public boolean canMerge() {
		if (!isActive() || isDelete() || getParent() == null) {
			return false;
		}
		List list = TableUtil.getSelectionCells(this);
		int size = list.size();
		List temp = new ArrayList();
		for (int i = 0; i < size; i++) {
			ReportElementEditPart part = (ReportElementEditPart) list.get(i);
			if (part.isDelete()) {
				return false;
			}
			temp.add(part.getModel());
		}
		boolean rt = getTableAdapter().canMerge(temp);

		if (rt) {
			TableUtil.calculateNewSelection(TableUtil.getUnionBounds(list), list, getChildren());
			return list.size() == size;
		}

		return rt;
	}

	/**
	 * Split merged cells
	 * 
	 * @param part
	 */
	public void splitCell(TableCellEditPart part) {
		try {
			getTableAdapter().splitCell(part.getModel());
		} catch (ContentException e) {
			ExceptionHandler.handle(e);
		} catch (NameException e) {
			ExceptionHandler.handle(e);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * @param bool
	 * @param id
	 */
	public void includeSlotHandle(boolean bool, int id) {
		try {
			if (bool) {
				getTableAdapter().insertRowInSlotHandle(id);
			} else {
				getTableAdapter().deleteRowInSlotHandle(id);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Inserts group in table.
	 */
	public boolean insertGroup() {
		return UIUtil.createGroup(getTableAdapter().getHandle());
	}

	/**
	 * Inserts group in table.
	 * 
	 * @param position insert position
	 */
	public boolean insertGroup(int position) {
		return UIUtil.createGroup(getTableAdapter().getHandle(), position);
	}

	/**
	 * Removes group in table
	 * 
	 * @param group
	 */
	public void removeGroup(Object group) {
		try {
			((TableHandleAdapter) getModelAdapter()).removeGroup(group);
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.core.model.ITableAdaptHelper#
	 * getClientAreaSize()
	 */
	public Dimension getClientAreaSize() {
		return getFigure().getParent().getClientArea().getSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractEditPart#showTargetFeedback(org.eclipse
	 * .gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		if (this.getSelected() == 0 && isActive() && request.getType() == RequestConstants.REQ_SELECTION) {

			// if ( isFigureLeft( request ) )
			// {
			// this.getViewer( ).setCursor( ReportPlugin.getDefault( )
			// .getLeftCellCursor( ) );
			// }
			// else
			// {
			// this.getViewer( ).setCursor( ReportPlugin.getDefault( )
			// .getRightCellCursor( ) );
			// }
		}
		super.showTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractEditPart#eraseTargetFeedback(org.eclipse
	 * .gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		if (isActive()) {
			this.getViewer().setCursor(null);
		}
		super.eraseTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#addChildVisual(org
	 * .eclipse.gef.EditPart, int)
	 */
	protected void addChildVisual(EditPart part, int index) {
		// make sure we don't keep a select cell cursor after new contents
		// are added
		this.getViewer().setCursor(null);
		super.addChildVisual(part, index);
	}

	/**
	 * The class use for select row in table.
	 * 
	 */
	public static class DummyColumnEditPart extends DummyEditpart {

		/**
		 * @param model
		 */
		public DummyColumnEditPart(Object model) {
			super(model);
			createEditPolicies();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
		 * .DummyEditpart#createEditPolicies()
		 */
		protected void createEditPolicies() {

			ReportComponentEditPolicy policy = new ReportComponentEditPolicy() {

				protected org.eclipse.gef.commands.Command createDeleteCommand(GroupRequest deleteRequest) {
					DeleteColumnCommand command = new DeleteColumnCommand(getModel());
					return command;
				}
			};
			installEditPolicy(EditPolicy.COMPONENT_ROLE, policy);
		}

		public int getColumnNumber() {

			ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(getModel());
			if (adapt.getTableParent() == null) {
				return -1;
			}
			return adapt.getColumnNumber();

		}
	}

	/**
	 * The class use for select row in table.
	 * 
	 */
	public static class DummyRowEditPart extends DummyEditpart {

		/**
		 * @param model
		 */
		public DummyRowEditPart(Object model) {
			super(model);
			createEditPolicies();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
		 * .DummyEditpart#createEditPolicies()
		 */
		protected void createEditPolicies() {
			ReportComponentEditPolicy policy = new ReportComponentEditPolicy() {

				protected org.eclipse.gef.commands.Command createDeleteCommand(GroupRequest deleteRequest) {
					DeleteRowCommand command = new DeleteRowCommand(getModel());
					return command;
				}
			};
			installEditPolicy(EditPolicy.COMPONENT_ROLE, policy);
		}

		public int getRowNumber() {
			RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(getModel());
			if (adapt.getTableParent() == null) {
				return -1;
			}
			return adapt.getRowNumber();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#notifyModelChange()
	 */
	public void notifyModelChange() {
		super.notifyModelChange();
		layoutManagerLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#isinterest(java.lang.Object)
	 */
	public boolean isinterest(Object model) {
		if (model instanceof RowHandle || model instanceof ColumnHandle || model instanceof TableGroupHandle) {
			if (getModelAdapter().isChildren((DesignElementHandle) model)) {
				return true;
			}
		}
		return super.isinterest(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner
	 * #getColumnWidth(int)
	 */
	public ITableLayoutOwner.DimensionInfomation getColumnWidth(int number) {
		Object obj = getColumn(number);
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(obj);

		// add to handle percentage case.
		DimensionHandle handle = ((ColumnHandle) adapt.getHandle()).getWidth();
		return new ITableLayoutOwner.DimensionInfomation(handle.getMeasure(), handle.getUnits(),
				((ColumnHandle) adapt.getHandle()).getWidth().isSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner
	 * #getRowHeight(int)
	 */
	public ITableLayoutOwner.DimensionInfomation getRowHeight(int number) {
		Object obj = getRow(number);
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(obj);

		// return ( (RowHandle) adapt.getHandle( ) ).getHeight( );
		DimensionHandle handle = ((RowHandle) adapt.getHandle()).getHeight();
		return new ITableLayoutOwner.DimensionInfomation(handle.getMeasure(), handle.getUnits(),
				((RowHandle) adapt.getHandle()).getHeight().isSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner
	 * #getDefinedWidth()
	 */
	public String getDefinedWidth() {
		TableHandleAdapter tadp = HandleAdapterFactory.getInstance().getTableHandleAdapter(getModel());
		return tadp.getDefinedWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner
	 * #getRawWidth(int)
	 */
	public String getRawWidth(int columNumber) {
		Object obj = getColumn(columNumber);
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(obj);
		return adapt.getRawWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner
	 * #getColumnWidthValue(int)
	 */
	public int getColumnWidthValue(int number) {
		Object obj = getColumn(number);
		ColumnHandleAdapter adapt = HandleAdapterFactory.getInstance().getColumnHandleAdapter(obj);
		return adapt.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner
	 * #getRowHeightValue(int)
	 */
	public int getRowHeightValue(int number) {
		Object obj = getRow(number);
		RowHandleAdapter adapt = HandleAdapterFactory.getInstance().getRowHandleAdapter(obj);

		int value = adapt.getHeight();
		if (isFixLayout() && obj instanceof RowHandle) {
			DimensionHandle handle = ((RowHandle) obj).getHeight();

			int px = (int) DEUtil.convertoToPixel(handle);
			if (handle.isSet() && px <= 0) {
				value = 1;
			} else if (px <= 0) {
				value = FixTableLayout.DEFAULT_ROW_HEIGHT;
			}
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#getResizePolice(org.eclipse.gef.EditPolicy)
	 */
	public EditPolicy getResizePolice(EditPolicy parentPolice) {
		TableResizeEditPolice rpc = new TableResizeEditPolice();
		rpc.setResizeDirections(PositionConstants.SOUTH_EAST);

		return rpc;
	}

	/**
	 * @return
	 */
	public int getOriColumnNumber() {
		return oriColumnNumber;
	}

	/**
	 * @param oriColumnNumber
	 */
	public void setOriColumnNumber(int oriColumnNumber) {
		this.oriColumnNumber = oriColumnNumber;
	}

	/**
	 * @return
	 */
	public int getOriRowNumner() {
		return oriRowNumner;
	}

	/**
	 * @param oriRowNumner
	 */
	public void setOriRowNumner(int oriRowNumner) {
		this.oriRowNumner = oriRowNumner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner#
	 * getDefinedHeight()
	 */
	public String getDefinedHeight() {
		// Table don't support the table height
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner#
	 * isForceWidth()
	 */
	public boolean isForceWidth() {
		TableHandleAdapter tadp = HandleAdapterFactory.getInstance().getTableHandleAdapter(getModel());
		return tadp.isForceWidth();
	}

	public Object getAdapter(Class key) {
		if (key == IBreadcrumbNodeProvider.class) {
			return new TableElementBreadcrumbNodeProvider();
		}
		return super.getAdapter(key);
	}
}
