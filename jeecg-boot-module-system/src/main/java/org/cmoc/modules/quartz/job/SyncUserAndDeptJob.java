package org.cmoc.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cmoc.common.constant.CommonConstant;
import org.cmoc.common.util.PasswordUtil;
import org.cmoc.modules.srm.entity.BasSupplier;
import org.cmoc.modules.srm.entity.OaEntity;
import org.cmoc.modules.srm.entity.SupplierAccount;
import org.cmoc.modules.srm.service.IBasSupplierService;
import org.cmoc.modules.srm.service.IOaService;
import org.cmoc.modules.srm.service.ISupplierAccountService;
import org.cmoc.modules.system.entity.SysDepart;
import org.cmoc.modules.system.entity.SysUser;
import org.cmoc.modules.system.service.ISysDepartService;
import org.cmoc.modules.system.service.ISysUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 供应商解冻
 * 
 * @Author Scott
 */
@Slf4j
public class SyncUserAndDeptJob implements Job {
	@Autowired
	private IOaService iOaService;
	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private ISysDepartService iSysDepartService;
	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(" 同步人员、部门信息：");
		Date nowTime = new Date();
		List<OaEntity> userList = iOaService.queryUserList();
		if(userList != null && userList.size() > 0){
			List<SysUser> sysUserList = new ArrayList<>();
			for(OaEntity u : userList){
				//判断人员是否存在
				SysUser user = iSysUserService.getById(u.getId());
				if(user == null){
					user = new SysUser();

					user.setId(u.getId());
					user.setUsername(u.getWorkcode());
					user.setRealname(u.getLastname());
					String salt = "RCGTeGiH";
					String password = PasswordUtil.encrypt(u.getWorkcode(), "123456", salt);
					user.setPassword(password);
					user.setSalt(salt);
					user.setSex("男".equals(u.getSex()) ? 1 : 2);
					user.setWorkNo(u.getWorkcode());
				}
				user.setEmail(u.getEmail());
				user.setStatus("在岗-正常".equals(u.getGwzt()) ? 1 : 2);
				user.setDelFlag(0);
				user.setCreateTime(nowTime);
				user.setUpdateTime(nowTime);
				if(StringUtils.isNotEmpty(u.getFiveid()) && !"0".equals(u.getFiveid())){
					user.setDepartIds(u.getFiveid());
					if(StringUtils.isNotEmpty(u.getFivedep())){
						if(u.getFivedep().contains("天津")){
							user.setRelTenantIds("2");
						}else if(u.getFivedep().contains("宜兴")){
							user.setRelTenantIds("1");
						}else if(u.getFivedep().contains("内蒙")){
							user.setRelTenantIds("3");
						}else if(u.getFivedep().contains("徐州")){
							user.setRelTenantIds("4");
						}
					}

				}else if(StringUtils.isNotEmpty(u.getFourid()) && !"0".equals(u.getFourid())){
					user.setDepartIds(u.getFourid());
					if(StringUtils.isNotEmpty(u.getFourdep())){
						if(u.getFourdep().contains("天津")){
							user.setRelTenantIds("2");
						}else if(u.getFourdep().contains("宜兴")){
							user.setRelTenantIds("1");
						}else if(u.getFourdep().contains("内蒙")){
							user.setRelTenantIds("3");
						}else if(u.getFourdep().contains("徐州")){
							user.setRelTenantIds("4");
						}
					}

				}else if(StringUtils.isNotEmpty(u.getThirthid()) && !"0".equals(u.getThirthid())){
					user.setDepartIds(u.getThirthid());
					if(StringUtils.isNotEmpty(u.getThirthdep())){
						if(u.getThirthdep().contains("天津")){
							user.setRelTenantIds("2");
						}else if(u.getThirthdep().contains("宜兴")){
							user.setRelTenantIds("1");
						}else if(u.getThirthdep().contains("内蒙")){
							user.setRelTenantIds("3");
						}else if(u.getThirthdep().contains("徐州")){
							user.setRelTenantIds("4");
						}
					}

				}else if(StringUtils.isNotEmpty(u.getSecendid()) && !"0".equals(u.getSecendid())){
					user.setDepartIds(u.getSecendid());
					if(StringUtils.isNotEmpty(u.getSecenddep())){
						if(u.getSecenddep().contains("天津")){
							user.setRelTenantIds("2");
						}else if(u.getSecenddep().contains("宜兴")){
							user.setRelTenantIds("1");
						}else if(u.getSecenddep().contains("内蒙")){
							user.setRelTenantIds("3");
						}else if(u.getSecenddep().contains("徐州")){
							user.setRelTenantIds("4");
						}
					}

				}else if(StringUtils.isNotEmpty(u.getFirstid()) && !"0".equals(u.getFirstid())){
					user.setDepartIds(u.getFirstid());
					if(StringUtils.isNotEmpty(u.getFirstdep())){
						if(u.getFirstdep().contains("天津")){
							user.setRelTenantIds("2");
						}else if(u.getFirstdep().contains("宜兴")){
							user.setRelTenantIds("1");
						}else if(u.getFirstdep().contains("内蒙")){
							user.setRelTenantIds("3");
						}else if(u.getFirstdep().contains("徐州")){
							user.setRelTenantIds("4");
						}
					}

				}

				sysUserList.add(user);
			}
			iSysUserService.saveOrUpdateBatch(sysUserList);
		}

