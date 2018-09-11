package google.architecture.coremodel.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 班级对象
 * @since 2018/09/11
 */

@Entity(tableName = "tb_class")
public class ClassEntity {

    @PrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
