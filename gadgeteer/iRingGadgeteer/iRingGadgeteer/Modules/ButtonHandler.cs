using System;
using System.Threading;
using Microsoft.SPOT;
using Gadgeteer.Modules.GHIElectronics;

namespace iRingGadgeteer.Modules
{
    class ButtonHandler
    {
        /**
         * Possible actions for button
         */
        public const int BTN_PRESS = 1;
        public const int BTN_RELEASE = 2;

        /**
         * Button event handler delegate / type
         */
        public delegate void BtnEventCallback(int btnEvent);
        event BtnEventCallback eventCallback;

        Button mButton;

        public ButtonHandler(Button mBtn)
        {
            this.mButton = mBtn;
        }

        /**
         * This callback will be called when ever a button event occurs
         */
        public void SetCallback(BtnEventCallback btnEventHandle)
        {
            this.eventCallback = btnEventHandle;
        }

        /**
         * Called by gadgeteer when Button state changes
         */
        void ButtonStateChanged(Button sender, Button.ButtonState state)
        {
            if (state == Button.ButtonState.Pressed)
            {
                SendEventToCallback(BTN_PRESS);
            }
            else if (state == Button.ButtonState.Released)
            {
                SendEventToCallback(BTN_RELEASE);
            }
        }

        private void SendEventToCallback(int action)
        {
            if (eventCallback != null)
                eventCallback(action);
        }

        public void Start()
        {
            mButton.ButtonReleased += ButtonStateChanged;
            mButton.ButtonPressed += ButtonStateChanged;
        }
    }
}
