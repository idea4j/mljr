package com.mljr.biz;

import com.mljr.domain.User;
import com.mljr.facade.SpikeFacade;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SpikeBiz implements SpikeFacade {

    @Override
    @Cacheable(value = { "s:spike" }, key = "#root.method.name.concat('-').concat(#root.args[0].toString())")
    public User querySpikeList(Integer id) {
        return new User("小明", 10);
    }
}
