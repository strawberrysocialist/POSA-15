package vandy.mooc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
     * Only for debug
     */
    private String TAG = "PlayPingPong.java";
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
    // @@ TODO - you fill in here.
    private Handler mPingHandler, mPongHandler;

    /**
     * Define a CyclicBarrier synchronizer that ensures the
     * HandlerThreads are fully initialized before the ping-pong
     * algorithm begins.
     */
    // @@ TODO - you fill in here.
    private CyclicBarrier mBarrier = new CyclicBarrier(2);

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
            // @@ TODO - you fill in here.
            mMyType = myType;
            mIterationsCompleted = 0;

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
            // @@ TODO - you fill in here.
            if( mMyType == PingPong.PING){
                mPingHandler = new Handler(this);
            }else{
                mPongHandler = new Handler(this);       
            }           

            try {
                // Wait for both Threads to initialize their Handlers.
                // @@ TODO - you fill in here.
                mBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Start the PING_THREAD first by (1) creating a Message
            // where the PING Handler is the "target" and the PONG
            // Handler is the "obj" to use for the reply and (2)
            // sending the Message to the PING_THREAD's Handler.
            // @@ TODO - you fill in here.
            if( mMyType == PingPong.PING){
                    Message msg = mPingHandler.obtainMessage();
                    msg.setTarget(mPingHandler);
                    msg.obj = mPongHandler;
                    msg.sendToTarget();
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
            // @@ TODO - you fill in here, replacing "true" with the
            // appropriate code.
            Handler targetHandler = reqMsg.getTarget();
            if (mIterationsCompleted < mMaxIterations){
                if( targetHandler.equals(mPingHandler)) {               
                    mOutputStrategy.print("\nPING("+mIterationsCompleted+")");                  
                }else{
                    mOutputStrategy.print("\nPONG("+mIterationsCompleted+")");                  
                }
                mIterationsCompleted++;
            } else {

                // Shutdown the HandlerThread to the main PingPong
                // thread can join with it.
                // @@ TODO - you fill in here.              
                if(  targetHandler.getLooper().getThread().isAlive())
                    targetHandler.getLooper().quit();           
                
                /*For some reason that I didn't find I need to kill both Handlers
                 *  here if not they will never die and the "Done" message will never be called              * 
                 * */
                Handler obj = (Handler)reqMsg.obj;
                if( obj.getLooper().getThread().isAlive() )
                    obj.getLooper().quit();
                
                return true;
                
            }
                        
            // Create a Message that contains the Handler as the
            // reqMsg "target" and our Handler as the "obj" to use for
            // the reply.
            // @@ TODO - you fill in here.
            if(  targetHandler.getLooper().getThread().isAlive()){
                Message newMsg = new Message();
                newMsg.obj = targetHandler;
                newMsg.setTarget((Handler)reqMsg.obj);
            // Return control to the Handler in the other
            // HandlerThread, which is the "target" of the msg
            // parameter.
            // @@ TODO - you fill in here.          
                newMsg.getTarget().sendMessage(newMsg);
                
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Create the ping and pong threads.
        // @@ TODO - you fill in here.
        PingPongThread mPingThread = new PingPongThread(PingPong.PING);
        PingPongThread mPongThread = new PingPongThread(PingPong.PONG);


        // Start ping and pong threads, which cause their Looper to
        // loop.
        // @@ TODO - you fill in here.
        mPingThread.start();
        mPongThread.start();

        // Barrier synchronization to wait for all work to be done
        // before exiting play().
        // @@ TODO - you fill in here.
        
        try {           
            mPongThread.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {           
            mPingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Let the user know we're done.
        mOutputStrategy.print("\nDone!");
    }
}
