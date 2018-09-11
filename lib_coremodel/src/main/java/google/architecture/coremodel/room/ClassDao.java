package google.architecture.coremodel.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 班级
 * @since 2018/09/11
 */
@Dao
public interface ClassDao {

    @Query("SELECT * FROM tb_class Where id = :id")
    Flowable<ClassEntity> getClassEntityById(long id);
}
