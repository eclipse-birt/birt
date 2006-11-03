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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormWidgetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The sup-class of all attribute page, provides common register/unregister
 * implementation to DE model, and default refresh process after getting a
 * notify from DE.
 */
public abstract class AttributePage extends TabPage implements Listener
{

	/**
	 * A default quick button height which if different in win32 from other OS.
	 */
	public static final int QUICK_BUTTON_HEIGHT = Platform.getOS( )
			.equals( Platform.OS_WIN32 ) ? 20 : 22;

	/**
	 * The list kept Property & PropertyDescriptor pair.
	 */
	protected HashMap propertiesMap = new HashMap( 7 );

	protected List descriptorContainer = new ArrayList( );

	/**
	 * The current selection.
	 */
	protected Object input;


	public void refresh( )
	{
		if(this instanceof BindingPage)
		for ( int i = 0; i < descriptorContainer.size( ); i++ )
		{
			IPropertyDescriptor descriptor = (IPropertyDescriptor) descriptorContainer.get( i );
			descriptor.setInput( input );
			descriptor.load( );
		}

		Section[] sectionArray = getSections( );
		for ( int i = 0; i < sectionArray.length; i++ )
		{
			Section section = (Section) sectionArray[i];
			section.setInput( input );
			section.load( );
		}
		FormWidgetFactory.getInstance( ).paintFormStyle( container );
		FormWidgetFactory.getInstance( ).adapt( container );
	}

	public void setInput( Object handle )
	{
		deRegisterListeners( );
		input = handle;
		registerListeners( );
	}

	/**
	 * Removes model change listener.
	 */
	protected void deRegisterListeners( )
	{
		if ( input == null )
			return;
		for ( int i = 0; i < DEUtil.getInputSize( input ); i++ )
		{
			Object obj = DEUtil.getInputElement( input, i );
			if ( obj instanceof DesignElementHandle )
			{
				DesignElementHandle element = (DesignElementHandle) obj;
				element.removeListener( this );
			}
		}
	}

