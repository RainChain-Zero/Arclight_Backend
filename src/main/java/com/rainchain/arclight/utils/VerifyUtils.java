package com.rainchain.arclight.utils;


import com.rainchain.arclight.component.JoinOrQuitInfo;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.exception.OperationFailException;

import java.util.List;
import java.util.regex.Pattern;


public class VerifyUtils {
    public static void passwordVerify(String password) {
        boolean digitFlag = false, letterFlag = false;

        //密码合法性判断(英文+数字+至少8位）
        for (int i = 0; i < password.length(); ++i) {
            if (Character.isDigit(password.charAt(i))) {
                digitFlag = true;
            } else if (Character.isLetter(password.charAt(i))) {
                letterFlag = true;
            }
            if (digitFlag && letterFlag) {
                break;
            }
        }
        if (!digitFlag || !letterFlag) {
            throw new OperationFailException("密码至少同时包含1位数字和1位英文字符");
        }
    }

    public static void qqVerify(String qq) {
        if (!Pattern.compile("\\d*").matcher(qq).matches() || qq.length() < 5 || qq.length() > 10) {
            throw new OperationFailException("不合法的QQ号！");
        }
    }

    public static String decodeQq(String key) {
        String keyDecrypted = DesUtils.decryptBasedDes(Base64Utils.convertToPlain(key));
        return keyDecrypted.substring(0, keyDecrypted.indexOf("arclight"));
    }

    //校验JoinOrQuitInfo
    public static void verifyJoinOrQuitInfo(JoinOrQuitInfo joinOrQuitInfo) {
        List<Long> ids = joinOrQuitInfo.getId();
        Player player = joinOrQuitInfo.getPlayer();
        if (null == ids || ids.size() == 0) {
            throw new OperationFailException("至少需要提供一个id");
        }
        if (null == player) {
            throw new OperationFailException("缺少玩家信息");
        }
        VerifyUtils.qqVerify(player.getQq());
    }

    //校验game参数
    public static Game verifyGame(Game game) {
        if (game.getId() != null) {
            throw new OperationFailException("不可以给id参数赋值！");
        }
        if (game.getTitle().equals("") || game.getTitle() == null) {
            throw new OperationFailException("团名不能为空！");
        }
        if (game.getKp_name().equals("") || game.getKp_name() == null) {
            throw new OperationFailException("主持人昵称不能为空！");
        }
        if (game.getKp_qq().length() < 5 || game.getKp_qq().length() > 10) {
            throw new OperationFailException("QQ号非法！");
        }
        if (!Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(game.getStart_time()).find()) {
            throw new OperationFailException("开团日期格式错误！");
        }
        if (game.getLast_timeh() != null) {
            throw new OperationFailException("不可以对last_timeh参数赋值");
        }
        if (!Pattern.compile("\\d+[hd]").matcher(game.getLast_time()).find()) {
            throw new OperationFailException("开团持续时间格式错误！");
        }
        if (game.getMinper() < 1 || game.getMinper() > 30 || game.getMaxper() < 1 || game.getMaxper() > 30) {
            throw new OperationFailException("团人数超出范围！");
        }
        if (game.getTags() == null) {
            throw new OperationFailException("团标签不能为空！");
        }
        if (game.getDes().equals("") || game.getDes() == null) {
            throw new OperationFailException("团描述不能为空！");
        }
        if (game.getRestricted() != null) {
            throw new OperationFailException("不可以对参数restricted赋值！");
        }

        return Game.getRestricted(game);
    }
}
