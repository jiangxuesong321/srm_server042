package org.jeecg.modules.srm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.srm.entity.InquiryList;
import org.jeecg.modules.srm.entity.InquiryRecord;
import org.jeecg.modules.srm.entity.InquirySupplier;
import org.jeecg.modules.srm.service.IInquiryListService;
import org.jeecg.modules.srm.service.IInquiryRecordService;
import org.jeecg.modules.srm.service.IInquirySupplierService;
import org.jeecg.modules.srm.vo.InquiryListPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: 询价单主表
* @Author: jeecg-boot
* @Date:   2022-06-18
* @Version: V1.0
*/
@RestController
@RequestMapping("/srm/inquiryRecordList")
@Slf4j
public class InquiryRecordController {

    @Autowired
    private IInquiryRecordService inquiryRecordService;

   /**
    * 分页列表查询
    *
    * @param inquiryRecord
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "询价单主表-分页列表查询")
   @ApiOperation(value="询价单主表-分页列表查询", notes="询价单主表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<InquiryRecord>> queryPageList(InquiryRecord inquiryRecord,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<InquiryRecord> queryWrapper = QueryGenerator.initQueryWrapper(inquiryRecord, req.getParameterMap());
       Page<InquiryRecord> page = new Page<InquiryRecord>(pageNo, pageSize);
       IPage<InquiryRecord> pageList = inquiryRecordService.page(page, queryWrapper);
       return Result.OK(pageList);
   }


}
