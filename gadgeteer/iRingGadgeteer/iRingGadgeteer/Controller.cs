using System;
using Microsoft.SPOT;
using iRingGadgeteer.Modules;

namespace iRingGadgeteer
{
    class Controller
    {
        private ButtonHandler mButtonHandler;
        private AccelHandler mAccelHandler;
        
        public Controller(ButtonHandler btnHandler, AccelHandler accHandler)
        {
            this.mButtonHandler = btnHandler;
            this.mAccelHandler = accHandler;

            mButtonHandler.SetCallback(ButtonEvent);
            mAccelHandler.SetCallback(AccelEvent);
        }

        void ButtonEvent(int action)
        {
            if (action == ButtonHandler.BTN_RELEASE)
            {
                
            }
        }

        void AccelEvent(int action)
        {
            
        }

        /**
         * Set a callback for Button events.
         */
        public void SetButtonCallback(ButtonHandler.BtnEventCallback callback)
        {
            mButtonHandler.SetCallback(callback);
        }

        /**
         * Set a callback for Accelerometer events.
         */
        public void SetAccelCallback(AccelHandler.AccEventCallback callback)
        {
            mAccelHandler.SetCallback(callback);
        }
    }
}
