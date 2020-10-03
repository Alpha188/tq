package com.alpha.controller;

import com.alpha.common.ResultDTO;
import com.alpha.dto.DataDTO;
import com.alpha.dto.InfoDTO;
import com.alpha.mapper.BabelExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/analyse")
public class AnalyseController {
    @Autowired
    private BabelExtMapper babelExtMapper;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * ==================获取页面=======================
     */
    @GetMapping("/data")
    public ModelAndView getMainPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/analyse/data");
        return modelAndView;
    }

    @GetMapping("/behaviour")
    public ModelAndView geBehaviourPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/analyse/behaviour");
        return modelAndView;
    }

    /**
     * ==================接口=======================
     */
    @GetMapping("/chapter")
    public ResultDTO getData(@RequestParam(value = "startDate", required = false) String startDateStr,
                             @RequestParam(value = "endDate", required = false) String endDateStr) throws ParseException {
        if (StringUtils.isEmpty(startDateStr)) {
            //默认为7天前
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -6);
            startDateStr = sdf.format(c.getTime());
        }
        if (StringUtils.isEmpty(endDateStr)) {
            //默认为今天
            endDateStr = sdf.format(new Date());
        }
        DataDTO dataDTO = new DataDTO();
        List<String> names = babelExtMapper.listNames();
        List<String> dates = getDateByRange(startDateStr, endDateStr);
        dataDTO.setLegend(names);
        dataDTO.setXAxis(dates);

        List<InfoDTO> series = new ArrayList<>();
        for (String name : names) {
            List<Integer> data = new ArrayList<>();
            InfoDTO infoDTO = new InfoDTO();
            for (String date : dates) {
                List<Integer> chapterCountList = babelExtMapper.listChapterCountByNameAndReadTime(name, date);
                int chapterCount = 0;
                if (chapterCountList.size() != 0) {
                    chapterCount = chapterCountList.stream().reduce(0, Integer::sum);
                }
                data.add(chapterCount);
            }
            infoDTO.setData(data);
            infoDTO.setName(name);
            series.add(infoDTO);
        }
        dataDTO.setSeries(series);
        return ResultDTO.okOf(dataDTO);
    }

    @GetMapping("/count")
    public ResultDTO getCountData() throws ParseException {
        DataDTO dataDTO = new DataDTO();
        List<String> names = babelExtMapper.listNames();
        dataDTO.setLegend(Arrays.asList("章节总数", "打卡天数"));
        dataDTO.setXAxis(names);

        List<InfoDTO> series = new ArrayList<>();

        //设置章节总数
        InfoDTO infoDTO1 = new InfoDTO();
        List<Integer> chapterCountTotalList = new ArrayList<>();
        infoDTO1.setName("章节总数");
        for (String name : names) {
            chapterCountTotalList.add(babelExtMapper.sumChapterCountByName(name));
        }
        infoDTO1.setData(chapterCountTotalList);
        //设置打卡天数
        InfoDTO infoDTO2 = new InfoDTO();
        List<Integer> DayTotalList = new ArrayList<>();
        infoDTO2.setName("打卡天数");
        for (String name : names) {
            DayTotalList.add(babelExtMapper.countReadTimeByName(name));
        }
        infoDTO2.setData(DayTotalList);

        //设置series
        series.add(infoDTO1);
        series.add(infoDTO2);
        dataDTO.setSeries(series);
        return ResultDTO.okOf(dataDTO);
    }


    @GetMapping("/behaviour/time")
    public ResultDTO getBehaviourTimeData() throws ParseException {
        DataDTO dataDTO = new DataDTO();
        List<String> names = babelExtMapper.listNames();
        dataDTO.setLegend(Arrays.asList("上午(6:00~11:59)", "下午(12:00~17:59)", "晚上(18:00~23:59)", "半夜(00:00~05:59)"));
        dataDTO.setXAxis(names);

        List<InfoDTO> series = new ArrayList<>();

        List<Integer> morningCountList = new ArrayList<>();
        List<Integer> afternoonCountList = new ArrayList<>();
        List<Integer> eveningCountList = new ArrayList<>();
        List<Integer> nightCountList = new ArrayList<>();
        //根据姓名统计上午、下午、晚上、半夜的打卡
        for (String name : names) {
            morningCountList.add(babelExtMapper.countMorningByName(name));
            afternoonCountList.add(babelExtMapper.countAfternoonByName(name));
            eveningCountList.add(babelExtMapper.countEveningByName(name));
            nightCountList.add(babelExtMapper.countNightByName(name));
        }
        //设置infoDTO
        InfoDTO morning = new InfoDTO();
        morning.setName("上午(6:00~11:59)");
        morning.setData(morningCountList);
        series.add(morning);

        InfoDTO afternoon = new InfoDTO();
        afternoon.setName("下午(12:00~17:59)");
        afternoon.setData(afternoonCountList);
        series.add(afternoon);

        InfoDTO evening = new InfoDTO();
        evening.setName("晚上(18:00~23:59)");
        evening.setData(eveningCountList);
        series.add(evening);

        InfoDTO night = new InfoDTO();
        night.setName("半夜(00:00~05:59)");
        night.setData(nightCountList);
        series.add(night);

        //设置series

        dataDTO.setSeries(series);
        return ResultDTO.okOf(dataDTO);
    }

    private List<String> getDateByRange(String startDateStr, String endDateStr) throws ParseException {
        List<String> result = new ArrayList<>();
        result.add(startDateStr);
        if (startDateStr.equals(endDateStr)) {
            return result;
        }
        Date startDate = sdf.parse(startDateStr);

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        while (true) {
            //利用Calendar 实现 Date日期+1天
            c.add(Calendar.DAY_OF_MONTH, 1);
            Date tmp = c.getTime();
            result.add(sdf.format(tmp));
            if (sdf.format(tmp).equals(endDateStr)) {
                break;
            }
        }
        return result;
    }


}
