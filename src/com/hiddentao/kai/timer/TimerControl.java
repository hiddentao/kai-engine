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

import java.util.Hashtable;

import com.hiddentao.kai.logging.Logger;
import com.hiddentao.kai.utils.HasStaticDataImpl;


/**
 * The timer control.
 */
public final class TimerControl extends HasStaticDataImpl
{
	private static Logger LOG = Logger.getLogger(TimerControl.class.getName());
	
	private static TimerControl iInstance = null;
	
	private volatile static boolean iTimerThreadActive = false;
	
	private volatile Hashtable<TimerEvent, ScheduledEvent> iEvents = new Hashtable<TimerEvent, ScheduledEvent>();
	
	private Thread iTimerThread = null;
	
	
	private TimerControl()
	{
	}

	public static TimerControl getInstance()
	{
		if (null == iInstance)
		{
			iInstance = new TimerControl();
		}
		return iInstance;
	}
	
	public void resetStaticData()
	{
		iTimerThreadActive = false;
		iInstance = null;
	}	
	
	
	/**
	 * Unschedule a timer event.
	 * 
	 * @param aEvent the event to unschedule
	 * @return true if successfully unscheduled; false otherwise.
	 */
	public void remove(TimerEvent aEvent)
	{
		synchronized(iEvents)
		{
			if (null != aEvent && iEvents.containsKey(aEvent))
			{
				iEvents.remove(aEvent);
				LOG.debug("Removed timer event: " + aEvent.getTimerEventDescription());				
			}
		}
	}
	
	
	/**
	 * Schedule a timer event.
	 * 
	 * If the timer event is already scheduled then it will be rescheduled.
	 * 
	 * @param aEvent the event to schedule
	 * @param aMillisecondInterval the interval between each run of the event.
	 * 
	 * @return true if successfully scheduled; false otherwise.
	 */	
	public void add(TimerEvent aEvent, int aMillisecondInterval)
	{
		if (null != aEvent && 0 < aMillisecondInterval)
		{
			synchronized(iEvents)
			{
				iEvents.put(aEvent, new ScheduledEvent(aEvent, aMillisecondInterval));
				LOG.debug("Added timer event: " + aEvent.getTimerEventDescription());
				if (null == iTimerThread)
				{
					iTimerThreadActive = true;
					iTimerThread = new Thread(new TimerThread());
					iTimerThread.setDaemon(true);
					iTimerThread.start();
				}
			}			
		}
	}
	

	/**
	 * Represents a scheduled timer event.
	 */
	private class ScheduledEvent
	{
		public TimerEvent iEvent;
		public int iMillisecondInterval;
		public long iLastRunTime = 0;

		public ScheduledEvent(TimerEvent aEvent, int aMillisecondInterval)
		{
			iEvent = aEvent;
			iMillisecondInterval = aMillisecondInterval;
		}
	}
	
	
	
	private class TimerThread implements Runnable
	{
		public void run()
		{
			while (iTimerThreadActive)
			{
				synchronized(iEvents)
				{
					for (TimerEvent timerEvent : iEvents.keySet())
					{
						ScheduledEvent scheduledEvent = iEvents.get(timerEvent);
						if (scheduledEvent.iMillisecondInterval < System.currentTimeMillis() - scheduledEvent.iLastRunTime)
						{
							timerEvent.doTimerEvent();
							scheduledEvent.iLastRunTime = System.currentTimeMillis();
						}
					} // for each timer event
				} // mutex on iEvents
			} // while thread active
		}
	}
	
}
