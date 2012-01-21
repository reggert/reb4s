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
 * This enumeration represents flags that may be embedded into regular 
 * expressions.
 * 
 * @author Richard W. Eggert II
 * @see Regex#enable(Flag...)
 * @see Regex#disable(Flag...)
 * @see Regex#enableDirective(Flag...)
 * @see Regex#disableDirective(Flag...)
 *
 */
public enum Flag
{
	CASE_INSENSITIVE, // 'i'
	UNIX_LINES, // 'd'
	MULTILINE, // 'm'
	DOTALL, // 's'
	UNICODE_CASE, // 'u' 
	COMMENTS // 'x'
}
