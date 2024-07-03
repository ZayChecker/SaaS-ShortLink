package com.nageoffer.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nageoffer.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.nageoffer.shortlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.nageoffer.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.nageoffer.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/recycle-bin")
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    //添加短链接到回收站
    @PostMapping("/save")
    public void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO recycleBinSaveReqDTO){
        recycleBinService.saveRecycleBin(recycleBinSaveReqDTO);
    }

    //分页查询当前用户的分组下的短链接
    @GetMapping("/page")
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO shortLinkRecycleBinPageReqDTO){
        return recycleBinService.pageShortLink(shortLinkRecycleBinPageReqDTO);
    }

    //恢复短链接
    @PostMapping("/recover")
    public void recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO recycleBinRecoverReqDTO){
        recycleBinService.recoverRecycleBin(recycleBinRecoverReqDTO);
    }

    //彻底删除短链接
    @DeleteMapping("/remove")
    public void removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO recycleBinRemoveReqDTO){
        recycleBinService.removeRecycleBin(recycleBinRemoveReqDTO);
    }
}
