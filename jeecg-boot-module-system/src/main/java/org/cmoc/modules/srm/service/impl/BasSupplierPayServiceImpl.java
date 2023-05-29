package org.cmoc.modules.srm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.cmoc.common.api.vo.Result;
import org.cmoc.common.system.vo.LoginUser;
import org.cmoc.common.util.ObjectCheck;
import org.cmoc.modules.srm.entity.BasMaterial;
import org.cmoc.modules.srm.entity.BasMaterialCategory;
import org.cmoc.modules.srm.entity.BasSupplierPay;
import org.cmoc.modules.srm.mapper.BasMaterialMapper;
import org.cmoc.modules.srm.mapper.BasSupplierPayMapper;
import org.cmoc.modules.srm.service.IBasMaterialCategoryService;
import org.cmoc.modules.srm.service.IBasMaterialService;
import org.cmoc.modules.srm.service.IBasSupplierPayService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 设备管理
 * @Author: jeecg-boot
 * @Date:   2022-06-16
 * @Version: V1.0
 */
@Service
public class BasSupplierPayServiceImpl extends ServiceImpl<BasSupplierPayMapper, BasSupplierPay> implements IBasSupplierPayService {

}
