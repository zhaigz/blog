package com.zgz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: BlogQuery
 * @Description: TODO
 * @Author: Zgz
 * @Date: 2021/9/1 19:55
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;
}
