using System;
using Microsoft.SPOT;

using Gadgeteer.Networking;
using GT = Gadgeteer;
using GTM = Gadgeteer.Modules;
using Gadgeteer.Modules.GHIElectronics;
using Microsoft.SPOT.Net.NetworkInformation;
using iRingGadgeteer.Utils;

namespace iRingGadgeteer.Modules
{
    class EthernetHandler
    {
        public const String ServerAddr = "http://10.171.0.184:8080/";
        private const String IP_ADDR = "10.171.0.194";
        private const String SUBNET = "255.255.254.0";
        private const String GATEWAY = "10.171.0.254";

        EthernetJ11D mEthernet;

        /**
         * Accelerometer event handler delegate / type
         */
        public delegate void EthernetResponseCallback(HttpResponse response);
        event EthernetResponseCallback eventCallback;

        public EthernetHandler(EthernetJ11D eth)
        {
            this.mEthernet = eth;
            SetupEthernet();

            // Setup callbacks
            mEthernet.NetworkUp += OnNetworkUp;
            mEthernet.NetworkDown += OnNetworkDown;
        }

        /**
         * This callback will be called when the url response has arrived
         */
        public void SetCallback(EthernetResponseCallback ethRespHandle)
        {
            this.eventCallback = ethRespHandle;
        }

        /**
         * Sends data to remote server
         */
        public void SendData(int action)
        {
            OpenUrl(ServerAddr + "?data=" + action);
        }

        /**
         * Opens the specified url. Results will posted through respective callbacks. 
         */
        public void OpenUrl(String url)
        {
            WebClient.GetFromWeb(url).ResponseReceived +=
                new HttpRequest.ResponseHandler(Program_ResponseReceived);
        }

        void Program_ResponseReceived(HttpRequest sender, HttpResponse response)
        {
            // Send event to callback
            if (eventCallback != null)
                eventCallback(response);

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

        void OnNetworkUp(GTM.Module.NetworkModule sender, GTM.Module.NetworkModule.NetworkState state)
        {
            Debug.Print("Network up.");
            ListNetworkInterfaces();
            OpenUrl(ServerAddr);
        }

        void OnNetworkDown(GTM.Module.NetworkModule sender, GTM.Module.NetworkModule.NetworkState state)
        {
            Debug.Print("Network down.");
        }

        void ListNetworkInterfaces()
        {
            var settings = mEthernet.NetworkSettings;

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
            mEthernet.UseThisNetworkInterface();
            mEthernet.UseDHCP();
            mEthernet.UseStaticIP(IP_ADDR, SUBNET, GATEWAY,
                new String[] { "8.8.8.8", "8.8.4.4" });
        }
    }
}
