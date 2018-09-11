package google.architecture.coremodel.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 学生表查询类
 * @since 2018/09/11
 */

@Dao
public interface StudentDao {

    @Query("SELECT * FROM tb_student")
    List<StudentEntity> getAll();

    @Query("SELECT * FROM tb_student WHERE id IN (:ids)")
    List<StudentEntity> getAllByIds(long[] ids);

    @Insert
    void insert(StudentEntity... entities);

    @Delete
    void delete(StudentEntity entity);

    /**
     * 注解增加实务冲突处理参数{@link OnConflictStrategy}
     *
     * @param entity
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(StudentEntity entity);

//    public @interface OnConflictStrategy {
//        //策略冲突就替换旧数据
//        int REPLACE = 1;
//        //策略冲突就回滚事务
//        int ROLLBACK = 2;
//        //策略冲突就退出事务
//        int ABORT = 3;
//        //策略冲突就使事务失败
//        int FAIL = 4;
//        //忽略冲突
//        int IGNORE = 5;
//    }

}
