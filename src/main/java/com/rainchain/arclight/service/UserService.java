package com.rainchain.arclight.service;

import com.rainchain.arclight.entity.Game;
import com.rainchain.arclight.entity.SearchCondition;
import com.rainchain.arclight.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<Game> searchIdGame(int id) {
        return userMapper.searchIdGame(id);
    }

    public List<Game> searchKpGames(String qq, int maxnum) {
        return userMapper.searchKpGames(qq, maxnum);
    }

    public List<Game> searchAllGames(int maxnum) {
        return userMapper.searchAllGames(maxnum);
    }

    public List<Game> searchGames(SearchCondition searchCondition) {
        return userMapper.searchGames(searchCondition);
    }
}
