
package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @deprecated use {@link DataColumnBindingDialog} instead
 */
public class DataItemBindingDialog extends BaseDialog {

	protected static final String NAME = Messages.getString("DataItemBindingDialog.text.Name"); //$NON-NLS-1$
	protected static final String DISPLAY_NAME = Messages.getString("DataItemBindingDialog.text.displayName"); //$NON-NLS-1$

	protected static final String DATA_TYPE = Messages.getString("DataItemBindingDialog.text.DataType"); //$NON-NLS-1$

	protected static final String EXPRESSION = Messages.getString("DataItemBindingDialog.text.Expression"); //$NON-NLS-1$

	protected static final String AGGREGATE_ON = Messages.getString("DataItemBindingDialog.text.AggregateOn"); //$NON-NLS-1$

	protected static final String FORCE_BINDING_TEXT = Messages.getString("DataItemBindingDialog.text.ForceBinding"); //$NON-NLS-1$

	protected static final String DEFAULT_ITEM_NAME = "data item"; //$NON-NLS-1$

	protected static final String ALL = Messages.getString("DataItemBindingDialog.text.All"); //$NON-NLS-1$

	protected static final String NONE = Messages.getString("DataItemBindingDialog.text.None"); //$NON-NLS-1$

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices();

	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices(null);

	protected static final String NEW_DATAITEM_TITLE = Messages.getString("DataItemBindingDialog.title.CreateNewItem"); //$NON-NLS-1$

	protected static final String EDIT_DATAITEM_TITLE = Messages.getString("DataItemBindingDialog.title.EditDataItem"); //$NON-NLS-1$

	protected ReportItemHandle input;

	protected DesignElementHandle bindingObject;

	protected String[] dataTypes;

	protected String[] itemNames;

	// protected String[] aggregateOns;

	protected String expression;

	private Combo itemType;

	private Text itemName;

	private Text itemDisplayName;

	// private Combo itemAggregateOn;

	private Text itemExpression;

	private String typeSelect;

	// private String aggregateOnSelect;
	//
	// private Label aggregateOnLabel;
	//
	// private Label hiddenLabel;

	protected ComputedColumnHandle bindingColumn;

	protected boolean isCreateNew;

	public DataItemBindingDialog(boolean isCreateNew) {
		super(isCreateNew == true ? NEW_DATAITEM_TITLE : EDIT_DATAITEM_TITLE);
		this.isCreateNew = isCreateNew;
	}

	public DataItemBindingDialog(String title) {
		super(title);
	}

