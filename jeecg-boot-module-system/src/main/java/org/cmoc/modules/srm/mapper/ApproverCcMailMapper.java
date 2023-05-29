package org.cmoc.modules.srm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cmoc.modules.srm.entity.ApproverCcMail;
import org.cmoc.modules.system.entity.SysUser;

import java.util.List;

/**
 * @Description: approver_cc_mail
 * @Author: jeecg-boot
 * @Date:   2023-02-20
 * @Version: V1.0
 */
public interface ApproverCcMailMapper extends BaseMapper<ApproverCcMail> {
    /**
     * @param
     * @param ids
     * @return List<SysUser>
     * @author Kevin.Wang
     * @date : 2023/2/20 16:24
     **/
    List<SysUser> selectUserMails(@Param("ids") String ids);
    /**
     *
     * @author Kevin.Wang
     * @date : 2023/2/20 16:41
     * @param
     * @return String
     **/
    String getCcUsersId();
}
