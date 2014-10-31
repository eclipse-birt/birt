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

package org.eclipse.birt.report.model.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ThemeStyleNameValidator;
import org.eclipse.birt.report.model.command.ContentElementInfo.Step;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.ContentElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.strategy.TabularDimensionPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Renames a design element.
 * 
 */

public class NameCommand extends AbstractElementCommand
{

	// ident {nmstart}{nmchar}*
	// h [0-9a-f]
	// nonascii [^\0-\177]
	// unicode \\{h}{1,6}[ \t\r\n\f]?
	// escape {unicode}|\\[ -~\200-\4177777]
	// nmstart [a-z]|{nonascii}|{escape}
	// nmchar [a-z0-9-]|{nonascii}|{escape}
	private static final String STYLE_NAME_PATTERN = "([a-z]|[^\0-\177]|((\\[0-9a-f]{1,6}[ \n\r\t\f]?)|\\[ -~\200-\4177777]))([a-z0-9-_]|[^\0-\177]|((\\[0-9a-f]{1,6}[ \n\r\t\f]?)|\\[ -~\200-\4177777]))*"; //$NON-NLS-1$
	public static final Pattern styleNamePattern = Pattern.compile(
			STYLE_NAME_PATTERN, Pattern.CASE_INSENSITIVE );

	private ContentElementInfo eventTarget;
	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param obj
	 *            the element to modify.
	 */

	public NameCommand( Module module, DesignElement obj )
	{
		super( module, obj );
	}

	/**
	 * Sets the element name.
	 * 
	 * @param name
	 *            the new name.
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	public void setName( String name ) throws NameException
	{
		name = StringUtil.trimString( name );

		// Ignore change to the current name.

		String oldName = element.getName( );
		if ( ( name == null && oldName == null )
				|| ( name != null && oldName != null && name.equals( oldName ) ) )
			return;

		// ignore change to the dimension that refers a shared dimension
		Dimension sharedDimension = TabularDimensionPropSearchStrategy
				.getSharedDimension( module, element );
		if ( sharedDimension != null
				&& !sharedDimension.getName( ).equals( name ) )
			return;

		checkName( name );

		DesignElement tmpElement = element;
		// if a contentElement is not local, need to make a local copy before changing the name
		if ( tmpElement instanceof ContentElement && !( (ContentElement) tmpElement).isLocal( ) )
		{
			eventTarget = ( ( ContentElement ) tmpElement ).getValueContainer( );
			tmpElement = copyTopCompositeValue( );
		}
		
		// Record the change.

		ActivityStack stack = getActivityStack( );
		NameRecord rename = new NameRecord( tmpElement, name );
		stack.startTrans( rename.getLabel( ) );

		// Drop the old name from the name space.

		// Change the name.
		stack.execute( rename );

		// Add the new name to the name space.
		renameSymbolFrom( oldName );

		// change the name of the dimension that shares this element
		if ( element instanceof Dimension )
		{
			updateDimensions( stack );
		}

		stack.commit( );
	}

	private void updateDimensions( ActivityStack stack )
	{
		Dimension dimension = (Dimension) element;
		List<BackRef> clients = dimension.getClientList( );
		for ( BackRef client : clients )
		{
			DesignElement content = client.getElement( );
			String propName = client.getPropertyName( );
			if ( content instanceof Dimension
					&& ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP
							.equals( propName ) )
			{
				NameRecord rename = new NameRecord( content, element.getName( ) );
				stack.execute( rename );
			}
		}
	}

	/**
	 * Checks the current element name. Done when adding a newly created element
	 * where the element name is already set on the new element.
	 * 
	 * @param name
	 *            the name to check.
	 * @throws NameException
	 *             if the element name is not allowed to change.
	 */

	public void checkName( String name ) throws NameException
	{
		ElementDefn metaData = (ElementDefn) element.getDefn( );
		if ( name == null )
		{
			if ( !element.isManagedByNameSpace( ) )
			{
				return;
			}

			// Cannot clear the name when there are references. It would leave
			// the dependents with no way to identify this element.

			if ( element.hasDerived( ) || element.hasReferences( ) )
				throw new NameException( element, null,
						NameException.DESIGN_EXCEPTION_HAS_REFERENCES );

			// Cannot clear the name of an element when the name is required.

			if ( metaData.getNameOption( ) == MetaDataConstants.REQUIRED_NAME )
				throw new NameException( element, null,
						NameException.DESIGN_EXCEPTION_NAME_REQUIRED );

			if ( ( module instanceof Library )
					&& ( element.getContainer( ) instanceof Library ) )
			{
				throw new NameException( element, null,
						NameException.DESIGN_EXCEPTION_NAME_REQUIRED );
			}
		}
		else
		{

			PropertyDefn propDefn = (PropertyDefn) metaData
					.getProperty( IDesignElementModel.NAME_PROP );

			// no name is defined in this element
			if ( propDefn == null )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN );

			try
			{
				name = (String) propDefn.validateValue( module, element, name );
			}
			catch ( PropertyValueException e )
			{
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_INVALID_NAME );
			}

