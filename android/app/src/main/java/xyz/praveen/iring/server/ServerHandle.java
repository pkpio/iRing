package xyz.praveen.iring.server;

import android.util.Log;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import xyz.praveen.iring.util.LogUtils;

import static xyz.praveen.iring.util.LogUtils.LOGE;
import static xyz.praveen.iring.util.LogUtils.LOGI;
import static xyz.praveen.iring.util.LogUtils.makeLogTag;

/**
 * Created by praveen on 27/6/15.
 */
public class ServerHandle {
    final static String TAG = makeLogTag(ServerHandle.class);

    private final int PORT = 8080;

    MyHTTPD mServer;
    OnGadgetActionListener mOnActionListener;

    /**
     * Init server handle. Server must be started using @see startServer method.
     *
     * @param onActionListener To receive callbacks on Gadget action.
     */
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
            LOGE(TAG, "Failed to start server!");
            e.printStackTrace();
        }
    }

    /**
     * Stops the webserver
     */
    public void stopServer() {
        mServer.stop();
    }


    /**
     * Custom HTTP class to handle server requests
     */
    private class MyHTTPD extends NanoHTTPD {

        public MyHTTPD() throws IOException {
            super(PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            int action = Integer.valueOf(session.getParms().get("data"));
            LOGI(TAG, "Gadget action code : " + action);

            // Pass action
            if (mOnActionListener != null)
                mOnActionListener.onGadgetAction(action);

            final String html = "success";
            return new NanoHTTPD.Response(html);
        }
    }
}
