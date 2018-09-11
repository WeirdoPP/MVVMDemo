package google.architecture.coremodel.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 数据库demo 注意build.gradle
 * @since 2018/09/11
 */

@Database(entities = {StudentEntity.class, ClassEntity.class}, version = 1)
public abstract class RoomDemoDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private static RoomDemoDatabase INSTANCE;

    public static RoomDemoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), RoomDemoDatabase.class, RoomManager.DATABASE_NAME)
                                .build();
            }
            return INSTANCE;
        }
    }

    public abstract StudentDao studentDao();

    public abstract ClassDao classDaoDao();

}
