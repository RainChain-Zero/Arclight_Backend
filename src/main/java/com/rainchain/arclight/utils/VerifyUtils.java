package com.rainchain.arclight.utils;


import cn.hutool.core.collection.CollUtil;
import com.rainchain.arclight.component.JoinOrQuitInfo;
import com.rainchain.arclight.component.Player;
import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.InviteOrRemoveInfo;
import com.rainchain.arclight.exception.OperationFailException;

import java.util.List;
import java.util.regex.Pattern;


//todo 带列表参数的最大长度限制检验
public class VerifyUtils {
    //校验是否全为数字
    private static final java.util.regex.Pattern NUMBER_PATTERN = java.util.regex.Pattern.compile("-?\\d+(\\.\\d+)?");

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

    public static void groupVerify(String group) {
        if (!NUMBER_PATTERN.matcher(group).matches() || group.length() < 6 || group.length() > 12) {
            throw new OperationFailException("QQ群号不合法！");
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
        if (CollUtil.isEmpty(ids)) {
            throw new OperationFailException("至少需要提供一个id");
        }
        if (null == player) {
            throw new OperationFailException("缺少玩家信息");
        }
        VerifyUtils.qqVerify(player.getQq());
    }

    public static void verifyInviteOrRemoveInfo(InviteOrRemoveInfo inviteOrRemoveInfo) {
        if (null == inviteOrRemoveInfo.getId()) {
            throw new OperationFailException("缺少必要的id参数");
        }
        VerifyUtils.qqVerify(inviteOrRemoveInfo.getKp_qq());
        List<Player> players = inviteOrRemoveInfo.getPlayers();
        if (!CollUtil.isEmpty(players)) {
            players.forEach(player -> {
                VerifyUtils.qqVerify(player.getQq());
            });
        } else {
            throw new OperationFailException("缺少玩家信息");
        }
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
        if (!CollUtil.isEmpty(game.getPlayers())) {
            throw new OperationFailException("不可以在创建团阶段对players参数赋值！");
        }

        List<String> groups = game.getGroups();
        if (!CollUtil.isEmpty(groups)) {
            CollUtil.distinct(groups);
            groups.forEach(VerifyUtils::groupVerify);
        }
        return game;
    }
}
