package com.pn.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExportDTO {
    //当前页码
    private Integer pageNum;
    //每页显示的行数
    private Integer pageSize;
    //角色代码
    private String roleCode;
    //角色名称
    private String roleName;
    //角色状态
    private String roleState;
    //总行数
    private Integer totalNum;
}
