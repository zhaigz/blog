package com.zgz.service;
import com.zgz.NotFoundException;
import com.zgz.dao.BlogRepository;
import com.zgz.pojo.Blog;
import com.zgz.pojo.Type;
import com.zgz.utils.MarkdownUtils;
import com.zgz.utils.MyBeanUtils;
import com.zgz.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @ClassName: BlogServiceImpl
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/1 17:46
 * @Version: 1.0
 **/
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).get();
    }

    //博客详情获取博客，Markdown转HTML
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();
        if(blog == null){
            throw new NotFoundException("该博客不存在");
        }
        //防止修改数据库中的字段，将转换后的存在新的blog中
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        //浏览次数累加
        blogRepository.updateViews(id);

        return b;
    }

    //后台博客查询，根据多条件查询
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            //Root指明要查询的对象，对应表中的字段
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle() !=null) {
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId() !=null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                //list转数组
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    //Jpa的CriteriaQuery查询
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }


    //首页全局搜索
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    //前端展示推荐博客
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort =Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    //博客归档
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        //查询年份
        List<String> years=blogRepository.findGroupYear();
        //这里不要用HashMap：无序 LinkedHashMap：有序
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years){
            System.out.println(year);
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    //归档页面：返回博客数目
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //新增判断条件：无Id
        if(blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            //初始化浏览次数
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if(b==null){
            throw new NotFoundException("不存在该博客！");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
