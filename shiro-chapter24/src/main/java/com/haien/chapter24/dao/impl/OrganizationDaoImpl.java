package com.haien.chapter24.dao.impl;

import com.haien.chapter24.dao.OrganizationDao;
import com.haien.chapter24.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrganizationDaoImpl implements OrganizationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Organization createOrganization(final Organization organization) {
        final String sql = "insert into sys_organization( name, parent_id, parent_ids, available)" +
                " values(?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"});
                int count = 1;
                psst.setString(count++, organization.getName());
                psst.setLong(count++, organization.getParentId());
                psst.setString(count++, organization.getParentIds());
                psst.setBoolean(count++, organization.getAvailable());
                return psst;
            }
        }, keyHolder);
        organization.setId(keyHolder.getKey().longValue());
        return organization;
    }

    @Override
    public Organization updateOrganization(Organization organization) {
        final String sql = "update sys_organization set name=?, parent_id=?, parent_ids=?, " +
                "available=? where id=?";
        jdbcTemplate.update(
                sql,
                organization.getName(), organization.getParentId(), organization.getParentIds(),
                organization.getAvailable(), organization.getId());
        return organization;
    }

    @Override
    public void deleteOrganization(Long organizationId) {
        //删除该公司
        final String deleteSelfSql = "delete from sys_organization where id=?";
        jdbcTemplate.update(deleteSelfSql, organizationId);

        //删除所有子公司、孙子公司、...
        Organization organization = findOne(organizationId);
        final String deleteDescendantsSql = "delete from sys_organization where parent_ids like ?";
        //makeSelfAsParentIds：父编号+自编号
        jdbcTemplate.update(deleteDescendantsSql, organization.makeSelfAsParentIds() + "%");
    }


    @Override
    public Organization findOne(Long organizationId) {
        final String sql = "select id, name, parent_id, parent_ids, available from sys_organization" +
                " where id=?";
        List<Organization> organizationList = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Organization.class), organizationId);

        if(organizationList.size() == 0) {
            return null;
        }
        return organizationList.get(0);
    }

    @Override
    public List<Organization> findAll() {
        final String sql = "select id, name, parent_id, parent_ids, available from sys_organization";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class));
    }

    /**
     * @Author haien
     * @Description 查询所有公司，除了指定公司及其all子公司
     * @Date 2019/3/13
     * @Param [excludeOraganization]
     * @return java.util.List<com.haien.chapter16.entity.Organization>
     **/
    @Override
    public List<Organization> findAllWithExclude(Organization excludeOraganization) {
        //TODO 改成not exists 利用索引
        final String sql = "select id, name, parent_id, parent_ids, available from sys_organization" +
                " where id!=? and parent_ids not like ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class),
                excludeOraganization.getId(), excludeOraganization.makeSelfAsParentIds() + "%");
    }

    /**
     * @Author haien
     * @Description 将source移动到target下作为其子公司
     * @Date 2019/3/18
     * @Param [source, target]
     * @return void
     **/
    @Override
    public void move(Organization source, Organization target) {
        //将source移到target下作为其子公司
        String moveSourceSql = "update sys_organization set parent_id=?,parent_ids=? where id=?";
        jdbcTemplate.update(moveSourceSql,
              target.getId(), target.getParentIds()+target.getId()+"/", source.getId());

        //将source原all子公司的parent_ids更新为source现parent_ids+与source间隔代数
        String moveSourceDescendantsSql = "update sys_organization set parent_ids=" +
                "concat(?, substring(parent_ids, length(?))) where parent_ids like ?";
        //substring剪去source及以上所有父公司，截取后面与source间的隔代
        jdbcTemplate.update(moveSourceDescendantsSql,
                target.makeSelfAsParentIds(), source.makeSelfAsParentIds(),
                source.makeSelfAsParentIds() + "%");
    }
}
