package net.sourceforge.reb4j.charclass;

public interface Unitable extends CharacterClass
{
	Union union(Union right);
	Union union(Unitable right);
}