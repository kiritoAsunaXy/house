package com.xuyu.house_biz.service;



import com.xuyu.house_common.model.City;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import java.util.List;


@Service
public class CityService {
  
  public List<City> getAllCitys(){
    City city = new City();
    city.setCityCode("110000");
    city.setCityName("北京");
    city.setId(1);
    return Lists.newArrayList(city);
  }

}
