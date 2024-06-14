package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper categoryMapper;

    public List<CourseCategoryTreeDto> queryTreeNodes(String id){
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = categoryMapper.selectTreeNode(id);
        //list转map,排除根节点
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream()
                .filter(item -> !id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));

        ArrayList<CourseCategoryTreeDto> courseCategoryDtos = new ArrayList<>();

        //遍历，排除根节点
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId()))
                .forEach(item->{
                    //如果当前节点是根节点的子节点
                    if(item.getParentid().equals(id)){
                        courseCategoryDtos.add(item);
                    }

                    //得到当前节点的父节点信息
                    CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());

                    if(courseCategoryTreeDto!=null) {//排空
                        if (courseCategoryTreeDto.getChildrenTreeNodes() == null) {
                            courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                        }
                        //添加节点信息
                        courseCategoryTreeDto.getChildrenTreeNodes().add(item);
                    }

                });
        return courseCategoryDtos;
    }

}
