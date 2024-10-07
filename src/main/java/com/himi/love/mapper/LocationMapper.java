package com.himi.love.mapper;

import com.himi.love.model.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.math.BigDecimal;

@Mapper
public interface LocationMapper {
    Location findById(Integer locationId);
    List<Location> findAll();
    int insert(Location location);
    int update(Location location);
    int deleteById(Integer locationId);
    
    // 新增方法用于搜索附近位置
    List<Location> findNearbyLocations(@Param("latitude") double latitude,
                                       @Param("longitude") double longitude,
                                       @Param("radiusInKm") double radiusInKm);
    
    Location findByLatLongAndName(@Param("latitude") BigDecimal latitude, 
                                  @Param("longitude") BigDecimal longitude, 
                                  @Param("locationName") String locationName);
}