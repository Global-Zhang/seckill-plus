package org.example.seckillPlus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckillPlus.mapper.SeckillGoodsMapper;
import org.example.seckillPlus.pojo.SeckillGoods;
import org.example.seckillPlus.service.ISeckillGoodsService;
import org.springframework.stereotype.Service;

@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService  {
}
