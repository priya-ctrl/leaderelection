import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.SQLOutput;

public class leaderElection implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String ELECTION_NAMESPACE = "/election";
    private ZooKeeper zooKeeper;
    private String currentZnodeName;

    public static void main(String[] args) throws IOException, InterruptedIOException, InterruptedException {
        leaderElection leaderElection = new leaderElection();
        leaderElection.connecttoZookeeper();
        leaderElection.run();
        leaderElection.close();
        System.out.println("Disconnected from Zookeeper, exiting application");
    }

    //Implementation of the Leader selection algorithm
    public void volunteerForLeadership() throws InterruptedException, KeeperException {
        String znodePrefix = ELECTION_NAMESPACE +"/c";
        String znodeFullPath =zooKeeper.create(znodePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("znode name" + znodeFullPath);
        this.currentZnodeName = znodeFullPath.replace(ELECTION_NAMESPACE+ "/", "");
    }
    // Elect Leader method
    public void electLeader(){

    }
        public void connecttoZookeeper () throws IOException {
            this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
        }

        public void run() throws InterruptedIOException, InterruptedException {
        synchronized (zooKeeper){
            zooKeeper.wait();
            }
        }
        public void close() throws InterruptedException {
        zooKeeper.close();
        }

        @Override
        public void process (WatchedEvent watchedEvent){
            switch (watchedEvent.getType()) {
                case None -> {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("Successfully Connected to Zookeeper");
                    } else {
                        synchronized (zooKeeper){
                            System.out.println("Disconnected from Zookeeper event");
                            zooKeeper.notifyAll();
                        }
                    }
                }

            }

        }

        }