package com.nageoffer.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nageoffer.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//不同的用户访问短链接uv才+1
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {

    /**
     * 记录基础访问监控数据
     */
    @Insert("INSERT INTO t_link_access_stats (full_short_url, gid, date, pv, uv, uip, hour, weekday, create_time, update_time, del_flag)" +
            "VALUES(#{linkAccessStats.fullShortUrl}, #{linkAccessStats.gid}, #{linkAccessStats.date}, #{linkAccessStats.pv}," +
            "#{linkAccessStats.uv}, #{linkAccessStats.uip}, #{linkAccessStats.hour}," +
            "#{linkAccessStats.weekday}, NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE pv = pv + 1," +
            "uv = uv + #{linkAccessStats.uv}," +
            "uip = uip + #{linkAccessStats.uip};")
    void insertShortLinkStats(@Param("linkAccessStats") LinkAccessStatsDO linkAccessStatsDO);

    /**
     * 根据短链接获取指定日期内基础监控数据
     */
    @Select("SELECT DATE_FORMAT(date, '%Y-%m-%d') AS date, SUM(pv) AS pv, SUM(uv) AS uv, SUM(uip) AS uip " +
            "FROM t_link_access_stats " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} " +
            "AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY full_short_url, gid, date;")
    List<LinkAccessStatsDO> listStatsByShortLink(@Param("param") ShortLinkStatsReqDTO shortLinkStatsReqDTO);

    /**
     * 根据短链接获取指定日期内小时基础监控数据
     */
    @Select("SELECT hour, SUM(pv) AS pv " +
            "FROM t_link_access_stats " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} " +
            "AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY full_short_url, gid, hour;")
    List<LinkAccessStatsDO> listHourStatsByShortLink(@Param("param") ShortLinkStatsReqDTO shortLinkStatsReqDTO);

    /**
     * 根据短链接获取指定日期内每天基础监控数据
     */
    @Select("SELECT weekday, SUM(pv) AS pv " +
            "FROM t_link_access_stats " +
            "WHERE full_short_url = #{param.fullShortUrl} AND gid = #{param.gid} " +
            "AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY full_short_url, gid, weekday;")
    List<LinkAccessStatsDO> listWeekdayStatsByShortLink(@Param("param") ShortLinkStatsReqDTO shortLinkStatsReqDTO);
}
