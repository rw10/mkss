package org.se.lab.metamodel;

import java.util.ArrayList;
import java.util.List;

public class MInterface
	extends MType
{
	/*
	 * Constructor
	 */
	public MInterface(String name)
	{
		super(name);
	}
	
	/*
	 * Reference: ---[*]-> MOperation
	 */
	List<MOperation> operations = new ArrayList<MOperation>();
	public List<MOperation> getOperations()
	{
		return operations;
	}
	public void setOperations(List<MOperation> operations)
	{
		this.operations = operations;
	}
}
