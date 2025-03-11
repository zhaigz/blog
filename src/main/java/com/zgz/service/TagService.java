package com.zgz.service;

import com.zgz.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    //初始化标签
    List<Tag> listTag();

    //获取填写的多个id值
    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);

    Tag updateTag(Long id,Tag tag);

    void deleteTag(Long id);

}
