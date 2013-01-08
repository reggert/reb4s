package net.sourceforge.reb4j;

public final class StringLiteral extends Literal
{
	private static final long serialVersionUID = 1L;
	private final String unescaped;
	
	StringLiteral(final String unescaped)
	{
		if (unescaped == null) throw new NullPointerException("unescaped");
		this.unescaped = unescaped;
	}

	@Override
	public String unescaped()
	{
		return unescaped;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + unescaped.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StringLiteral other = (StringLiteral) obj;
		return unescaped.equals(other.unescaped);
	}

}
