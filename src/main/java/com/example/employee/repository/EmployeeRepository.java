package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //以下所有的*都代表变量

    //1.查询名字是*的第一个employee
    @Query(value = "select * from Employee  where name=?1 limit 1", nativeQuery = true)
    Employee findSomeOneByName(String name);

    //2.找出Employee表中第一个姓名包含`*`字符并且薪资大于*的雇员个人信息
    @Query(value = "select * from Employee where name like %?1% and salary>?2 limit 1", nativeQuery = true)
    Employee findSomeOneByNameLikeAndSalaryGreaterThan(String likeName, int salary);

    //3.找出一个薪资最高且公司ID是*的雇员以及该雇员的姓名
    @Query(value = "select name from Employee where companyId=?1 order by salary desc limit 1", nativeQuery = true)
    String getNameWhoseSalayIsHighestInGivenCompany(int companyId);

    //4.实现对Employee的分页查询，每页两个数据
    @Query(value = "select * from Employee",
            countQuery = "select count(*) from Employee",
            nativeQuery = true)
    Page<Employee> divideIntoPages(Pageable pageable);

    //5.查找**的所在的公司的公司名称
    @Query(value = "select c.companyName from Employee e  left join  Company c on e.companyId=c.id where e.name=?1", nativeQuery = true)
    String getCompanyNameOfGivenEmployeeName(String name);

    //6.将*的名字改成*,输出这次修改影响的行数
    @Modifying
    @Transactional
    @Query(value = "update Employee set name=?2 where name=?1", nativeQuery = true)
    int updateName(String name, String newName);

    //7.删除姓名是*的employee
    @Modifying
    @Transactional
    @Query(value = "delete from Employee where name=?1", nativeQuery = true)
    void dropByName(String name);
}
