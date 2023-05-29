package com.cmoc.modules.srm.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmoc.modules.srm.entity.OaEntity;
import com.cmoc.modules.srm.mapper.OaMapper;
import com.cmoc.modules.srm.service.IOaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 合同标的
 * @Author: jeecg-boot
 * @Date:   2022-06-21
 * @Version: V1.0
 */
@Service
@DS("oa_db")
public class OaServiceImpl extends ServiceImpl<OaMapper, OaEntity> implements IOaService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 同步人员
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<OaEntity> queryUserList() throws Exception {
        List<OaEntity> userList = new ArrayList<>();
        // 查询数据的SQL语句
        String sql = "SELECT * FROM dcadmin.HR_INFO " ;
        userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OaEntity.class));
        return userList;
    }

    /**
     * 同步部门
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<OaEntity> queryDeptList() throws Exception {
        List<OaEntity> deptList = new ArrayList<>();
        // 查询数据的SQL语句
        String sql = "SELECT * FROM dcadmin.HRM_COM" ;
        deptList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OaEntity.class));
        return deptList;
    }
}
