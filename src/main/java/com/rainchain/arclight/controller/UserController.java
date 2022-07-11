package com.rainchain.arclight.controller;

import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.SearchCondition;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.service.UserService;
import com.rainchain.arclight.utils.TimeUtils;
import com.rainchain.arclight.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public List<Game> searchKpGames(@Validated SearchCondition searchCondition) {
        String qq = searchCondition.getKp_qq();
        int maxnum = searchCondition.getMaxnum();

        //模糊匹配
        searchCondition.setTitle("%" + searchCondition.getTitle() + "%");
        VerifyUtils.qqVerify(qq);
        if (searchCondition.getData_now().compareTo(searchCondition.getStart_time()) > 0) {
            throw new OperationFailException("指定日期不能早于当日");
        }

        //将团持续时间统一成小时制
        String lastTime = searchCondition.getLast_time();
        searchCondition.setLast_timeh(TimeUtils.convertToTimeH(lastTime));

        if (searchCondition.getId() != -1) {
            return userService.searchIdGame(searchCondition.getId());
        } else if (!searchCondition.getKp_qq().equals("00000")) {
            return userService.searchKpGames(qq, maxnum);
        } else {
            return searchCondition.isAll() ? userService.searchAllGames(maxnum) : userService.searchGames(searchCondition);
        }
    }

}
