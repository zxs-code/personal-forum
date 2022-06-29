package com.github.code.zxs.resource.service.biz.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.code.zxs.resource.model.bo.FavDetailBO;
import com.github.code.zxs.resource.model.bo.FavStateBO;
import com.github.code.zxs.resource.model.dto.FavAddDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.model.entity.Favorites;

import java.util.List;

public interface FavoritesService extends IService<Favorites> {

    List<FavStateBO> listFavWithState(Long userId, ResourceDTO resourceDTO);

    FavDetailBO createFav(FavAddDTO favAddDTO);

    List<FavDetailBO> listMyFav();
}
