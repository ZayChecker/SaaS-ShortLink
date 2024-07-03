package com.nageoffer.shortlink.api.dto;

import lombok.Data;

/**
 * 短链接分组查询返回参数
 */
@Data
public class ShortLinkGroupCountQueryDTO {

    private String gid;

    private Integer shortLinkCount;
}
