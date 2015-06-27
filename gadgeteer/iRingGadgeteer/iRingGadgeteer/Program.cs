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
            //accHandler.Start();

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

            WebClient.GetFromWeb("http://192.168.178.29:8080/").ResponseReceived +=
                new HttpRequest.ResponseHandler(Program_ResponseReceived);
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
            ethernet.UseThisNetworkInterface();
            ethernet.UseDHCP();
            ethernet.UseStaticIP(
                "192.168.178.55",
                "255.255.255.0",
                "192.168.178.1",
                new String[]{"8.8.8.8", "4.4.4.4"});
        }

        void Program_ResponseReceived(HttpRequest sender, HttpResponse response)
        {
            if (response.StatusCode == "200")
            {
                Debug.Print("HTTP-Response: " + response.StatusCode);
                Debug.Print("HTTP-Response: " + response.Text);  
            }
            else
            {
                Debug.Print("HTTP-Response: " + response.StatusCode);
            }
        }
    }
}
