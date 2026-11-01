package com.ihr.module.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ihr.module.system.entity.SysMenu;
import com.ihr.module.system.mapper.SysMenuMapper;
import com.ihr.module.system.vo.MenuTreeVO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现：从 sys_menu 表构建菜单树。
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    public SysMenuServiceImpl(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<MenuTreeVO> listMenuTree() {
        List<SysMenu> menus = sysMenuMapper.selectList(
                Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getStatus, 1));
        List<MenuTreeVO> nodes = menus.stream()
                .sorted(Comparator.comparingInt(m -> m.getSortNo() == null ? 0 : m.getSortNo()))
                .map(m -> new MenuTreeVO(m.getId(), m.getParentId(), m.getMenuName(),
                        m.getMenuType(), m.getPath(), m.getIcon(), m.getSortNo(), List.of()))
                .collect(Collectors.toList());
        Map<Long, List<MenuTreeVO>> byParent = nodes.stream()
                .collect(Collectors.groupingBy(MenuTreeVO::parentId));
        return nodes.stream()
                .filter(n -> n.parentId() == 0L)
                .map(n -> buildTree(n, byParent))
                .collect(Collectors.toList());
    }

    private MenuTreeVO buildTree(MenuTreeVO node, Map<Long, List<MenuTreeVO>> byParent) {
        List<MenuTreeVO> children = byParent.getOrDefault(node.id(), List.of()).stream()
                .map(child -> buildTree(child, byParent))
                .collect(Collectors.toList());
        return new MenuTreeVO(node.id(), node.parentId(), node.menuName(), node.menuType(),
                node.path(), node.icon(), node.sortNo(), children);
    }
}
