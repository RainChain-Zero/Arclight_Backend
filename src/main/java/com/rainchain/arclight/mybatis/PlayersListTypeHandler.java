package com.rainchain.arclight.mybatis;


import com.alibaba.fastjson.TypeReference;
import com.rainchain.arclight.component.Player;

import java.util.List;

public class PlayersListTypeHandler extends ListTypeHandler<Player> {
    @Override
    protected TypeReference<List<Player>> specificType() {
        return new TypeReference<List<Player>>() {
        };
    }
}
