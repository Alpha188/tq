package com.alpha.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BabelExtMapper {
    @Select("SELECT DISTINCT `name` FROM babel")
    List<String> listNames();

    /**
     * 获取所有日期 正序
     *
     * @return {@link List<String>}
     */

    List<String> listDate(@Param("desc") Boolean desc);

    /**
     * 根据name和日期获取读经的章节数
     *
     * @param name 的名字
     * @return {@link List<Integer>}
     */
    @Select("SELECT `chapter_count`  FROM babel WHERE name=#{name} and LEFT(`read_time`,10) = #{readDate}")
    List<Integer> listChapterCountByNameAndReadTime(@Param("name") String name, @Param("readDate") String readDate);


    @Select("SELECT sum(chapter_count) chapter_count FROM `babel` GROUP BY name HAVING name=#{name}")
    Integer sumChapterCountByName(@Param("name") String name);

    @Select("SELECT count(*) FROM `babel` where name=#{name}")
    Integer countReadTimeByName(String name);

    @Select("SELECT count(*) FROM babel where substring(read_time,11,3) >=6 and substring(read_time,11,3)<12 and name=#{name}")
    Integer countMorningByName(@Param("name") String name);

    @Select("SELECT count(*) FROM babel where substring(read_time,11,3) >=12 and substring(read_time,11,3)<18 and name=#{name}")
    Integer countAfternoonByName(@Param("name") String name);

    @Select("SELECT count(*) FROM babel where substring(read_time,11,3) >=18 and substring(read_time,11,3)<24 and name=#{name}")
    Integer countEveningByName(@Param("name") String name);

    @Select("SELECT count(*) FROM babel where substring(read_time,11,3) >=0 and substring(read_time,11,3)<6 and name=#{name}")
    Integer countNightByName(@Param("name") String name);

    @Select("SELECT name  FROM babel WHERE read_time like CONCAT(#{date},'%')")
    List<String> listNameByReadDate(@Param("date") String date);

}
