package com.alpha.controller;

import com.alpha.common.ResultDTO;
import com.alpha.dto.TimeLineDTO;
import com.alpha.mapper.BabelExtMapper;
import com.alpha.mapper.BabelMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TimeLineController {
    @Autowired
    private BabelExtMapper babelExtMapper;
    @Autowired
    private BabelMapper babelMapper;

    @GetMapping("/timeline")
    public ModelAndView getPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", defaultValue = "8") Integer limit,
                                ModelAndView modelAndView) {
        PageHelper.startPage(page, limit);
        List<String> dates = babelExtMapper.listDate(true);
        PageInfo<String> pageInfo = new PageInfo<String>(dates);
        modelAndView.addObject("pageTotal", pageInfo.getTotal());
        modelAndView.setViewName("timeline");
        return modelAndView;
    }

    @GetMapping("/timeline/list")
    public ResultDTO list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "limit", defaultValue = "8") Integer limit) {
        //分页获取日期
        PageHelper.startPage(page, limit);
        List<String> dates = babelExtMapper.listDate(true);
        PageInfo<String> pageInfo = new PageInfo<String>(dates);
        //设置timeLineDTO
        List<TimeLineDTO> timeLineDTOS = pageInfo.getList().stream().map(date -> {
            TimeLineDTO timeLineDTO = new TimeLineDTO();
            timeLineDTO.setDate(date);
            List<String> readNames = babelExtMapper.listNameByReadDate(date);
            timeLineDTO.setReadNames(readNames);
            timeLineDTO.setUnreadNames(arrContrast(babelExtMapper.listNames(), readNames));
            return timeLineDTO;
        }).collect(Collectors.toList());
        //设置时间线内容
        return ResultDTO.okOf(pageInfo.getTotal(), timeLineDTOS);
    }

    // 数组相减
    private List<String> arrContrast(List<String> arr1, List<String> arr2) {
        List<String> list = new LinkedList<String>();
        for (String str : arr1) { // 处理第一个数组,list里面的值为1,2,3,4
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : arr2) { // 如果第二个数组存在和第一个数组相同的值，就删除
            if (list.contains(str)) {
                list.remove(str);
            }
        }
        return list;
    }
}
