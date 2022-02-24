/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.RenameInputDialog;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ListenerElementVisitor;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.cubebuilder.BuilderPlugin;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.DateLevelDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.GroupDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.GroupRenameDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.LevelPropertyDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.MeasureDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeContentProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeLabelProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.DataContentProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureGroupModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;

public class CubeGroupContent extends Composite implements Listener {

	private TreeItem[] dragSourceItems = new TreeItem[1];
	private boolean[] useSorting = new boolean[] { false };
	private static final String SORTING_PREFERENCE_KEY = "ExpressionBuilder.preference.enable.sorting"; //$NON-NLS-1$

	class CustomDragListener implements DragSourceListener {

		private TreeViewer viewer;

		CustomDragListener(TreeViewer viewer) {
			this.viewer = viewer;
		}

		public void dragFinished(DragSourceEvent event) {

		};

		public void dragSetData(DragSourceEvent event) {
			event.data = dragSourceItems[0].getText();
		}

		public void dragStart(DragSourceEvent event) {
			TreeItem[] selection = viewer.getTree().getSelection();

			if (selection.length > 0) {
				if (viewer == dataFieldsViewer) {
					dragSourceItems[0] = selection[0];
				} else if (viewer == groupViewer && selection[0].getData() != null
						&& selection[0].getData() instanceof LevelHandle) {
					dragSourceItems[0] = selection[0];
				} else
					event.doit = false;
			} else {
				event.doit = false;
			}

		}
	}

	private TreeViewer dataFieldsViewer;

	public CubeGroupContent(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout(4, false);
		layout.marginTop = 0;
		this.setLayout(layout);
		createContent();
	}

	public void dispose() {
		dataBackup.dispose();
		groupBackup.dispose();
		if (visitor != null) {
			if (input != null)
				visitor.removeListener(input);
			visitor.dispose();
			visitor = null;
		}
		super.dispose();
	}

	private TabularCubeHandle input;
	private TreeViewer groupViewer;

	public void setInput(TabularCubeHandle cube) {
		if (input != null)
			getListenerElementVisitor().removeListener(input);
		this.input = cube;
	}

	private DataSetHandle[] datasets = new DataSetHandle[1];

	public void setInput(TabularCubeHandle cube, DataSetHandle dataset) {
		this.input = cube;
		datasets[0] = dataset;
	}

	public void createContent() {
		createDataField();
		createMoveButtonsField();
		createGroupField();
		createOperationField();
	}

