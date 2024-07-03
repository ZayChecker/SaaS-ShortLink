package com.nageoffer.shortlink.admin.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkGroupSaveReqDTO {

    /**
     * 分组名称
     */
    private String name;

}
