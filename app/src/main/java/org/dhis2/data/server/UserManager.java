package org.dhis2.data.server;

import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.user.User;

import androidx.annotation.NonNull;
import io.reactivex.Single;

public interface UserManager {

    @NonNull
    Single<User> logIn(@NonNull String username, @NonNull String password);

    @NonNull
    Single<Boolean> isUserLoggedIn();

    D2 getD2();
}
