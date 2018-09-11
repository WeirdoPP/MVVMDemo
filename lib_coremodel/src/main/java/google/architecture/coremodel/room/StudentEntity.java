package google.architecture.coremodel.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 学生对象
 * @since 2018/09/11
 */
@Entity(tableName = "tb_student",//定义表名
        indices = @Index(value = {"name", "sex"}, unique = true))//定义索引
public class StudentEntity {

//    /**
//     * 这是表对象注解注释
//     */
//    public @interface Entity {
//        //定义表名
//        String tableName() default "";
//        //定义索引
//        Index[] indices() default {};
//        //设为true则父类的索引会自动被当前类继承
//        boolean inheritSuperIndices() default false;
//        //定义主键
//        String[] primaryKeys() default {};
//        //定义外键
//        ForeignKey[] foreignKeys() default {};
//    }

//    /**
//     * 这是Index注解注释
//     */
//    public @interface Index {
//        //定义需要添加索引的字段
//        String[] value();
//        //定义索引的名称
//        String name() default "";
//        //true-设置唯一键，标识value数组中的索引字段必须是唯一的，不可重复
//        boolean unique() default false;
//    }

//    /**
//     * 这是外键注解注释
//     */
//    public @interface ForeignKey {
//        //引用外键的表的实体
//        Class entity();
//        //要引用的外键列
//        String[] parentColumns();
//        //要关联的列
//        String[] childColumns();
//        //当父类实体(关联的外键表)从数据库中删除时执行的操作
//        @Action int onDelete() default NO_ACTION;
//        //当父类实体(关联的外键表)更新时执行的操作
//        @Action int onUpdate() default NO_ACTION;
//        //在事务完成之前，是否应该推迟外键约束
//        boolean deferred() default false;
//        //给onDelete，onUpdate定义的操作
//        int NO_ACTION = 1;
//        int RESTRICT = 2;
//        int SET_NULL = 3;
//        int SET_DEFAULT = 4;
//        int CASCADE = 5;
//        @IntDef({NO_ACTION, RESTRICT, SET_NULL, SET_DEFAULT, CASCADE})
//        @interface Action {
//        }
//    }

    @PrimaryKey //定义主键
    private long id;
    @ColumnInfo(name = "name")//定义数据表中的字段名
    private String name;
    @ColumnInfo(name = "sex")
    private int sex;
    @Ignore//指示Room需要忽略的字段或方法
    private String ignoreText;
    @ColumnInfo(name = "class_id")
    private String class_id;

    //setter and getter


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIgnoreText() {
        return ignoreText;
    }

    public void setIgnoreText(String ignoreText) {
        this.ignoreText = ignoreText;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }


}