		List<OaEntity> deptList = iOaService.queryDeptList();
		List<SysDepart> departList = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		if(deptList != null && deptList.size() > 0){
			for(OaEntity d : deptList){
				//最多5层
				if(StringUtils.isNotEmpty(d.getFirstid())){
					if(!map.containsKey(d.getFirstid())){
						map.put(d.getFirstid(),d.getFirstid());

						SysDepart depart = new SysDepart();
						depart.setId(d.getFirstid());
						depart.setParentId(null);
						depart.setDepartName(d.getFirstdep());
						depart.setDepartOrder(0);
						depart.setOrgCategory("1");
						depart.setOrgCode(d.getFirstid());
						depart.setDelFlag("0");
						depart.setCreateBy("admin");
						depart.setCreateTime(nowTime);
						depart.setUpdateBy("admin");
						depart.setUpdateTime(nowTime);
						departList.add(depart);
					}
				}
				if(StringUtils.isNotEmpty(d.getSecendid())){
					if(!map.containsKey(d.getSecendid())){
						SysDepart depart = new SysDepart();
						depart.setId(d.getSecendid());
						depart.setParentId(d.getFirstid());
						depart.setDepartName(d.getSecenddep());
						depart.setDepartOrder(0);
						depart.setOrgCategory("2");
						depart.setOrgCode(d.getSecendid());
						depart.setDelFlag("0");
						depart.setCreateBy("admin");
						depart.setCreateTime(nowTime);
						depart.setUpdateBy("admin");
						depart.setUpdateTime(nowTime);
						departList.add(depart);
					}
				}
				if(StringUtils.isNotEmpty(d.getThirdid())){
					if(!map.containsKey(d.getThirdid())){
						SysDepart depart = new SysDepart();
						depart.setId(d.getThirdid());
						depart.setParentId(d.getSecendid());
						depart.setDepartName(d.getThirddep());
						depart.setDepartOrder(0);
						depart.setOrgCategory("3");
						depart.setOrgCode(d.getThirdid());
						depart.setDelFlag("0");
						depart.setCreateBy("admin");
						depart.setCreateTime(nowTime);
						depart.setUpdateBy("admin");
						depart.setUpdateTime(nowTime);
						departList.add(depart);
					}
				}
				if(StringUtils.isNotEmpty(d.getFourid())){
					if(!map.containsKey(d.getFourid())){
						SysDepart depart = new SysDepart();
						depart.setId(d.getFourid());
						depart.setParentId(d.getThirdid());
						depart.setDepartName(d.getFourdep());
						depart.setDepartOrder(0);
						depart.setOrgCategory("4");
						depart.setOrgCode(d.getFourid());
						depart.setDelFlag("0");
						depart.setCreateBy("admin");
						depart.setCreateTime(nowTime);
						depart.setUpdateBy("admin");
						depart.setUpdateTime(nowTime);
						departList.add(depart);
					}
				}
				if(StringUtils.isNotEmpty(d.getFiveid())){
					if(!map.containsKey(d.getFiveid())){
						SysDepart depart = new SysDepart();
						depart.setId(d.getFiveid());
						depart.setParentId(d.getFourid());
						depart.setDepartName(d.getFivedep());
						depart.setDepartOrder(0);
						depart.setOrgCategory("5");
						depart.setOrgCode(d.getFiveid());
						depart.setDelFlag("0");
						depart.setCreateBy("admin");
						depart.setCreateTime(nowTime);
						depart.setUpdateBy("admin");
						depart.setUpdateTime(nowTime);
						departList.add(depart);
					}
				}
			}
		}
		if(departList != null && departList.size() > 0){
			iSysDepartService.saveOrUpdateBatch(departList);
		}
	}
}
