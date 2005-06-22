/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.util.mediator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;

/**
 * Mediator calss to control the interactive between different views.
 * This class is used for selection sychronization and other tasks.
 */
public class ReportMediator
{

	private boolean isDispatching = false;
	private List listeners = new ArrayList( );
	private List stack = new ArrayList( );
	private int stackPointer = 0;
	private ReportMediatorState currentState = new ReportMediatorState();
	
	//suport the globol colleague
	private static List globalListener = new ArrayList();

	
	public void addGlobalColleague(IColleague colleague )
	{
		if ( !globalListener.contains( colleague ) )
		{
			globalListener.add( colleague );
		}
	}
	/**
	 * Add a colleague to mediator.
	 * @param colleague
	 */
	public void addColleague( IColleague colleague )
	{
		if ( !listeners.contains( colleague ) )
		{
			listeners.add( colleague );
		}
	}

	/**
	 * Remove colleagure from mediator.
	 * @param colleague
	 */
	public void removeColleague( IColleague colleague )
	{
		listeners.remove( colleague );
	}
	
	/**
	 * Remove colleagure from mediator.
	 * @param colleague
	 */
	public void removeGlobalColleague( IColleague colleague )
	{
		globalListener.remove( colleague );
	}

	/**
	 * Send a request to mediator. 
	 * Mediator handle and dispatch this request to colleaues. 
	 * @param request
	 */
	public void notifyRequest( ReportRequest request )
	{
		if ( isDispatching )
			return;
		isDispatching = true;
		if (isInterestRequest(request))
		{
			currentState.copyFrom(convertRequestToState(request));
		}
		int size = listeners.size( );
		for ( int i = 0; i < size; i++ )
		{
			IColleague colleague = (IColleague) listeners.get( i );
			colleague.performRequest( request );
		}
		
		size = globalListener.size();
		for ( int i = 0; i < size; i++ )
		{
			IColleague colleague = (IColleague) globalListener.get( i );
			colleague.performRequest( request );
		}
		isDispatching = false;
	}

	private boolean isInterestRequest(ReportRequest request)
	{
		return ReportRequest.SELECTION.equals(request.getType());
	}
	
	/**
	 * Dispose mediator. 
	 */
	public void dispose( )
	{
		currentState = null ;
		listeners.clear();
		stackPointer = 0;
		stack = null;
	}

	
	/**
	 * Return top state in stack.
	 */
	public void popState( )
	{
		stackPointer--;
		if (stackPointer != 0)
		{
			restoreState( (ReportMediatorState) stack.get( stackPointer ) );
		}
		if (stackPointer == 0)
		{
			stack.clear();
		}
	}

	/**Gets the current state
	 * @return
	 */
	public IMediatorState getCurrentState()
	{
		return currentState;
	}
	/**
	 * Push state of colleague, which send the notification, into stack.
	 */
	public void pushState( )
	{
		try
		{
			ReportMediatorState s;
			if ( stack.size( ) > stackPointer )
			{
				s = (ReportMediatorState) stack.get( stackPointer );
				s.copyFrom( currentState );
			}
			else
			{
				stack.add( currentState.clone( ) );
			}
			stackPointer++;
		}
		catch ( CloneNotSupportedException e )
		{
			throw new RuntimeException( e.getMessage( ) );
		}
	}

	
	private ReportMediatorState convertRequestToState(ReportRequest request)
	{
		ReportMediatorState retValue = new ReportMediatorState();
		retValue.setSource(request.getSource());
		retValue.setSelectiobObject(request.getSelectionModelList());
		return retValue;
	}
	
	private ReportRequest convertStateToRequest(ReportMediatorState s)
	{
		ReportRequest request = new ReportRequest();
		request.setSource(s.getSource());
		request.setSelectionObject(s.getSelectionObject());
		return request;
	}
	/**
	 * Restore previous state and discard the top one.
	 */
	public void restoreState( )
	{
		restoreState( (ReportMediatorState) stack.get( stackPointer - 1 ) );
	}

	/**
	 * Sets all State information to that of the given State, called by
	 * restoreState()
	 * 
	 * @param s
	 *            the State
	 */
	protected void restoreState( ReportMediatorState s )
	{
		currentState.copyFrom(s);
		ReportRequest request = convertStateToRequest(s);
		notifyRequest(request);
	}
	
	

	/** Contains the state variables of this SWTGraphics object * */
	protected static class ReportMediatorState implements Cloneable, IMediatorState
	{

		private List selectiobObject = new ArrayList( );
		private Object source;

		/** @see Object#clone() * */
		public Object clone( ) throws CloneNotSupportedException
		{
			ReportMediatorState state = new ReportMediatorState( );
			state.setSelectiobObject( getSelectionObject( ) );
			return state;
		}

		/**
		 * Copies all state information from the given State to this State
		 * 
		 * @param state
		 *            The State to copy from
		 */
		protected void copyFrom( ReportMediatorState state )
		{
			setSelectiobObject( state.getSelectionObject( ) );
			setSource(state.getSource());
		}

		/**
		 * @return Returns the selectiobObject.
		 */
		public List getSelectionObject( )
		{
			return selectiobObject;
		}

		/**
		 * @param selectiobObject
		 *            The selectiobObject to set.
		 */
		protected void setSelectiobObject( List selectiobObject )
		{
			this.selectiobObject = selectiobObject;
		}

		/**
		 * @return Returns the source.
		 */
		public Object getSource( )
		{
			return source;
		}

		/**
		 * @param source
		 *            The source to set.
		 */
		protected void setSource( Object source )
		{
			this.source = source;
		}
	}
}