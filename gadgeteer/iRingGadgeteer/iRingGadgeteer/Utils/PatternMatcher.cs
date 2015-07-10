using System;
using System.Collections;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer
{
    /**
     * contains the queue for automatic window calibration and pattern matches 
     * new acceleration readings for movement events
     */
    class PatternMatcher
    {
        /**
         * possible movement events
         */
        public const int MOVEMENT_UP = 1;
        public const int MOVEMENT_RIGHT = 2;
        public const int MOVEMENT_DOWN = 3;
        public const int MOVEMENT_LEFT = 4;
        public const int MOVEMENT_UNKNOWN = 10;

        private int accX;
        private int accY;
        private int accZ;
        private int eCount; //number of readings since movement event started

        private int windowSize;

        private FixedSizedQueue queue; //window in which the last readings are stored

        public PatternMatcher()
        {
            this.queue = new FixedSizedQueue();
            windowSize = 5;
            queue.Limit = windowSize;

            eCount = 0;
        }

        /**
         * adds a new acceleration reading to the queue
         */
        public int addReading(Accelerometer.MeasurementCompleteEventArgs e)
        {
            accX = (int)(e.X * 1000);
            accY = (int)(e.Y * 1000);
            accZ = (int)((e.Z * 1000) - 1000);
            int[] reading = new int[] { accX, accY, accZ };
            int[] cal = new int[3];
            int result = 0;
            /** 
             * when no movement event has been registered yet, the reading gets calibrated 
             * by subtracting the first reading in the queue from the current reading
             * then we check if a movement event can bbe found by calling checkPattern(cal)
             */
            if(eCount == 0)
            {
                int[] first = (int[]) queue.Peek();
                if (first != null)
                {                    
                    cal[0] = reading[0] - first[0];
                    cal[1] = reading[1] - first[1];
                    cal[2] = reading[2] - first[2];
                    result = checkPattern(cal);
                    if(result > 0)
                    {
                        eCount = 1;
                    }
                }               
            }
            //if a movement event has already been registered we wait until the event is done before looking for new ones
            else
            {
                eCount++;
                if(eCount == 10)
                {
                    eCount = 0;
                }
            }
            Debug.Print("x" + cal[0] + " y" + cal[1] + " z" + cal[2]);
            queue.Enqueue(reading);
            return result;
        }

        /**
         * by looking at the acceleration along the x-, y- and z-axis we distinguish movement events and
         * return the registered event
         */
        public int checkPattern(int[] array)
        {  
            int result = 0;
            
            if (array[2] > 50)
            {
                result = MOVEMENT_UP;
            }
            else if (array[2] < - 50)
            {
                result = MOVEMENT_DOWN;
            }

            // If no event occured and readings are off the zero point
            if (result == 0 && (System.Math.Abs(array[0]) > 70 
                || System.Math.Abs(array[1]) > 70 || System.Math.Abs(array[2]) > 70))
            {
                result = MOVEMENT_UNKNOWN;
                Debug.Print("unknown");
            }

            return result;
       }
        /**
         * a queue with fixed length that automatically dequeues if the limit is reached
         */
        public class FixedSizedQueue
        {
            Queue q = new Queue();

            public int Limit = 5;
            public void Enqueue(Object obj)
            {
                q.Enqueue(obj);
                if (q.Count > Limit)
                    q.Dequeue();
            }
            public Object Peek()
            {
                try
                {
                    return q.Peek();
                }
                catch(InvalidOperationException e)
                {
                    return null;
                }
            }
        }
    }
}
