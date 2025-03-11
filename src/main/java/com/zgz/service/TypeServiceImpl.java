package com.zgz.service;

import com.zgz.NotFoundException;
import com.zgz.dao.TypeRepository;
import com.zgz.pojo.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: TypeServiceImpl
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 19:34
 * @Version: 1.0
 **/
@Service
public class TypeServiceImpl implements TypeService{
    @Autowired
    private TypeRepository typeRepository;

    @Transactional//事务处理
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getTypeById(Long id) {
        return typeRepository.findById(id).get();
    }

    //自定义
    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    //拿到类型的大小，由高到低排序
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort =Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable =PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if(t == null){
            throw new NotFoundException("不存在该类型！");
        }
        //将type里的值复制到t中
        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
