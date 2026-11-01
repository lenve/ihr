package com.ihr.module.system.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单树节点（含下级）。
 */
public record MenuTreeVO(
        Long id,
        Long parentId,
        String menuName,
        Integer menuType,
        String path,
        String icon,
        Integer sortNo,
        List<MenuTreeVO> children
) {
    public MenuTreeVO withChildren() {
        return new MenuTreeVO(id, parentId, menuName, menuType, path, icon, sortNo, new ArrayList<>());
    }
}
