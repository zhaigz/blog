package com.zgz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: Blog
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 14:11
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//JPA实体类
@Table(name = "t_blog")
//映射表名，用来标识实体类与数据表的对应关系，默认和类名一致，name指定。
public class Blog {
    @Id
    @GeneratedValue
    //@Id - 主键
    // @GeneratedValue(strategy=GenerationType.IDENTITY) 生成策略- 自动递增生成
    private Long id;
    private String title;
    //大字段类型 内容较多时用 第一次初始化时有效  FetchType.LAZY懒加载 当变量使用的时候才加载 longtext比较大耗资源
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean comment;
    private boolean published;
    private boolean recommend;
    @Temporal(TemporalType.TIMESTAMP)
    //时间格式，在页面端取值：2016-09-28 15:52:32:000
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    //多对一 维护关系 多的一方为维护关系，少的一方为被维护关系
    @ManyToOne
    private Type type;

    //级联新增，在新建blog时，标签也被保存在数据库中
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments =new ArrayList<>();

    //tagIds用于存选择的id 不用存数据库
    //@Transient 不入库 不进行数据库的操作 表明其是一个正常属性值，不和数据库一一映射
    @Transient
    private String tagIds;

    private String description;

    //初始化标签显示在新增博客
    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }
    //新增博客时，对标签初始化，将数组里的数据转化成字符串
    private String tagsToIds(List<Tag> tags){
        if(!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags){
                if(flag){
                    ids.append(",");
                }else{
                    flag=true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }else {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", comment=" + comment +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
