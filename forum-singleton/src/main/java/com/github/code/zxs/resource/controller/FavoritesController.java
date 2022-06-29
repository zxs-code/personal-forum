package com.github.code.zxs.resource.controller;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.model.enums.ResourceTypeEnum;
import com.github.code.zxs.resource.model.bo.FavDetailBO;
import com.github.code.zxs.resource.model.bo.FavStateBO;
import com.github.code.zxs.resource.model.dto.FavAddDTO;
import com.github.code.zxs.resource.model.dto.ResourceDTO;
import com.github.code.zxs.resource.service.biz.base.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("fav")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;

    @GetMapping("state/list")
    public List<FavStateBO> getFavWithState(@RequestParam Integer type, @RequestParam Long id) {
        Long userId = UserContext.getId();
        return favoritesService.listFavWithState(userId, new ResourceDTO(id, ResourceTypeEnum.valueOf(type)));
    }

    @PostMapping
    public FavDetailBO createFav(@RequestBody @Valid FavAddDTO favAddDTO){
            return favoritesService.createFav(favAddDTO);
    }

    @GetMapping("owner")
    public List<FavDetailBO> listMyFav(){
        return favoritesService.listMyFav();
    }
}
