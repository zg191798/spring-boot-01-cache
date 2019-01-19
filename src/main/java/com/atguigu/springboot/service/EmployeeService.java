package com.atguigu.springboot.service;

import com.atguigu.springboot.bean.Employee;
import com.atguigu.springboot.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

/**
 * @author zhangge
 * @date 2019/1/19 - 13:37
 */
@CacheConfig(cacheNames = "emp") // 可以省略下面的value="emp"
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的运行结果进行缓存,以后再相同的数据，直接从缓存中获取不用调用方法
     * CacheManager 管理多个Cache组件的,对缓存的真正CRUD操作在Cache组件中,每一个缓存组件有自己唯一一个名字
     * 几个属性
     * cacheNames/value:指定缓存组件的名字
     * key；缓存数据使用的Key,可以用它来只当,默认是使用方法参数的值
     * keyGenerator:key的生成器,可以自己指定Key的生成器
     * cacheManager:指定缓存管理器,或者cacheResolver指定获取解析器
     * condition:指定符合条件的情况下才进行缓存
     * unless:否定缓存,当unless指定的条件为true,方法的返回值就不会被缓存
     * ,condition = "#id>0",unless = "#result==null"
     * key = "#root.method+'['+#id+']'",
     * ,keyGenerator = "myKeyGenerator"
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "emp")
    public Employee getEmp(Integer id){
        System.out.println("查询 "+id+" 员工");
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }

    /**
     * @CachePut;既调用方法,又更新缓存数据
     * 修改了数据库的某个数据,同时更新缓存
     * 先调用方法,再将木匾方法的结果缓存
     * key:传入的emmployee对象,值:返回的empoloyee对象
     */
    @CachePut(value = "emp",key = "#employee.id")
    public Employee updateEmp(Employee employee) {
        System.out.println("updateEmp:"+employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     * @CacheEvict；缓存清除
     * key:指定要清除的数据
     * allEntries:清空缓存中所有数据
     * beforeInvocation = false; 缓存的清除是否在方法执行之前
     * 默认代表是在方法执行之后执行
     */
    @CacheEvict(value = "emp",key = "#id")
    public void deleteEmp(Integer id){
        System.out.println("deleteEmp:"+id);
        //employeeMapper.deleteEmpById(id);
    }

    @Caching(
            cacheable = {
                    @Cacheable(value = "emp",key="#lastName")
            },
            put = { // 方法执行前调用,放入缓存id,email都可以获取
                    @CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName){
        return employeeMapper.getEmpByLastName(lastName);
    }
}