	protected String[] convertListToStrings(List list) {
		if (list == null)
			return null;
		String[] strings = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strings[i] = list.get(i).toString();
		}
		return strings;
	}

	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite) super.createDialogArea(parent);
		UIUtil.bindHelp(composite, IHelpContextIds.DATA_ITEM_BINDING_DIALOG);
		((GridLayout) composite.getLayout()).numColumns = 3;

		new Label(composite, SWT.NONE).setText(NAME);
		itemName = new Text(composite, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.minimumWidth = 250;
		itemName.setLayoutData(data);
		WidgetUtil.createGridPlaceholder(composite, 1, false);

		itemName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtonStatus();

			}

		});

		new Label(composite, SWT.NONE).setText(DISPLAY_NAME);
		itemDisplayName = new Text(composite, SWT.BORDER);
		itemDisplayName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		WidgetUtil.createGridPlaceholder(composite, 1, false);

		new Label(composite, SWT.NONE).setText(DATA_TYPE);
		itemType = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		itemType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		itemType.setVisibleItemCount(30);
		WidgetUtil.createGridPlaceholder(composite, 1, false);

		new Label(composite, SWT.NONE).setText(EXPRESSION);
		itemExpression = new Text(composite, SWT.BORDER);
		itemExpression.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		itemExpression.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtonStatus();
			}

		});

		Button expressionButton = new Button(composite, SWT.PUSH);
		// expressionButton.setText( "..." );
		// GridData gd = new GridData( );
		// gd.heightHint = 20;
		// gd.widthHint = 20;
		// expressionButton.setLayoutData( gd );
		expressionButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleExpressionButtonSelectEvent();
			}
		});

		UIUtil.setExpressionButtonImage(expressionButton);

		/*
		 * itemExpression.addModifyListener( new ModifyListener( ) {
		 * 
		 * public void modifyText( ModifyEvent e ) { if ( ExpressionUtil.hasAggregation(
		 * itemExpression.getText( ) ) ) { String groupType =
		 * DEUtil.getGroupControlType( input ); if ( !( bindingColumn != null &&
		 * bindingColumn.getExpression( ) != null && bindingColumn.getExpression( )
		 * .equals( itemExpression.getText( ) ) ) ) { if ( groupType.equals(
		 * DEUtil.TYPE_GROUP_GROUP ) ) { setAggregateOnSelect( ( (GroupHandle)
		 * DEUtil.getGroups( input ) .get( 0 ) ).getName( ) ); } else if (
		 * groupType.equals( DEUtil.TYPE_GROUP_LISTING ) ) { setAggregateOnSelect( ALL
		 * ); } else setAggregateOnSelect( NONE ); } else if ( groupType.equals(
		 * DEUtil.TYPE_GROUP_NONE ) ) { setAggregateOnSelect( NONE ); } } if (
		 * ExpressionUtil.hasAggregation( itemExpression.getText( ) ) &&
		 * !itemAggregateOn.getVisible( ) ) { aggregateOnLabel.setVisible( true );
		 * itemAggregateOn.setVisible( true ); hiddenLabel.setVisible( true ); } else if
		 * ( !ExpressionUtil.hasAggregation( itemExpression.getText( ) ) &&
		 * itemAggregateOn.getVisible( ) ) { aggregateOnLabel.setVisible( false );
		 * itemAggregateOn.setVisible( false ); hiddenLabel.setVisible( false ); } } }
		 * );
		 * 
		 * aggregateOnLabel = new Label( composite, SWT.NONE );
		 * aggregateOnLabel.setText( AGGREGATE_ON ); itemAggregateOn = new Combo(
		 * composite, SWT.BORDER | SWT.READ_ONLY ); itemAggregateOn.setLayoutData( new
		 * GridData( GridData.FILL_HORIZONTAL ) ); hiddenLabel =
		 * WidgetUtil.createGridPlaceholder( composite, 1, false );
		 * aggregateOnLabel.setVisible( false ); itemAggregateOn.setVisible( false );
		 * hiddenLabel.setVisible( false );
		 */
		init();
		return composite;
	}

	private ComputedColumnHandle getInputBinding(ReportItemHandle input, String bindingName) {
		List elementsList = DEUtil.getVisiableColumnBindingsList(input);
		if (elementsList != null && elementsList.size() > 0) {
			for (int i = 0; i < elementsList.size(); i++) {
				if (((ComputedColumnHandle) elementsList.get(i)).getName().equals(bindingName))
					return (ComputedColumnHandle) elementsList.get(i);
			}
		}
		return null;
	}

	private int getItemIndex(String[] items, String item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(item))
				return i;
		}
		return -1;
	}

	private void init() {
		initDataTypes();
		initName();
		initDisplayName();
		// initAggregateOns( );
		initExpression();
	}

	private String displayName;

	private void initDisplayName() {
		if (displayName != null && itemDisplayName != null) {
			itemDisplayName.setText(displayName);
		}
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		initDisplayName();
	}

	/*
	 * private void initAggregateOns( ) { if ( aggregateOns != null &&
	 * itemAggregateOn != null ) { itemAggregateOn.setItems( aggregateOns ); if (
	 * aggregateOnSelect != null ) itemAggregateOn.select( getItemIndex(
	 * itemAggregateOn.getItems( ), aggregateOnSelect ) ); else
	 * itemAggregateOn.select( 0 ); } }
	 */
	private void initDataTypes() {
		if (dataTypes != null && itemType != null) {
			itemType.setItems(dataTypes);
			if (typeSelect != null)
				itemType.select(getItemIndex(itemType.getItems(), typeSelect));
			else
				itemType.select(0);
		}
	}

	private void initExpression() {
		if (expression != null && itemExpression != null)
			itemExpression.setText(expression);
	}

	private String name;

	private void initName() {
		if (name != null && itemName != null)
			itemName.setText(name);
	}

	protected void save() throws SemanticException {
		if (itemName.getText() != null && itemName.getText().trim().length() > 0) {

			if (bindingColumn == null) {
				if (itemExpression.getText() == null || itemExpression.getText().length() == 0) {
					return;
				}
				newBinding.setName(itemName.getText());
				newBinding.setDisplayName(itemDisplayName.getText());
				for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
					if (DATA_TYPE_CHOICES[i].getDisplayName().endsWith(itemType.getText())) {
						newBinding.setDataType(DATA_TYPE_CHOICES[i].getName());
						break;
					}
				}
				newBinding.setExpression(itemExpression.getText());
				/*
				 * if ( itemAggregateOn.isVisible( ) && !( itemAggregateOn.getText( ).equals(
				 * ALL ) || itemAggregateOn.getText( ) .equals( NONE ) ) ) {
				 * newBinding.setAggregateOn( itemAggregateOn.getText( ) ); } else
				 * newBinding.setAggregateOn( null );
				 */
				bindingColumn = DEUtil.addColumn(getBindingObject(), newBinding, isForceBinding());
			} else {
				if (itemExpression.getText() != null && itemExpression.getText().length() == 0) {
					DataItemHandle itemHandle = (DataItemHandle) getBindingObject();
					String resultSetName = itemHandle.getResultSetColumn();
					if (bindingColumn.getName().equals(resultSetName)) {
						itemHandle.setResultSetColumn(null);
					}
					itemHandle.getColumnBindings().removeItem(bindingColumn.getStructure());
					bindingColumn = null;
					return;
				}

				if (!(bindingColumn.getName() != null && bindingColumn.getName().equals(itemName.getText().trim())))
					bindingColumn.setName(itemName.getText());

				if (!(bindingColumn.getDisplayName() != null
						&& bindingColumn.getDisplayName().equals(itemDisplayName.getText().trim())))
					bindingColumn.setDisplayName(itemDisplayName.getText());

				for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
					if (DATA_TYPE_CHOICES[i].getDisplayName().endsWith(itemType.getText())) {
						bindingColumn.setDataType(DATA_TYPE_CHOICES[i].getName());
						break;
					}
				}

				bindingColumn.setExpression(itemExpression.getText());
				/*
				 * if ( itemAggregateOn.isVisible( ) && !( itemAggregateOn.getText( ).equals(
				 * ALL ) || itemAggregateOn.getText( ) .equals( NONE ) ) ) {
				 * bindingColumn.setAggregateOn( itemAggregateOn.getText( ) ); } else
				 * bindingColumn.setAggregateOn( null );
				 */
			}
		}
	}

	protected boolean isForceBinding() {
		return false;
	}

	protected void setResultSetColumn() throws SemanticException {
		if (input instanceof DataItemHandle) {
			((DataItemHandle) input).setResultSetColumn(bindingColumn.getName());
		}
	}

	protected void okPressed() {
		try {
			save();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
			return;
		}

		super.okPressed();
	}

	/*
	 * public void setAggregateOns( List aggregateOnList ) { if ( aggregateOnList ==
	 * null ) setAggregateOns( new String[0] ); String[] strings = new
	 * String[aggregateOnList.size( )]; for ( int i = 0; i < aggregateOnList.size(
	 * ); i++ ) { strings[i] = ( (GroupHandle) aggregateOnList.get( i ) ).getName(
	 * ); } setAggregateOns( strings ); }
	 * 
	 * public void setAggregateOns( String[] aggregateOns ) { if ( aggregateOns ==
	 * null || aggregateOns.length == 0 ) { if ( input != null &&
	 * DEUtil.getGroupControlType( input ) != DEUtil.TYPE_GROUP_NONE ) {
	 * aggregateOns = new String[]{ ALL }; } else aggregateOns = new String[]{ NONE
	 * }; this.aggregateOns = aggregateOns; } else { this.aggregateOns = new
	 * String[aggregateOns.length + 1]; this.aggregateOns[0] = ALL;
	 * System.arraycopy( aggregateOns, 0, this.aggregateOns, 1, aggregateOns.length
	 * ); } initAggregateOns( ); }
	 * 
	 * public void setAggregateOnSelect( String aggregateOnSelect ) {
	 * this.aggregateOnSelect = aggregateOnSelect; initAggregateOns( ); }
	 */
	protected DesignElementHandle getBindingObject() {
		return DEUtil.getBindingHolder(input);
	}

	public void setDataTypes(String[] dataTypes) {
		this.dataTypes = dataTypes;
		initDataTypes();
	}

	public void setExpression(String expression) {
		this.expression = expression;
		initExpression();
	}

	public void setName(String name) {
		this.name = name;
		initName();
	}

	private ComputedColumn newBinding;

	protected void createColumnName(ReportItemHandle input, String name) {
		newBinding = StructureFactory.newComputedColumn(input, name);
		setName(newBinding.getName());
	}

	public void setInput(ReportItemHandle input) {
		this.input = input;
		// setAggregateOns( DEUtil.getGroups( input ) );
		setDataTypes(ChoiceSetFactory.getDisplayNamefromChoiceSet(DATA_TYPE_CHOICE_SET));
		try {
			if (!isCreateNew) {
				bindingColumn = getInputBinding(input, ((DataItemHandle) input).getResultSetColumn());
			}
			if (isCreateNew) {
				createColumnName(input, DEFAULT_ITEM_NAME);
				setTypeSelect(dataTypes[0]);
			} else {
				if (bindingColumn != null) {
					setName(bindingColumn.getName());
					setDisplayName(bindingColumn.getDisplayName());
					setTypeSelect(DATA_TYPE_CHOICE_SET.findChoice(bindingColumn.getDataType()).getDisplayName());
					setExpression(bindingColumn.getExpression());
					// setAggregateOnSelect( bindingColumn.getAggregateOn( ) );
				} else {
					createColumnName(input, ((DataItemHandle) input).getResultSetColumn());
					setTypeSelect(dataTypes[0]);
				}
			}

		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	public void setTypeSelect(String typeSelect) {
		this.typeSelect = typeSelect;
		initDataTypes();
	}

	public String getName() {
		return itemName.getText().trim();
	}

	public String getExpression() {
		return itemExpression.getText();
	}

	/*
	 * public String getAggregateOnSelect( ) { return itemAggregateOn.getText( ); }
	 */
	public String getTypeSelect() {
		return itemType.getText();
	}

	ComputedColumnExpressionFilter filter;

	protected void handleExpressionButtonSelectEvent() {
		ExpressionBuilder expression = new ExpressionBuilder(getExpression());
		if (expressionProvider == null)
			expressionProvider = new BindingExpressionProvider(DEUtil.getBindingHolder(input), this.bindingColumn);
		if (bindingColumn != null) {
			if (filter != null)
				expressionProvider.removeFilter(filter);
			filter = new ComputedColumnExpressionFilter(bindingColumn);
			expressionProvider.addFilter(filter);
		}
		expression.setExpressionProvier(expressionProvider);

		if (expression.open() == OK) {
			setExpression(expression.getResult());
		}
	}

	protected ExpressionProvider expressionProvider;

	public void setExpressionProvider(ExpressionProvider provider) {
		expressionProvider = provider;
	}

	public ComputedColumnHandle getBindingColumn() {
		return bindingColumn;
	}

	protected void updateButtonStatus() {
		if (DataItemBindingDialog.this.getOkButton() != null) {
			if (itemName.getText() == null || itemName.getText().trim().equals("") //$NON-NLS-1$
					|| itemExpression.getText() == null || itemExpression.getText().trim().equals("")) //$NON-NLS-1$
				DataItemBindingDialog.this.getOkButton().setEnabled(false);
			else
				DataItemBindingDialog.this.getOkButton().setEnabled(true);
		}
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		updateButtonStatus();
	}
}
