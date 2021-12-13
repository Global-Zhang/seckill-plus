package org.example.seckillPlus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckillPlus.pojo.Order;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.vo.GoodsVo;
import org.example.seckillPlus.vo.OrderDetailVo;

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

        /**
         * 订单详情
         * @param orderId
         * @return
         */
        OrderDetailVo detail(Long orderId);

        /*
        * 生成秒杀地址
        * */
    String createPath(User user, Long goodsId);
    /**
     * 验证秒杀地址
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
