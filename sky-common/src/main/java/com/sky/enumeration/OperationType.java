package com.sky.enumeration;

/**
 * 数据库操作类型
 */
public enum OperationType {

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 插入操作
     */
    INSERT
    /*1.记录操作日志
    比如你在做一个 AOP 切面，拦截所有增删改操作时，就可以区分是更新还是插入：

    public void log(OperationType type, String tableName) {
        if (type == OperationType.UPDATE) {
            System.out.println("执行了更新操作: " + tableName);
        } else if (type == OperationType.INSERT) {
            System.out.println("执行了插入操作: " + tableName);
        }
    }


    调用：

    log(OperationType.UPDATE, "employee");


    注解标记方法

    有些系统会配合注解 + 枚举来标记方法的操作类型，比如：

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AutoFill {
        OperationType value();
    }


    然后在方法上使用：

    @AutoFill(OperationType.INSERT)
    public void saveEmployee(Employee emp) {
        // 插入员工
    }


    这样在切面里就能通过 annotation.value() 判断是插入还是更新，再自动填充 createTime、updateTime 等字段。*/
}
