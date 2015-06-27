package xyz.praveen.iring.server;

import android.util.Log;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by praveen on 27/6/15.
 */
public class ServerHandle {
    final String DEBUG_TAG = this.getClass().getName();
    private final int PORT = 8080;

    MyHTTPD mServer;
    OnGadgetActionListener mOnActionListener;

    public ServerHandle(OnGadgetActionListener onActionListener) {
        this.mOnActionListener = onActionListener;
    }

    /**
     * Starts a server in the background
     */
    public void startServer() {
        try {
            mServer = new MyHTTPD();
            mServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the webserver
     */
    public void stopServer() {
        mServer.stop();
    }


    private class MyHTTPD extends NanoHTTPD {

        public MyHTTPD() throws IOException {
            super(PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            String action = session.getParms().get("data");
            Log.d(DEBUG_TAG, action);

            // Pass action
            if (mOnActionListener != null)
                mOnActionListener.onGadgetAction(action);

            final String html = "success";
            return new NanoHTTPD.Response(html);
        }
    }
}
