/*
	Copyright 2012 by Richard W. Eggert II.
 
	This file is part of the reb4j library.
  
	reb4j is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	reb4j is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public License
	along with reb4j.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.reb4j;

/**
 * Enumerates the types of quantifiers supported by 
 * {@link java.util.regex.Pattern}.
 * 
 * As per the documentation for the {@link java.util.regex.Pattern} class, 
 * three types of quantifiers are defined:
 * <ul>
 * 	<li><b>greedy</b> (the default)</li>
 * 	<li><b>reluctant</b> (indicated by a ? suffix on the quantifier)</li>
 * 	<li><b>possessive</b> (indicated by a + suffix on the quantifier)</li>
 * </ul>
 * 
 * This enum is used by the quantifier methods of the {@link Regex} class.
 * 
 * @author Richard W. Eggert II
 * @see Regex#atLeast(int, QuantifierMode)
 * @see Regex#atLeastOnce(QuantifierMode)
 * @see Regex#optional(QuantifierMode)
 * @see Regex#repeat(int, int, QuantifierMode)
 *
 */
public enum QuantifierMode
{
	GREEDY,
	RELUCTANT,
	POSSESSIVE
}
