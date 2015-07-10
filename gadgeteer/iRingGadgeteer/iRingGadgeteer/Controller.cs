using System;
using Microsoft.SPOT;
using iRingGadgeteer.Modules;

namespace iRingGadgeteer
{
    class Controller
    {
        /**
         * possible mode changing events
         */
        public const int MODE_LOCK = 5;
        public const int MODE_INPUT = 6;

        private EthernetHandler mEthernetHandle;
        private ButtonHandler mButtonHandlerCali;
        private ButtonHandler mButtonHandlerMode;
        private AccelHandler mAccelHandler;
        private int currentMode = MODE_LOCK;
        
        public Controller(ButtonHandler btnHandlerCali, ButtonHandler btnHandlerMode, AccelHandler accHandler, EthernetHandler ethernetHandle)
        {
            this.mButtonHandlerCali = btnHandlerCali;
            this.mButtonHandlerMode = btnHandlerMode;
            this.mAccelHandler = accHandler;
            this.mEthernetHandle = ethernetHandle;

            //setting the callbacks
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

        /**
         * callback that is called when the button for changing the mode is pressed
         */
        void ButtonEventMode(int action)
        {
            if (action == ButtonHandler.BTN_RELEASE)
            {
                if(currentMode == MODE_LOCK)
                {
                    currentMode = MODE_INPUT;
                    Debug.Print("Mode changed to input");
                }
                else
                {
                    currentMode = MODE_LOCK;
                    Debug.Print("Mode changed to lock");
                }
                //sending the mode change to the app
                mEthernetHandle.SendData(currentMode);
            }
        }

        /*
         * fired when a motion is detected, gets then sent via bluetooth to the phone
         */
        void AccelEvent(int action)
        {
            mEthernetHandle.SendData(action);

        }
    }
}
