package org.dhis2.data.server;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.user.User;

import androidx.annotation.NonNull;
import io.reactivex.Single;

public class UserManagerImpl implements UserManager {
    private final D2 d2;

    public UserManagerImpl(@NonNull D2 d2) {
        this.d2 = d2;
    }

    @NonNull
    @Override
    public Single<User> logIn(@NonNull String username, @NonNull String password) {
        return d2.userModule().logIn(username, password);
    }

    @NonNull
    @Override
    public Single<Boolean> isUserLoggedIn() {
        return d2.userModule().isLogged();
    }

    @Override
    public D2 getD2(){
        return d2;
    }
}
