package com.nageoffer.shortlink.api.client;

import com.nageoffer.shortlink.api.dto.ShortLinkGroupCountQueryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("shortlink-service")
public interface ShortLinkClient {

    //查询短链接分组内数量
    @GetMapping("/api/short-link/v1/count")
    public List<ShortLinkGroupCountQueryDTO> listGroupShortLinkCount(@RequestParam("gids")List<String> gids);
}
