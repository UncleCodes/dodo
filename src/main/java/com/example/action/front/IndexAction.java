package com.example.action.front;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dodo.common.framework.service.HqlHelperService;
import com.example.enums.DemoEnum;
import com.example.enums.PersonKind;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Controller
public class IndexAction {

    @Autowired
    private HqlHelperService helperService;

    // 一个例子，查询省份名称包含'天'的省份，并且返回名字 和 id
    //    @RequestMapping({ "/mydata.json" })
    //    @ResponseBody
    //    public List<Map<String, Object>> mydata() {
    //        HqlHelper helper = HqlHelper.queryFrom(Province.class);
    //        helper.fetch(ProvinceFields.name).fetch(ProvinceFields.id).like(ProvinceFields.name, "天");
    //        return helperService.getRecords(helper, Boolean.FALSE).getRawData();
    //    }

    @RequestMapping({ "/index.htm", "/index.dhtml" })
    public String index() {
        return "test_index";
    }

    @RequestMapping({ "/test.json", "/test.dhtml" })
    @ResponseBody
    public Map<String, Object> test() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("VALUE1", DemoEnum.VALUE1);
        map.put("VALUE2", DemoEnum.VALUE2);
        map.put("DiDi", PersonKind.DiDi);
        return map;
    }
}
