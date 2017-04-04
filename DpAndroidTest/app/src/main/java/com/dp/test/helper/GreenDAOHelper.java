package com.dp.test.helper;

import android.app.Application;

import com.dp.test.db.DaoMaster;
import com.dp.test.db.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by dapaul on 2017/4/4.
 */

public class GreenDAOHelper {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;
    private static DaoSession daoSession;

    public static void init(Application context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
