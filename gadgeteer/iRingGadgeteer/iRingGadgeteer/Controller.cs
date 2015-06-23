using System;
using Microsoft.SPOT;
using iRingGadgeteer.Modules;

namespace iRingGadgeteer
{
    class Controller
    {
        private ButtonHandler mButtonHandlerCali;
        private ButtonHandler mButtonHandlerMode;
        private AccelHandler mAccelHandler;
        
        public Controller(ButtonHandler btnHandlerCali, ButtonHandler btnHandlerMode, AccelHandler accHandler)
        {
            this.mButtonHandlerCali = btnHandlerCali;
            this.mButtonHandlerMode = btnHandlerMode;
            this.mAccelHandler = accHandler;

            mButtonHandlerCali.SetCallback(ButtonEventCali);
            mButtonHandlerMode.SetCallback(ButtonEventMode);
            mAccelHandler.SetCallback(AccelEvent);
        }

        void ButtonEventCali(int action)
        {
            if (action == ButtonHandler.BTN_RELEASE)
            {
                mAccelHandler.CalibrateAccel();
            }
        }

        void ButtonEventMode(int action)
        {
            if (action == ButtonHandler.BTN_RELEASE)
            {
                //TODO: forward mode change via bluetooth
            }
        }

        /*
         * fired when a motion is detected, gets then send via bluetooth to the phone
         */
        void AccelEvent(int action)
        {
            //TODO: forward motion action and acceleration via bluetooth

        }

        /**
         * Set a callback for calibrate Button events.
         *
        public void SetButtonCallbackCali(ButtonHandler.BtnEventCallback callback)
        {
            mButtonHandlerCali.SetCallback(callback);
        }

        /**
         * Set a callback for Mode Button events.
         *
        public void SetButtonCallbackMode(ButtonHandler.BtnEventCallback callback)
        {
            mButtonHandlerMode.SetCallback(callback);
        }

        /**
         * Set a callback for Accelerometer events.
         *
        public void SetAccelCallback(AccelHandler.AccEventCallback callback)
        {
            mAccelHandler.SetCallback(callback);
        }*/
    }
}
