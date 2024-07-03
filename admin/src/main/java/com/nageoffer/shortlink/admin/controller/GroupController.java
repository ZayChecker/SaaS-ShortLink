package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.nageoffer.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/group")
public class GroupController {

    private final GroupService groupService;

    //新增短链接分组
    @PostMapping
    public void saveGroup(@RequestBody ShortLinkGroupSaveReqDTO shortLinkGroupSaveReqDTO){
        groupService.saveGroup(shortLinkGroupSaveReqDTO);
    }

    //查询用户短链接分组集合
    @GetMapping
    public Result<List<ShortLinkGroupRespDTO>> listGroup(){
        return Results.success(groupService.listGroup());
    }

    //修改短链接分组名称
    @PutMapping
    public void updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO shortLinkGroupUpdateReqDTO){
        groupService.updateGroup(shortLinkGroupUpdateReqDTO);
    }

    //删除短链接分组
    @DeleteMapping
    public void deleteGroup(@RequestParam("gid") String gid){
        groupService.deleteGroup(gid);
    }

    //对短链接分组进行排序
    @PutMapping("/sort")
    public void sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> shortLinkGroupSortReqDTOS){
        groupService.sortGroup(shortLinkGroupSortReqDTOS);
    }

}
/**
 * 短链接跳转：状态码：
 * 301：永久性转移，只会访问一次短链接服务获取目标地址
 * 302：临时性转移，每次都要来访问短链接服务获取目标地址
 *
 * 域名下唯一：
 * a.com/abcdef
 * b.com/abcdef(完整短链接，包括域名)
 *
 * 因为一个用户可以有多个短链接分组
 * 所以t_group也要分表
 */
