using System;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer.Modules
{
    class AccelHandler
    {
        public const int MOVEMENT_UP = 1;
        public const int MOVEMENT_RIGHT = 2;
        public const int MOVEMENT_DOWN = 3;
        public const int MOVEMENT_LEFT = 4;
        public const int MEASUREMENT_COMPLETE = 5;
        public const int THRESHOLD_EXCEEDED = 6;

        private int accX;
        private int accY;
        private int accZ;
        //private int accel;

        PatternMatcher pMatcher;

        /**
         * Accelerometer event handler delegate / type
         */
        public delegate void AccEventCallback(int accEvent);
        event AccEventCallback eventCallback;

        Accelerometer mAccel;

        public AccelHandler(Accelerometer mAcc)
        {
            this.mAccel = mAcc;
            pMatcher = new PatternMatcher();
        }

        /**
         * This callback will be called when ever a accelerometer event occurs
         */
        public void SetCallback(AccEventCallback accEventHandle)
        {
            this.eventCallback = accEventHandle;
        }

        /**
         * Calibrating the accelerometer
         */
        public void CalibrateAccel()
        {
            mAccel.Calibrate();
            Debug.Print("calibrated");
        }

        /*
         * get acceleration of movement event
         */
        /*public int getAccel()
        {
            return accel;
        }*/

        void accelerometer_MeasurementComplete(Accelerometer sender, Accelerometer.MeasurementCompleteEventArgs e)
        {
            /*accX = (int) (e.X * 1000);
            accY = (int) (e.Y * 1000);
            accZ = (int) ((e.Z * 1000)-1000);*/
            //Debug.Print("" + accX + "," + accY + "," + accZ);
            int result = pMatcher.addReading(e);
            if(result > 0)
            {
                SendEventToCallback(result);
            }
            /*if (accX > 50 && accY > 70 && accZ < -100)
            {
                accel = accX;
                SendEventToCallback(MOVEMENT_RIGHT);
                Debug.Print("right " + accel);
            } 
            else if (accX > 70 && accY < 0)
            {
                accel = accZ;
                SendEventToCallback(MOVEMENT_DOWN);
                Debug.Print("down "+accel);
            }          
            if(accX < -20 && accY < -70 && accZ > 50)
            {
                accel = accX;
                SendEventToCallback(MOVEMENT_LEFT);
                Debug.Print("left "+accel);
            }
            else if (accX < -100)
            {
                accel = accZ;
                SendEventToCallback(MOVEMENT_UP);
                Debug.Print("up " + accel);
            }*/
        }

        private void SendEventToCallback(int action)
        {
            if (eventCallback != null)
                eventCallback(action);
        }

        public void Start()
        {
            mAccel.Calibrate();
            mAccel.StartTakingMeasurements();
            mAccel.MeasurementComplete += accelerometer_MeasurementComplete;
        }
    }
}
