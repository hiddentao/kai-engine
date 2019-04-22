/**
 * Copyright (C) 2010 Ramesh Nair (www.hiddentao.com)
 * 
 * This is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This is distributed in the hope that it will  be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hiddentao.kai.timer;


/**
 * A timer event.
 */
public interface TimerEvent
{
	/**
	 * Do the timer event.
	 * 
	 * This gets automatically called by {@link TimerControl} at the set 
	 * scheduling interval.
	 */
	public void doTimerEvent();
	
	/**
	 * Get the description of this timer event.
	 * @return a string.
	 */
	public String getTimerEventDescription();
}
