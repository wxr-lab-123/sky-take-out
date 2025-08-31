package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

        /**
     * 保存员工信息
     *
     * @param employeeDTO 员工数据传输对象，包含员工的基本信息
     */
    void save(EmployeeDTO employeeDTO);


        /**
     * 分页查询员工信息
     *
     * @param employeePageQueryDTO 员工分页查询条件对象，包含分页参数和查询条件
     * @return PageResult 分页查询结果对象，包含员工信息列表和分页信息
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    void stautsUpdate(Integer status, long id);

    Employee getById(Long id);

    void Update(EmployeeDTO employeeDTO);
}
