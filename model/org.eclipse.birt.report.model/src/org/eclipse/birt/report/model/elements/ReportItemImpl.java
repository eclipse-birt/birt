package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ContentIterator;

public abstract class ReportItemImpl extends ReferencableStyledElement
		implements
		IReportItemModel,
		ISupportThemeElement
{

	public ReportItemImpl() {
		super();
	}

	public ReportItemImpl(String theName) {
		super(theName);
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module
	 *            the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */
	public DesignElement getDataSetElement(Module module) {
		ElementRefValue dataSetRef = (ElementRefValue) getProperty( module,
				DATA_SET_PROP );
		if ( dataSetRef == null )
			return null;
		return dataSetRef.getElement( );
	}

	/**
	 * Returns the cube element, if any, for this element.
	 * 
	 * @param module
	 *            the report design of the report item
	 * 
	 * @return the cube element defined on this specific element
	 */
	public DesignElement getCubeElement(Module module) {
		ElementRefValue cubeRef = (ElementRefValue) getProperty( module,
				CUBE_PROP );
		if ( cubeRef == null )
			return null;
		return cubeRef.getElement( );
	}

	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate( module );
	
		// Check the element reference of dataSet property
	
		list.addAll( ElementReferenceValidator.getInstance( ).validate( module,
				this, DATA_SET_PROP ) );
	
		list.addAll( validateStructureList( module, PARAM_BINDINGS_PROP ) );
	
		return list;
	}

	/**
	 * Checks whether the listing element refers to another listing element.
	 * 
	 * @param module
	 *            the root of the listing element
	 * @return <code>true</code> if the listing element shares data with other
	 *         listing element. Otherwise <code>false</code>.
	 */
	public boolean isDataBindingReferring(Module module) {
		ElementRefValue refValue = (ElementRefValue) getLocalProperty( module,
				IReportItemModel.DATA_BINDING_REF_PROP );
		if ( refValue == null || !refValue.isResolved( ) )
			return false;
	
		return true;
	}

	public Object getProperty(Module module, ElementPropertyDefn prop) {
	
		String propName = prop.getName( );
		if ( IReportItemModel.CASCADE_ACL_PROP.equals( propName )
				&& ( !getDefn( ).isContainer( ) ) )
		{
			return false;
		}
		return super.getProperty( module, prop );
	}

	/**
	 * Determines whether this report item can cascade ACL or not. True if and
	 * only if this item has define
	 * <code>IReportItemModel.CASCADE_ACL_PROP</code> property and it is a
	 * container.
	 * 
	 * @return true if this item has define
	 *         <code>IReportItemModel.CASCADE_ACL_PROP</code> property and it is
	 *         a container, otherwise false
	 */
	public boolean canCascadeACL() {
		if ( getPropertyDefn( IReportItemModel.CASCADE_ACL_PROP ) != null
				&& getDefn( ).isContainer( ) )
			return true;
		return false;
	}

	/**
	 * Caches values for the element. The caller must be the report design.
	 */
	public void cacheValues() {
	
	}

	public void checkExtends(DesignElement parent) throws ExtendsException {
		super.checkExtends( parent );
		Module lib = parent.getRoot( );
		checkDataBindingReferring( lib, parent );
	
		ContentIterator iter = new ContentIterator( lib, parent );
		while ( iter.hasNext( ) )
		{
			DesignElement element = iter.next( );
			checkDataBindingReferring( lib, element );
		}
	
	}

	/**
	 * Checks whether the report item refers to another report item.
	 * 
	 * @param lib
	 *            the library.
	 * @param element
	 *            the design element.
	 * @throws ExtendsException
	 *             if the listing element shares data with other listing
	 *             element.
	 */
	private void checkDataBindingReferring(Module lib, DesignElement element)
			throws ExtendsException {
				if ( element instanceof ReportItem
						&& ( (ReportItem) element ).isDataBindingReferring( lib ) )
				{
					throw new ExtendsForbiddenException(
							null,
							element,
							ExtendsForbiddenException.DESIGN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND );
				}
			}

	/**
	 * 
	 * @param module
	 * @return
	 */
	public AbstractTheme getTheme(Module module) {
		ElementRefValue value = (ElementRefValue) getProperty( module,
				THEME_PROP );
		if ( value != null )
			return (ReportItemTheme) value.getElement( );
		return getDefaultTheme( module );
	}
	
	/**
	 * get the default theme of report item
	 * 
	 * return the one defined in report level. report level item theme is defined as:
	 * 
	 * theme_type '-' theme_name
	 * 
	 * for example, if report level theme is defined as "theme_1", the table's
	 * default theme is "Table-theme_1"
	 * 
	 * @param module
	 *            design module
	 * @return
	 */
	private AbstractTheme getDefaultTheme( Module module )
	{
		String themeType = MetaDataDictionary.getInstance( ).getThemeType(
				this.getDefn( ) );
		if ( themeType == null )
		{
			return null;
		}
		Theme reportTheme = module.getTheme( );
		if ( reportTheme != null )
		{
			String reportThemeName = reportTheme.getName( );
			return reportTheme.getRoot( ).findReportItemTheme(
					themeType + '-' + reportThemeName );
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public AbstractTheme getTheme() {
		return getTheme( getRoot( ) );
	}

	/**
	 * 
	 * @return
	 */
	public String getThemeName() {
		return getStringProperty( getRoot( ), THEME_PROP );
	}

}