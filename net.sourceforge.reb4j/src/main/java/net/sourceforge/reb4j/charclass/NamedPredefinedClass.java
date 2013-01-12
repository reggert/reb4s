package net.sourceforge.reb4j.charclass;

import fj.data.LazyString;

/**
 * Predefined character class that is accepted by name using \p{..} or \P{..}.
 */
public final class NamedPredefinedClass extends PredefinedClass
{
	private static final long serialVersionUID = 1L;
	public final String className;

	NamedPredefinedClass(final char nameChar, final String className)
	{
		super(nameChar);
		if (className == null) throw new NullPointerException("className");
		this.className = className;
	}
	
	NamedPredefinedClass(final String className)
	{
		super('p');
		if (className == null) throw new NullPointerException("className");
		this.className = className;
	}

	@Override
	public NamedPredefinedClass negated()
	{
		return new NamedPredefinedClass(invertedNameChar(), className);
	}

	@Override
	public LazyString unitableForm()
	{
		return super.unitableForm().append("{").append(className).append("}");
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + className.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		final NamedPredefinedClass other = (NamedPredefinedClass) obj;
		return className.equals(other.className);
	}

}
