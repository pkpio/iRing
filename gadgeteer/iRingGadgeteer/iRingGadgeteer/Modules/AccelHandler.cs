using System;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer.Modules
{
    class AccelHandler
    {
        double accX;
        double accY;
        double accZ;

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
            accX = e.X;
            accY = e.Y;
            accZ = e.Z;
            Debug.Print("X: " + accX + "Y: " + accY + "Z: " + accZ);
        }

        public void Start()
        {
            mAccel.Calibrate();
            mAccel.StartTakingMeasurements();
            mAccel.MeasurementComplete += accelerometer_MeasurementComplete;
        }
    }
}
