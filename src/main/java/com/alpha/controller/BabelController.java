package com.alpha.controller;

import com.alpha.common.ResultDTO;
import com.alpha.dto.InfoSaveDTO;
import com.alpha.dto.jingwenDTO;
import com.alpha.entity.Babel;
import com.alpha.entity.BabelExample;
import com.alpha.mapper.BabelExtMapper;
import com.alpha.mapper.BabelMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/babel")
public class BabelController {
    private final Pattern CHAPTER_COUNT_REG = Pattern.compile("([0-9]+)(-|—)?([0-9]+)");
    private final Pattern JINGWEN_REG = Pattern.compile("(姣姐|王姣|林南|恩江|梁许|谢作曼|张艺慧|郑丹丹|何媛媛|薛明惠|吴加会|颜双双|童娴馨)(.*\\d+)");

    @Autowired
    private BabelMapper babelMapper;
    @Autowired
    private BabelExtMapper babelExtMapper;

    /**
     * ==================获取页面=======================
     */
    @GetMapping("/edit")
    public ModelAndView getEditPage(@RequestParam("id") Integer id, ModelAndView modelAndView) {
        modelAndView.setViewName("/system/babel/edit");
        modelAndView.addObject("data", babelMapper.selectByPrimaryKey(id));
        return modelAndView;
    }

    @GetMapping("/main")
    public ModelAndView getMainPage(ModelAndView modelAndView) {
        modelAndView.setViewName("/system/babel/main");
        modelAndView.addObject("names", babelExtMapper.listNames());
        return modelAndView;
    }

    @GetMapping("/parser")
    public ModelAndView getParserPage(ModelAndView modelAndView) {
        modelAndView.setViewName("system/parser/main");
        return modelAndView;
    }

    @GetMapping("/parser/add")
    public ModelAndView getParserAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("system/parser/add");
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView getAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("system/babel/add");
        return modelAndView;
    }

    /**
     * ==================接口=======================
     */
    @PostMapping("/parser")
    public ResultDTO parser(@Validated @RequestBody jingwenDTO dto, BindingResult result) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        //分割成数组
        String[] strings = dto.getRecord().split("\\n");
        for (String info : strings) {
            //去除所有空格
            info = info.replace(" ", "");
            Matcher m = JINGWEN_REG.matcher(info);
            if (m.find()) {
                //获取经文
                String jingwen = m.group(2);
                //获取人名
                String name = m.group(1);
                HashMap<String, Object> map = new HashMap<>(4);
                map.put("name", name);
                map.put("jingwen", jingwen);
                map.put("chapterCount", getChapterCount(jingwen));
                resultList.add(map);
            }
        }
        return ResultDTO.okOf(resultList);
    }

    private Integer getChapterCount(String jingwen) {
        if (StringUtils.isEmpty(jingwen)) {
            return null;
        }
        List<Integer> chapterArr = new ArrayList<>();
        String regEx = "[^0-9]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(jingwen);
        String[] split = m.replaceAll(" ").split(" ");
        for (String s : split) {
            if (!StringUtils.isEmpty(s)) {
                chapterArr.add(Integer.valueOf(s));
            }
        }
        if (chapterArr.size() == 0) {
            return null;
        }
        if (chapterArr.size() == 1) {
            return 1;
        }
        if (chapterArr.size() == 2) {
            if (jingwen.indexOf("-") == -1 && jingwen.indexOf("~") == -1)
                return 2;
            return chapterArr.get(1) - chapterArr.get(0) + 1;

        }
        if (chapterArr.size() == 3) {
            return chapterArr.get(1) - chapterArr.get(0) + 1 + 1;
        }
        if (chapterArr.size() == 4) {
            return chapterArr.get(1) - chapterArr.get(0) + 1 + chapterArr.get(3) - chapterArr.get(2) + 1;
        }
        return null;
    }


    @PostMapping("/save")
    public ResultDTO save(@Validated @RequestBody InfoSaveDTO dto, BindingResult result) {
        List<String> infos = dto.getInfos();
        List<Date> dates = dto.getDates();
        if (dates.size() != infos.size()) {
            return ResultDTO.errorOf();
        }
        for (int i = 0; i < infos.size(); i++) {
            Babel babel = new Babel();
            String[] s = infos.get(i).split(" ");
            if (s.length != 3) {
                return ResultDTO.errorOf();
            }
            babel.setJingwen(s[1]);
            babel.setName(s[0]);
            babel.setChapterCount(Integer.parseInt(s[2]));
            babel.setReadTime(dates.get(i));
            babelMapper.insertSelective(babel);
        }
        return ResultDTO.okOf();
    }

    @PostMapping("/save/one")
    public ResultDTO save(@RequestBody Babel babel) {
        if (babel.getId() != null) {
            babelMapper.updateByPrimaryKeySelective(babel);
            return ResultDTO.okOf();
        }
        babelMapper.insertSelective(babel);
        return ResultDTO.okOf();
    }

    @GetMapping("/list")
    public ResultDTO list(@RequestParam("page") Integer page,
                          @RequestParam("limit") Integer limit,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "readTimeRange", required = false) String readTimeRange) throws ParseException {
        PageHelper.startPage(page, limit);
        BabelExample example = new BabelExample();
        example.setOrderByClause("read_time desc");
        BabelExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(readTimeRange)) {
            String pat = "yyyy-MM-dd";
            SimpleDateFormat format2 = new SimpleDateFormat(pat);
            String[] split = readTimeRange.split("~");
            //生成时间对象
            Date startTime = format2.parse(split[0]);
            Date endTime = format2.parse(split[1]);
            criteria.andReadTimeGreaterThanOrEqualTo(startTime);
            criteria.andReadTimeLessThanOrEqualTo(endTime);
        }
        List<Babel> babels = babelMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(babels);
        return ResultDTO.okOf(pageInfo.getTotal(), babels);
    }

    @DeleteMapping("/del/{id}")
    public ResultDTO del(@PathVariable("id") Integer id) {
        babelMapper.deleteByPrimaryKey(id);
        return ResultDTO.okOf();
    }

    @DeleteMapping("/del/batch")
    public ResultDTO delBatch(@RequestBody Map<String, List<Integer>> map) {
        List<Integer> ids = map.get("ids");
        if (ids == null) {
            return ResultDTO.errorOf();
        }
        for (Integer id : ids) {
            babelMapper.deleteByPrimaryKey(id);
        }
        return ResultDTO.okOf();
    }

}