			if ( element instanceof StyleElement )
			{
				if ( !styleNamePattern.matcher( name ).matches( ) )
					throw new NameException( element, name,
							NameException.DESIGN_EXCEPTION_INVALID_STYLE_NAME );
			}

			// if it is a style in the theme, no need to check duplicate names.
			// In the library, style names can be duplicate.
			if ( !isNameValidInContext( name ) )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_DUPLICATE );

			// Cannot set the name of an element when the name is not allowed.

			if ( metaData.getNameOption( ) == MetaDataConstants.NO_NAME )
				//the element has name without namespace
				return;
//				throw new NameException( element, name,
//						NameException.DESIGN_EXCEPTION_NAME_FORBIDDEN );

			// if the element is a pending node and not in any module, or it is
			// in a slot that is not managed by namespace, then we need not
			// check whether the name is duplicate
			if ( !element.isManagedByNameSpace( ) )
			{
				return;
			}

			// first found the element with the given name. Since the library
			// has it own namespace -- prefix, the range of name check should be
			// in the current module.
			NameExecutor executor = new NameExecutor( module, element );
			DesignElement existedElement = executor.getElement( name );

			// if the element is null, then the name is OK. Now, the name of the
			// element is inserted into the namespace only if the element is in
			// a design tree(module) and the slot it exists is managed by
			// namespace

			if ( existedElement != null )
				throw new NameException( element, name,
						NameException.DESIGN_EXCEPTION_DUPLICATE );

		}
	}

	/**
	 * Adds the element into its name space.
	 * 
	 * @throws NameException
	 *             if the element with the same name exists.
	 */

	protected void addElement( ) throws NameException
	{
		checkName( element.getName( ) );
		addSymbol( );
	}

	/**
	 * Drops the element from its name space.
	 */

	protected void dropElement( )
	{
		dropSymbol( );
	}

	/**
	 * Implementation of adding a symbol to a name space. Adds the name only if
	 * it is not null and it has container. This means that we can modify the
	 * element name as will if it's not attached on other elements, and the name
	 * is saved in name space only after the element is added to a slot of the
	 * container.
	 */

	private void addSymbol( )
	{
		String name = element.getName( );
		if ( name == null )
			return;

		// add a style into theme
		DesignElement container = element.getContainer( );
		if ( container instanceof Theme )
		{
			( (Theme) container ).dropCachedName( name );
			return;
		}

		if ( !element.isManagedByNameSpace( ) )
			return;

		assert element.getRoot( ) != null;

		// if the element has been in the name space, that is, the element
		// is added to another element through handles but the outermost
		// compound element is not in the design tree, then do not insert
		// the element to the name space again.
		NameExecutor executor = new NameExecutor( module, element );
		if ( executor.hasNamespace( ) )
		{
			DesignElement existedElement = executor.getElement( name );
			assert existedElement == null;
			getActivityStack( ).execute(
					new NameSpaceRecord( executor.getNameHelper( ), executor
							.getNameSpaceId( ), element, true ) );
		}
	}

	/**
	 * Implementation of dropping a symbol from a name space. No need to do the
	 * drop if the current name is null.
	 */

	private void dropSymbol( )
	{
		if ( element.getName( ) == null || !element.isManagedByNameSpace( ) )
			return;
		NameExecutor executor = new NameExecutor( module, element );
		INameHelper nameHelper = executor.getNameHelper( );
		if ( nameHelper != null )
		{
			String ns = executor.getNameSpaceId( );
			NameSpace namespace = executor.getNameSpace( );
			if ( namespace.getElement( element.getName( ) ) != element )
				return;
			getActivityStack( ).execute(
					new NameSpaceRecord( nameHelper, ns, element, false ) );
		}
	}

	/**
	 * Renames the namespace when call {@link #setName(String)}.
	 * 
	 * @param oldName
	 *            the old name of the element
	 */

	private void renameSymbolFrom( String oldName )
	{
		// only the slot the element to add or exist need to handle some
		// namespace issue, we will do some replace operations for namespace; if
		// not, we will not handle

		if ( element.isManagedByNameSpace( ) )
		{
			RenameInNameSpaceRecord record = new RenameInNameSpaceRecord(
					module, element, oldName, element.getName( ) );
			getActivityStack( ).execute( record );
		}
	}

	/**
	 * Checks whether the name is valid in the context.
	 * 
	 * @param name
	 *            the new name
	 * @return <code>true</code> if the name is valid. Otherwise
	 *         <code>false</code>.
	 */

	private boolean isNameValidInContext( String name )
	{
		if ( element instanceof Style )
		{
			DesignElement tmpContainer = element.getContainer( );
			if ( tmpContainer instanceof AbstractTheme )
			{
				List<SemanticException> errors = ThemeStyleNameValidator
						.getInstance( )
						.validateForRenamingStyle(
								(AbstractThemeHandle) tmpContainer.getHandle( module ),
								(StyleHandle) element.getHandle( module ), name );
				if ( !errors.isEmpty( ) )
					return false;
			}
		}

		return true;
	}

	public void checkDimension( ) throws SemanticException
	{
		if ( element instanceof TabularDimension )
		{
			Dimension sharedDimension = TabularDimensionPropSearchStrategy
					.getSharedDimension( module, element );
			if ( sharedDimension != null )
			{
				String name = (String) element.getName( );
				String sharedName = sharedDimension.getName( );

				if ( !sharedName.equals( name ) )
				{
					NameExecutor nameExecutor = new NameExecutor( module,
							element );
					INameHelper nameHelper = nameExecutor.getNameHelper( );
					NameSpace namespace = nameExecutor.getNameSpace( );
					String namespaceId = nameExecutor.getNameSpaceId( );
					DesignElement existedElement = namespace.getElement( name );
					if ( existedElement == null )
					{
						// the name is not put in name space, then simply rename
						// it to the name of the shared element
						setName( sharedName );
					}
					else
					{
						if ( existedElement == element )
						{
							// remove it from the name space and then rename it
							getActivityStack( ).execute(
									new NameSpaceRecord( nameHelper, namespaceId, element,
											false ) );
							setName( sharedName );
						}
						else
						{
							// do nothing and simply rename it to the name of
							// the shared element
							setName( sharedName );
						}
					}
				}
			}
		}
	}
	
	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value,
	 * if it has, the method returns, otherwise, a copy of the list value
	 * inherited from container or parent will be set locally on the element
	 * itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element.
	 * This method will be called to ensure that a local copy will be made, so
	 * change to the child won't affect the original value in the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	private DesignElement makeLocalCompositeValue( DesignElement topElement,
			ElementPropertyDefn prop, DesignElement content )
	{
		// Top level property is a list.

		Object localValue = topElement.getLocalProperty( module, prop );

		if ( localValue != null )
			return content;

		// Make a local copy of the inherited list value.

		Object inherited = topElement.getProperty( module, prop );

		// if the action is add, the inherited can be null.

		if ( inherited == null )
			return null;

		int index = -1;

		if ( content != null && inherited instanceof List )
			index = ( (List) inherited ).indexOf( content );

		Object newValue = ModelUtil.copyValue( prop, inherited );
		ActivityStack activityStack = module.getActivityStack( );

		ContainerContext context = new ContainerContext( topElement, prop
				.getName( ) );

		if ( newValue instanceof List )
		{
			List list = new ArrayList( );
			PropertyRecord propRecord = new PropertyRecord( topElement, prop,
					list );
			activityStack.execute( propRecord );

			list = (List) newValue;
			for ( int i = 0; i < list.size( ); i++ )
			{
				DesignElement tmpContent = (DesignElement) list.get( i );
				ContentRecord addRecord = new ContentRecord( module, context,
						tmpContent, i );
				activityStack.execute( addRecord );
			}
		}
		else
		{
			PropertyRecord propRecord = new PropertyRecord( topElement, prop,
					newValue );
			activityStack.execute( propRecord );
		}

		if ( index != -1 )
			return (DesignElement) ( (List) newValue ).get( index );

		return content;
	}
	
	/**
	 * The property is a simple value list. If property is a list property, the
	 * method will check to see if the current element has the local list value,
	 * if it has, the method returns, otherwise, a copy of the list value
	 * inherited from container or parent will be set locally on the element
	 * itself.
	 * <p>
	 * This method is supposed to be used when we need to change the value of a
	 * composite property( a simple list property ). These kind of property is
	 * inherited as a whole, so when the value changed from a child element.
	 * This method will be called to ensure that a local copy will be made, so
	 * change to the child won't affect the original value in the parent.
	 * 
	 * @param ref
	 *            a reference to a list property or member.
	 */

	private DesignElement copyTopCompositeValue( )
	{
		if ( !( element instanceof ContentElement ) )
		{
			return null;
		}

		DesignElement topElement = eventTarget.getElement( );
		String propName = eventTarget.getPropName( );
		ElementPropertyDefn prop = topElement.getPropertyDefn( propName );

		makeLocalCompositeValue( topElement, prop, null );

		return matchElement( topElement );
	}

	private DesignElement matchElement( DesignElement topElement )
	{
		List<Step> steps = eventTarget.stepIterator( );

		DesignElement tmpElement = topElement;
		for ( int i = steps.size( ) - 1; i >= 0; i-- )
		{
			Step step = steps.get( i );
			PropertyDefn stepPropDefn = step.stepPropDefn;
			int index = step.index;

			Object stepValue = tmpElement.getLocalProperty( module,
					(ElementPropertyDefn) stepPropDefn );

			if ( stepPropDefn.isListType( ) )
			{
				tmpElement = (DesignElement) ( (List) stepValue ).get( index );
			}
			else
				tmpElement = (DesignElement) stepValue;
		}

		return tmpElement;
	}
}
