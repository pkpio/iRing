using System;
using System.Collections;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer
{
    class PatternMatcher
    {
        public const int MOVEMENT_UP = 1;
        public const int MOVEMENT_RIGHT = 2;
        public const int MOVEMENT_DOWN = 3;
        public const int MOVEMENT_LEFT = 4;
        public const int MOVEMENT_UNKNOWN = 10;

        private int accX;
        private int accY;
        private int accZ;
        private int eCount;

        private int windowSize;

        private FixedSizedQueue queue;

        public PatternMatcher()
        {
            this.queue = new FixedSizedQueue();
            windowSize = 5;
            queue.Limit = windowSize;

            eCount = 0;
        }

        public int addReading(Accelerometer.MeasurementCompleteEventArgs e)
        {
            accX = (int)(e.X * 1000);
            accY = (int)(e.Y * 1000);
            accZ = (int)((e.Z * 1000) - 1000);
            int[] reading = new int[] { accX, accY, accZ };
            int[] cal = new int[3];
            int result = 0;
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

        public int checkPattern(int[] array)
        {  
            int result = 0;
            if (array[0] > 50 && array[1] > 70 && array[2] < -100)
            {
                result = MOVEMENT_RIGHT;
                Debug.Print("right");
            }
            else if (array[0] > 70 && array[1] < 0)
            {
                result = MOVEMENT_DOWN;
                Debug.Print("down");
            }
            if (array[0] < -20 && array[1] < -70 && array[2] > 50)
            {
                result = MOVEMENT_LEFT;
                Debug.Print("left");
            }
            else if (array[0] < -100)
            {
                result = MOVEMENT_UP;
                Debug.Print("up");
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
