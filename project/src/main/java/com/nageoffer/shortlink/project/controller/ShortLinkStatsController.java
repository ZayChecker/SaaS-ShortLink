package com.nageoffer.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nageoffer.shortlink.project.common.convention.result.Result;
import com.nageoffer.shortlink.project.common.convention.result.Results;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkStatsRespDTO;
import com.nageoffer.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {

    private final ShortLinkStatsService shortLinkStatsService;

    //访问单个短链接指定时间内监控数据
    @GetMapping("/api/short-link/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO shortLinkStatsReqDTO){
        return Results.success(shortLinkStatsService.oneShortLinkStats(shortLinkStatsReqDTO));
    }

    //获取单个短链接指定时间内的新旧访客
    @GetMapping("/api/short-link/v1/stats/access-record")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO shortLinkStatsAccessRecordReqDTO){
        return Results.success(shortLinkStatsService.shortLinkStatsAccessRecord(shortLinkStatsAccessRecordReqDTO));
    }
}
