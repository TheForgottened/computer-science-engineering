package pt.isec.metapd.logic.data.runnables;

import pt.isec.metapd.communication.*;
import pt.isec.metapd.logic.data.MetaPDData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpHandler implements Runnable {
    private final AtomicBoolean mustStop;

    private final AtomicBoolean isDataReady;

    private final MetaPDData metaPDData;

    public TcpHandler(
            AtomicBoolean mustStop,
            AtomicBoolean isDataReady,
            MetaPDData metaPDData
    ) {
        this.mustStop = mustStop;
        this.isDataReady = isDataReady;
        this.metaPDData = metaPDData;
    }

    @Override
    public void run() {
        while (!mustStop.get()) {
            try {
                TinyRequest tinyRequest = (TinyRequest) metaPDData.ois.readObject();

                switch (tinyRequest.requestType()) {
                    case OPERATION_SUCCESSFUL -> metaPDData.setOperationSuccessful(true);
                    case OPERATION_FAILED -> metaPDData.setOperationSuccessful(false);

                    case GET_CONTACT_MESSAGES -> metaPDData.setMessageContactList((List<TinyMessageReceived>) tinyRequest.object());
                    case GET_GROUP_MESSAGES -> metaPDData.setMessageGroupList((List<TinyMessageReceived>) tinyRequest.object());
                    case GET_CONTACTS -> metaPDData.setContactList((List<TinyUser>) tinyRequest.object());
                    case GET_USERS -> metaPDData.setSystemUserList((List<TinyUser>) tinyRequest.object());
                    case GET_FRIEND_REQUESTS -> metaPDData.setFriendshipRequestList((List<TinyUser>) tinyRequest.object());
                    case GET_ALL_GROUP_LIST -> metaPDData.setGroupList((List<TinyGroup>) tinyRequest.object());
                    case GET_MY_GROUP_LIST -> metaPDData.setMyGroupsLists((List<TinyGroup>) tinyRequest.object());
                    case GET_GROUP_REQUESTS -> metaPDData.setGroupRequestList((List<TinyGroupRequest>) tinyRequest.object());
                    case GET_NAME -> metaPDData.setName((String) tinyRequest.object());


                    case UPDATE_VIEW -> {
                        Thread thread = new Thread(metaPDData::updateAllData);
                        thread.start();
                        continue;
                    }
                }

                isDataReady.set(true);
            } catch (SocketTimeoutException e) {
                /* this is needed so the server can close gracefully */
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                metaPDData.connectToNewServer();
            }
        }
    }
}
