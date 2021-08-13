package com.atguigu.snowflake.controlller;

import com.atguigu.snowflake.service.SnowFlakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： chenr
 * @date： Created on 2021/8/13 15:18
 * @version： v1.0
 * @modified By:
 */
@RestController
@RequestMapping("/snow")
public class SnowFlakeController {

  @Autowired
    private SnowFlakeService snowFlakeService;
  @GetMapping("/getSnowFlakeId")
  public String getSnowFlakeId(){
      return  snowFlakeService.getIdBySnowFlake();
  }


}
