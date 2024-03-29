package com.github.code.zxs.resource.consumer;

import com.github.code.zxs.resource.service.biz.base.TagService;
import com.github.code.zxs.resource.service.manager.PostsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagConsumer {
    @Autowired
    private TagService tagService;
    @Autowired
    private PostsManager postsManager;

//    @KafkaListener(topics = TopicConstant.USER_VIEW, groupId = "tag", containerFactory = "batchFactory")
//    public void saveCache(List<UserEventDTO> userEventDTOS, Consumer consumer) {
//        List<Long> collect = userEventDTOS.stream()
//                .filter(e -> e.getResourceDTO().getType() == ResourceTypeEnum.POSTS)
//                .map(UserEventDTO::getResourceDTO)
//                .map(ResourceDTO::getId)
//                .collect(Collectors.toList());
//        List<Object> objects = postsManager.listObjs(new LambdaQueryWrapper<Posts>()
//                .select(Posts::getTags)
//                .in(Posts::getId, collect)
//        );
//        ArrayList<String> tagStr = new ArrayList<>();
//
//        for (Object object : objects) {
//            List<String> strings = JsonUtils.parseList((String) object, String.class);
//            tagStr.addAll(strings);
//        }
//        tagService.addTagScore(tagStr);
//    }
}