	private void createOperationField() {
		Composite operationField = new Composite(this, SWT.NONE);
		operationField.setLayout(new GridLayout());
		operationField.setLayout(new GridLayout());

		String[] btnTexts = new String[] { Messages.getString("GroupsPage.Button.Add"), //$NON-NLS-1$
				Messages.getString("GroupsPage.Button.Edit"), //$NON-NLS-1$
				Messages.getString("GroupsPage.Button.Delete"), //$NON-NLS-1$
		};
		addBtn = new Button(operationField, SWT.PUSH);
		addBtn.setEnabled(false);
		addBtn.setText(btnTexts[0]);
		addBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleAddEvent();
			}

		});

		editBtn = new Button(operationField, SWT.PUSH);
		editBtn.setText(btnTexts[1]);
		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleEditEvent();
			}

		});

		delBtn = new Button(operationField, SWT.PUSH);
		delBtn.setText(btnTexts[2]);
		delBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDelEvent();
			}

		});

		int width = UIUtil.getMaxStringWidth(btnTexts, operationField) + 10;
		if (width < 60)
			width = 60;
		layoutButton(addBtn, width);
		layoutButton(editBtn, width);
		layoutButton(delBtn, width);
		addBtn.setEnabled(false);
		editBtn.setEnabled(false);
		delBtn.setEnabled(false);

		GridData data = (GridData) addBtn.getLayoutData();
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.BOTTOM;

		data = (GridData) delBtn.getLayoutData();
		data.grabExcessVerticalSpace = true;
		data.verticalAlignment = SWT.TOP;

	}

	private void layoutButton(Button button, int width) {
		GridData gd = new GridData();
		gd.widthHint = width;
		button.setLayoutData(gd);
	}

	TreeListener groupTreeListener = new TreeListener() {

		public void treeCollapsed(TreeEvent e) {
			Item item = (Item) e.item;
			if (groupBackup != null)
				groupBackup.updateCollapsedStatus(groupViewer, item.getData());

		}

		public void treeExpanded(TreeEvent e) {
			Item item = (Item) e.item;
			if (groupBackup != null)
				groupBackup.updateExpandedStatus(groupViewer, item.getData());
		}

	};

	private void initSorting() {
		// read setting from preference
		useSorting[0] = PreferenceFactory.getInstance().getPreferences(BuilderPlugin.getDefault())
				.getBoolean(SORTING_PREFERENCE_KEY);
	}

	private void toggleSorting(boolean sorted) {
		useSorting[0] = sorted;

		// update preference
		PreferenceFactory.getInstance().getPreferences(BuilderPlugin.getDefault()).setValue(SORTING_PREFERENCE_KEY,
				useSorting[0]);

		groupViewer.refresh();
	}

	private void createGroupField() {
		Composite groupField = new Composite(this, SWT.NONE);
		groupField.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		groupField.setLayout(layout);

		Label groupLabel = new Label(groupField, SWT.NONE);
		groupLabel.setText(Messages.getString("GroupsPage.Label.Group")); //$NON-NLS-1$

		ToolBar toolBar = new ToolBar(groupField, SWT.FLAT);
		toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		final ToolItem sortBtn = new ToolItem(toolBar, SWT.CHECK);
		sortBtn.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ALPHABETIC_SORT));
		sortBtn.setToolTipText(Messages.getString("GroupsPage.tooltip.Sort")); //$NON-NLS-1$
		sortBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleSorting(sortBtn.getSelection());
			}
		});

		groupViewer = new TreeViewer(groupField, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		groupViewer.getTree().setLayoutData(gd);
		((GridData) groupViewer.getTree().getLayoutData()).heightHint = 250;
		((GridData) groupViewer.getTree().getLayoutData()).widthHint = 200;
		groupViewer.setLabelProvider(getCubeLabelProvider());
		groupViewer.setContentProvider(new CubeContentProvider(useSorting));
		groupViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}

		});
		groupViewer.getTree().addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					try {
						if (delBtn.isEnabled())
							handleDelEvent();
					} catch (Exception e1) {
						ExceptionUtil.handle(e1);
					}
				}
			}
		});

		groupViewer.getTree().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				// Do nothing

			}

			// Handle double click event
			public void widgetDefaultSelected(SelectionEvent e) {
				if (editBtn.isEnabled())
					handleEditEvent();
			}

		});

		groupViewer.getTree().addListener(SWT.PaintItem, new org.eclipse.swt.widgets.Listener() {

			public void handleEvent(Event e) {
				TreeItem item = (TreeItem) e.item;

				Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);

				if (item != null) {
					if (item.getData() != null) {
						if (checkSharedDimension(item.getData()) && item.getData() instanceof LevelHandle)
							item.setForeground(gray);
						else
							item.setForeground(item.getParent().getForeground());
					} else
						item.setForeground(item.getParent().getForeground());
				}
			}
		});
		final DragSource fieldsSource = new DragSource(groupViewer.getTree(), operations);
		fieldsSource.setTransfer(types);
		fieldsSource.addDragListener(new CustomDragListener(groupViewer));

		DropTarget target = new DropTarget(groupViewer.getTree(), operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
				if (OlapUtil.isFromLibrary(input)) {
					event.detail = DND.DROP_NONE;
					return;
				}
				if (event.item != null) {
					TreeItem item = (TreeItem) event.item;
					Object element = item.getData();
					event.detail = DND.DROP_MOVE;

					Object obj = dragSourceItems[0].getData();
					ResultSetColumnHandle dataField = null;
					DataSetHandle dataset = null;
					if (obj == null || obj instanceof DataSetHandle
							|| (obj instanceof VirtualField
									&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_OTHER_DATASETS))
							|| (obj instanceof VirtualField
									&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_SHARED_DIMENSIONS))) {
						event.detail = DND.DROP_NONE;
						return;
					}

					if (checkSharedDimension(element)) {
						event.detail = DND.DROP_NONE;
						return;
					}

					if (obj instanceof ResultSetColumnHandle) {
						dataField = (ResultSetColumnHandle) obj;
						dataset = (DataSetHandle) dataField.getElementHandle();

						if (element instanceof LevelHandle) {
							DataSetHandle temp = OlapUtil.getHierarchyDataset(
									((TabularHierarchyHandle) ((LevelHandle) element).getContainer()));
							if (temp != null && dataset != null && dataset != temp) {
								event.detail = DND.DROP_NONE;
								return;
							}
							// if ( dataField != null
							// && isDateType( dataField.getDataType( ) ) )
							// {
							// event.detail = DND.DROP_NONE;
							// return;
							// }

							DesignElementHandle hierarchy = ((TabularLevelHandle) element).getContainer();
							DimensionHandle dimension = (DimensionHandle) hierarchy.getContainer();

							if (isTimeType(dimension)) {
								event.detail = DND.DROP_NONE;
								return;
							}

						} else if (element instanceof DimensionHandle || (element instanceof VirtualField
								&& ((VirtualField) element).getType().equals(VirtualField.TYPE_LEVEL))) {
							DimensionHandle dimension = null;
							if (element instanceof DimensionHandle)
								dimension = (DimensionHandle) element;
							else
								dimension = (DimensionHandle) ((VirtualField) element).getModel();

							if (isTimeType(dimension)) {

								if (dimension.getDefaultHierarchy().getLevelCount() != 0
										|| !isDateType(dataField.getDataType())) {
									event.detail = DND.DROP_NONE;
									return;
								}
							}

							DataSetHandle temp = OlapUtil
									.getHierarchyDataset((TabularHierarchyHandle) dimension.getDefaultHierarchy());
							if (temp != null && dataset != null && dataset != temp) {
								event.detail = DND.DROP_NONE;
								return;
							}
							// TabularHierarchyHandle hierarchy = (
							// (TabularHierarchyHandle)
							// dimension.getDefaultHierarchy( ) );
							// if ( hierarchy.getContentCount(
							// IHierarchyModel.LEVELS_PROP ) > 0 )
							// {
							// if ( dataField != null
							// && isDateType( dataField.getDataType( ) ) )
							// {
							// event.detail = DND.DROP_NONE;
							// return;
							// }
							// }

						} else if (element instanceof MeasureGroupHandle
								|| (element instanceof VirtualField
										&& ((VirtualField) element).getType().equals(VirtualField.TYPE_MEASURE))
								|| element instanceof MeasureHandle
								|| (element instanceof VirtualField
										&& ((VirtualField) element).getType().equals(VirtualField.TYPE_MEASURE_GROUP))
								|| (element instanceof PropertyHandle && ((PropertyHandle) element).getPropertyDefn()
										.getName().equals(ICubeModel.MEASURE_GROUPS_PROP))) {
							DataSetHandle primary = input.getDataSet();
							if (primary == null || primary != dataset) {
								event.detail = DND.DROP_NONE;
								return;
							}
						}
					}
					if (obj instanceof DimensionHandle) {
						DimensionHandle dimension = (DimensionHandle) obj;
						if (dimension.getContainer() instanceof TabularCubeHandle) {
							event.detail = DND.DROP_NONE;
							return;
						}

						if (!((element instanceof PropertyHandle && ((PropertyHandle) element).getPropertyDefn()
								.getName().equals(ICubeModel.DIMENSIONS_PROP))
								|| (element instanceof VirtualField
										&& ((VirtualField) element).getType().equals(VirtualField.TYPE_DIMENSION)))) {
							event.detail = DND.DROP_NONE;
							return;
						}
					}
					if (obj instanceof LevelHandle) {
						if (!(element instanceof LevelHandle) || element == obj
								|| ((LevelHandle) obj).getContainer() != ((LevelHandle) element).getContainer()) {
							event.detail = DND.DROP_NONE;
							return;
						}
					}

					Point pt = Display.getCurrent().map(null, groupViewer.getTree(), event.x, event.y);
					Rectangle bounds = item.getBounds();
					if (pt.y < bounds.y + bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				} else {
					event.detail = DND.DROP_NONE;
				}
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}

				Object obj = dragSourceItems[0].getData();
				ResultSetColumnHandle dataField = null;
				DataSetHandle dataset = null;
				if (obj == null || obj instanceof DataSetHandle) {
					event.detail = DND.DROP_NONE;
					return;
				}

				TreeItem item = (TreeItem) event.item;
				Object element = item.getData();

				if (obj instanceof DimensionHandle) {
					if ((element instanceof VirtualField
							&& ((VirtualField) element).getType().equals(VirtualField.TYPE_DIMENSION))
							|| (element instanceof PropertyHandle && ((PropertyHandle) element).getPropertyDefn()
									.getName().equals(ICubeModel.DIMENSIONS_PROP))) {
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(""); //$NON-NLS-1$

						try {
							TabularDimensionHandle dimension = DesignElementFactory.getInstance()
									.newTabularDimension(null);
							input.add(CubeHandle.DIMENSIONS_PROP, dimension);
							dimension.setSharedDimension((DimensionHandle) obj);
							stack.commit();
						} catch (SemanticException e) {
							stack.rollback();
							refresh();
							ExceptionUtil.handle(e);
						}
					}
				}

				if (obj instanceof ResultSetColumnHandle) {
					dataField = (ResultSetColumnHandle) obj;
					dataset = (DataSetHandle) dataField.getElementHandle();

					if (event.item == null || dataField == null) {
						event.detail = DND.DROP_NONE;
						return;
					} else {

						Point pt = Display.getCurrent().map(null, groupViewer.getTree(), event.x, event.y);
						Rectangle bounds = item.getBounds();

						Boolean isValidName = UIUtil.validateDimensionName(OlapUtil.getDataFieldDisplayName(dataField));

						if (pt.y < bounds.y + bounds.height / 3) {
							if (element instanceof MeasureHandle) {
								CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
								stack.startTrans(""); //$NON-NLS-1$
								try {
									TabularMeasureHandle measure = DesignElementFactory.getInstance()
											.newTabularMeasure(OlapUtil.getDataFieldDisplayName(dataField));

									Expression expression = new Expression(
											ExpressionUtility.getExpression(dataField,
													ExpressionUtility
															.getExpressionConverter(UIUtil.getDefaultScriptType())),
											UIUtil.getDefaultScriptType());
									measure.setExpressionProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, expression);
									ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
									if (column != null) {
										measure.setAlignment(column.getHorizontalAlign());
										measure.setFormat(column.getValueFormat());
									}

									initMeasure(dataField, measure);
									((MeasureHandle) element).getContainer().add(IMeasureGroupModel.MEASURES_PROP,
											measure);
									if (!isValidName) {
										MeasureDialog dialog = new MeasureDialog(false);
										dialog.setInput(measure);
										dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
										if (dialog.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();
								} catch (SemanticException e) {
									stack.rollback();
									refresh();
									ExceptionUtil.handle(e);
								}
								return;
							} else if (element instanceof LevelHandle) {
								DesignElementHandle hierarchy = ((TabularLevelHandle) element).getContainer();
								DimensionHandle dimension = (DimensionHandle) hierarchy.getContainer();

								if (isTimeType(dimension)) {
									event.detail = DND.DROP_NONE;
									return;
								}
								int index = ((LevelHandle) element).getIndex();
								CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
								stack.startTrans(""); //$NON-NLS-1$
								try {
									TabularLevelHandle level = DesignElementFactory.getInstance()
											.newTabularLevel(dimension, OlapUtil.getDataFieldDisplayName(dataField));
									level.setColumnName(dataField.getColumnName());
									level.setDataType(dataField.getDataType());
									((LevelHandle) element).getContainer().add(IHierarchyModel.LEVELS_PROP, level,
											index);
									ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
									if (column != null) {
										level.setAlignment(column.getHorizontalAlign());
										level.setFormat(column.getValueFormat());
									}

									if (!isValidName) {
										LevelPropertyDialog dialog = new LevelPropertyDialog(true);
										dialog.setInput(level);
										if (dialog.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();
								} catch (SemanticException e) {
									stack.rollback();
									refresh();
									ExceptionUtil.handle(e);
								}
								return;
							}
						}

						{
							if (element instanceof MeasureHandle) {
								if (!checkColumnDataType(dataField))
									return;
								CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
								stack.startTrans(""); //$NON-NLS-1$
								try {
									TabularMeasureHandle measure = DesignElementFactory.getInstance()
											.newTabularMeasure(OlapUtil.getDataFieldDisplayName(dataField));
									Expression expression = new Expression(
											ExpressionUtility.getExpression(dataField,
													ExpressionUtility
															.getExpressionConverter(UIUtil.getDefaultScriptType())),
											UIUtil.getDefaultScriptType());
									measure.setExpressionProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, expression);
									ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
									if (column != null) {
										measure.setAlignment(column.getHorizontalAlign());
										measure.setFormat(column.getValueFormat());
									}
									initMeasure(dataField, measure);
									((MeasureHandle) element).getContainer().add(IMeasureGroupModel.MEASURES_PROP,
											measure);
									if (!isValidName) {
										MeasureDialog dialog = new MeasureDialog(false);
										dialog.setInput(measure);
										dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
										if (dialog.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();
								} catch (SemanticException e) {
									stack.rollback();
									refresh();
									ExceptionUtil.handle(e);
									return;
								}
							} else if (element instanceof MeasureGroupHandle
									|| (element instanceof VirtualField
											&& ((VirtualField) element).getType().equals(VirtualField.TYPE_MEASURE))
									|| (element instanceof VirtualField && ((VirtualField) element).getType()
											.equals(VirtualField.TYPE_MEASURE_GROUP))
									|| (element instanceof PropertyHandle && ((PropertyHandle) element)
											.getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP))) {
								if (!checkColumnDataType(dataField))
									return;
								MeasureGroupHandle measureGroup = null;
								CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
								stack.startTrans(""); //$NON-NLS-1$
								try {
									if ((element instanceof VirtualField && ((VirtualField) element).getType()
											.equals(VirtualField.TYPE_MEASURE_GROUP))
											|| (element instanceof PropertyHandle
													&& ((PropertyHandle) element).getPropertyDefn().getName()
															.equals(ICubeModel.MEASURE_GROUPS_PROP))) {
										measureGroup = DesignElementFactory.getInstance().newTabularMeasureGroup(null);
										input.add(CubeHandle.MEASURE_GROUPS_PROP, measureGroup);
									} else {
										if (element instanceof MeasureGroupHandle)
											measureGroup = (MeasureGroupHandle) element;
										else
											measureGroup = (MeasureGroupHandle) ((VirtualField) element).getModel();
									}
									TabularMeasureHandle measure = DesignElementFactory.getInstance()
											.newTabularMeasure(OlapUtil.getDataFieldDisplayName(dataField));
									Expression expression = new Expression(
											ExpressionUtility.getExpression(dataField,
													ExpressionUtility
															.getExpressionConverter(UIUtil.getDefaultScriptType())),
											UIUtil.getDefaultScriptType());
									measure.setExpressionProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, expression);
									ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
									if (column != null) {
										measure.setAlignment(column.getHorizontalAlign());
										measure.setFormat(column.getValueFormat());
									}
									initMeasure(dataField, measure);
									measureGroup.add(IMeasureGroupModel.MEASURES_PROP, measure);
									if (!isValidName) {
										MeasureDialog dialog = new MeasureDialog(false);
										dialog.setInput(measure);
										dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
										if (dialog.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();
								} catch (SemanticException e) {
									stack.rollback();
									refresh();
									ExceptionUtil.handle(e);
									return;
								}
							} else if (element instanceof LevelHandle) {
								// if ( isDateType( dataField.getDataType( )
								// ) )
								// {
								// event.detail = DND.DROP_NONE;
								// return;
								// }

								TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) ((LevelHandle) element)
										.getContainer();
								DimensionHandle dimension = (DimensionHandle) hierarchy.getContainer();

								if (isTimeType(dimension)) {
									event.detail = DND.DROP_NONE;
									return;
								}
								CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
								stack.startTrans(""); //$NON-NLS-1$
								// if ( hierarchy.getDataSet( ) == null
								// && dataset != null )
								// {
								// hierarchy.setDataSet( dataset );
								// }
								try {
									int index = ((LevelHandle) element).getIndex();
									TabularLevelHandle level = DesignElementFactory.getInstance()
											.newTabularLevel(dimension, OlapUtil.getDataFieldDisplayName(dataField));
									level.setColumnName(dataField.getColumnName());
									level.setDataType(dataField.getDataType());
									ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
									if (column != null) {
										level.setAlignment(column.getHorizontalAlign());
										level.setFormat(column.getValueFormat());
									}
									((LevelHandle) element).getContainer().add(IHierarchyModel.LEVELS_PROP, level,
											index + 1);
									if (!isValidName) {
										LevelPropertyDialog dialog = new LevelPropertyDialog(true);
										dialog.setInput(level);
										if (dialog.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();
								} catch (SemanticException e) {
									stack.rollback();
									refresh();
									ExceptionUtil.handle(e);
									return;
								}
							} else if (element instanceof DimensionHandle
									|| (element instanceof VirtualField
											&& ((VirtualField) element).getType().equals(VirtualField.TYPE_LEVEL))
									|| (element instanceof VirtualField
											&& ((VirtualField) element).getType().equals(VirtualField.TYPE_DIMENSION))
									|| (element instanceof PropertyHandle && ((PropertyHandle) element)
											.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP))) {
								DimensionHandle dimension = null;
								CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
								stack.startTrans(""); //$NON-NLS-1$
								try {
									if ((element instanceof VirtualField
											&& ((VirtualField) element).getType().equals(VirtualField.TYPE_DIMENSION))
											|| (element instanceof PropertyHandle && ((PropertyHandle) element)
													.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP))) {

										if (!isDateType(dataField.getDataType())) {
											dimension = DesignElementFactory.getInstance().newTabularDimension(null);
											input.add(CubeHandle.DIMENSIONS_PROP, dimension);
											GroupRenameDialog inputDialog = createRenameDialog(dimension,
													Messages.getString("CubeGroupContent.Group.Add.Title"), //$NON-NLS-1$
													Messages.getString("CubeGroupContent.Group.Add.Message") //$NON-NLS-1$
											);
											if (inputDialog.open() != Window.OK) {
												stack.rollback();
												refresh();
												return;
											}
										} else {
											GroupDialog dialog = createGroupDialog();
											dialog.setInput(input, dataField);
											if (dialog.open() != Window.OK) {
												stack.rollback();
											} else {
												dimension = (DimensionHandle) dialog.getResult();
												TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
														.getDefaultHierarchy();
												if (!isValidName) {
													TabularLevelHandle level = (TabularLevelHandle) hierarchy
															.getLevel(dataField.getColumnName());
													LevelPropertyDialog dialog2 = new LevelPropertyDialog(false);
													dialog2.setInput(level);
													if (dialog2.open() == Window.CANCEL) {
														SessionHandleAdapter.getInstance().getCommandStack().rollback();
													}
												} else
													stack.commit();
											}

											refresh();
											return;
										}
									} else {
										if (element instanceof DimensionHandle)
											dimension = (DimensionHandle) element;
										else
											dimension = (DimensionHandle) ((VirtualField) element).getModel();
									}
									if (isTimeType(dimension) && dimension.getDefaultHierarchy().getLevelCount() > 0) {
										event.detail = DND.DROP_NONE;
										stack.rollback();
										return;
									}

									TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
											.getDefaultHierarchy();

									if (hierarchy.getDataSet() == null && hierarchy.getLevelCount() == 0
											&& (dataset != null && dataset != input.getDataSet())) {
										hierarchy.setDataSet(dataset);
									}

									TabularLevelHandle level = DesignElementFactory.getInstance()
											.newTabularLevel(dimension, OlapUtil.getDataFieldDisplayName(dataField));
									level.setColumnName(dataField.getColumnName());
									level.setDataType(dataField.getDataType());
									ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
									if (column != null) {
										level.setAlignment(column.getHorizontalAlign());
										level.setFormat(column.getValueFormat());
									}
									hierarchy.add(IHierarchyModel.LEVELS_PROP, level);
									if (!isValidName) {
										LevelPropertyDialog dialog = new LevelPropertyDialog(true);
										dialog.setInput(level);
										if (dialog.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();

								} catch (SemanticException e) {
									stack.rollback();
									refresh();
									ExceptionUtil.handle(e);
									return;
								}
							}
						}
					}
				}

				if (obj instanceof LevelHandle) {
					int oldIndex = ((LevelHandle) obj).getIndex();
					if (event.item == null) {
						event.detail = DND.DROP_NONE;
						return;
					} else {
						Point pt = Display.getCurrent().map(null, groupViewer.getTree(), event.x, event.y);
						Rectangle bounds = item.getBounds();

						if (element instanceof LevelHandle) {
							int newIndex = ((LevelHandle) element).getIndex();
							if (newIndex < oldIndex) {
								if (pt.y < bounds.y + bounds.height / 3) {
									newIndex = ((LevelHandle) element).getIndex();
								} else
									newIndex = ((LevelHandle) element).getIndex() + 1;
							} else if (newIndex > oldIndex) {
								if (pt.y < bounds.y + bounds.height / 3) {
									newIndex = ((LevelHandle) element).getIndex() - 1;
								} else
									newIndex = ((LevelHandle) element).getIndex();
							}

							CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
							stack.startTrans(""); //$NON-NLS-1$
							try {
								((LevelHandle) obj).moveTo(newIndex);
								stack.commit();
							} catch (SemanticException e) {

								stack.rollback();
								refresh();
								ExceptionUtil.handle(e);
								return;
							}
							groupViewer.expandToLevel((obj), AbstractTreeViewer.ALL_LEVELS);
							groupViewer.setSelection(new StructuredSelection((obj)), true);
						}
					}
				}
				refresh();
			}
		});

		initSorting();
		sortBtn.setSelection(useSorting[0]);
	}

	private void createMoveButtonsField() {
		Composite buttonsField = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		buttonsField.setLayout(layout);

		addButton = new Button(buttonsField, SWT.PUSH);
		addButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				handleAddEvent();

			}

		});

		removeButton = new Button(buttonsField, SWT.PUSH);
		removeButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				handleDelEvent();
			}

		});

		addButton.setText(">"); //$NON-NLS-1$
		addButton.setToolTipText(Messages.getString("GroupsPage.ArrowButton.Add.Tooltip"));//$NON-NLS-1$
		removeButton.setText("<"); //$NON-NLS-1$
		removeButton.setToolTipText(Messages.getString("GroupsPage.ArrowButton.Remove.Tooltip"));//$NON-NLS-1$
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = Math.max(25, addButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		gd.verticalAlignment = SWT.BOTTOM;
		addButton.setLayoutData(gd);

		gd = new GridData();
		gd.widthHint = Math.max(25, removeButton.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		removeButton.setLayoutData(gd);

	}

	TreeListener dataTreeListener = new TreeListener() {

		public void treeCollapsed(TreeEvent e) {
			Item item = (Item) e.item;
			if (dataBackup != null)
				dataBackup.updateCollapsedStatus(dataFieldsViewer, item.getData());

		}

		public void treeExpanded(TreeEvent e) {
			Item item = (Item) e.item;
			if (dataBackup != null)
				dataBackup.updateExpandedStatus(dataFieldsViewer, item.getData());
		}

	};

	private void createDataField() {
		Composite dataField = new Composite(this, SWT.NONE);
		dataField.setLayoutData(new GridData(GridData.FILL_BOTH));
		dataField.setLayout(new GridLayout());

		Label dataLabel = new Label(dataField, SWT.NONE);
		dataLabel.setText(Messages.getString("GroupsPage.Label.DataField")); //$NON-NLS-1$
		dataFieldsViewer = new TreeViewer(dataField, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		cubeLabelProvider = getCubeLabelProvider();
		cubeLabelProvider.setProivderViewer(true);
		dataFieldsViewer.setLabelProvider(cubeLabelProvider);
		dataFieldsViewer.setContentProvider(dataContentProvider);
		dataFieldsViewer.setAutoExpandLevel(3);
		GridData gd = new GridData(GridData.FILL_BOTH);
		dataFieldsViewer.getTree().setLayoutData(gd);
		((GridData) dataFieldsViewer.getTree().getLayoutData()).heightHint = 250;
		((GridData) dataFieldsViewer.getTree().getLayoutData()).widthHint = 200;
		dataFieldsViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}

		});

		final DragSource fieldsSource = new DragSource(dataFieldsViewer.getTree(), operations);
		fieldsSource.setTransfer(types);
		fieldsSource.addDragListener(new CustomDragListener(dataFieldsViewer));

	}

	private CubeLabelProvider getCubeLabelProvider() {
		Object label = ElementAdapterManager.getAdapter(this, CubeLabelProvider.class);
		if (label instanceof CubeLabelProvider)
			return (CubeLabelProvider) label;
		else
			return new CubeLabelProvider();
	}

	private Button addBtn;
	private Button delBtn;
	private int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	private Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	private Button editBtn;

	public void load() {
		if (input != null) {
			dataFieldsViewer.getTree().removeTreeListener(dataTreeListener);
			if (datasets[0] != null) {
				cubeLabelProvider.setInput(input);
				dataFieldsViewer.setInput(datasets);
			} else if (input.getDataSet() != null) {
				cubeLabelProvider.setInput(input);
				dataFieldsViewer.setInput(input);
			}
			refreshDataFieldViewer();
			if (dataBackup != null) {
				dataBackup.restoreBackup(dataFieldsViewer);
			} else {
				dataBackup = new TreeViewerBackup();
				dataFieldsViewer.expandToLevel(2);
				dataBackup.updateStatus(dataFieldsViewer);
			}
			dataFieldsViewer.getTree().addTreeListener(dataTreeListener);

			groupViewer.getTree().removeTreeListener(groupTreeListener);
			groupViewer.setInput(input);
			refreshDataFieldViewer();
			if (groupBackup != null) {
				groupBackup.restoreBackup(groupViewer);
			} else {
				groupBackup = new TreeViewerBackup();
				groupViewer.expandToLevel(4);
				groupBackup.updateStatus(groupViewer);
			}
			groupViewer.getTree().addTreeListener(groupTreeListener);

			getListenerElementVisitor().addListener(input);
			refresh();
		}
	}

	private ListenerElementVisitor visitor;

	private ListenerElementVisitor getListenerElementVisitor() {
		if (visitor == null) {
			visitor = new ListenerElementVisitor(this);
		}
		return visitor;
	}

	protected void updateButtons() {
		groupViewer.refresh();
		TreeSelection selections = (TreeSelection) groupViewer.getSelection();
		ResultSetColumnHandle dataField = null;
		if (selections.size() == 1) {
			Iterator iter = selections.iterator();
			Object obj = iter.next();

			TreeSelection dataSelection = (TreeSelection) dataFieldsViewer.getSelection();

			DataSetHandle dataset = null;
			if (dataSelection.size() == 1 && dataSelection.getFirstElement() != null
					&& dataSelection.getFirstElement() instanceof ResultSetColumnHandle) {
				dataField = (ResultSetColumnHandle) dataSelection.getFirstElement();
				dataset = (DataSetHandle) dataField.getElementHandle();
			}

			if (dataField == null || dataset == null)
				addBtn.setEnabled(false);

			/**
			 * Deal add button and del button.
			 */
			if (obj instanceof DimensionHandle || obj instanceof LevelHandle || obj instanceof MeasureHandle
					|| obj instanceof MeasureGroupHandle || obj instanceof VirtualField) {
				if (checkSharedDimension(obj)) {
					addBtn.setEnabled(false);
					delBtn.setEnabled(false);
					editBtn.setEnabled(false);
					if (obj instanceof DimensionHandle) {
						delBtn.setEnabled(true);
						// editBtn.setEnabled( true );
					}
					removeButton.setEnabled(delBtn.isEnabled());
					addButton.setEnabled(addBtn.isEnabled());

					return;
				}

				DimensionHandle dimenTemp = null;
				if (obj instanceof DimensionHandle) {
					dimenTemp = ((DimensionHandle) obj);
				} else if (obj instanceof VirtualField
						&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_LEVEL)) {
					dimenTemp = (DimensionHandle) ((VirtualField) obj).getModel();

				} else if (obj instanceof LevelHandle) {
					DesignElementHandle hierarchy = ((LevelHandle) obj).getContainer();
					dimenTemp = (DimensionHandle) hierarchy.getContainer();
				} else
					addBtn.setEnabled(true);

				if (dimenTemp != null) {
					DataSetHandle table = OlapUtil
							.getHierarchyDataset((TabularHierarchyHandle) dimenTemp.getDefaultHierarchy());
					if (table == null && dataField != null)
						addBtn.setEnabled(true);
					else if (table != null && dataset != table)
						addBtn.setEnabled(false);
					else {
						if (isTimeType(dimenTemp) && dataField != null) {
							if (isDateType(dataField.getDataType())
									&& dimenTemp.getDefaultHierarchy().getLevelCount() == 0)
								addBtn.setEnabled(true);
							else
								addBtn.setEnabled(false);
						}
						if (!isTimeType(dimenTemp) && dataField != null)
							addBtn.setEnabled(true);
					}

					// if ( dataField != null
					// && isDateType( dataField.getDataType( ) ) )
					// {
					// if ( dimenTemp.getDefaultHierarchy( )
					// .getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
					// {
					// addBtn.setEnabled( false );
					// }
					// else
					// addBtn.setEnabled( true );
					// }

				}

				if (obj instanceof MeasureGroupHandle
						|| (obj instanceof VirtualField
								&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_MEASURE))
						|| obj instanceof MeasureHandle) {
					// if ( dataset == input.getDataSet( ) || dataField == null
					// )
					// addBtn.setEnabled( true );
					// else
					addBtn.setEnabled(true);
				}

				if (obj instanceof LevelHandle) {
					DimensionHandle dimension = (DimensionHandle) ((LevelHandle) obj).getContainer().getContainer();
					if (isTimeType(dimension)) {
						if (dimension.getDefaultHierarchy().getLevelCount() > 1)
							delBtn.setEnabled(true);
						else
							delBtn.setEnabled(false);
					} else {
						delBtn.setEnabled(true);
					}
				} else if (obj instanceof VirtualField) {
					delBtn.setEnabled(false);
				} else {
					delBtn.setEnabled(true);
				}
			} else {
				delBtn.setEnabled(false);
			}

			/**
			 * CubeHandle can do nothing.
			 */
			if (obj instanceof CubeHandle)
				addBtn.setEnabled(false);
			/**
			 * CubeModel can and a group or a summary field
			 */
			else if (obj instanceof PropertyHandle)
				addBtn.setEnabled(true);

			/**
			 * Only Level Handle has EditBtn and PropBtn.
			 */
			if (obj instanceof LevelHandle) {
				TabularLevelHandle level = (TabularLevelHandle) obj;
				if (level != null && level.attributesIterator() != null && level.attributesIterator().hasNext()) {
					String name = level.getName() + " ("; //$NON-NLS-1$
					// + level.getColumnName( )
					// + ": "; //$NON-NLS-1$
					Iterator attrIter = level.attributesIterator();
					while (attrIter.hasNext()) {
						name += ((LevelAttributeHandle) attrIter.next()).getName();
						if (attrIter.hasNext())
							name += ", "; //$NON-NLS-1$
					}
					name += ")"; //$NON-NLS-1$
					groupViewer.getTree().getSelection()[0].setText(name);
				}
				editBtn.setEnabled(true);
			} else {
				if (obj instanceof DimensionHandle || obj instanceof MeasureGroupHandle || obj instanceof MeasureHandle)
					editBtn.setEnabled(true);
				else
					editBtn.setEnabled(false);
			}
			if (OlapUtil.isFromLibrary(input)) {
				addBtn.setEnabled(false);
				delBtn.setEnabled(false);
			}
		} else {
			addBtn.setEnabled(false);
			delBtn.setEnabled(false);
			editBtn.setEnabled(false);
		}

		removeButton.setEnabled(delBtn.isEnabled());
		addButton.setEnabled(addBtn.isEnabled());
	}

	private DataContentProvider dataContentProvider = new DataContentProvider();
	private CubeLabelProvider cubeLabelProvider;
	private TreeViewerBackup dataBackup;
	private TreeViewerBackup groupBackup;
	private Button addButton;
	private Button removeButton;

	private void handleDelEvent() {
		if (OlapUtil.isFromLibrary(input))
			return;
		TreeSelection slections = (TreeSelection) groupViewer.getSelection();
		Iterator iter = slections.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			stack.startTrans(""); //$NON-NLS-1$
			try {
				if (obj instanceof DimensionHandle) {
					DimensionHandle dimension = (DimensionHandle) obj;

					boolean hasExecuted = OlapUtil.enableDrop(dimension);
					if (hasExecuted) {
						UIHelper.dropDimensionProperties(dimension);
						dimension.dropAndClear();
					}

				} else if (obj instanceof LevelHandle) {
					LevelHandle level = (LevelHandle) obj;
					TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) level.getContainer();
					DimensionHandle dimension = (DimensionHandle) hierarchy.getContainer();

					boolean hasExecuted = OlapUtil.enableDrop(level);
					if (hasExecuted) {
						level.dropAndClear();
					}

					if (hierarchy.getContentCount(IHierarchyModel.LEVELS_PROP) == 0) {
						dimension.setTimeType(false);
						hierarchy.setDataSet(null);
					}

				} else if (obj instanceof MeasureGroupHandle) {
					MeasureGroupHandle measureGroup = (MeasureGroupHandle) obj;
					boolean hasExecuted = OlapUtil.enableDrop(measureGroup);
					if (hasExecuted) {
						measureGroup.dropAndClear();
					}
				} else if (obj instanceof MeasureHandle) {
					MeasureHandle measure = (MeasureHandle) obj;
					boolean hasExecuted = OlapUtil.enableDrop(measure);
					if (hasExecuted) {
						measure.dropAndClear();
					}
				}
				stack.commit();
			} catch (SemanticException e) {
				stack.rollback();
				refresh();
				ExceptionUtil.handle(e);
			}
		}
		refresh();
	}

	private void handleAddEvent() {
		if (OlapUtil.isFromLibrary(input))
			return;
		TreeSelection slections = (TreeSelection) groupViewer.getSelection();
		Iterator iter = slections.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();

			TreeSelection dataFields = (TreeSelection) dataFieldsViewer.getSelection();
			Iterator iterator = dataFields.iterator();
			Object dataField = null;
			while (iterator.hasNext()) {
				Object temp = iterator.next();
				if (!(temp instanceof ResultSetColumnHandle || temp instanceof DimensionHandle))
					continue;
				dataField = temp;
			}

			if (dataField instanceof ResultSetColumnHandle) {
				handleDataAddEvent();
			} else {
				if (obj instanceof MeasureGroupHandle || (obj instanceof VirtualField
						&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_MEASURE))) {
					MeasureGroupHandle measureGroup = null;
					if (obj instanceof MeasureGroupHandle)
						measureGroup = (MeasureGroupHandle) obj;
					else
						measureGroup = (MeasureGroupHandle) ((VirtualField) obj).getModel();
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					TabularMeasureHandle measure = DesignElementFactory.getInstance().newTabularMeasure(null);
					try {
						measureGroup.add(IMeasureGroupModel.MEASURES_PROP, measure);
						MeasureDialog dialog = new MeasureDialog(true);
						dialog.setInput(measure);
						dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
						if (dialog.open() == Window.CANCEL) {
							stack.rollback();
						} else
							stack.commit();
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
						stack.rollback();
					}
					refresh();
					return;
				} else if (obj instanceof MeasureHandle) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					TabularMeasureHandle measure = DesignElementFactory.getInstance().newTabularMeasure(null);
					try {
						((MeasureHandle) obj).getContainer().add(IMeasureGroupModel.MEASURES_PROP, measure);
						MeasureDialog dialog = new MeasureDialog(true);
						dialog.setInput(measure);
						dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
						if (dialog.open() == Window.CANCEL) {
							stack.rollback();
						} else
							stack.commit();
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
						stack.rollback();
					}
					refresh();
					return;
				}

				if (obj instanceof PropertyHandle
						|| (obj instanceof VirtualField && ((VirtualField) obj).getModel() instanceof PropertyHandle)) {
					PropertyHandle model;
					if (obj instanceof PropertyHandle)
						model = (PropertyHandle) obj;
					else
						model = (PropertyHandle) ((VirtualField) obj).getModel();
					if (model.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP)) {
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(""); //$NON-NLS-1$
						TabularDimensionHandle dimension = DesignElementFactory.getInstance().newTabularDimension(null);
						try {
							if (dataField instanceof DimensionHandle) {
								model.getElementHandle().add(ICubeModel.DIMENSIONS_PROP, dimension);
								dimension.setSharedDimension((DimensionHandle) dataField);
								stack.commit();
								refresh();
								continue;
							}
							model.getElementHandle().add(ICubeModel.DIMENSIONS_PROP, dimension);
						} catch (SemanticException e1) {
							stack.rollback();
							ExceptionUtil.handle(e1);
							refresh();
							continue;
						}

						GroupRenameDialog inputDialog = createRenameDialog(dimension,
								Messages.getString("CubeGroupContent.Group.Add.Title"), //$NON-NLS-1$
								Messages.getString("CubeGroupContent.Group.Add.Message") //$NON-NLS-1$
						);
						if (inputDialog.open() == Window.OK) {
							stack.commit();
						} else {
							stack.rollback();
							refresh();
							continue;
						}

						refresh();
					} else if (model.getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP)) {
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(""); //$NON-NLS-1$
						MeasureGroupHandle measureGroup = DesignElementFactory.getInstance()
								.newTabularMeasureGroup(null);
						try {
							model.getElementHandle().add(ICubeModel.MEASURE_GROUPS_PROP, measureGroup);
						} catch (SemanticException e1) {
							stack.rollback();
							ExceptionUtil.handle(e1);
							refresh();
							continue;
						}

						RenameInputDialog inputDialog = new RenameInputDialog(getShell(),
								Messages.getString("CubeGroupContent.Measure.Add.Title"), //$NON-NLS-1$
								Messages.getString("CubeGroupContent.Message.Add.Message"), //$NON-NLS-1$
								((DesignElementHandle) measureGroup).getName(),
								IHelpContextIds.SUMMARY_FIELD_DIALOG_ID);
						inputDialog.create();
						if (inputDialog.open() == Window.OK) {
							try {
								((DesignElementHandle) measureGroup).setName(inputDialog.getResult().toString().trim());
								stack.commit();
							} catch (Exception e1) {
								ExceptionUtil.handle(e1);
								stack.rollback();
								refresh();
								continue;
							}
						} else {
							stack.rollback();
							refresh();
							continue;
						}

						refresh();
					}
				}
			}

		}
	}

	private boolean isDateType(String dataType) {
		return dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATE)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_TIME);
	}

	protected void handleDataAddEvent() {
		if (OlapUtil.isFromLibrary(input))
			return;
		TreeSelection dataFields = (TreeSelection) dataFieldsViewer.getSelection();
		Iterator iterator = dataFields.iterator();
		while (iterator.hasNext()) {
			Object temp = iterator.next();
			if (!(temp instanceof ResultSetColumnHandle))
				continue;

			ResultSetColumnHandle dataField = (ResultSetColumnHandle) temp;
			Boolean isValidName = UIUtil.validateDimensionName(OlapUtil.getDataFieldDisplayName(dataField));
			DataSetHandle dataset = (DataSetHandle) dataField.getElementHandle();
			DataSetHandle primary = (input).getDataSet();

			TreeSelection slections = (TreeSelection) groupViewer.getSelection();
			Iterator iter = slections.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof TabularLevelHandle) {
					// if ( isDateType( dataField.getDataType( ) ) )
					// continue;

					TabularHierarchyHandle hierarchy = ((TabularHierarchyHandle) ((TabularLevelHandle) obj)
							.getContainer());
					TabularDimensionHandle dimension = (TabularDimensionHandle) hierarchy.getContainer();
					if (isTimeType(dimension))
						continue;

					DataSetHandle dasetTemp = OlapUtil.getHierarchyDataset(hierarchy);
					if (dasetTemp != null && dataset != null && dataset != dasetTemp) {
						continue;
					}

					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					// if ( hierarchy.getDataSet( ) == null )
					// {
					// try
					// {
					// if ( hierarchy.getLevelCount( ) == 0
					// && dataset != primary )
					// hierarchy.setDataSet( dataset );
					// }
					// catch ( SemanticException e )
					// {
					// ExceptionUtil.handle( e );
					// }
					// }
					TabularLevelHandle level = DesignElementFactory.getInstance().newTabularLevel(dimension,
							OlapUtil.getDataFieldDisplayName(dataField));
					try {
						level.setColumnName(dataField.getColumnName());
						level.setDataType(dataField.getDataType());
						((TabularLevelHandle) obj).getContainer().add(IHierarchyModel.LEVELS_PROP, level,
								((TabularLevelHandle) obj).getIndex() + 1);
						ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
						if (column != null) {
							level.setAlignment(column.getHorizontalAlign());
							level.setFormat(column.getValueFormat());
						}
					} catch (SemanticException e) {
						stack.rollback();
						refresh();
						ExceptionUtil.handle(e);
						continue;
					}
					if (!isValidName) {
						LevelPropertyDialog dialog = new LevelPropertyDialog(true);
						dialog.setInput(level);
						if (dialog.open() == Window.CANCEL) {
							SessionHandleAdapter.getInstance().getCommandStack().rollback();
						}
					} else
						stack.commit();
					refresh();
					return;
				} else if (obj instanceof DimensionHandle
						|| (obj instanceof VirtualField
								&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_LEVEL))
						|| (obj instanceof VirtualField
								&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_DIMENSION))
						|| (obj instanceof PropertyHandle && ((PropertyHandle) obj).getPropertyDefn().getName()
								.equals(ICubeModel.DIMENSIONS_PROP))) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					DimensionHandle dimension = null;
					if ((obj instanceof VirtualField
							&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_DIMENSION))
							|| (obj instanceof PropertyHandle && ((PropertyHandle) obj).getPropertyDefn().getName()
									.equals(ICubeModel.DIMENSIONS_PROP))) {

						try {

							if (!isDateType(dataField.getDataType())) {
								dimension = DesignElementFactory.getInstance().newTabularDimension(null);
								input.add(CubeHandle.DIMENSIONS_PROP, dimension);
								GroupRenameDialog inputDialog = createRenameDialog(dimension,
										Messages.getString("CubeGroupContent.Group.Add.Title"), //$NON-NLS-1$
										Messages.getString("CubeGroupContent.Group.Add.Message") //$NON-NLS-1$
								);
								if (inputDialog.open() != Window.OK) {
									stack.rollback();
									refresh();
									continue;
								}
							} else {
								GroupDialog dialog = createGroupDialog();
								dialog.setInput(input, dataField);
								if (dialog.open() != Window.OK) {
									stack.rollback();
								} else {
									dimension = (DimensionHandle) dialog.getResult();
									TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
											.getDefaultHierarchy();
									if (!isValidName) {
										TabularLevelHandle level = (TabularLevelHandle) hierarchy
												.getLevel(dataField.getColumnName());
										LevelPropertyDialog dialog2 = new LevelPropertyDialog(false);
										dialog2.setInput(level);
										if (dialog2.open() == Window.CANCEL) {
											SessionHandleAdapter.getInstance().getCommandStack().rollback();
										}
									} else
										stack.commit();
								}
								refresh();
								continue;
							}
						} catch (SemanticException e) {
							stack.rollback();
							refresh();
							ExceptionUtil.handle(e);
							continue;
						}
					} else {
						if (obj instanceof TabularDimensionHandle)
							dimension = (TabularDimensionHandle) obj;
						else
							dimension = (TabularDimensionHandle) ((VirtualField) obj).getModel();
					}
					if (isTimeType(dimension)) {
						if (dimension.getDefaultHierarchy().getLevelCount() > 0 || !isDateType(dataField.getDataType()))
							stack.rollback();
						continue;
					}

					TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimension
							.getContent(IDimensionModel.HIERARCHIES_PROP, 0);

					DataSetHandle dasetTemp = OlapUtil.getHierarchyDataset(hierarchy);
					if (dasetTemp != null && dataset != null && dataset != dasetTemp) {
						stack.rollback();
						continue;
					}

					if (hierarchy.getDataSet() == null) {
						try {
							if (hierarchy.getLevelCount() == 0 && dataset != primary)
								hierarchy.setDataSet(dataset);
						} catch (SemanticException e) {
							stack.rollback();
							refresh();
							ExceptionUtil.handle(e);
							continue;
						}
					}
					try {

						TabularLevelHandle level = DesignElementFactory.getInstance().newTabularLevel(dimension,
								OlapUtil.getDataFieldDisplayName(dataField));
						level.setColumnName(dataField.getColumnName());
						level.setDataType(dataField.getDataType());
						ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
						if (column != null) {
							level.setAlignment(column.getHorizontalAlign());
							level.setFormat(column.getValueFormat());
						}
						hierarchy.add(IHierarchyModel.LEVELS_PROP, level);
						if (!isValidName) {
							LevelPropertyDialog dialog = new LevelPropertyDialog(true);
							dialog.setInput(level);
							if (dialog.open() == Window.CANCEL) {
								SessionHandleAdapter.getInstance().getCommandStack().rollback();
							}
						} else
							stack.commit();

						// if ( dataset != input.getDataSet( ) )
						// {
						// builder.showSelectionPage( builder.getLinkGroupNode(
						// ) );
						// }
					} catch (SemanticException e) {
						stack.rollback();
						ExceptionUtil.handle(e);
					}
					refresh();
					return;
				} else {
					if (obj instanceof MeasureGroupHandle
							|| (obj instanceof VirtualField
									&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_MEASURE))
							|| (obj instanceof VirtualField
									&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_MEASURE_GROUP))
							|| (obj instanceof PropertyHandle && ((PropertyHandle) obj).getPropertyDefn().getName()
									.equals(ICubeModel.MEASURE_GROUPS_PROP))) {
						if (!checkColumnDataType(dataField))
							return;
						MeasureGroupHandle measureGroup = null;
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(""); //$NON-NLS-1$
						if ((obj instanceof VirtualField
								&& ((VirtualField) obj).getType().equals(VirtualField.TYPE_MEASURE_GROUP))
								|| (obj instanceof PropertyHandle && ((PropertyHandle) obj).getPropertyDefn().getName()
										.equals(ICubeModel.MEASURE_GROUPS_PROP))) {
							measureGroup = DesignElementFactory.getInstance().newTabularMeasureGroup(null);
							try {
								input.add(CubeHandle.MEASURE_GROUPS_PROP, measureGroup);
							} catch (SemanticException e) {
								stack.rollback();
								refresh();
								ExceptionUtil.handle(e);
								continue;
							}
						} else {
							if (obj instanceof MeasureGroupHandle)
								measureGroup = (MeasureGroupHandle) obj;
							else
								measureGroup = (MeasureGroupHandle) ((VirtualField) obj).getModel();
						}
						TabularMeasureHandle measure = DesignElementFactory.getInstance()
								.newTabularMeasure(OlapUtil.getDataFieldDisplayName(dataField));
						try {
							if (dataset != null && primary != null && dataset == primary) {
								Expression expression = new Expression(
										ExpressionUtility.getExpression(dataField,
												ExpressionUtility
														.getExpressionConverter(UIUtil.getDefaultScriptType())),
										UIUtil.getDefaultScriptType());
								measure.setExpressionProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, expression);
								ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
								if (column != null) {
									measure.setAlignment(column.getHorizontalAlign());
									measure.setFormat(column.getValueFormat());
								}
							}
							initMeasure(dataField, measure);
							measureGroup.add(IMeasureGroupModel.MEASURES_PROP, measure);
							if (!isValidName || measure.getMeasureExpression() == null) {
								MeasureDialog dialog = new MeasureDialog(false);
								dialog.setInput(measure);
								dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
								if (dialog.open() == Window.CANCEL) {
									stack.rollback();
								} else
									stack.commit();
							} else
								stack.commit();
						} catch (SemanticException e1) {
							stack.rollback();
							ExceptionUtil.handle(e1);
						}
						refresh();
						return;
					} else if (obj instanceof MeasureHandle) {
						if (!checkColumnDataType(dataField))
							return;
						CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
						stack.startTrans(""); //$NON-NLS-1$
						TabularMeasureHandle measure = DesignElementFactory.getInstance()
								.newTabularMeasure(OlapUtil.getDataFieldDisplayName(dataField));
						try {
							if (dataset != null && primary != null && dataset == primary) {
								Expression expression = new Expression(
										ExpressionUtility.getExpression(dataField,
												ExpressionUtility
														.getExpressionConverter(UIUtil.getDefaultScriptType())),
										UIUtil.getDefaultScriptType());
								measure.setExpressionProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, expression);
								ColumnHintHandle column = OlapUtil.getColumnHintHandle(dataField);
								if (column != null) {
									measure.setAlignment(column.getHorizontalAlign());
									measure.setFormat(column.getValueFormat());
								}
							}
							initMeasure(dataField, measure);
							((MeasureHandle) obj).getContainer().add(IMeasureGroupModel.MEASURES_PROP, measure);
							if (!isValidName || measure.getMeasureExpression() == null) {
								MeasureDialog dialog = new MeasureDialog(false);
								dialog.setInput(measure);
								dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
								if (dialog.open() == Window.CANCEL) {
									stack.rollback();
								} else
									stack.commit();
							} else
								stack.commit();
						} catch (SemanticException e1) {
							stack.rollback();
							ExceptionUtil.handle(e1);
						}
						refresh();
						return;
					}
				}

			}
		}
	}

	private void initMeasure(ResultSetColumnHandle dataField, TabularMeasureHandle measure) throws SemanticException {
		String dataType = dataField.getDataType();
		measure.setDataType(dataType);
		if (DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals(dataType))
			return;

		IAggrFunction countFunction = getCountFunction();
		if (countFunction != null) {
			measure.setFunction(countFunction.getName());
			measure.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER);
		}

	}

	private IAggrFunction getCountFunction() {
		IAggrFunction countFunction = null;

		try {
			String countFunctionName = DataAdapterUtil
					.adaptModelAggregationType(DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT);
			List aggrInfoList = DataUtil.getAggregationManager().getAggregations(AggregationManager.AGGR_MEASURE);
			for (int i = 0; i < aggrInfoList.size(); i++) {
				IAggrFunction function = (IAggrFunction) aggrInfoList.get(i);
				if (function.getDisplayName().equals(countFunctionName)) {
					countFunction = function;
					break;
				}

			}
		} catch (BirtException e) {
			ExceptionUtil.handle(e);
		}
		return countFunction;
	}

	private GroupRenameDialog createRenameDialog(DimensionHandle handle, String title, String message) {
		GroupRenameDialog inputDialog = new GroupRenameDialog(getShell(), title, message);
		inputDialog.setInput(handle);
		return inputDialog;
	}

	/**
	 * @deprecated
	 */
	private InputDialog createInputDialog(ReportElementHandle handle, String title, String message) {
		InputDialog inputDialog = new InputDialog(getShell(), title, message, handle.getName(), null) {

			public int open() {

				return super.open();
			}
		};
		inputDialog.create();
		return inputDialog;
	}

	private boolean checkColumnDataType(ResultSetColumnHandle dataField) {
		if (dataField.getDataType().equals(DesignChoiceConstants.COLUMN_DATA_TYPE_ANY)) {
			MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("CubeGroupContent.MeasureDataTypeErrorDialog.Title"), //$NON-NLS-1$
					null, Messages.getFormattedString("CubeGroupContent.MeasureDataTypeErrorDialog.Message", //$NON-NLS-1$
							new Object[] { OlapUtil.getDataFieldDisplayName(dataField) }),
					MessageDialog.WARNING, new String[] { IDialogConstants.OK_LABEL }, 0);
			dialog.open();
			return false;
		}
		return true;
	}

	public void refresh() {
		updateButtons();
		refreshDataFieldViewer();
	}

	protected void refreshDataFieldViewer() {
		dataFieldsViewer.refresh();
	}

	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (groupViewer == null || groupViewer.getControl().isDisposed()) {
			return;
		}
		groupViewer.refresh();
		expandNodeAfterCreation(ev);
		groupBackup.updateStatus(groupViewer);
		getListenerElementVisitor().addListener(focus);
	}

	private void expandNodeAfterCreation(NotificationEvent ev) {
		if (ev instanceof ContentEvent && ev.getEventType() == NotificationEvent.CONTENT_EVENT
				&& ((ContentEvent) ev).getAction() == ContentEvent.ADD) {
			IDesignElement element = ((ContentEvent) ev).getContent();
			if (element != null) {
				final DesignElementHandle handle = element.getHandle(input.getModule());
				groupViewer.expandToLevel(handle, AbstractTreeViewer.ALL_LEVELS);
				groupViewer.setSelection(new StructuredSelection(handle), true);
				refresh();
			}
		}
	}

	private void handleEditEvent() {
		TreeSelection slections = (TreeSelection) groupViewer.getSelection();
		Iterator iter = slections.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();

			if (obj instanceof TabularLevelHandle) {
				TabularLevelHandle level = (TabularLevelHandle) obj;
				// if ( level.getDataType( )
				// .equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME ) )
				if (isTimeType((DimensionHandle) level.getContainer().getContainer())) {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					DateLevelDialog dialog = new DateLevelDialog();
					dialog.setInput(input, level);
					if (dialog.open() == Window.OK) {
						stack.commit();
						refresh();
					} else {
						stack.rollback();
					}
				} else {
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					LevelPropertyDialog dialog = new LevelPropertyDialog(false);
					dialog.setInput(level);
					if (dialog.open() == Window.OK) {
						stack.commit();
						refresh();
					} else {
						stack.rollback();
					}
				}
			} else if (obj instanceof TabularMeasureHandle) {
				TabularMeasureHandle level = (TabularMeasureHandle) obj;
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans(""); //$NON-NLS-1$
				MeasureDialog dialog = new MeasureDialog(false);
				dialog.setInput(level);
				dialog.setAutoPrimaryKeyStatus(input.autoPrimaryKey());
				if (dialog.open() == Window.OK) {
					stack.commit();
					refresh();
				} else {
					stack.rollback();
				}
			} else if (obj instanceof DimensionHandle && isTimeType((DimensionHandle) obj)
					&& ((DimensionHandle) obj).getDefaultHierarchy().getLevelCount() > 0
					&& !checkSharedDimension(obj)) {
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans(""); //$NON-NLS-1$
				GroupDialog dialog = createGroupDialog();
				dialog.setInput((TabularHierarchyHandle) ((DimensionHandle) obj).getDefaultHierarchy());
				if (dialog.open() == Window.OK) {
					stack.commit();
				} else {
					stack.rollback();
				}
			} else {
				String title = Messages.getString("RenameInputDialog.DialogTitle"); //$NON-NLS-1$
				String message = Messages.getString("RenameInputDialog.DialogMessage"); //$NON-NLS-1$
				if (obj instanceof DimensionHandle) {
					title = Messages.getString("CubeGroupContent.Group.Edit.Title"); //$NON-NLS-1$
					message = Messages.getString("CubeGroupContent.Group.Edit.Message"); //$NON-NLS-1$
					CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
					stack.startTrans(""); //$NON-NLS-1$
					GroupRenameDialog inputDialog = createRenameDialog((DimensionHandle) obj, title, message);
					if (inputDialog.open() == Window.OK) {
						stack.commit();
					} else {
						stack.rollback();
					}
				} else if (obj instanceof MeasureGroupHandle) {
					RenameInputDialog inputDialog = new RenameInputDialog(getShell(),
							Messages.getString("CubeGroupContent.Measure.Edit.Title"), //$NON-NLS-1$ ,
							Messages.getString("CubeGroupContent.Measure.Edit.Message"), //$NON-NLS-1$ ,
							((ReportElementHandle) obj).getName(), IHelpContextIds.SUMMARY_FIELD_DIALOG_ID);
					inputDialog.create();
					if (inputDialog.open() == Window.OK) {
						try {
							((DesignElementHandle) obj).setName(inputDialog.getResult().toString().trim());
						} catch (NameException e1) {
							ExceptionUtil.handle(e1);
						}
					}
				}
			}
		}
		refresh();
	}

	private boolean checkSharedDimension(Object element) {
		DimensionHandle tempDimension = null;

		if (element instanceof LevelHandle) {
			tempDimension = (DimensionHandle) ((LevelHandle) element).getContainer().getContainer();
		} else if (element instanceof DimensionHandle) {
			tempDimension = (DimensionHandle) element;
		}

		if (tempDimension != null && (!(tempDimension.getContainer() instanceof CubeHandle)
				|| (tempDimension instanceof TabularDimensionHandle
						&& (((TabularDimensionHandle) tempDimension).getSharedDimension() != null
								|| ((TabularDimensionHandle) tempDimension).getContent(DimensionHandle.HIERARCHIES_PROP,
										0) == null)))) {
			return true;
		}

		return false;
	}

	protected GroupDialog createGroupDialog() {
		return new GroupDialog();
	}

	protected GroupDialog createGroupDialog(TabularHierarchyHandle hierarchy) {
		return new GroupDialog(hierarchy);
	}

	protected boolean isTimeType(DimensionHandle dimension) {
		return dimension.isTimeType();
	}
}
