package com.zgz.dao;

import com.zgz.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {

    //b.recommend = true代表是推荐的博客
    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    //首页全局搜索 1指定传递的参数

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    //查询年份
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by function('date_format',b.updateTime,'%Y') desc")
    List<String> findGroupYear();

    //根据年份查询博客
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 order by function('date_format',b.updateTime,'%d %b') desc")
    List<Blog> findByYear(String year);

    //更新博客详情浏览次数
    @Transactional
    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long id);
}
