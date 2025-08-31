package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user) VALUES (" +
            "#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Employee employee);

    Page<Employee> page(EmployeePageQueryDTO employeePageQueryDTO);


        /**
     * 更新员工信息
     *
     * @param employee 需要更新的员工对象，包含更新后的员工信息
     */
    void Update(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
