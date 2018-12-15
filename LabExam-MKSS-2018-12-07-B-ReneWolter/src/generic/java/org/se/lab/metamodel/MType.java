package org.se.lab.metamodel;

import java.util.ArrayList;
import java.util.List;

public class MType
	extends MNamedElement
{
	/*
	 * Constructor
	 */
	public MType(String name)
	{
		super(name);
	}


	/*
	 * Reference: ---[*]-> MTypeModifier
	 */
	List<MTypeModifier> modifiers = new ArrayList<>();
	public List<MTypeModifier> getModifiers()
	{
		return modifiers;
	}
	public void setModifiers(List<MTypeModifier> modifiers)
	{
		this.modifiers = modifiers;
	}
}
