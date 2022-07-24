package com.rainchain.arclight.controller;

import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.SearchCondition;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.TimeUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public List<Game> searchGames(@Validated SearchCondition searchCondition) {
        String qq = searchCondition.getKp_qq();

        //模糊匹配
        if (searchCondition.getTitle() != null) {
            searchCondition.setTitle("%" + searchCondition.getTitle() + "%");
        }
        //校验qq号
        if (qq != null) {
            VerifyUtils.qqVerify(qq);
        }
        //校验开团时间
        String start_time = searchCondition.getStart_time();
        if (start_time != null && searchCondition.getData_now().compareTo(start_time) > 0) {
            throw new OperationFailException("指定日期不能早于当日");
        }

        //将团持续时间统一成小时制
        String lastTime = searchCondition.getLast_time();
        if (searchCondition.getLast_time() != null) {
            searchCondition.setLast_timeh(TimeUtils.convertToTimeH(lastTime));
        }

        return userService.searchGames(searchCondition);
    }

}
