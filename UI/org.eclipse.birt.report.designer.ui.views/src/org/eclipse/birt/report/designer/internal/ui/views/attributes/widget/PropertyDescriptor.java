
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.widgets.Control;

public abstract class PropertyDescriptor implements IPropertyDescriptor {

	protected Control control;

	protected Object input;

	protected List descriptorContainer = new ArrayList();

	private boolean formStyle = true;

	public IPropertyDescriptor[] getChildren() {
		IPropertyDescriptor[] children = new IPropertyDescriptor[0];
		descriptorContainer.toArray(children);
		return children;
	}

	/*
	 * public void setDescriptorProvider(PropertyDescriptorProvider provider){
	 * this.descriptorProvider = provider; }
	 */

	public Control getControl() {
		return control;
	}

	/*
	 * public String getDefaultUnit( ) { if ( defaultUnit != null ) return
	 * defaultUnit; if ( elements.getElements( ) == null || elements.getElements(
	 * ).size( ) == 0 ) { return null; } String unit = null; if (
	 * !elements.isSameType( ) ) { return null; } DesignElementHandle handle =
	 * (DesignElementHandle) elements.getElements( ) .get( 0 ); unit =
	 * handle.getPropertyHandle( property ).getDefaultUnit( ); return unit; }
	 */

	/*
	 * public String getMeasureValue( ) { String value = getStringValue( ); if (
	 * value == null || value.equals( "" ) ) //$NON-NLS-1$ return value; try {
	 * DimensionValue dimensionValue = DimensionValue.parse( value ); return
	 * StringUtil.doubleToString( dimensionValue.getMeasure( ), 3 ); } catch (
	 * PropertyValueException e ) { ExceptionUtil.handle( e ); } return "";
	 * //$NON-NLS-1$ }
	 */

	public Object getInput() {
		return input;
	}

	/*
	 * private GroupElementHandle getMultiSelectionHandle( List modelList ) { return
	 * DEUtil.getMultiSelectionHandle( modelList ); }
	 */

	/*
	 * public String getUnit( ) { String value = getStringValue( );
	 * 
	 * if ( value == null || value.equals( "" ) ) //$NON-NLS-1$ return value; try {
	 * DimensionValue dimensionValue = DimensionValue.parse( value ); return
	 * dimensionValue.getUnits( ); } catch ( PropertyValueException e ) {
	 * ExceptionUtil.handle( e ); } return ""; //$NON-NLS-1$ }
	 */

	/*
	 * public void load( ) { String value = descriptorProvider.load( ).toString( );
	 * if ( value != null ) refresh( value ); }
	 */

	public void setInput(Object handle) {
		this.input = handle;
	}

	// abstract void refresh( String value );

	// ������Provider������Load��Save��
	/*
	 * public void save( Object value ) throws SemanticException {
	 * descriptorProvider.save( value ); }
	 */

	public boolean isFormStyle() {
		return formStyle;
	}

	public void setFormStyle(boolean formStyle) {
		this.formStyle = formStyle;
	}

	IDescriptorProvider descriptorProvider;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		this.descriptorProvider = provider;
	}

	public IDescriptorProvider getDescriptorProvider() {
		return descriptorProvider;
	}

	public void reset() {
		if (descriptorProvider != null && descriptorProvider.canReset()) {
			try {
				descriptorProvider.reset();
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
	}
}
