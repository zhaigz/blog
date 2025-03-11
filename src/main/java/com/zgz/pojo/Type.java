package com.zgz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Type
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/8/31 14:22
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_type")
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    //被维护blog与type之间的关系，通过blog里面的type建立关联，多对一
    @OneToMany(mappedBy = "type")
    private List<Blog> blogs = new ArrayList<Blog>();

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
