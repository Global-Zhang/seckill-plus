package org.example.seckillPlus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.seckillPlus.pojo.Order;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LaoZhang
 * @since 2021-12-11
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
