package com.github.code.zxs.resource.service.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.exception.UserEventException;
import com.github.code.zxs.core.model.entity.BaseEntity;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.config.FavConfig;
import com.github.code.zxs.resource.converter.FavConverter;
import com.github.code.zxs.resource.mapper.FavoritesMapper;
import com.github.code.zxs.resource.model.bo.FavDetailBO;
import com.github.code.zxs.resource.model.bo.FavStateBO;
import com.github.code.zxs.resource.model.dto.FavAddDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Favorites;
import com.github.code.zxs.resource.service.biz.base.CollectService;
import com.github.code.zxs.resource.service.biz.base.FavoritesService;
import com.github.code.zxs.resource.service.biz.base.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements FavoritesService {
    @Autowired
    private FavoritesServiceImpl favoritesService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private FavConverter favConverter;
    @Autowired
    private FavConfig favConfig;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public List<FavStateBO> listFavWithState(Long userId, ResourceDTO resourceDTO) {
        Set<Long> selected = collectService.userSelectedFavIds(userId, resourceDTO);
        List<FavStateBO> favStateBOs = favoritesService.listFav(userId, resourceDTO.getType())
                .stream()
                .map(favConverter::entity2StateBo)
                .collect(Collectors.toList());
        favStateBOs.forEach(e -> e.setState(selected.contains(e.getId())));
        return favStateBOs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavDetailBO createFav(FavAddDTO favAddDTO) {
        Long userId = UserContext.getId();
        Long favCount = baseMapper.selectCount(new LambdaQueryWrapper<Favorites>().eq(BaseEntity::getCreateBy, userId));
        if (favCount >= favConfig.getSingleUserMaxFavCount())
            throw new UserEventException("收藏夹数量达到上限", userId);
        Favorites favorites = favConverter.AddDto2Entity(favAddDTO);
        baseMapper.insert(favorites);
        FavDetailBO favDetailBO = favConverter.entity2DetailBo(favorites);
        favDetailBO.setAuthor(userInfoService.getAuthorBoById(favorites.getCreateBy()));
        return favDetailBO;
    }

    @Override
    public List<FavDetailBO> listMyFav() {
        Long userId = UserContext.getId();
        List<Favorites> favorites = baseMapper.selectList(new LambdaQueryWrapper<Favorites>()
                .eq(Favorites::getCreateBy, userId)
        );
        return favorites.stream().map(favConverter::entity2DetailBo).collect(Collectors.toList());
    }

    public List<Favorites> listFav(Long userId, ResourceTypeEnum type) {
        if (type == ResourceTypeEnum.POSTS)
            return baseMapper.selectList(new LambdaQueryWrapper<Favorites>()
                    .select(Favorites::getId,
                            Favorites::getName,
                            Favorites::getTotal,
                            Favorites::getMaxTotal)
                    .eq(BaseEntity::getCreateBy, userId));
        return null;
    }
}
