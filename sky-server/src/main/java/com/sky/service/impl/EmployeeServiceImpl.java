package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //明文密码 MD5 处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号状态
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码
        employee.setPassword(DigestUtils.md5DigestAsHex(
                PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //设置时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建人
        /*String jwt = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(),jwt);
        long userid = ((Integer) claims.get(JwtClaimsConstant.EMP_ID)).longValue();*/
        long userid = BaseContext.getCurrentId();
        //ThreadLocal 每一个请求自动创建一个线程变量, 线程变量保存当前登录用户id,已经创建
        //BaseContext类来封装线程变量, 获取当前登录用户id
        employee.setCreateUser(userid);
        employee.setUpdateUser( userid);

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询员工信息
     *
     * @param employeePageQueryDTO 员工分页查询条件对象，包含分页参数和查询条件
     * @return PageResult 分页查询结果对象，包含员工信息列表和分页信息
     */
    @Override
    public PageResult page(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> res = employeeMapper.page(employeePageQueryDTO);
        PageResult pageResult = new PageResult(res.getTotal(), res.getResult());
        return pageResult;
    }

    @Override
    public void stautsUpdate(Integer status, long id) {
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employeeMapper.Update(employee);
    }

    /**
     * 根据员工ID获取员工信息
     *
     * @param id 员工ID
     * @return 员工对象，其中密码字段被替换为掩码显示
     */
    @Override
    public Employee getById(Long id) {
        // 从数据库获取员工信息
        Employee employee = employeeMapper.getById(id);
        // 隐藏密码信息，避免敏感数据泄露
        employee.setPassword("****");
        return  employee;
    }


    /**
     * 更新员工信息
     * @param employeeDTO 员工数据传输对象，包含需要更新的员工信息
     */
    @Override
    public void Update(EmployeeDTO employeeDTO) {
       // 将DTO对象转换为实体对象
       Employee employee = new Employee();
       BeanUtils.copyProperties(employeeDTO, employee);

       // 设置更新时间和更新人信息
       employee.setUpdateTime(LocalDateTime.now());
       employee.setUpdateUser(BaseContext.getCurrentId());

       // 调用mapper执行更新操作
       employeeMapper.Update(employee);
    }


}