	/**
	 * Registers model change listener to DE elements.
	 */
	protected void registerListeners( )
	{
		if ( input == null )
			return;
		for ( int i = 0; i < DEUtil.getInputSize( input ); i++ )
		{
			Object obj = DEUtil.getInputElement( input, i );
			if ( obj instanceof DesignElementHandle )
			{
				DesignElementHandle element = (DesignElementHandle) obj;
				element.addListener( this );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.core.DesignElement,
	 *      org.eclipse.birt.model.activity.NotificationEvent) @note: For
	 *      structure property pages, subclass should override this method, as
	 *      well as refreshValues().
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		for ( int i = 0; i < descriptorContainer.size( ); i++ )
		{
			IPropertyDescriptor descriptor = (IPropertyDescriptor) descriptorContainer.get( i );
			descriptor.load( );
		}

		Section[] sectionArray = getSections( );
		for ( int i = 0; i < sectionArray.length; i++ )
		{
			Section section = (Section) sectionArray[i];
			section.load( );
		}
	}

	/*
	 * public void elementChanged( DesignElementHandle focus, NotificationEvent
	 * ev ) { int eventType = ev.getEventType( ); if ( eventType ==
	 * NotificationEvent.NAME_EVENT ) {
	 * refreshDescriptor(DesignElementHandle.NAME_PROP); } else if ( eventType ==
	 * NotificationEvent.PROPERTY_EVENT ) { PropertyEvent event =
	 * (PropertyEvent) ev; String propertyName = event.getPropertyName( ); if (
	 * StyleHandle.TEXT_ALIGN_PROP.equals( propertyName ) ) { propertyName =
	 * AttributeConstant.HORIZONTAL_ALIGN; } refreshDescriptor(propertyName); }
	 * else if ( eventType == NotificationEvent.STYLE_EVENT ) {
	 * refreshDescriptor( ReportItemHandle.STYLE_PROP ); } else if ( eventType ==
	 * NotificationEvent.THEME_EVENT) { refreshDescriptor(
	 * ModuleHandle.THEME_PROP ) ; } else { return; } }
	 * 
	 * private void refreshDescriptor(String property){ for(int i=0;i<descriptorContainer.size(
	 * );i++){ if(descriptorContainer.get( i ) instanceof
	 * TestTextPropertyDescriptor){ String descriptorProperty =
	 * ((TestTextPropertyDescriptor)descriptorContainer.get( i )).getProperty( );
	 * if(descriptorProperty!=null && descriptorProperty.equals( property
	 * ))((TestTextPropertyDescriptor)descriptorContainer.get( i )).load( ); } } }
	 */

	/*
	 * public void elementChanged( DesignElementHandle focus, NotificationEvent
	 * ev ) { int eventType = ev.getEventType( ); if ( eventType ==
	 * NotificationEvent.NAME_EVENT ) { if ( propertiesMap.containsKey(
	 * DesignElementHandle.NAME_PROP ) ) propertiesSet.add(
	 * DesignElementHandle.NAME_PROP ); } else if ( eventType ==
	 * NotificationEvent.PROPERTY_EVENT ) { PropertyEvent event =
	 * (PropertyEvent) ev; String propertyName = event.getPropertyName( ); if (
	 * StyleHandle.TEXT_ALIGN_PROP.equals( propertyName ) ) { propertyName =
	 * AttributeConstant.HORIZONTAL_ALIGN; } if ( propertiesMap.containsKey(
	 * propertyName ) ) propertiesSet.add( propertyName ); } else if ( eventType ==
	 * NotificationEvent.STYLE_EVENT ) { if ( propertiesMap.containsKey(
	 * ReportItemHandle.STYLE_PROP ) ) propertiesSet.add(
	 * ReportItemHandle.STYLE_PROP ); } else if ( eventType ==
	 * NotificationEvent.THEME_EVENT) { if ( propertiesMap.containsKey(
	 * ModuleHandle.THEME_PROP ) ) propertiesSet.add( ModuleHandle.THEME_PROP ); }
	 * else { return; } if ( refreshCount == input.size( ) - 1 ) { refreshCount =
	 * 0; refreshValues( propertiesSet ); } else { refreshCount++; } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose( )
	{
		if(container != null || !container.isDisposed( ))
			container.dispose( );
		deRegisterListeners( );
	}

	protected SortMap sections = new SortMap( );

	public void addSection( String sectionKey, Section section )
	{
		if ( sections == null )
		{
			sections = new SortMap( );
		}
		sections.put( sectionKey, section );
	}

	public void removeSection( String sectionKey )
	{
		if ( sections == null )
		{
			sections = new SortMap( );
		}
		sections.remove( sectionKey );
	}

	/**
	 * Adjust the layout of the field editors so that they are properly aligned.
	 */

	public void createSections( )
	{
		applyCustomSections();
		Section[] sectionArray = getSections( );
		for ( int i = 0; i < sectionArray.length; i++ )
		{
			Section section = (Section) sectionArray[i];
			section.createSection( );
		}
	}

	protected void applyCustomSections( )
	{
		
	}

	public void layoutSections( )
	{
		Section[] sectionArray = getSections( );
		for ( int i = 0; i < sectionArray.length; i++ )
		{
			Section section = (Section) sectionArray[i];
			section.layout( );
		}
	}

	public Section[] getSections( )
	{
		if ( sections == null )
		{
			return new Section[0];
		}
		Section[] sectionArray = new Section[sections.size( )];
		for ( int i = 0; i < sections.size( ); i++ )
		{
			sectionArray[i] = (Section) sections.getValue( i );
		}
		return sectionArray;
	}

	public Section getSection( String key )
	{
		if ( sections == null )
		{
			return null;
		}
		return (Section) sections.getValue( key );
	}

	public String getTabDisplayName( )
	{
		return null;
	}
	
	protected Composite container;
	
	public void buildUI(Composite parent){
		container = new Composite(parent,SWT.NONE);
		container.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				deRegisterListeners( );
			}
		} );
		if ( sections == null )
			sections = new SortMap( );	
	}
	
	public Control getControl( )
	{
		return container;
	}
}