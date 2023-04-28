package org.jeecg.modules.srm.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.srm.entity.BiddingBarginRecord;
import org.jeecg.modules.srm.service.IBiddingBarginRecordService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.srm.vo.FixBiddingPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: bidding_bargin_record
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@RestController
@RequestMapping("/srm/biddingBarginRecord")
@Slf4j
public class BiddingBarginRecordController extends JeecgController<BiddingBarginRecord, IBiddingBarginRecordService> {
	@Autowired
	private IBiddingBarginRecordService biddingBarginRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param biddingBarginRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "bidding_bargin_record-分页列表查询")
	@ApiOperation(value="bidding_bargin_record-分页列表查询", notes="bidding_bargin_record-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BiddingBarginRecord>> queryPageList(BiddingBarginRecord biddingBarginRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BiddingBarginRecord> queryWrapper = QueryGenerator.initQueryWrapper(biddingBarginRecord, req.getParameterMap());
		Page<BiddingBarginRecord> page = new Page<BiddingBarginRecord>(pageNo, pageSize);
		IPage<BiddingBarginRecord> pageList = biddingBarginRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param biddingBarginRecord
	 * @return
	 */
	@AutoLog(value = "bidding_bargin_record-添加")
	@ApiOperation(value="bidding_bargin_record-添加", notes="bidding_bargin_record-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FixBiddingPage biddingBarginRecord) {
//		biddingBarginRecordService.save(biddingBarginRecord);
		biddingBarginRecordService.saveBargin(biddingBarginRecord);
		return Result.OK("提交成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param biddingBarginRecord
	 * @return
	 */
	@AutoLog(value = "bidding_bargin_record-编辑")
	@ApiOperation(value="bidding_bargin_record-编辑", notes="bidding_bargin_record-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BiddingBarginRecord biddingBarginRecord) {
		biddingBarginRecordService.updateById(biddingBarginRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "bidding_bargin_record-通过id删除")
	@ApiOperation(value="bidding_bargin_record-通过id删除", notes="bidding_bargin_record-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		biddingBarginRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "bidding_bargin_record-批量删除")
	@ApiOperation(value="bidding_bargin_record-批量删除", notes="bidding_bargin_record-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.biddingBarginRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "bidding_bargin_record-通过id查询")
	@ApiOperation(value="bidding_bargin_record-通过id查询", notes="bidding_bargin_record-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BiddingBarginRecord> queryById(@RequestParam(name="id",required=true) String id) {
		BiddingBarginRecord biddingBarginRecord = biddingBarginRecordService.getById(id);
		if(biddingBarginRecord==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(biddingBarginRecord);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param biddingBarginRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BiddingBarginRecord biddingBarginRecord) {
        return super.exportXls(request, biddingBarginRecord, BiddingBarginRecord.class, "bidding_bargin_record");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BiddingBarginRecord.class);
    }

	 /**
	  * 分页列表查询
	  *
	  * @param biddingBarginRecord
	  * @param req
	  * @return
	  */
	 @GetMapping(value = "/fetchPriceHistory")
	 public Result<List<BiddingBarginRecord>> fetchPriceHistory(BiddingBarginRecord biddingBarginRecord,HttpServletRequest req) {
		 List<BiddingBarginRecord> pageList = biddingBarginRecordService.fetchPriceHistory(biddingBarginRecord);
		 return Result.OK(pageList);
	 }

}
