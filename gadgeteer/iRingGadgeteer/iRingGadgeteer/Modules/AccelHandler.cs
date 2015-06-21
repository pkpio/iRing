using System;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer.Modules
{
    class AccelHandler
    {
        int accX;
        int accY;
        int accZ;

        /**
         * Accelerometer event handler delegate / type
         */
        public delegate void AccEventCallback(int accEvent);
        event AccEventCallback eventCallback;

        Accelerometer mAccel;

        public AccelHandler(Accelerometer mAcc)
        {
            this.mAccel = mAcc;
        }

        /**
         * This callback will be called when ever a accelerometer event occurs
         */
        public void SetCallback(AccEventCallback accEventHandle)
        {
            this.eventCallback = accEventHandle;
        }

        void accelerometer_MeasurementComplete(Accelerometer sender, Accelerometer.MeasurementCompleteEventArgs e)
        {
            accX = (int) (e.X * 1000);
            accY = (int) (e.Y * 1000);
            //accZ = (int) (e.Z * 10);
            Debug.Print("X: " + accX + "  Y: " + accY ); //+ "  Z: " + accZ);
        }

        public void Start()
        {
            mAccel.Calibrate();
            mAccel.StartTakingMeasurements();
            mAccel.MeasurementComplete += accelerometer_MeasurementComplete;
        }
    }
}
