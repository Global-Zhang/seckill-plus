package org.example.seckillPlus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckillPlus.pojo.Order;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LaoZhang
 * @since 2021-12-11
 */
public interface IOrderService extends IService<Order> {
        /**
         * 秒杀
         * @param user
         * @param goods
         * @return
         */
        Order seckill(User user, GoodsVo goods);
}
