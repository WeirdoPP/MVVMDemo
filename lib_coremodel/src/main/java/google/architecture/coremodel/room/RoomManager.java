package google.architecture.coremodel.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 数据库管理类
 * @since 2018/09/11
 */

public class RoomManager {

    public static final String DATABASE_NAME = "mvvm_demo";

    /**
     * 初始化数据库
     *
     * @param context
     */
    public static void initRoom(Context context) {

        Room.databaseBuilder(context,
                RoomDemoDatabase.class, DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    //第一次创建数据库时调用，但是在创建所有表之后调用的
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }

                    //当数据库被打开时调用
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
//                .allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations()//迁移数据库使用，下面会单独拿出来讲
                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .build();
    }

    /**
     * 升级数据库
     */
    public static void updateRoom(Context context) {

        final Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
                        + "`name` TEXT, PRIMARY KEY(`id`))");
            }
        };

        final Migration MIGRATION_2_3 = new Migration(2, 3) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE Book "
                        + " ADD COLUMN pub_year INTEGER");
            }
        };

        Room.databaseBuilder(context, RoomDemoDatabase.class, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build();

    }

    /**
     * 获取学生列表
     *
     * @param context
     */
    public void getSudent(Context context) {
        RoomDemoDatabase.getInstance(context).studentDao().getAll();
    }
}
