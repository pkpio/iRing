using System;
using System.Collections;
using System.Threading;
using Microsoft.SPOT;
using Microsoft.SPOT.Presentation;
using Microsoft.SPOT.Presentation.Controls;
using Microsoft.SPOT.Presentation.Media;
using Microsoft.SPOT.Presentation.Shapes;
using Microsoft.SPOT.Touch;

using Gadgeteer.Networking;
using GT = Gadgeteer;
using GTM = Gadgeteer.Modules;
using Gadgeteer.Modules.GHIElectronics;
using Microsoft.SPOT.Net.NetworkInformation;
using iRingGadgeteer.Modules;
using iRingGadgeteer.Utils;

namespace iRingGadgeteer
{
    public partial class Program
    {
        // This method is run when the mainboard is powered up or reset.   
        void ProgramStarted()
        {
            Debug.Print("Program Started");

            // Init Button 1
            ButtonHandler btnHandlerCali = new ButtonHandler(button);
            btnHandlerCali.Start();

            // Init Button 2
            ButtonHandler btnHandlerMode = new ButtonHandler(button2);
            btnHandlerMode.Start();

            // Init Accelerometer
            AccelHandler accHandler = new AccelHandler(accelerometer);
            accHandler.Start();

            // Init Ethernet
            EthernetHandler ethHandler = new EthernetHandler(ethernet);

            // Setup a controller
            Controller mController = new Controller(btnHandlerCali, btnHandlerMode, 
                                                    accHandler, ethHandler);

        }
    }
}
