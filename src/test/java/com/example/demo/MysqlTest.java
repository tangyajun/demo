package com.example.demo;

import com.example.demo.dao.DepartmentRepository;
import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.entity.Department;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfiguration.class})
public class MysqlTest {
    private static Logger logger= LoggerFactory.getLogger(MysqlTest.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RoleRepository roleRepository;

    @Before
    public void initData() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
        Department department=new Department();
        department.setName("开发部");
        departmentRepository.save(department);
        org.junit.Assert.assertNotNull(department.getId());
        Role role=new Role();
        role.setName("admin");
        roleRepository.save(role);
        org.junit.Assert.assertNotNull(role.getId());
        User user=new User();
        user.setName("user");
        user.setCreatedate(new Date());
        user.setDepartment(department);
        List<Role> roles=roleRepository.findAll();
        org.junit.Assert.assertNotNull(roles);
        user.setRoles(roles);
        userRepository.save(user);
        org.junit.Assert.assertNotNull(user.getId());
    }

    @Test
    public void findPage() {
        Pageable pageable=new PageRequest(0,10,new Sort(Sort.Direction.ASC,"id"));
        Page<User> page=userRepository.findAll(pageable);
        org.junit.Assert.assertNotNull(page);
        for (User user : page.getContent()) {
            logger.info("=============user===========user name:{},department name:{},role name:{}",
                    user.getName(),user.getDepartment(),user.getRoles().get(0).getName());
        }
    }
}
