/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.index;

import it.uniroma3.mat.extendedset.intset.ConciseSet;
import it.uniroma3.mat.extendedset.intset.IntSet;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * This class use IntSet iterator to enhance batch OR operations speed.
 * 
 * For Non-batch OR operation upon massive data, the IntSet's own OR operation should be used to optimize the performance.
 *  
 * @author lzhu
 *
 */
public class OrIntSetImpl implements IntSet
{
	private IntSet[] data;
	public OrIntSetImpl( IntSet[] data )
	{
		this.data = data;
		
	}
	
	public int compareTo(IntSet o) {
		throw new UnsupportedOperationException();
	}

	
	public IntSet intersection(IntSet other) {
		if( this.data.length == 1 )
		{
			return this.data[0].intersection( other );
		}
		
		IntIterator it = this.iterator();
		ConciseSet cs = new ConciseSet();
		
	
		while( it.hasNext() )
		{
			cs.add(it.next());
		}
	
		IntSet result =  cs.intersection( other );
	
		return result;
	}

	
	public IntSet union(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public IntSet difference(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public IntSet symmetricDifference(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public IntSet complemented() {
		throw new UnsupportedOperationException();
	}

	
	public void complement() {
		throw new UnsupportedOperationException();
	}

	
	public boolean containsAny(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public boolean containsAtLeast(IntSet other, int minElements) {
		throw new UnsupportedOperationException();
	}

	
	public int intersectionSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public int unionSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public int symmetricDifferenceSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public int differenceSize(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public int complementSize() {
		throw new UnsupportedOperationException();
	}

	
	public IntSet empty() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public double bitmapCompressionRatio() {
		throw new UnsupportedOperationException();
	}

	
	public double collectionCompressionRatio() {
		throw new UnsupportedOperationException();
	}

	
	public IntIterator iterator() {
		
		IntIterator[] its = new IntIterator[this.data.length];
		for( int i = 0; i < its.length; i++ )
		{
			its[i] = this.data[i].iterator( );
		}
		
		IntIterator[] result = getMergeNode( its );
		while( !(result.length == 1 || (result.length == 2 && result[1] == null)) )
		{
			result = getMergeNode( result );
		}
		IntIterator it =  result[0];
		if( it == null )
		{
			it = new ConciseSet().iterator( );
		}
		return it;
	}
	
	/**
	 * Create a merge join tree to ensure least number of comparing needed
	 * @param data
	 * @return
	 */
	private IntIterator[] getMergeNode( IntIterator[] data )
	{
		IntIterator[] result = new IntIterator[data.length/2+1];
		for( int i = 0; i < data.length; i++ )
		{
			IntIterator left = data[i];
			i++;
			if( i >= data.length || data[i] == null )
			{
				result[(i-1)/2] = left;
			}
			else
			{
				result[(i-1)/2] = new MergeNode( left,data[i]);
			}
		}
		return result;
	}
	
	
	
	public IntIterator descendingIterator() {
		throw new UnsupportedOperationException();
	}

	
	public String debugInfo() {
		throw new UnsupportedOperationException();
	}

	
	public void fill(int from, int to) {
		throw new UnsupportedOperationException();
	}

	
	public void clear(int from, int to) {
		throw new UnsupportedOperationException();
	}

	
	public void flip(int e) {
		throw new UnsupportedOperationException();
	}

	
	public int get(int i) {
		throw new UnsupportedOperationException();
	}

	
	public int indexOf(int e) {
		throw new UnsupportedOperationException();
	}

	
	public IntSet convert(int... a) {
		throw new UnsupportedOperationException();
	}

	
	public IntSet convert(Collection<Integer> c) {
		throw new UnsupportedOperationException();
	}

	
	public int first() {
		throw new UnsupportedOperationException();
	}

	
	public int last() {
		throw new UnsupportedOperationException();
	}

	
	public int size() {
		throw new UnsupportedOperationException();
	}

	
	public boolean isEmpty() {
		for( IntSet temp : this.data )
		{
			if( !temp.isEmpty())
				return false;
		}
		return true;
	}

	
	public boolean contains(int i) {
		throw new UnsupportedOperationException();
	}

	
	public boolean add(int i) {
		throw new UnsupportedOperationException();
	}

	
	public boolean remove(int i) {
		throw new UnsupportedOperationException();
	}

	
	public boolean containsAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	
	public boolean addAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	
	public boolean retainAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	
	public boolean removeAll(IntSet c) {
		throw new UnsupportedOperationException();
	}

	
	public void clear() {
		throw new UnsupportedOperationException();
		
	}
	
	public IntSet clone()
	{
		throw new UnsupportedOperationException();
	}

	
	public int[] toArray() {
		throw new UnsupportedOperationException();
	}

	
	public int[] toArray(int[] a) {
		throw new UnsupportedOperationException();
	}

	
	public List<? extends IntSet> powerSet() {
		throw new UnsupportedOperationException();
	}

	
	public List<? extends IntSet> powerSet(int min, int max) {
		throw new UnsupportedOperationException();
	}

	
	public int powerSetSize() {
		throw new UnsupportedOperationException();
	}

	
	public int powerSetSize(int min, int max) {
		throw new UnsupportedOperationException();
	}

	
	public double jaccardSimilarity(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public double jaccardDistance(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public double weightedJaccardSimilarity(IntSet other) {
		throw new UnsupportedOperationException();
	}

	
	public double weightedJaccardDistance(IntSet other) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Warning: Do not change the implementation of this class unless absolutely sure. Any change to it may lead to 
	 * serious performance regression.
	 * 
	 * After change this class, make sure that the BTree index performance test cases are run by Perf Team.
	 * 
	 * @author lzhu
	 *
	 */
	private class MergeNode implements IntIterator
	{
		private IntIterator left;
		private IntIterator right;
		private int leftValue;
		private int rightValue;
		private boolean notStart = true;
		
		public MergeNode( IntIterator left, IntIterator right )
		{
			this.left = left;
			this.right = right;

		}
		
		public int next( )
		{
			if( notStart )
			{
				leftValue = this.left.next();
				rightValue = this.right.next();
				notStart = false;
			}

			if ( this.leftValue > this.rightValue && right != null )
			{
				int result = this.rightValue;
				try
				{
					this.rightValue = right.next( );
				}
				catch ( NoSuchElementException e )
				{
					this.right = null;
					this.rightValue = Integer.MAX_VALUE;
				}
				return result;
			}
			
			if( left != null )
			{
				int result = this.leftValue;
				try
				{
					this.leftValue = left.next( );
				}
				catch ( NoSuchElementException e )
				{
					this.left = null;
					this.leftValue = Integer.MAX_VALUE;
				}
				return result;
			}	
			
			throw new NoSuchElementException();
		}
		
		

		/* (non-Javadoc)
		 * @see it.uniroma3.mat.extendedset.intset.IntSet.IntIterator#hasNext()
		 */
		
		public boolean hasNext( )
		{
			return !( (this.leftValue == this.rightValue) && (this.leftValue == Integer.MAX_VALUE) );
		}

		/* (non-Javadoc)
		 * @see it.uniroma3.mat.extendedset.intset.IntSet.IntIterator#remove()
		 */
		
		public void remove( )
		{
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see it.uniroma3.mat.extendedset.intset.IntSet.IntIterator#skipAllBefore(int)
		 */
		
		public void skipAllBefore( int element )
		{
			throw new UnsupportedOperationException();
		}

	}

}