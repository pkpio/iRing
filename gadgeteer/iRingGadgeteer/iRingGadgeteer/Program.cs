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

            // Init Button
            ButtonHandler btnHandlerCali = new ButtonHandler(button);
            btnHandlerCali.Start();
            // Init Button
            ButtonHandler btnHandlerMode = new ButtonHandler(button2);
            btnHandlerMode.Start();

            //Init Accelerometer
            AccelHandler accHandler = new AccelHandler(accelerometer);
            accHandler.Start();

            // Setup a controller
            Controller mController = new Controller(btnHandlerCali, btnHandlerMode, accHandler);

            // Ethernet testing
            SetupEthernet();
            ethernet.NetworkUp += OnNetworkUp;
            ethernet.NetworkDown += OnNetworkDown;
        }



        void OnNetworkDown(GTM.Module.NetworkModule sender, GTM.Module.NetworkModule.NetworkState state)
        {
            Debug.Print("Network down.");
        }

        void OnNetworkUp(GTM.Module.NetworkModule sender, GTM.Module.NetworkModule.NetworkState state)
        {
            Debug.Print("Network up.");

            ListNetworkInterfaces();
        }



        void ListNetworkInterfaces()
        {
            var settings = ethernet.NetworkSettings;

            Debug.Print("------------------------------------------------");
            Debug.Print("MAC: " + ByteExtensions.ToHexString(settings.PhysicalAddress, "-"));
            Debug.Print("IP Address:   " + settings.IPAddress);
            Debug.Print("DHCP Enabled: " + settings.IsDhcpEnabled);
            Debug.Print("Subnet Mask:  " + settings.SubnetMask);
            Debug.Print("Gateway:      " + settings.GatewayAddress);
            Debug.Print("------------------------------------------------");
        }

        void SetupEthernet()
        {
            ethernet.NetworkInterface.Open();
            //ethernet.UseDHCP();69.254.
            ethernet.UseStaticIP(
                "169.254.220.163",
                "255.255.255.0",
                "192.168.2.1");
        }
    }
}
