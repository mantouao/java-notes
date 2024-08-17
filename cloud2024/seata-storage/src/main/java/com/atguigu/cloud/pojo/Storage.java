package com.atguigu.cloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Repository
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Storage {
    private Long id;
    private Long productId;
    private Integer total; // 总库存
    private Integer used;
    private Integer residue;
}
