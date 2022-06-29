package com.github.code.zxs.resource.converter;

import com.github.code.zxs.core.util.BeanUtils;
import com.github.code.zxs.resource.config.FavConfig;
import com.github.code.zxs.resource.model.bo.FavDetailBO;
import com.github.code.zxs.resource.model.bo.FavStateBO;
import com.github.code.zxs.resource.model.dto.FavAddDTO;
import com.github.code.zxs.resource.model.entity.Favorites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavConverter {
    @Autowired
    private FavConfig favConfig;

    public FavStateBO entity2StateBo(Favorites fav) {
        return BeanUtils.copy(fav, new FavStateBO());
    }

    public FavDetailBO entity2DetailBo(Favorites fav) {
        return BeanUtils.copy(fav, new FavDetailBO());
    }

    public Favorites AddDto2Entity(FavAddDTO favAddDTO) {
        Favorites favorites = BeanUtils.copy(favAddDTO, new Favorites());
        favorites.setTotal(0);
        favorites.setMaxTotal(favConfig.getMaxItemInSingleFav());
        return favorites;
    }
}
