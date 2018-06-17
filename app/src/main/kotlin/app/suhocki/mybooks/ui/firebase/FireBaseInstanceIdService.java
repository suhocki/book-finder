package app.suhocki.mybooks.ui.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireBaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    @android.support.annotation.WorkerThread
    public void onTokenRefresh() {

        /* obtain the current InstanceId token: */
        String token = FirebaseInstanceId.getInstance().getToken();
    }
}
