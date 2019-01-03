/**
 * GENERAL DIRECTIONS -
 *
 * 1. You may add private data fields and private methods as you see fit.
 * 2. Implement ALL the methods defined in the PriorityQueueADT interface.
 * 3. DO NOT change the name of the methods defined in the PriorityQueueADT interface.
 * 4. DO NOT edit the PriorityQueueADT interface.
 * 5. DO NOT implement a shadow array.
 */

public class MaxPQ<E extends Comparable<E>> implements PriorityQueueADT<E>
{
	//Internal variables for MaxPQ
    private E[] items;
    private static final int INITIAL_SIZE = 10;
    private int numItems;

    
    //Contructor
    public MaxPQ()
    {
        this.items = (E[]) new Comparable[INITIAL_SIZE];
        this.numItems = 0;

    }

	//Check if Priority Queue is empty or not. Returns true if empty and returns false if size() > 0
	public boolean isEmpty() {
		if (size() == 0)
		{
			return true;
		}
		return false;
	}

	
	public void insert(E item) 
	{
		//Expands array if numItems >= items.length
		if (numItems >= items.length)
		{
			items = expandArray(items);
		}
		
		//Increases numItems by 1
		numItems++;
		//Insert incoming item into array
		items[numItems] = item;
		
		//Reorganize the heap
		heapify(0);
	}

	@Override
	public E getMax() throws EmptyQueueException {
		//Returns the largest number in the heap array
		//Returns first element
		return items[1];
	}

	@Override
	public E removeMax() throws EmptyQueueException {
		//Removes the max element of the heap array
		E removed = getMax();
		//Sets the first element to the last element in the heap array
		items [1] = items [numItems];
		//Sets the last element of the heap to null
		items [numItems] = null;
		//Decreases the numItems by 1
		numItems--;
		//Heapify and rearrange the heap array
		heapify(1);
		
		return removed;
	}

	@Override
	public int size() {
		//FIXME: PRINT HEAP ARRAY

				
		//Returns numItems
		return numItems;
	}
	
	//Method that takes the oldArray as a parameter and doubles its size
	private E[] expandArray(E [] old)
	{
		//Creates new array that is twice the size of the old array
		E [] newArray = (E[]) new Comparable [old.length * 2];
		
		//Loops for all elements in the heap array
		for (int i = 1; i <= old.length; i++)
		{
			//Copies all the elements from the old array to the new array
			newArray [i] = old [i];
		}
		
		return newArray;
	}
	
	//Reorganize the internal array of the max prority queue
	//Mode 0 : starts from bottom of heap array and moves up
	//Mode 1 : stars from top of heap array and moves down
	private void heapify (int mode)
	{
		boolean heapified = false;
		
		
		if (mode == 0) 
		{
			int tempPos = numItems;
		//Loop until correct spot is found or node reaches top of the heap array
			while (!heapified)
			{
				// Exits loop if no parent exists
				if (tempPos <= 1) {
					return;
				}
			//If child is larger than parent, then swap positions of the two
				if (items[tempPos].compareTo(items[tempPos/2]) > 0)
				{	
					//Set temp to child element
					E temp = items[tempPos];
					//Set child element to parent element
					items[tempPos] = items[tempPos/2];
					//Set parent element to temp
					items[tempPos/2] = temp;
				
					//Change tempPos to pos of the parent
					tempPos = tempPos/2;
				}
				else
				{
					//Sets heapified to true and exits while loop
					heapified = true;
				}
			}
		}
		
		if (mode == 1)
		{
			int tempPos = 1;
			int maxChildPos = 0;
			//Compare the parent to its children and swap with the larger child
			while (!heapified)
			{
				
				
				//Exits loop if no children exists
				if (items[tempPos*2] == null && items[tempPos*2 + 1] == null)
				{
					return;
				}
				
				if (tempPos*2 >= numItems)
				{
					maxChildPos = tempPos * 2;
				}
				//Picks the bigger element to be the maxChild
				else if (items[tempPos*2].compareTo(items[tempPos*2 + 1]) > 0)
				{
					maxChildPos = tempPos * 2;
				}
				else
				{
					maxChildPos = tempPos * 2 + 1;
				}
				
				//Swap positions of child and parent
				if (items[tempPos].compareTo(items[maxChildPos]) < 0)
				{	
					//Set temp to child element
					E temp = items[tempPos];
					//Set child element to parent element
					items[tempPos] = items[maxChildPos];
					//Set parent element to temp
					items[maxChildPos] = temp;
				
					//Change tempPos to pos of the parent
					tempPos = maxChildPos;
				}
				//Else return heapified becomes true and returns
				else
				{
					heapified = true;
				}
				
				
			}
		}
	}
}
