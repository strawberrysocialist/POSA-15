package vandy.mooc;

import java.util.concurrent.CyclicBarrier;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * @class PlayPingPong
 *
 * @brief This class uses elements of the Android HaMeR framework to
 *        create two Threads that alternately print "Ping" and "Pong",
 *        respectively, on the display.
 */
public class PlayPingPong implements Runnable {
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
    // An array of handlers?....get back to this when I need i
    // @@ DONE - you fill in here.
    //private Handler handlerPing;
    //private Handler handlerPong;
    private Handler mHandlers[]=new Handler[PingPong.values().length];
  

    /**
     * Define a CyclicBarrier synchronizer that ensures the
     * HandlerThreads are fully initialized before the ping-pong
     * algorithm begins.
     */
    // @@ DONE - you fill in here.
    private CyclicBarrier barrier = new CyclicBarrier(2);

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
            // @@ DONE - you fill in here.
            mMyType = myType;
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
            // @@ DONE - you fill in here.
            
            if (mMyType == PingPong.PING) {
                mHandlers[0] = new Handler(getLooper(), this);  
            }

            if (mMyType == PingPong.PONG) {
                mHandlers[1] = new Handler(getLooper(), this);  
            }


            try {
                // Wait for both Threads to initialize their Handlers.
                // @@ DONE - you fill in here.
                barrier.await();
                
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Start the PING_THREAD first by (1) creating a Message
            // where the PING Handler is the "target" and the PONG
            // Handler is the "obj" to use for the reply and (2)
            // sending the Message to the PING_THREAD's Handler.
            // @@ DONE - you fill in here.
            Message m = mHandlers[0].obtainMessage();
            m.setTarget(mHandlers[0]);
            m.obj = mHandlers[1];
            mHandlers[0].sendMessage(m);
            
        }

        /**
         * Hook method called back by HandlerThread to perform the
         * ping-pong protocol concurrently.
         */
        @Override
        public boolean handleMessage(Message reqMsg) {
            // Print the appropriate string if this thread isn't done
            // with all its iterations yet.
            // @@ DONE - you fill in here, replacing "true" with the
            // appropriate code.
            if (mIterationsCompleted < mMaxIterations) {
                mIterationsCompleted++;
                if (reqMsg.getTarget() == mHandlers[0]) {
                    mOutputStrategy.print("Ping (" + mIterationsCompleted + ")" );  
                } else {
                    mOutputStrategy.print("Pong (" + mIterationsCompleted + ")");
                }

                
            } else {
                // Shutdown the HandlerThread to the main PingPong
                // thread can join with it.
                // @@ DONE - you fill in here.
                this.quit();
            }

            // Create a Message that contains the Handler as the
            // reqMsg "target" and our Handler as the "obj" to use for
            // the reply.
            // @@ DONE - you fill in here.
            
            // Probably there's a much better way to do this using reqMsg targets and objs
            if (reqMsg.getTarget() == mHandlers[0]) {
                Message m = mHandlers[1].obtainMessage();
                m.setTarget(mHandlers[1]);
                m.obj = mHandlers[0];
                mHandlers[1].sendMessage(m);
                
            } else {
                Message m = mHandlers[0].obtainMessage();
                m.setTarget(mHandlers[0]);
                m.obj = mHandlers[1];
                mHandlers[0].sendMessage(m);
            }
            
            // Return control to the Handler in the other
            // HandlerThread, which is the "target" of the msg
            // parameter.
            // @@ DONE - you fill in here.
            

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
        // @@ DONE - you fill in here.
        PingPongThread mPing = new PingPongThread(PingPong.PING);
        PingPongThread mPong = new PingPongThread(PingPong.PONG);

        // Start ping and pong threads, which cause their Looper to
        // loop.
        // @@ DONE - you fill in here.
        mPing.start();
        mPong.start();
        
        // Barrier synchronization to wait for all work to be done
        // before exiting play().
        // @@ DONE - you fill in here.
        try {
            mPing.join();
        } catch (InterruptedException e) {
            mOutputStrategy.print("Ping Interrupted!");
        }
        try {
            mPong.join();
        } catch (InterruptedException e) {
            mOutputStrategy.print("Pong Interrupted!");
        }

        // Let the user know we're done.
        mOutputStrategy.print("Done!");
    }
}
