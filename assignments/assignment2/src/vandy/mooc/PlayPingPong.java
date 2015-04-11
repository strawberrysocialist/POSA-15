package vandy.mooc;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * @class PlayPingPong
 *
 * @brief This class uses elements of the Android HaMeR framework to
 *        create two Threads that alternately print "Ping" and "Pong",
 *        respectively, on the display.
 */
public class PlayPingPong implements Runnable {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Keep track of whether a Thread is printing "ping" or "pong".
     */
    private enum PingPong {
        PING, PONG
    };

    /**
     * Number of iterations to run the ping-pong algorithm.
     */
    private final int mMaxIterations;

    /**
     * The strategy for outputting strings to the display.
     */
    private final OutputStrategy mOutputStrategy;

    /**
     * Define a pair of Handlers used to send/handle Messages via the
     * HandlerThreads.
     */
    // @@ FIXED - you fill in here.
    Handler[] mHandlers = {null, null};

    /**
     * Define a CyclicBarrier synchronizer that ensures the
     * HandlerThreads are fully initialized before the ping-pong
     * algorithm begins.
     */
    // @@ FIXED - you fill in here.
    CyclicBarrier barrier = new CyclicBarrier(2);

    /**
     * Implements the concurrent ping/pong algorithm using a pair of
     * Android Handlers (which are defined as an array field in the
     * enclosing PlayPingPong class so they can be shared by the ping
     * and pong objects).  The class (1) extends the HandlerThread
     * superclass to enable it to run in the background and (2)
     * implements the Handler.Callback interface so its
     * handleMessage() method can be dispatched without requiring
     * additional subclassing.
     */
    class PingPongThread extends HandlerThread implements Handler.Callback {
        /**
         * Keeps track of whether this Thread handles "pings" or
         * "pongs".
         */
        private PingPong mMyType;

        /**
         * Number of iterations completed thus far.
         */
        private int mIterationsCompleted;

        /**
         * Constructor initializes the superclass and type field
         * (which is either PING or PONG).
         */
        public PingPongThread(PingPong myType) {
        	super(myType.toString());
            // @@ FIXED - you fill in here.
            mMyType = myType;
            mIterationsCompleted = 1;
        }

        /**
         * This hook method is dispatched after the HandlerThread has
         * been started.  It performs ping-pong initialization prior
         * to the HandlerThread running its event loop.
         */
        @Override    
        protected void onLooperPrepared() {
            // Create the Handler that will service this type of
            // Handler, i.e., either PING or PONG.
            // @@ FIXED - you fill in here.
            if (mMyType == PingPong.PING) {
                Log.d(TAG, "Creating Ping handler");
            } else if (mMyType == PingPong.PONG) {
                Log.d(TAG, "Creating Pong handler");
            }
            mHandlers[mMyType.ordinal()] = new Handler(PingPongThread.this);

            try {
                // Wait for both Threads to initialize their Handlers.
                // @@ FIXED - you fill in here.
                Log.d(TAG, "Waiting to both handlers to be ready");
                Log.d(TAG, barrier.getNumberWaiting() + " threads waiting.");
                barrier.await(5, TimeUnit.SECONDS);
                Log.d(TAG, "Barrier done in " + mMyType.name() + " thread.");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Start the PING_THREAD first by (1) creating a Message
            // where the PING Handler is the "target" and the PONG
            // Handler is the "obj" to use for the reply and (2)
            // sending the Message to the PING_THREAD's Handler.
            // @@ FIXED - you fill in here.
            Log.d(TAG, "Launching...");
            if (mMyType == PingPong.PING) {
                Log.d(TAG, "Sending kick-off message to Ping");
                Message msg = Message.obtain();
                msg.setTarget(mHandlers[PingPong.PING.ordinal()]);
                msg.obj = mHandlers[PingPong.PONG.ordinal()];
                msg.sendToTarget();
                Log.d(TAG, "Finished Ping handlerThread");
            } else {
                Log.d(TAG, "Finished Pong handlerThread");
            }
        }

        /**
         * Hook method called back by HandlerThread to perform the
         * ping-pong protocol concurrently.
         */
        @Override
        public boolean handleMessage(Message reqMsg) {
            // Print the appropriate string if this thread isn't done
            // with all its iterations yet.
            // @@ FIXED - you fill in here, replacing "true" with the
            // appropriate code.
            Log.d(TAG, "Handling message...");
            if (mIterationsCompleted < mMaxIterations) {
                Log.d(TAG, 
                        "Incrementing..." + mMyType.name() 
                        + " to " + mIterationsCompleted);
                mOutputStrategy.print(mMyType.name() 
                        + "(" + mIterationsCompleted + ")"); 
                mIterationsCompleted++;
            } else {
                // Shutdown the HandlerThread to the main PingPong
                // thread can join with it.
                // @@ FIXED - you fill in here.
                if (mMyType == PingPong.PING) {
                    Handler receiver = (Handler) reqMsg.obj;
                    receiver.sendEmptyMessage(-1);
                }
                Log.d(TAG, "Quitting..." + mMyType.name());
                Looper looper = getLooper();
                looper.quit();
                return true;
            }

            // Create a Message that contains the Handler as the
            // reqMsg "target" and our Handler as the "obj" to use for
            // the reply.
            // @@ FIXED - you fill in here.
            Message msg = null;
            if (null != reqMsg) {
                msg = Message.obtain();
                msg.setTarget((Handler) reqMsg.obj);
                msg.obj = reqMsg.getTarget();
                
                Log.d(TAG, 
                        "Preparing message for " + mMyType.name());
            }

            // Return control to the Handler in the other
            // HandlerThread, which is the "target" of the msg
            // parameter.
            // @@ FIXED - you fill in here.
            if (null != msg && null != msg.getTarget()) {
                int otherTypeIndex = Math.abs(mMyType.ordinal() - 1);
                Log.d(TAG, "Sending " 
                        + PingPong.values()[otherTypeIndex].name() 
                        + " message from " + mMyType.name());
                msg.sendToTarget();
            }

            return true;
        }
    }

    /**
     * Constructor initializes the data members.
     */
    public PlayPingPong(int maxIterations,
                        OutputStrategy outputStrategy) {
        // Number of iterations to perform pings and pongs.
        mMaxIterations = maxIterations;

        // Strategy that controls how output is displayed to the user.
        mOutputStrategy = outputStrategy;
    }

    /**
     * Start running the ping/pong code, which can be called from a
     * main() method in a Java class, an Android Activity, etc.
     */
    public void run() {
        // Let the user know we're starting. 
        mOutputStrategy.print("Ready...Set...Go!");
       
        // Create the ping and pong threads.
        // @@ FIXED - you fill in here.
        PingPongThread ping = new PingPongThread(PingPong.PING);
        PingPongThread pong = new PingPongThread(PingPong.PONG);

        // Start ping and pong threads, which cause their Looper to
        // loop.
        // @@ FIXED - you fill in here.
        Log.d(TAG, "Starting Ping & Ping threads");
        ping.start();
        pong.start();

        // Barrier synchronization to wait for all work to be done
        // before exiting play().
        // @@ FIXED - you fill in here.
        try {
            Log.d(TAG, "Waiting for Ping & Ping threads to finish");
            ping.join();
            pong.join();
            Log.d(TAG, "Main done...Quitting");
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted!");
            ping.interrupt();
            pong.interrupt();
            e.printStackTrace();
        }

        // Let the user know we're done.
        mOutputStrategy.print("Done!");
    }
}
