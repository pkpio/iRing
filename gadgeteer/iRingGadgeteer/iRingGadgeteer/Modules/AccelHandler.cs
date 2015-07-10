using System;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer.Modules
{
    /**
     * Handles acceleration events and sends movement events to callback
     */
    class AccelHandler
    {
        /**
         * relevant events for acceleration measurements
         */
        public const int MOVEMENT_UP = 1;
        public const int MOVEMENT_RIGHT = 2;
        public const int MOVEMENT_DOWN = 3;
        public const int MOVEMENT_LEFT = 4;

        //contains automatic window calibration and pattern matching for movement events
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
         * Not used anymore, instead automatic window calibration
         */
        public void CalibrateAccel()
        {
            mAccel.Calibrate();
            Debug.Print("calibrated");
        }

        /**
         * Event that is fired everytime a new reading from the accelerometer is available
         */
        void accelerometer_MeasurementComplete(Accelerometer sender, Accelerometer.MeasurementCompleteEventArgs e)
        {
            int result = pMatcher.addReading(e);
            //when a movement event is registered it is send to the callback
            if(result > 0)
            {
                SendEventToCallback(result);
            }
        }

        /**
         * sends an acceleration event to the callback that was registered
         */
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
